package com.example.markus.customdictionary;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class AddWordActivity extends AppCompatActivity {

    private ArrayList<String> languages;
    private DatabaseHandler handler;
    private int indicator;
    private boolean addMultiple;
    private EditText meaning;
    private EditText word;
    private static final int PICK_IMAGE = 8585;
    private RelativeLayout rl;
    private Bitmap imageToStore;
    private Boolean imageInput;

    private ImageButton addImage;
    private Button imageSave;
    private Button imageRemove;
    private ImageView potentialImage;
    private String action;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The application supports adding properly formatted dictionary files from outside of the application.
        // The following code handles the intent of adding external dictionary.

        setContentView(R.layout.activity_add_word); // set the layout file of the application
        indicator = 0;
        Intent intent = getIntent();// Fetch the intent directed to the activity and the action that is related to it.
        action = intent.getAction();
        rl = findViewById(R.id.addWord);
        type = intent.getType();
        potentialImage = findViewById(R.id.potential_image);
        imageSave = findViewById(R.id.imageSave);
        imageRemove = findViewById(R.id.remove_image);


        imageInput = false;
        imageToStore = null;
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



        if(Intent.ACTION_SEND.equals(action) && type != null){ // check that the action is send and type is not null
        if("application/octet-stream".equals(type)){ // if the type is text or plain => we handle the external dictionary

            // Handle the external dictionary
            handleInputs(intent);
        }
        }
        // map edit texts of layout to code representations
        meaning =  findViewById(R.id.meaning_field);
        meaning.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meaning.setRawInputType(EditorInfo.TYPE_CLASS_TEXT);
        word =  findViewById(R.id.word_field);

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
        addImage = findViewById(R.id.imageAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchAndDisplay(v);
            }
        });
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWord(languages.get(0));
            }
        });
        imageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meaning.setVisibility(View.VISIBLE);
                imageSave.setVisibility(View.INVISIBLE);
                imageRemove.setVisibility(View.INVISIBLE);
                potentialImage.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void searchAndDisplay(View view){
        imageInput = true;
        // search image for base 64 conversion.
        Intent intent = new Intent(Intent.ACTION_PICK);
        // SET TYPE TO BE IMAGE
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);



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

    // word and meaning need to be prepared. If adding an image, jump to else.
    if(!word.getText().toString().equals("")&&!meaning.getText().toString().equals("") && !imageInput) {

        if(!word1.contains(word.getText().toString().trim())&& !mean1.contains(meaning.getText().toString().trim())) {

            handler.addWord(word.getText().toString().trim(), meaning.getText().toString().trim(), language);

            Toast.makeText(getApplicationContext(), "Entry added!", Toast.LENGTH_SHORT).show();
            word.setText("");
            meaning.setText("");

        }else{
            Toast.makeText(getApplicationContext(),"This is already in the dictionary!",Toast.LENGTH_SHORT).show();
            word.setText("");
            meaning.setText("");
        }

    }else{
        if(!word.getText().toString().equals("") && imageToStore!= null){
            if(!word1.contains(word.getText().toString().trim())){
                String encodedImage = encodeImage(imageToStore);
                handler.addWord(word.getText().toString().trim() + "<base64>", encodedImage, language);
                word.setText("");
                imageToStore = null;
                Toast.makeText(getApplicationContext(),"Picture saved!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"This is already in the dictionary!",Toast.LENGTH_SHORT).show();
                word.setText("");
                imageToStore = null;
            }
        }else {

            Toast.makeText(getApplicationContext(), "Please ensure that you have provided a word and a meaning or selected an image!", Toast.LENGTH_SHORT).show();
        }
    }
    imageInput = false;
    meaning.setVisibility(View.VISIBLE);
    potentialImage.setVisibility(View.INVISIBLE);
    imageSave.setVisibility(View.INVISIBLE);
}

public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Use switch case to determine which activity result to process
        switch(requestCode){
            // If Intent was to open an image, open it here.
            case PICK_IMAGE:
                if(resultCode == Activity.RESULT_OK){
                    Uri image = null;
                    if(data != null){
                        image = data.getData();
                        try{
                       Bitmap tmp = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                       int targetWidth = 0;
                       int targetHeight = 0;

                        if(tmp.getWidth() > 240){
                            targetWidth = 240;

                        }else{
                            targetWidth = tmp.getWidth();
                        }

                            if(tmp.getHeight() > 450){
                                targetHeight = 450;

                            }else{
                                targetHeight = tmp.getHeight();
                            }
                        imageToStore = Bitmap.createScaledBitmap(tmp, targetWidth, targetHeight, true);
                        displayImage(imageToStore);
                        }catch(IOException e){
                            e.printStackTrace();
                        }

                    }

                }

        }
}
public String encodeImage(Bitmap btm){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] bytes = baos.toByteArray();
    String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
    return encoded;

}

public void displayImage( Bitmap btm){

        potentialImage.setImageBitmap(btm);
        meaning.setVisibility(View.INVISIBLE);
        imageSave.setVisibility(View.VISIBLE);
        imageRemove.setVisibility(View.VISIBLE);


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
