package com.shenjianli.fly.app.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.shenjianli.fly.R;

/**
 * Created by edianzu on 2016/9/30.
 */
public class ItemLayout extends RelativeLayout{

    public ItemLayout(Context context) {
        super(context);
    }

    public ItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItemLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyle) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_ad, this, true);
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.item_layout, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.item_layout_item_leftImage:
                    break;
                case R.styleable.item_layout_item_text:
                    break;
                case R.styleable.item_layout_item_textColor:
                    break;
                case R.styleable.item_layout_item_textSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    //mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                    //        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.item_layout_item_rightText:
                    break;
                case R.styleable.item_layout_item_rightTextColor:
                    break;
                case R.styleable.item_layout_item_rightTextSize:
                    break;
                case R.styleable.item_layout_item_rightImage:
                    break;

            }

        }
        a.recycle();

        /**
         * 获得绘制文本的宽和高
         */

    }

}
