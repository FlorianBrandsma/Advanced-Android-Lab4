package advanced.android.stockmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    private ArrayList<String> stockList = new ArrayList<>();

    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] companies = new String[]{"AAPL", "GOOGL", "FB", "NOK"};

        for (final String company : companies)
        {
            String url = "https://financialmodelingprep.com/api/company/price/"+company+"?datatype=json";

            myListView = (ListView) findViewById(R.id.stock_list_view);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                CompanyData companyData = gson.fromJson(response.getJSONObject(company).toString(), CompanyData.class);
                                companyData.setName(company);

                                stockList.add(companyData.getName() + ": " + companyData.price + " USD");

                                setStockList();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    });

            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private void setStockList()
    {
        final ArrayAdapter<String> aa;
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stockList);

        myListView.setAdapter(aa);
    }
}
