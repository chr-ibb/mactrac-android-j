package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import chribb.mactrac.R;
import chribb.mactrac.Utils;

public class DayFragment extends Fragment {
    private DayViewModel dayViewModel;

    private NavController navController;

    private ViewPager2 viewPager;
    private FloatingActionButton fab;

    //This is the number of days between January 1 1970 and January 1 2070
    //TODO Probably move this somewhere else
    private static final int NUM_DAYS =  36525;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);

        fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        return inflater.inflate(R.layout.fragment_day, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Utils.hideKeyboardFrom(getContext(), view);

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
                navToAddName();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Handles an item being selected from the AppBar. 'Edit macro' button is handled in
     * DayScreenSlideFragment, the others are handled here.
     * @param item MenuItem that was selected.
     * @return Whether selection was handled, I believe.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_today:
                changeDayOnScreen(dayViewModel.getToday(), true);
                return true;
            case R.id.action_search_day:
                //TODO
                return true;
            case R.id.action_edit_macros:
                return false;
            case R.id.action_delete_all:
                dayViewModel.deleteAll();
                return true;
            case R.id.action_test_10000:
                dayViewModel.test10000();
                return true;
            case R.id.action_test_today:
                dayViewModel.testToday();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /* Private Methods */


    /**
     * Navigates from DayFragment to AddFragment, for adding macros.
     * Passes current day on screen and number of macros on the day
     */
    private void navToAddName() {
        //TODO make a swipe up animation
        NavDirections action = DayFragmentDirections
                .actionNavDayToNavAddName(viewPager.getCurrentItem());
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
