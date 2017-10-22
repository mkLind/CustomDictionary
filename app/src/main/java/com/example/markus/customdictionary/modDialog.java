package com.example.markus.customdictionary;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Markus on 11.6.2016.
 */

public class modDialog extends DialogFragment{

    EditText word;
    EditText meaning;
     View view;
    String tmp;
    String[] tmp1;
    public Dialog onCreateDialog(Bundle modifiables){
       try{
            tmp = getArguments().getString("WORDMEANING");
            Log.d("Word deleted@modDialog",""+ tmp);

       }catch(Exception e){

       }



    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflant = getActivity().getLayoutInflater();

        View view = inflant.inflate(R.layout.modui,null);

        word = (EditText) view.findViewById(R.id.OLD_WORD);
        meaning = (EditText) view.findViewById(R.id.OLD_MEANING);
         tmp1 = tmp.split(":");
        word.setText(tmp1[0]);
        meaning.setText(tmp1[1]);

        builder.setView(view).setPositiveButton("Modify",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

if(!word.getText().toString().equals("") &&!meaning.getText().toString().equals("") ) {
    DatabaseHandler handler = new DatabaseHandler(getActivity());
    handler.updateWord(tmp1[0], tmp1[1], word.getText().toString(), meaning.getText().toString());
}else{
    Toast.makeText(getActivity(),"Please provide new word and/or meaning!",Toast.LENGTH_SHORT).show();
}
            }
        }).setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
dialog.dismiss();
            }
        });
return builder.create();
    }
    @Override
    public void onDismiss(final DialogInterface dialog){
        super.onDismiss(dialog);
        final Activity activity = getActivity();
        if(activity instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener)activity).onDismiss(dialog);
        }

    }
}
