package com.example.markus.customdictionary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Markus on 20.5.2015.
 */
public class DeleteConfirm extends DialogFragment {
    private ArrayList<String> deleteKeys;
    private String Message;
    private String Dictionary;
    private DatabaseHandler handler;

    public Dialog onCreateDialog(Bundle textFromActivity){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        handler = new DatabaseHandler(getActivity());

        Message = getArguments().getString("Message");
        Dictionary ="";
        try{
            deleteKeys = getArguments().getStringArrayList("DeleteKeys");
            Dictionary = getArguments().getString("Dictionary");
        }catch(Exception e){

        }
        ScrollView view = new ScrollView(getActivity());
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setBackgroundResource(R.drawable.corners);
        layout.setOrientation(LinearLayout.VERTICAL);

        for(int i = 0; i<deleteKeys.size();i++) {
            TextView text = new TextView(getActivity());
            text.setTextSize(20);

            text.setText("" + deleteKeys.get(i) + "\n");
            text.setPadding(50,0,0,0);

            layout.addView(text);
        }
view.addView(layout);

        builder.setView(view);

        builder.setTitle(Message).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
           handler = new DatabaseHandler(getActivity());
                if(!deleteKeys.isEmpty()) {
                    for (int i = 0; i < deleteKeys.size(); i++) {
                        String[] tmp = deleteKeys.get(i).split(":");
                        handler.deleteWord(tmp[0]);
                    }

                    Log.d("DeleteConfirm", "Selected Words Deleted");
                }
                if(!Dictionary.equals("")){
                    Log.d("DeleteConfirm","NOW DELETING DICTIONARY" + Dictionary );
                    String[] tmp = Dictionary.split("=>");
                    handler.deleteDictionary(tmp[0]);
                    Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(getActivity(), "Deleted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
