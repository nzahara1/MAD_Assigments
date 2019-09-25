package com.example.inclass4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBar;
    int complexityNumber = 0;
    ProgressDialog progressDialog;
    TextView avgVal;
    TextView maxVal;
    TextView minVal;
    ProgressBar progressBar;
    ExecutorService threadPoolExecutor;
    Handler handler;
    static final int START = 0;
    static final int PROGRESS = 1;
    static final int STOP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMax(10);

        avgVal = findViewById(R.id.avg_val);
        maxVal = findViewById(R.id.max_val);
        minVal = findViewById(R.id.min_val);

        final TextView complexityView = findViewById(R.id.complexity_val);
        Button asynCallBtn = findViewById(R.id.async_btn);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                complexityNumber = i;
                complexityView.setText(String.valueOf(i) + " times");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        asynCallBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (complexityNumber != 0) {
//                    new ArrayTask().execute(complexityNumber);
//                }
//            }
//        });
        threadPoolExecutor = Executors.newFixedThreadPool(2);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case START:
                        progressDialog = new ProgressDialog(MainActivity.this);
                        break;
                    case PROGRESS:
                        progressDialog.show();
                        break;
                    case STOP:
                        List<Double> resultValues = (List<Double>) message.obj;
                        avgVal.setText(String.valueOf(resultValues.get(0) / complexityNumber));
                        maxVal.setText(String.valueOf(resultValues.get(1)));
                        minVal.setText(String.valueOf(resultValues.get(2)));
                        progressDialog.dismiss();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        asynCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (complexityNumber > 0) {
                    threadPoolExecutor.execute(new ThreadTask(complexityNumber));
                }
            }
        });
    }

    class ThreadTask implements Runnable {

        private int complexityCount;

        public ThreadTask(int complexityCount) {
            this.complexityCount = complexityCount;
            Message startMessage = new Message();
            startMessage.what = START;
            handler.sendMessage(startMessage);
        }

        @Override
        public void run() {
            Message progressMessage = new Message();
            progressMessage.what = PROGRESS;
            handler.sendMessage(progressMessage);
            List<Double> arrayList = HeavyWork.getArrayNumbers(this.complexityCount);
            Log.d("val", String.valueOf(arrayList));
            Double max = arrayList.get(arrayList.size() - 1);
            Double min = arrayList.get(0);
            Double avg = 0.0;
            for (Double val : arrayList) {
                avg += val;
                min = (min < val) ? min : val;
                max = (max > val) ? max : val;
            }
            List<Double> result = new ArrayList<>();
            result.add(avg);
            result.add(max);
            result.add(min);
            Message stopMessage = new Message();
            stopMessage.obj = result;
            stopMessage.what = STOP;
            handler.sendMessage(stopMessage);
        }
    }
}

   /* class ArrayTask extends AsyncTask<Integer, String, List<Double>> {

        @Override
        protected List<Double> doInBackground(Integer... integers) {
            publishProgress("Start");
            List<Double> doubleValues = HeavyWork.getArrayNumbers(integers[0]);
            Double max = doubleValues.get(doubleValues.size() - 1);
            Double min = doubleValues.get(0);
            Double avg = 0.0;
            for (Double val : doubleValues) {
                Log.d("val", String.valueOf(val));
                avg += val;
                min = (min < val) ? min : val;
                max = (max > val) ? max : val;
            }
            List<Double> result = new ArrayList<>();
            result.add(avg);
            result.add(max);
            result.add(min);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            //linearLayout = findViewById(R.id.linearLayout2);
        }

        @Override
        protected void onPostExecute(List<Double> doubleValues) {
            super.onPostExecute(doubleValues);
            avgVal.setText(String.valueOf(doubleValues.get(0) / complexityNumber));
            maxVal.setText(String.valueOf(doubleValues.get(1)));
            minVal.setText(String.valueOf(doubleValues.get(2)));
            linearLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            linearLayout.setVisibility(View.VISIBLE);
        }
    } */
