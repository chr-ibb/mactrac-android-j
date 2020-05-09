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
    private RecyclerView recyclerView;
    private FoodListAdapter adapter;
    private int daysSinceEpoch;
    private ItemTouchHelper itemTouchHelper;
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

        itemTouchHelper = makeItemTouchHelper();

        return inflater.inflate(
                R.layout.fragment_screen_slide_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        thisView = view;
        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new FoodListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        editModeTextView = view.findViewById(R.id.edit_mode_text);
        //TODO consider view binding or data binding
        TextView dateText = view.findViewById(R.id.date_text);
        TextView relativeDayText = view.findViewById(R.id.relative_day_text);
        TextView totalCaloriesText = view.findViewById(R.id.total_calories_text);
        TextView totalProteinText = view.findViewById(R.id.total_protein_text);
        TextView totalFatText = view.findViewById(R.id.total_fat_text);
        TextView totalCarbsText = view.findViewById(R.id.total_carbs_text);

        dateText.setText(dayViewModel.getDateText(daysSinceEpoch));
        relativeDayText.setText(dayViewModel.getRelativeDayText(daysSinceEpoch));

        disableEditMode();

        /* AppBar button observer for toggling edit mode */
        appBarViewModel.getEditPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed && dayViewModel.getDayOnScreen() == daysSinceEpoch) {
                    // TODO there is a bug when pressing edit button after adding where it doesn't
                    // toggle edit mode for the correct day, presumably because you're observing
                    // this on all the DayScreenSlideFragments that are active instead of just one
                    toggleEditMode();
                    appBarViewModel.setEditPressed(false);
                }
            }
        });

        /* Observer for the list of Macros that belong to this day */
        dayViewModel.loadFood(daysSinceEpoch).observe(getViewLifecycleOwner(), new Observer<List<Macro>>() {

            /**
             * Submits MACROS to adapter, visually updating screen if the list 'looks' different.
             * Calculates totals for each macro type and updates textViews with the results.
             *
             * @param macros List of macros for this day from Room.
             */
            @Override
            public void onChanged(@Nullable final List<Macro> macros) {

                adapter.submitList(macros);

                //TODO move as much out/into view model as possible
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
    }

    /**
     * Called when Fragment is paused, more or less when the fragment leaves the screen.
     * Disables edit mode if it is enabled.
     */
    @Override
    public void onPause() {
        super.onPause();
        disableEditMode();
    }


    /* Private Methods */


    /**
     * Creates the appropriate ItemTouchHelper for the recycler view and returns it.
     * Solely exists to move the code out of onCreateView
     * @return a new ItemTouchHelper to attach to the recycler view
     */
    private ItemTouchHelper makeItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
                | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            /**
             * Called when an item in recycler view is moved (up or down, after long touch).
             * "moved" does not mean dragged and dropped, it means dragged over a different item.
             * This can be called multiple times before the item is dropped.
             * It's my understanding that while the 'to' and 'from' positions are usually adjacent,
             * they can be further apart with quick drags.
             *
             * Creates a copy of the list of macros in the adapter for editing. Shifts over every
             * item between the 'from' and 'to' positions by swapping with each item on the way,
             * inserting the item where it was dragged to. Updates the POSITION of each macro while
             * shifting. Updates the list of macros in adapter with the changed copy.
             *
             * DOES NOT update the Room Database. This is done when the item is finally dropped.
             * @param recyclerView Recycler view where the 'move' is taking place.
             * @param viewHolder ViewHolder holding the view/item that is being moved.
             * @param target ViewHolder holding the view/item that is being moved to.
             * @return Boolean, whether or not the item was moved.
             */
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
                return true;
            }

            /**
             * Called when an item in the recycler view is dropped from being dragged (moved).
             * Now that the Macro positions are finished updating from the move, update Room.
             * @param recyclerView RecyclerView in which item was dragged and dropped.
             * @param viewHolder ViewHolder holding the view that was dragged and dropped.
             */
            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                dayViewModel.update(adapter.getCurrentList());
            }

            /**
             * Called when an item in the recycler view is swiped away (left or right).
             * Creates a copy of the macro list, deletes the macro from room (which updates the
             * visible list on screen automatically), corrects the POSITION of each macro after
             * the deleted one, and updates Room with the new position values.
             * Shows snackbar for undoing delete.
             * @param viewHolder ViewHolder in which item was swiped.
             * @param direction Direction of the swipe (irrelevant, both directions work the same).
             */
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

            /**
             * isEditMode gets toggled whenever the edit button is pressed, and gets set to false
             * when the fragment is paused.
             * TODO update these two comments when the method for toggling edit mode is changed
             * @return whether or not swiping items in recycler view is currently enabled
             */
            @Override
            public boolean isItemViewSwipeEnabled() {
                return isEditMode;
            }

            /**
             * isEditMode gets toggled whenever the edit button is pressed, and gets set to false
             * when the fragment is paused.
             * @return whether or not long press dragging in recycler view is currently enabled
             */
            @Override
            public boolean isLongPressDragEnabled() {
                return isEditMode;
            }


        });
    }

    /**
     * Toggles edit mode.
     */
    private void toggleEditMode() {
        if (isEditMode) {
            disableEditMode();
        } else {
            enableEditMode();
        }
    }

    /**
     * Enables edit mode.
     */
    private void enableEditMode() {
        //TODO  Make a textview visable that says "swipe to delete, click to edit"
        // change layout to like highlight the section to be edited. Make it noticeably different.
        // Maybe increase the "height" of each recycler item, like its floating higher now (shadow)
        editModeTextView.setVisibility(View.VISIBLE);

        isEditMode = true;
    }

    /**
     * Disables edit mode
     */
    private void disableEditMode() {
        //TODO get rid of textView, change layout back
        editModeTextView.setVisibility(View.GONE);
        isEditMode = false;
    }

    /**
     * Handles showing a Snackbar when a Macro is swipe deleted, allowing the option to Undo
     * the deletion by clicking the undo button.
     * In English the String displayed will be "Deleted [food]",
     * and the undo button will read "Click to Undo"
     */
    private void showUndoSnackbar() {
        //TODO extract strings
        String snackString = "Deleted " + deleteMacro.getFood();
        Snackbar snackbar = Snackbar.make(thisView, snackString, Snackbar.LENGTH_LONG);
        snackbar.setAction("Click to Undo", f -> undoDelete());
        snackbar.show();
    }

    /**
     * Undoes the most previous deletion. Updates the POSITION of each macro below the insertion.
     */
    private void undoDelete() {
        List<Macro> macrosCopy = new ArrayList<>(adapter.getCurrentList());
        for (int i = deletePosition; i < macrosCopy.size(); i++) {
            macrosCopy.get(i).setPosition(i + 1);
        }
        dayViewModel.update(macrosCopy);
        dayViewModel.insert(deleteMacro);
    }

    /**
     * For testing. Shows a snackbar when DayScreenSlideFragment is paused, displaying which day's
     * fragment was paused.
     */
    private void showPausedSnackbar() {
        String dayString = dayViewModel.getDateText(daysSinceEpoch);
        String snackString = dayString + " PAUSED";
        Snackbar snackbar = Snackbar.make(thisView, snackString, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
