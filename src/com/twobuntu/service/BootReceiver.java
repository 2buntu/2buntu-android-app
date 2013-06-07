package com.twobuntu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

// Immediately starts the update service when the device boots.
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		UpdateService.scheduleUpdate(context);
	}
}
