package com.example.markus.customdictionary;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Markus on 19.6.2017.
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

    public trainingControl(String language, Context context){
    this.language = language;
        handler = new DatabaseHandler(context);
        words = new ArrayList<>();
        words = handler.groupByLanguage(language);
        questions = new ArrayList<>();
        failedQuestions = new ArrayList<>();

        CorrectAnswers = 0;
        decoys = new ArrayList<>();
    r = new Random();

    }

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
        Collections.shuffle(words);
        if (words.size() > 15) {
            for (int i = 0; i < 15; i++) {
                String[] dec = new String[3];
                int[] usedInd = new int[3];
                for (int j = 0; j < 3; j++) {
                    String decoy ="";
                    int e = r.nextInt(words.size());
                    if(i==e){

                        if(i<1) {
                            decoy = words.get(e + 1).split("=>")[1];
                        }else if(i == words.size()-1){
                            decoy = words.get(e - 1).split("=>")[1];
                        }

                    }else{
                        decoy = words.get(e).split("=>")[1];
                    }


                    dec[j] = decoy;


                }

                String[] tmp = words.get(i).split("=>");
                Question q = new Question(tmp[0], tmp[1], dec);
                questions.add(q);
            }
        }

        // forming a question when words list is less than 15
        else if (words.size() >= 4 && words.size() < 15) {
            for (int i = 0; i < words.size(); i++) {
                String[] dec = new String[3];

                for (int j = 0; j < 3; j++) {
                    String decoy ="";
                    int e = r.nextInt(words.size());
                  if(i==e){

                      if(i<1) {
                          decoy = words.get(e + 1).split("=>")[1];
                      }else if(i == words.size()-1){
                          decoy = words.get(e - 1).split("=>")[1];
                      }

                  }else{
                      decoy = words.get(e).split("=>")[1];
                  }






                        dec[j] = decoy;



                    String[] tmp = words.get(i).split("=>");
                    Question q = new Question(tmp[0], tmp[1], dec);
                    questions.add(q);
                }
            }

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


