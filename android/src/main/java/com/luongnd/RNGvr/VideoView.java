package com.luongnd.RNGvr;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.util.Log;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class VideoView extends RelativeLayout {
    private static final String TAG = VideoView.class.getSimpleName();

    private VrVideoView vrView;

    private boolean isPaused = false;

    @UiThread
    public VideoView(Context context, Activity activity) {
        super(context);
        vrView = new VrVideoView(activity);
        vrView.setEventListener(new ActivityEventListener(this));
        this.addView(vrView);
    }

    public void setDisplayMode(String mode) {
        switch(mode) {
            case "embedded":
                vrView.setDisplayMode(VrWidgetView.DisplayMode.EMBEDDED);
                break;
            case "fullscreen":
                vrView.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_MONO);
                break;
            case "cardboard":
                vrView.setDisplayMode(VrWidgetView.DisplayMode.FULLSCREEN_STEREO);
                break;
            default:
                vrView.setDisplayMode(VrWidgetView.DisplayMode.EMBEDDED);
                break;
        }
    }

    public void setVolume(float value) {
        vrView.setVolume(value);
    }

    public void setFullscreenButtonEnabled(Boolean enabled) {
        vrView.setFullscreenButtonEnabled(enabled);
    }

    public void setCardboardButtonEnabled(Boolean enabled) {
        vrView.setStereoModeButtonEnabled(enabled);
    }

    public void setTouchTrackingEnabled(Boolean enabled) {
        vrView.setTouchTrackingEnabled(enabled);
    }

    public void setTransitionViewEnabled(Boolean enabled) {
        vrView.setTransitionViewEnabled(!enabled);
    }

    public void setInfoButtonEnabled(Boolean enabled) {
        vrView.setInfoButtonEnabled(enabled);
    }

    public void setPaused(Boolean paused) {
        this.isPaused = paused;
        if (paused) {
            vrView.pauseVideo();
        } else {
            vrView.playVideo();
        }
    }

    public void setSrc(ReadableMap src) {

        String type = src.getString("type");
        String uri = src.getString("uri");
        String format = src.getString("format");

        VrVideoView.Options videoOptions = new VrVideoView.Options();

        switch(type) {
            case "mono":
                videoOptions.inputType = VrVideoView.Options.TYPE_MONO;
                break;
            case "stereo":
                videoOptions.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
                break;
            default:
                videoOptions.inputType = VrVideoView.Options.TYPE_MONO;
                break;
        }

        switch(format) {
            case "hls":
                videoOptions.inputFormat = VrVideoView.Options.FORMAT_HLS;
                break;
            case "mp4":
                videoOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
                break;
            case "mpeg":
                videoOptions.inputFormat = VrVideoView.Options.FORMAT_DASH;
                break;
            default:
                videoOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
                break;
        }

        Source source = new Source(uri, videoOptions);
        VideoLoaderTask videoLoaderTask = new VideoLoaderTask();
        videoLoaderTask.execute(source);
        if (isPaused) {
            vrView.pauseVideo();
        } else {
            vrView.playVideo();
        }
    }

    public double getDuration() {
        return (double)vrView.getDuration();
    }

    public void seekTo(double progress) {
        double position = progress * (double)vrView.getDuration();
        vrView.seekTo(Double.valueOf(position).longValue());
    }

    public void onContentLoaded() {
        WritableMap event = Arguments.createMap();
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onContentLoad", event);
    }

    public void onContentLoadedError(String errorMessage) {
        WritableMap event = Arguments.createMap();
        event.putString("error", errorMessage);
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onContentLoad", event);
    }

    public void onTap() {
        WritableMap event = Arguments.createMap();
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onTap", event);
    }

    public void onUpdatePosition(double position, double duration) {
        WritableMap event = Arguments.createMap();
        event.putDouble("position", position);
        event.putDouble("duration", duration);
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onUpdatePosition", event);
    }

    public void onChangeDisplayMode(String mode) {
        WritableMap event = Arguments.createMap();
        event.putString("mode", mode);
        ReactContext reactContext = (ReactContext)getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onChangeDisplayMode", event);
    }


    private class ActivityEventListener extends VrVideoEventListener {
        private VideoView videoView;
        private int id;

        public ActivityEventListener(VideoView view) {
            this.videoView = view;
        }

        @Override
        public void onLoadSuccess() {
            Log.i(TAG, "Successfully loaded video ");
            videoView.onContentLoaded();
        }

        /**
         * Called by video widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            // An error here is normally due to being unable to decode the video format.
            Log.e(TAG, "Error loading video: " + errorMessage);
            videoView.onContentLoadedError(errorMessage);
        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {
            double position = ((double)vrView.getCurrentPosition() / vrView.getDuration());
            videoView.onUpdatePosition(position, (double)vrView.getDuration());
        }

        @Override
        public void onClick() {
            videoView.onTap();
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            String mode = "";
            switch (newDisplayMode) {
                case VrWidgetView.DisplayMode.EMBEDDED:
                    mode = "embedded";
                    break;
                case VrWidgetView.DisplayMode.FULLSCREEN_MONO:
                    mode = "fullscreen";
                    break;
                case VrWidgetView.DisplayMode.FULLSCREEN_STEREO:
                    mode = "cardboard";
                    break;
                default:
                    break;
            }
            videoView.onChangeDisplayMode(mode);
        }

    }

    class Source {
        public String uri;
        public VrVideoView.Options options;

        public Source(String uri, VrVideoView.Options videoOptions) {
            this.uri = uri;
            this.options = videoOptions;
        }
    }

    class VideoLoaderTask extends AsyncTask<Source, Void, Boolean> {
        @SuppressWarnings("WrongThread")
        protected Boolean doInBackground(Source... args) {
            try {
                Uri uri = Uri.parse(args[0].uri);
                vrView.loadVideo(uri, args[0].options);
            } catch (IOException e) {}

            return true;
        }
    }
}
