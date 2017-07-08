package com.example.markus.customdictionary;

import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
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

    builder.setTitle("Choose action:").setPositiveButton("Word", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            AddWordDialog dial = new AddWordDialog();
            FragmentManager manager = getFragmentManager();

            dial.show(manager,"");
            dismiss();

        }
    }).setNegativeButton("Dictionary", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {
            AddDictDialog dial = new AddDictDialog();
            FragmentManager manager = getFragmentManager();

            dial.show(manager,"");
            dismiss();
        }
    }).setNeutralButton("Back", new DialogInterface.OnClickListener(){
        public void onClick(DialogInterface dialog, int id){
            dismiss();
        }
    });


    return builder.create();
}
 }
