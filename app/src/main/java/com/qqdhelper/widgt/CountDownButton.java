package com.qqdhelper.widgt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.qqdhelper.BaseApplication;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.qqdhelper.R;

/**
 * 
 * @author sdash
 * @Statement 验证码倒计时控件
 */
public class CountDownButton extends Button implements OnClickListener {
	/** 倒计时长度,默认60秒 */
	private long COUNTDOWM_LENGHT = 60 * 1000;// 倒计时长度,默认60秒
	private Boolean isStar = false;// 是否启动计时，默认false
	private Boolean isStop= false;// 是否停止计时，默认false
	private String textAfter;// 启动计时后的button_text
	private String textBefore;// 启动计时前的button_text
	private String textReset;// 重新获取启动计时前的button_text
//	private int colorAfter = Color.WHITE;// 启动计时后的button_text_color
//	private int colorBefore = Color.WHITE;// 启动计时前的button_text_color
	
	private int colorAfter = Color.GRAY;// 启动计时后的button_text_color
    private int colorBefore = Color.BLACK;// 启动计时前的button_text_color
    
    
	private final String TIME = "time";// 存入countDown_map中的目标时间键值
	private final String CTIME = "ctime";// 存入countDown_map中的当前时间键值
	private OnClickListener mOnclickListener;// CountDownButton的点击时间监听
	private Timer mTimer;// 创建时间线程
	private TimerTask mTimerTask;// 创建时间线程任务类
	private long temp_time;// 定义临时变量

	/** 构造方法 */
	public CountDownButton(Context context) {
		super(context);
		setOnClickListener(this);
		textBefore=context.getString(R.string.verification_code_Before);
		textAfter=context.getString(R.string.verification_code_After);
		textReset=context.getString(R.string.verification_code_Reset);
		setTextColor(colorBefore);
	}

	/** 构造方法 */
	public CountDownButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
		textBefore=context.getString(R.string.verification_code_Before);
		textAfter=context.getString(R.string.verification_code_After);
		textReset=context.getString(R.string.verification_code_Reset);
		setTextColor(colorBefore);
	}

	@SuppressLint("HandlerLeak")
	/**实例一个mHandler
	 * 用来接收并响应Timer发送的消息
	 * */
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
		    switch (msg.what) {
            case 0x01:
              //CountDownButton.this.setText(temp_time / 1000 + textAfter);// 显示运行消息
                CountDownButton.this.setText(String.format(textAfter, temp_time / 1000));// 显示运行消息
                temp_time -= 1000;// 每运行1s，temp_time减少1s
                // 判断当temp_time<0时，CountDownButton恢复默认状态，并清除时间线程
                if (temp_time < 0) {
                    ResetTimer();//重置计时按钮
                }
                break;
            case 0x02:
                initTimer();//初始化计时按钮
                break;
            default:
                break;
            }
			
		};
	};

	/**
	 * 初始化计时按钮
	 * */
	private void initTimer() {
	    CountDownButton.this.setEnabled(true);//设置按钮可用
        CountDownButton.this.setText(textBefore);//初始化按钮文字
        CountDownButton.this.setTextColor(colorBefore);//初始化按钮字体颜色
        clearTimer();//清除线程
	}
	
	/**
     * 重置计时按钮
     * */
    private void ResetTimer() {
        CountDownButton.this.setEnabled(true);//设置按钮可用
        CountDownButton.this.setText(textReset);//初始化按钮文字(重新获取)
        CountDownButton.this.setTextColor(colorBefore);//初始化按钮字体颜色
        clearTimer();//清除线程
    }
	
	/**
     * 唤醒时间线程
     * */
    private void WakeupTimer(){
	    temp_time = COUNTDOWM_LENGHT;// 将接收到的时间赋值给temp_time;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0x01);
            }
        };
	}

	/**
	 * 清除时间线程
	 * */
	private void clearTimer() {
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		if (mTimer != null)
			mTimer.cancel();
		mTimer = null;
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		if (l instanceof CountDownButton) {
			super.setOnClickListener(l);
		} else
			this.mOnclickListener = l;
	}

	@Override
	public void onClick(View v) {
		if (mOnclickListener != null)
			mOnclickListener.onClick(v);
		if(isStar){
		    WakeupTimer();
//			this.setText(temp_time / 1000 + textAfter);
			this.setText(String.format(textAfter, temp_time / 1000));
			this.setEnabled(false);
			this.setTextColor(colorAfter);
			mTimer.schedule(mTimerTask, 0, 1000);// 安排指定的任务在指定的时间开始进行重复的固定延迟执行。
			// t.scheduleAtFixedRate(task, delay, period);
		}
	}

	/**
	 * 和activity的onDestroy()方法同步
	 */
	public void onDestroy() {
		if (BaseApplication.countDown_map == null)
			BaseApplication.countDown_map = new HashMap<String, Long>();
		BaseApplication.countDown_map.put(TIME, temp_time);
		BaseApplication.countDown_map.put(CTIME, System.currentTimeMillis());
		clearTimer();
	}

	/**
	 * 和activity的onCreate()方法同步
	 */
	public void onCreate(Bundle bundle) {

		this.setText(textBefore);//初始化btn文字
		if (BaseApplication.countDown_map == null)
			return;
		if (BaseApplication.countDown_map.size() <= 0)// 这里表示没有上次未完成的计时
			return;
		long time = System.currentTimeMillis() - BaseApplication.countDown_map.get(CTIME)
				- BaseApplication.countDown_map.get(TIME);
		BaseApplication.countDown_map.clear();
		if (time > 0)
			return;
		else {
		    WakeupTimer();
			this.temp_time = Math.abs(time);
			mTimer.schedule(mTimerTask, 0, 1000);
//			this.setText(time + textAfter);
			this.setText(String.format(textAfter, temp_time / 1000));
			this.setEnabled(false);

		}
		int btn_color = isEnabled() == true ? colorBefore : colorAfter;
		setTextColor(btn_color);
	}

	/** * 设置计时时候显示的文本 */
	@SuppressWarnings("null")
	public CountDownButton setTextAfter(String mStringAfter) {
		if (!(mStringAfter != null || mStringAfter.equals(""))) {
			this.textAfter = mStringAfter;
		}
		this.setText(textAfter);
		return this;
	}

	/** * 设置点击之前的文本 */
	@SuppressWarnings("null")
	public CountDownButton setTextBefore(String mStringBefore) {
		if (!(mStringBefore != null || mStringBefore.equals(""))) {
			this.textBefore = mStringBefore;
		}
		this.setText(textBefore);
		return this;
	}

	/**
	 * 设置到计时长度
	 * 
	 * @param conutDownLenght
	 *            时间 默认毫秒
	 * @return
	 */
	public CountDownButton setLenght(long conutDownLenght) {
	
		this.COUNTDOWM_LENGHT = conutDownLenght;
		return this;
	}
	
	/**
	 * 设置倒计时是否开始
	 * 
	 * @param isStar
	 *            布尔值
	 * @return
	 */
	public CountDownButton setSatr(Boolean isStar) {
		this.isStar = isStar;
		return this;
	}
	
	/**
     * 设置倒计时是否停止
     * 
     * @param isStop
     *            布尔值
     * @return
     */
    public CountDownButton setStop(Boolean isStop) {
        if(isStop){
            mHandler.sendEmptyMessage(0x02);
        }
        return this;
    }
    
    /**
     * 设置倒计时是否自动开始
     * 
     * @param isAuto
     *            布尔值
     * @return
     */
    public CountDownButton setAutoSatr(Boolean isAuto) {
        this.isStar = isAuto;
        this.performClick();
        return this;
    }
}
