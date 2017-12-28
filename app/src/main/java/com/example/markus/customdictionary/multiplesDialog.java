package com.example.markus.customdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Markus on 29.11.2015.
 */
public class multiplesDialog extends DialogFragment {



        private ArrayList<String> Meanings;
        private String Word;



        public Dialog onCreateDialog(Bundle textFromActivity){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


            Word = getArguments().getString("Word");

            try{
                Meanings = getArguments().getStringArrayList("Meanings");

            }catch(Exception e){

            }
            ScrollView view = new ScrollView(getActivity());
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setBackgroundResource(R.drawable.corners);
            layout.setOrientation(LinearLayout.VERTICAL);
            for(int i = 0; i<Meanings.size();i++) {
                Button text = new Button(getActivity());
                text.setText(Meanings.get(i) + "\n");
                text.setBackground(getResources().getDrawable(R.drawable.ab_transparent_example));
                layout.addView(text);
            }
            view.addView(layout);

            builder.setView(view);

            builder.setTitle("The word" + Word + "Has multiple meanings:").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();

                }
            });
            return builder.create();
        }

    }


