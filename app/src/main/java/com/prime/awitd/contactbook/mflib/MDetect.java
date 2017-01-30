package com.prime.awitd.contactbook.mflib;

import android.graphics.Paint;

/**
 * Created by SantaClaus on 12/01/2017.
 */

public class MDetect {
    /**
     * method for getting user's encoding
     *
     * @return whether user's encoding follows myanmar unicode standard
     */
    public static boolean isUnicode() {
        Paint paint = new Paint();
        return paint.measureText("\u1000\u1039\u1000")
                < paint.measureText("\u1000") + paint.measureText("\u1000") / 3;
    }
}
