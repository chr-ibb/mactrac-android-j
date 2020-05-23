package chribb.mactrac.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

public class MacroRepository {
    private MacroDao macroDao;
    private FavoriteDao favoriteDao;

    //TODO this repo is unnecessary since we only have one source of data, but for eventual backing up to server,
    // this will be used to get the data either locally or from server, if I ever implement that

    //TODO change method names for Macro methods to say Macro in it (ie insert > insertMacro)
    // I also might change Macro to Food or something, so I'll hold off on this for now.

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public MacroRepository(Application application) {
        MacroRoomDatabase db = MacroRoomDatabase.getDatabase(application);
        macroDao = db.macroDao();
        favoriteDao = db.favoriteDao();
    }


    /* MACRO DAO METHODS */

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Macro macro) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.insert(macro);
        });
    }

    public void update(List<Macro> macros) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.update(macros);
        });
    }

    public void deleteAll() {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.deleteAll();
        });
    }

    public void deleteFood(long id) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.deleteFood(id);
        });
    }

    public LiveData<List<Macro>> loadFood(Integer day) {
        return macroDao.loadFood(day);
    }

    public int countFood(int day) {
        return  macroDao.countFood(day);
    }


    /* FAVORITE DAO METHODS */

    public void insertFavorite(Favorite favorite) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.insert(favorite);
        });
    }

    public void deleteAllFavorites() {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.deleteAll();
        });
    }

    public void deleteFavorite(String name) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            favoriteDao.deleteFavorite(name);
        });
    }

    public List<Favorite> loadFavorites() {
        return favoriteDao.loadFavorites();
    }

    public Favorite getFavorite(String name) {
        return favoriteDao.getFavorite(name);
    }


}
