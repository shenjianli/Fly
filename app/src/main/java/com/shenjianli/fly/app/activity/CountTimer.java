package com.shenjianli.fly.app.activity;

import android.os.CountDownTimer;

/**
 * Created by edianzu on 2017/3/23.
 */
public class CountTimer extends CountDownTimer{


    private CountDownTimerListener countDownTimerListener;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public CountTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        millisUntilFinished /= 1000;
        int second = (int) Math.floor(millisUntilFinished % 60); // 计算秒
        //String secondS = second < 10 ? "0" + second : second + "";
        int minite = (int) Math.floor((millisUntilFinished / 60) % 60); // 计算分
       // String miniteS = minite < 10 ? "0" + minite : minite + "";
        int day = (int) Math.floor((millisUntilFinished / 3600) / 24); // 计算天
        //String dayS = day < 10 ? "0" + day : day + "";
        int hour1 = (int) (Math.floor(millisUntilFinished / 3600) - day * 24); // 计算小时详情页
        //String hour1S = hour1 < 10 ? "0" + hour1 : hour1 + "";

        if(null != countDownTimerListener){
            countDownTimerListener.updateCountDownTime(String.valueOf(day),String.valueOf(hour1),String.valueOf(minite),String.valueOf(second));
        }
    }

    @Override
    public void onFinish() {
        this.cancel();
        if(null != countDownTimerListener) {
            countDownTimerListener.countDownTimerFinish();
        }
    }


    public void setCountDownTimerListener(CountDownTimerListener countDownTimerListener) {
        this.countDownTimerListener = countDownTimerListener;
    }

    public interface CountDownTimerListener{
        public void updateCountDownTime(String day,String hour,String min,String second);

        public void countDownTimerFinish();
    }
}
