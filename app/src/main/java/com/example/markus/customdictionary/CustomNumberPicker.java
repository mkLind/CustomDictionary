package com.example.markus.customdictionary;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by marku on 27.12.2017.
 * an UI element for adding or reducing an integer value
 */

public class CustomNumberPicker {
    private int maxValue;
    private int minValue;
    private int currentValue;
    private Button minus;
    private Button plus;
    private TextView number;
    private Context cont;
    private LinearLayout base;
    private SeekBar bar;


    public CustomNumberPicker(int minValue, int maxValue,  int currentValue, Context cont){
        this.currentValue = currentValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.cont = cont;
        // layout params for margins
        LinearLayout.LayoutParams lpnum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpnum.setMargins(20,10,20,10);
        LinearLayout.LayoutParams lpcomp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpcomp.setMargins(20,10,20,10);
        base = new LinearLayout(cont);// base layout for the component
        // Number that can be incremented or lessened
        bar = new SeekBar(cont);
        bar.setMax(maxValue);


        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress > getCurrentValue()){
                    increaseCount();
                }else if(progress < getCurrentValue()){
                    decreaseCount();
                }
                Log.d("cnb","changed questions: " + progress + "max progress: " + bar.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        number = new TextView(cont);
        number.setText("" + currentValue);
        number.setTextColor(Color.BLACK);
        number.setGravity(Gravity.CENTER);


        base.setOrientation(LinearLayout.VERTICAL);
        // Buttons for adding to the number and subtracting from the number
        plus = new Button(cont);
        plus.setTextColor(Color.rgb(22,108,22));

        plus.setText("+");
        plus.setBackgroundResource(R.drawable.buttonback);
        plus.setWidth(50);
        plus.setHapticFeedbackEnabled(true);


        minus = new Button(cont);
        minus.setTextColor(Color.rgb(22,108,22));
        minus.setText("-");
        minus.setBackgroundResource(R.drawable.buttonback);
        minus.setHapticFeedbackEnabled(true);

        minus.setWidth(50);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment number only if below max value

           increaseCount();
            }
        });




        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             // diminish number only if bigger than minimum number
               decreaseCount();
            }
        });




        base.addView(number,lpnum);
        base.addView(bar,lpcomp);
        /*
        base.addView(minus,lpcomp);
        base.addView(plus,lpcomp);
        */
    }

    public void increaseCount(){
        if(getCurrentValue()>=getMaxValue()){
            number.setTextColor(Color.DKGRAY);
            number.setText("" + getCurrentValue());
        }else{
            number.setTextColor(Color.BLACK);

            setCurrentValue(getCurrentValue() + 1);
            number.setText("" + getCurrentValue());
        }
    }
    public void decreaseCount(){
        if(getCurrentValue()<=getMinValue()){
            number.setTextColor(Color.DKGRAY);
            number.setText("" + getCurrentValue());
        }else{
            number.setTextColor(Color.BLACK);
            setCurrentValue(getCurrentValue() - 1);
            number.setText("" + getCurrentValue());
        }
    }


    public void setBackground(int drawable){
    base.setBackgroundResource(drawable);
    number.setBackgroundResource(drawable);
    }
    public  View getNBPicker(){
        return base;
    }
    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        bar.setMax(maxValue);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
        bar.setProgress(currentValue);
    }

    public Button getMinus() {
        return minus;
    }

    public void setMinus(Button minus) {
        this.minus = minus;
    }

    public Button getPlus() {
        return plus;
    }

    public void setPlus(Button plus) {
        this.plus = plus;
    }

    public TextView getNumber() {
        return number;
    }

    public void setNumber(TextView number) {
        this.number = number;
    }

    public Context getCont() {
        return cont;
    }

    public void setCont(Context cont) {
        this.cont = cont;
    }
}
