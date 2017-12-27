package ru.spbau.mit.circuit.ui;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;

public class MySquareImageButton extends AppCompatImageButton {
    public MySquareImageButton(Context context) {
        super(context);
    }

    public MySquareImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySquareImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec / 3 * 2);
    }
}