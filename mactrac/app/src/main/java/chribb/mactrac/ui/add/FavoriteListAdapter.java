package chribb.mactrac.ui.add;

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

import chribb.mactrac.data.Favorite;
import chribb.mactrac.R;

public class FavoriteListAdapter extends ListAdapter<Favorite, FavoriteListAdapter.FavoriteViewHolder> {
    private final LayoutInflater inflater;
    AddNameFragment.OnItemTouchListener onItemTouchListener;

    FavoriteListAdapter(Context context, AddNameFragment.OnItemTouchListener onItemTouchListener) {
        super(DIFF_CALLBACK);
        inflater = LayoutInflater.from(context);
        this.onItemTouchListener = onItemTouchListener;
    }

    private static final DiffUtil.ItemCallback<Favorite> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Favorite>() {
                @Override
                public boolean areItemsTheSame(@NonNull Favorite oldItem, @NonNull Favorite newItem) {
                    return oldItem.getName().equals(newItem.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Favorite oldItem, @NonNull Favorite newItem) {
                    return oldItem.equals(newItem);
                }
            };

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.food_item, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        //TODO do I need to check if the list of Favorites is null?

        Favorite current = getItem(position);

        //TODO extract strings
        String name = current.getName();
        holder.FavoriteName.setText(name);
        String cal = current.getCalories() + " Calories";
        holder.FavoriteCalories.setText(cal);
        String pro = current.getProtein() + "g Protein";
        holder.FavoriteProtein.setText(pro);
        String fat = current.getFat() + "g Fat";
        holder.FavoriteFat.setText(fat);
        String car = current.getCarbs() + "g Carbs";
        holder.FavoriteCarbs.setText(car);
    }



    Favorite getFavorite(int position) {
        return getItem(position);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView FavoriteName;
        private final TextView FavoriteCalories; //TODO Viewbinding
        private final TextView FavoriteProtein;
        private final TextView FavoriteFat;
        private final TextView FavoriteCarbs;
        private final Button deleteButton;
        private final Button selectButton;
        private boolean buttonsVisible;

        private FavoriteViewHolder(View itemView) {
            super(itemView);
            FavoriteName = itemView.findViewById(R.id.food_name);
            FavoriteCalories = itemView.findViewById(R.id.food_calories);
            FavoriteProtein = itemView.findViewById(R.id.food_protein);
            FavoriteFat = itemView.findViewById(R.id.food_fat);
            FavoriteCarbs = itemView.findViewById(R.id.food_carbs);

            deleteButton = itemView.findViewById(R.id.button_food_delete);
            selectButton = itemView.findViewById(R.id.button_food_select);
            buttonsVisible = false;

//            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getLayoutPosition, getBindingAdapterPosition() or getAbsoluteAdapterPosition() depending on use case
                    onItemTouchListener.onCardViewTap(v, getLayoutPosition());
                    toggleButtons();
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onDeleteClick(v, getLayoutPosition());
                    toggleButtons();
                }
            });

            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onSelectClick(v, getLayoutPosition());
                    toggleButtons();
                }
            });
        }


        @Override
        public void onClick(View v) {
            //TODO this is empty
        }

        private void toggleButtons() {
            if (buttonsVisible) {
                deleteButton.setVisibility(View.GONE);
                selectButton.setVisibility(View.GONE);
            } else {
                deleteButton.setVisibility(View.VISIBLE);
                selectButton.setVisibility(View.VISIBLE);
            }
            buttonsVisible = !buttonsVisible;
        }
    }

    //TODO Do I need to Override getItemCount?
//    // getItemCount() is called many times, and when it is first called,
//    // Favorites has not been updated (means initially, it's null, and we can't return null).
//    @Override
//    public int getItemCount() {
//        if (Favorites != null)
//            return Favorites.size();
//        else return 0;
//    }
}
