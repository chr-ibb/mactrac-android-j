package chribb.mactrac.ui.day;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.widget.ViewPager2;

import java.time.LocalDate;

public class DayViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    private LiveData<Integer> dayOnScreen; //TODO not sure if useful


    public DayViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is day fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public int getToday() {
        return (int) LocalDate.now().toEpochDay();
    }

    public String getDayOfWeekText(int daysSinceEpoch) {
        return LocalDate.ofEpochDay(daysSinceEpoch).getDayOfWeek().toString();
    }

    public String getDateText(int daysSinceEpoch) {
        LocalDate day = LocalDate.ofEpochDay(daysSinceEpoch);
        String month = day.getMonth().toString();
        String dayOfMonth = Integer.toString(day.getDayOfMonth());
        String year = Integer.toString(day.getYear());
        return month + " " + dayOfMonth + ", " + year; //TODO function that makes this using extracted strings
    }

    public String getRelativeDayText(int daysSinceEpoch) {
        String relativeDay = "today";
        int relative =   daysSinceEpoch - (int) LocalDate.now().toEpochDay();
        
        if (relative < -1) {
            relativeDay = -relative + " days ago"; //TODO extract these strings for translations
        } else if (relative > 1) {
            relativeDay = relative + " days from now";
        } else {
            switch(relative) {
                case -1:
                    relativeDay = "Yesterday";
                    break;
                case 0:
                    relativeDay = "Today";
                    break;
                case 1:
                    relativeDay = "Tomorrow";
                    break;
            }
        }

        return relativeDay;
    }

}
