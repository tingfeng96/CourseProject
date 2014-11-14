package com.example.mobilesafe.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.mobilesafe.adapter.AppManagerAdapter;
import com.example.mobilesafe.domain.AppInfo;
import com.example.mobilesafe.engine.AppInfoProvider;

public class AppManagerActivity extends Activity implements OnClickListener {
	protected static final int GET_ALL_APP_FINISH = 80;
	protected static final int GET_USER_APP_FINISH = 81;
	private static final String TAG = "AppManagerActivity";
	private ListView lv_app_manager;
	private LinearLayout ll_loading;
	private AppInfoProvider provider;
	private List<AppInfo> appinfos;
	private List<AppInfo> userAppinfos;
	private AppManagerAdapter adapter;
	private PopupWindow localPopupWindow;
	private TextView tv_app_manager_title;
	private boolean isloading = false;
	private String packname;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_ALL_APP_FINISH:
				ll_loading.setVisibility(View.INVISIBLE);
				// TODO: ���������ø�listview������������
				adapter = new AppManagerAdapter(appinfos,
						AppManagerActivity.this);
				lv_app_manager.setAdapter(adapter);
				isloading = false;
				break;
			case GET_USER_APP_FINISH:
				ll_loading.setVisibility(View.INVISIBLE);
				// TODO: ���������ø�listview������������
				adapter = new AppManagerAdapter(userAppinfos,
						AppManagerActivity.this);
				lv_app_manager.setAdapter(adapter);
				isloading = false;
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager);
		tv_app_manager_title = (TextView) this
				.findViewById(R.id.tv_app_manager_title);
		tv_app_manager_title.setOnClickListener(this);
		lv_app_manager = (ListView) this.findViewById(R.id.lv_app_manager);
		// lv_app_manager.setAdapter(adapter);
		ll_loading = (LinearLayout) this
				.findViewById(R.id.ll_app_manager_loading);

		initUI(true);

		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				dismissPopUpwindow();

				// ��ȡ��ǰview�����ڴ����е�λ��
				int[] arrayOfInt = new int[2];
				view.getLocationInWindow(arrayOfInt);

				int i = arrayOfInt[0] + 60;
				int j = arrayOfInt[1];

				AppInfo info = (AppInfo) lv_app_manager
						.getItemAtPosition(position);
				// String packname = info.getPackname();
				/*
				 * TextView tv = new TextView (AppManagerActivity.this);
				 * tv.setTextSize(20); tv.setTextColor(Color.RED);
				 * tv.setText(packname);
				 */
				View popupview = View.inflate(AppManagerActivity.this,
						R.layout.popup_item, null);
				LinearLayout ll_start = (LinearLayout) popupview
						.findViewById(R.id.ll_start);
				LinearLayout ll_uninstall = (LinearLayout) popupview
						.findViewById(R.id.ll_uninstall);
				LinearLayout ll_share = (LinearLayout) popupview
						.findViewById(R.id.ll_share);

				// �ѵ�ǰ��Ŀ��listview�е�λ�����ø�view����
				ll_share.setTag(position);
				ll_uninstall.setTag(position);
				ll_start.setTag(position);

				ll_start.setOnClickListener(AppManagerActivity.this);
				ll_uninstall.setOnClickListener(AppManagerActivity.this);
				ll_share.setOnClickListener(AppManagerActivity.this);

				LinearLayout ll = (LinearLayout) popupview
						.findViewById(R.id.ll_popup);
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(200);
				localPopupWindow = new PopupWindow(popupview, 350, 100);
				// һ��Ҫ�ǵø�popupwindow���ñ�����ɫ
				// Drawable background = new ColorDrawable(Color.TRANSPARENT);
				Drawable background = getResources().getDrawable(
						R.drawable.local_popup_bg);
				localPopupWindow.setBackgroundDrawable(background);
				localPopupWindow.showAtLocation(view, Gravity.LEFT
						| Gravity.TOP, i, j);
				ll.startAnimation(sa);

			}
		});

		lv_app_manager.setOnScrollListener(new OnScrollListener() {

			public void onScrollStateChanged(AbsListView view, int scrollState) {
				dismissPopUpwindow();

			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopUpwindow();

			}
		});
	}

	/**
	 * 
	 * @param flag
	 *            true ������Ǹ������еĳ��� false ������Ǹ����û��ĳ���
	 */
	private void initUI(final boolean flag) {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			@Override
			public void run() {
				isloading = true;
				if (flag) {
					provider = new AppInfoProvider(AppManagerActivity.this);
					appinfos = provider.getAllApps();
					//
					Message msg = new Message();
					msg.what = GET_ALL_APP_FINISH;
					handler.sendMessage(msg);
				}else {
					provider = new AppInfoProvider(AppManagerActivity.this);
					appinfos = provider.getAllApps();
					userAppinfos = getUserApps(appinfos);
					Message msg = new Message();
					msg.what = GET_USER_APP_FINISH;
					handler.sendMessage(msg);
				}
			}
		}.start();
	}

	private void dismissPopUpwindow() {
		/*
		 * ��ֻ֤��һ��popupwindow��ʵ������
		 */
		if (localPopupWindow != null) {
			localPopupWindow.dismiss();
			localPopupWindow = null;
		}
	}

	public void onClick(View v) {
		if (isloading) {
			return;
		}

		int positon = 0;
		if (v.getTag() != null) {
			positon = (Integer) v.getTag();
		}
		// �жϵ�ǰ�б��״̬
		String titletext;

		AppInfo item;

		TextView tv;
		if (v instanceof TextView) {
			tv = (TextView) v;
			titletext = tv.getText().toString();
			if ("���г���".equals(titletext)) {
				item = appinfos.get(positon);
				packname = item.getPackname();
			} else {
				item = userAppinfos.get(positon);
				packname = item.getPackname();
			}
		} else {
			if ("���г���".equals(tv_app_manager_title.getText().toString())) {
				item = appinfos.get(positon);
				packname = item.getPackname();
			} else {
				item = userAppinfos.get(positon);
				packname = item.getPackname();
			}
		}

		dismissPopUpwindow();
		switch (v.getId()) {
		case R.id.tv_app_manager_title:
			tv = (TextView) v;
			titletext = tv.getText().toString();

			if ("���г���".equals(titletext)) {
				// �л����û�����
				tv.setText("�û�����");
				// ����listview���б�
				userAppinfos = getUserApps(appinfos);
				adapter.setAppInfos(userAppinfos);
				adapter.notifyDataSetChanged();

			} else {
				// �л������г���
				tv.setText("���г���");
				adapter.setAppInfos(appinfos);
				adapter.notifyDataSetChanged();
			}

			break;
		case R.id.ll_share:
			Log.i(TAG, "����" + packname);
			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			// shareIntent.putExtra("android.intent.extra.SUBJECT", "����");
			shareIntent.setType("text/plain");
			// ��Ҫָ����ͼ����������
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "����");
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"�Ƽ���ʹ��һ������" + item.getAppname());
			shareIntent = Intent.createChooser(shareIntent, "����");
			startActivity(shareIntent);
			break;
		case R.id.ll_uninstall:

			// ������ж��ϵͳ��Ӧ�ó���
			if (item.isSystemApp()) {
				Toast.makeText(this, "ϵͳӦ�ò��ܱ�ɾ��", 0).show();
			} else {
				Log.i(TAG, "ж��" + packname);
				String uristr = "package:" + packname;
				Uri uri = Uri.parse(uristr);
				Intent deleteIntent = new Intent();
				deleteIntent.setAction(Intent.ACTION_DELETE);
				deleteIntent.setData(uri);
				startActivityForResult(deleteIntent, 0);
			}
			break;
		case R.id.ll_start:
			Log.i(TAG, "����" + packname);
			// getPackageManager().queryIntentActivities(intent, flags);
			try {
				PackageInfo info = getPackageManager().getPackageInfo(
						packname,
						PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_ACTIVITIES);
				ActivityInfo[] activityinfos = info.activities;
				if (activityinfos.length > 0) {
					ActivityInfo startActivity = activityinfos[0];
					Intent intent = new Intent();
					intent.setClassName(packname, startActivity.name);
					startActivity(intent);
				} else {
					Toast.makeText(this, "��ǰӦ�ó����޷�����", 0).show();
				}
			} catch (Exception e) {
				Toast.makeText(this, "Ӧ�ó����޷�����", 0).show();
				e.printStackTrace();
			}

			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if ("���г���".equals(tv_app_manager_title.getText().toString())) {
			// ˢ�µ�ǰ�ĳ����б�
			initUI(true);
		} else {
			// �����û�������б���Ϣ
			initUI(false);
		}
	}

	/**
	 * ��ȡ���е��û���װ��app
	 * 
	 * @param appinfos
	 * @return
	 */
	private List<AppInfo> getUserApps(List<AppInfo> appinfos) {
		List<AppInfo> userAppinfos = new ArrayList<AppInfo>();
		for (AppInfo appinfo : appinfos) {
			if (!appinfo.isSystemApp()) {
				userAppinfos.add(appinfo);
			}
		}
		return userAppinfos;
	}

	@Override
	protected void onDestroy() {
		dismissPopUpwindow();
		super.onDestroy();
	}

	
	
}
