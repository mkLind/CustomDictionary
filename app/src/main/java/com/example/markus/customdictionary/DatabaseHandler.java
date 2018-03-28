package com.example.markus.customdictionary;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;
import android.widget.Toast;

import java.io.LineNumberReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Markus on 3.4.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "MultiDictionary";
    private static final String MULTI_WORDS_TABLE="Multi_Words";
    private static final String LANGUAGES_TABLE = "Languages";
    private static final String LANGUAGE_ID = "Language_id";
    private static final String KEY_ID= "Dictionary_id";
    private static final String KEY_LANGUAGE = "Language";
    private static final String KEY_FAMILIAR_LANGUAGE = "FamLang";
    private static final String KEY_WORD = "word";
    private static final String KEY_MEANING = "Meaning";
    private static final String KEY_WORDLANGUAGE = "WordLanguage";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
// constructor for creating a table
 public void onCreate(SQLiteDatabase database){
 String CREATE_DICTIONARY_TABLE = "CREATE TABLE " + MULTI_WORDS_TABLE + "("
                                  + KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                                  + KEY_WORD +  " TEXT, "
                                  + KEY_MEANING + " TEXT, "
                                  + KEY_WORDLANGUAGE + " TEXT "+")";

     String CREATE_LANGUAGES_TABLE = "CREATE TABLE " + LANGUAGES_TABLE + "("
                                      + LANGUAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                      + KEY_LANGUAGE + " TEXT, "
                                      + KEY_FAMILIAR_LANGUAGE + " TEXT " + ")";

     database.execSQL(CREATE_DICTIONARY_TABLE);
     database.execSQL(CREATE_LANGUAGES_TABLE);
     Log.d("On Create", "tables created");
 }

 // updates database. drops existing tables and creates new ones
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
     database.execSQL("DROP TABLE IF EXISTS MULTI_WORDS_TABLE");
        database.execSQL("DROP TABLE IF EXISTS LANGUAGES_TABLE");
        Log.d("On upgrade", "old tables dropped");
        this.onCreate(database);
    }
public void addWord(String word, String meaning, String WordLanguage){
Log.d("addWord","" + WordLanguage);

    SQLiteDatabase database = this.getWritableDatabase();
    // set values to be added
    ContentValues values = new ContentValues();
    values.put(KEY_WORD,word);
    values.put(KEY_MEANING,meaning);
    values.put(KEY_WORDLANGUAGE, WordLanguage);
    // insert to database
    database.insert(MULTI_WORDS_TABLE, null, values);
    Log.d("addWord", "Word added to the database!");
    database.close();
}


    public void addLanguage(String language, String FamLang){
        // add language to the database
        SQLiteDatabase databaseW = this.getWritableDatabase();


                ContentValues values = new ContentValues();
                values.put(KEY_LANGUAGE, language);
                values.put(KEY_FAMILIAR_LANGUAGE, FamLang);
                databaseW.insert(LANGUAGES_TABLE, null, values);

                Log.d("addLanguage", "Language added to the database!");

    }
    public ArrayList<String> getLanguages(){
        // readable database
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> Languages = new ArrayList<String>();
        // raw query forms Cursor object that contain the results of the query
        Cursor cursor = db.rawQuery("SELECT * FROM Languages",null);

        if(cursor != null && cursor.getCount()>0) {
            // if there is something in the cursor => move to first result
            cursor.moveToFirst();
            // Format dictionary name and add to Languages ArrayList
            do{
                String dictionary = cursor.getString(1)+""+ "=>" +""+ cursor.getString(2);
                Languages.add(dictionary);
            }while(cursor.moveToNext());

            return Languages;
        }else{
            return Languages;
        }

    }

    public ArrayList<String> getAllWords(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> words = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT Word, Meaning FROM Multi_Words", null);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                String word = cursor.getString(0)+ "" + ":" +""+cursor.getString(1);
                words.add(word);
            }
            while(cursor.moveToNext());

            Collections.sort(words,String.CASE_INSENSITIVE_ORDER);
            return words;
        }
                return words;
    }

    public void updateWord(String oldW, String oldM, String newW, String newM){
       SQLiteDatabase db = this.getWritableDatabase();
       // new word into content values
        ContentValues values = new ContentValues();

        values.put(KEY_WORD,newW);

        values.put(KEY_MEANING,newM);

        // update the database
        db.update("Multi_Words",values,"word =?",new String[]{oldW});
        db.close();
        Log.d("updateWord","Update successful!");

    }

    public void deleteWord(String word){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Multi_words", "word=?", new String[]{word});

        db.close();
    }
public void deleteDictionary(String language){
    SQLiteDatabase db = this.getWritableDatabase();
    try {

        db.delete("Languages", "Language=?", new String[]{language});
    }catch(Exception e){
        Log.d("DELETE DICTIONARY","OPERATION WAS NOT SUCCESFULL!");
    }
    db.close();
}

    public ArrayList<String> groupByLanguage(String language){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> grouped = new ArrayList<String>();
        // Select word and meaning from the database with the language
        Cursor cursor = db.rawQuery("SELECT word, Meaning FROM Multi_Words WHERE WordLanguage = ? ",new String[]{language});
        Log.d("groupByLanguage", "cursor formed!");

        // if there are words in cursor, move to first result
        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

// Format all the words for display.
            do {
                String wordPair = cursor.getString(0) + "" + ":" + "" + cursor.getString(1);
                grouped.add(wordPair);
            } while (cursor.moveToNext());
            // sort the words.
            Collections.sort(grouped,String.CASE_INSENSITIVE_ORDER);
            return grouped;
        }else{
            return grouped;
        }
    }


}
