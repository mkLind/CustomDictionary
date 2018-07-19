package com.example.markus.customdictionary;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class MainActivity extends ActionBarActivity {

private DatabaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        handler = new DatabaseHandler(getApplicationContext());

        setContentView(R.layout.activity_main);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void addDictionary(View view){ // has to have a View as parameter(The on that the user interacted with)
      AddDictDialog dialog = new AddDictDialog();
        FragmentManager manager = getFragmentManager();

        dialog.show(manager,"");

    }
public void addWord(View view){
    Chooser dialog = new Chooser();
    FragmentManager manager = getFragmentManager();

    dialog.show(manager,"");
}
    public void groupBy(View view){
        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> test =  handler.getLanguages();
        Log.d("groupBy","Checking the amount of languages!");
        if(test.size()>=1) {
            Log.d("groupBy","one or more languages detected");
            groupByDialog dialog = new groupByDialog();
            FragmentManager manager = getFragmentManager();
            dialog.show(manager, "");
        }else{

            Toast.makeText(getApplicationContext(),"No words or dictionaries yet!", Toast.LENGTH_SHORT).show();
        }
    }

    public void useDictionary(View view){
        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> test = handler.getLanguages();
        if(test.size()>=1) {
            Intent intent = new Intent(getApplicationContext(), Dictionary_use.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"No words yet!", Toast.LENGTH_SHORT).show();
        }
    }
    public void startTraining(View view){
        DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
        ArrayList<String> test =  handler.getLanguages();
        if(test.size()>=1) {

            trainingOptions opt = new trainingOptions();
            FragmentManager manager = getFragmentManager();
            opt.show(manager, "");
        }else{
            Toast.makeText(getApplicationContext(),"Dictionary is empty!", Toast.LENGTH_SHORT).show();
        }
    }
public void importDictionaries(Context context){
        File path = context.getFilesDir();
        File file = new File(path,"Dictionary.txt");
        try{
            int length = (int) file.length();
            byte[] bytes = new byte[length];
            FileInputStream in = new FileInputStream(file);
            in.read(bytes);
           String dict = new String(bytes);
           parseInputs(dict);

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"Import failed", Toast.LENGTH_SHORT).show();
        }

}

    public void parseInputs(String dictionary){
        // Extract the whole external dictionary from the intent


        // Ensure that the added dictionary in fact is properly formatted dictionary
        // Check that the dictionary contains "||" separating the dictionary entries, ":" separating the word and a meaning, "=>" for separating the two languages of the dictionary
        // and # for separating the individual entries of a dictionary

        if((dictionary.contains("||") && dictionary.contains("=>") && dictionary.contains(":")) && dictionary.contains("#")) {

            StringTokenizer token1 = new StringTokenizer(dictionary, "||"); // Separate dictionary name from entries

            String[] dictWords = new String[2]; // table for tokenized data
            dictWords[0] = token1.nextToken(); // dictionary name
            dictWords[1] = token1.nextToken(); // Entries



            StringTokenizer token2 = new StringTokenizer(dictWords[1], "#"); // Separate entries from one another

            ArrayList<String> languages = handler.getLanguages(); // get all languages from the database

            // if external dictionary is not in the database, create the dictionary here
            if (!languages.contains(dictWords[0])) {
                // Separate source and target language from each other
                StringTokenizer lang = new StringTokenizer(dictWords[0], "=>");

                String language = lang.nextToken(); // source language
                String famlang = lang.nextToken(); // target language

                handler.addLanguage(language, famlang); // create dictionary in the database
            }


            // Load existing words from the database based on given dictionary name

            ArrayList<String> wordsMeaning = handler.groupByLanguage(dictWords[0]);
            ArrayList<String> word1 = new ArrayList<String>(); // arrayList for control words

            for(int i = 0; i<wordsMeaning.size();i++){

                String[] tmp = wordsMeaning.get(i).split(":"); // separate the word and meaning.
                word1.add(tmp[0]); // Add only the foreign word for control list
            }

            // Go through each entry of the external dictionary
            for (int i = 0; i < token2.countTokens(); i++) {
                StringTokenizer wordmean = new StringTokenizer(token2.nextToken(), ":"); // separate words and meanings
                String word = wordmean.nextToken();

                String meaning = wordmean.nextToken();

                if(!word1.contains(word)) {// if the foreign word of the entry is not in the already existing set of words in the database, add the new entry
                    handler.addWord(word, meaning, dictWords[0]);
                }

            }

            Toast.makeText(getApplicationContext(), "External dictionary added", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "File was not properly formatted for this application", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.import_dictionaries){
        importDictionaries(getApplicationContext());
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
