package com.twobuntu.twobuntu;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

// Performs application level initialization.
public class TwobuntuApplication extends Application {
	
	@Override
    public void onCreate() {
		super.onCreate();
		// Set up the image loader.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		        .memoryCacheExtraOptions(48, 48)
		        .build();
		ImageLoader.getInstance().init(config);
	}
}
