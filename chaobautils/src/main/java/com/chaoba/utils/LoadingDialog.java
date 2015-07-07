package com.chaoba.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

public class LoadingDialog extends ProgressDialog {
	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	public LoadingDialog(Context context) {
		super(context, R.style.loading_dialog_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		this.setCanceledOnTouchOutside(false);
	}

}
