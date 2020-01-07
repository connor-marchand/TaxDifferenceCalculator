import org.json.JSONObject;

import java.util.Scanner;

import static java.lang.Math.abs;

public class main {

    public static void main(String[] args) {

        JSONObject response1 = null;
        JSONObject response2 = null;
        TaxObject taxObject1 = new TaxObject();
        TaxObject taxObject2 = new TaxObject();
        String state1 = "";
        String state2 = "";
        Double state1TotalTax = 0.0;
        Double state2TotalTax = 0.0;
        Scanner in = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.println("Origin State:");
            state1 = in.nextLine();

            System.out.println("Destination State:");
            state2 = in.nextLine();

            System.out.println("Enter Pay Rate:");
            double payRate = in.nextDouble();

            try {
                response1 = taxObject1.getTaxes(payRate, "single", state1.toUpperCase());
                response2 = taxObject2.getTaxes(payRate, "single", state2.toUpperCase());
            }catch(Exception e){
                continue;
            }


            taxObject1.buildObject(response1);
            taxObject2.buildObject(response2);
            state1TotalTax = taxObject1.getTotalTax();
            state2TotalTax = taxObject2.getTotalTax();

            run = false;
        }

        Double taxDifference = state2TotalTax - state1TotalTax;

        if(taxDifference > 0){
            System.out.println("If you move to " + state2 +" from "+ state1 + ", you will pay " + taxDifference + " more in taxes");
        }else if(taxDifference < 0){
            System.out.println("If you move to " + state2 +" from "+ state1 + ", you will pay " + abs(taxDifference) + " less in taxes");
        }
    }
}
