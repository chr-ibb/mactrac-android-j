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
    private FragmentStateAdapter pagerAdapter;
    private FloatingActionButton fab;

    //This is the number of days between January 1 1970 and January 1 2070
    private static final int NUM_DAYS =  36525;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dayViewModel = new ViewModelProvider(this).get(DayViewModel.class);
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
                dayViewModel.setDayOnScreen(viewPager.getCurrentItem());
            }
        });

        //TODO probably something wrong here
        setDayOnScreen(dayViewModel.getToday(), false);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                navToAdd();
            }
        });

        /* * * App Bar button Observers * * */

        appBarViewModel.getTodayPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                   setDayOnScreen(dayViewModel.getToday(), true);
                   appBarViewModel.setTodayPressed(false);
                }
            }
        });

        appBarViewModel.getDeleteAllPressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    dayViewModel.deleteAll();
                    appBarViewModel.setDeleteAllPressed(false);
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
