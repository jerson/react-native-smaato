package me.jerson.mobile.ads.airpush;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.smaato.soma.AdDimension;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.AdSettings;
import com.smaato.soma.BannerView;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.bannerutilities.constant.BannerStatus;
import com.smaato.soma.debug.Debugger;
import com.smaato.soma.exception.AdReceiveFailed;

import java.util.Map;

public class RNSmaatoBannerViewManager extends SimpleViewManager<ReactViewGroup> {

    public static final String TAG = "RNSmaatoBannerView";

    public static final String PROP_AD_DIMENSION = "adDimension";
    public static final String PROP_PUBLISHER_ID = "publisherID";
    public static final String PROP_AD_SPACE_ID = "adSpaceID";
    public static final String PROP_TEST_DEVICE_ID = "testDeviceID";

    private String testDeviceID = null;
    private AdSettings adSettings = null;
    private ThemedReactContext mThemedReactContext;
    private RCTEventEmitter mEventEmitter;

    @Override
    public String getName() {
        return "RNSmaatoBannerView";
    }

    @Override
    protected ReactViewGroup createViewInstance(ThemedReactContext themedReactContext) {

        Debugger.DEBUG_LEVEL = 3;
        mThemedReactContext = themedReactContext;
        mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
        ReactViewGroup view = new ReactViewGroup(themedReactContext);
        adSettings = new AdSettings();
        adSettings.setPublisherId(-1);
        adSettings.setAdspaceId(-1);
        // adSettings.setDimensionStrict(true);
        adSettings.setAdDimension(AdDimension.NOT_SET);
        attachNewAdView(view);

        return view;
    }

    protected void attachNewAdView(final ReactViewGroup view) {
        Log.d(TAG, "attachNewAdView");

        final BannerView adView = new BannerView(mThemedReactContext);

        // destroy old AdView if present
        BannerView oldAdView = (BannerView) view.getChildAt(0);
        view.removeAllViews();
        if (oldAdView != null) {
            oldAdView.destroy();
        }

        view.addView(adView);
        // updateSize(view);
        // updateSize(adView);

    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        for (Events event : Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @ReactProp(name = PROP_AD_SPACE_ID)
    public void setAdSpaceID(final ReactViewGroup view, String adSpaceID) {
        Log.d(TAG, "setAdSpaceID");

        attachNewAdView(view);
        BannerView newAdView = (BannerView) view.getChildAt(0);
        if (newAdView != null) {
            if (adSpaceID != null && !adSpaceID.equals("")) {
                adSettings.setAdspaceId(Long.parseLong(adSpaceID));
            }
            loadAd(newAdView);
        }
    }

    @ReactProp(name = PROP_PUBLISHER_ID)
    public void setPublisherID(final ReactViewGroup view, String publisherID) {
        Log.d(TAG, "setPublisherID");

        attachNewAdView(view);
        BannerView newAdView = (BannerView) view.getChildAt(0);
        if (newAdView != null) {
            if (publisherID != null && !publisherID.equals("")) {
                adSettings.setPublisherId(Long.parseLong(publisherID));
            }
            loadAd(newAdView);
        }
    }

    @ReactProp(name = PROP_AD_DIMENSION)
    public void setAdDimension(final ReactViewGroup view, String adDimension) {
        Log.d(TAG, "setAdDimension");

        attachNewAdView(view);
        BannerView newAdView = (BannerView) view.getChildAt(0);
        if (newAdView != null) {
            if (adDimension != null && !adDimension.equals("")) {
                adSettings.setAdDimension(getAdDimensionForString(adDimension));
            }
            loadAd(newAdView);
        }
    }

    @ReactProp(name = PROP_TEST_DEVICE_ID)
    public void setTestDeviceID(final ReactViewGroup view, final String testDeviceID) {
        Log.d(TAG, "setTestDeviceID");

        this.testDeviceID = testDeviceID;
    }

    private int convertDpToPx(int dp) {
        return Math.round(
                dp * (mThemedReactContext.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    private void updateSize(View adView) {
        Resources r = mThemedReactContext.getResources();
        int width = 0;
        int height = 0;

        int left = adView.getLeft();
        int top = adView.getTop();

        switch (adSettings.getAdDimension()) {
        case MEDIUMRECTANGLE:
            width = convertDpToPx(300);
            height = convertDpToPx(250);

            break;
        case SKYSCRAPER:
            width = convertDpToPx(120);
            height = convertDpToPx(600);

            break;
        case DEFAULT:
        default:
            width = convertDpToPx(320);
            height = convertDpToPx(50);
        }

        adView.measure(width, height);
        adView.layout(left, top, left + width, top + height);
        adView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
    }

    private void loadAd(final BannerView adView) {
        Log.d(TAG, "loadAd");

        if (adSettings != null) {
            adView.setAdSettings(adSettings);

            switch (adSettings.getAdDimension()) {
            case MEDIUMRECTANGLE:
                adView.getAdSettings().setBannerWidth(300);
                adView.getAdSettings().setBannerHeight(250);
                break;
            case SKYSCRAPER:
                adView.getAdSettings().setBannerWidth(120);
                adView.getAdSettings().setBannerHeight(600);
                break;
            case DEFAULT:
            default:
                adView.getAdSettings().setBannerWidth(320);
                adView.getAdSettings().setBannerHeight(50);
            }

            if (adSettings.getAdspaceId() != -1 && adSettings.getPublisherId() != -1
                    && adSettings.getAdDimension() != AdDimension.NOT_SET) {
                Log.d(TAG, "loadAd OK");

                adView.addAdListener(new AdListenerInterface() {
                    @Override
                    public void onReceiveAd(AdDownloaderInterface adDownloaderInterface,
                            ReceivedBannerInterface receivedBannerInterface) throws AdReceiveFailed {
                        if (receivedBannerInterface.getStatus() == BannerStatus.ERROR) {
                            Log.d(TAG, receivedBannerInterface.getErrorCode() + ":"
                                    + receivedBannerInterface.getErrorMessage());
                        } else {
                            Log.d(TAG, "OK SUPUESTAMENTE");
                        }
                        updateSize(adView);
                    }
                });
                adView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6,
                            int i7) {
                        Log.d(TAG, "onLayoutChange");

                    }
                });
                adView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View view) {
                        Log.d(TAG, "onViewAttachedToWindow");

                        // updateSize(view);

                    }

                    @Override
                    public void onViewDetachedFromWindow(View view) {
                        Log.d(TAG, "onViewDetachedFromWindow");

                    }
                });
                adView.asyncLoadNewBanner();
                // updateSize(adView);
            }
        }
    }

    private AdDimension getAdDimensionForString(String adSize) {
        switch (adSize) {
        case "mediumrectangle":
            return AdDimension.MEDIUMRECTANGLE;
        case "skyscraper":
            return AdDimension.SKYSCRAPER;
        default:
            return AdDimension.DEFAULT;
        }
    }

    public enum Events {
        EVENT_SIZE_CHANGE("onSizeChange"), EVENT_RECEIVE_AD("onAdViewDidReceiveAd"),
        EVENT_ERROR("onDidFailToReceiveAdWithError"), EVENT_WILL_PRESENT("onAdViewWillPresentScreen"),
        EVENT_WILL_DISMISS("onAdViewWillDismissScreen"), EVENT_DID_DISMISS("onAdViewDidDismissScreen"),
        EVENT_WILL_LEAVE_APP("onAdViewWillLeaveApplication");

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }
}
