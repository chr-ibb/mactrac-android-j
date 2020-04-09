package chribb.mactrac;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

public class FoodItem {

    //TODO I think I need the ID to delete later, so I basically need every field. so DELETE this class later unless you find a use for it

    @ColumnInfo(name = "food")
    @NonNull
    public String food;

    @ColumnInfo(name = "calories")
    public Integer calories;

    @ColumnInfo(name = "protein")
    public Integer protein;

    @ColumnInfo(name = "carbs")
    public Integer carbs;

}
