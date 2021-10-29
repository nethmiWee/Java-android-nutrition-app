package nearchos.github.nutitioninfoapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class SearchActivity extends AppCompatActivity {
    CustomAdapterList customAdapterList;
    CustomAdapter customAdapter;
    RecyclerView recyclerViewMerged;
    ArrayList<String> product_code, product_name, product_grade, product_group, product_nutrients, product_ingredients, product_spinner;
    ArrayList<String> product_list_name;
    ArrayList<String> list_id;
    ArrayList<String> list_description;
    ArrayList<String> list_favs;
    ArrayList<String> list_colour;
    EditText searchView;

    Button button;
    MyDatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
button = findViewById(R.id.button);

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent refresh = new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(refresh);
    }
});

                myDB = new MyDatabaseHelper(SearchActivity.this);

        product_code = new ArrayList<>();
        product_name = new ArrayList<>();
        product_grade = new ArrayList<>();
        product_group = new ArrayList<>();
        product_nutrients = new ArrayList<>();
        product_ingredients = new ArrayList<>();
        product_spinner = new ArrayList<>();


        list_description = new ArrayList<>();
        list_id = new ArrayList<>();
        product_list_name = new ArrayList<>();
        list_favs = new ArrayList<>();
        list_colour = new ArrayList<>();


        recyclerViewMerged = findViewById(R.id.recyclerViewMerged);
        storeAllProductData();
        storeAllListData();

        customAdapter = new CustomAdapter(SearchActivity.this, product_code,
                product_name, product_grade, product_group, product_nutrients, product_ingredients, product_spinner);

        customAdapterList = new CustomAdapterList(SearchActivity.this, product_list_name, list_id, list_description, list_favs, list_colour, true);

        // Create a new ConcatAdapter and pass created adapters in sequence we need to show.
        ConcatAdapter concatAdapter = new ConcatAdapter(customAdapter, customAdapterList);


        recyclerViewMerged.setAdapter(concatAdapter);
        recyclerViewMerged.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().replaceAll("\\s",""));


            }
        });

    }

    private void filter(String text){
        ArrayList<Integer> filteredIndex = new ArrayList<Integer>();
        ArrayList<String> filteredList = new ArrayList<String>();
        ArrayList filteredListList = new ArrayList<>();


        for(String item : product_name){

                if (String.valueOf(item).toLowerCase().contains(text.toLowerCase())) {


                        filteredList.add(item);

                }

        }


    customAdapter.filterList(filteredList);




        for(Object item : product_list_name) {
            if (!text.toString().toLowerCase().equals(" ")) {
                if (String.valueOf(item).toLowerCase().contains(text.toString().toLowerCase())) {
                    Log.w("Filter", String.valueOf(item).toLowerCase() + " contains " + text.toLowerCase());
                    filteredListList.add(item);
                    Log.w("Filter list item", String.valueOf(item) + "");
                }
            }
        }
        customAdapterList.filterList(filteredListList);



    }


    void storeAllProductData(){
        Cursor cursor = myDB.readAllProductData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                product_code.add(cursor.getString(0));
                product_name.add(cursor.getString(1));
                product_grade.add(cursor.getString(2));
                product_group.add(cursor.getString(3));
                product_ingredients.add(cursor.getString(4));
                product_nutrients.add(cursor.getString(5));
                product_spinner.add(cursor.getString(6));

            }
        }
    }

    void storeAllListData(){
        Cursor cursor = myDB.readAllListData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Add new list data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                list_id.add(cursor.getString(0));
                product_list_name.add(cursor.getString(1));
                list_description.add(cursor.getString(2));
                list_favs.add(String.valueOf(cursor.getInt(3)));
                list_colour.add(String.valueOf(cursor.getInt(4)));
            }
        }

    }

}