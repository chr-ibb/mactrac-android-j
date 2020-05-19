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

    private String name;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;


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

    public void addFood() {
        insert(day, name, calories, protein, fat, carbs, count);
    }


    public void setNumbers(String calories, String protein, String fat, String carbs) {
        if (calories.isEmpty()) {
            setCalories(0);
        } else {
            setCalories(Integer.parseInt(calories));
        }

        if (protein.isEmpty()) {
            setProtein(0);
        } else {
            setProtein(Integer.parseInt(protein));
        }

        if (fat.isEmpty()) {
            setFat(0);
        } else {
            setFat(Integer.parseInt(fat));
        }

        if (carbs.isEmpty()) {
            setCarbs(0);
        } else {
            setCarbs(Integer.parseInt(carbs));
        }

    }

    //TODO the number setters could maybe be private

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

}
