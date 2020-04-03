package chribb.mactrac.ui.day;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import chribb.mactrac.R;

public class DayFragment_copy extends Fragment {
    private DayViewModel dayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dayViewModel =
                ViewModelProviders.of(this).get(DayViewModel.class);
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        return root;
    }
}
