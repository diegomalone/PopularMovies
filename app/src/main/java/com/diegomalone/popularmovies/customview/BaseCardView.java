package com.diegomalone.popularmovies.customview;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * Created by malone on 7/24/16.
 */

public class BaseCardView extends CardView {

    protected Context mContext;

    public BaseCardView(Context context) {
        this(context, null);
    }

    public BaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setRadius(0f);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setCardElevation(0f);
        }
    }
}
