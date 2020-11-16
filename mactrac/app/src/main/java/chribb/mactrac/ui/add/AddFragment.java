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

import java.util.concurrent.Executors;

import chribb.mactrac.R;
import chribb.mactrac.data.MacroRoomDatabase;

public class AddFragment extends Fragment {
    private AddViewModel addViewModel;

    private NavController navController;

    private EditText editName;
    private EditText editCalories;
    private EditText editProtein;
    private EditText editFat;
    private EditText editCarbs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(this).get(AddViewModel.class);
        View view = inflater.inflate(R.layout.fragment_add, container, false);
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

        //TODO use View Binding instead of this.
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
        int day = addViewModel.getDay();
        String name = editName.getText().toString();
        Integer calories = Integer.parseInt(editCalories.getText().toString());
        Integer protein = Integer.parseInt(editProtein.getText().toString());
        Integer fat = Integer.parseInt(editFat.getText().toString());
        Integer carbs = Integer.parseInt(editCarbs.getText().toString());
        int position = addViewModel.getCount();

        addViewModel.insertMacro(day, name, calories, protein, fat, carbs, position);
    }

    private void pop() {
        navController.popBackStack();
        navController.navigateUp();
//        NavDirections action = AddFragmentDirections.actionNavAddToNavDay();
//        navController.navigate(action);
    }
}
