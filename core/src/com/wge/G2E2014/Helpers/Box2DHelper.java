package com.wge.G2E2014.Helpers;

public class Box2DHelper {
    private static float PPM = 200; //pixels per meter}

    public static float PixelsToMeters(float inputPixels) {
        return inputPixels / PPM;
    }

    public static float MetersToPixels(float inputMeters) {
        return inputMeters * PPM;
    }
}
