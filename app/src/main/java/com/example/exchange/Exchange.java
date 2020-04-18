package com.example.exchange;



import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class Exchange {


    public Exchange() {
    }

    public BigDecimal getExchangeRates(String toCurrency) {

        StringBuffer response = new StringBuffer();
        try {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append("https://api.exchangerate-api.com/v4/latest/").append(toCurrency);
            //String url_str = "https://api.exchangerate-api.com/v4/latest/" + toCurrency;


            URL url = new URL(urlBuilder.toString());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            int responseCode = request.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;

            while ((inputLine=in.readLine()) !=null) {
                response.append(inputLine);
            }
            in.close();


        } catch (MalformedURLException e) {
            System.out.println("bad url");
        }  catch (IOException e) {
            System.out.println("Connection failed");
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        Map m = gson.fromJson(response.toString(), Map.class);

        Map<String, Object> ratesMap = (Map<String, Object>) m.get("rates");
        //ratesMap.forEach((k, v) -> System.out.println("Key = " + k + ", Value = " + v));

        String rate = ratesMap.get("PLN").toString();
        BigDecimal rateDecimal = new BigDecimal(rate);

        return rateDecimal;
    }

    public BigDecimal exchange(BigDecimal amount, String currency) {


        BigDecimal comission = null;
        BigDecimal rate= getExchangeRates(currency);


        if (isBetween(amount,BigDecimal.valueOf(3e6),BigDecimal.valueOf(10e6)))
            comission = BigDecimal.valueOf(0.0008);
        if (isBetween(amount,BigDecimal.valueOf(1e6),BigDecimal.valueOf(3e6)))
            comission = BigDecimal.valueOf(0.001);
        if (isBetween(amount,BigDecimal.valueOf(2e5),BigDecimal.valueOf(1e6)))
            comission = BigDecimal.valueOf(0.0015);
        if (isBetween(amount,BigDecimal.valueOf(0),BigDecimal.valueOf(2e5)))
            comission = BigDecimal.valueOf(0.002);


        return amount.add(amount.multiply(comission)).multiply(rate).setScale(2,RoundingMode.HALF_UP);
    }

    public boolean isBetween(BigDecimal value, BigDecimal start, BigDecimal end){
        return value.compareTo(start) > 0 && value.compareTo(end) <= 0;
    }
}
