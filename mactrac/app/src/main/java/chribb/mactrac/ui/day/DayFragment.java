package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import chribb.mactrac.AppBarViewModel;
import chribb.mactrac.R;

public class DayFragment extends Fragment {
    private DayViewModel dayViewModel;
    private AppBarViewModel appBarViewModel;

    private NavController navController;

    private ViewPager2 viewPager;
    private FloatingActionButton fab;

    //This is the number of days between January 1 1970 and January 1 2070
    //TODO Probably move this somewhere else
    private static final int NUM_DAYS =  36525;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
        appBarViewModel = new ViewModelProvider(requireActivity()).get(AppBarViewModel.class);

        fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        View view = inflater.inflate(R.layout.fragment_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        viewPager = view.findViewById(R.id.pager);
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        //TODO Unregister OnPageChangeCallback in onDestroy of this fragment? do i need dayOnScreen?
        // When do I need dayOnScreen where I cant just use viewpager.getCurrentItem()?
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                dayViewModel.setDayOnScreen(viewPager.getCurrentItem());
            }
        });

        //TODO Will only ever open to current day when view is created, could be a problem.
        changeDayOnScreen(dayViewModel.getToday(), false);

        //FAB on click will make itself invisible, and then navigate to Add Macro fragment
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                navToAdd();
            }
        });

        /* * * Observers * * */
        /* * * App Bar button Observers * * */

        // smoothly slides to today when the 'today' button on app bar is pressed
        appBarViewModel.getTodayPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                   changeDayOnScreen(dayViewModel.getToday(), true);
                   appBarViewModel.setTodayPressed(false);
                }
            }
        });

        //Deletes all Macros when Delete All is pressed in overflow
        appBarViewModel.getDeleteAllPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    dayViewModel.deleteAll();
                    appBarViewModel.setDeleteAllPressed(false);
                }
            }
        });

        //Adds 10000 random Macros to random days when button is pressed in overflow, for testing.
        appBarViewModel.getTest10000Pressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    dayViewModel.test10000();
                    appBarViewModel.setTest10000Pressed(false);
                }
            }
        });

        // Adds 10 Macros to the day on screen, for testing.
        appBarViewModel.getTestTodayPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    dayViewModel.testToday();
                    appBarViewModel.setTestTodayPressed(false);
                }
            }
        });
    }

    /**
     * Navigates from DayFragment to AddFragment, for adding macros.
     * Passes current day on screen and number of macros on the day
     */
    private void navToAdd() {
        //TODO make a swipe up animation
        NavDirections action = DayFragmentDirections
                .actionNavDayToNavAdd(viewPager.getCurrentItem());
        navController.navigate(action);
    }

    /**
     * Changes the day that is visible on the screen.
     * @param day Day to put on screen (counting from Unix Epoch)
     * @param isSmooth true: smoothly slide to the day. false: instant transition
     */
    private void changeDayOnScreen(int day, boolean isSmooth) {
        viewPager.setCurrentItem(day, isSmooth);
    }

    /**
     * Pager Adapter for ViewPager2. Provides individual DayScreenslideFragments to DayFragment
     */
    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        ScreenSlidePagerAdapter(Fragment fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return DayScreenSlideFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return NUM_DAYS;
        }
    }
}
