package com.example.mobilesafe.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	
	/**
	 * 
	 * @param dbname ���ݿ��·��
	 * @return ���ݿ�Ķ��� 
	 */
	public static SQLiteDatabase getAddressDB(String path){ 
		return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	
	
}
