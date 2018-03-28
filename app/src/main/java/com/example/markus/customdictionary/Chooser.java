package com.example.markus.customdictionary;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by Markus on 3.9.2016.
 */

public class Chooser extends DialogFragment {
public Dialog onCreateDialog( Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    LinearLayout b = new LinearLayout(getActivity()); // layout for dialog
    // use layoutparameters for margin
    LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutparams.setMargins(10,20,10,20);
    // ba
    b.setBackgroundResource(R.drawable.corners);
    b.setOrientation(LinearLayout.VERTICAL);



    // buttons for starting dialogs for new dictionary addition and for new word addition
    Button newDict = new Button(getActivity());
    newDict.setText("New Dictionary");
    newDict.setPadding(10,20,10,40);


     newDict.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             // open dialog for adding new dctionary
             AddDictDialog dial = new AddDictDialog();
             FragmentManager manager = getFragmentManager();

             dial.show(manager,"");
             dismiss();
         }
     });

    Button newWord = new Button(getActivity());
    newWord.setPadding(10,20,10,40);
    newWord.setBackgroundResource(R.drawable.buttonback);
    newWord.setText("New Word");

   newWord.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           // open dialog for selecting to which dictionary a new word should be added
           AddWordDialog dial = new AddWordDialog();
           FragmentManager manager = getFragmentManager();

           dial.show(manager,"");
           dismiss();
       }
   });
    newDict.setBackgroundResource(R.drawable.buttonback);
    b.addView(newDict, layoutparams);
    b.addView(newWord, layoutparams);
    builder.setView(b);
    builder.setTitle("Choose action:");


    return builder.create();
}
 }
