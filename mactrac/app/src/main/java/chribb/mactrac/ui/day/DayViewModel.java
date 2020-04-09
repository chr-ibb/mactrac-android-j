package chribb.mactrac.ui.day;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.widget.ViewPager2;

import java.time.LocalDate;
import java.util.List;

import chribb.mactrac.Macro;
import chribb.mactrac.MacroRepository;

public class DayViewModel extends AndroidViewModel {
    private MacroRepository repo;
//    private LiveData<List<Macro>> allMacros;


//    private LiveData<Integer> dayOnScreen; //TODO not sure if useful

    public DayViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
//        allMacros = repo.loadAll();
    }

    public void insert(Integer day, String food, Integer calories, Integer protein, Integer carbs) {
        repo.insert(new Macro(day, food, calories, protein, carbs));
    }

    public void deleteFood(int id) {
        repo.deleteFood(id);
    }

    LiveData<List<Macro>> loadFood(Integer day) {
        return repo.loadFood(day);
    }

//    LiveData<List<Macro>> loadAll() {
//        return allMacros;
//    }


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
