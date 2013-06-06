package com.twobuntu.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

// Periodically updates the internal database to reflect the current articles on the site.
public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
