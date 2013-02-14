package jp.dip.sys1.yagi.actionbarswapper;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ActionBarSwapper {
    private final static String TAG = ActionBarSwapper.class.getSimpleName();

    public static void dumpViewTree(View v, String padding) {
        Log.d(TAG, padding + v.getClass().getName());
        if (v instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) v;
            for (int i = 0; i < g.getChildCount(); i++) {
                dumpViewTree(g.getChildAt(i), padding + " ");
            }
        }
    }

    public final static void actionBarUpsideDown(Activity activity) {
        View root = activity.getWindow().getDecorView();
        View firstChild = ((ViewGroup) root).getChildAt(0);
        if (firstChild instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) firstChild;
            List<View> views = ActionBarSwapper.findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");
            if (!views.isEmpty()) {
                for (View vv : views) {
                    viewGroup.removeView(vv);
                }
                for (View vv : views) {
                    viewGroup.addView(vv);
                }
            }
        } else {
            Log.e(TAG, "first child is not ViewGroup.");
        }
    }

    public final static void actionBarTabUpsideDown(Activity activity) {
        View root = activity.getWindow().getDecorView();
        if (!(root instanceof ViewGroup)) {
            Log.e("Util", "error rootContainer is not ViewGroup.");
        }
        List<View> containers = ActionBarSwapper.findViewsWithClassName(root, "com.android.internal.widget.ActionBarContainer");
        if (containers.size() < 2) {
            Log.e("Util", "error invalid container count:" + containers.size());
            return;
        }
        ViewGroup srcContainer = (ViewGroup) containers.get(0);
        ViewGroup dstContainer = (ViewGroup) containers.get(1);
        List<View> views = ActionBarSwapper.findViewsWithClassName(root, "com.android.internal.widget.ScrollingTabContainerView");
        if (!views.isEmpty()) {
            for (View vv : views) {
                srcContainer.removeView(vv);
            }
            for (View vv : views) {
                dstContainer.addView(vv);
            }
        }
        srcContainer.setVisibility(View.GONE);
        dstContainer.setVisibility(View.VISIBLE);
    }

    public static <T extends View> List<T> findViewsWithClass(View v, Class<T> clazz) {
        List<T> views = new ArrayList<T>();
        findViewsWithClass(v, clazz.getName(), views);
        return views;
    }

    public static List<View> findViewsWithClassName(View v, String className) {
        List<View> views = new ArrayList<View>();
        findViewsWithClass(v, className, views);
        return views;
    }

    @SuppressWarnings("unchecked")
    private static <T extends View> void findViewsWithClass(View v, String clazz, List<T> views) {
        if (v.getClass().getName().equals(clazz)) {
            views.add((T) v);
        }
        if (v instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) v;
            for (int i = 0; i < g.getChildCount(); i++) {
                findViewsWithClass(g.getChildAt(i), clazz, views);
            }
        }
    }
}
