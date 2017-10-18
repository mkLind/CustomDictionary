package com.example.markus.customdictionary;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;


public class AddWordActivity extends ActionBarActivity {
    private String language;
    private DatabaseHandler handler;
    EditText meaning;
    EditText word;

    private String action;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        action = intent.getAction();
        type = intent.getType();
        handler = new DatabaseHandler(getApplicationContext());
        setContentView(R.layout.activity_add_word);
        if(Intent.ACTION_SEND.equals(action) && type != null){
        if("text/plain".equals(type)){
            handleInputs(intent);
        }
        }
        meaning = (EditText) findViewById(R.id.meaning_field);
        word = (EditText) findViewById(R.id.word_field);

        meaning.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    Log.d("Keyboard action","Pressed done");
                    addWord();
                    handled = true;
                }
                return handled;
            }



        });


       language = intent.getStringExtra(AddWordDialog.EXTRA_LANGUAGE);


    }

    public void handleInputs(Intent intent){
        String dictionary = intent.getStringExtra(Intent.EXTRA_TEXT);
        if((dictionary.contains("||") && dictionary.contains("=>") && dictionary.contains(":")) && dictionary.contains("#")) {

            StringTokenizer token1 = new StringTokenizer(dictionary, "||"); // Separate dicionary name from words
            String[] dictWords = new String[2];
            dictWords[0] = token1.nextToken();
            dictWords[1] = token1.nextToken();



            StringTokenizer token2 = new StringTokenizer(dictWords[1], "#"); // separate word pairse

            ArrayList<String> languages = handler.getLanguages();


            if (!languages.contains(dictWords[0])) {
                StringTokenizer lang = new StringTokenizer(dictWords[0], "=>");
                String language = lang.nextToken();
                String famlang = lang.nextToken();
                handler.addLanguage(language, famlang);
            }
// Load existing words from the base

            ArrayList<String> wordsMeaning = handler.groupByLanguage(dictWords[0]);
            ArrayList<String> word1 = new ArrayList<String>();
            for(int i = 0; i<wordsMeaning.size();i++){
                String[] tmp = wordsMeaning.get(i).split("=>");
                word1.add(tmp[0]);
            }


            for (int i = 0; i < token2.countTokens(); i++) {
                StringTokenizer wordmean = new StringTokenizer(token2.nextToken(), ":"); // separate words and meanings
                String word = wordmean.nextToken();
                String meaning = wordmean.nextToken();
                Log.d("Word to be added","Word: " + word + " Meaning: " + meaning);
            if(!word1.contains(word)) {
                handler.addWord(word, meaning, dictWords[0]);
            }
            }

            Toast.makeText(getApplicationContext(), "External dictionary added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "File was not properly formatted for this application", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
public void addWord(){

    ArrayList<String> wordsMeaning = handler.groupByLanguage(language);
    ArrayList<String> word1 = new ArrayList<String>();
    ArrayList<String> mean1 = new ArrayList<>();
for(int i = 0; i<wordsMeaning.size();i++){
    String[] tmp = wordsMeaning.get(i).split("=>");
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
