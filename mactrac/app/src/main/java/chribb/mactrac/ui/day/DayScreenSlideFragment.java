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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.List;

import chribb.mactrac.FoodListAdapter;
import chribb.mactrac.Macro;
import chribb.mactrac.R;

public class DayScreenSlideFragment extends Fragment {
    private DayViewModel viewModel;
    private int daysSinceEpoch;
    public static final int NEW_FOOD_ACTIVITY_REQUEST_CODE = 1;

    /**
     * This is the standard way of "instantiating" a new fragment with data to pass in,
     * since you cannot make a custom constructor for a fragment.
     */
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

        viewModel = new ViewModelProvider(requireParentFragment()).get(DayViewModel.class);
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final FoodListAdapter adapter = new FoodListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        assert getArguments() != null;
        daysSinceEpoch = getArguments().getInt("daysSinceEpoch", 0);

        viewModel.loadFood(daysSinceEpoch).observe(getViewLifecycleOwner(), new Observer<List<Macro>>() {
            @Override
            public void onChanged(@Nullable final List<Macro> macros) {
                // Update the cached copy of the words in the adapter.
                adapter.setMacros(macros);
            }
        });

        TextView dayOfWeekText = view.findViewById(R.id.day_of_week_text); //TODO consider view binding or data binding
        TextView dateText = view.findViewById(R.id.date_text);
        TextView relativeDayText = view.findViewById(R.id.relative_day_text);

        dayOfWeekText.setText(viewModel.getDayOfWeekText(daysSinceEpoch));
        dateText.setText(viewModel.getDateText(daysSinceEpoch));
        relativeDayText.setText(viewModel.getRelativeDayText(daysSinceEpoch));

    }


}
