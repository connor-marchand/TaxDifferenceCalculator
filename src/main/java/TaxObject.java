import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxObject{

    private double ficaAmount;
    private double federalAmount;
    private double stateAmount;
    private double totalTax;
    private Logger logger;

    // Constructor

    public void buildObject(JSONObject response) {
        try{
        this.ficaAmount = response.getJSONObject("annual").getJSONObject("fica").getDouble("amount");
        this.federalAmount = response.getJSONObject("annual").getJSONObject("federal").getDouble("amount");
        this.stateAmount = response.getJSONObject("annual").getJSONObject("state").getDouble("amount");

        }catch(JSONException j){
            if(response.getJSONObject("annual").getJSONObject("state").isEmpty()){
                this.stateAmount = 0;
            }
        }

        calculateTotalTax();
    }

    private void calculateTotalTax() {
        this.totalTax = ficaAmount + federalAmount + stateAmount;
    }


    // Getters

    public double getFicaAmount() {
        return ficaAmount;
    }

    public double getFederalAmount() {
        return federalAmount;
    }

    public double getStateAmount() {
        return stateAmount;
    }

    public double getTotalTax() {
        return totalTax;
    }

    // Setters

    public void setFicaAmount(double ficaAmount) {
        this.ficaAmount = ficaAmount;
    }

    public void setFederalAmount(double federalAmount) {
        this.federalAmount = federalAmount;
    }

    public void setStateAmount(double stateAmount) {
        this.stateAmount = stateAmount;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public JSONObject getTaxes(double payRate, String filingStatus, String state){

        HttpResponse<JsonNode> response = null;
        JSONObject responseJSON = null;
        String body = "pay_rate=" + payRate + "&filing_status=" + filingStatus + "&state=" + state;

        try {
            response = Unirest.post("https://taxee.io/api/v2/calculate/2019")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJBUElfS0VZX01BTkFHRVIiLCJodHRwOi8vdGF4ZWUuaW8vdXNlcl9pZCI6IjVkNTU2Y2IwNDRmMzYwMWEyODMwYjI5MiIsImh0dHA6Ly90YXhlZS5pby9zY29wZXMiOlsiYXBpIl0sImlhdCI6MTU2NTg3OTQ3Mn0.ZjUzi_6AsCtaY65p9gUTTS_hw3joVMY9sidbQ3vBKIw")
                    .header("Accept", "*/*")
                    .body(body)
                    .asJson();
            responseJSON = response.getBody().getObject();
        } catch (UnirestException e) {
            e.printStackTrace();
        } catch(NullPointerException n){
            if(responseJSON.getJSONObject("annual").getJSONObject("fica").getString("amount") == null){
                this.ficaAmount = 0;
            }else if(responseJSON.getJSONObject("annual").getJSONObject("federal").getString("amount") == null){
                this.federalAmount = 0;
            }else if(responseJSON.getJSONObject("annual").getJSONObject("state").getString("amount") == null){
                this.stateAmount = 0;
            }
        }


        return responseJSON;
    }

}
