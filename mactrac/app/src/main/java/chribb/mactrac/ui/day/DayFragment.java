package chribb.mactrac.ui.day;

import android.content.Intent;
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

import java.util.List;

import chribb.mactrac.AppBarViewModel;
import chribb.mactrac.Macro;
import chribb.mactrac.R;

public class DayFragment extends Fragment {
    private DayViewModel viewModel;
    private AppBarViewModel appBarViewModel;
    private NavController navController;
    private FloatingActionButton fab;
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;

    //This is the number of days between January 1 1970 and January 1 2070
    private static final int NUM_DAYS =  36525;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DayViewModel.class);
        appBarViewModel = new ViewModelProvider(getActivity()).get(AppBarViewModel.class);
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
                viewModel.setDayOnScreen(viewPager.getCurrentItem());
            }
        });

        //TODO so below doesnt actually do anything. If we recreate the fragment, we also recreate the viewmodel...
        // so either dont save the day and just pop back, like we are currently, or save it to room if I need
        // that functionality in the future. we'll see
//        if (viewModel.getDayOnScreen() == null) {
//            setDayOnScreen(viewModel.getToday(), false);
//        } else {
//            setDayOnScreen(viewModel.getDayOnScreen(), true);
//        }
        //TODO this might be overwriting any attempt to save the day on screen...
        // I need to set the day to today when you open the app, but not when you recreate the view..
        // I think maybe the viewmodel wont have a dayOnScreen on fresh open, because its just a private int
        setDayOnScreen(viewModel.getToday(), false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                navToAdd();
            }
        });

        appBarViewModel.getTodayPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                   setDayOnScreen(viewModel.getToday(), true);
                   appBarViewModel.setTodayPressed(false);
                }
            }
        });

    }

    private void navToAdd() {
        //TODO make a swipe up animation
        NavDirections action = DayFragmentDirections.actionNavDayToNavAdd(viewPager.getCurrentItem());
        navController.navigate(action);
    }

    private void setDayOnScreen(int day, boolean isSmooth) {
        viewPager.setCurrentItem(day, isSmooth);
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
