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

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import chribb.mactrac.AppBarViewModel;
import chribb.mactrac.data.Macro;
import chribb.mactrac.R;

public class DayScreenSlideFragment extends Fragment {
    private DayViewModel dayViewModel;
    private AppBarViewModel appBarViewModel;
    private FoodListAdapter adapter;
    private int daysSinceEpoch;
    private ItemTouchHelper helper;
    private boolean isEditMode;

    private View thisView;

    private Macro deleteMacro;
    private int deletePosition;
    private TextView editModeTextView;

    /* This is the standard way of "instantiating" a new fragment with data to pass in,
     * since you cannot make a custom constructor for a fragment. */
    static DayScreenSlideFragment newInstance(int daysSinceEpoch) {
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

        assert getArguments() != null;
        daysSinceEpoch = getArguments().getInt("daysSinceEpoch", 0);


        //TODO can i put this somewhere else?
        helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
                | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {



            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                List<Macro> macrosCopy = new ArrayList<>(adapter.getCurrentList());
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i ++) {
                        // TODO consider making  a method swapMacroPositions(list, int, int)
                        Collections.swap(macrosCopy, i, i + 1);
                        Macro macro1 = macrosCopy.get(i);
                        Macro macro2 = macrosCopy.get(i + 1);
                        int position1 = macro1.getPosition();
                        int position2 = macro2.getPosition();
                        macro1.setPosition(position2);
                        macro2.setPosition(position1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i --) {
                        Collections.swap(macrosCopy, i, i - 1);
                        Macro macro1 = macrosCopy.get(i);
                        Macro macro2 = macrosCopy.get(i - 1);
                        int position1 = macro1.getPosition();
                        int position2 = macro2.getPosition();
                        macro1.setPosition(position2);
                        macro2.setPosition(position1);
                    }
                }
                adapter.submitList(macrosCopy);
//                adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                dayViewModel.update(adapter.getCurrentList());
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                List<Macro> macrosCopy = new ArrayList<>(adapter.getCurrentList());
                deletePosition = viewHolder.getAdapterPosition();
                deleteMacro = adapter.getMacro(deletePosition);
                dayViewModel.deleteFood(deleteMacro.getId());
                macrosCopy.remove(deletePosition);

                for (int i = deletePosition; i < macrosCopy.size(); i++) {
                    macrosCopy.get(i).setPosition(i);
                }

                dayViewModel.update(macrosCopy);
                showUndoSnackbar();
            }

            @Override
            public boolean isItemViewSwipeEnabled() {
                return isEditMode;
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return isEditMode;
            }


        });

        return inflater.inflate(
                R.layout.fragment_screen_slide_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thisView = view;

        appBarViewModel.getEditPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed && dayViewModel.getDayOnScreen() == daysSinceEpoch) {
                    // TODO there is a bug when pressing edit button after adding where it doesn't
                    // toggle edit mode for the correct day, presumably because you're observing
                    // this on all the dayscreenslidefragments that are active instead of just one
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

        editModeTextView = view.findViewById(R.id.edit_mode_text);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new FoodListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        helper.attachToRecyclerView(recyclerView);

        dayViewModel.loadFood(daysSinceEpoch).observe(getViewLifecycleOwner(), new Observer<List<Macro>>() {
            @Override
            public void onChanged(@Nullable final List<Macro> macros) {

                // Update the List of macros in ListAdapter
                adapter.submitList(macros);

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

        disableEditMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        //disable edit mode when you leave this 'day'
        disableEditMode();
    }

    private void toggleEditMode() {
        if (isEditMode) {
            disableEditMode();
        } else {
            enableEditMode();
        }
    }
    private void enableEditMode() {
        //TODO  Make a textview visable that says "swipe to delete, click to edit"
        // change layout to like highlight the section to be edited. make it noticably different
        // maybe increase the "height" of each recycler item, like its floating higher now (shadow)
        editModeTextView.setVisibility(View.VISIBLE);
        isEditMode = true;
    }
    private void disableEditMode() {
        //TODO get rid of textView, change layout back
        editModeTextView.setVisibility(View.GONE);
        isEditMode = false;
    }

    private void showUndoSnackbar() {
        //TODO extract strings
        String snackString = "Deleted " + deleteMacro.getFood();
        Snackbar snackbar = Snackbar.make(thisView, snackString, Snackbar.LENGTH_LONG);
        snackbar.setAction("Click to Undo", f -> undoDelete());
        snackbar.show();
    }
    private void undoDelete() {
        List<Macro> macrosCopy = new ArrayList<>(adapter.getCurrentList());
        for (int i = deletePosition; i < macrosCopy.size(); i++) {
            macrosCopy.get(i).setPosition(i + 1);
        }
        dayViewModel.update(macrosCopy);
        dayViewModel.insert(deleteMacro);
    }
}
