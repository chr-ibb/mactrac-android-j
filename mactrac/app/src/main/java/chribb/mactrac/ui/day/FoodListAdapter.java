package chribb.mactrac.ui.day;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import chribb.mactrac.data.Macro;
import chribb.mactrac.R;

public class FoodListAdapter extends ListAdapter<Macro, FoodListAdapter.FoodViewHolder> {

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

    public FoodListAdapter(Context context) {
        super(DIFF_CALLBACK);
        inflater = LayoutInflater.from(context);
    }

    public static final DiffUtil.ItemCallback<Macro> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Macro>() {
                @Override
                public boolean areItemsTheSame(@NonNull Macro oldItem, @NonNull Macro newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Macro oldItem, @NonNull Macro newItem) {
                    //TODO return oldItem.equals(newItem) // you must implement equals first.
                    return false;
                }
            };

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        //TODO do I need to check if the list of macros is null?

        Macro current = getItem(position);

        holder.foodName.setText(current.getFood());
        String cal = current.getCalories() + " Calories";
        holder.foodCalories.setText(cal);
        String pro = current.getProtein() + "g Protein";
        holder.foodProtein.setText(pro);
        String fat = current.getFat() + "g Fat";
        holder.foodFat.setText(fat);
        String car = current.getCarbs() + "g Carbs";
        holder.foodCarbs.setText(car);
    }

    public Macro getMacro(int position) {
        return getItem(position);
    }

    //TODO Do I need to Override getItemCount?
//    // getItemCount() is called many times, and when it is first called,
//    // macros has not been updated (means initially, it's null, and we can't return null).
//    @Override
//    public int getItemCount() {
//        if (macros != null)
//            return macros.size();
//        else return 0;
//    }
}
