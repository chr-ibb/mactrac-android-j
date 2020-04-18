package chribb.mactrac;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "macro_table")
public class Macro {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int day;
    private String food;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;

    public Macro(int day, String food, Integer calories, Integer protein, Integer fat, Integer carbs) {
        this.day = day;
        this.food = food;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Integer getDay() {
        return day;
    }

    public String getFood() {
        return food;
    }

    public Integer getCalories() {
        return calories;
    }

    public Integer getProtein() {
        return protein;
    }

    public Integer getFat() { return fat; }

    public Integer getCarbs() {
        return carbs;
    }

}
