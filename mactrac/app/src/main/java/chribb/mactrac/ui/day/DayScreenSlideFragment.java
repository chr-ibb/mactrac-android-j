package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import chribb.mactrac.AppBarViewModel;
import chribb.mactrac.FoodListAdapter;
import chribb.mactrac.Macro;
import chribb.mactrac.R;

public class DayScreenSlideFragment extends Fragment {
    private DayViewModel dayViewModel;
    private AppBarViewModel appBarViewModel;
    private FoodListAdapter adapter;
    private int daysSinceEpoch;
    private ItemTouchHelper helper;
    private boolean isEditMode;
    private boolean isRecycleSwipeable;
    private boolean isRecycleDraggable;

    private Macro deleteMacro;
    private int deletePosition;

    /* This is the standard way of "instantiating" a new fragment with data to pass in,
     * since you cannot make a custom constructor for a fragment. */
    public static DayScreenSlideFragment newInstance(int daysSinceEpoch) {
        DayScreenSlideFragment fragment = new DayScreenSlideFragment();

        Bundle args = new Bundle();
        args.putInt("daysSinceEpoch", daysSinceEpoch);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dayViewModel = new ViewModelProvider(requireParentFragment()).get(DayViewModel.class);
        appBarViewModel = new ViewModelProvider(requireActivity()).get(AppBarViewModel.class);

        //TODO can i put this somewhere else?
        isEditMode = false;
        isRecycleSwipeable = false;
        isRecycleDraggable = false;
        helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
                | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deletePosition = viewHolder.getAdapterPosition();
                adapter.notifyItemRemoved(deletePosition);
                deleteMacro = adapter.getMacro(deletePosition);
                dayViewModel.deleteFood(deleteMacro.getId());
                //TODO this messes up the animations, and now I'm realizing that Maybe I should
                // be using listadapter instead, which handles when to redraw the recyclerview
                // instead of just always redrawing the entire thing like I'm doing...
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return isRecycleSwipeable;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return isRecycleDraggable;
            }
        });

        return (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appBarViewModel.getEditPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    toggleEditMode();
                    appBarViewModel.setEditPressed(false);
                }
            }
        });

        //TODO consider view binding or data binding
        TextView dateText = view.findViewById(R.id.date_text);
        TextView relativeDayText = view.findViewById(R.id.relative_day_text);
        TextView totalCaloriesText = view.findViewById(R.id.total_calories_text);
        TextView totalProteinText = view.findViewById(R.id.total_protein_text);
        TextView totalFatText = view.findViewById(R.id.total_fat_text);
        TextView totalCarbsText = view.findViewById(R.id.total_carbs_text);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new FoodListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helper.attachToRecyclerView(recyclerView);

        assert getArguments() != null;
        daysSinceEpoch = getArguments().getInt("daysSinceEpoch", 0);

        dayViewModel.loadFood(daysSinceEpoch).observe(getViewLifecycleOwner(), new Observer<List<Macro>>() {
            @Override
            public void onChanged(@Nullable final List<Macro> macros) {
                // Update the cached copy of the words in the adapter.
                adapter.setMacros(macros);
                int calories = 0;
                int protein = 0;
                int fat = 0;
                int carbs = 0;

                for (int i = 0; i < macros.size(); i++) {
                    Macro current = macros.get(i);
                    calories += current.getCalories();
                    protein += current.getProtein();
                    fat += current.getFat();
                    carbs += current.getCarbs();
                }
                String totalCalories = calories + " Calories";
                totalCaloriesText.setText(totalCalories);
                String totalProtein = protein + "g Protein";
                totalProteinText.setText(totalProtein);
                String totalFat = fat + "g Fat";
                totalFatText.setText(totalFat);
                String totalCarbs = carbs + "g Carbs";
                totalCarbsText.setText(totalCarbs);
            }
        });

        dateText.setText(dayViewModel.getDateText(daysSinceEpoch));
        relativeDayText.setText(dayViewModel.getRelativeDayText(daysSinceEpoch));

    }

    @Override
    public void onPause() {
        super.onPause();
        //disable edit mode when you leave this 'day'
        disableEditMode();
    }

    public void toggleEditMode() {
        if (isEditMode) {
            disableEditMode();
        } else {
            enableEditMode();
        }
    }
    public void enableEditMode() {
        //TODO  Make a textview visable that says "swipe to delete, click to edit"
        // change layout to like highlight the section to be edited. make it noticably different
        isEditMode = true;
        isRecycleSwipeable = true;
        isRecycleDraggable = true;
    }
    public void disableEditMode() {
        //TODO get rid of textView, change layout back
        isEditMode = false;
        isRecycleSwipeable = false;
        isRecycleDraggable = false;
    }
}
