/**
 * IMPORTANT: Make sure you are using the correct package name. 
 * This example uses the package name:
 * package com.example.android.justjava
 * If you get an error when copying this code into Android studio, update it to match the
 * package name found in the project's AndroidManifest.xml file.
 **/

package com.example.android.justjava;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Locale;

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

        // sets the current radio to be default
        Locale currentLocale = getCurrentLocale(this);
        setDefaultRadio(currentLocale.getLanguage());
    }

    /**
     * This method sets the default radio
     * @param localeName is the current locale language abbreviation
     */
    private void setDefaultRadio(String localeName) {
        switch (localeName) {
            case "zh":
                RadioButton zhRadio = (RadioButton) findViewById(R.id.radio_chinese);
                zhRadio.setChecked(true);
                break;
            default:
                RadioButton enRadio = (RadioButton) findViewById(R.id.radio_english);
                enRadio.setChecked(true);
                break;
        }
    }

    /**
     * This method changes the current language
     */
    public void onLanguagePick(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which language was clicked
        switch(view.getId()) {
            case R.id.radio_english:
                if (checked)
                    setCurrentLocale(this,"en");
                    break;
            case R.id.radio_chinese:
                if (checked)
                    setCurrentLocale(this,"zh");
                    break;
        }
    }

    /**
     * Get the current locale
     */
    private Locale getCurrentLocale(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return context.getResources().getConfiguration().getLocales().get(0);
        } else{
            //noinspection deprecation
            return context.getResources().getConfiguration().locale;
        }
    }

    /**
     * This method sets locale from the param
     * has not worked yet
     */
    private void setCurrentLocale(Context context, String language) {
        Locale languageToSet = new Locale(language);
        Resources resources = getBaseContext().getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            configuration.setLocale(languageToSet);
            context.createConfigurationContext(configuration);
        } else {
            //noinspection deprecation
            configuration.locale = languageToSet;
            resources.updateConfiguration(configuration,displayMetrics);
        }
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
        EditText nameView = (EditText) findViewById(R.id.name_view);
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);

        String name = nameView.getText().toString();
        boolean isWhippedCreamChecked = whippedCreamCheckBox.isChecked();
        boolean isChocolateChecked = chocolateCheckBox.isChecked();
        int price = calculatePrice(number, uniCost, isWhippedCreamChecked, isChocolateChecked);

        String result = createOrderSummary(name, price, isWhippedCreamChecked, isChocolateChecked);

        composeEmail(name, result);
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
     * @param isWhippedCreamChecked checked whipped cream topping
     * @param isChocolateChecked checked chocolate topping
     */
    private int calculatePrice(int quantity,
                               int uniCost,
                               boolean isWhippedCreamChecked,
                               boolean isChocolateChecked) {
        if (isWhippedCreamChecked) {
            uniCost += 1;
        }
        if (isChocolateChecked) {
            uniCost += 2;
        }
        return quantity * uniCost;
    }

    /**
     * Create order summary
     * @param name is the user's name
     * @param price is the total price
     * @param isWhippedCreamChecked checks if Whipped Cream is checked
     * @param isChocolateChecked checks if Chocolate is checked
     * @return the summary message
     */
    private String createOrderSummary(String name,
                                      int price,
                                      boolean isWhippedCreamChecked,
                                      boolean isChocolateChecked) {
        String summary = getString(R.string.order_summary_name, name);
        summary += "\n" + getString(R.string.whipped_cream_topping) + isWhippedCreamChecked;
        summary += "\n" + getString(R.string.chocolate_topping) + isChocolateChecked;
        summary += "\n" + getString(R.string.quantity) + ": " + number;
        summary += "\n" + getString(R.string.price) + price;
        summary += "\n" + getString(R.string.thank_you);
        return summary;
    }

    /**
     * Intent to email app
     * @param subject is email title
     * @param bodyText is email body text
     */
    public void composeEmail(String subject, String bodyText) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java for " + subject);
        intent.putExtra(Intent.EXTRA_TEXT, bodyText);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}