/**
 * IMPORTANT: Make sure you are using the correct package name. 
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match the
 * package name found in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int number = 0;
    int uniCost = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method minus 1 to the current quantity.
     */
    public void decrement(View view) {
        number = number - 1;
        if (number <= 0) number = 0;
        display(number);
    }

    /**
     * This method adds 1 to the current quantity.
     */
    public void increment(View view) {
        number = number + 1;
        display(number);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        String result = createOrderSummary();
        displayMessage(result);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity is the number of cups of coffee ordered
     * @param uniCost is the price per cup of coffee
     */
    private int calculatePrice(int quantity, int uniCost) {
        return quantity * uniCost;
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView order_summary_text_view = (TextView) findViewById(R.id.order_summary_text_view);
        order_summary_text_view.setText(message);
    }

    /**
     * Create order summary
     * @return the summary message
     */
    private String createOrderSummary() {
        String summary = "Name: ShisaQ";
        summary += "\nQuantity: " + number;
        summary += "\nTotal: $" + calculatePrice(number, 10);
        summary += "\nThank you!";
        return summary;
    }
}