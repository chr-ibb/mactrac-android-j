package chribb.mactrac.ui.add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import java.util.concurrent.Executors;

import chribb.mactrac.data.Macro;
import chribb.mactrac.data.MacroRepository;


public class AddViewModel extends AndroidViewModel {
    private MacroRepository repo;

    private int day;
    private int count;


    public AddViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
    }

    /* * * Repo Methods * * */
    void insert(Integer day, String food, Integer calories,
                Integer protein, Integer fat, Integer carbs, int position) {
        repo.insert(new Macro(day, food, calories, protein, fat, carbs, position));
    }

    private int countFood(int day) {
        return repo.countFood(day);
    }

    void findCount() {
        //Queries Room for the number of macros already on this day, to use as position.
        Executors.newSingleThreadExecutor().execute(() -> count = countFood(day));
    }


    /* * * Getters/Setters * * */
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    int getCount() {
        return count;
    }

    //TODO make it so this saves the data that has been entered so far, so that if you rotate screens you dont lose it

}
