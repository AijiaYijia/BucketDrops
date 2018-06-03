package zhao.dong.bucketdrops.extras;

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
}