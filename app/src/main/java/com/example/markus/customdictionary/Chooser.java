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
    LinearLayout b = new LinearLayout(getActivity());
    b.setOrientation(LinearLayout.VERTICAL);
    Button newDict = new Button(getActivity());
    newDict.setText("New Dictionary");

    Button newWord = new Button(getActivity());
    newWord.setText("New Word");
     newDict.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             AddDictDialog dial = new AddDictDialog();
             FragmentManager manager = getFragmentManager();

             dial.show(manager,"");
             dismiss();
         }
     });

   newWord.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           AddWordDialog dial = new AddWordDialog();
           FragmentManager manager = getFragmentManager();

           dial.show(manager,"");
           dismiss();
       }
   });

    b.addView(newDict);
    b.addView(newWord);
    builder.setView(b);
    builder.setTitle("Choose action:");


    return builder.create();
}
 }
