package chribb.mactrac.ui.day;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.widget.ViewPager2;

public class DayViewModel extends ViewModel {
    private MutableLiveData<String> mText;


    public DayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is day fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public int getToday() {
        //return today's date minus the "start" date
        return 0;
    }
}
