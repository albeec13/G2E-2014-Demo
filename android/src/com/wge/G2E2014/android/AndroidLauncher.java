package com.wge.G2E2014.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.wge.G2E2014.G2E_Demo;
import com.wge.G2E2014.Helpers.Platform;


public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;

        PlatformAndroid platform = new PlatformAndroid(this);

		initialize(new G2E_Demo((Platform) platform), config);
	}
}

