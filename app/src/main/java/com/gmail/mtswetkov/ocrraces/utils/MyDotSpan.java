package com.gmail.mtswetkov.ocrraces.utils;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.prolificinteractive.materialcalendarview.spans.DotSpan;

public class MyDotSpan extends DotSpan {

    private  final float radius;
    private  int color;

    public MyDotSpan() {
        this.radius = DEFAULT_RADIUS;
        this.color = 0;
    }

    public MyDotSpan(int color) {
        this.radius = DEFAULT_RADIUS;
        this.color = color;
    }

    public MyDotSpan(float radius) {
        this.radius = radius;
        this.color = 0;
    }

    /**
     * Create a span to draw a dot using a specified radius and color
     *
     * @param radius radius for the dot
     * @param color  color of the dot
     */
    public MyDotSpan(float radius, int color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public void drawBackground(
            Canvas canvas, Paint paint,
            int left, int right, int top, int baseline, int bottom,
            CharSequence charSequence,
            int start, int end, int lineNum
    ) {
        int oldColor = paint.getColor();
        if (color != 0) {
            paint.setColor(color);
        }
        canvas.drawCircle((left + right) / 2, (top+bottom)/2, radius, paint);
        paint.setColor(oldColor);
    }
}
