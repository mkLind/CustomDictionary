        package com.example.markus.customdictionary;

        import android.app.ActionBar;
        import android.app.FragmentManager;
        import android.app.SearchManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.Rect;
        import android.graphics.drawable.Drawable;
        import android.graphics.drawable.GradientDrawable;
        import android.graphics.drawable.shapes.Shape;


        import android.net.Uri;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.ParcelFileDescriptor;
        import android.support.constraint.solver.widgets.Rectangle;
        import android.support.v4.content.res.TypedArrayUtils;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.view.menu.MenuView;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.transition.Transition;
        import android.transition.TransitionValues;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.SubMenu;
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

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;

        import java.io.OutputStreamWriter;



        import java.util.ArrayList;


        public class groupByActivity extends AppCompatActivity implements DialogInterface.OnDismissListener{
        private String language;
            private DatabaseHandler handler;
            private RadioGroup words;
            private RecyclerView recycler;
            private wordAdapter adapter;
            private LinearLayoutManager viewManager;
            private ArrayList<String> wordsToDelete;
            private ArrayList<dictElement> grouped;
            private ScrollView scroll;
            private SortingType type;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Intent intent = getIntent();
                type = SortingType.ALPHABETICALLY;

                language =  intent.getStringExtra(groupByDialog.EXTRA_LANGUAGE);
                viewManager = new LinearLayoutManager(getApplicationContext());
                wordsToDelete = new ArrayList<String>();
                handler = new DatabaseHandler(getApplicationContext());
                grouped = handler.groupByLanguage(language, type);
/*
                setContentView(R.layout.activity_group_by);
                scroll = (ScrollView) findViewById(R.id.scroller);
                words = (RadioGroup) findViewById(R.id.search_history1);
                setWordsFromDictionary();
*/
           setContentView(R.layout.activity_group_by_new);
                displayWords(SortingType.ALPHABETICALLY);

            }
            public void displayWords(SortingType type){

                if(!grouped.isEmpty()) {
                    adapter = new wordAdapter(grouped);
                    recycler = findViewById(R.id.words_recycler);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(viewManager);
                    recycler.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    Button no_words = findViewById(R.id.no_words_button);
                    no_words.setVisibility(View.VISIBLE);
                    no_words.setText("No words in this dictionary yet");

                }
            }

        public void setWordsFromDictionary(){

            handler = new DatabaseHandler(getApplicationContext());
            grouped = handler.groupByLanguage(language, type);
            if(!grouped.isEmpty()){
                int avg = 0;
                int sum = 0;
                for(int i = 0; i<grouped.size();i++){
                    sum += grouped.get(i).getFamiliarity();
                }
                avg = (int) sum / grouped.size();


                LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                para.setMargins(0,0,0,6);

                for(int i = 0;i<grouped.size();i++){

                    CheckBox view = new CheckBox(getApplicationContext());
                    view.setId(i);
                    view.setTextSize(25f);

                    int difference = Math.abs(avg - grouped.get(i).getFamiliarity());


                    if(grouped.get(i).getEntry().contains("<base64>")){
                        String entry = grouped.get(i).getEntry().split("<base64>:")[0];
                        view.setText(entry + ":image_attached");
                        view.setTextColor(Color.BLACK);
                        view.setLayoutParams(para);
                    }else {
                        view.setText(grouped.get(i).getEntry());
                        view.setTextColor(Color.BLACK);
                        view.setLayoutParams(para);
                    }
                    if(type == SortingType.FAMILIARITY) {
                        if (grouped.get(i).getFamiliarity() < avg && difference>=5) {
                            view.setBackgroundResource(R.drawable.corners_red);
                        } else if (grouped.get(i).getFamiliarity() < avg && difference < 5) {
                            view.setBackgroundResource(R.drawable.corners_orange);
                        } else if (grouped.get(i).getFamiliarity() == avg) {
                            view.setBackgroundResource(R.drawable.corners_yellow);
                        } else if (grouped.get(i).getFamiliarity() > avg && difference < 10) {
                            view.setBackgroundResource(R.drawable.corners_yellowgreen);
                        } else if (grouped.get(i).getFamiliarity() > avg && difference>=10) {
                            view.setBackgroundResource(R.drawable.corners_green);
                        }
                    }else{
                        view.setBackgroundResource(R.drawable.corners);
                    }



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
            for(int i = 0; i<grouped.size(); i++){
                grouped.get(i).setSelected(true);
            }
            adapter.update(grouped);
            }

            public void deSelectAll(){
                for(int i = 0; i<grouped.size(); i++){
                    grouped.get(i).setSelected(false);
                }
                adapter.update(grouped);
            }
            public void updateList(){
                grouped = handler.groupByLanguage(language, type);
                adapter.update(grouped);
            }


            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_group_by, menu);
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                final SearchView searchView = (SearchView) menu.findItem(R.id.Search).getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                menu.findItem(R.id.alphabetically).setChecked(true);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) { // What to do on search
                        ArrayList<dictElement> forSearch = adapter.getItems();

                      for(int i = 0; i<forSearch.size();i++){
                          String word = forSearch.get(i).getEntry().split(":")[0];
                          String meaning = forSearch.get(i).getEntry().split(":")[1];

                          if(word.contains(query) || meaning.contains(query)){
                             forSearch.get(i).setSelected(true);
                             adapter.update(forSearch);
                             recycler.scrollToPosition(i);
                          }else{
                              forSearch.get(i).setSelected(false);
                          }
                      }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) { // What to do on text change
                    return true;
                    }
                });

                return true;
            }

            public void exportDictionary(String data, Uri uri){

try{
    ParcelFileDescriptor desc = getApplicationContext().getContentResolver().openFileDescriptor(uri, "w");
    FileOutputStream stream = new FileOutputStream(desc.getFileDescriptor());
    stream.write(data.getBytes());
    stream.close();
    desc.close();
}catch(FileNotFoundException e){

e.printStackTrace();
}catch(IOException e){
    e.printStackTrace();
                }
            }
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();
                Log.d("SORTING ID: ","" + id);
                ArrayList<dictElement> manipulateData = adapter.getItems();
                if (id == R.id.Delete_word ) {
        wordsToDelete.clear();



        if(!manipulateData.isEmpty()) {
            for (int i = 0; i < manipulateData.size(); i++) {
                dictElement element = manipulateData.get(i);
                if (element.isSelected()) {
                    wordsToDelete.add(element.getEntry());
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
                if(id == R.id.alphabetically){
                    if(!item.isChecked()){item.setChecked(true);}
                    type = SortingType.ALPHABETICALLY;
                    updateList();
                }
                if(id == R.id.by_familiarity){
                    if(!item.isChecked()){item.setChecked(true);}
                    type = SortingType.FAMILIARITY;
                    updateList();
                }
                if(id == R.id.by_times_displayed){
                    if(!item.isChecked()){item.setChecked(true);}
                    type = SortingType.BY_TIMES_DISPLAYED;
                    updateList();
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
                        for (int i = 0; i < manipulateData.size(); i++) {
                            dictElement element = manipulateData.get(i);

                            wordsToDelete.add(element.getEntry());

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
                        for (int i = 0; i < manipulateData.size(); i++) {
                            dictElement element = manipulateData.get(i);
                            if (element.isSelected()) {
                                wordsToMod.add(element.getEntry());
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
                            updateList();
                       }

                    return true;
                }
                if(id == R.id.Export_dictionary){
                    if(!grouped.isEmpty()){
                        createFile("application/json","Dictionary.json");
                    }

                }




                return super.onOptionsItemSelected(item);
            }

            public void createFile(String mimetype, String name){
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType(mimetype);
                intent.putExtra(Intent.EXTRA_TITLE, name);
                startActivityForResult(intent,9898);


            }
public void onActivityResult(int requestCode, int resultCode, Intent data){

                if(data == null){
                    return;
                }
                switch(requestCode){
                    case 9898:
                        selectAll();
                        String export =  language + "||" ;
                        JSONArray dictionary = new JSONArray();
                        ArrayList<dictElement> words = adapter.getItems();
                        try{
                            for (int i = 0; i < words.size(); i++) {
                                dictElement element = words.get(i);
                                JSONObject Entry = new JSONObject();

                                Entry.put(element.getEntry().split(":")[0],element.getEntry().split(":")[1]);
                                dictionary.put(Entry);
                            }
                            JSONObject forSave = new JSONObject();
                            forSave.put(language, dictionary);
                            exportDictionary(forSave.toString(), data.getData());
                        }catch(JSONException e){
                            e.printStackTrace();

                        }



                        deSelectAll();


            }
}
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateList();
            }



        }



