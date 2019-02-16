package com.example.markus.customdictionary;

import android.app.ActionBar;
import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class AddWordActivity extends AppCompatActivity {
    private ArrayList<String> languages;
    private DatabaseHandler handler;
    private int indicator;
    private boolean addMultiple;
    private EditText meaning;
    private EditText word;

    private String action;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The application supports adding properly formatted dictionary files from outside of the application.
        // The following code handles the intent of adding external dictionary.
        indicator = 0;
        Intent intent = getIntent();// Fetch the intent directed to the activity and the action that is related to it.
        action = intent.getAction();

        type = intent.getType();
        handler = new DatabaseHandler(getApplicationContext());
        // The adding of new word requires the knowledge to which language pair the new word belongs. This fetches the language specified in the add Word Dialog.
        languages = getIntent().getExtras().getStringArrayList("Dictionaries");
        if(languages.size() == 1){
            getSupportActionBar().setTitle(languages.get(indicator));
            addMultiple = false;
        }else{
            addMultiple = true;
            getSupportActionBar().setTitle( 1 + "/" + languages.size() + ": " + languages.get(indicator));
        }




        setContentView(R.layout.activity_add_word); // set the layout file of the application
        if(Intent.ACTION_SEND.equals(action) && type != null){ // check that the action is send and type is not null
        if("application/octet-stream".equals(type)){ // if the type is text or plain => we handle the external dictionary

            // Handle the external dictionary
            handleInputs(intent);
        }
        }
        // map edit texts of layout to code representations
        meaning = (EditText) findViewById(R.id.meaning_field);
        meaning.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meaning.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);
        word = (EditText) findViewById(R.id.word_field);
        // set editor action so that when user presses the add button on the keyboard, the word is added.

        meaning.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Log.d("Keyboard action","Pressed done");
                    if(addMultiple) {
                        addWord(languages.get(indicator));
                    }else{
                        addWord(languages.get(0));
                    }
                    handled = true;
                }
                if(indicator != languages.size()-1 && addMultiple) {

                    int displayed = indicator + 1;
                        getSupportActionBar().setTitle(displayed + "/" + languages.size() + ": " + languages.get(indicator));

                }else{

                        getSupportActionBar().setTitle(languages.get(0));
                }
                word.requestFocus();
                Log.d("INDICATOR:","" + indicator);
                Log.d("Languages size","" + (languages.size()));
                indicator += 1;
                if(indicator == languages.size() && addMultiple ){
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                    return handled;
                }

                return handled;
            }



        });



    }

    public void handleInputs(Intent intent){
        // Extract the whole external dictionary from the intent

        String dictionary = intent.getStringExtra(Intent.EXTRA_TEXT);
      // Ensure that the added dictionary in fact is properly formatted dictionary
        // Check that the dictionary contains "||" separating the dictionary entries, ":" separating the word and a meaning, "=>" for separating the two languages of the dictionary
        // and # for separating the individual entries of a dictionary


        // Extract the whole external dictionary from the intent
        String label = "";
        String word = "";
        String meaning = "";
        try{
            JSONObject dict= new JSONObject(dictionary);
            label = dict.keys().next();

            JSONArray array = dict.getJSONArray(label);
            // if external dictionary is not in the database, create the dictionary here
            ArrayList<String> languages = handler.getLanguages();
            if (!languages.contains(label)) {
                // Separate source and target language from each other
                StringTokenizer lang = new StringTokenizer(label, "=>");

                String language = lang.nextToken(); // source language
                String famlang = lang.nextToken(); // target language

                handler.addLanguage(language, famlang); // create dictionary in the database
            }
            // Load existing words from the database based on given dictionary name

            ArrayList<dictElement> wordsMeaning = handler.groupByLanguage(label,false);
            ArrayList<String> word1 = new ArrayList<String>(); // arrayList for control words

            for(int i = 0; i<wordsMeaning.size();i++){

                String[] tmp = wordsMeaning.get(i).getEntry().split(":"); // separate the word and meaning.
                word1.add(tmp[0]); // Add only the foreign word for control list
            }


            for(int i = 0; i<array.length();i++) {
                JSONObject entry = array.getJSONObject(i);

                word = entry.keys().next();
                meaning = entry.getString(word);

                if(!word1.contains(word)) {// if the foreign word of the entry is not in the already existing set of words in the database, add the new entry
                    handler.addWord(word, meaning, label);
                }

            }
            Toast.makeText(getApplicationContext(), "External dictionary added", Toast.LENGTH_SHORT).show();
        }catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Problems in import", Toast.LENGTH_LONG).show();
        }


        this.finish();
    }
public void addWord(String language){

    ArrayList<dictElement> wordsMeaning = handler.groupByLanguage(language,false);
    ArrayList<String> word1 = new ArrayList<String>();
    ArrayList<String> mean1 = new ArrayList<>();


for(int i = 0; i<wordsMeaning.size();i++){
    String[] tmp = wordsMeaning.get(i).getEntry().split(":");
    word1.add(tmp[0].trim());
    mean1.add(tmp[1].trim());
}

    // word and meaning need to be prepared
    if(!word.getText().toString().equals("")&&!meaning.getText().toString().equals("")) {

        if(!word1.contains(word.getText().toString().trim())&& !mean1.contains(meaning.getText().toString().trim())) {

            handler.addWord(word.getText().toString().trim(), meaning.getText().toString().trim(), language);

            Toast.makeText(getApplicationContext(), "Word added!", Toast.LENGTH_SHORT).show();
            word.setText("");
            meaning.setText("");

        }else{
            Toast.makeText(getApplicationContext(),"Word is already in the dictionary!",Toast.LENGTH_SHORT).show();
            word.setText("");
            meaning.setText("");
        }

    }else{
        Toast.makeText(getApplicationContext(),"Please ensure that you have provided a word and a meaning!",Toast.LENGTH_SHORT).show();
    }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_word, menu);
        return true;
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

}
