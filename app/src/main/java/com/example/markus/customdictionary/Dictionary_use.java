package com.example.markus.customdictionary;

import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;


public class Dictionary_use extends ActionBarActivity {
private DatabaseHandler handler;
    private EditText tview;
    private RadioGroup meanings;
    private String[] wrds0;
    private String[] means0;
    private String[] wrds;
    private String[] means;
    private String[] all;


    private ArrayList<Button> foundWords = new ArrayList<Button>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_use);

        handler = new DatabaseHandler(getApplicationContext());
        tview = (EditText) findViewById(R.id.wordToSearch);
     meanings = (RadioGroup) findViewById(R.id.search_history);

        ArrayList<String> compWords = handler.getAllWords();

        wrds = new String[compWords.size()];
        means = new String[compWords.size()];


        for(int i = 0; i<compWords.size();i++){

           String[] tmp = compWords.get(i).split("=>");
           wrds[i] = tmp[0];
            means[i] = tmp[1];

        }
        tview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override

            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
             if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                 searchWord();
                 handled = true;

             }
                return handled;

            }
        });

/*
     all = new String[wrds.length +means.length];
        //merge words and meanings to one array
        System.arraycopy(wrds,0,all,0,wrds.length); // source array/start insource/ dest array/start pos of data in dest/ num. of elements to be copied
        System.arraycopy(means,0,all,wrds.length,means.length);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, all);

        */



    }





public void searchWord(){
EditText input = (EditText) findViewById(R.id.wordToSearch);

   final String word = input.getText().toString().trim();


    boolean found = false;
    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    if(!found) {
        for (int i = 0; i < wrds.length; i++) {


            if (wrds[i].contains(word)) {
                if(!containsDuplicates(wrds,word).isEmpty()){


                    ArrayList<Integer> ind = containsDuplicates(wrds,word);
                    String result = word + ":";
                    for(int j = 0; j<ind.size();j++){
                        Button meaning = new Button(getApplicationContext());
                        meaning.setTextColor(Color.BLACK);

                        meaning.setText(result + means[ind.get(j)]);

                        meaning.setLayoutParams(para);
                        meaning.setBackground(getResources().getDrawable(R.drawable.corners));
                        foundWords.add(0, meaning);
                        meanings.addView(meaning, 0);
                    }

                    input.setText("");


                    found = true;
                    break;
                }else {
                    Button meaning = new Button(getApplicationContext());
                    meaning.setText(word + ": " + means[i]);
                    Log.d("SEARCHING ENTRY 2", "word + menaing: " + word + ": " + wrds[i]);
                    meaning.setLayoutParams(para);
                    meaning.setBackground(getResources().getDrawable(R.drawable.corners));
                    meaning.setTextColor(Color.BLACK);
                    input.setText("");

                    foundWords.add(0, meaning);
                    meanings.addView(meaning, 0);
                    found = true;
                    break;
                }
            }
        }
    }
    if(!found) {
        for (int i = 0; i < wrds.length; i++) {
            if (means[i].contains(word)) {
                if(!containsDuplicates(means,word).isEmpty()){

                    ArrayList<Integer> ind = containsDuplicates(means,word);
                    String result = word + ":";
                    for(int j = 0; j<ind.size();j++){

                        Button meaning = new Button(getApplicationContext());
                        meaning.setText(result + wrds[ind.get(j)]);

                        meaning.setLayoutParams(para);
                        meaning.setBackground(getResources().getDrawable(R.drawable.corners));
                        meaning.setTextColor(Color.BLACK);
                        foundWords.add(0,meaning);
                        meanings.addView(meaning,0);

                    }

                    input.setText("");


                    found = true;
                    break;

                }else {
                    Button meaning = new Button(getApplicationContext());
                    meaning.setText(word + ": " + wrds[i]);
                    Log.d("SEARCHING ENTRY 2", "word + menaing: " + word + ": " + wrds[i]);
                    meaning.setLayoutParams(para);
                    meaning.setBackground(getResources().getDrawable(R.drawable.corners));
                    meaning.setTextColor(Color.BLACK);
                    input.setText("");

                    foundWords.add(0, meaning);
                    meanings.addView(meaning, 0);
                    found = true;
                    break;
                }
            }
        }
    }
if(found == false && !word.equals("")){

    Toast.makeText(getApplicationContext(), "The word searched was no in the dictionary.", Toast.LENGTH_LONG).show();




}
}
    public void toMainMenu(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dictionary_use, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    public ArrayList<Integer> containsDuplicates(String[] array, String word){
    ArrayList<Integer> indexes = new ArrayList<Integer>();

        for(int i = 0; i<array.length;i++){
            if(array[i].equals(word.trim())){
               indexes.add(i);

            }
        }
        if(indexes.size()>1) {
            return indexes;
        }else{
            return new ArrayList<Integer>();
        }
    }
}
