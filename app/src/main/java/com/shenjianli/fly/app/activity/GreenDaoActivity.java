package com.shenjianli.fly.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shenjianli.fly.R;
import com.shenjianli.fly.app.FlyApp;
import com.shenjianli.fly.app.base.BaseActivity;
import com.shenjianli.fly.app.db.LocEntityDao;
import com.shenjianli.fly.model.LocEntity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edianzu on 2017/4/10.
 */
public class GreenDaoActivity extends BaseActivity {

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.button2)
    Button button2;
    @Bind(R.id.button3)
    Button button3;
    @Bind(R.id.button4)
    Button button4;

    LocEntityDao locEntityDao;
    LocEntity locEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dao_layout);
        ButterKnife.bind(this);
        locEntityDao = FlyApp.getAppInstance().getDaoSession().getLocEntityDao();
    }

    @OnClick({R.id.button, R.id.button2, R.id.button3, R.id.button4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                addDate();
                break;
            case R.id.button2:
                deleteDate();
                break;
            case R.id.button3:
                updateDate();
                break;
            case R.id.button4:
                findDate();
                break;
        }
    }

    /**
     * 增加数据
     */
    private void addDate() {
        locEntity = new LocEntity();
        locEntity.setId(100001L);
        locEntity.setNickname("cqtddt");
        locEntity.setUsername("shenjianli001");
        locEntityDao.insert(locEntity);//添加一个
        textView.setText(locEntity.getUsername());
    }

    /**
     * 删除数据
     */
    private void deleteDate() {
        deleteUserById(100001L);
    }

    /**
     * 根据主键删除User
     *
     * @param id User的主键Id
     */
    public void deleteUserById(long id) {
        locEntityDao.deleteByKey(id);
    }

    /**
     * 更改数据
     */
    private void updateDate() {
        locEntity = new LocEntity();
        locEntity.setId(100001L);
        locEntity.setNickname("cqtddt");
        locEntity.setUsername("shenjianli");
        locEntityDao.update(locEntity);
    }

    /**
     * 查找数据
     */
    private void findDate() {
        List<LocEntity> users = locEntityDao.loadAll();
        String userName = "";
        for (int i = 0; i < users.size(); i++) {
            userName += users.get(i).getUsername()+",";
        }
        textView.setText("查询全部数据==>"+userName);
    }
}
