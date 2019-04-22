package advanced.android.stockmonitor;

public class CompanyData {

    private String name;
    public float price;

    public String getName() {
        return name;
    }

    public void setName(String name) {

        switch (name){

            case "AAPL":    this.name = "Apple";    break;
            case "GOOGL":   this.name = "Google";   break;
            case "FB":      this.name = "Facebook"; break;
            case "NOK":     this.name = "Nokia";    break;
            default:        this.name = "WRONG ID"; break;
        }
    }
}
