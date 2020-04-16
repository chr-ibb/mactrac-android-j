package chribb.mactrac;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/** AppBarViewModel provides booleans which can be updated in main activity (onOptionsItemSelected)
 *  when buttons are pressed, and can be observed within different fragments to react accordingly.
 *
 *  If you're reading this and know a better way of doing things in fragments based on appbar
 *  button presses, let me know! I don't know how to append to the onOptionsItemSelected of the
 *  activity. My assumption is that you'd have to overwrite the whole thing, and I don't even know
 *  how to do that from outside of the activity.
 */
public class AppBarViewModel extends ViewModel {
    private MutableLiveData<Boolean> todayPressed;
    private MutableLiveData<Boolean> searchPressed;
    private MutableLiveData<Boolean> deleteAllPressed;
    private MutableLiveData<Boolean> test10000Pressed;

    public AppBarViewModel() {
        super();
        todayPressed = new MutableLiveData<>(false);
        searchPressed = new MutableLiveData<>(false);
        deleteAllPressed = new MutableLiveData<>(false);
        test10000Pressed = new MutableLiveData<>(false);
    }

    public MutableLiveData<Boolean> getTodayPressed() {
        return todayPressed;
    }

    public void setTodayPressed(Boolean pressed) {
        this.todayPressed.setValue(pressed);
    }

    public MutableLiveData<Boolean> getSearchPressed() {
        return searchPressed;
    }

    public void setSearchPressed(Boolean pressed) {
        this.searchPressed.setValue(pressed);
    }

    public MutableLiveData<Boolean> getDeleteAllPressed() {
        return deleteAllPressed;
    }

    public void setDeleteAllPressed(Boolean pressed) {
        this.deleteAllPressed.setValue(pressed);
    }

    public MutableLiveData<Boolean> getTest10000Pressed() {
        return test10000Pressed;
    }

    public void setTest10000Pressed(Boolean pressed) {
        this.test10000Pressed.setValue(pressed);
    }
}
