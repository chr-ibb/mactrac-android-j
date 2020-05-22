package chribb.mactrac.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
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

public class AddDetailsFragment extends Fragment {
    private AddViewModel addViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addViewModel = new ViewModelProvider(requireActivity()).get(AddViewModel.class);

        View view = inflater.inflate(R.layout.fragment_add_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.hideKeyboardFrom(getContext(), view);

        navController = Navigation.findNavController(view);

        fillOutFoodCard(view);

        Button confirmButton = view.findViewById(R.id.button_details_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO animate the card swiping up, basically moving to the Day Fragment which is
                // above. The whole screen will move up to match when returning to Day, so you may
                // need a delay, maybe just wait for animation to finish
                // even better would be to have the card slide up from below on the next screen,
                // but that would also be much harder to do. With my current implementation
                // the new food is already there when you get there.

                addViewModel.addFood();

                navPop();
            }
        });
    }

    private void navPop() {
        NavDirections action = AddDetailsFragmentDirections.actionNavAddDetailsToNavDay();
        navController.navigate(action);
    }

    private void fillOutFoodCard(View view) {
        TextView foodName = view.findViewById(R.id.food_name);
        TextView foodCalories = view.findViewById(R.id.food_calories);
        TextView foodProtein = view.findViewById(R.id.food_protein);
        TextView foodFat = view.findViewById(R.id.food_fat);
        TextView foodCarbs = view.findViewById(R.id.food_carbs);

        String name = addViewModel.getName();
        foodName.setText(name);

        String calories = addViewModel.getCalories().toString() + " Calories";
        foodCalories.setText(calories);

        String protein = addViewModel.getProtein().toString() + "g Protein";
        foodProtein.setText(protein);

        String fat = addViewModel.getFat().toString() + "g Fat";
        foodFat.setText(fat);

        String carbs = addViewModel.getCarbs().toString() + "g Carbs";
        foodCarbs.setText(carbs);
    }
}
