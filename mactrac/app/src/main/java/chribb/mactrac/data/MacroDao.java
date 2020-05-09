package chribb.mactrac.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MacroDao {

    @Insert
    void insert(Macro macro);

    @Query("DELETE FROM macro_table")
    void deleteAll();

    @Query("DELETE FROM macro_table WHERE id = :id")
    void deleteFood(long id);

    @Query("SELECT * from macro_table WHERE day = :day ORDER BY `position`")
    LiveData<List<Macro>> loadFood(int day);

    @Query("SELECT COUNT(*) from macro_table WHERE day = :day")
    int countFood(int day);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(List<Macro> macros);

}
