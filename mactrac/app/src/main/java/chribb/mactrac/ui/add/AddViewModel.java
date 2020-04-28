package chribb.mactrac.ui.add;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.crypto.Mac;

import chribb.mactrac.Macro;
import chribb.mactrac.MacroRepository;


public class AddViewModel extends AndroidViewModel {
    private MacroRepository repo;

    public AddViewModel(Application application) {
        super(application);
        repo = new MacroRepository(application);
    }

    public void insert(Integer day, String food, Integer calories,
                       Integer protein, Integer fat, Integer carbs, int order) {
        repo.insert(new Macro(day, food, calories, protein, fat, carbs, order));
    }


    //TODO make it so this saves the data that has been entered so far, so that if you rotate screens you dont lose it

}
