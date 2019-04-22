package com.example.markus.customdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Markus on 3.4.2015.
 */
public class AddWordDialog extends DialogFragment{

    public static final String EXTRA_LANGUAGE = "language";
    public Spinner spinner;
    public DatabaseHandler handler;
    public List<String> Languages;
    public ArrayAdapter<String> LanguagesChoices;
    public LinearLayout languages;


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        // Form the database handler and load all languages.
        handler = new DatabaseHandler(getActivity());
        try {
            Languages = handler.getLanguages();
        }catch(Exception e){
            Log.d("addWordDialog", "Problems with loading the languages.");
        }

        // if there is at least one language, populate a spinner with all found languages.
        if(!Languages.isEmpty() && Languages.size()>0) {

                spinner = new Spinner(getActivity());
                // Array adapter for languages
                LanguagesChoices = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,Languages);
                LanguagesChoices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // add adapter to spinner
            spinner.setAdapter(LanguagesChoices);
            // Styling the spinner
            spinner.setPopupBackgroundResource(R.drawable.spinner_background);
            spinner.setPadding(35,0,0,0);
            spinner.setBackgroundResource(R.drawable.corners);
            spinner.setPopupBackgroundResource(R.drawable.corners);

            languages = new LinearLayout(getActivity());
            languages.setOrientation(LinearLayout.VERTICAL);
            languages.setBackgroundResource(R.drawable.corners);
            // Set the checkboxes.
            for(String language : Languages){
            CheckBox dictionary = new CheckBox(getActivity());
            dictionary.setText(language);
            dictionary.setPadding(35,0,0,0);

            languages.addView(dictionary);

            }



            builder.setView(languages);

        }else{
            // if there are no dictionaries yet, add a button to view to inform that there are no dictionaries in the database
            Button noWords = new Button(getActivity());
            noWords.setId(0);
            noWords.setText("No dictionaries yet");
            noWords.setTextSize(25f);
            noWords.setBackgroundColor(Color.rgb(203, 203, 203));
            noWords.setTextColor(Color.rgb(50,50,50));
            builder.setView(spinner);
        }

     builder.setTitle(R.string.Word_language).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
             if(!Languages.isEmpty()&& Languages.size()>0) {
                 // start a new AddWordActivity with the selected language as string extra

                 ArrayList<String> dictionaries = getSelectedlanguages();
                 if(!dictionaries.isEmpty()){
                     Intent intent = new Intent(getActivity(), AddWordActivity.class);
                     intent.putStringArrayListExtra("Dictionaries",dictionaries);
                     startActivity(intent);
                     dialog.dismiss();
                 }else{
                     Toast.makeText(getActivity(),"Select at least one dictionary",Toast.LENGTH_SHORT).show();
                 }

             }else{
                 Toast.makeText(getActivity(),"Please add a dictionary first before adding any words!",Toast.LENGTH_SHORT).show();
             }
         }
     }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int id) {
             dialog.dismiss();
         }
     });

return builder.create();
    }
    public ArrayList<String> getSelectedlanguages(){
        int children = languages.getChildCount();
        ArrayList<String> dictionaries = new ArrayList<String>();
        for(int i = 0; i<children; i++){
            CheckBox dictionary =(CheckBox)languages.getChildAt(i);
            if(dictionary.isChecked()){
                dictionaries.add((String)dictionary.getText());
            }
        }
        return dictionaries;
    }

}
