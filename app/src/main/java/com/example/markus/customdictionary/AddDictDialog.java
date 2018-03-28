package com.example.markus.customdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Markus on 3.4.2015.
 */
public class AddDictDialog extends DialogFragment {
    private EditText source;
    private EditText target;
    private DatabaseHandler handler;
    private boolean added  ;
public Dialog onCreateDialog(Bundle savedInstanceState){
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
     final LinearLayout layout = new LinearLayout(getActivity());
    layout.setOrientation(LinearLayout.VERTICAL);

      source = new EditText(getActivity());
      target = new EditText(getActivity());

    source.setSingleLine();
    target.setSingleLine();
    source.setHint("Foreign language or a concept");
    target.setHint("Familiar language or explanation");
    layout.addView(source);
    layout.addView(target);

    builder.setView(layout);
    added = false;

    builder.setTitle(R.string.source_target).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            // function to add the dictionary here
            String ownL;
            String foreignL;
            handler = new DatabaseHandler(getActivity());
            // Ensure that source language and target language has been provided
            if (!source.getText().toString().equals("") && !target.getText().toString().equals("")) {
                // Fetch provided languages from gui  and then fetch all existing languages from the database.
                foreignL = source.getText().toString();
                ownL = target.getText().toString();
                ArrayList<String> existing = handler.getLanguages();

                String newDict = foreignL + " " + "=>" + " " + ownL; // Connect the languages into one entry with '=>' symbol.
                // First check if there is at least one dictionary
                if(existing.size()>0){
                    // IF there is at least one, check that the provided one is not already in the database
                    if(!existing.contains(newDict)){
                        // Add the new dictionary to the database and inform the user
                        handler.addLanguage(foreignL, ownL);
                        Toast.makeText(getActivity(), "Dictionary added!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();


                    }else{
                        // if the dictionary is already in the database, inform the user
                        Toast.makeText(getActivity(), "Dictionary already in the database!", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    // Add the first dictionary in the database
                    handler.addLanguage(foreignL, ownL);
                    Toast.makeText(getActivity(), "Dictionary added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
/*
                if (existing.size() < 1) {

                    handler.addLanguage(foreignL, ownL);
                    Toast.makeText(getActivity(), "Dictionary added!", Toast.LENGTH_SHORT).show();
                    added = true;
                    dialog.dismiss();

                    // if there are already some dictionaries in the database, check if the provided one is already in the base. If it is not, add the dictionary in the database

                } else if (!existing.contains(newDict) && !added) {

                    Log.d("add dict dialog", newDict);
                    Log.d("add dict dialog", "Apparently the dictionary does not already exist...");
                    handler.addLanguage(foreignL, ownL);
                    Toast.makeText(getActivity(), "Dictionary added!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    if (!added) {
                        Toast.makeText(getActivity(), "Dictionary already in the database!", Toast.LENGTH_SHORT).show();
                    }
                }
                */

            } else {
                Toast.makeText(getActivity(), "The text fields should not be empty!", Toast.LENGTH_SHORT).show();
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
