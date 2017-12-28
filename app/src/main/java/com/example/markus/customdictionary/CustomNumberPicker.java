package com.example.markus.customdictionary;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by marku on 27.12.2017.
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


    public CustomNumberPicker(int minValue, int maxValue,  int currentValue, Context cont){
        this.currentValue = currentValue;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.cont = cont;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,10,20,10);
        base = new LinearLayout(cont);
        number = new TextView(cont);
        number.setText("" + currentValue);
        number.setTextColor(Color.BLACK);
        number.setWidth(50);


        base.setOrientation(LinearLayout.HORIZONTAL);
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
                Log.d("CURRENT VALUE: ","" + getCurrentValue());
                plus.performHapticFeedback(5);
            if(getCurrentValue()>=getMaxValue()){
                number.setTextColor(Color.DKGRAY);
                number.setText("" + getCurrentValue());
            }else{
                number.setTextColor(Color.BLACK);

                setCurrentValue(getCurrentValue() + 1);
                number.setText("" + getCurrentValue());
            }
            }
        });



        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus.performHapticFeedback(5);
                Log.d("CURRENT VALUE: ","" + getCurrentValue());
                if(getCurrentValue()<=getMinValue()){
                    number.setTextColor(Color.DKGRAY);
                    number.setText("" + getCurrentValue());
                }else{
                    number.setTextColor(Color.BLACK);
                    setCurrentValue(getCurrentValue() - 1);
                    number.setText("" + getCurrentValue());
                }
            }
        });



        base.addView(minus,lp);
        base.addView(number,lp);
        base.addView(plus,lp);

    }

    public void setBackground(int drawable){
    base.setBackgroundResource(drawable);
    }
    public  View getNBPicker(){
        return base;
    }
    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
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
