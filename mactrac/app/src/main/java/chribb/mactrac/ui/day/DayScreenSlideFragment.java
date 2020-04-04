package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.time.LocalDateTime;

import chribb.mactrac.R;

public class DayScreenSlideFragment extends Fragment {
    private DayViewModel viewModel; //TODO how do you share a view model? im afraid of accidentally using two seperate instances
    private TextView dayText;

    /**
     * This is the standard way of "instantiating" a new fragment with data to pass in,
     * since you cannot make a custom constructor for a fragment.
     */
    public static DayScreenSlideFragment newInstance(int day) {
        DayScreenSlideFragment fragment = new DayScreenSlideFragment();

        Bundle args = new Bundle();
        args.putInt("day", day);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dayText = view.findViewById(R.id.day_text);
        assert getArguments() != null;
        int day = getArguments().getInt("day", 0);

        LocalDate date = LocalDate.ofEpochDay(day);

        dayText.setText(date.toString());

    }


}
