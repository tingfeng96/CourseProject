package com.example.mobilesafe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.mobilesafe.R;

public class SetupGudie1Activity extends Activity implements OnClickListener {
	private Button bt_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setupguide1);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_next:
			Intent intent = new Intent(this,SetupGudie2Activity.class);
			//һ��Ҫ�ѵ�ǰ��activity������ջ�����Ƴ�
			finish();
			startActivity(intent);
			//����activity�л�ʱ��Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;

		}
		
	}

}
