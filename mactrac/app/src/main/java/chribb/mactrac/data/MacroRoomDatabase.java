package chribb.mactrac.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


///TODO expertSchema = true, figure out what that does/means
@Database(entities = {Macro.class, Favorite.class}, version = 1, exportSchema = false)
public abstract class MacroRoomDatabase extends RoomDatabase {

    public abstract MacroDao macroDao();
    public abstract FavoriteDao favoriteDao();

    private static volatile MacroRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MacroRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MacroRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MacroRoomDatabase.class, "macro_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build(); //TODO remove addCallback
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            //TODO get rid of this whole thing below... do i get rid of the whole callback or not?

            //TODO is this where I would create my own data on loading the app?
            // for example if I was going to make a Trie for the autofill suggestions when adding,
            // should i make it here? Where would i store it and how would i access it? It would
            // be nice to access it from the DB just like all the other calls to Room.

            // If you want to keep data through app restarts,
            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                MacroDao dao = INSTANCE.macroDao();
//                dao.deleteAll();
//
//                LocalDate today = LocalDate.now();
//                LocalDate tomorrow = today.plusDays(1);
//
//                Macro macro = new Macro((int) today.toEpochDay(), "Banana", 100, 2, 5);
//                dao.insert(macro);
//                macro = new Macro((int) today.toEpochDay(), "hamburger", 500, 14, 20);
//                dao.insert(macro);
//                macro = new Macro((int) tomorrow.toEpochDay(), "Banana", 100, 2, 5);
//                dao.insert(macro);
//            });
        }
    };
}
