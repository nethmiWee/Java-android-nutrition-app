package nearchos.github.nutitioninfoapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList product_code, product_name, product_grade, product_group, product_nutrients, product_ingredients, product_spinner;

    boolean isSearch = false;
    ArrayList<Integer> filteredIndex  = new ArrayList<>();
    private MyViewHolder holder;
    private int position;

    CustomAdapter(Context context, ArrayList product_code,
                  ArrayList product_name, ArrayList product_grade, ArrayList product_group,
                  ArrayList product_nutrients, ArrayList product_ingredients, ArrayList product_spinner) {

        this.context = context;
        this.product_code = product_code;
        this.product_name = product_name;
        this.product_grade = product_grade;
        this.product_group = product_group;
        this.product_nutrients = product_nutrients;
        this.product_ingredients = product_ingredients;
        this.product_spinner = product_spinner;


    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_row, parent, false);
        return new MyViewHolder(view);
    }

    public void filterList(ArrayList filteredList) {
        ArrayList<String> product_code_filtered  = new ArrayList<>();
        ArrayList<String> product_name_filtered  =  new ArrayList<>();
        ArrayList<String> product_grade_filtered  =  new ArrayList<>();
        ArrayList<String> product_group_filtered  =  new ArrayList<>();
        ArrayList<String> product_nutrients_filtered  =  new ArrayList<>();
        ArrayList<String> product_ingredients_filtered  =  new ArrayList<>();
        ArrayList<String> product_spinner_filtered  = new ArrayList<>();



filteredIndex.clear();

        for(Object item : product_name) {
            for (Object list : filteredList) {
                if (list.equals(item) && !(filteredIndex.contains(item))) {
                    filteredIndex.add(product_name.indexOf(item));
                }

            }
        }

        try {
             if (filteredList.size() > 0) {
                 isSearch = true;

                for (int index : filteredIndex) {

                    Log.w("FilteredIndex", index + "");
                    Log.w("Size of index", "" + filteredIndex.size());
                    product_code_filtered.add((String) product_code.get(index));
                    product_grade_filtered.add((String) product_grade.get(index));
                    product_name_filtered.add((String) product_name.get(index));
                    product_group_filtered.add((String) product_group.get(index));
                    product_nutrients_filtered.add((String) product_nutrients.get(index));
                    product_ingredients_filtered.add((String) product_ingredients.get(index));
                    product_spinner_filtered.add((String) product_spinner.get(index));
                }


                product_name = product_name_filtered;
                product_grade = product_grade_filtered;
                product_code = product_code_filtered;
                product_group = product_group_filtered;
                product_ingredients = product_ingredients_filtered;
                product_nutrients = product_nutrients_filtered;
                product_spinner = product_spinner_filtered;

                notifyDataSetChanged();
            }



        }  catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int num = 0;
        boolean isVisible = false;

            if (isSearch) {

                //Shows filtered cards
                if(filteredIndex.contains(position) && position == filteredIndex.get(position)) {
                    num = filteredIndex.get(position);
                    isVisible = true;
                    Log.w("num", num + "");
                }


            } else {
                Log.w("notin", position + "");
                num = position;

            }


        if (isVisible || !isSearch) {
            holder.product_name_txt.setText(String.valueOf(product_name.get(num)));
            holder.product_grade_txt.setText(String.valueOf(product_grade.get(num)));


            try {
                getImageAccordingToSpinner(String.valueOf(product_spinner.get(num)), holder.imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("product_name", String.valueOf(product_name.get(position)));

                context.startActivity(intent);
            }
        });
    }

    //Changed from product_name
    @Override
    public int getItemCount() {
        return product_name.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

      TextView product_name_txt, product_grade_txt;
      ImageView imageView;
      LinearLayout layoutRow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutRow = itemView.findViewById(R.id.layoutRow);
            product_name_txt = itemView.findViewById(R.id.recentProductName);
            product_grade_txt = itemView.findViewById(R.id.recentProductGrade);
            imageView = itemView.findViewById(R.id.recentProductImage);



        }

    }

    public interface RecyclerViewClickListener{
      void onClick(View v, int position);
    }


    void getImageAccordingToSpinner(String product_spinner,
                                    ImageView rImage) throws IOException {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        String[] stringArray = context.getResources().getStringArray(R.array.names);


        DatabaseReference getImage = null;


        boolean isDiff = false;

        /*
        for(String item : stringArray){
            if(product_spinner.contains(item)) {
                isDiff = false;
                break;
            }else{
                isDiff = true;
            }

        }
*/
        switch (product_spinner) {
            case "Fruit":
                getImage = databaseReference.child("Apple");
                break;

            case "Healthy":
                getImage = databaseReference.child("Healthy");
                break;

            case "Grain":
                getImage = databaseReference.child("Grain");
                break;

            case "Seafood":
                getImage = databaseReference.child("Seafood");
                break;

            case "Vegetable":
                getImage = databaseReference.child("Vegetable");
                break;

            case "Fast-Food":
                getImage = databaseReference.child("Fast-Food");
                break;

            case "Sugar":
                getImage = databaseReference.child("Sugar");
                break;

            case "Poultry":
                getImage = databaseReference.child("Poultry");
                break;

            case "Alcohol":
                getImage = databaseReference.child("Alcohol");
                break;
            case "Cold-Drink":
                getImage = databaseReference.child("Cold-Drink");
                break;
            case "Dairy":
                getImage = databaseReference.child("Dairy");
                break;
            case "Hot-Drink":
                getImage = databaseReference.child("Hot-Drink");
                break;
            case "Milkshake":
                getImage = databaseReference.child("Milkshake");
                break;
            case "Misc":
                getImage = databaseReference.child("Misc");
                break;
            case "Noodles":
                getImage = databaseReference.child("Noodles");
                break;
            case "Pastry":
                getImage = databaseReference.child("Pastry");
                break;
            case "Crustacean":
                getImage = databaseReference.child("Crustacean");
                break;
            case "Unspecified":
                getImage = databaseReference.child("Unspecified");
                break;
            default:
                    String[] twoImages = product_spinner.split(",");
                    Glide.with(context.getApplicationContext()).load(twoImages[1]).dontAnimate().into(rImage);
                    isDiff = true;
                break;
        }

        if (!isDiff) {
            getImage.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String link = snapshot.getValue(String.class);
                    Glide.with(context.getApplicationContext()).load(link).dontAnimate().into(rImage);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context.getApplicationContext(), "Error Loading Image", Toast.LENGTH_SHORT).show();

                }

            });

        }
    }
}
