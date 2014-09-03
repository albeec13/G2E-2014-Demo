package com.wge.G2E2014.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.wge.G2E2014.G2E_Demo;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(1280, 800);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new G2E_Demo();
        }
}