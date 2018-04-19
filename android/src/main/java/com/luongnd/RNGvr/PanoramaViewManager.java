package com.luongnd.RNGvr;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

public class PanoramaViewManager extends SimpleViewManager<PanoramaView> {
    private static final String REACT_CLASS = "Panorama";

    private PanoramaView view;

    public PanoramaViewManager(ReactApplicationContext context) {
        super();
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    public PanoramaView createViewInstance(ThemedReactContext context) {
        view = new PanoramaView(context, context.getCurrentActivity());
        return view;
    }

    @Override
    public void onDropViewInstance(PanoramaView view) {
        super.onDropViewInstance(view);
        view.shutdown();
        view = null;
    }

    @Override
    protected void onAfterUpdateTransaction(PanoramaView view) {
        super.onAfterUpdateTransaction(view);
        view.onAfterUpdateTransaction();
    }

    @ReactProp(name = "src")
    public void setSrc(PanoramaView view, ReadableMap src) {
        String imageUrl = src.getString("uri");
        String type = src.getString("type");
        view.setImageUrl(imageUrl);
        view.setInputType(type);

    }

    @ReactProp(name = "displayMode")
    public void setDisplayMode(PanoramaView view, String mode) {
        view.setDisplayMode(mode);
    }

    @ReactProp(name = "enableFullscreenButton")
    public void setFullscreenButtonEnabled(PanoramaView view, Boolean enabled) {
        view.setFullscreenButtonEnabled(enabled);
    }

    @ReactProp(name = "enableCardboardButton")
    public void setCardboardButtonEnabled(PanoramaView view, Boolean enabled) {
        view.setCardboardButtonEnabled(enabled);
    }

    @ReactProp(name = "enableTouchTracking")
    public void setTouchTrackingEnabled(PanoramaView view, Boolean enabled) {
        view.setTouchTrackingEnabled(enabled);
    }

    @ReactProp(name = "hidesTransitionView")
    public void setTransitionViewEnabled(PanoramaView view, Boolean enabled) {
        view.setTransitionViewEnabled(!enabled);
    }

    @ReactProp(name = "enableInfoButton")
    public void setInfoButtonEnabled(PanoramaView view, Boolean enabled) {
        view.setInfoButtonEnabled(enabled);
    }

    public @Nullable Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
            .put("onContentLoad", MapBuilder.of("registrationName", "onContentLoad"))
            .put("onTap", MapBuilder.of("registrationName", "onTap"))
            .put("onChangeDisplayMode", MapBuilder.of("registrationName", "onChangeDisplayMode"))
            .build();
    }
}
