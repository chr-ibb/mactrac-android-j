package chribb.mactrac.ui.add;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

public class AddNumbersFragment extends Fragment {
    private AddViewModel addViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);
        View view = inflater.inflate(R.layout.fragment_add_numbers, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        TextView nameText = view.findViewById(R.id.text_add_numbers_name);
        String name = addViewModel.getName();
        nameText.setText(name);

        //TODO use View Binding instead of this.
        EditText editCalories = view.findViewById(R.id.edit_calories);
        EditText editProtein = view.findViewById(R.id.edit_protein);
        EditText editFat = view.findViewById(R.id.edit_fat);
        EditText editCarbs = view.findViewById(R.id.edit_carbs);
        Button nextButton = view.findViewById(R.id.button_add_numbers_next);

        //TODO This needs to be on the LAST VISIBLE EDITTEXT. If the user isnt tracking carbs
        // or fat, this should be on editProtein. You can probably just get a reference to the last
        // editText and just do lastText.set.....
        editCarbs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                addViewModel.setNumbers(
                        editCalories.getText().toString(),
                        editProtein.getText().toString(),
                        editFat.getText().toString(),
                        editCarbs.getText().toString());


                navToDetails();
            }
        });
    }

    private void navToDetails() {
        NavDirections action = AddNumbersFragmentDirections.actionNavAddNumbersToNavAddDetails();
        navController.navigate(action);
    }
}
