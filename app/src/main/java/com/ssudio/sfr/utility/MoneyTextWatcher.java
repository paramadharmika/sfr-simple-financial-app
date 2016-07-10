package com.ssudio.sfr.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MoneyTextWatcher implements TextWatcher {
    private EditText mDishPrice;

    public MoneyTextWatcher(EditText mDishPrice) {
        this.mDishPrice = mDishPrice;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        /***
         * No need to continue the function if there is nothing to
         * format
         ***/
        if (s.length() == 0){
            return;
        }

        String temp = "";
        if (s.toString().contains(".")) {
            temp =  s.toString().replace(".", ",");
        } else {
            temp = s.toString();
        }

        String value = temp.replaceAll(",", "");

        if (value.length() > 12) {
            value = value.substring(0, 12);
        }

        String formattedPrice = getFormattedCurrency(value);

        if (!(formattedPrice.equalsIgnoreCase(temp))) {
            /***
             * The below given line will call the function recursively
             * and will ends at this if block condition
             ***/
            mDishPrice.setText(formattedPrice.replace(",", "."));
            mDishPrice.setSelection(mDishPrice.length());
        }
    }

    public static String getFormattedCurrency(String value) {
        try {
            NumberFormat formatter = new DecimalFormat("###,###,###,###");

            return formatter.format(Double.parseDouble(value));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
