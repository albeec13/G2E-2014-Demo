package com.wge.G2E2014.Helpers;

/**
 * Created by achaharbakhshi on 9/4/2014.
 */
public class Box2DHelper {
    private static final float PPM = 100; //pixels per meter}

    public static final float PPM(float inputPixels) {
        return inputPixels / PPM;
    }
}