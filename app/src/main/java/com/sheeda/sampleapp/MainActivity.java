package com.sheeda.sampleapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.ConsoliAdsListener;
import com.consoliads.mediation.constants.AdNetworkName;
import com.consoliads.sdk.iconads.IconAdBase;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.nativeads.ActionButton;
import com.consoliads.sdk.nativeads.AdChoices;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    int currentScene = 0;
    List<String> scenes = new ArrayList<String>();
    CheckBox consent;
    Boolean userConsent = true;

    //for consoliads native ad
    LinearLayout adContainer;
    AdChoices adChoices;
    TextView adTitle;
    TextView adSubTitle;
    TextView adDescription;
    ImageView adImage;
    ActionButton actionButton;

    //for facebook native ad
    LinearLayout adView;
    LinearLayout adChoicesContainer;
    AdIconView nativeAdIcon;
    TextView nativeAdTitle;
    MediaView nativeAdMedia;
    TextView nativeAdSocialContext;
    TextView nativeAdBody;
    TextView sponsoredLabel;
    Button nativeAdCallToAction;

    //for consolaiads iconad
    IconAdView iconAdView;

    //for admob native ad
    FrameLayout nativeFrameLayout;

    ListenersConsoliAds listenersConsoliAds;

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.isConsoliadsInitialized) {
            ConsoliAds.Instance().setConsoliAdsListener(listenersConsoliAds);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiConsoliNativeAssets();

        initFacebookAssets();

        setSpinner();

        listenersConsoliAds = new ListenersConsoliAds();

        iconAdView = findViewById(R.id.consoli_icon_view);

        nativeFrameLayout = findViewById(R.id.native_frame);

        Button init = findViewById(R.id.btn_init);
        Button show_init = findViewById(R.id.btn_show_int);
        Button int_rew = findViewById(R.id.btn_int_rew);
        Button show_rew = findViewById(R.id.btn_show_rew);

        Button show_banner = findViewById(R.id.btn_show_banner);
        Button hide_banner = findViewById(R.id.btn_hide_banner);

        Button show_native_consoli = findViewById(R.id.btn_show_native_consoli);
        Button show_admob_native = findViewById(R.id.btn_show_native_admob);
        Button show_facebook_native = findViewById(R.id.btn_show_native_facebook);
        Button hide_native = findViewById(R.id.btn_hide_native);

        Button show_icon = findViewById(R.id.btn_show_icon_ad);
        Button hide_icon = findViewById(R.id.btn_hide_icon_ad);

        Button btn_list_view = findViewById(R.id.btn_list_view);

        consent = findViewById(R.id.ch_consent);
        consent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    consent.setText("True");
                    userConsent = true;
                } else {
                    consent.setText("False");
                    userConsent = false;
                }
            }
        });

        init.setOnClickListener(this);
        show_init.setOnClickListener(this);
        int_rew.setOnClickListener(this);
        show_rew.setOnClickListener(this);
        show_banner.setOnClickListener(this);
        hide_banner.setOnClickListener(this);
        show_native_consoli.setOnClickListener(this);
        show_admob_native.setOnClickListener(this);
        show_facebook_native.setOnClickListener(this);
        hide_native.setOnClickListener(this);
        show_icon.setOnClickListener(this);
        hide_icon.setOnClickListener(this);
        btn_list_view.setOnClickListener(this);
    }

    private void intiConsoliNativeAssets() {
        adContainer = (LinearLayout) findViewById(R.id.native_container);
        adTitle = (TextView) findViewById(R.id.tv_ad_title);
        adSubTitle = (TextView) findViewById(R.id.tv_ad_sub_title);
        adDescription = (TextView) findViewById(R.id.tv_ad_description);
        adImage = (ImageView) findViewById(R.id.iv_ad_image);
        adChoices = (AdChoices) findViewById(R.id.native_adchoices_main);
        actionButton = (ActionButton) findViewById(R.id.native_action_button);
    }

    private void initFacebookAssets() {
        adView = findViewById(R.id.ad_unit);
        ;
        adChoicesContainer = findViewById(R.id.ad_choices_container);
        nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        nativeAdBody = adView.findViewById(R.id.native_ad_body);
        sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        currentScene = position;

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void setSpinner() {
        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements

        for (int i = 0; i < 10; i++) {
            scenes.add("Scene Index : " + i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, scenes);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_init: {
                ConsoliAds.Instance().setConsoliAdsListener(listenersConsoliAds);
                ConsoliAds.Instance().productName = Config.productName;
                ConsoliAds.Instance().bundleIdentifier = Config.bundleIdentifier;
                ConsoliAds.Instance().initialize(userConsent, MainActivity.this);
                break;
            }
            case R.id.btn_show_int: {
                ConsoliAds.Instance().ShowInterstitial(currentScene, MainActivity.this);
                break;
            }
            case R.id.btn_int_rew: {
                ConsoliAds.Instance().LoadRewarded(currentScene);
                break;
            }
            case R.id.btn_show_rew: {
                ConsoliAds.Instance().ShowRewardedVideo(currentScene, MainActivity.this);
                break;
            }
            case R.id.btn_show_banner: {
                ConsoliAds.Instance().ShowBanner(currentScene, MainActivity.this);
                break;
            }
            case R.id.btn_hide_banner: {
                ConsoliAds.Instance().HideBanner();
                break;
            }
            case R.id.btn_show_native_facebook: {
                ConsoliAds.Instance().ConfigureFacebookNativeAd(currentScene, adView, adChoicesContainer, nativeAdIcon, nativeAdTitle, nativeAdMedia, nativeAdSocialContext, nativeAdBody, sponsoredLabel, nativeAdCallToAction);
                ConsoliAds.Instance().ShowNativeAd(currentScene, MainActivity.this);

                break;
            }
            case R.id.btn_show_native_admob: {
                ConsoliAds.Instance().ConfigureAdmobNativeAd(currentScene, nativeFrameLayout);
                ConsoliAds.Instance().ShowNativeAd(currentScene, MainActivity.this);
                break;
            }
            case R.id.btn_hide_native: {
                ConsoliAds.Instance().onDestroyForNativeAd(currentScene);
                break;
            }
            case R.id.btn_show_native_consoli: {
                ConsoliAds.Instance().ConfigureConsoliadsNativeAd(currentScene, adContainer, adChoices, adTitle, adSubTitle, adDescription, adImage, actionButton);
                ConsoliAds.Instance().ShowNativeAd(currentScene, MainActivity.this);
                break;
            }
            case R.id.btn_show_icon_ad: {
                IconAdBase iconAdBase = (IconAdBase) ConsoliAds.Instance().getIconAdView(currentScene, MainActivity.this);
                if (iconAdBase != null) {
                    iconAdView.setIconAd(iconAdBase);
                }
                break;
            }
            case R.id.btn_hide_icon_ad: {
                if (iconAdView != null) {
                    iconAdView.hideAd();
                }
                break;
            }
            case R.id.btn_list_view: {
                if (Constants.isConsoliadsInitialized) {
                    startActivity(new Intent(MainActivity.this, ConsoliAdsListActivity.class));
                } else {
                    Toast.makeText(getBaseContext(), "Please Intilize consoliads First", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class ListenersConsoliAds extends ConsoliAdsListener {

        @Override
        public void onInterstitialAdShownEvent() {

        }

        @Override
        public void onInterstitialAdClickedEvent() {

        }

        @Override
        public void onVideoAdShownEvent() {

        }

        @Override
        public void onVideoAdClickedEvent() {

        }

        @Override
        public void onRewardedVideoAdShownEvent() {

        }

        @Override
        public void onRewardedVideoAdCompletedEvent() {

        }

        @Override
        public void onRewardedVideoAdClickEvent() {

        }

        @Override
        public void onPopupAdShownEvent() {

        }

        @Override
        public void onNativeAdLoadedEvent(AdNetworkName adNetworkName) {

        }

        @Override
        public void onNativeAdLoadedEvent(AdNetworkName adNetworkName, int i) {

        }

        @Override
        public void onIconAdShownEvent() {

        }

        @Override
        public void onIconAdFailedToShownEvent() {

        }

        @Override
        public void onIconAdClosedEvent() {

        }

        @Override
        public void onIconAdClickEvent() {

        }

        @Override
        public void onNativeAdFailedToLoadEvent(AdNetworkName adNetworkName) {

        }

        @Override
        public void onNativeAdFailedToLoadEvent(AdNetworkName adNetworkName, int i) {

        }

        @Override
        public void onConsoliAdsInitializationSuccess() {
            Constants.isConsoliadsInitialized = true;
        }

    }
}