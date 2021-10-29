package nearchos.github.nutitioninfoapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class GetFoodApi extends AppCompatActivity {
    String name, group, grade, ingredients, nutrients;
    private RequestQueue mQueue;
    Context context;


    public GetFoodApi() {

    }

    void jsonParse(Context context, EditText barcode, EditText productName_input, EditText grade_input, EditText group_input, EditText ingredients_input, EditText nutrients_input, EditText sodium, EditText calcium, EditText fat, EditText sugar, EditText energy, EditText cholestrol, EditText imageMain, LottieAnimationView loading){

        mQueue = Volley.newRequestQueue(context);
        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode.getText().toString().trim();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    loading.setVisibility(View.INVISIBLE);
                    JSONObject product = response.getJSONObject("product");

                    name = product.getString("generic_name");
                    grade = product.getString("nutrition_grade_fr");
                    ingredients = product.getString("ingredients_text");

                    group = product.getString("nova_group");

                  String imageOf = product.getString("image_url") + "," + product.getString("image_thumb_url");
                    imageMain.setText(imageOf);
                    Log.w("takenUrlLast: ", imageMain.getText().toString() + "");

                    productName_input.setText(name);
                    group_input.setText(group);
                    grade_input.setText(grade.toUpperCase());

                    ingredients_input.setText(ingredients);

                    JSONObject nutriments = product.getJSONObject("nutriments");
                    String tmp = nutriments.getString("calcium") + " " + nutriments.getString("calcium_unit");

                    calcium.setText(tmp);

                    tmp = nutriments.getString("cholesterol") + " " + nutriments.getString("cholesterol_unit");

                    cholestrol.setText(tmp);

                    tmp = nutriments.getString("sodium") + " " +  nutriments.getString("sodium_unit");
                    sodium.setText(tmp);

                    tmp = nutriments.getString("sugars") + " " + nutriments.getString("sugars_unit");
                    sugar.setText(tmp);

                    tmp = nutriments.getString("fat") + " " + nutriments.getString("fat_unit");
                    fat.setText(tmp);

                    tmp = nutriments.getString("energy") + " " + nutriments.getString("energy_unit");
                    energy.setText(tmp);

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


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Try again", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.setVisibility(View.INVISIBLE);
                error.printStackTrace();
                Toast.makeText(context, "Cant find product", Toast.LENGTH_LONG).show();
            }
        });

        mQueue.add(request);
        loading.setVisibility(View.VISIBLE);
    }
}
