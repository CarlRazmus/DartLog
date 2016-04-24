package com.fraz.dartlog;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CheckoutChart {

    private Map<Integer, String> checkouts = new HashMap<>();

    public CheckoutChart(Context context, int checkoutChartRawResourceId) {
        initCheckoutMap(context, checkoutChartRawResourceId);
    }

    public String getCheckoutText(int score) {
        String checkout = checkouts.get(score);
        if (checkout == null) {
            checkout = "N/A";
        }
        return checkout;
    }

    private void initCheckoutMap(Context context, int checkoutChartRawResourceId) {
        InputStream inputStream = context.getResources().openRawResource(checkoutChartRawResourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String line = reader.readLine();
            while (line != null) {
                String[] checkout = line.split(",");
                checkouts.put(Integer.parseInt(checkout[0]), checkout[1]);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
