package chribb.mactrac.data;

import androidx.annotation.Nullable;
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
    private Integer position;
    private String note;

    public Macro(int day, String food, Integer calories,
                 Integer protein, Integer fat, Integer carbs, int position) {
        this.day = day;
        this.food = food;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.position = position;
    }

    //TODO I might be able to have an arbitrary number of custom macronutrients tracked if I switch
    // over to an array. calories, protein, fat, carbs will be the first 4 values by default,
    // but the user can edit that, and add to it. There will be maybe something in settings or idk
    // where it saves which nutrients belong to which position in the array.
    // and then all of the UI would have to be built programmatically. It seems doable though,
    // despite Room limitations. who knows maybe you cannot save an array of Integers to Room.

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

    public Integer getPosition() { return position; }
    public void setPosition(int position) {
        this.position = position;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Macro o = (Macro) obj;

        return  o.getFood().equals(this.food) &&
                o.getCalories().equals(this.calories) &&
                o.getProtein().equals(this.protein) &&
                o.getFat().equals(this.fat) &&
                o.getCarbs().equals(this.carbs);
    }
}
