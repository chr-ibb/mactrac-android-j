package chribb.mactrac;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AppBarViewModel extends ViewModel {
    private MutableLiveData<Boolean> todayPressed;

    public AppBarViewModel() {
        super();
        todayPressed = new MutableLiveData<Boolean>(false);
    }

    public MutableLiveData<Boolean> getTodayPressed() {
        return todayPressed;
    }

    public void setTodayPressed(Boolean pressed) {
        this.todayPressed.setValue(pressed);
    }
}
