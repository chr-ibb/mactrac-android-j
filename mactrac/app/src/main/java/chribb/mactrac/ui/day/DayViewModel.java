package chribb.mactrac.ui.day;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import chribb.mactrac.Macro;
import chribb.mactrac.MacroOrder;
import chribb.mactrac.MacroRepository;

public class DayViewModel extends AndroidViewModel {
    private MacroRepository repo;
    private Integer dayOnScreen; //TODO not sure if useful
    private int macrosOnDay;

    //repo.countFood(day)

    public DayViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
    }

        /* Repo Methods */
    public void insert(Integer day, String food, Integer calories,
                       Integer protein, Integer fat, Integer carbs, int order) {
        repo.insert(new Macro(day, food, calories, protein, fat, carbs, order));
    }
    public void insert(Macro macro) {
        repo.insert(macro);
    }

    public void deleteAll() { repo.deleteAll(); }

    public void deleteFood(long id) {
        repo.deleteFood(id);
    }

    LiveData<List<Macro>> loadFood(Integer day) {
        return repo.loadFood(day);
    }

    LiveData<Integer> countFood(Integer day) {
        return repo.countFood(day);
    }

    public void updateOrder(MacroOrder order) {
        repo.updateOrder(order);
    }


        /* public methods */

    public int getToday() {
        return (int) LocalDate.now().toEpochDay();
    }

    public String getDayOfWeekText(int daysSinceEpoch) {
        return LocalDate.ofEpochDay(daysSinceEpoch).getDayOfWeek().toString();
    }

    public String getDateText(int daysSinceEpoch) {
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
            return ", " + Integer.toString(day.getYear());
        }
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

    public Integer getDayOnScreen() {
        return dayOnScreen;
    }

    public void setDayOnScreen(Integer dayOnScreen) {
        this.dayOnScreen = dayOnScreen;
    }

    public int getMacrosOnDay() {
        return macrosOnDay;
    }

    public void setMacrosOnDay(int macrosOnDay) {
        this.macrosOnDay = macrosOnDay;
    }

    public void test10000() {
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

    public void testToday() {
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
