package com.fraz.dartlog;

import android.content.Context;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;

public class CheckoutChart implements Serializable{

    private SparseArray<String> checkouts = new SparseArray<>();

    public CheckoutChart(Context context, int checkoutChartRawResourceId) {
        initCheckoutMap(context, checkoutChartRawResourceId);
    }

    public boolean checkoutAvailable(int score) {
        return checkouts.get(score) != null;
    }

    public String getCheckoutText(int score) {
        return checkouts.get(score, "N/A");
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
