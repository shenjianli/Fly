package com.shenjianli.fly.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shenjianli.fly.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by edianzu on 2017/3/3.
 */
public class FontActivity extends AppCompatActivity {

    @Bind(R.id.font_sum_layout)
    LinearLayout fontSumLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);
        ButterKnife.bind(this);
        initFontView();
    }

    private void initFontView() {

        TextView text;
        for(int i=8;i<29;i++){
            text = new TextView(this);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,i);
            text.setText("这里的字号是:" + i + "sp");
            text.setGravity(Gravity.CENTER);
            fontSumLayout.addView(text);
        }

    }


}
