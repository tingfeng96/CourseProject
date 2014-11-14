package com.example.mobilesafe.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class SetupGudie3Activity extends Activity implements OnClickListener {
	private Button bt_next;
	private Button bt_pre;
	private Button bt_select_contact;
	private EditText et_number;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupguide3);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_pre = (Button) this.findViewById(R.id.bt_pre);
		bt_select_contact = (Button) this.findViewById(R.id.bt_select_contact);
		bt_next.setOnClickListener(this);
		bt_pre.setOnClickListener(this);
		bt_select_contact.setOnClickListener(this);
		et_number = (EditText) this.findViewById(R.id.et_setup3_number);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_next:
			String number = et_number.getText().toString().trim();
			if("".equals(number)){
				Toast.makeText(this, "��ȫ���벻��Ϊ��", 0).show();
				return;
			}else{
			   Editor editor = sp.edit();
			   editor.putString("safenumber", number);
			   editor.commit();
			}
			Intent intent4 = new Intent(this,SetupGudie4Activity.class);
			//һ��Ҫ�ѵ�ǰ��activity������ջ�����Ƴ�
			finish();
			startActivity(intent4);
			//����activity�л�ʱ��Ķ���Ч��
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;

		case R.id.bt_pre:
			Intent intent2 = new Intent(this,SetupGudie2Activity.class);
			//һ��Ҫ�ѵ�ǰ��activity������ջ�����Ƴ�
			finish();
			startActivity(intent2);
			//����activity�л�ʱ��Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			
			break;
		case R.id.bt_select_contact:
			Intent intent = new Intent(this,SelectContactActivity.class);
			//����һ��������ֵ�Ľ���
			startActivityForResult(intent, 0);
			
			
			break;
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if(data!=null){
			String number = data.getStringExtra("number");
			et_number.setText(number);
		}
	}

}
