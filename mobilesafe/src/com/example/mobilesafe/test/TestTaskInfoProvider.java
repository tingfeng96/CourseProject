package com.example.mobilesafe.test;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.test.AndroidTestCase;

import com.example.mobilesafe.domain.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoProvider;

public class TestTaskInfoProvider extends AndroidTestCase{

	public void testGetAlltask() throws Exception{
		TaskInfoProvider provider = new TaskInfoProvider(getContext());
		ActivityManager am = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo>  runingappinfos = am.getRunningAppProcesses();
		List<TaskInfo> taskinfos =provider.getAllTasks(runingappinfos);
		System.out.println(taskinfos.size());
		for(TaskInfo taskinfo : taskinfos){
			System.out.println(taskinfo.getAppname());
		}
	}
}
