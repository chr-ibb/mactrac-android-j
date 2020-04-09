package chribb.mactrac;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MacroRepository {
    private MacroDao macroDao;
//    private LiveData<List<Macro>> allMacros;

    //TODO this repo is unneccessary since we only have one source of data, but for eventual backing up to server,
    // this will be used to get the data either locally or from server, if I ever implement that

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public MacroRepository(Application application) {
        MacroRoomDatabase db = MacroRoomDatabase.getDatabase(application);
        macroDao = db.macroDao();
//        allMacros = macroDao.LoadAll();
    }


    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Macro macro) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.insert(macro);
        });
    }

    public void deleteFood(int id) {
        MacroRoomDatabase.databaseWriteExecutor.execute(() -> {
            macroDao.deleteFood(id);
        });
    }

    public LiveData<List<Macro>> loadFood(Integer day) {
        return macroDao.loadFood(day);
    }

//    public LiveData<List<Macro>> loadAll() {
//        return allMacros;
//    }

}
