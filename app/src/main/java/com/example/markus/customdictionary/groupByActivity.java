        package com.example.markus.customdictionary;

        import android.app.ActionBar;
        import android.app.FragmentManager;
        import android.app.SearchManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.Drawable;
        import android.graphics.drawable.GradientDrawable;
        import android.graphics.drawable.shapes.Shape;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
        import android.transition.Transition;
        import android.transition.TransitionValues;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.LinearLayout;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.support.v7.widget.SearchView;
        import android.widget.ScrollView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.util.ArrayList;


        public class groupByActivity extends ActionBarActivity implements DialogInterface.OnDismissListener{
        private String language;
            private DatabaseHandler handler;
            private RadioGroup words;
            private ArrayList<String> wordsToDelete;
            private ArrayList<String> grouped;
            private ScrollView scroll;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Intent intent = getIntent();
                scroll = (ScrollView) findViewById(R.id.scroller);
                language =  intent.getStringExtra(groupByDialog.EXTRA_LANGUAGE);

                wordsToDelete = new ArrayList<String>();
                setContentView(R.layout.activity_group_by);
                  setWordsFromDictionary();
            }

        public void setWordsFromDictionary(){
            words = (RadioGroup) findViewById(R.id.search_history1);
            handler = new DatabaseHandler(getApplicationContext());
            grouped = handler.groupByLanguage(language);
            if(!grouped.isEmpty()){

        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        );
        para.setMargins(0,0,0,6);

                for(int i = 0;i<grouped.size();i++){

                    CheckBox view = new CheckBox(getApplicationContext());
                    view.setId(i);

                    view.setTextSize(25f);



                    view.setText(grouped.get(i).replace("=>", ":"));
                     view.setTextColor(Color.BLACK);
                    view.setLayoutParams(para);
                    view.setBackground(getResources().getDrawable(R.drawable.radiogroupdrawable));

                    words.addView(view);
                }



            }else{
                Button noWords = new Button(getApplicationContext());
                noWords.setId(0);
                noWords.setText("No words in this dictionary!");
                noWords.setTextSize(25f);
                noWords.setBackgroundColor(Color.rgb(203, 203, 203));
                noWords.setTextColor(Color.rgb(50,50,50));
                words.addView(noWords);
            }
        }
            public void selectAll(){
                for(int i = 0; i<words.getChildCount();i++){
                    CheckBox b = (CheckBox) words.getChildAt(i);
                    if(!b.isChecked()){
                        b.setChecked(true);
                    }
                }
            }

            public void deSelectAll(){
                for(int i = 0; i<words.getChildCount();i++){
                    CheckBox b = (CheckBox) words.getChildAt(i);
                    if(b.isChecked()){
                        b.setChecked(false);
                    }
                }
            }
            public void updateList(){
                words.removeAllViews();
                setWordsFromDictionary();
            }
            public void deleteDictionary( String language){

            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_group_by, menu);
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                final SearchView searchView = (SearchView) menu.findItem(R.id.Search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) { // What to do on search

                      for(int i = 0; i<words.getChildCount();i++){
                          CheckBox check = (CheckBox) words.getChildAt(i);
                          check.setFocusable(true);
                          if(check.getText().toString().contains(query)){
                             check.setChecked(true);
                              int x = (int) words.getChildAt(i).getX();
                              int y = (int)words.getChildAt(i).getX();

                             words.scrollTo(x,y);


                          }
                      }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) { // What to do on text change
                        if(newText.contains(newText)){

                            for(int i = 0; i<words.getChildCount();i++){
                                CheckBox check = (CheckBox) words.getChildAt(i);
                                if(check.getText().toString().contains(newText)){
                                    check.setChecked(true);



                                }else{
                                    check.setChecked(false);
                                }
                            }
                        }

                        return true;
                    }
                });

                return true;
            }


            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                if (id == R.id.Delete_word ) {
        wordsToDelete.clear();


        if(!grouped.isEmpty()) {
            for (int i = 0; i < words.getChildCount(); i++) {
                CheckBox b = (CheckBox) words.getChildAt(i);
                if (b.isChecked()) {
                    wordsToDelete.add(b.getText().toString());
                }
            }
        }else{
            Toast.makeText(getApplicationContext(),"No words to delete!",Toast.LENGTH_SHORT).show();
        }

        if(wordsToDelete.size()>0) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("DeleteKeys", wordsToDelete);
            bundle.putString("Message", "Really delete?");
            bundle.putString("Dictionary", "");
            DeleteConfirm d = new DeleteConfirm();

            d.setArguments(bundle);

            FragmentManager m = getFragmentManager();


            d.show(m, "");

        }else{
            Toast.makeText(getApplicationContext(),"Select a word to delete!",Toast.LENGTH_SHORT).show();
        }





                    return true;
                }
                if(id == R.id.Deselect_all){
                    deSelectAll();
                }
                if(id == R.id.action_Select_all ) {
                    if (!grouped.isEmpty()) {
                        selectAll();

                    } else {
                        Toast.makeText(getApplicationContext(), "No words to select!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                if(id == R.id.action_delete_dictionary ){
                    if(!grouped.isEmpty()) {
                        selectAll();
                        for (int i = 0; i < words.getChildCount(); i++) {
                            CheckBox b = (CheckBox) words.getChildAt(i);

                            wordsToDelete.add(b.getText().toString());

                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("DeleteKeys", wordsToDelete);
                    bundle.putString("Message", "Delete dictionary?");
                    bundle.putString("Dictionary", language);

                    DeleteConfirm d = new DeleteConfirm();

                    d.setArguments(bundle);

                    FragmentManager m = getFragmentManager();


                    d.show(m, "");
                    return true;

                }
                if(id == R.id.modify_word){
                    ArrayList<String> wordsToMod = new ArrayList<>();
                    if(!grouped.isEmpty()) {
                        for (int i = 0; i < words.getChildCount(); i++) {
                            CheckBox b = (CheckBox) words.getChildAt(i);
                            if (b.isChecked()) {
                                wordsToMod.add(b.getText().toString());
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"No words to modify!",Toast.LENGTH_SHORT).show();
                    }

                       if(wordsToMod.size()==1) {
                           Bundle b = new Bundle();

                           b.putString("WORDMEANING", wordsToMod.get(0));
                           modDialog mod = new modDialog();
                           mod.setArguments(b);
                           FragmentManager manager = getFragmentManager();
                           mod.show(manager, "");

                       }else{
                           Toast.makeText(getApplicationContext(),"Only one word can be selected to be modified at a time",Toast.LENGTH_SHORT).show();
                           words.removeAllViews();
                           setWordsFromDictionary();
                       }

                    return true;
                }
                if(id == R.id.Export_dictionary){
                    if(!grouped.isEmpty()){
                        selectAll();
                        String export =  language + "||" ;

                        for (int i = 0; i < words.getChildCount(); i++) {
                            CheckBox b = (CheckBox) words.getChildAt(i);

                                export = export + b.getText() + "#";

                        }
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, export);
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, "Choose application:"));
                    }

                }


                return super.onOptionsItemSelected(item);
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                updateList();
            }
        }
