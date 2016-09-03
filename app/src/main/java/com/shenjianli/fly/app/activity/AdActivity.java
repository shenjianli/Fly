package com.shenjianli.fly.app.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.shenjianli.fly.R;
import com.shenjianli.shenlib.base.BaseActivity;
import com.shenjianli.shenlib.util.FileUtils;
import com.shenjianli.shenlib.util.SharedPreUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class AdActivity extends BaseActivity {
	private ImageView iv_splash_ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad);
		iv_splash_ad = (ImageView) findViewById(R.id.iv_splash_ad);
		
		String fileName = SharedPreUtil.get(AdActivity.this, "splash_file_name", "");
		
		if(!TextUtils.isEmpty(fileName)){
			String path = FileUtils.getAppCache(this, "emall");
			File file = new File(path);
			File[] files = file.listFiles();
			if(files.length>0){
				for(File f:files){
					if(f.getName().equals(fileName)){
						try {
							InputStream inputStream = new FileInputStream(files[0]);
							if(inputStream!=null){
								
								Bitmap bitmap =  BitmapFactory.decodeStream(inputStream);
								inputStream.close();
								iv_splash_ad.setImageBitmap(bitmap);
								showAnim();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			else{
				goToMain();
			}
		}
		
	}
	
	private void goToMain() {
		Intent intent = new Intent(AdActivity.this,MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void showAnim(){
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		iv_splash_ad.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				goToMain();
			}

			
		});
	}
}
