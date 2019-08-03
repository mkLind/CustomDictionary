package com.example.markus.customdictionary;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Markus on 19.6.2017.
 * Helper class for setting up the training sesstion
 */

public class trainingControl {
    private DatabaseHandler handler;
    private ArrayList<Question> questions;
    private ArrayList<Question> failedQuestions;
    private ArrayList<String> words;
    private int CorrectAnswers;
    private String language;
    private ArrayList<String[]> decoys;
    private Random r;
    private String length;
    private SortingType type;

    public trainingControl(String language, String length, SortingType type,Context context){
    this.language = language;
        handler = new DatabaseHandler(context);
        words = new ArrayList<>();
        this.type = type;
        // Get all words for setup.

        words = handler.group_ByLanguage(language, type);
        questions = new ArrayList<>();
        failedQuestions = new ArrayList<>();
        this.length = length;


        CorrectAnswers = 0;
        decoys = new ArrayList<>();
        r = new Random();

    }
// at least four entries need to be in the dictionary for it to be trainable.
    public boolean isTrainable(){
        Log.d("isTrainable","if the vocabulary is trainable");
        if(words.size()>=4){
            return true;
        }else{
            return false;
        }
    }


    public void setUpTraining() {
        Log.d("setUpTraining", "on top");
        if(type != SortingType.FAMILIARITY) {
            Collections.shuffle(words); // mix the words
        }

        int maxQuestions =(int) Math.floor(Integer.parseInt(length));
            // generate max amount of questions
            // i = index for correct answer e for decoys
            for (int i = 0; i < maxQuestions; i++) {
                String[] dec = new String[3];

                HashMap<String, Integer> usedDecoys = new HashMap<>();
                for (int j = 0; j < 3; j++) {
                    String decoy ="";



                    // ensure that there will not be the right answer in the decoys
                    boolean newDecoy = false;
                    while(!newDecoy) {
                     int e = r.nextInt(words.size());
                     // i is index of the right answer, e is for the decoy
                        if (i != e) {
                            if (!usedDecoys.containsKey(words.get(e).split(":")[1])) {
                                decoy = words.get(e).split(":")[1];
                                usedDecoys.put(decoy, e);
                                newDecoy = true;
                            }
                        }
                    }



                    dec[j] = decoy; // Set decoy to dec array


                }
                // Generate question.
                String[] tmp = words.get(i).split(":");
                Question q = new Question(tmp[0], tmp[1], dec);
                questions.add(q);
            }



    }


public void addFailed(Question failed){
    failedQuestions.add(failed);
}

    public Question getOneQuestion(int index){
        return questions.get(index);
    }

    public int amountOfQuestions(){
        return questions.size();
    }

    public DatabaseHandler getHandler() {
        return handler;
    }

    public void setHandler(DatabaseHandler handler) {
        this.handler = handler;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public int getCorrectAnswers() {
        return CorrectAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        CorrectAnswers = correctAnswers;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<String[]> getDecoys() {
        return decoys;
    }

    public void setDecoys(ArrayList<String[]> decoys) {
        this.decoys = decoys;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    public void controlAnswer(int index, String attempt){
        if(questions.get(index).checkAnswer(attempt)){
            int tmp = getCorrectAnswers();
            tmp++;
            setCorrectAnswers(tmp);
        }else{
            failedQuestions.add(questions.get(index));
        }
    }

    public ArrayList<Question> getFailedQuestions() {
        return failedQuestions;
    }

    public void setFailedQuestions(ArrayList<Question> failedQuestions) {
        this.failedQuestions = failedQuestions;
    }
}


