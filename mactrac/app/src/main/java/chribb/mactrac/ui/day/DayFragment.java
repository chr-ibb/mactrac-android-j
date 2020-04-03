package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import chribb.mactrac.R;

public class DayFragment extends Fragment {
    private DayViewModel dayViewModel;

    private static final int NUM_DAYS =  5;

    private ViewPager2 viewPager;

    private FragmentStateAdapter pagerAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dayViewModel =
                ViewModelProviders.of(this).get(DayViewModel.class);
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        viewPager.setCurrentItem(getToday());
    }


    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(Fragment fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment dayFragment = new DayScreenSlideFragment();
            //set up the day, based on POSITION

            return dayFragment;
        }

        @Override
        public int getItemCount() {
            return NUM_DAYS;
        }
    }

    private int getToday() {
        //return todays date minus the starting date. should this be in the viewmodel?
        return 0;
    }

}
