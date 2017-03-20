package com.shenjianli.fly.app.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shenjianli.fly.R;
import com.shenjianli.fly.app.base.BaseHolder;

import butterknife.Bind;

/**
 * Created by shenjianli on 2016/7/27.
 */
public class DemoViewHolder extends BaseHolder {
    // 大图
    @Bind(R.id.imavPic)
    public ImageView imavPic;
    // 图片url
    @Bind(R.id.tvUrl)
    public TextView tvUrl;

    public DemoViewHolder(ViewGroup parent, @LayoutRes int resId) {
        super(parent, resId);
    }

    public DemoViewHolder(View view) {
        super(view);
    }
}
