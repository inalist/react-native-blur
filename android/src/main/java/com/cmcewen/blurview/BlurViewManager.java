package com.cmcewen.blurview;

import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.fivehundredpx.android.blur.BlurringView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlurViewManager extends SimpleViewManager<BlurringView> {
    public static final String REACT_CLASS = "BlurView";

    public static final int defaultRadius = 1;
    public static final int defaultSampling = 10;
    public static final int defaultBlurSpeed = 300;
    private int configuredBlurRadius = defaultRadius;
    private int configuredBlurSpeed = defaultBlurSpeed;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public BlurringView createViewInstance(ThemedReactContext context) {
        BlurringView blurringView = new BlurringView(context);
        blurringView.setBlurRadius(defaultRadius);
        blurringView.setDownsampleFactor(defaultSampling);
        return blurringView;
    }

    @ReactProp(name = "blurRadius", defaultInt = defaultRadius)
    public void setRadius(BlurringView view, int radius) {
        configuredBlurRadius = radius;
    }

    @ReactProp(name = "overlayColor", customType = "Color")
    public void setColor(BlurringView view, int color) {
        view.setOverlayColor(color);
    }

    @ReactProp(name = "downsampleFactor", defaultInt = defaultSampling)
    public void setDownsampleFactor(BlurringView view, int factor) {
        view.setDownsampleFactor(factor);
    }

    @ReactProp(name = "downsampleFactor", defaultInt = defaultBlurSpeed)
    public void setBlurSpeed(BlurringView view, int milliseconds) {
        configuredBlurSpeed = milliseconds;
    }

    @ReactProp(name = "viewRef")
    public void setViewRef(BlurringView view, int viewRef) {
        if (viewRef == 0 || view.getParent() == null)
        {
            view.setBlurredView(null);
            view.invalidate();
            return;
        }
        if (viewRef == -1) {
            animateBlurRadius(view, configuredBlurRadius, defaultRadius, configuredBlurSpeed);
            return;
        }

        View v = ((ViewGroup) view.getParent()).getChildAt(0);
        view.setBlurredView(v);
        animateBlurRadius(view, defaultRadius, configuredBlurRadius, configuredBlurSpeed);
    }

    private void animateBlurRadius(final BlurringView view, final int startRadius, final int endRadius, int milliseconds) {
        if (startRadius == endRadius)
            return;
        final boolean animateUp = endRadius >= startRadius;

        final ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            int currentBlurRadius = startRadius;
            @Override
            public void run() {
                if (animateUp)
                    currentBlurRadius++;
                else
                    currentBlurRadius--;
                view.setBlurRadius(currentBlurRadius);
                view.postInvalidateOnAnimation();
                if ((animateUp && currentBlurRadius >= endRadius) || (!animateUp && currentBlurRadius <= endRadius)) {
                    if (!animateUp) {
                        view.setBlurRadius(0);
                        view.postInvalidateOnAnimation();
                    }
                    ses.shutdown();
                }
            }
        }, 0, milliseconds/(configuredBlurRadius - 1), TimeUnit.MILLISECONDS);
    }
}