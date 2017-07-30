package com.example.jinphy.mvp_sample.tasks;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jinphy on 2017/7/30.
 */

public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View scrollUpChild;

    public ScrollChildSwipeRefreshLayout(Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if (scrollUpChild != null) {
            return scrollUpChild.canScrollVertically(-1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View view) {
        scrollUpChild = view;
    }

}
