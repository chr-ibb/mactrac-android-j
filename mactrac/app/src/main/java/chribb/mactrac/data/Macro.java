package chribb.mactrac.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "macro_table")
public class Macro {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private int day;
    private String food;
    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;
    private int order;
    private String note;

    public Macro(int day, String food, Integer calories,
                 Integer protein, Integer fat, Integer carbs, int order) {
        this.day = day;
        this.food = food;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.order = order;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
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

    public int getOrder() { return order; }
    public void setOrder(int order) {
        this.order = order;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
