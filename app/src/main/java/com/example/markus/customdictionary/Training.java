package com.example.markus.customdictionary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Training extends AppCompatActivity {
    private TextView question;
    private TextView prg;
    private Question current;
    private Button opt1;
    private Button opt2;
    private Button opt3;
    private Button opt4;
    private Button prgInd;
    private Random r;
    private trainingControl cntrl;
    private int progressCounter;
    private int correctLocation;
    private int target;
    private ProgressBar bar;


    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        prg = (TextView) findViewById(R.id.textView);



        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutparams.setMargins(60,20,60,20);

        correctLocation = 0;
        Intent intent = getIntent();
        // Instatiate the training control
        cntrl = new trainingControl(intent.getStringExtra(trainingOptions.EXTRA_LANGUAGE), intent.getStringExtra(trainingOptions.TRAINING_LENGTH), intent.getExtras().getBoolean(trainingOptions.TRAIN_LEAST_FAMILIAR),getApplicationContext());
        r = new Random();
        setContentView(R.layout.activity_training);
// The cuestion section UI
        question = (TextView) findViewById(R.id.Question);
        opt1 = (Button) findViewById(R.id.option1);
        opt2 = (Button) findViewById(R.id.option2);
        opt3 = (Button) findViewById(R.id.option3);
        opt4 = (Button) findViewById(R.id.option4);

        progressCounter = 0;

        if(cntrl.isTrainable()) {
            // set up training and set maximum and current values to progressbar
            cntrl.setUpTraining();
            target = cntrl.getQuestions().size();
            bar = (ProgressBar) findViewById(R.id.progressBar);
            bar.setMax(target);
            bar.setProgress(progressCounter);
            // Display first question
            displayQuestion();


        }else{
            // if not enough words for training
        question.setText("Not enough words for training");
            // Display full progress bar
            bar = (ProgressBar) findViewById(R.id.progressBar);
            bar.setMax(10);
            bar.setProgress(10);
            opt1.setText("");
            // Decoys
            opt2.setText("");
            opt3.setText("");
            opt4.setText("");
        }


    }


    public void displayQuestion(){



        int i = r.nextInt(3);
        current = cntrl.getQuestions().get(progressCounter);
        question.setText(current.getQuestion());
        String[] dec = cntrl.getQuestions().get(progressCounter).getDecoys();
        Log.d("training","Decoys in display question: " + dec[0] +"|"+dec[1]+"|"+dec[2]);
            // set the locations of the answers
        if(i == 0){
            correctLocation = 1;
            // Correct answer
            opt1.setText(cntrl.getQuestions().get(progressCounter).getCorrectAnswer());
            // Decoys
            opt2.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[0]);
            opt3.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[1]);
            opt4.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[2]);

        }

        if(i == 1){
            correctLocation = 2;

            opt2.setText(cntrl.getQuestions().get(progressCounter).getCorrectAnswer());

            opt1.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[0]);
            opt3.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[1]);
            opt4.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[2]);
        }

        if(i == 2){
            correctLocation = 3;
            opt3.setText(cntrl.getQuestions().get(progressCounter).getCorrectAnswer());

            opt1.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[0]);
            opt2.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[1]);
            opt4.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[2]);
        }

        if(i == 3){
            correctLocation = 4;
            opt4.setText(cntrl.getQuestions().get(progressCounter).getCorrectAnswer());

            opt1.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[0]);
            opt2.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[1]);
            opt3.setText(cntrl.getQuestions().get(progressCounter).getDecoys()[2]);
        }

    }
    public void checkAnswer(View view){




        // each button has a tag that is compared to correct location.
        int tag = Integer.parseInt((String)view.getTag());

        // if correct answer is found, update progress bar and give feedback
        if(tag == correctLocation){

            current.modifyFamiliarity(1, getApplicationContext());
            question.setText("Correct!");
            question.setTextColor(Color.GREEN);
            // Update progress bar based on updated progress counter
            progressCounter++;
            bar.setProgress(progressCounter);





        // feedback on incorrect answer

        }else{
            current.modifyFamiliarity(-3, getApplicationContext());
            int dec = r.nextInt(6);

            String toast ="";

              if(dec == 0){
                  toast="Are you sure?";
              }
            else if(dec == 1){
                  toast = "Try again!";
            }
              else if(dec == 2){
                  toast = "That's wrong!";
            }
              else if(dec == 3){
                  toast = "Not that one!";
            }
              else if(dec == 4){
                  toast = "Think again!";
            }
              else if(dec == 5){
                  toast = "Hmm...";
            }else{
                  toast = "Was it that one?";
                  }

            question.setText(toast);
            question.setTextColor(Color.RED);

        // Add the current question back to question queue to be again presented to the user
                cntrl.getQuestions().add(current);

            // lower the value in progressCounter. this way the user jumps back one question and after answering this, has to answer the previous failed question again
        if(progressCounter>0){
            progressCounter--;
        }
        // update the new maximum value to progress bar. Update the target variable as well and update the progress bar
        bar.setMax(cntrl.getQuestions().size());
            target = cntrl.getQuestions().size();
       bar.setProgress(progressCounter);




        }
if(progressCounter >= target){
    // Feedback
    Toast toast = Toast.makeText(getApplicationContext(),"You did well!", Toast.LENGTH_SHORT);
    toast.setGravity(Gravity.CENTER,0,0);
    toast.show();
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(intent);
}else{



        // Delay a bit the displaying of the next question

        question.postDelayed(
                new Runnable(){
                    public void run(){
                        question.setTextColor(Color.BLACK);
                        displayQuestion();
                    }
                }, 1500
        );


}



    }


}
