package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
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
        appBarViewModel = new ViewModelProvider(requireActivity()).get(AppBarViewModel.class);
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

        //TODO unregister OnPageChangeCallback in onDestroy of this fragment?
        // also if I'm not using dayViewModel.getDayOnScreen, this can just be deleted.
        // Now I'm not sure if I even need to unregister it unless I want to keep using the pager
        // without it doing the callback.
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                dayViewModel.setDayOnScreen(viewPager.getCurrentItem());
            }
        });

        //TODO This makes it so it will only ever open to current day when view is created
        // could be a problem if you want it to open to previously open day.
        setDayOnScreen(dayViewModel.getToday(), false);

        //FAB on click will make itself invisible, and then navigate to Add Macro fragment
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setVisibility(View.INVISIBLE);
                navToAdd();
            }
        });

        /* Observers */
        dayViewModel.countFood(dayViewModel.getDayOnScreen()).observe(getViewLifecycleOwner(),
                new Observer<Integer>() {
            @Override
            public void onChanged(Integer numberOfMacros) {
                dayViewModel.setMacrosOnDay(numberOfMacros);
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

        appBarViewModel.getTest10000Pressed().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@NonNull final Boolean pressed) {
                if (pressed) {
                    dayViewModel.test10000();
                    appBarViewModel.setTest10000Pressed(false);
                }
            }
        });

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

    private void navToAdd() {
        //TODO make a swipe up animation
        NavDirections action = DayFragmentDirections
                .actionNavDayToNavAdd(viewPager.getCurrentItem(), dayViewModel.getMacrosOnDay());
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
