package nearchos.github.nutitioninfoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Search bar
    EditText search;

    //Product add button and recyclerview for cards
    RecyclerView recyclerView;
    Button add_button_home;

    //Viewing Products
    MyDatabaseHelper myDB;
    ArrayList<String> product_code, product_name, product_grade, product_group, product_nutrients, product_ingredients, product_spinner;
    CustomAdapter customAdapter;

   //Recycler view for list
    RecyclerView recyclerView2;
    ArrayList<String> product_list_name;
    ArrayList<String> list_id;
    ArrayList<String> list_description;
    ArrayList<String> list_favs;
    ArrayList<String> list_colour;
    CustomAdapterList customAdapterList;

    //Delete all list button
    ImageButton delete_all_lists;

   //Delete all products button
   ImageButton delete_all_products;

   //Recent product and list
    ImageView recentProductImage;
    TextView recentProductName, recentProductGrade, nutritionText;
    Button button2;
    FloatingActionButton button3;
    ImageView recentStar, imageView4;
    TextView recentListName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recent item initialization
        recentProductName = findViewById(R.id.recentProductName);
        nutritionText = findViewById(R.id.nutritionText);
        recentListName = findViewById(R.id.recentListName);
        recentProductGrade = findViewById(R.id.recentProductGrade);
        recentProductImage = findViewById(R.id.recentProductImage);
        imageView4 = findViewById(R.id.imageView4);
        recentStar = findViewById(R.id.recentListStar);
        button2 = findViewById(R.id.recentButton);
        button3 = findViewById(R.id.floatingActionButton3);

        //Go to search screen on clicking search bar
        search = findViewById(R.id.searchBar);
        search.setInputType(InputType.TYPE_NULL);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        //Add product to store
        recyclerView = findViewById(R.id.recycleView);
        add_button_home = findViewById(R.id.product_add_button_home);

        if(Build.VERSION.SDK_INT > 25){
            add_button_home.setTooltipText("Add a food item");
        }

        add_button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });


        //Store data of products to display with recyclerview
        myDB = new MyDatabaseHelper(MainActivity.this);
        product_code = new ArrayList<>();
        product_name = new ArrayList<>();
        product_grade = new ArrayList<>();
        product_group = new ArrayList<>();
        product_nutrients = new ArrayList<>();
        product_ingredients = new ArrayList<>();
        product_spinner = new ArrayList<>();

        storeAllProductData();

        //Update mappings (Handles deleted products in list and so on)
       // myDB.removeDuplicates();
        myDB.updateMapping();

       //Delete all button for lists
        delete_all_lists = findViewById(R.id.delete_all_button);

        if(Build.VERSION.SDK_INT > 25){
            delete_all_lists.setTooltipText("Deletes all lists");
        }

        delete_all_lists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogDeleteAll(true);
            }
        });

        //Delete all button for product
        delete_all_products = findViewById(R.id.delete_all_products);

        if(Build.VERSION.SDK_INT > 25){
            delete_all_products.setTooltipText("Deletes all lists");
        }

        delete_all_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogDeleteAll(false);
            }
        });

        //Show products in cards
        customAdapter = new CustomAdapter(MainActivity.this, product_code,
                product_name, product_grade, product_group, product_nutrients, product_ingredients, product_spinner);


        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        //Storing list data to display in cards
        Button add_list_button;
        recyclerView2 = findViewById(R.id.recyclerView2);

        list_description = new ArrayList<>();
        list_id = new ArrayList<>();
        product_list_name = new ArrayList<>();
        list_favs = new ArrayList<>();
        list_colour = new ArrayList<>();

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


        //Add to list button
        add_list_button = findViewById(R.id.add_list_button);
        add_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateListActivity.class);
                startActivity(intent);
            }
        });

        //Show lists in cards
        customAdapterList = new CustomAdapterList(MainActivity.this, product_list_name, list_id, list_description, list_favs, list_colour, false);
        recyclerView2.setAdapter(customAdapterList);
        recyclerView2.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

        //Recent product and list
        try {
            getRecentProduct();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Puts product information into array list for use
    */
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

    /*
   Confirmation alert to delete all products or lists
    */
    void confirmDialogDeleteAll(Boolean isList){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String toDelete = "";
        if(isList) {
            toDelete = "lists";
        }else{
            toDelete = "products";
        }
        builder.setTitle("Delete " + toDelete);
        builder.setMessage("Are you sure you want to delete all " + toDelete + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(isList){myDB.deleteAllLists();
                }else {
                    myDB.deleteAllProducts();
                }

                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(refresh);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /*
   Display recently added product and list
   */
    void getRecentProduct() throws IOException {
        String spinner = "";

        if(product_name.size()-1 > 0) {
            recentProductName.setText(product_name.get(product_name.size() - 1));
            recentProductGrade.setText(product_grade.get(product_name.size() - 1));
            spinner = product_spinner.get(product_name.size() - 1);
            customAdapter.getImageAccordingToSpinner(spinner, recentProductImage);


            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                    intent.putExtra("product_name", recentProductName.getText().toString());
                    startActivity(intent);
                }
            });
        }else{
            button2.setVisibility(View.INVISIBLE);
            recentProductName.setVisibility(View.INVISIBLE);
            recentProductGrade.setVisibility(View.INVISIBLE);
            recentProductImage.setVisibility(View.INVISIBLE);
            nutritionText.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }

if(product_list_name.size()-1 > 0){
    int fav;
    recentListName.setText(product_list_name.get(product_list_name.size()-1));
    fav = Integer.parseInt(list_favs.get(product_list_name.size()-1).trim());

    if(fav == 1){
        recentStar.setVisibility(View.VISIBLE);
    }else{
        recentStar.setVisibility(View.INVISIBLE);
    }

    button3.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ListDetailsActivity.class);
            intent.putExtra("list_name", recentListName.getText().toString());
            intent.putExtra("list_id", list_id.get(list_id.size()-1));
            intent.putExtra("list_description", list_description.get(list_description.size()-1));
            startActivity(intent);
        }
    });

}else{
    button3.setVisibility(View.INVISIBLE);
    recentListName.setVisibility(View.INVISIBLE);
}


}

}