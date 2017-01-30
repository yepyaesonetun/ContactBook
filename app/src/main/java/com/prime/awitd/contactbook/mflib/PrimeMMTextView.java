package com.prime.awitd.contactbook.mflib;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by SantaClaus on 12/01/2017.
 */

public class PrimeMMTextView extends TextView {

    public PrimeMMTextView(Context context) {
        super(context);
        setMMText(getText().toString());
    }

    public PrimeMMTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMMText(getText().toString());
    }

    public PrimeMMTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setMMText(getText().toString());
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * method for setting text
     *
     * @param unicodeString string in Myanmar Unicode
     */
    public void setMMText(String unicodeString) {
        if (MDetect.isUnicode()) {
            setText(unicodeString);
        } else {
            setText(Rabbit.uni2zg(unicodeString));
        }
    }

    /**
     * method for getting text
     *
     * @return CharSequence in Myanmar Unicode
     */
    public CharSequence getMMText() {
        return MDetect.isUnicode() ? getText() : Rabbit.zg2uni(getText().toString());
    }
}

