package com.fraz.dartlog.game.settings;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;


public class SwipeDetector implements View.OnTouchListener {

    private View holder;
    private ListView listViewReference;
    private int listIdx;
    private static final int MIN_DISTANCE = 300;
    private static final int MIN_LOCK_DISTANCE = 30; // disallow motion intercept
    private boolean motionInterceptDisallowed = false;
    private float downX, upX;

    public SwipeDetector(View holder, int listIdx, ListView listView){
        this.holder = holder;
        this.listIdx = listIdx;
        this.listViewReference = listView;
    }


@Override
 public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            downX = event.getX();
            return true; // allow other events like Click to be processed
        }

        case MotionEvent.ACTION_MOVE: {
            upX = event.getX();
            float deltaX = downX - upX;

            if (Math.abs(deltaX) > MIN_LOCK_DISTANCE && listViewReference != null && !motionInterceptDisallowed) {
                listViewReference.requestDisallowInterceptTouchEvent(true);
                motionInterceptDisallowed = true;
            }

            if (deltaX > 0) {
                //holder.deleteView.setVisibility(View.GONE);
            } else {
                // if first swiped left and then swiped right
                //holder.deleteView.setVisibility(View.VISIBLE);
            }

            swipe(-(int) deltaX);
            return true;
        }

        case MotionEvent.ACTION_UP:
            upX = event.getX();
            float deltaX = upX - downX;
            if (Math.abs(deltaX) > MIN_DISTANCE) {
                // left or right
                //swipeRemove();
            } else {
                swipe(0);
            }

            if (listViewReference != null) {
                listViewReference.requestDisallowInterceptTouchEvent(false);
                motionInterceptDisallowed = false;
            }

            //holder.deleteView.setVisibility(View.VISIBLE);
            return true;

        case MotionEvent.ACTION_CANCEL:
            //holder.deleteView.setVisibility(View.VISIBLE);
            return false;
    }

    return true;
}

    private void swipe(int distance) {
        View animationView = holder;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) animationView.getLayoutParams();
        params.rightMargin = -distance;
        params.leftMargin = distance;
        animationView.setLayoutParams(params);
    }
}
