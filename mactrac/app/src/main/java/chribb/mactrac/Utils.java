package chribb.mactrac;

public class Utils {
    public static int convertDPtoPixels(float scale, int dp) {
        return (int) (dp * scale + 0.5f);
    }
}
