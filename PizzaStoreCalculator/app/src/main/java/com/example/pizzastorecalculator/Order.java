package com.example.pizzastorecalculator;

import android.util.Log;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Order implements Serializable {

    private boolean isDelivery;

    private StringBuilder toppings;


    public Order(boolean isDelivery, StringBuilder toppings) {
        this.isDelivery = isDelivery;
        this.toppings = toppings;
    }

    public boolean isDelivery() {
        return isDelivery;
    }

    public StringBuilder getToppings() {
        return toppings;
    }
}
