package chribb.mactrac.ui.month;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import chribb.mactrac.R;


public class MonthFragment extends Fragment {
    private MonthViewModel monthViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        monthViewModel =
                ViewModelProviders.of(this).get(MonthViewModel.class);
        View root = inflater.inflate(R.layout.fragment_month, container, false);
        final TextView textView = root.findViewById(R.id.text_month);
        monthViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
