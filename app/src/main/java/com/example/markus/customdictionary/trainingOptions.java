package com.example.markus.customdictionary;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Markus on 18.6.2017.
 */

public class trainingOptions extends DialogFragment {

    public DatabaseHandler handler;
    public static final String EXTRA_LANGUAGE = "language";
    public static final String TRAINING_LENGTH = "length";
    public static final String SORTING_TYPE = "sorting_type";
    public List<String> Languages;
    public ArrayAdapter<String> LanguagesChoices;
    public Spinner spinner;
    public String language ="";
    public NumberPicker nb;
    public CustomNumberPicker cnb;
    public CheckBox train_least_familiar;
    public CheckBox train_least_displayed;
     public RadioGroup r ;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        train_least_familiar = new CheckBox(getActivity());
        train_least_familiar.setText("Prioritize least familiar words");
        train_least_familiar.setSelected(false);

        train_least_displayed = new CheckBox(getActivity());
        train_least_displayed.setText("Prioritize least displayed words");
        train_least_displayed.setSelected(false);

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(layout.VERTICAL);
        layout.setBackgroundResource(R.drawable.corners);
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(60,20,60,20);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(60,20,60,60);
        cnb = new CustomNumberPicker(4,4,4,getActivity());
        cnb.setBackground(R.drawable.corners);


        handler = new DatabaseHandler(getActivity());
        Log.d("addWordDialog", "db handler formed");

        try {
            Languages = handler.getLanguages();
        }catch(Exception e){
            Log.d("addWordDialog", "Problems with loading the languages.");
        }


        if(!Languages.isEmpty() && Languages.size()>0) {
            spinner = new Spinner(getActivity());
            LanguagesChoices = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,Languages);
            LanguagesChoices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(LanguagesChoices);

            spinner.setPopupBackgroundResource(R.drawable.corners);


            spinner.setBackgroundResource(R.drawable.corners);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cnb.setMaxValue(handler.groupByLanguage(String.valueOf(spinner.getSelectedItem()), SortingType.ALPHABETICALLY).size());
                }
                public void onNothingSelected(AdapterView<?> adapterView){

                }
            });
            // Change to radiogroup
            train_least_displayed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    train_least_familiar.setSelected(false);
                }
            });
            train_least_familiar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    train_least_displayed.setSelected(false);
                }
            });


                cnb.setMaxValue(handler.groupByLanguage(String.valueOf(spinner.getSelectedItem()),SortingType.ALPHABETICALLY).size());
            layout.addView(spinner, layoutparams);
            //layout.addView(nb,lp)
            layout.addView(cnb.getNBPicker(),layoutparams);

            layout.addView(train_least_familiar, layoutparams);
            layout.addView(train_least_displayed, layoutparams);
            builder.setView(layout);

        }else{
            Button noWords = new Button(getActivity());
            noWords.setId(0);
            noWords.setText("No dictionaries yet");
            noWords.setTextSize(25f);
            noWords.setBackgroundColor(Color.rgb(203, 203, 203));
            noWords.setTextColor(Color.rgb(50,50,50));
            builder.setView(spinner);
        }


         AlertDialog.Builder builder1 = builder.setTitle("Choose Vocabulary ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


                if (!Languages.isEmpty() && Languages.size() > 0) {
                    Intent intent = new Intent(getActivity(), Training.class);
                    intent.putExtra(EXTRA_LANGUAGE,String.valueOf(spinner.getSelectedItem()));
                    intent.putExtra(TRAINING_LENGTH,String.valueOf(cnb.getCurrentValue()));

                        if (train_least_familiar.isSelected()) {

                            intent.putExtra(SORTING_TYPE, SortingType.FAMILIARITY.name());
                        } else if (train_least_displayed.isSelected()) {

                            intent.putExtra(SORTING_TYPE, SortingType.BY_TIMES_DISPLAYED.name());
                        } else {

                            intent.putExtra(SORTING_TYPE, SortingType.ALPHABETICALLY.name());
                        }

                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "No words to train yet", Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dismiss();
            }
        });
        return builder.create();
    }
}
