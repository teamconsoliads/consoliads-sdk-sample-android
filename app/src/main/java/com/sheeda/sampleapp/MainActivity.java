package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.bannerads.CAMediatedBannerView;
import com.consoliads.mediation.constants.IconSize;
import com.consoliads.mediation.constants.PlaceholderName;
import com.consoliads.mediation.listeners.ConsoliAdsBannerListener;
import com.consoliads.mediation.listeners.ConsoliAdsIconListener;
import com.consoliads.mediation.listeners.ConsoliAdsInAppListener;
import com.consoliads.mediation.listeners.ConsoliAdsInterstitialListener;
import com.consoliads.mediation.listeners.ConsoliAdsListener;
import com.consoliads.mediation.listeners.ConsoliAdsRewardedListener;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.inapp.CAInAppDetails;
import com.consoliads.sdk.inapp.CAInAppError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConsoliAdsBannerListener, ConsoliAdsInterstitialListener, ConsoliAdsRewardedListener, ConsoliAdsListener, ConsoliAdsInAppListener {

    PlaceholderName placeholderName;
    List<String> placeholderNames = new ArrayList<String>();
    List<PlaceholderName> placeholderValues = new ArrayList<>();
    CheckBox consent, devMode;
    Boolean userConsent = true;
    Boolean isDevMode = false;

    String TAG = "ConsoliAdsListners";

    //for consolaiads iconad
    IconAdView iconAdView;
    CAMediatedBannerView mediatedBannerView;
    CAMediatedBannerView mediatedBannerView_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSpinner();

        mediatedBannerView_second = findViewById(R.id.consoli_banner_view_second);
        mediatedBannerView = findViewById(R.id.consoli_banner_view);
        iconAdView = findViewById(R.id.consoli_icon_view);

        Button init = findViewById(R.id.btn_init);
        Button show_init = findViewById(R.id.btn_show_int);
        Button int_rew = findViewById(R.id.btn_int_rew);
        Button show_rew = findViewById(R.id.btn_show_rew);

        Button show_banner = findViewById(R.id.btn_show_banner);
        Button hide_banner = findViewById(R.id.btn_hide_banner);

        Button show_banner_two = findViewById(R.id.btn_show_banner2);
        Button hide_banner_two = findViewById(R.id.btn_hide_banner2);

        findViewById(R.id.btn_load_int).setOnClickListener(this);

        show_banner_two.setOnClickListener(this);
        hide_banner_two.setOnClickListener(this);

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

        devMode = findViewById(R.id.ch_dev_mode);
        devMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    devMode.setText("True");
                    isDevMode = true;
                } else {
                    devMode.setText("False");
                    isDevMode = false;
                }
            }
        });

        init.setOnClickListener(this);
        findViewById(R.id.btn_load_sint).setOnClickListener(this);
        findViewById(R.id.btn_show_sint).setOnClickListener(this);
        show_init.setOnClickListener(this);
        int_rew.setOnClickListener(this);
        show_rew.setOnClickListener(this);
        show_banner.setOnClickListener(this);
        hide_banner.setOnClickListener(this);
        show_icon.setOnClickListener(this);
        hide_icon.setOnClickListener(this);
        btn_list_view.setOnClickListener(this);

        findViewById(R.id.btn_show_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), NativeAdActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        placeholderName = placeholderValues.get(position);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + placeholderName.name(), Toast.LENGTH_LONG).show();
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

        /*for (int i = 0; i < 10; i++) {
            scenes.add("Scene Index : " + i);
        }*/

        for (PlaceholderName nativePlaceholderName : PlaceholderName.values()) {
            placeholderNames.add(nativePlaceholderName.name());
            placeholderValues.add(nativePlaceholderName);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, placeholderNames);

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
                ConsoliAds.Instance().setConsoliAdsInAppListener(this);
                ConsoliAds.Instance().setConsoliAdsInterstitialAndVideoListener(this);
                ConsoliAds.Instance().setConsoliAdsRewardedListener(this);

                Log.e("dev_mode", isDevMode + "");
                ConsoliAds.Instance().initialize(isDevMode, userConsent, MainActivity.this, Config.userSignature);
                break;
            }
            case R.id.btn_load_int: {
                ConsoliAds.Instance().LoadInterstitial(placeholderName);
                break;
            }
            case R.id.btn_show_int: {
                ConsoliAds.Instance().ShowInterstitial(placeholderName, MainActivity.this);
                break;
            }
            case R.id.btn_load_sint: {
                //ConsoliAds.Instance().LoadStaticInterstitial();
                break;
            }
            case R.id.btn_show_sint: {
                //ConsoliAds.Instance().ShowStaticInterstitial(placeholderName, MainActivity.this);
                break;
            }
            case R.id.btn_int_rew: {
                ConsoliAds.Instance().LoadRewarded();
                break;
            }
            case R.id.btn_show_rew: {
                ConsoliAds.Instance().ShowRewardedVideo(placeholderName, MainActivity.this);
                break;
            }
            case R.id.btn_show_banner: {
                mediatedBannerView.setBannerListener(this);
                ConsoliAds.Instance().ShowBanner(placeholderName, MainActivity.this, mediatedBannerView);
                break;
            }
            case R.id.btn_hide_banner: {
                mediatedBannerView.destroyBanner();
                break;
            }
            case R.id.btn_show_banner2: {
                mediatedBannerView_second.setBannerListener(this);
                ConsoliAds.Instance().ShowBanner(placeholderName, MainActivity.this, mediatedBannerView_second);
                break;
            }
            case R.id.btn_hide_banner2: {
                mediatedBannerView_second.destroyBanner();
                break;
            }
            case R.id.btn_show_icon_ad: {
                ConsoliAds.Instance().showIconAd(placeholderName, MainActivity.this, iconAdView, IconSize.SmallIcon, new ConsoliAdsIconListener() {
                    @Override
                    public void onIconAdShownEvent() {
                        Log.i(TAG, "onIconAdShownEvent");
                    }

                    @Override
                    public void onIconAdFailedToShownEvent() {
                        Log.i(TAG, "onIconAdFailedToShownEvent");
                    }

                    @Override
                    public void onIconAdRefreshEvent() {
                        Log.i(TAG, "onIconAdRefreshEvent");
                    }

                    @Override
                    public void onIconAdClosedEvent() {
                        Log.i(TAG, "onIconAdClosedEvent");
                    }

                    @Override
                    public void onIconAdClickEvent() {
                        Log.i(TAG, "onIconAdClickEvent");
                    }
                });
                break;
            }
            case R.id.btn_hide_icon_ad: {
                if (iconAdView != null) {
                    iconAdView.hideAd();
                }
                break;
            }
            case R.id.btn_list_view: {
                startActivity(new Intent(MainActivity.this, ConsoliAdsListActivity.class));
            }
        }
    }

    @Override
    public void onBannerAdShownEvent() {
        Log.i(TAG, "onBannerAdShown");
    }

    @Override
    public void onBannerAdRefreshEvent() {
        Log.i(TAG, "onBannerAdRefreshEvent");
    }

    @Override
    public void onBannerAdFailToShowEvent() {
        Log.i(TAG, "onBannerAdFailToShow");
    }

    @Override
    public void onBannerAdClickEvent() {
        Log.i(TAG, "onBannerAdClick");
    }

    @Override
    public void onConsoliAdsInitializationSuccess() {
        Log.i(TAG, "onInitializationSuccess");
    }

    @Override
    public void onRewardedVideoAdLoadedEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdLoadedEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdShownEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdShownEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdCompletedEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdCompletedEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdClickEvent() {
        Log.i(TAG, "onRewardedVideoClick");
    }

    @Override
    public void onRewardedVideoAdFailToLoadEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdFailToLoadEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdFailToShowEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdFailToShowEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdClosedEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdClosedEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInAppSuccessEvent(CAInAppDetails caInAppDetails) {
        Log.i(TAG, "onInAppSuccessEvent : " + caInAppDetails.toJson());
    }

    @Override
    public void onInAppFailureEvent(CAInAppError caInAppError) {
        Log.i(TAG, "onInAppFailureEvent : " + caInAppError.toJson());
    }

    @Override
    public void onInAppPurchaseRestoredEvent(CAInAppDetails caInAppDetails) {
        Log.i(TAG, "onInAppPurchaseRestoredEvent : " + caInAppDetails.toJson());
    }

    @Override
    public void onInterstitialAdLoadedEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAndVideoAdLoadedEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdShownEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAndVideoAdShownEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdClickedEvent() {
        Log.i(TAG, "onInterstitialAndVideoAdClickedEvent");
    }

    @Override
    public void onInterstitialAdClosedEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAndVideoAdClosedEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdFailToLoadEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAndVideoAdFailToLoadEvent for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdFailedToShowEvent(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAndVideoAdFailedToShowEvent for placeholderName : " + placeholderName.name());
    }
}