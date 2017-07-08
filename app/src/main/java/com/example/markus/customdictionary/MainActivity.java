package com.example.markus.customdictionary;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
