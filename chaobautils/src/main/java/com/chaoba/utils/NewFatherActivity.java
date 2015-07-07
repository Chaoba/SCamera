package com.chaoba.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A FatherActivity that contains some useful member variables and methods other
 * activity can inherit to make a new Activity easier
 * 
 * @author Liyanshun
 * 
 */
public abstract class NewFatherActivity extends Activity {
	protected Context mContext;
	protected ImageButton mBackButton;
	protected TextView mTitleView, mRrightButton;
	protected LoadingDialog mLoading;
	protected LayoutInflater mLayoutInflater;
    protected View mTitleBar;
	private ViewGroup mBodyViewGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLoading = new LoadingDialog(mContext);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(com.chaoba.utils.R.layout.father_layout);
        mTitleBar=findViewById(R.id.title_bar);
		mBackButton = (ImageButton) findViewById(com.chaoba.utils.R.id.back_button);
		mBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mTitleView = (TextView) findViewById(com.chaoba.utils.R.id.title);
		mRrightButton = (TextView) findViewById(com.chaoba.utils.R.id.right_button);
		mBodyViewGroup = (ViewGroup) findViewById(com.chaoba.utils.R.id.body);
		init();
	}

	/**
	 * set the id of content view
	 * 
	 * @param id
	 */
	public void setView(int id) {
		LayoutInflater.from(this).inflate(id, mBodyViewGroup);
	}

	/**
	 * set the title of current page
	 */
	public void setTitle(int stringId) {
		mTitleView.setText(stringId);
	}

	public void setTitle(String title) {
		mTitleView.setText(title);
	}

	/**
	 * set the right button of current page
	 */
	public void setRightButton(int stringId, boolean show) {
		mRrightButton.setText(stringId);
		mRrightButton.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	/**
	 * get the data delivered by intent
	 * 
	 * @param key
	 * @return the extra object or null
	 */
	public Object getExtra(String key) {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle dataBundle = intent.getExtras();
			if (dataBundle != null) {
				return dataBundle.get(key);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * implement this method to do some init jobs,such as
	 * {@link NewFatherActivity#setView}
	 * {@link NewFatherActivity#setTitle}
	 * {@link NewFatherActivity#setRightButton}
	 * {@link NewFatherActivity#getExtra}
	 */
	public abstract void init();

	/**
	 * override this method to avoid fast click start to many activities.
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (isFastDoubleClick()) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private static long lastClickTime;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (timeD >= 0 && timeD <= 400) {
			return true;
		} else {
			lastClickTime = time;
			return false;
		}
	}

	@Override
	public void startActivity(Intent i) {
		super.startActivity(i);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}

	@Override
	public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.slide_right_in,
		// R.anim.slide_right_out);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
}
