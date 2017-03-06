package com.bracketcove.postrainer.reminderlist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bracketcove.postrainer.R;


/**
 * Created by Ryan on 07/04/2016.
 */
public class CustomItemDecorator extends RecyclerView.ItemDecoration {

    private Drawable divider;
    private static int verticalSpaceHeight = 16;
    private static int horizontalSpaceHeight = 16;

    public CustomItemDecorator(Context context) {
        divider = context.getResources().getDrawable(R.drawable.divider_white);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
       // outRect.bottom = verticalSpaceHeight;
        outRect.top = verticalSpaceHeight;
        outRect.left = verticalSpaceHeight;
        outRect.right = verticalSpaceHeight;
    }
}

