package advanced.android.stockmonitorv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    private ArrayList<String> stockList = new ArrayList<>();
    final ArrayList<Company> companyList = new ArrayList(Arrays.asList(
            new Company("Apple",    "AAPL") ,
            new Company("Google",   "GOOGL"),
            new Company("Facebook", "FB"),
            new Company("Nokia",    "NOK")));

    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button addButton = (Button) findViewById(R.id.add_button);

        getStocks();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = ((EditText) findViewById(R.id.stock_name)).getText().toString();
                String id = ((EditText) findViewById(R.id.stock_id)).getText().toString();

                companyList.add(new Company(name, id));
                getStocks();
            }
        });
    }

    private void getStocks(){

        stockList.clear();

        for (final Company company : companyList)
        {
            String url = "https://financialmodelingprep.com/api/company/price/"+company.id+"?datatype=json";

            myListView = (ListView) findViewById(R.id.stock_list_view);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                CompanyData companyData = gson.fromJson(response.getJSONObject(company.id).toString(), CompanyData.class);
                                companyData.name = company.name;

                                stockList.add(companyData.name + ": " + companyData.price + " USD");

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
