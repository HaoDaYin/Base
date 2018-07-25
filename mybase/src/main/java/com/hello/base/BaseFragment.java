package com.hello.base;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hello.utils.Utils;


public abstract class BaseFragment extends Fragment
{
	protected BaseActivity context;
	protected Bundle bundle;
	
	public void setContext(BaseActivity context)
	{
		this.context = context;
	}
	
	public void setBundle(Bundle bundle)
	{
		this.bundle = bundle;
	}
	
	public BaseActivity getContext()
	{
		return context;
	}

	public void onResume()
	{
//		context.setCurrentFragment();
		this.onLoadData();
		super.onResume();
	}
	

	public void onPause()
	{
		this.onUnLoadData();
		super.onPause();
	}
	
	public void onDestroy()
	{
		this.onRelease();
		super.onDestroy();
	}
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		this.onInitVariable();
		super.onCreate(savedInstanceState);
	}
	
	
	protected abstract void onInitVariable();
	
	protected abstract View onInitView(LayoutInflater inflater, final Bundle savedInstanceState);
	
	protected abstract void onRequseData();
	
	protected abstract void onLoadData();
	
	protected abstract void onUnLoadData();
	
	protected abstract void onRelease();
	
	protected void onViewCreate(final View v)
	{
		
	}
	
	
	/**是否显示断网提示*/
	protected boolean isShowNetOff = true;
	
	View v = null;
	
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		startView(inflater, container, savedInstanceState);
		return v;
	}
	
	
	private void startView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		if (isShowNetOff)
		{
			final NetworkInfo netInfo = Utils.getNetworkInfo(context);
			if (netInfo == null)
			{
				onNetErrorShowPage(inflater, container, savedInstanceState);
			}
			else
			{
				createView(inflater, container, savedInstanceState);
			}
		}
		else
		{
			createView(inflater, container, savedInstanceState);
		}
	}
	
	
	private void createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		v = this.onInitView(inflater, savedInstanceState);
		final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		if (v == null)
		{
			v = new View(this.getActivity());
		}
		v.setLayoutParams(params);
		this.onViewCreated(v, savedInstanceState);
		this.onRequseData();
	}
	
	//--------------------------------------------------------------------------------------------
	protected void onNetErrorShowPage(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
	{
		v = inflater.inflate(R.layout.act_empty, null);
		final Button imageView = (Button) v.findViewById(R.id.retry);
		final TextView setting = (TextView) v.findViewById(R.id.textView1);
		
		setting.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		
		
		imageView.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				final NetworkInfo netInfo = Utils.getNetworkInfo(context);
				if (netInfo == null)
				{
					Toast.makeText(context, "没有可用网络", Toast.LENGTH_LONG).show();
				}
				else
				{
					context.initActivity(savedInstanceState);
					context.onRequestData();
				}
			}
		});
		
	}
	
	
	
	
	
	
	
	
	
}
