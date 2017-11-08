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
        nb.setMaxValue(100);
        nb.setMinValue(10);
        nb.setValue(10);
        r = new RadioGroup(getActivity());
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


            r.setBackgroundResource(android.R.drawable.divider_horizontal_bright);

            RadioButton shortTr = new RadioButton(getActivity());
            shortTr.setText("Short training");
            shortTr.setId(R.id.option1);
            RadioButton normalTr = new RadioButton(getActivity());
            normalTr.setText("Normal training");
            normalTr.setId(R.id.option2);
            RadioButton longTr = new RadioButton(getActivity());
            longTr.setText("Long training");
            longTr.setId(R.id.option3);
            normalTr.setSelected(true);

            r.addView(shortTr);
            r.addView(normalTr);
            r.addView(longTr);
            normalTr.setSelected(true);
            r.setSelected(true);
            nb.setBackgroundResource(android.R.drawable.divider_horizontal_bright);
            layout.addView(spinner, layoutparams);
            layout.addView(r,layoutparams);

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
                    intent.putExtra(TRAINING_LENGTH,String.valueOf(r.getCheckedRadioButtonId()));
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
