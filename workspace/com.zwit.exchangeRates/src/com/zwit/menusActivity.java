package com.zwit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zwit.menus.R;

public class menusActivity extends Activity {
	private EditText text;
	private TextView textView;
	private Float[] rates;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		text = (EditText) findViewById(R.id.editText1);
		textView = (TextView) findViewById(R.id.autoCompTextView1);

		// Checking if network is available
		if(isNetworkAvailable()){ 
			rates = getRates();		
			Toast.makeText(this, "Network is OK!", Toast.LENGTH_SHORT).show();			
		}
		//else 
			//Toast.makeText(this, "Network is not available:(", Toast.LENGTH_LONG).show();
		
			

	}

	/**
	 * @return the rates
	 */
	public Float[] getRates() {
		return ratesFromfxRate("http://fx-rate.net/PLN/");
	}

	/**
	 * @param rates the rates to set
	 */
	public void setRates(Float[] rates) {
		this.rates = rates;
	}

	// This method is called at button click because we assigned the name to the
	// "On Click property" of the button
	public void myClickHandler(View view) {
		switch (view.getId()) {
		case R.id.button1:
			RadioButton dollarButton = (RadioButton) findViewById(R.id.radioButton1);
			RadioButton euroButton = (RadioButton) findViewById(R.id.radioButton2);
			RadioButton poundButton = (RadioButton) findViewById(R.id.radioButton4);
			RadioButton frankButton = (RadioButton) findViewById(R.id.radioButton3);
			if (text.getText().length() == 0) {
				Toast.makeText(this, "Please enter a valid number",
						Toast.LENGTH_LONG).show();
				return;
			}

			float inputValue = Float.parseFloat(text.getText().toString());
			if (dollarButton.isChecked()) {
				text.setText(String
						.valueOf(convertToPLN(inputValue, rates[0])));
			}
			if (euroButton.isChecked()) {
				text.setText(String
						.valueOf(convertToPLN(inputValue, rates[1])));
			}
			if (poundButton.isChecked()) {
				text.setText(String
						.valueOf(convertToPLN(inputValue, rates[2])));
			}
			if(frankButton.isChecked()){
				text.setText(String
						.valueOf(convertToPLN(inputValue, rates[3])));
			}
			dollarButton.setChecked(false);
			euroButton.setChecked(false);
			poundButton.setChecked(false);
			frankButton.setChecked(false);
			break;
		}
	}

	// Converts to currency
	private float convertToPLN(Float currency, Float rate) {
		return ((currency/rate) );
	}
	
	private Float[] ratesFromfxRate(String urlName){
		Float[] rates = new Float[4];
		try {
			textView.setText("");
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI("http://fx-rate.net/PLN/"));
			HttpResponse response = client.execute(request);
			// Get the response O MASAKRA!!
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String str = "";
			while((str = rd.readLine())!= null){
				// USD to PLN
				if (str.contains("<a href=\"/USD/PLN/\" title=\"Dollar to Zloty\">")){
			 		// apply string cutting </a></div>
					int start = str.indexOf("Dollar to Zloty\">")+ new String("Dollar to Zloty\">").length();
					int end = str.indexOf("</a></div>", start);
					rates[0] = new Float (str.substring(start, end));
				}
				// EUR to PLN
				if (str.contains("<a href=\"/EUR/PLN/\" title=\"Euro to Zloty\">")){
			 		// apply string cutting </a></div>
					int start = str.indexOf("Euro to Zloty\">")+ new String("Euro to Zloty\">").length();
					int end = str.indexOf("</a></div>", start);
					rates[1] = new Float (str.substring(start, end));
				}
				
				// GBP to PLN
				if (str.contains("<a href=\"/GBP/PLN/\" title=\"Pound to Zloty\">")){
			 		// apply string cutting </a></div>
					int start = str.indexOf("Pound to Zloty\">")+ new String("Pound to Zloty\">").length();
					int end = str.indexOf("</a></div>", start);
					rates[2] = new Float (str.substring(start, end));
				}
				// CHF to PLN
				if (str.contains("<a href=\"/CHF/PLN/\" title=\"Franc to Zloty\">")){
			 		// apply string cutting </a></div>
					int start = str.indexOf("\"Franc to Zloty\">")+ new String("\"Franc to Zloty\">").length();
					int end = str.indexOf("</a></div>", start);
					rates[3] = new Float (str.substring(start, end));		
				}				
			}			
		}
		catch (Exception e){

			textView.setText("Can't connect");
		}
		
		return rates;
	}
	
	public boolean isNetworkAvailable() {
		    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo netInfo = cm.getActiveNetworkInfo();
		    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		        return true;
		    }
		    return false;
		}

}
