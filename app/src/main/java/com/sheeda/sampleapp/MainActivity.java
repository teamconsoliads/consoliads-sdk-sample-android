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

import com.consoliads.sdk.ConsoliadsSdk;
import com.consoliads.sdk.PlaceholderName;
import com.consoliads.sdk.SDKPlatform;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerAdListener;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerSize;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerView;
import com.consoliads.sdk.delegates.ConsoliadsSdkInAppPurchaseListener;
import com.consoliads.sdk.delegates.ConsoliadsSdkInitializationListener;
import com.consoliads.sdk.delegates.ConsoliadsSdkInterstitialAdListener;
import com.consoliads.sdk.delegates.ConsoliadsSdkRewardedAdListener;
import com.consoliads.sdk.iconads.ConsoliadsSdkIconAdListener;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.iconads.ConsoliadsSdkIconSize;
import com.consoliads.sdk.inapp.CAInAppDetails;
import com.consoliads.sdk.inapp.CAInAppError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConsoliadsSdkInterstitialAdListener , ConsoliadsSdkRewardedAdListener, ConsoliadsSdkInAppPurchaseListener {

    String TAG = "ConsoliAdsSdkListeners";

    PlaceholderName placeholderName;
    List<String> placeholderNames = new ArrayList<String>();
    List<PlaceholderName> placeholderValues = new ArrayList<>();

    CheckBox consent, devMode;
    Boolean userConsent = true;
    Boolean isDevMode = false;
    Boolean initCompleted = false;

    String userSignature = "567f2188d5d283f6fa4fccec99dc6677";
    Activity activity;

    IconAdView iconAdView;
    ConsoliadsSdkBannerView mediatedBannerView;
    ConsoliadsSdkBannerView mediatedBannerView_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

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
                if (!initCompleted) {
                    Log.e("dev_mode", isDevMode + "");
                    ConsoliadsSdk.getInstance().setSdkInterstitialAdListener(this);
                    ConsoliadsSdk.getInstance().setSdkRewardedAdListener(this);
                    ConsoliadsSdk.getInstance().setSdkInAppPurchaseListener(this);
                    ConsoliadsSdk.getInstance().init(activity, userSignature, userConsent, isDevMode, SDKPlatform.Google, new ConsoliadsSdkInitializationListener() {
                        @Override
                        public void onInitializationSuccess() {
                            initCompleted = true;
                        }

                        @Override
                        public void onInitializationError(String s) {
                            initCompleted = false;
                        }
                    });
                }
                break;
            }
            case R.id.btn_load_int: {
                ConsoliadsSdk.getInstance().loadInterstitial(placeholderName);
                break;
            }
            case R.id.btn_show_int: {
                ConsoliadsSdk.getInstance().showInterstitial(placeholderName , activity);
                break;
            }
            case R.id.btn_int_rew: {
                ConsoliadsSdk.getInstance().loadRewardedVideoAd(placeholderName);
                break;
            }
            case R.id.btn_show_rew: {
                ConsoliadsSdk.getInstance().showRewardedVideoAd(placeholderName , activity);
                break;
            }
            case R.id.btn_show_banner: {
                ConsoliadsSdk.getInstance().showBanner(placeholderName, activity, ConsoliadsSdkBannerSize.Banner, mediatedBannerView, new ConsoliadsSdkBannerAdListener() {
                    @Override
                    public void onBannerAdLoaded(PlaceholderName placeholderName) {
                        mediatedBannerView.setVisibility(View.VISIBLE);
                        Log.i(TAG, "onBannerAdLoaded for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdFailedToLoad(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onBannerAdFailedToLoad for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdRefreshed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onBannerAdRefreshed for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdClicked(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onBannerAdClicked for placeholderName : " + placeholderName.name() + " with product id : " + s);
                    }

                    @Override
                    public void onBannerAdClosed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onBannerAdClosed for placeholderName : " + placeholderName.name());
                    }
                });
                break;
            }
            case R.id.btn_hide_banner: {
                mediatedBannerView.destroyBanner();
                mediatedBannerView.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_show_banner2: {
                ConsoliadsSdk.getInstance().showBanner(placeholderName, activity, ConsoliadsSdkBannerSize.LargeBanner, mediatedBannerView_second, new ConsoliadsSdkBannerAdListener() {
                    @Override
                    public void onBannerAdLoaded(PlaceholderName placeholderName) {
                        mediatedBannerView_second.setVisibility(View.VISIBLE);
                        Log.i(TAG, "onBannerAdLoaded for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdFailedToLoad(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onBannerAdFailedToLoad for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdRefreshed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onBannerAdRefreshed for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onBannerAdClicked(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onBannerAdClicked for placeholderName : " + placeholderName.name() + " with product id : " + s);
                    }

                    @Override
                    public void onBannerAdClosed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onBannerAdClosed for placeholderName : " + placeholderName.name());
                    }
                });
                break;
            }
            case R.id.btn_hide_banner2: {
                mediatedBannerView_second.destroyBanner();
                mediatedBannerView_second.setVisibility(View.GONE);
                break;
            }
            case R.id.btn_show_icon_ad: {
                ConsoliadsSdk.getInstance().showIconAd(placeholderName, activity, iconAdView, new ConsoliadsSdkIconAdListener() {
                    @Override
                    public void onIconAdShown(PlaceholderName placeholderName) {
                        Log.i(TAG, "onIconAdShown for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onIconAdFailedToShow(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onIconAdFailedToShow for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onIconAdClosed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onIconAdClosed for placeholderName : " + placeholderName.name());
                    }

                    @Override
                    public void onIconAdClicked(PlaceholderName placeholderName, String s) {
                        Log.i(TAG, "onIconAdClicked for placeholderName : " + placeholderName.name() + " with product id : " + s);
                    }

                    @Override
                    public void onIconAdRefreshed(PlaceholderName placeholderName) {
                        Log.i(TAG, "onIconAdRefreshed for placeholderName : " + placeholderName.name());
                    }
                } , ConsoliadsSdkIconSize.LARGEICON);
                break;
            }
            case R.id.btn_hide_icon_ad: {
                if (iconAdView != null) {
                    iconAdView.hideAd();
                    iconAdView.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.btn_list_view: {
                startActivity(new Intent(MainActivity.this, ConsoliAdsListActivity.class));
            }
        }
    }

    @Override
    public void onInAppPurchaseRestored(CAInAppDetails caInAppDetails) {
        Log.i(TAG, "onInAppPurchaseRestoredEvent : " + caInAppDetails.toJson());
    }

    @Override
    public void onInAppPurchaseSuccessed(CAInAppDetails caInAppDetails) {
        Log.i(TAG, "onInAppSuccessEvent : " + caInAppDetails.toJson());
    }

    @Override
    public void onInAppPurchaseFailed(CAInAppError caInAppError) {
        Log.i(TAG, "onInAppFailureEvent : " + caInAppError.toJson());
    }

    @Override
    public void onInterstitialAdLoaded(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAdLoaded for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdFailedToLoad(PlaceholderName placeholderName, String s) {
        Log.i(TAG, "onInterstitialAdFailedToLoad for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdClosed(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAdClosed for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdClicked(PlaceholderName placeholderName, String s) {
        Log.i(TAG, "onInterstitialAdClicked for placeholderName : " + placeholderName.name() + " with product id : " + s);
    }

    @Override
    public void onInterstitialAdShown(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAdShown for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onInterstitialAdFailedToShow(PlaceholderName placeholderName) {
        Log.i(TAG, "onInterstitialAdFailedToShow for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdLoaded(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdLoaded for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(PlaceholderName placeholderName, String s) {
        Log.i(TAG, "onRewardedVideoAdFailedToLoad for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdShown(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdShown for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdFailedToShow(PlaceholderName placeholderName, String s) {
        Log.i(TAG, "onRewardedVideoAdFailedToShow for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdCompleted(PlaceholderName placeholderName, int i) {
        Log.i(TAG, "onRewardedVideoAdCompleted for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdClosed(PlaceholderName placeholderName) {
        Log.i(TAG, "onRewardedVideoAdClosed for placeholderName : " + placeholderName.name());
    }

    @Override
    public void onRewardedVideoAdClicked(PlaceholderName placeholderName, String s) {
        Log.i(TAG, "onRewardedVideoAdClicked for placeholderName : " + placeholderName.name() + " with product id : " + s);
    }
}