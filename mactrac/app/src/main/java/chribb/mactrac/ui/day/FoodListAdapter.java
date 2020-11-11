package chribb.mactrac.ui.day;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import chribb.mactrac.data.Macro;
import chribb.mactrac.R;

public class FoodListAdapter extends ListAdapter<Macro, FoodListAdapter.FoodViewHolder> {
    private final LayoutInflater inflater;
    private DayScreenSlideFragment.OnItemTouchListener onItemTouchListener;
    private String adapter_context;

    FoodListAdapter(Context context, DayScreenSlideFragment.OnItemTouchListener onItemTouchListener,
                    String adapter_context) {
        super(DIFF_CALLBACK);
        inflater = LayoutInflater.from(context);
        this.onItemTouchListener = onItemTouchListener;
        this.adapter_context = adapter_context;
    }

    private static final DiffUtil.ItemCallback<Macro> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Macro>() {
                @Override
                public boolean areItemsTheSame(@NonNull Macro oldItem, @NonNull Macro newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Macro oldItem, @NonNull Macro newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, int position) {
        //TODO do I need to check if the list of macros is null?

        Macro current = getItem(position);

        //TODO extract strings
        String name = current.getFood(); // + " Position: " + current.getPosition();
        holder.foodName.setText(name);
        String cal = current.getCalories() + " Calories";
        holder.foodCalories.setText(cal);
        String pro = current.getProtein() + "g Protein";
        holder.foodProtein.setText(pro);
        String fat = current.getFat() + "g Fat";
        holder.foodFat.setText(fat);
        String car = current.getCarbs() + "g Carbs";
        holder.foodCarbs.setText(car);
    }



    Macro getMacro(int position) {
        return getItem(position);
    }
    
    public void toggleButtonVis (int position) {
        
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView foodName;
        private final TextView foodCalories; //TODO Viewbinding
        private final TextView foodProtein;
        private final TextView foodFat;
        private final TextView foodCarbs;
        private final Button editButton;
        private final Button deleteButton;
        private final Button selectButton;
        private final TextView notes;

        private boolean detailsVisible;

        public FoodViewHolder(View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodCalories = itemView.findViewById(R.id.food_calories);
            foodProtein = itemView.findViewById(R.id.food_protein);
            foodFat = itemView.findViewById(R.id.food_fat);
            foodCarbs = itemView.findViewById(R.id.food_carbs);

            editButton = itemView.findViewById(R.id.button_food_edit);
            deleteButton = itemView.findViewById(R.id.button_food_delete);
            selectButton = itemView.findViewById(R.id.button_food_select);
            notes = itemView.findViewById(R.id.food_note);
            detailsVisible = false;
//            itemView.setOnClickListener(this);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getLayoutPosition, getBindingAdapterPosition() or getAbsoluteAdapterPosition() depending on use case
                    onItemTouchListener.onCardViewTap(v, getLayoutPosition());
                    toggleDetails();
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onEditClick(v, getLayoutPosition());
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onDeleteClick(v, getLayoutPosition());
                }
            });
        }


        @Override
        public void onClick(View v) {
            //TODO this is empty
        }

        public void toggleDetails() {
            if (adapter_context == "day") {
                toggleDetailsDay();
            } else if (adapter_context == "add") {
                toggleDetailsAdd();
            }
            detailsVisible = !detailsVisible;
        }
        private void toggleDetailsDay() {
            if (detailsVisible) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                notes.setVisibility(View.GONE);
            } else {
                editButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.VISIBLE);
                notes.setVisibility(View.VISIBLE);
            }
        }
        private void toggleDetailsAdd() {
            if (detailsVisible) {
                deleteButton.setVisibility(View.GONE);
                selectButton.setVisibility(View.GONE);
            } else {
                deleteButton.setVisibility(View.VISIBLE);
                selectButton.setVisibility(View.VISIBLE);
            }
        }

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
