package com.example.markus.customdictionary;

import android.content.Context;
import android.provider.ContactsContract;

/**
 * Created by Markus on 19.6.2017.
 * Helper class for the training part of the software. A question contains the question answer and three decoys
 *
 */

public class Question {
    public String Question;
    public String correctAnswer;
    public String[] Decoys;
    public boolean answeredCorrectly;
    public DatabaseHandler handler;

    public Question(String question, String correctAnswer, String[] decoys){
    Question = question;
        this.correctAnswer = correctAnswer.toUpperCase();
        Decoys = decoys;
        answeredCorrectly = false;




    }
// Check if an attempted solution is correct
    public boolean checkAnswer(String attempt){
        if(attempt.matches(attempt)){
            return true;
        }else{
            return false;
        }
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getDecoys() {
        return Decoys;
    }

    public void setDecoys(String[] decoys) {
        Decoys = decoys;
    }

    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }
    public void modifyFamiliarity(int change, Context context){
        handler = new DatabaseHandler(context);
        handler.changeFamiliarity(change, Question);
    }
    public void modifyFamiliarity(boolean familiarity, Context context){
        handler = new DatabaseHandler(context);
        handler.changeFamiliarity(familiarity, Question);
    }
    public void changeTimesDisplayed(Context context){
        handler = new DatabaseHandler(context);
        handler.incrementTimesDisplayed(Question);
    }
}
