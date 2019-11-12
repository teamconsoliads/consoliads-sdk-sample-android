package com.sheeda.sampleapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.nativeads.CAAdChoicesView;
import com.consoliads.mediation.nativeads.CAAppIconView;
import com.consoliads.mediation.nativeads.CACallToActionView;
import com.consoliads.mediation.nativeads.CAMediaView;
import com.consoliads.mediation.nativeads.CANativeAdView;
import com.consoliads.mediation.nativeads.ConsoliAdsNativeListener;
import com.consoliads.mediation.nativeads.MediatedNativeAd;

public class NativeAdActivity extends Activity implements View.OnClickListener {

    TextView title , subtitle , body , sponsered;
    CANativeAdView adView;
    CAAdChoicesView adChoicesView;
    CAAppIconView appIconView;
    CAMediaView mediaView;
    CACallToActionView actionView;

    EditText sceneIndex;
    MediatedNativeAd mediatedNativeAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_ad);

        sceneIndex = findViewById(R.id.scene);

        title = findViewById(R.id.native_ad_title);
        subtitle = findViewById(R.id.native_ad_sub_title);
        body = findViewById(R.id.native_ad_body);
        sponsered = findViewById(R.id.native_ad_sponsored_label);

        adView = findViewById(R.id.native_ad_frame);
        adChoicesView = findViewById(R.id.ad_choices_container);
        appIconView = findViewById(R.id.native_ad_icon);
        mediaView = findViewById(R.id.native_ad_media);
        actionView = findViewById(R.id.native_ad_call_to_action);

        findViewById(R.id.btn_show_ad).setOnClickListener(this);
        findViewById(R.id.btn_destroy_ad).setOnClickListener(this);
        findViewById(R.id.btn_go_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_show_ad:
            {
                String scene = sceneIndex.getText().toString();
                if(!scene.equals(""))
                {
                    Toast.makeText(getBaseContext() , "LOADING NATIVE AD" , Toast.LENGTH_SHORT).show();

                    ConsoliAds.Instance().loadNativeAd(Integer.parseInt(scene), NativeAdActivity.this, new ConsoliAdsNativeListener() {
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
                            mediatedNativeAd.registerViewForInteraction(NativeAdActivity.this , appIconView , mediaView , actionView , adView,adChoicesView);
                        }

                        @Override
                        public void onNativeAdLoadFailed() {
                            Log.i("ConsoliAdsListners","onNativeAdLoadFailed");
                        }
                    });
                }
                else
                {
                    Toast.makeText(getBaseContext() , "ENTER SCENE INDEX" , Toast.LENGTH_SHORT).show();
                }
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
