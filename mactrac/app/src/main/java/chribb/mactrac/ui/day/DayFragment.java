package chribb.mactrac.ui.day;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class DayFragment extends Fragment {
    private DayViewModel viewModel;
    private NavController navController;
    private FloatingActionButton fab;

    //This is the number of days between January 1 1970 and January 1 2070
    private static final int NUM_DAYS =  36525;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DayViewModel.class);
        fab = requireActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        viewPager = view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                //TODO change the fab onClick here
                //TODO perhaps set the selected day in viewModel, so that you can return to said day when you come back to Day View
            }
        });



        int today = viewModel.getToday();
        viewPager.setCurrentItem(today, false);

        //TODO TODO Figure out where you're actually setting this (here or in the sub fragment?)
        // and then set it up to actually take you to the Add Macro Fragment using NAVIGATION
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                navToAdd();
            }
        });
    }

    private void navToAdd() {
        //TODO make a swipe up animation
        NavDirections action = DayFragmentDirections.actionNavDayToNavAdd(viewPager.getCurrentItem());
        navController.navigate(action);
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(Fragment fa) {
            super(fa);
        }

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
