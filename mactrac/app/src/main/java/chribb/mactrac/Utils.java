package chribb.mactrac;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.RemoteInput;

public class Utils {
    public static int convertDPtoPixels(float scale, int dp) {
        return (int) (dp * scale + 0.5f);
    }

    /**
     * Hide the keyboard from a view in a context
     * @source https://stackoverflow.com/questions/1109022/close-hide-android-soft-keyboard
     */
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboardTo(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
