package com.example.markus.customdictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by Markus on 29.4.2015.
 */
public class modifyWords extends DialogFragment {

    private DatabaseHandler handler;


    private EditText newMeaning;
    private EditText newWord;
    private String selection;
    private static final int SCROLL_ID = 0;
    private static final int WORD_ID = 1;


    public Dialog onCreateDialog(Bundle savedInstanceState){
        final LinearLayout layout = new LinearLayout(getActivity());
        final TableLayout base = new TableLayout(getActivity());
        final LinearLayout textFields = new LinearLayout(getActivity());
        newWord = new EditText(getActivity());
        newWord.setBackground(getResources().getDrawable(R.drawable.textfield_border));
        final ScrollView sview = new ScrollView(getActivity());


        newMeaning = new EditText(getActivity());
        newMeaning.setBackgroundColor(Color.BLUE);
        newMeaning.setBackground(getResources().getDrawable(R.drawable.textfield_border));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        newWord.setHint("Corrected foreign word");
        newMeaning.setHint("Corrected familiar word");
        layout.setOrientation(LinearLayout.VERTICAL);

        textFields.setOrientation(LinearLayout.VERTICAL);
        handler = new DatabaseHandler(getActivity());




     final RadioGroup group = new RadioGroup(getActivity());
group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
       RadioButton tmp = (RadioButton)group.getChildAt(checkedId);
        String[] word = tmp.getText().toString().split("=>");
        newWord.setText(word[0]);
        newMeaning.setText(word[1]);

      sview.scrollTo(0,sview.getBottom());
    }
});

ArrayList<String> allW = handler.getAllWords();
        if(allW.size()>0){
            for(int i = 0;i<allW.size();i++){
                RadioButton box = new RadioButton(getActivity());
         box.setId(i);
               box.setBackgroundColor(Color.parseColor("#6AE4FC"));
                box.setText(allW.get(i));
                box.setTextSize(20f);
                box.setWidth(1000);
                box.setBackground(getResources().getDrawable(R.drawable.radiogroupdrawable));

                group.addView(box);
            }
            layout.addView(group);

        }else{
            Toast.makeText(getActivity(),"No words to modify",Toast.LENGTH_SHORT).show();
        }

        layout.addView(newWord,0);
      layout.addView(newMeaning,1);

        sview.addView(layout);




builder.setView(sview);
     ;

        builder.setTitle("Word modification").setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (group.getCheckedRadioButtonId() != -1 && !newWord.getText().toString().equals("") && !newMeaning.getText().toString().equals("") ) {
                    int id1 = group.getCheckedRadioButtonId();

                   for(int i = 0; i<group.getChildCount();i++){
                       RadioButton b = (RadioButton) group.getChildAt(i);
                       if(id1 == b.getId()){
                           selection = b.getText().toString();
                       }
                   }
                   String[] oldies = selection.split("=>");
                    handler = new DatabaseHandler(getActivity());
                    handler.updateWord(oldies[0], oldies[1], newWord.getText().toString(), newMeaning.getText().toString());

                    Toast.makeText(getActivity(),"Word updated!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Please select a word to update and provide new word and meaning for it!", Toast.LENGTH_LONG).show();
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
