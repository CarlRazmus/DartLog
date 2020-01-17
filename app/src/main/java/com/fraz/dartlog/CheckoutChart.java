package com.fraz.dartlog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashMap;

public class CheckoutChart implements Serializable {

    private static HashMap<Integer, String> doubleCheckouts = new HashMap<>();
    private static HashMap<Integer, String> singleCheckouts = new HashMap<>();

    public CheckoutChart() {
        if (doubleCheckouts.isEmpty())
            initCheckoutMap();
    }

    public String getSingleOutCheckoutText(int score) {
        return getCheckoutText(score, singleCheckouts);
    }

    public String getDoubleOutCheckoutText(int score) {
        return getCheckoutText(score, doubleCheckouts);
    }

    public static void initCheckoutMap() {
        initCheckoutMap(singleCheckouts,
                MyApplication.getInstance().getResources().openRawResource(R.raw.single_checkout_chart));
        initCheckoutMap(doubleCheckouts,
                MyApplication.getInstance().getResources().openRawResource(R.raw.double_checkout_chart));
    }

    private String getCheckoutText(int score, HashMap<Integer, String> checkouts) {
        String checkoutText = checkouts.get(score);
        if (checkoutText == null)
            return "No checkout";
        else
            return checkoutText;
    }

    private static void initCheckoutMap(HashMap<Integer, String> checkouts, InputStream inputStream) {
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
