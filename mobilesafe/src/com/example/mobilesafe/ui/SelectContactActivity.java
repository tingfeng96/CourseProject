package com.example.mobilesafe.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.domain.ContactInfo;
import com.example.mobilesafe.engine.ContactInfoService;

public class SelectContactActivity extends Activity {
	private ListView lv;
	private List<ContactInfo>  infos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_contact);

		ContactInfoService service = new ContactInfoService(this);
		infos = service.getContactInfos();
		
		
		lv = (ListView) this.findViewById(R.id.lv_select_contact);
		lv.setAdapter(new SelectContactAdapter());
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String phone = infos.get(position).getPhone();
				Intent intent  = new Intent();
				intent.putExtra("number", phone);
				setResult(0, intent);
				finish();
				
			}});
	}

	private class SelectContactAdapter extends BaseAdapter{

		public int getCount() {
			return infos.size();
		}

		public Object getItem(int position) {
			return infos.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ContactInfo info = infos.get(position);
			LinearLayout ll = new LinearLayout(SelectContactActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView tv_name = new TextView(SelectContactActivity.this);
			tv_name.setText("����"+ info.getName());
			TextView tv_phone = new TextView(SelectContactActivity.this);
			tv_phone.setText("��ϵ�绰"+ info.getPhone());
			ll.addView(tv_name);
			ll.addView(tv_phone);
			
			return ll;
		}
		
	}
}
