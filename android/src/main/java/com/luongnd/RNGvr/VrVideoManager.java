package com.luongnd.RNGvr;

import androidx.annotation.Nullable;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

/**
 * VrVideoManager.java
 *
 * Created by Pietralberto Mazza on 22/06/17.
 * Copyright © 2017 Facebook. All rights reserved.
 *
 */

public class VrVideoManager extends SimpleViewManager<VideoView> {
    private static final String CLASS_NAME = "VrVideo";
    private static final String TAG = VrVideoManager.class.getSimpleName();

    private VideoView view;

    public VrVideoManager(ReactApplicationContext context) { super(); }

    @Override
    public String getName() {
        return CLASS_NAME;
    }

    @Override
    protected VideoView createViewInstance(ThemedReactContext context) {
        view = new VideoView(context, context.getCurrentActivity());
        return view;
    }

    @Override
    public void onDropViewInstance(VideoView view) {
        super.onDropViewInstance(view);
        view.setPaused(true);
        view.shutdown();
        view = null;
    }

    @ReactProp(name = "displayMode")
    public void setDisplayMode(VideoView view, String mode) {
        view.setDisplayMode(mode);
    }

    @ReactProp(name = "volume")
    public void setVolume(VideoView view, float value) {
        view.setVolume(value);
    }

    @ReactProp(name = "enableFullscreenButton")
    public void setFullscreenButtonEnabled(VideoView view, Boolean enabled) {
        view.setFullscreenButtonEnabled(enabled);
    }

    @ReactProp(name = "enableCardboardButton")
    public void setCardboardButtonEnabled(VideoView view, Boolean enabled) {
        view.setCardboardButtonEnabled(enabled);
    }

    @ReactProp(name = "enableTouchTracking")
    public void setTouchTrackingEnabled(VideoView view, Boolean enabled) {
        view.setTouchTrackingEnabled(enabled);
    }

    @ReactProp(name = "hidesTransitionView")
    public void setTransitionViewEnabled(VideoView view, Boolean enabled) {
        view.setTransitionViewEnabled(!enabled);
    }

    @ReactProp(name = "enableInfoButton")
    public void setInfoButtonEnabled(VideoView view, Boolean enabled) {
        view.setInfoButtonEnabled(enabled);
    }

    @ReactProp(name = "paused")
    public void setPaused(VideoView view, Boolean paused) {
        view.setPaused(paused);
    }

    @ReactProp(name = "src")
    public void setSrc(VideoView view, ReadableMap src) {
        view.setSrc(src);
    }

    public double getDuration(VideoView view) {
        return view.getDuration();
    }

    @Override
    public Map<String,Integer> getCommandsMap() {
        return MapBuilder.of(
                "seekTo",
                1);
    }

    @Override
    public void receiveCommand(VideoView view, int commandType, @Nullable ReadableArray args) {
        Assertions.assertNotNull(view);
        Assertions.assertNotNull(args);
        switch (commandType) {
            case 1: {
                view.seekTo(args.getDouble(0));
                break;
            }
            default:
                throw new IllegalArgumentException(String.format(
                        "Unsupported command %d received by %s.",
                        commandType,
                        getClass().getSimpleName()));
        }
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        return builder
                .put("onContentLoad", MapBuilder.of("registrationName", "onContentLoad"))
                .put("onTap", MapBuilder.of("registrationName", "onTap"))
                .put("onFinish", MapBuilder.of("registrationName", "onFinish"))
                .put("onUpdatePosition", MapBuilder.of("registrationName", "onUpdatePosition"))
                .put("onChangeDisplayMode", MapBuilder.of("registrationName", "onChangeDisplayMode"))
                .build();
    }
}
