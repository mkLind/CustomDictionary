package com.example.markus.customdictionary;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.*;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Markus on 3.4.2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "MultiDictionary";
    private static final String MULTI_WORDS_TABLE="Multi_Words";
    private static final String LANGUAGES_TABLE = "Languages";
    private static final String LANGUAGE_ID = "Language_id";
    private static final String KEY_ID= "Dictionary_id";
    private static final String KEY_LANGUAGE = "Language";
    private static final String KEY_FAMILIAR_LANGUAGE = "FamLang";
    private static final String KEY_WORD = "word";
    private static final String KEY_MEANING = "Meaning";
    private static final String KEY_WORD_FAMILIARITY = "familiarity";
    private static final String KEY_TIMES_DISPLAYED = "times_displayed";
    private static final String KEY_WORDLANGUAGE = "WordLanguage";
    private static final String KEY_CORRECT = "times_correct";
    private static final String KEY_WRONG = "times_wrong";

    private static final String KEY_DICT_FAMILIARITY = "familiarity_of_dictionary";
    private static final String KEY_TIME_USED = "time_trained";
    private static final String KEY_WORDS_TRAINED = "Amount_of_words_trained";
    private static final String KEY_DICTIONART = "Dictionary";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
// constructor for creating a table
 public void onCreate(SQLiteDatabase database){
 String CREATE_DICTIONARY_TABLE = "CREATE TABLE " + MULTI_WORDS_TABLE + "("
                                  + KEY_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                                  + KEY_WORD +  " TEXT, "
                                  + KEY_MEANING + " TEXT, "
                                  + KEY_WORD_FAMILIARITY + " INTEGER DEFAULT 0, "
                                  + KEY_TIMES_DISPLAYED + " INTEGER DEFAULT 0, "
                                  + KEY_CORRECT + " INTEGER DEFAULT 0, "
                                  + KEY_WRONG + " INTEGER DEFAULT 0, "
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
int initial_avg = getAvgFamiliarity(WordLanguage);
    SQLiteDatabase database = this.getWritableDatabase();
    // set values to be added
    ContentValues values = new ContentValues();
    values.put(KEY_WORD,word);
    values.put(KEY_MEANING,meaning);
    values.put(KEY_WORDLANGUAGE, WordLanguage);
    values.put(KEY_WORD_FAMILIARITY, initial_avg);
    values.put(KEY_TIMES_DISPLAYED, 0);
    // insert to database
    database.insert(MULTI_WORDS_TABLE, null, values);
    Log.d("addWord", "Word added to the database!");
    database.close();
}

public void changeFamiliarity(int change, String word){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db_write = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT familiarity FROM Multi_Words WHERE word = ? ",new String[]{word});
        int chng = change;
        Log.d("fam","CURSOR COUNT: " + cursor.getCount());

    if (cursor != null && cursor.getCount()>0) {
        cursor.moveToFirst();
        int familiarity;
        do{
            familiarity = cursor.getInt(0);
            Log.d("fam","FAMILIARITY: " + familiarity);
        }while(cursor.moveToNext());
        ContentValues values = new ContentValues();
        int new_value = familiarity + chng;
        Log.d("fam","new_familiarity: " + new_value);
        values.put(KEY_WORD_FAMILIARITY,new_value);


        db_write.update("Multi_Words",values,"word =?",new String[]{word});

    }
    db.close();
    db_write.close();

}

public void incrementTimesDisplayed(String word){
    SQLiteDatabase db = this.getReadableDatabase();
    SQLiteDatabase db_write = this.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT times_displayed FROM Multi_Words WHERE word = ?", new String[]{word});
    if(cursor != null && cursor.getCount() > 0){
        cursor.moveToFirst();
        int times_displayed;
        int new_times_displayed;
        do{
            times_displayed = cursor.getInt(0);
        }while(cursor.moveToNext());
        ContentValues values = new ContentValues();
        new_times_displayed = times_displayed + 1;
        values.put(KEY_TIMES_DISPLAYED, new_times_displayed);
        db_write.update("Multi_Words",values,"word =?",new String[]{word});
        db.close();
        db_write.close();
    }

}
    public void changeFamiliarity(boolean correct, String word){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db_write = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT familiarity, times_correct, times_wrong FROM Multi_Words WHERE word = ? ",new String[]{word});

        Log.d("fam","CURSOR COUNT: " + cursor.getCount());

        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            int familiarity;
            int familiarity_new;
            int correct_ans;
            int wrong;
            do{
                familiarity = cursor.getInt(0);
                correct_ans = cursor.getInt(1);
                wrong = cursor.getInt(2);

            }while(cursor.moveToNext());
            ContentValues values = new ContentValues();
            if(correct){
                correct_ans = correct_ans + 1;
                familiarity_new = familiarity + 1;
            }else{
                wrong = wrong + 1;
                familiarity_new = familiarity - 3;
            }
            values.put(KEY_WORD_FAMILIARITY,familiarity_new);
            values.put(KEY_CORRECT,correct_ans);
            values.put(KEY_WRONG,wrong);
            db_write.update("Multi_Words",values,"word =?",new String[]{word});

        }
        db.close();
        db_write.close();

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
    public ArrayList<String> getLanguagesWithStats(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> data = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM Languages",null);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            do{
                String dictionary = cursor.getString(1)+""+ "=>" +""+ cursor.getString(2);
                int avg_familiarity = getAvgFamiliarity(dictionary);
                String info = dictionary + "=>" + avg_familiarity;
                data.add(info);
            }while(cursor.moveToNext());

        }
        return data;
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

    public ArrayList<String> group_ByLanguage(String language, SortingType type){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> grouped = new ArrayList<String>();
        ArrayList<dictElement> element = new ArrayList<>();

        // Select word and meaning from the database with the language
        Cursor cursor = db.rawQuery("SELECT word, Meaning, familiarity, times_displayed FROM Multi_Words WHERE WordLanguage = ? ",new String[]{language});

        // if there are words in cursor, move to first result
        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            // Format all the words for display.
            do {
                String wordPair = cursor.getString(0) + "" + ":" + "" + cursor.getString(1);
                if(type == SortingType.FAMILIARITY ||type == SortingType.BY_TIMES_DISPLAYED ) {
                    int familiarity = cursor.getInt(2);
                    int timesDisplayed = cursor.getInt(3);
                    element.add(new dictElement(wordPair, familiarity, timesDisplayed, type));

                }else {
                    grouped.add(wordPair);
                }
            } while (cursor.moveToNext());
            // sort the words.
            if(type != SortingType.ALPHABETICALLY){
                Collections.sort(element);
            for(dictElement e : element){
                grouped.add(e.getEntry());
            }
            // Sort alphabetically
            }else{
            Collections.sort(grouped,String.CASE_INSENSITIVE_ORDER);
            }
            return grouped;
        }else{
            return grouped;
        }
    }

public int getAvgFamiliarity(String language){
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery("SELECT familiarity FROM Multi_Words WHERE WordLanguage = ? ",new String[]{language});
    int sum = 0;
    int avg = 0;
    int length = 0;
    if(cursor != null && cursor.getCount()>0) {
        cursor.moveToFirst();
        do {
            sum += cursor.getInt(0);
            length += 1;
        } while (cursor.moveToNext());
        avg = (int) sum / length;
    }
   return avg;
}

    public ArrayList<dictElement> groupByLanguage(String language, SortingType type){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> grouped = new ArrayList<String>();
        ArrayList<dictElement> element = new ArrayList<>();
        SortingType sorting = type;

        // Select word and meaning from the database with the language
        Cursor cursor = db.rawQuery("SELECT word, Meaning, familiarity, times_displayed FROM Multi_Words WHERE WordLanguage = ? ",new String[]{language});

        // if there are words in cursor, move to first result
        if(cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();

            // Format all the words for display.
            do {
                String wordPair = cursor.getString(0) + "" + ":" + "" + cursor.getString(1);
                int familiarity = cursor.getInt(2);
                int times_displayed = cursor.getInt(3);
                    element.add(new dictElement(wordPair, familiarity, times_displayed, sorting));


            } while (cursor.moveToNext());
                Collections.sort(element);


        }
        return element;
    }

}
