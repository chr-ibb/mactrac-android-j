package chribb.mactrac.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import chribb.mactrac.R;

public class AddFragment extends Fragment {
    private AddViewModel viewModel; //TODO change to addViewModel
    private NavController navController;

    private EditText editName;
    private EditText editCalories;
    private EditText editProtein;
    private EditText editFat;
    private EditText editCarbs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddViewModel.class);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        //TODO use View Binding instead of this junk.
        editName = view.findViewById(R.id.edit_name);
        editCalories = view.findViewById(R.id.edit_calories);
        editProtein = view.findViewById(R.id.edit_protein);
        editFat = view.findViewById(R.id.edit_fat);
        editCarbs = view.findViewById(R.id.edit_carbs);

        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO remove keyboard from screen
                addMacro();
                pop();
            }
        });

    }

    private void addMacro() {
        Integer day = AddFragmentArgs.fromBundle(getArguments()).getDay();
        String name = editName.getText().toString();
        Integer calories = Integer.parseInt(editCalories.getText().toString());
        Integer protein = Integer.parseInt(editProtein.getText().toString());
        Integer fat = Integer.parseInt(editFat.getText().toString());
        Integer carbs = Integer.parseInt(editCarbs.getText().toString());
        int order = AddFragmentArgs.fromBundle(getArguments()).getMacrosOnDay();

        viewModel.insert(day, name, calories, protein, fat, carbs, order);
    }

    private void pop() {
        navController.popBackStack();
        navController.navigateUp();
//        NavDirections action = AddFragmentDirections.actionNavAddToNavDay();
//        navController.navigate(action);
    }
}
