package com.sheeda.sampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.sdk.ConsoliadsSdk;
import com.consoliads.sdk.PlaceholderName;
import com.consoliads.sdk.PrivacyPolicy;
import com.consoliads.sdk.nativeads.ActionButton;
import com.consoliads.sdk.nativeads.ConsoliadsSdkNativeAd;
import com.consoliads.sdk.nativeads.ConsoliadsSdkNativeAdListener;

import java.util.ArrayList;
import java.util.List;

public class NativeAdActivity extends Activity implements View.OnClickListener {

    ConsoliadsSdkNativeAd consoliadsSdkNativeAd;
    LinearLayout nativeAdContainer;
    PrivacyPolicy privacyPolicy;
    TextView adTitle;
    TextView adSubTitle;
    TextView adDescription;
    ImageView adImage;
    ActionButton actionButton;

    EditText etPlaceholder;

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

        nativeAdContainer = (LinearLayout) findViewById(R.id.native_container);
        adTitle = (TextView) findViewById(R.id.tv_ad_title);
        adSubTitle = (TextView) findViewById(R.id.tv_ad_sub_title);
        adDescription = (TextView) findViewById(R.id.tv_ad_description);
        adImage = (ImageView) findViewById(R.id.iv_ad_image);
        actionButton = (ActionButton) findViewById(R.id.native_action_button);
        privacyPolicy = (PrivacyPolicy) findViewById(R.id.native_privacy_policy);

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
                ConsoliadsSdkNativeAdListener consoliadsSdkNativeAdListener = new ConsoliadsSdkNativeAdListener() {
                    @Override
                    public void onAdLoaded(ConsoliadsSdkNativeAd nativeAd) {
                        if (nativeAd != null){
                            consoliadsSdkNativeAd = nativeAd;
                            nativeAdContainer.setVisibility(View.VISIBLE);
                            adTitle.setText(nativeAd.getAdTitle());
                            adSubTitle.setText(nativeAd.getAdSubTitle());
                            adDescription.setText(nativeAd.getAdDescription());
                            nativeAd.setAdPrivacyPolicy(privacyPolicy);
                            nativeAd.loadAdImage(adImage);
                            nativeAd.registerClickToAction(actionButton);
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(PlaceholderName placeholderName, String error) {

                    }

                    @Override
                    public void onAdClicked(String ProductId) {
                        Log.i("ConsoliAdsSdkListeners", "onNativeAdClicked " + " with product id : " + ProductId);
                    }

                    @Override
                    public void onLoggingImpression() {

                    }

                    @Override
                    public void onAdClosed() {

                    }
                };
                ConsoliadsSdk.getInstance().showNative(nativePlaceholderName , consoliadsSdkNativeAdListener);
                break;
            }
            case R.id.btn_destroy_ad:
            {
                if(consoliadsSdkNativeAd != null)
                {
                    consoliadsSdkNativeAd.hideNativeAd();
                }
                nativeAdContainer.setVisibility(View.GONE);
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
