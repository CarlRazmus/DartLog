package com.fraz.dartlog;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

public class CheckoutChart implements Serializable {

    private HashMap<Integer, String> checkouts = new HashMap<>();

    public CheckoutChart(Context context, int checkoutChartRawResourceId) {
        initCheckoutMap(context, checkoutChartRawResourceId);
    }

    public boolean checkoutAvailable(int score) {
        return checkouts.get(score) != null;
    }

    public String getCheckoutText(int score) {
        String checkoutText = checkouts.get(score);
        if (checkoutText == null)
            return "No checkout";
        else
            return checkoutText;
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
