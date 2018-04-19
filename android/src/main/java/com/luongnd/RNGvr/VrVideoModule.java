package com.luongnd.RNGvr;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

public class VrVideoModule extends ReactContextBaseJavaModule {
    private static final String E_MODULE_ERROR = "E_MODULE_ERROR";

    private ReactApplicationContext mReactContext;

    public VrVideoModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "VrVideoModule";
    }

    @ReactMethod
    public void getDuration(final int reactTag, final Promise promise) {
        try {
            UIManagerModule uiManager = mReactContext.getNativeModule(UIManagerModule.class);
            uiManager.addUIBlock(new UIBlock() {
                public void execute (NativeViewHierarchyManager nvhm) {
                    VideoView videoView = (VideoView) nvhm.resolveView(reactTag);
                    VrVideoManager videoManager = (VrVideoManager) nvhm.resolveViewManager(reactTag);
                    WritableMap map = Arguments.createMap();
                    map.putDouble("duration", videoManager.getDuration(videoView));
                    promise.resolve(map);
                }
            });
        } catch (IllegalViewOperationException e) {
            promise.reject(E_MODULE_ERROR, e);
        }
    }
}
