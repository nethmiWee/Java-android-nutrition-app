 package nearchos.github.nutitioninfoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

 public class AddActivity extends AppCompatActivity {

     FloatingActionButton zxing_barcode_scanner;
     EditText productCode_input, productName_input, grade_input, group_input, ingredients_input, nutrients_input, sodium, calcium, fat, sugar, energy, cholestrol, imageMain;
     Button add_button;
     GetFoodApi getFoodApi = new GetFoodApi();
     LottieAnimationView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //Loader
        loading = findViewById(R.id.loader);

        //Get input
        productCode_input = findViewById(R.id.productCode_input);
        productName_input = findViewById(R.id.productName_input);
        grade_input = findViewById(R.id.grade_input);
        group_input = findViewById(R.id.group_input);
        ingredients_input = findViewById(R.id.ingredients_input);
        nutrients_input = findViewById(R.id.nutrients_input);
        sodium = findViewById(R.id.Sodium);
        fat = findViewById(R.id.Fat);
        calcium = findViewById(R.id.calcium);
        cholestrol = findViewById(R.id.cholesterol);
        sugar = findViewById(R.id.Sugar);
        energy = findViewById(R.id.Energy);
        imageMain = findViewById(R.id.imageMain);

        //Spinner for selecting icon type
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_selected_item_spinner, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(R.layout.my_drop_down_items);
        mySpinner.setAdapter(myAdapter);


        //Scan barcode
        zxing_barcode_scanner = findViewById(R.id.zxing_barcode_scanner);
        zxing_barcode_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCode();

            }
        });


        //Add to the database the new product

        add_button = findViewById(R.id.product_add_button_home);
        add_button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String spinnerState = "";
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);

                Log.w("url", "" +  imageMain.getText().toString());

                if(!( imageMain.getText().toString().trim().equals("Name"))){
                    spinnerState =  imageMain.getText().toString();
                } else {
                    spinnerState = mySpinner.getSelectedItem().toString();
                }

                String[] holder = {sodium.getText().toString(), calcium.getText().toString(), fat.getText().toString(), sugar.getText().toString(), energy.getText().toString(), cholestrol.getText().toString()};
                String[] value = {"Sodium", "calcium", "fat", "sugar", "energy", "cholesterol"};
                String build = "";
                for(int i = 0; i < holder.length; i++){
                    build = build + value[i] + " " + holder[i];

                    if(i != holder.length - 1){
                        build = build + ", ";
                    }
                }

                nutrients_input.setText(build);

                myDB.addProduct(productCode_input.getText().toString().trim(), productName_input.getText().toString().trim(),
                        grade_input.getText().toString().trim().toUpperCase(), Integer.valueOf(group_input.getText().toString().trim()),
                        nutrients_input.getText().toString().trim(),
                        ingredients_input.getText().toString().trim(), spinnerState);

                Intent refresh = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(refresh);

            }
        });



    }


     /*
     Open camera to scan
     */
    private void scanCode(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureBarcodeActivity.class);
        integrator.setOrientationLocked((false));
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();

    }

     /*
    When scanning
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents() != null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Scanning result");

                builder.setPositiveButton("Scan Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scanCode();
                    }
                }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productCode_input.setText(result.getContents());
                        getFoodApi.jsonParse(AddActivity.this, productCode_input ,productName_input, grade_input, group_input, ingredients_input, nutrients_input,
                                sodium, calcium, fat, sugar, energy, cholestrol, imageMain, loading);
                        Log.w("takenVal", imageMain.getText().toString() + "");
                    }


                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else {
                Toast.makeText(this,"No Results", Toast.LENGTH_LONG).show();

            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
