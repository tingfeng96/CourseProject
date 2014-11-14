package com.example.mobilesafe.ui;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.clearcache.CacheInfo;
import com.example.mobilesafe.clearcache.TextFormater;

public class ClearCacheActivity extends ListActivity {
	private PackageManager pm;
	private ListView lv;
	private MyAdapter adapter;

	private Map<String, CacheInfo> maps;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clearcachemain);
		pm = getPackageManager();
		maps = new HashMap<String, CacheInfo>();
		lv = getListView();

		// lv.setAdapter(adapter);

		// 1.��ȡ���еİ�װ�õ�Ӧ�ó���

		List<PackageInfo> packageinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo info : packageinfos) {
			String name = info.applicationInfo.loadLabel(pm).toString();
			String packname = info.packageName;
			CacheInfo cacheinfo = new CacheInfo();
			cacheinfo.setName(name);
			cacheinfo.setPackname(packname);
			maps.put(packname, cacheinfo);
			getAppSize(packname);
		}

		adapter = new MyAdapter();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/**
				 *       <intent-filter>
                <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS" />
                <category android:name="android.intent.category.DEFAULT" />
                <data android:scheme="package" />
            </intent-filter>
				 */
				/**
				 *             <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.VOICE_LAUNCH" />
            </intent-filter>
				 */
				System.out.println("haha");
				Intent intent  = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.addCategory("android.intent.category.VOICE_LAUNCH");
				CacheInfo info = (CacheInfo) lv.getItemAtPosition(position);
				intent.putExtra("pkg", info.getPackname());
				startActivity(intent);
			}
		});

	}

	/**
	 * ���ݰ�����ȡӦ�ó���������Ϣ ע��: ���������һ���첽�ķ��� ��������Ҫ��һ��ʱ����ܻ�ȡ��.
	 * 
	 * @param packname
	 */
	private void getAppSize(final String packname) {
		try {
			Method method = PackageManager.class.getMethod(
					"getPackageSizeInfo", new Class[] { String.class,
							IPackageStatsObserver.class });

			method.invoke(pm, new Object[] { packname,
					new IPackageStatsObserver.Stub() {

						public void onGetStatsCompleted(PackageStats pStats,
								boolean succeeded) throws RemoteException {
							// ע�����������һ���첽�Ĳ���
							long cachesize = pStats.cacheSize;
							long codesize = pStats.codeSize;
							long datasize = pStats.dataSize;

							CacheInfo info = maps.get(packname);
							info.setCache_size(TextFormater
									.getDataSize(cachesize));
							info.setData_size(TextFormater
									.getDataSize(datasize));
							info.setCode_size(TextFormater
									.getDataSize(codesize));
							maps.put(packname, info);
							
							
							
						}
					} });

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyAdapter extends BaseAdapter {

		private Set<Entry<String, CacheInfo>> sets;
		private List<CacheInfo> cacheinfos;

		public MyAdapter() {

			sets = maps.entrySet();
			cacheinfos = new ArrayList<CacheInfo>();
			for (Entry<String, CacheInfo> entry : sets) {
				cacheinfos.add(entry.getValue());
			}

		}

		public int getCount() {

			return cacheinfos.size();
		}

		public Object getItem(int position) {

			return cacheinfos.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			CacheInfo info = cacheinfos.get(position);
			if (convertView == null) {
				view = View.inflate(ClearCacheActivity.this, R.layout.item, null);
			} else {
				view = convertView;
			}
			TextView tv_cache_size = (TextView) view
					.findViewById(R.id.tv_cache_size);
			TextView tv_code_size = (TextView) view
					.findViewById(R.id.tv_code_size);
			TextView tv_data_size = (TextView) view
					.findViewById(R.id.tv_data_size);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_name);

			tv_cache_size.setText(info.getCache_size());
			tv_code_size.setText(info.getCode_size());
			tv_data_size.setText(info.getData_size());
			tv_name.setText(info.getName());

			return view;
		}

	}

}