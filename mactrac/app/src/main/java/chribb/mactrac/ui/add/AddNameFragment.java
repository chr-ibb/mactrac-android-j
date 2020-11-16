package chribb.mactrac.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import chribb.mactrac.R;
import chribb.mactrac.data.Favorite;
import chribb.mactrac.data.Macro;
import chribb.mactrac.ui.day.DayScreenSlideFragment;


public class AddNameFragment extends Fragment {
    private AddViewModel addViewModel;
    private NavController navController;
    private RecyclerView recyclerView;
    private FavoriteListAdapter adapter;
    private View thisView;

    private Favorite favoriteToDelete;
    private int deletePosition;

    private EditText editName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
        View view = inflater.inflate(R.layout.fragment_add_name, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO move to method to lessen clutter in onViewCreated(here)
        OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Toast.makeText(requireContext(), "Tapped position" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(View view, int position) {
                Toast.makeText(requireContext(), "Clicked Delete on position " + position, Toast.LENGTH_SHORT).show();
                deleteFavorite(position);
            }

            @Override
            public void onSelectClick(View view, int position) {
                Toast.makeText(requireContext(), "Clicked Select on position " + position, Toast.LENGTH_SHORT).show();
                selectFavorite(position);
            }
        };

        thisView = view;
        navController = Navigation.findNavController(view);

        assert getArguments() != null;
        addViewModel.setDay(AddNameFragmentArgs.fromBundle(getArguments()).getDay());
        //Queries Room for number of macros on this day and sets result to COUNT in viewModel
        addViewModel.findCount();

        recyclerView = view.findViewById(R.id.recyclerview_add_name);
        adapter = new FavoriteListAdapter(getContext(), itemTouchListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.submitList(addViewModel.getSortedFavoritesWithPrefix(""));



        editName = view.findViewById(R.id.edit_name);
        editName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable mEdit)
            {
                String text = mEdit.toString();
                List<Favorite> l = addViewModel.getSortedFavoritesWithPrefix(text);
                adapter.submitList(l);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

        Button nextButton = view.findViewById(R.id.button_name_next);

//        editName.requestFocus();
//        Utils.showKeyboardTo(getContext(), editName);

        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    nextButton.callOnClick();
                    handled = true;
                }
                return handled;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO remove keyboard from screen
                String name = editName.getText().toString();
                addViewModel.setName(name);

                //Checks if this food type is already Favorited, finds and saves Favorite if it is
                addViewModel.getFavorite();

                navToNumbers();
            }
        });
    }

    //TODO docstring
    //TODO maybe consolidate this with the one in DayScreenSlideFragment
    public interface OnItemTouchListener {
        public void onCardViewTap(View view, int position);
        public void onDeleteClick(View view, int position);
        public void onSelectClick(View view, int position);
    }

    private void selectFavorite(int position) {
        Favorite selected = adapter.getFavorite(position);
        addViewModel.setNumbers(selected);
        addViewModel.setName(selected.getName());

        //Checks if this food type is already Favorited, finds and saves Favorite if it is
        //TODO in this case we KNOW it is already favorited... probably should change this
        addViewModel.getFavorite();

        navToDetails();
        //TODO
        //Problem is that if you press back (want to change some of the numbers), it just goes back
        //to the name fragment...
        //Two potential solutions: actually use navToNumbers here, and make the numbers fragment always
        //populate the editTexts with the current saved values (I kind of need to do that anyways)
        //OR mess with the Navigation so that it can go forward 2 steps but only back 1 step later.
    }

    /**
     * Deletes Food Item, updates position of all items that follow
     * Similar to Method in DayScreenSlideFragment of the same name
     */
    private void deleteFavorite(int position) {

        favoriteToDelete = adapter.getFavorite(position);
        addViewModel.deleteFavorite(favoriteToDelete);

        List<Favorite> l = addViewModel.getSortedFavoritesWithPrefix(editName.getText().toString());
        adapter.submitList(l);


        showUndoSnackbar();
    }

    /**
     * Handles showing a Snackbar when a Macro is swipe deleted, allowing the option to Undo
     * the deletion by clicking the undo button.
     * In English the String displayed will be "Deleted [food]",
     * and the undo button will read "Click to Undo"
     */
    private void showUndoSnackbar() {
        //TODO extract strings
        String snackString = "Deleted " + favoriteToDelete.getName();
        Snackbar snackbar = Snackbar.make(thisView, snackString, Snackbar.LENGTH_LONG);
        snackbar.setAction("Click to Undo", f -> undoDelete());
        snackbar.show();
    }

    /**
     * Undoes the most previous deletion. Updates the POSITION of each macro below the insertion.
     */
    private void undoDelete() {
        //TODO for some reason undoing after deleting with button rather than swipe results in wrong placement
//        List<Favorite> macrosCopy = new ArrayList<>(adapter.getCurrentList());
//        for (int i = deletePosition; i < macrosCopy.size(); i++) {
//            macrosCopy.get(i).setPosition(i + 1);
//        }
//        addViewModel.update(macrosCopy);
        addViewModel.insertFavorite(favoriteToDelete);
        List<Favorite> l = addViewModel.getSortedFavoritesWithPrefix(editName.getText().toString());
        adapter.submitList(l);
    }

    private void navToNumbers() {
        NavDirections action = AddNameFragmentDirections.actionNavAddNameToNavAddNumbers();
        navController.navigate(action);
    }
    private void navToDetails() {
        NavDirections action = AddNameFragmentDirections.actionNavAddNameToNavAddDetails();
        navController.navigate(action);
    }
}
