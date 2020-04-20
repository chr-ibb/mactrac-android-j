package chribb.mactrac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView foodName;
        private final TextView foodCalories; //TODO Viewbinding
        private final TextView foodProtein;
        private final TextView foodFat;
        private final TextView foodCarbs;

        private FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodCalories = itemView.findViewById(R.id.food_calories);
            foodProtein = itemView.findViewById(R.id.food_protein);
            foodFat = itemView.findViewById(R.id.food_fat);
            foodCarbs = itemView.findViewById(R.id.food_carbs);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }

    private final LayoutInflater inflater;
    private List<Macro> macros; // Cached copy of macros

    public FoodListAdapter(Context context) { inflater = LayoutInflater.from(context); }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        if (macros != null) {
            Macro current = macros.get(position);

            holder.foodName.setText(current.getFood());
            String cal = current.getCalories() + " Calories";
            holder.foodCalories.setText(cal);
            String pro = current.getProtein() + "g Protein";
            holder.foodProtein.setText(pro);
            String fat = current.getFat() + "g Fat";
            holder.foodFat.setText(fat);
            String car = current.getCarbs() + "g Carbs";
            holder.foodCarbs.setText(car);

        } else {
            // Covers the case of data not being ready yet.
            //TODO is this neccessary? / extract
            holder.foodName.setText("Data Not Ready");
        }
    }

    public void setMacros(List<Macro> macros){
        this.macros = macros;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // macros has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (macros != null)
            return macros.size();
        else return 0;
    }
}
