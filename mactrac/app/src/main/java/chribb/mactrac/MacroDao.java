package chribb.mactrac;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MacroDao {

    @Insert
    void insert(Macro macro);

    @Query("DELETE FROM macro_table")
    void deleteAll();

    @Query("DELETE FROM macro_table WHERE id = :id")
    public void deleteFood(int id);

    @Query("SELECT * from macro_table WHERE day = :day ORDER BY `order`")
    public LiveData<List<Macro>> loadFood(int day);

//    @Query("SELECT * from macro_table") //TODO do i need this? its in more than one file
//    public LiveData<List<Macro>> LoadAll();
}
