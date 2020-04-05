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

import chribb.mactrac.R;

public class DayScreenSlideFragment extends Fragment {
    private DayViewModel viewModel; //TODO how do you share a view model? im afraid of accidentally using two seperate instances

    /**
     * This is the standard way of "instantiating" a new fragment with data to pass in,
     * since you cannot make a custom constructor for a fragment.
     */
    public static DayScreenSlideFragment newInstance(int daySinceEpoch) {
        DayScreenSlideFragment fragment = new DayScreenSlideFragment();

        Bundle args = new Bundle();
        args.putInt("daySinceEpoch", daySinceEpoch);
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

        //TODO once you figure out sharing a viewmodel across fragments in a one activity app,
        //TODO you should move most of this to the viewmodel. just do like
        // relativeDayText.setText(viewModel.getRelativeDay(day));

        TextView dayOfWeekText = view.findViewById(R.id.day_of_week_text);
        TextView dateText = view.findViewById(R.id.date_text);
        TextView relativeDayText = view.findViewById(R.id.relative_day_text);
        int relative;
        String relativeDay = "today";
        String dayOfWeek;
        String month;
        String dayOfMonth;
        String year;
        String combinedDate;

        assert getArguments() != null;
        int daySinceEpoch = getArguments().getInt("daySinceEpoch", 0);
        LocalDate day = LocalDate.ofEpochDay(daySinceEpoch);
        LocalDate today = LocalDate.now(); //TODO do i need this for anything else? if not just put this in line below

        relative =   daySinceEpoch - (int) today.toEpochDay();
        if (relative < -1) {
            relativeDay = -relative + " days ago"; //TODO extract these strings for translations
        } else if (relative > 1) {
            relativeDay = relative + " days from now";
        } else {
            switch(relative) {
                case -1:
                    relativeDay = "Yesterday";
                    break;
                case 0:
                    relativeDay = "Today";
                    break;
                case 1:
                    relativeDay = "Tomorrow";
                    break;
            }
        }

        dayOfWeek = day.getDayOfWeek().toString();
        month = day.getMonth().toString();
        dayOfMonth = Integer.toString(day.getDayOfMonth());
        year = Integer.toString(day.getYear());
        combinedDate = month + " " + dayOfMonth + ", " + year; //TODO function that makes this using extracted strings

        dayOfWeekText.setText(dayOfWeek);
        dateText.setText(combinedDate);
        relativeDayText.setText(relativeDay);
    }


}
