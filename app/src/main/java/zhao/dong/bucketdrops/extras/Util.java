package zhao.dong.bucketdrops.extras;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import java.util.List;

public class Util {

    public static void showView(List<View> views) {

        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void hideView(List<View> views) {

        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static boolean moreThanJellyBean() {

        return Build.VERSION.SDK_INT > 15;
    }

    public static void setBackground(View view, Drawable drawable) {

        if (moreThanJellyBean()) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}