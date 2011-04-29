package edu.berkeley.cs169.tangrams.test;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.view.MotionEvent;

public class TouchUtils {
	public static void drag(InstrumentationTestCase test, float fromX, float toX, float fromY, float toY,
            int stepCount) {
        Instrumentation inst = test.getInstrumentation();

        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();

        float y = fromY;
        float x = fromX;
        
        float yStep = (toY - fromY) / stepCount;
        float xStep = (toX - fromX) / stepCount;

        MotionEvent event = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, fromX, fromY, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();

        for (int i = 0; i < stepCount; ++i) {
            y += yStep;
            x += xStep;
            eventTime = SystemClock.uptimeMillis();
            event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE, x, y, 0);
            inst.sendPointerSync(event);
            inst.waitForIdleSync();
        }

        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        inst.sendPointerSync(event);
        inst.waitForIdleSync();
    }

}
