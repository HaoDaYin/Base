package com.hello.base;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.yicheng.base.utils.Utils;


public abstract class BaseActivity extends FragmentActivity
{

	private Fragment currentFragment;

	public static BaseActivity context;


	/**
	 * 是否显示断网提示
	 * */
	protected boolean isShowNetOff = true;
	private ProgressDialog loadingFragment;


	public Fragment getCurrentFragment()
	{
		return currentFragment;
	}

	public void  setCurrentFragment(Fragment fragment)
	{
		this.currentFragment = fragment;;
	}




	protected void onCreate(final Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		onInitVariable();
//		FrameApplication.addToActivityList(this);

		this.startView(savedInstanceState);

	}

	@Override
	protected void onResume()
	{
		super.onResume();
		this.onLoadData();
	}





	protected abstract void onInitVariable();


	protected abstract void onInitView(final Bundle savedInstanceState);

	protected abstract void onInitEvent();


	protected abstract void onLoadData();

	protected abstract void onRequestData();



	protected void onNetErrorShowPage(final Bundle savedInstanceState)
	{
		setContentView(R.layout.act_empty);
		this.findViewById(R.id.textView1).setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		this.findViewById(R.id.retry).setOnClickListener(new OnClickListener()
		{

			public void onClick(View v)
			{
				BaseActivity.this.startView(BaseActivity.this.mStartBundle);
			}
		});


	}

	public void initActivity(final Bundle savedInstanceState)
	{

		this.onInitView(savedInstanceState);
		this.onInitEvent();
	}





	private Bundle mStartBundle;
	private void startView(final Bundle savedInstanceState)
	{

		if (this.isShowNetOff)
		{
			final NetworkInfo netInfo = Utils.getNetworkInfo(this);
			if (netInfo == null)
			{
				Toast.makeText(this, "网络错误", Toast.LENGTH_LONG).show();
				//				this.initActivity(savedInstanceState);
				this.onNetErrorShowPage(savedInstanceState);
			}
			else
			{
				this.initActivity(savedInstanceState);
				this.onRequestData();
			}

		}
		else
		{
			this.initActivity(savedInstanceState);
			this.onRequestData();
		}
	}


	public static View findFirstCanScrollView(ViewGroup viewGroup)
	{
		if (viewGroup != null )
		{

			for (int i = 0, N = viewGroup.getChildCount(); i < N; i++) {
				View child = viewGroup.getChildAt(i);
				if (child instanceof AbsListView) {
					Log.i("findFirstCanScrollView", "get AbsListView");
					return child;
				} else if (child instanceof ScrollView) {
					Log.i("findFirstCanScrollView", "get ScrollView");
					return child;
				} else if (child instanceof WebView) {
					Log.i("findFirstCanScrollView", "get WebView");
					return child;
				} else if (child instanceof ViewGroup) {
					View scrollView = findFirstCanScrollView((ViewGroup) child);
					if (scrollView != null) {
						return scrollView;
					}
				}
			}
		}
		return null;
	}


	public void showLoadingView(final boolean visable)
	{
		if (visable)
		{
			if (loadingFragment != null && loadingFragment.isShowing())
			{
				loadingFragment.dismiss();
				loadingFragment = null;
			}
			loadingFragment = Utils.createProgressDialog(this, "正在加载，请稍候...");
			loadingFragment.show();
		}
		else
		{
			if (loadingFragment != null && loadingFragment.isShowing())
			{
				loadingFragment.dismiss();
				loadingFragment = null;
			}
			loadingFragment = null;
		}
	}



	public  void startActivity(final Class<?> className)
	{
		final Intent i = new Intent();
		i.setClass(this, className);
		BaseActivity.this.startActivity(i);
	}


	public void addFragment(int viewID, Fragment f, boolean addToBackstack)
	{
		final NetworkInfo netInfo = Utils.getNetworkInfo(this);
		if (netInfo != null)
		{
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			transaction.add(viewID, f);
			if (addToBackstack)
			{
				transaction.addToBackStack(null);
			}
			currentFragment = f;
			transaction.commitAllowingStateLoss();
		}
	}

	//----------------------------------------------------------------------------

	public void replaceFragment(int viewID, Fragment f, boolean addToBackstack)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(viewID, f);
		if (addToBackstack)
		{
			transaction.addToBackStack(null);
		}
		currentFragment = f;
		transaction.commitAllowingStateLoss();
	}


	public void replaceFragment(int viewID,Fragment f)
	{
		replaceFragment(viewID, f, true);
	}

	//----------------------------------------------------------------------------

	public void deleteFragment(int viewID, Fragment f, boolean addToBackstack)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.remove(f);
		if (addToBackstack)
		{
			transaction.addToBackStack(null);
		}
		transaction.commitAllowingStateLoss();

	}
	//----------------------------------------------------------------------------	

	public void popStackFragment()
	{
		getSupportFragmentManager().popBackStack();
	}



}
