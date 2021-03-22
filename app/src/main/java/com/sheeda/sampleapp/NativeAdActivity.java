package com.sheeda.sampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.constants.PlaceholderName;
import com.consoliads.mediation.nativeads.CAAdChoicesView;
import com.consoliads.mediation.nativeads.CAAppIconView;
import com.consoliads.mediation.nativeads.CACallToActionView;
import com.consoliads.mediation.nativeads.CACustomView;
import com.consoliads.mediation.nativeads.CAMediaView;
import com.consoliads.mediation.nativeads.CANativeAdView;
import com.consoliads.mediation.nativeads.ConsoliAdsNativeListener;
import com.consoliads.mediation.nativeads.MediatedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class NativeAdActivity extends Activity implements View.OnClickListener {

    TextView title , subtitle , body , sponsered;
    CANativeAdView adView;
    CACustomView caCustomView;
    CAAdChoicesView adChoicesView;
    CAAppIconView appIconView;
    CAMediaView mediaView;
    CACallToActionView actionView;

    EditText etPlaceholder;
    MediatedNativeAd mediatedNativeAd;

    PlaceholderName nativePlaceholderName = PlaceholderName.Default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        etPlaceholder = findViewById(R.id.placeholder);
        etPlaceholder.setText(nativePlaceholderName.name());
        etPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceHolderSelector();
            }
        });

        title = findViewById(R.id.native_ad_title);
        subtitle = findViewById(R.id.native_ad_sub_title);
        body = findViewById(R.id.native_ad_body);
        sponsered = findViewById(R.id.native_ad_sponsored_label);

        adView = findViewById(R.id.native_ad_frame);
        caCustomView = findViewById(R.id.native_custom_view);
        adChoicesView = findViewById(R.id.ad_choices_container);
        appIconView = findViewById(R.id.native_ad_icon);
        mediaView = findViewById(R.id.native_ad_media);
        actionView = findViewById(R.id.native_ad_call_to_action);

        findViewById(R.id.btn_show_ad).setOnClickListener(this);
        findViewById(R.id.btn_destroy_ad).setOnClickListener(this);
        findViewById(R.id.btn_go_back).setOnClickListener(this);

        showPlaceHolderSelector();
    }

    private void showPlaceHolderSelector(){

        List<String> placeholderNames = new ArrayList<String>();
        final List<PlaceholderName> placeholderValues = new ArrayList<>();

        for (PlaceholderName nativePlaceholderName : PlaceholderName.values()){
            placeholderNames.add(nativePlaceholderName.name());
            placeholderValues.add(nativePlaceholderName);
        }

        CharSequence items[] = placeholderNames.toArray(new CharSequence[placeholderNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(NativeAdActivity.this);
        builder.setTitle("Select Placeholder");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("value is", "" + which);
                nativePlaceholderName = PlaceholderName.fromInteger(placeholderValues.get(which).getValue());
                etPlaceholder.setText("Selected Placeholder : " + nativePlaceholderName.name());
            }
        });
        builder.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_show_ad:
            {
                Toast.makeText(getBaseContext() , "LOADING NATIVE AD" , Toast.LENGTH_SHORT).show();
                ConsoliAds.Instance().loadNativeAd(nativePlaceholderName, NativeAdActivity.this, new ConsoliAdsNativeListener() {
                    @Override
                    public void onNativeAdLoaded(MediatedNativeAd ad) {
                        Log.i("ConsoliAdsListners","onNativeAdLoaded");
                        mediatedNativeAd = ad;
                        adView.setVisibility(View.VISIBLE);

                        actionView.setTextColor("#ffffff");
                        actionView.setTextSize_UNIT_SP(12);

                        mediatedNativeAd.setSponsered(sponsered);
                        mediatedNativeAd.setAdTitle(title);
                        mediatedNativeAd.setAdSubTitle(subtitle);
                        mediatedNativeAd.setAdBody(body);
                        mediatedNativeAd.registerViewForInteraction(NativeAdActivity.this , appIconView , mediaView , actionView , adView,adChoicesView,caCustomView);
                    }

                    @Override
                    public void onNativeAdLoadFailed() {
                        Log.i("ConsoliAdsListners","onNativeAdLoadFailed");
                    }
                });
                break;
            }
            case R.id.btn_destroy_ad:
            {
                if(mediatedNativeAd != null)
                {
                    mediatedNativeAd.destroy();
                    adView.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.btn_go_back:
            {
                finish();
                break;
            }
        }
    }

}
