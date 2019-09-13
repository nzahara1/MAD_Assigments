package com.example.pizzastorecalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int progressCounter = 0;
    public static String ORDER = "order";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);

        setTitle("Pizza Store");

        Button addToppings = findViewById(R.id.toppingbtn);
        Button clearToppings = findViewById(R.id.clearbtn);
        Button checkOut = findViewById(R.id.checkout);

        final GridLayout gridLayout = findViewById(R.id.gridLayout);

        final LinkedHashMap<String, Integer> itemIDMap = new LinkedHashMap<>();
        itemIDMap.put("Bacon", R.drawable.bacon);
        itemIDMap.put("Cheese", R.drawable.cheese);
        itemIDMap.put("Garlic", R.drawable.garlic);
        itemIDMap.put("Green Pepper", R.drawable.green_pepper);
        itemIDMap.put("Mushroom", R.drawable.mashroom);
        itemIDMap.put("Olives", R.drawable.olive);
        itemIDMap.put("Onions", R.drawable.onion);
        itemIDMap.put("Red Pepper", R.drawable.red_pepper);

        final String[] items = itemIDMap.keySet().toArray(new String[itemIDMap.size()]);

        final List<String> selectedItems = new ArrayList<String>();
        final LinkedList<ImageView> selectedImages = new LinkedList<ImageView>();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        final CheckBox checkBox = findViewById(R.id.checkBox);

        alertDialog.setTitle("Choose a Topping").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectedItems.add(items[i]);
                if (selectedImages.size() > 9) {
                    Toast.makeText(MainActivity.this, "Maximum Topping capacity reached!", Toast.LENGTH_LONG).show();
                    return;
                }
                ImageView view = new ImageView(MainActivity.this);
                view.setImageDrawable(getDrawable(itemIDMap.get(items[i])));
                view.setContentDescription(items[i]);
                view.setId(View.generateViewId());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gridLayout.removeView(view);
                        progressCounter = progressCounter - 10;
                        progressBar.setProgress(progressCounter);
                        selectedImages.remove(view);
                    }
                });
                gridLayout.addView(view);
                selectedImages.add(view);
                progressCounter += 10;
                progressBar.setProgress(progressCounter);
            }
        }).create();

        addToppings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        clearToppings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // for (ImageView view1 : selectedImages) {
                    gridLayout.removeAllViews();
                    checkBox.setChecked(false);
                    selectedImages.clear();
               // }
                progressCounter = 0;
                progressBar.setProgress(0);
            }
        });

        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder selectedItems = new StringBuilder();
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                for (ImageView views : selectedImages) {
                    selectedItems.append(",").append(views.getContentDescription());
                }
                if (selectedImages.size() > 0) {
                    selectedItems.setCharAt(0,' ');
                }
                Order order = new Order(checkBox.isChecked(), selectedItems);
                intent.putExtra(ORDER, order);
                startActivity(intent);
            }
        });
    }
}
