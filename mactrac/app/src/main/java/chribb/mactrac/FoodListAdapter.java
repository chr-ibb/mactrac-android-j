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

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        private final TextView foodItemView;

        private FoodViewHolder(View itemView) {
            super(itemView);
            foodItemView = itemView.findViewById(R.id.food_text);
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
            holder.foodItemView.setText(current.getFood());
        } else {
            // Covers the case of data not being ready yet.
            holder.foodItemView.setText("No Macros");
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
