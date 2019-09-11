package com.example.homework02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int toppings_count = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Pizza Store");

        Button add_topping = findViewById(R.id.add_topping);
        Button clear_topping = findViewById(R.id.clear_topping);
        Button checkout = findViewById(R.id.checkout);
        final GridLayout gridLayout = findViewById(R.id.gridLayout);
        final ProgressBar progress_bar = findViewById(R.id.progressBar);


        add_topping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toppings_count == 10){
                    Toast.makeText(getApplicationContext(), "Maximum Topping capacity reached", Toast.LENGTH_LONG).show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle(R.string.choose_topping);
                    builder.setItems(R.array.toppings, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toppings_count = toppings_count + 1;
                            final ImageView imageView = new ImageView(MainActivity.this);
                            switch (which) {
                                case 0:  imageView.setImageResource(R.drawable.bacon);
                                         imageView.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View view) {
                                                 toppings_count = toppings_count -1;
                                                 gridLayout.removeView(imageView);
                                                 progress_bar.setProgress(toppings_count*10);
                                             }
                                         });
                                         break;
                                case 1:  imageView.setImageResource(R.drawable.cheese);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                toppings_count = toppings_count -1;
                                                gridLayout.removeView(imageView);
                                                progress_bar.setProgress(toppings_count*10);
                                            }
                                        });
                                         break;
                                case 2: imageView.setImageResource(R.drawable.garlic);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                    break;
                                case 3: imageView.setImageResource(R.drawable.green_pepper);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                break;
                                case 4: imageView.setImageResource(R.drawable.mashroom);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                break;
                                case 5: imageView.setImageResource(R.drawable.olive);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                break;
                                case 6: imageView.setImageResource(R.drawable.onion);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                break;
                                case 7: imageView.setImageResource(R.drawable.red_pepper);
                                    imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            toppings_count = toppings_count -1;
                                            gridLayout.removeView(imageView);
                                            progress_bar.setProgress(toppings_count*10);
                                        }
                                    });
                                break;
                            }
                            gridLayout.addView(imageView, 0);
                            progress_bar.setProgress(toppings_count*10);

                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });


        clear_topping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toppings_count = 0;
                progress_bar.setProgress(0);
                gridLayout.removeAllViews();
            }
        });







    }




}
