package com.sheeda.sampleapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import com.consoliads.mediation.listeners.ConsoliAdsBannerListener;
import com.consoliads.mediation.listeners.ConsoliAdsIconListener;
import com.consoliads.mediation.listeners.ConsoliAdsInterstitialListener;
import com.consoliads.mediation.listeners.ConsoliAdsListener;
import com.consoliads.mediation.listeners.ConsoliAdsRewardedListener;
import com.consoliads.sdk.iconads.IconAdBase;
import com.consoliads.sdk.iconads.IconAdView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConsoliAdsBannerListener, ConsoliAdsInterstitialListener, ConsoliAdsIconListener, ConsoliAdsRewardedListener, ConsoliAdsListener {

    int currentScene = 0;
    List<String> scenes = new ArrayList<String>();
    CheckBox consent;
    Boolean userConsent = true;

    String TAG = "ConsoliAdsListners";

    //for consolaiads iconad
    IconAdView iconAdView;
    FrameLayout bannerFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSpinner();

        iconAdView = findViewById(R.id.consoli_icon_view);
        bannerFrame = findViewById(R.id.banner_frame);
        Button init = findViewById(R.id.btn_init);
        Button show_init = findViewById(R.id.btn_show_int);
        Button int_rew = findViewById(R.id.btn_int_rew);
        Button show_rew = findViewById(R.id.btn_show_rew);

        Button show_custom_banner = findViewById(R.id.btn_custom_show_banner);
        Button show_banner = findViewById(R.id.btn_show_banner);
        Button hide_banner = findViewById(R.id.btn_hide_banner);

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
        show_icon.setOnClickListener(this);
        hide_icon.setOnClickListener(this);
        btn_list_view.setOnClickListener(this);
        show_custom_banner.setOnClickListener(this);

        findViewById(R.id.btn_show_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext() , NativeAdActivity.class));
            }
        });
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
                ConsoliAds.Instance().setConsoliAdsBannerListener(this);
                ConsoliAds.Instance().setConsoliAdsIconListener(this);
                ConsoliAds.Instance().setConsoliAdsInterstitialListener(this);
                ConsoliAds.Instance().setConsoliAdsRewardedListener(this);
                ConsoliAds.Instance().setConsoliAdsInterstitialListener(this);

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
                ConsoliAds.Instance().ShowBanner(currentScene, MainActivity.this,bannerFrame);
                break;
            }
            case R.id.btn_custom_show_banner: {
                ConsoliAds.Instance().ShowBanner(currentScene, 320,50,MainActivity.this,bannerFrame);
                break;
            }
            case R.id.btn_hide_banner: {
                ConsoliAds.Instance().HideBanner();
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
                startActivity(new Intent(MainActivity.this, ConsoliAdsListActivity.class));
            }
        }
    }

    @Override
    public void onBannerAdShownEvent() {
        Log.i(TAG,"onBannerAdShown");
    }

    @Override
    public void onBannerAdFailToShowEvent() {
        Log.i(TAG,"onBannerAdFailToShow");
    }

    @Override
    public void onBannerAdClickEvent() {
        Log.i(TAG,"onBannerAdClick");
    }

    @Override
    public void onIconAdShownEvent() {
        Log.i(TAG,"onIconAdShown");
    }

    @Override
    public void onIconAdFailedToShowEvent() {
        Log.i(TAG,"onIconAdFailedToShow");
    }

    @Override
    public void onIconAdClosedEvent() {
        Log.i(TAG,"onIconAdClosed");
    }

    @Override
    public void onIconAdClickEvent() {
        Log.i(TAG,"onIconAdClick");
    }

    @Override
    public void onInterstitialAdShownEvent() {
        Log.i(TAG,"onInterstitialAdShown");
    }

    @Override
    public void onInterstitialAdClickedEvent() {
        Log.i(TAG,"onInterstitialAdClicked");
    }

    @Override
    public void onInterstitialAdClosedEvent() {
        Log.i(TAG,"onInterstitialAdClosed");
    }

    @Override
    public void onInterstitialAdFailedToShowEvent() {
        Log.i(TAG,"onInterstitialAdFailedToShow");
    }

    @Override
    public void onConsoliAdsInitializationSuccess() {
        Log.i(TAG,"onInitializationSuccess");
    }

    @Override
    public void onRewardedVideoAdLoadedEvent() {
        Log.i(TAG,"onRewardedVideoLoaded");
    }

    @Override
    public void onRewardedVideoAdShownEvent() {
        Log.i(TAG,"onRewardedVideoShown");
    }

    @Override
    public void onRewardedVideoAdCompletedEvent() {
        Log.i(TAG,"onRewardedAdCompleted");
    }

    @Override
    public void onRewardedVideoAdClickEvent() {
        Log.i(TAG,"onRewardedVideoClick");
    }

    @Override
    public void onRewardedVideoAdFailToLoadEvent() {
        Log.i(TAG,"onRewardedVideoFailToLoad");
    }

    @Override
    public void onRewardedVideoAdFailToShowEvent() {
        Log.i(TAG,"onRewardedVideoFailToShow");
    }

    @Override
    public void onRewardedVideoAdClosedEvent() {
        Log.i(TAG,"onRewardedAdClosed");
    }
}