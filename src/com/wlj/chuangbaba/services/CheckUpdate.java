package com.wlj.chuangbaba.services;

import com.wlj.update.Update;
import com.wlj.update.Update.NextStep;
import com.wlj.update.Update.state;
import com.wlj.util.Log;

import android.app.AlertDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

public class CheckUpdate extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}

	
	@Override
	public void onCreate() {
		CheckVersion("http://www.chuangbb.com/update.xml");
		super.onCreate();
	}

	private void CheckVersion(String path) {

		new Update(getApplicationContext(), path,new NextStep(){

			@Override
			public void next(state what) {
				
				System.out.println(what.name() + "  " + what.ordinal());

				if (what == state.no_update) {

				} else if (what == state.service_timeout) {

				} else if (what == state.download_failed) {

				} else if (what == state.version_exception) {

				}
				Log.e("dd", "CheckUpdate  stopSelf");
				stopSelf();
			}
		}){}.check();

	}
}
