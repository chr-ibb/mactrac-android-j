package chribb.mactrac.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_table")
public class Favorite {

    @PrimaryKey @NonNull
    private String name;

    private Integer calories;
    private Integer protein;
    private Integer fat;
    private Integer carbs;
    private Integer count;

    public Favorite(String name, Integer calories, Integer protein, Integer fat, Integer carbs,
                    Integer count) {
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Favorite o = (Favorite) obj;

        return  o.getName().equals(this.name) &&
                o.getCalories().equals(this.calories) &&
                o.getProtein().equals(this.protein) &&
                o.getFat().equals(this.fat) &&
                o.getCarbs().equals(this.carbs);
    }
}
