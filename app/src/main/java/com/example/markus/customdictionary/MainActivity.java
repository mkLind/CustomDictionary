package com.example.markus.customdictionary;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
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
public void importDictionaries(){
  Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
  intent.addCategory(Intent.CATEGORY_OPENABLE);
  intent.setType("application/octet-stream");
  startActivityForResult(intent,8787);


}

    public void parseInputs(String dictionary){
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

            ArrayList<String> wordsMeaning = handler.groupByLanguage(label);
            ArrayList<String> word1 = new ArrayList<String>(); // arrayList for control words

            for(int i = 0; i<wordsMeaning.size();i++){

                String[] tmp = wordsMeaning.get(i).split(":"); // separate the word and meaning.
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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.import_dictionaries){
        importDictionaries();
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case 8787:
                if(requestCode == 8787    && resultCode == Activity.RESULT_OK){
                    Uri uri = null;
                            if(data != null){
                                uri = data.getData();
                                try {
                                    InputStream stream = getContentResolver().openInputStream(uri);
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                                    StringBuilder builder = new StringBuilder();
                                    String line;
                                    while((line = reader.readLine())!= null){
                                        builder.append(line);


                                    }
                                    stream.close();
                                    reader.close();
                                    parseInputs(builder.toString());
                                }catch(IOException e){
                                    e.printStackTrace();
                                }


                            }
                }

                Toast.makeText(getApplicationContext(),"Export complete",Toast.LENGTH_SHORT).show();

        }
    }
}
