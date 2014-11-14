package com.example.mobilesafe.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.mobilesafe.engine.NumberAddressService;

public class QueryNumberActivity extends Activity {
	private TextView tv_query_number_address;
	private EditText et_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.query_number);
		tv_query_number_address = (TextView) this.findViewById(R.id.tv_query_number_address);
		et_number = (EditText) this.findViewById(R.id.et_query_number);
	}	
	
	
	public void query(View view){
		// �жϺ����Ƿ�Ϊ�� 
		String number =  et_number.getText().toString().trim();
		if(TextUtils.isEmpty(number)){
	        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
	        et_number.startAnimation(shake);
		}else{
			//�����ݿ� ��ѯ���������
		   String address =	NumberAddressService.getAddress(number);
		   tv_query_number_address.setText("��������Ϣ "+ address);
			
		}
		
	}

}
