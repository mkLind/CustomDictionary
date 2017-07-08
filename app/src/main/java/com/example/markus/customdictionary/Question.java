package com.example.markus.customdictionary;

/**
 * Created by Markus on 19.6.2017.
 */

public class Question {
    public String Question;
    public String correctAnswer;
    public String[] Decoys;
    public boolean answeredCorrectly;

    public Question(String question, String correctAnswer, String[] decoys){
    Question = question;
        this.correctAnswer = correctAnswer.toUpperCase();
        Decoys = decoys;
        answeredCorrectly = false;



    }

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
}
