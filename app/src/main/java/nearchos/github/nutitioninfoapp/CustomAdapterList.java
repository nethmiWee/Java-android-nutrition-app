package nearchos.github.nutitioninfoapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapterList extends RecyclerView.Adapter<CustomAdapterList.MyViewHolder> {

    Context context;
    ArrayList product_list_name, list_id, list_description, list_favs, list_colour;
    boolean isSearch;
    boolean isSearchList = false;
    ArrayList<Integer> filteredIndex  = new ArrayList<>();

    CustomAdapterList(Context context, ArrayList product_list_name, ArrayList list_id, ArrayList list_description, ArrayList list_favs, ArrayList list_colour, boolean isSearch){
        this.context = context;
        this.list_description = list_description;
        this.list_id = list_id;
        this.product_list_name = product_list_name;
        this.list_favs = list_favs;
        this.list_colour = list_colour;
        this.isSearch = isSearch;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = null;
        view = inflater.inflate(R.layout.list_row, parent, false);

        if(isSearch) {
            view = inflater.inflate(R.layout.product_row, parent, false);
        }


      return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int num = 0;
        boolean isVisible = false;
        if(isSearch) {

            if (isSearchList) {

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
            holder.product_name_txt.setText(String.valueOf(product_list_name.get(num)));
            holder.product_grade_txt.setText(String.valueOf(list_id.get(num)));
            holder.textView7.setText("List ID:");
            Glide.with(context.getApplicationContext()).load("https://img.icons8.com/officel/80/ffffff/List-of-parts.png").dontAnimate().into(holder.imageView);

            holder.layoutRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ListDetailsActivity.class);
                    intent.putExtra("list_name", String.valueOf(product_list_name.get(position)));
                    intent.putExtra("list_id", String.valueOf(list_id.get(position))); //Changed
                    intent.putExtra("list_description", String.valueOf(list_description.get(position)));
                    context.startActivity(intent);
                }
            });

        } else if(!isSearch){

            holder.product_list_txt.setText(String.valueOf(product_list_name.get(position)));

            if (String.valueOf(list_favs.get(position)).equals("1")) {
                holder.star.setVisibility(View.VISIBLE);
            } else {
                holder.star.setVisibility(View.INVISIBLE);
            }

            int colorChange = 1;

            switch (String.valueOf(list_colour.get(position)).trim()) {
                case "1":

                    break;
                case "2":
                    colorChange = Color.parseColor("#A61010");
                    holder.circle.setColorFilter(colorChange);
                    break;
                case "3":
                    colorChange = Color.parseColor("#E36B93");
                    holder.circle.setColorFilter(colorChange);
                    break;
                case "4":
                    colorChange = Color.parseColor("#4C93BA");
                    holder.circle.setColorFilter(colorChange);
                    break;
                case "5":
                    colorChange = Color.parseColor("#71AE73");
                    holder.circle.setColorFilter(colorChange);
                    break;

            }


            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ListDetailsActivity.class);
                    intent.putExtra("list_name", String.valueOf(product_list_name.get(position)));
                    intent.putExtra("list_id", String.valueOf(list_id.get(position))); //Changed
                    intent.putExtra("list_description", String.valueOf(list_description.get(position)));
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return product_list_name.size();
    }

    public void filterList(ArrayList<String> filteredList) {
        ArrayList<String> list_description_filtered = new ArrayList<>();
        ArrayList<String> list_id_filtered = new ArrayList<>();
        ArrayList<String> list_favs_filtered = new ArrayList<>();
        ArrayList<String> list_colour_filtered = new ArrayList<>();
        ArrayList<String> product_list_name_filtered = new ArrayList<>();

        filteredIndex.clear();

        for (Object item : product_list_name) {
            for (Object list : filteredList) {
                if (list.equals(item) && !(filteredIndex.contains(item))) {
                    filteredIndex.add(product_list_name.indexOf(item));
                }

            }
        }

        try {
            if (filteredList.size() > 0) {
                isSearchList = true;

                for (int index : filteredIndex) {

                    Log.w("FilteredIndex", index + "");
                    Log.w("Size of index", "" + filteredIndex.size());
                    product_list_name_filtered.add((String) product_list_name.get(index));
                    list_favs_filtered.add((String) list_favs.get(index));
                    list_colour_filtered.add((String) list_colour.get(index));
                    list_description_filtered.add((String) list_description.get(index));
                    list_id_filtered.add((String) list_id.get(index));

                }


                product_list_name = product_list_name_filtered;
                list_favs = product_list_name_filtered;
                list_colour = list_colour_filtered;
                list_description = list_description_filtered;
                list_id = list_id_filtered;

                notifyDataSetChanged();
            }
}  catch (Exception e) {
        e.printStackTrace();
        }
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        //List row
        TextView product_name_txt, product_grade_txt, textView7;
        LinearLayout layoutRow;
        ImageView imageView;

        //List circle
        TextView product_list_txt;
        ImageView star;
        ImageView circle;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //Row
            product_name_txt = itemView.findViewById(R.id.recentProductName);
            product_grade_txt = itemView.findViewById(R.id.recentProductGrade);
            textView7= itemView.findViewById(R.id.nutritionText);
            layoutRow = itemView.findViewById(R.id.layoutRow);
            imageView = itemView.findViewById(R.id.recentProductImage);

            //Circle
                product_list_txt = itemView.findViewById(R.id.listName);
                mainLayout = itemView.findViewById(R.id.mainLayout);
                star = itemView.findViewById(R.id.recentListStar);
                circle = itemView.findViewById(R.id.circle);

        }
    }

}
