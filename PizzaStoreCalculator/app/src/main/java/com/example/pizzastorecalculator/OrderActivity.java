package com.example.pizzastorecalculator;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        setTitle("Pizza Store");

        Intent intent = getIntent();
        TextView toppingsVal = findViewById(R.id.toppings_val);
        TextView toppings = findViewById(R.id.toppings_list);
        TextView totalVal = findViewById(R.id.total_val);
        TextView deliveryCost = findViewById(R.id.delivery_cost_val);

        if (intent != null) {
            Order orderDetails = (Order) intent.getSerializableExtra(MainActivity.ORDER);
            if (orderDetails.isDelivery()) {
                deliveryCost.setText(4.00 + "$");
            } else {
                deliveryCost.setText(0.00 + "$");
            }
            int toppingLen = (orderDetails.getToppings().length() <= 0) ? 0 : orderDetails.getToppings().toString().split(",").length;
            toppingsVal.setText(toppingLen * 1.50 + "$");
            toppings.setText(orderDetails.getToppings().toString());
            double total = (toppingLen * 1.50) + 6.5 + ((orderDetails.isDelivery()) ? 4 : 0);
            totalVal.setText(total + "$");
        }

        findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

}
