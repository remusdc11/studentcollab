package com.studentcollab.Globals;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class CustomRecyclerView extends RecyclerView {
    private boolean mChildIsScrolling = false;
    private int mTouchSlop;
    private float mOriginalX;
    private float mOriginalY;

    public CustomRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the scroll
            mChildIsScrolling = false;
            return false; // Let child handle touch event
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mChildIsScrolling = false;
                setOriginalMotionEvent(ev);
            }
            case MotionEvent.ACTION_MOVE: {
                if (mChildIsScrolling) {
                    // Child is scrolling so let child handle touch event
                    return false;
                }

                // If the user has dragged her finger horizontally more than
                // the touch slop, then child view is scrolling
                final int xDiff = calculateDistanceX(ev);
                final int yDiff = calculateDistanceY(ev);

                // Touch slop should be calculated using ViewConfiguration
                // constants.
                if (xDiff > mTouchSlop && xDiff > yDiff) {
                    mChildIsScrolling = true;
                    return false;
                }
            }
        }

        // Don't intercept touch events, they are handled by the child view.
        return super.onInterceptTouchEvent(ev);
    }

    public void setOriginalMotionEvent(MotionEvent ev) {
        mOriginalX = ev.getX();
        mOriginalY = ev.getY();
    }

    public int calculateDistanceX(MotionEvent ev) {
        return (int) Math.abs(mOriginalX - ev.getX());
    }

    public int calculateDistanceY(MotionEvent ev) {
        return (int) Math.abs(mOriginalY - ev.getY());
    }
}