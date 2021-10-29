package nearchos.github.nutitioninfoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ProductDetailsActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;
    TextView product_name_txt2, product_grade_txt2, product_code_txt,
            product_group_txt, product_ingredients_txt, product_nutrients_txt, spinner_txt;
    String product_spinner;
    Bundle extras;
    ImageView rImage, novaPic, nutriPic;
    //Add button for add product to list
    Button action_add_button;
    boolean[] checkedItems;
    ArrayList<String> product_list_name = new ArrayList<>();
    ArrayList<Integer> mUserItems = new ArrayList<>();
    String[] listItems;
    ArrayList<String> product_list_id = new ArrayList<>();
    ArrayList<String> product_list_id_in = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        myDB = new MyDatabaseHelper(this);
        //Move items into the details page and get picture

        rImage = findViewById(R.id.img);
        novaPic = findViewById(R.id.novaPic);
        nutriPic = findViewById(R.id.nutriPic);

        product_name_txt2 = findViewById(R.id.product_list_txt2);
        product_grade_txt2 = findViewById(R.id.product_grade_txt2);
        product_group_txt = findViewById(R.id.product_group_txt);
        product_code_txt = findViewById(R.id.product_code_txt);
        product_nutrients_txt = findViewById(R.id.product_nutrients_txt);
        product_ingredients_txt= findViewById(R.id.product_ingredients_txt);
        spinner_txt = findViewById(R.id.spinner_txt);

        extras = getIntent().getExtras();


        if(extras != null){

            product_name_txt2.setText(extras.getString("product_name"));
            getDataAccordingToName();

            try {
                getImageAccordingToSpinner(product_spinner, rImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //Delete button

        Button product_delete_button;

        product_delete_button = findViewById(R.id.list_delete_button);

        product_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogDelete(product_name_txt2,product_code_txt);

            }
        });

        //Customise list

        getListNames();
        itemSelectionDialog();
        getNovNutriImages();


    }


    void getDataAccordingToName(){
        Cursor cursor = myDB.readAllProductData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if(extras.getString("product_name").equals(cursor.getString(1))){
                    product_code_txt.setText(cursor.getString(0));
                    product_grade_txt2.setText(cursor.getString(2));
                    product_group_txt.setText(cursor.getString(3));
                    product_nutrients_txt.setText(cursor.getString(5));
                    product_ingredients_txt.setText(cursor.getString(4));
                    spinner_txt.setText(cursor.getString(6));
                    product_spinner = (cursor.getString(6));
                }
            }
        }

        cursor.close();
    }

    void getImageAccordingToSpinner(String product_spinner,
                                    ImageView imageView) throws IOException {
        if(imageView.getDrawable()== null) {
            switch (product_spinner) {
                case "Healthy":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-healthy-food-80.png?alt=media&token=55d3521a-bd32-49a1-809e-a8b964ec5fdf").into(imageView);
                    break;
                case "Seafood":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-whole-fish-80.png?alt=media&token=907211dd-cf3e-4f80-8e01-2f51a217f75a").into(imageView);
                    break;
                case "Vegetable":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-carrot-80.png?alt=media&token=7d67a85a-e6fa-4463-b6ba-9421c3571049").into(imageView);
                    break;
                case "Fruit":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-apple-80.png?alt=media&token=3ef50ce9-b3c6-4e52-b515-7fad2fbc67d3").into(imageView);
                    break;
                case "Grain":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-porridge-80.png?alt=media&token=926cf24d-2f95-4305-a976-06ec9aaffe76").into(imageView);
                    break;
                case "Fast-Food":
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-pizza-80.png?alt=media&token=be309247-9cff-48b1-8dc1-40d603a7b226").dontAnimate().into(imageView);
                    break;
                case "Sugar":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-doughnut-80.png?alt=media&token=2504f954-0b89-4205-aa6a-ffb59c4b4335").into(imageView);
                    break;
                case "Poultry":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-thanksgiving-80.png?alt=media&token=977ec8c9-db0a-4133-8ddd-da3566d5ccbd").into(imageView);
                    break;
                case "Crustacean":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-crab-80.png?alt=media&token=d6031bbe-8793-4107-baeb-407011452adb").into(imageView);
                    break;
                case "Alcohol":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-beer-80.png?alt=media&token=121e4f31-a39b-4e1c-b3e7-bcf5880eea06").into(imageView);
                    break;
                case "Cold-Drink":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-coconut-cocktail-80.png?alt=media&token=dafecf71-d09c-44f9-87fe-848e4146bf23").into(imageView);
                    break;
                case "Dairy":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-milk-80.png?alt=media&token=b8934e11-a638-4613-8ebf-1142bad3f92a").into(imageView);
                    break;
                case "Hot-Drink":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-tea-80.png?alt=media&token=80aa3e3c-82bb-408a-be26-a03667918aa2").into(imageView);
                    break;
                case "Milkshake":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-milkshake-80.png?alt=media&token=24bcc4e0-c794-409e-b328-1bc51a09727d").into(imageView);
                    break;
                case "Misc":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-ingredients-80.png?alt=media&token=a8e3fee0-7a07-4c8c-827d-30d92677cd87").into(imageView);
                    break;
                case "Noodles":
                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-noodles-80.png?alt=media&token=61652ba2-8879-4bf5-a27b-3666a1373fa8").into(imageView);
                    break;
                case "Pastry":
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-croissant-80.png?alt=media&token=52a9ab69-7c1f-4662-8ecb-2c677a1df727").dontAnimate().into(imageView);
                    break;
                case "Unspecified":
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/icons8-food-80.png?alt=media&token=bda8cc39-0234-47d7-88d6-12fcd2d411e2").dontAnimate().into(imageView);
                    break;
                default:
                    String[] twoImages = product_spinner.split(",");
                    Glide.with(getApplicationContext()).load(twoImages[0]).dontAnimate().into(imageView);
                    spinner_txt.setText("Api icon");
                    break;

            }
        }

    }

    void confirmDialogDelete(TextView product_name_txt2, TextView product_code_txt){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ product_name_txt2.getText() + "?");
        builder.setMessage("Are you sure you want to delete "+ product_name_txt2.getText() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(ProductDetailsActivity.this);
                myDB.deleteProduct(product_code_txt.getText().toString().trim());

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

    void itemSelectionDialog(){
        //Selection item dialog
        action_add_button = findViewById(R.id.update_list_button);


        checkedItems = new boolean[product_list_id.size()];  //Get check boxes

        action_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(ProductDetailsActivity.this);
                mBuilder.setTitle("Add food to list");

                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                        if (isChecked) {
                            if (!mUserItems.contains(position)) {
                                mUserItems.add(position);
                            } else {
                                mUserItems.remove(position);
                            }
                        }


                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMap();

                    }
                });
                //Dismiss
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                //ClearAll

                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i= 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                            mUserItems.clear();
                        }

                    }
                });

                androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    void getListNames(){

        Cursor cursor2 = myDB.readAllListData();
        if (cursor2.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor2.moveToNext()) {
                product_list_id.add(cursor2.getString(0));
                product_list_name.add(cursor2.getString(1)); //removed s

            }

        }


        listItems = new String[product_list_name.size()];
        for(int i=0; i<product_list_name.size(); i++){
            listItems[i] = product_list_name.get(i) ;
        }

        cursor2.close();

    }

    boolean isNotInList = true;
    int i = 0;
    void saveMap() {

        Cursor cursor = myDB.readAllMapData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if ((cursor.getString(0).equals(product_code_txt.getText().toString()))) {
                    product_list_id_in.add(cursor.getString(1)); //For mapping, if this product code mapped already list id not in array
                    Log.w("List", cursor.getString(1));

                    //Gives which code is in

                }

            }

            cursor.close();
        }

        Set<String> s = new LinkedHashSet<String>(product_list_id_in);
        product_list_id_in.clear();
        product_list_id_in.addAll(s);

        ArrayList<String> num = new ArrayList<>();


        for(int i = 0; i < product_list_id_in.size(); i++){
            Log.w("Hash", product_list_id_in.get(i));
        }

        boolean isContained;


        if (mUserItems != null) {


            Log.w("i value:", String.valueOf(i));
ArrayList<String> addedTo = new ArrayList<>();
            while(i < mUserItems.size()) {
                addedTo.add((product_list_id.get(mUserItems.get(i))));
                i++;
            }

           for(String item: addedTo){
               if(!(product_list_id_in.contains(item))){
                   myDB.addMapping(Integer.parseInt(item), product_code_txt.getText().toString());
               }
           }

            //   }
        } else {
            Toast.makeText(this, "No lists were selected", Toast.LENGTH_LONG).show();
        }


    }

    void getNovNutriImages(){
        //Nutrition
        switch (product_grade_txt2.getText().toString().trim()){
            case "A":
                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/1200px-Nutri-score-A.svg.png?alt=media&token=55927c18-f998-4e0c-a1e1-50e9ecbac90b").dontAnimate().into(nutriPic);
                break;
            case "B":
                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/score.png?alt=media&token=5faf4c19-fce7-447f-8166-1d414fee2abb").dontAnimate().into(nutriPic);
                break;
            case "C":
                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/1280px-Nutri-score-C.svg.png?alt=media&token=aacad238-3f2c-4b94-835c-6feec0dd80b8").dontAnimate().into(nutriPic);
                break;
            case "D":
                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/score_d.png?alt=media&token=a0414aaf-6923-4786-ba0d-0cba5da37e08").dontAnimate().into(nutriPic);
                break;
            case "E":
                Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/storeling-c0777.appspot.com/o/score_e.png?alt=media&token=1484e562-5e0d-4086-b41a-e792e1812546").dontAnimate().into(nutriPic);
                break;
            default:
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/attributes/nutriscore-unknown.svg", nutriPic);
                break;
        }

        //Nova
        switch (product_group_txt.getText().toString().trim()){
            case "1":
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/misc/nova-group-1.svg", novaPic);
                break;
            case "2":
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/misc/nova-group-2.svg", novaPic);
                break;
            case "3":
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/misc/nova-group-3.svg", novaPic);
                break;
            case "4":
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/misc/nova-group-4.svg", novaPic);
                break;
            default:
                Utils.fetchSvg(getApplicationContext(), "https://static.openfoodfacts.org/images/attributes/nova-group-unknown.svg", novaPic);


        }


    }

}

