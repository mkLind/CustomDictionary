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
import android.widget.EditText;
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


    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            spinner.setPadding(35,0,0,0);
            spinner.setBackgroundResource(R.drawable.corners);
            spinner.setPopupBackgroundResource(R.drawable.corners);
            builder.setView(spinner);

        }else{
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
                 Intent intent = new Intent(getActivity(), AddWordActivity.class);
                 String language = String.valueOf(spinner.getSelectedItem());
                 intent.putExtra(EXTRA_LANGUAGE, language);
                 startActivity(intent);
                 dialog.dismiss();
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

}
