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
    public List<String> Languages;
    public ArrayAdapter<String> LanguagesChoices;
    public Spinner spinner;
    public String language ="";
    public NumberPicker nb;
     public RadioGroup r ;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(layout.VERTICAL);
        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(60,20,60,20);
        nb = new NumberPicker(getActivity());
        getDialog().setTitle("Choose vocabulary and amount of words to train");
        nb.setMinValue(4);
        nb.setValue(4);

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

            spinner.setPopupBackgroundResource(R.drawable.spinner_background);


            spinner.setBackgroundResource(android.R.drawable.divider_horizontal_bright);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nb.setMaxValue(handler.groupByLanguage(String.valueOf(spinner.getSelectedItem())).size());
                }
                public void onNothingSelected(AdapterView<?> adapterView){

                }
            });



            nb.setBackgroundResource(android.R.drawable.divider_horizontal_bright);
          
                nb.setMaxValue(handler.groupByLanguage(String.valueOf(spinner.getSelectedItem())).size());

            layout.addView(spinner, layoutparams);
            layout.addView(nb,layoutparams);

            //layout.addView(nb, layoutparams);
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
                    intent.putExtra(TRAINING_LENGTH,String.valueOf(nb.getValue()));
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
