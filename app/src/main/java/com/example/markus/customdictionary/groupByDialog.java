package com.example.markus.customdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Markus on 28.4.2015.
 */
public class groupByDialog extends DialogFragment {

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
            spinner.setPadding(
                    35,0,0,35
            );
            spinner.setPopupBackgroundResource(R.drawable.corners);
            spinner.setBackgroundResource(R.drawable.corners);
            builder.setView(spinner);

        }else{
            Toast.makeText(getActivity(), "No dictionaries found!", Toast.LENGTH_SHORT);
            dismiss();
        }

        builder.setTitle(R.string.Word_language).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getActivity(), groupByActivity.class);
                String language = String.valueOf(spinner.getSelectedItem());
                intent.putExtra(EXTRA_LANGUAGE, language);
                startActivity(intent);
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

}


