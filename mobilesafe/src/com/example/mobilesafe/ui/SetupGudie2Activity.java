package com.example.mobilesafe.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.mobilesafe.R;

public class SetupGudie2Activity extends Activity implements OnClickListener {
	private Button bt_next;
	private Button bt_pre;
	private Button bt_bind;
	private CheckBox cb_bind;
	private SharedPreferences sp;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupguide2);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_pre = (Button) this.findViewById(R.id.bt_previous);
		bt_bind = (Button)this.findViewById(R.id.bt_bind);
		cb_bind = (CheckBox)this.findViewById(R.id.cb_bind);
	
		
		bt_bind.setOnClickListener(this);
		bt_pre.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		// ���ȳ�ʼ�� checkbox��״̬
		String sim = sp.getString("sim", null);
		if(sim!=null){
			cb_bind.setText("�Ѿ���");
			cb_bind.setChecked(true);
		}else{
			cb_bind.setText("û�а�");
			cb_bind.setChecked(false);
			resetSimInfo();
		}
		cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					cb_bind.setText("�Ѿ���");
					setSimInfo();
				}else{
					cb_bind.setText("û�а�");
					resetSimInfo();
				}
				
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_bind:
			setSimInfo();
			cb_bind.setText("�Ѿ���");
			cb_bind.setChecked(true);
			break;

		case R.id.bt_previous:
			Intent intent1 = new Intent(this,SetupGudie1Activity.class);
			//һ��Ҫ�ѵ�ǰ��activity������ջ�����Ƴ�
			finish();
			startActivity(intent1);
			//����activity�л�ʱ��Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.bt_next:
			Intent intent3 = new Intent(this,SetupGudie3Activity.class);
			//һ��Ҫ�ѵ�ǰ��activity������ջ�����Ƴ�
			finish();
			startActivity(intent3);
			//����activity�л�ʱ��Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
		
	}

	private void setSimInfo() {
		TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String simserial =  manager.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("sim", simserial);
		editor.commit();
	}
	private void resetSimInfo() {
		Editor editor = sp.edit();
		editor.putString("sim", null);
		editor.commit();
	}
}
