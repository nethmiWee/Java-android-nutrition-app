package nearchos.github.nutitioninfoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateListActivity extends AppCompatActivity {

    //List details
    EditText description_txt;
    Button finish_list_add_button;
    AutoCompleteTextView list_name;

    //Colour of list
    RadioGroup radioGroup;
    int colour = 0;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton radioButton3;
    RadioButton radioButton4;
    RadioButton radioButton5;

    //Selection of items to add to list
    String[] listItems;
    ArrayList<String> product_name, product_code;
    boolean[] checkedItems;
    ImageButton mOrder;
    TextView mItemSelected;
    ArrayList<Integer> mUserItems = new ArrayList<>();
    MyDatabaseHelper myDB = new MyDatabaseHelper(CreateListActivity.this);

    //Reading lists data
    String product_list_name, list_description;
    ArrayList<Integer> list_id;

    //Fav button
    ImageButton visible_star, invisible_star;
    int fav_clicked = 0;

    //Update
    boolean isUpdate = false;
    TextView textToUpdateOrAdd;
    Bundle extras;
    int sentId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_creation);

       //Radio buttons for colour
        radioGroup = findViewById(R.id.radioGroup);
       radioButton1 = findViewById(R.id.radioButton1);
       radioButton2 = findViewById(R.id.radioButton2);
       radioButton3 = findViewById(R.id.radioButton3);
       radioButton4 = findViewById(R.id.radioButton4);
       radioButton5 = findViewById(R.id.radioButton5);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        //Green
                        colour = 1;
                        break;
                    case R.id.radioButton2:
                        colour = 2;
                        //Red
                        break;
                    case R.id.radioButton3:
                        colour = 3;
                        //Pink
                        break;
                    case R.id.radioButton4:
                        colour = 4;
                        //Blue
                        break;
                    case R.id.radioButton5:
                        colour = 5;
                        //Black
                        break;
                    default:
                        colour = 1;
                        break;
                }
            }


        });

        product_name = new ArrayList<>();
        product_code = new ArrayList<>();

      //  String product_list_name;
        list_id = new ArrayList<>();
      //  String list_description;

        //Fav button activity
        visible_star = findViewById(R.id.visible_star);
        invisible_star = findViewById(R.id.invisible_star);




        visible_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_clicked = 1;
                visible_star.setVisibility(View.INVISIBLE);
                invisible_star.setVisibility(View.VISIBLE);
            }
        });

        invisible_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav_clicked = 0;
                invisible_star.setVisibility(View.INVISIBLE);
                visible_star.setVisibility(View.VISIBLE);
            }
        });


        getListsSize();

        //List here is list of names
        list_name = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.my_drop_down_items, getResources().getStringArray(R.array.list));
        list_name.setAdapter(adapter);

        description_txt = findViewById(R.id.description_input);
        finish_list_add_button = findViewById(R.id.finish_add_list);
        finish_list_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(CreateListActivity.this);

                if(!isUpdate){
                    myDB.addList(list_name.getText().toString().trim(), description_txt.getText().toString().trim(), fav_clicked, colour);
                    saveMapping();
                    Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(refresh);
                }  //Update
                else {

                    myDB.updateList(sentId, list_name.getText().toString().trim(), description_txt.getText().toString().trim(), fav_clicked, colour);
                    saveMapping();
                    isUpdate = false;
                    Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(refresh);
                }

            }
        });

        getProductItems();


        itemSelectionDialog();

        //Update
        extras = getIntent().getExtras();


        textToUpdateOrAdd = findViewById(R.id.textToUpdateOrAdd);

        if(extras != null){
            isUpdate = extras.getBoolean("isUpdateTrue");
            sentId = extras.getInt("list_id");
            getDataAccordingToCode();
        }


        if(isUpdate){
            textToUpdateOrAdd.setText("Update List");
        }else{
            textToUpdateOrAdd.setText("Create List");
        }




    }

    void getDataAccordingToCode(){
        Cursor cursor = myDB.readAllListData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if(sentId == (cursor.getInt(0))){
                    product_list_name = (cursor.getString(1));
                    list_description = (cursor.getString(2));
                    fav_clicked = (cursor.getInt(3));
                    colour = (cursor.getInt(4));
                }
            }
        }

        cursor.close();

        description_txt.setText(list_description);
        list_name.setText(product_list_name);

        switch (colour){
            case 1:
                radioButton1.setChecked(true);
                break;
            case 2:
                radioButton2.setChecked(true);
                break;
            case 3:
                radioButton3.setChecked(true);
                break;
            case 4:
                radioButton4.setChecked(true);
                break;
            case 5:
                radioButton5.setChecked(true);
                break;

        }

        if(fav_clicked == 1){
            invisible_star.setVisibility(View.VISIBLE);
        }

        myDB.deleteMappingForList(sentId);

    }

    void saveMapping(){

        for (int i=0; i <= mUserItems.size()-1; i++) {
            int id = 0;
            String code = "";

            if(isUpdate && sentId != 0){
               // id = list_id.indexOf(list_id.size()) + 1; //Replace with
                 id = sentId;
            }else{
                id =list_id.size()+1;
            }

            if(mUserItems!= null)
            code = (String)product_code.get(mUserItems.get(i));

            //Toast.makeText(this, (CharSequence) product_code, Toast.LENGTH_LONG).show();

            myDB.addMapping(id, code);

        }
    }

    void itemSelectionDialog(){
        //Selection item dialog
        mOrder = findViewById(R.id.btnSelect);
        mItemSelected = findViewById(R.id.mItemSelected);

        checkedItems = new boolean[product_name.size()];  //Get check boxes

        mOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateListActivity.this);
                mBuilder.setTitle("Add food to list");
                mBuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUserItems.contains(position)){
                                mUserItems.add(position);
                            }else{
                                mUserItems.remove(position);
                            }
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for(int i = 0; i < mUserItems.size(); i++){
                            item = item + listItems[mUserItems.get(i)];
                            //Commas
                            if(i != mUserItems.size() - 1){
                                item = item + ", ";
                            }
                        }

                        mItemSelected.setText(item);
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
                            mItemSelected.setText("");
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });
    }

    void getProductItems(){
        //Store product items
        Cursor cursor = myDB.readAllProductData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                product_code.add(cursor.getString(0));
                product_name.add(cursor.getString(1));
            }
        }

        listItems = new String[product_name.size()];
        for(int i=0; i<product_name.size(); i++){
            listItems[i] = product_name.get(i);
        }
    }

    void getListsSize(){
        Cursor cursor = myDB.readAllListData();
        if(cursor.getCount()==0){
            Toast.makeText(this, "Add new list data", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                list_id.add(cursor.getInt(0));
            }
        }

        cursor.close();
    }


}