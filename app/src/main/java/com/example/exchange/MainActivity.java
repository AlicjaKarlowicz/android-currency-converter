package com.example.exchange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.Bidi;

public class MainActivity extends AppCompatActivity {

    private Exchange exchange = new Exchange();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //allow to connect to the internet in order for json to work
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);

    }

    public void onClickConvert(View view) {
        Spinner currencyList = (Spinner) findViewById(R.id.currency);
        String currency = String.valueOf(currencyList.getSelectedItem());



       EditText value = (EditText) findViewById(R.id.enterValue);
       TextView pln = (TextView) findViewById(R.id.pln);

       try {
           if (!value.getText().toString().isEmpty()) {
               String val = value.getText().toString().replace(",",".");
               BigDecimal valueCurr= new BigDecimal(val);
              pln.setText("PLN ");
               pln.append(String.valueOf(exchange.exchange(valueCurr, currency)));

           }
       } catch (NumberFormatException e) {
           Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
       }


    }
}
