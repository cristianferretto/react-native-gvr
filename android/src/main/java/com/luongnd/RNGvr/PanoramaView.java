package com.luongnd.RNGvr;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.annotation.UiThread;
import android.util.Log;
import android.util.Pair;
import android.widget.RelativeLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.google.vr.sdk.widgets.common.VrWidgetView;
import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.sdk.widgets.pano.VrPanoramaView.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.commons.io.IOUtils;

public class PanoramaView extends RelativeLayout {
    private static final String TAG = PanoramaView.class.getSimpleName();

    private VrPanoramaView panoWidgetView;
    private Map<URL, Bitmap> imageCache = new HashMap<>();
    private ImageLoaderTask imageLoaderTask;
    private Options panoOptions = new Options();

    private URL imageUrl;

    @UiThread
    public PanoramaView(Context context, Activity activity) {
        super(context);
        panoWidgetView = new VrPanoramaView(activity);
        panoWidgetView.setEventListener(new ActivityEventListener());
        this.addView(panoWidgetView);
    }

    public void onAfterUpdateTransaction() {
        if (imageLoaderTask != null) {
            imageLoaderTask.cancel(true);
        }
        imageLoaderTask = new ImageLoaderTask();
        imageLoaderTask.execute(Pair.create(imageUrl, panoOptions));
    }


    public void shutdown() {
        panoWidgetView.pauseRendering();
        panoWidgetView.shutdown();
    }

    public void setImageUrl(String value) {
        if (imageUrl != null && imageUrl.toString().equals(value)) { return; }

        try {
            imageUrl = new URL(value);
        } catch(MalformedURLException e) {}
    }

    public void setInputType(String type) {
        switch (type) {
            case "mono":
                panoOptions.inputType = Options.TYPE_MONO;
                break;
            case "stereo":
                panoOptions.inputType = Options.TYPE_STEREO_OVER_UNDER;
                break;
            default:
                panoOptions.inputType = Options.TYPE_MONO;
                break;
        }
    }

    public void setDisplayMode(String mode) {
        int displayMode;
        switch(mode) {
            case "embedded":
                displayMode = VrWidgetView.DisplayMode.EMBEDDED;
                break;
            case "fullscreen":
                displayMode = VrWidgetView.DisplayMode.FULLSCREEN_MONO;
                break;
            case "cardboard":
                displayMode = VrWidgetView.DisplayMode.FULLSCREEN_STEREO;
                break;
            default:
                displayMode = VrWidgetView.DisplayMode.EMBEDDED;
                break;
        }
        panoWidgetView.setDisplayMode(displayMode);

    }

    public void setFullscreenButtonEnabled(Boolean enabled) {
        panoWidgetView.setFullscreenButtonEnabled(enabled);
    }

    public void setCardboardButtonEnabled(Boolean enabled) {
        panoWidgetView.setStereoModeButtonEnabled(enabled);
    }

    public void setTouchTrackingEnabled(Boolean enabled) {
        panoWidgetView.setTouchTrackingEnabled(enabled);
    }

    public void setTransitionViewEnabled(Boolean enabled) {
        panoWidgetView.setTransitionViewEnabled(!enabled);
    }

    public void setInfoButtonEnabled(Boolean enabled) {
        panoWidgetView.setInfoButtonEnabled(enabled);
    }

    class ImageLoaderTask extends AsyncTask<Pair<URL, Options>, Void, Boolean> {
        protected Boolean doInBackground(Pair<URL, Options>... fileInformation) {
            final URL imageUrl = fileInformation[0].first;
            Options panoOptions = fileInformation[0].second;

            InputStream istr = null;
            Bitmap image;

            if (!imageCache.containsKey(imageUrl)) {
                try {
                    HttpURLConnection connection = (HttpURLConnection) fileInformation[0].first.openConnection();
                    connection.connect();

                    istr = connection.getInputStream();

                    imageCache.put(imageUrl, decodeSampledBitmap(istr));
                } catch (IOException e) {
                    Log.e(TAG, "Could not load file: " + e);
                    return false;
                } finally {
                    try {
                        istr.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close input stream: " + e);
                    }
                }
            }

            image = imageCache.get(imageUrl);
            panoWidgetView.loadImageFromBitmap(image, panoOptions);

            return true;
        }

        private Bitmap decodeSampledBitmap(InputStream inputStream) throws IOException {
            final byte[] bytes = getBytesFromInputStream(inputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }

        private byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            IOUtils.copy(inputStream, baos);

            return baos.toByteArray();
        }

        private int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

    private class ActivityEventListener extends VrPanoramaEventListener {

        @Override
        public void onLoadSuccess() {
            emitEvent("onContentLoad", null);
        }

        @Override
        public void onLoadError(String errorMessage) {
            WritableMap event = Arguments.createMap();
            event.putString("error", errorMessage);
            emitEvent("onContentLoad", event);
        }

        @Override
        public void onClick() {
            emitEvent("onTap", null);
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
            WritableMap event = Arguments.createMap();
            event.putString("mode", mode);
            emitEvent("onChangeDisplayMode", event);
        }
    }

    void emitEvent(String name, @Nullable WritableMap event) {
        if (event == null) {
            event = Arguments.createMap();
        }
        ((ReactContext)getContext())
                .getJSModule(RCTEventEmitter.class)
                .receiveEvent(getId(), name, event);
    }
}
