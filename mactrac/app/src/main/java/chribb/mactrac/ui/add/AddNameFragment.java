package chribb.mactrac.ui.add;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import chribb.mactrac.R;
import chribb.mactrac.Utils;
import chribb.mactrac.ui.day.DayFragmentDirections;

public class AddNameFragment extends Fragment {
    private AddViewModel addViewModel;
    private NavController navController;

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

        navController = Navigation.findNavController(view);

        assert getArguments() != null;
        addViewModel.setDay(AddNameFragmentArgs.fromBundle(getArguments()).getDay());
        //Queries Room for number of macros on this day and sets result to COUNT in viewModel
        addViewModel.findCount();

        EditText editName = view.findViewById(R.id.edit_name);
        Button nextButton = view.findViewById(R.id.button_name_next);

        editName.requestFocus();
        Utils.showKeyboardTo(getContext(), editName);

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
                navToNumbers();
            }
        });
    }

    private void navToNumbers() {
        NavDirections action = AddNameFragmentDirections.actionNavAddNameToNavAddNumbers();
        navController.navigate(action);
    }
}
