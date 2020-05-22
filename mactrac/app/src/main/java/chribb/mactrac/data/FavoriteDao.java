package chribb.mactrac.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert (Favorite favorite);

    @Query("DELETE FROM favorite_table")
    void deleteAll();

    @Query("DELETE FROM favorite_table WHERE name = :name")
    void deleteFavorite(String name);

    @Query("SELECT * FROM favorite_table")
    List<Favorite> loadFavorites();

}
