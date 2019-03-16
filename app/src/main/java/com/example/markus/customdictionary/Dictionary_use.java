package com.example.markus.customdictionary;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class Dictionary_use extends AppCompatActivity {
private DatabaseHandler handler;
    private AutoCompleteTextView tview;
    private RadioGroup meanings;
    private String[] wrds0;
    private String[] means0;
    private String[] wrds;
    private String[] means;
    private String[] all;
    private HashMap<String, Bitmap> imgs;

    private ArrayList<Button> foundWords = new ArrayList<Button>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_use);
        imgs = new HashMap<>();
        handler = new DatabaseHandler(getApplicationContext());
        tview = (AutoCompleteTextView) findViewById(R.id.wordToSearch);
         meanings = (RadioGroup) findViewById(R.id.search_history);

        ArrayList<String> compWords = handler.getAllWords();
        // different list for words and meanings
        wrds = new String[compWords.size()];
        means = new String[compWords.size()];
        all = new String[compWords.size()*2];

        // Separate the fetched entries  to different tables
        int all_ind = 0;
        for(int i = 0; i<compWords.size();i++){

           String[] tmp = compWords.get(i).split(":");
           if(!tmp[0].contains("<base64>")) {
               wrds[i] = tmp[0];
               means[i] = tmp[1];
               all[all_ind] = tmp[0];
               all[all_ind + 1] = tmp[1];
               all_ind += 2;
           }else{
               String key = tmp[0].split("<")[0];
               byte[] decodedString = Base64.decode(tmp[1], Base64.DEFAULT);
               Bitmap map = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
               imgs.put(key, map);
               wrds[i] = key;
               means[i] ="" + i;
               all[all_ind] = key;
               all[all_ind + 1] = "" + i;
               all_ind += 2;
           }
        }

        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, all);
        tview.setAdapter(adapt);

        // When the user clicks search from keypad, conduct search
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





    }





public void searchWord(){
    // Fetch the word for the search
    EditText input = (EditText) findViewById(R.id.wordToSearch);
   final String word = input.getText().toString().trim();


    boolean found = false;
    LinearLayout.LayoutParams para = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    //handle base 64 first
    if(!found){

        if(!imgs.isEmpty()){

         if(imgs.containsKey(word)){
             LinearLayout layout = new LinearLayout(getApplicationContext());
             layout.setLayoutParams(para);
             Button label = new Button(getApplicationContext());
             ImageButton image = new ImageButton(getApplicationContext());
             label.setText(word);
             image.setImageBitmap(imgs.get(word));
             label.setLayoutParams(para);
             label.setBackground(getResources().getDrawable(R.drawable.corners));
             image.setLayoutParams(para);
             image.setBackground(getResources().getDrawable(R.drawable.corners));
             meanings.addView(image,0);
             meanings.addView(label,0);
             foundWords.add(0,label);
             found = true;
         }
        }
    }

    if(!found) {
        for (int i = 0; i < wrds.length; i++) {

            // search the word from list of foreign words
            if (wrds[i].equals(word)) {
                // if the word to be searched exists within means index i string, check if there are multiple meanings
                if(!containsMultiples(wrds,word).isEmpty()){

                    // if multiples exist, fetch their indexes
                    ArrayList<Integer> ind = containsMultiples(wrds,word);
                    String result = word + ":";

                    for(int j = 0; j<ind.size();j++){
                        // add buttton for each separate meaning
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
                    // if no duplicates, show one found entry
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
        //Search the word from list of familiar words
        for (int i = 0; i < wrds.length; i++) {
            // if the word to be searched exists within means index i string, check if there are multiple meanings
            if (means[i].equals(word)) {
                if(!containsMultiples(means,word).isEmpty()){
                    // if multiples exist, fetch their indexes
                    ArrayList<Integer> ind = containsMultiples(means,word);
                    String result = word + ":";
                    for(int j = 0; j<ind.size();j++){
                        // add buttton for each separate meaning
                        Button meaning = new Button(getApplicationContext());
                        meaning.setText(result + wrds[ind.get(j)]);

                        meaning.setLayoutParams(para);
                        meaning.setBackground(getResources().getDrawable(R.drawable.corners));
                        meaning.setTextColor(Color.BLACK);
                        // add found meaning to the foundWords array list.
                        foundWords.add(0,meaning);
                        // add the button to the view
                        meanings.addView(meaning,0);

                    }
                // clear input field
                    input.setText("");

                    // indicate found is true.
                    found = true;
                    break;
                // if no duplicates can be found, display the one found entry.
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

Toast.makeText(getApplicationContext(),"The word searched was not in the dictionary",Toast.LENGTH_LONG).show();




}
}
    public void toMainMenu(View view){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    }

    public boolean checkIfBase64(String data){
        boolean isEncoded = false;
        if(!data.equals("")){
            try{
                Base64.decode(data, Base64.DEFAULT);
                isEncoded = true;

            }catch(Exception e){
                isEncoded = false;
            }
        }
        return isEncoded;
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
    // method for finding out how many times a string exists in an array

    public ArrayList<Integer> containsMultiples(String[] array, String word){
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
