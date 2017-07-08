package com.example.markus.customdictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Markus on 21.11.2015.
 */
public class NetIntent extends DialogFragment {
    String language;
    String word;
 private String url;



    public Dialog onCreateDialog(Bundle textFromActivity){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

       url = getArguments().getString("url");
        LinearLayout base = new LinearLayout(getActivity());
        base.setOrientation(LinearLayout.VERTICAL);
        /*
TextView question = new TextView(getActivity());
        question.setText("Search the internet?");
        question.setTextSize(20f);
        question.setPadding(50, 40, 10, 40);
        base.addView(question);


        builder.setView(base);
*/
        builder.setTitle("Word not found. Search from Google Translate?").setPositiveButton("search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("URL", url);

                Intent intent = new Intent();

                intent.setAction(Intent.ACTION_VIEW);


                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(url));

                try {
                    Activity activity = getActivity();
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Log.d("Failed to commit action", "Exception: " + e.getMessage().toString());
                }
/*
                Intent intent2 = new Intent(getActivity(),web_activity.class);
                // intent2.setData(Uri.parse(url));
                intent2.putExtra("URL",Uri.parse(url));
                startActivity(intent2);
*/
            }

        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });
        return builder.create();
    }

}

