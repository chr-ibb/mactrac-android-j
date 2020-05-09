package chribb.mactrac.ui.day;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import chribb.mactrac.data.Macro;
import chribb.mactrac.data.MacroRepository;

public class DayViewModel extends AndroidViewModel {
    private MacroRepository repo;
    private Integer dayOnScreen; //TODO not sure if necessary

    public DayViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
    }

        /* Repo Methods */
    LiveData<List<Macro>> loadFood(Integer day) {
        return repo.loadFood(day);
    }

    void insert(Integer day, String food, Integer calories,
                       Integer protein, Integer fat, Integer carbs, int position) {
        repo.insert(new Macro(day, food, calories, protein, fat, carbs, position));
    }

    void insert(Macro macro) {
        repo.insert(macro);
    }

    void update(List<Macro> macros) {
        repo.update(macros);
    }

    int countFood(Integer day) {
        return repo.countFood(day);
    }

    void deleteFood(long id) {
        repo.deleteFood(id);
    }

    void deleteAll() { repo.deleteAll(); }



        /* public methods */

    int getToday() {
        return (int) LocalDate.now().toEpochDay();
    }

//    public String getDayOfWeekText(int daysSinceEpoch) {
//        return LocalDate.ofEpochDay(daysSinceEpoch).getDayOfWeek().toString();
//    }

    String getDateText(int daysSinceEpoch) {
        LocalDate day = LocalDate.ofEpochDay(daysSinceEpoch);
        String dayOfWeek = day.getDayOfWeek().toString();
        String month = day.getMonth().toString();
        String dayOfMonth = Integer.toString(day.getDayOfMonth());
        String year = getYearString(day);
        return dayOfWeek + ", " + month + " " + dayOfMonth + year; //TODO function that makes this using extracted strings
    }

    private String getYearString(LocalDate day) {
        //if current year, don't show it (make the year an empty string)
        if (day.getYear() == LocalDate.now().getYear()) {
            return "";
        } else {
            return ", " + day.getYear();
        }
    }

    String getRelativeDayText(int daysSinceEpoch) {
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

    Integer getDayOnScreen() {
        return dayOnScreen;
    }

    void setDayOnScreen(Integer dayOnScreen) {
        this.dayOnScreen = dayOnScreen;
    }

    void test10000() {
        Random r = new Random();
        int N = 10000;

        for(int i = 0; i < N; i++) {
            Integer d = r.nextInt(36525);
            String f = "Test Food " + i;
            Integer cal = r.nextInt(500);
            Integer p = r.nextInt(20);
            Integer fat = r.nextInt(20);
            Integer car = r.nextInt(20);

            insert(d, f, cal, p, fat, car, i);
        }
    }

    void testToday() {
        Random r = new Random();
        int N = 10;
        int d = dayOnScreen;

        for (int i = 0; i < N; i++) {
            String f = "Test Food " + i;
            Integer cal = r.nextInt(500);
            Integer p = r.nextInt(20);
            Integer fat = r.nextInt(20);
            Integer car = r.nextInt(20);

            insert(d, f, cal, p, fat, car, i);
        }
    }
}
