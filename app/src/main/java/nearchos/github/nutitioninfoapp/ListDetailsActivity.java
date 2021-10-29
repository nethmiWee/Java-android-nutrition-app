package nearchos.github.nutitioninfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ListDetailsActivity extends AppCompatActivity {
    MyDatabaseHelper myDB;
    ListView listView;
    ArrayList<String> productCodeForCurrent, ProductName, ProductSpinner;
    Button list_delete_button;
    TextView product_list_txt, lis_desc;
    String currentListId;
    ArrayAdapter adapter;

    //Update
    Button update_list_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        productCodeForCurrent = new ArrayList<>();
        ProductName = new ArrayList<>();
        ProductSpinner = new ArrayList<>();

        listView = findViewById(R.id.product_list_view);
        myDB = new MyDatabaseHelper(ListDetailsActivity.this);

        product_list_txt = findViewById(R.id.product_list_txt2);
        lis_desc = findViewById(R.id.lis_desc);

        getIntentData();
        populateList();


        //Delete button
       list_delete_button = findViewById(R.id.list_delete_button);
       list_delete_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               myDB.deleteList(currentListId);

               Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(refresh);
           }
       });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListDetailsActivity.this, ProductDetailsActivity.class);
                intent.putExtra("product_name", String.valueOf(ProductName.get(position)));
                startActivity(intent);
            }
        });

        //Update button

        update_list_button = findViewById(R.id.update_list_button);
        update_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogUpdate();
            }
        });

    }

    void confirmDialogUpdate(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update " + product_list_txt.getText().toString() + "?");
        builder.setMessage("Updating the list resets the products in the list, select yes to continue. ");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(ListDetailsActivity.this, CreateListActivity.class);
                intent.putExtra("isUpdateTrue", true);
                intent.putExtra("list_id", Integer.parseInt(currentListId));
                startActivity(intent);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    private void populateList() {
        Cursor cursor = myDB.readAllMapData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                if(currentListId.equals(cursor.getString(1))){
                    productCodeForCurrent.add(cursor.getString(0));
                }
            }
        }


        Cursor cursor2 = myDB.readAllProductData();

        if (cursor2.getCount() == 0) {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor2.moveToNext()) {
                for(int i = 0; i <productCodeForCurrent.size(); i++){
                    if(cursor2.getString(0).equals(productCodeForCurrent.get(i))){
                        ProductName.add(cursor2.getString(1));

                    }

                }
            }
        }


        cursor.close();

        cursor2.close();
        adapter = new ArrayAdapter<String>(this, R.layout.my_selected_item_spinner, ProductName);
        listView.setAdapter(adapter);

    }

    void getIntentData() {
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            product_list_txt.setText(extras.getString("list_name"));
            lis_desc.setText(extras.getString("list_description"));
            currentListId = extras.getString("list_id");
        }

    }



}