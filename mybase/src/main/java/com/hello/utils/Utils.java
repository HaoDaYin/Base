package com.hello.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hello.base.BaseActivity;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Utils
{
	/**
	 * @return 返回当前活动的详细信息默认数据网络
	 * */
	public static NetworkInfo getNetworkInfo(final Context context)
	{
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = cm.getActiveNetworkInfo();
		return info;
	}

	//-----------------------------------------------------------------------------------------------
	public static ProgressDialog createProgressDialog(final BaseActivity a, final String msg)
	{
		
		final ProgressDialog dialog = new ProgressDialog(a);
		dialog.setMessage(msg);
		dialog.setCanceledOnTouchOutside(false);
		return dialog;
	}
	//-----------------------------------------------------------------------------------------------
	private static DecimalFormat dfs = null;

	public static DecimalFormat format(String pattern) {
		if (dfs == null) {
			dfs = new DecimalFormat();
		}
		dfs.setRoundingMode(RoundingMode.FLOOR);
		dfs.applyPattern(pattern);
		return dfs;
	}












}
