package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.ConsoliAdsListener;
import com.consoliads.mediation.constants.AdNetworkName;
import com.facebook.ads.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.sheeda.sampleapp.Constants.isConsoliadsInitialized;
import static com.sheeda.sampleapp.Constants.isListActivity;

public class ConsoliAdsListActivity extends Activity {

    private List<Object> recipeList;
    private ListViewAdapter listViewAdapter;

    private EditText listIndexForFacebookAd , listIndexForAdmobAd , listIndexForConsoliAd , listIndexForConsoliAdNative , sceneIndex;
    private TextView add_new_facebook_ad , add_new_Admob_ad , add_new_consoli_ad , add_new_consoli_native;

    private int showOnIndex;

    @Override
    protected void onResume() {
        super.onResume();
        isListActivity = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consoli_ads_list);

        ConsoliAds.Instance().setConsoliAdsListener(new ListenersConsoliAds());

        try {
            recipeList = getRecipeListFromJSON(loadJSONFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sceneIndex = findViewById(R.id.et_scene_index);
        RecyclerView rvRecipes = findViewById(R.id.list_recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rvRecipes.addItemDecoration(dividerItemDecoration);
        listViewAdapter = new ListViewAdapter(this, recipeList);
        rvRecipes.setAdapter(listViewAdapter);
        rvRecipes.setLayoutManager(layoutManager);

        listIndexForFacebookAd = findViewById(R.id.et_list_index_for_ad_facebook);
        add_new_facebook_ad = findViewById(R.id.btn_add_ad_facebook);
        add_new_facebook_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConsoliadsInitialized)
                {
                    if(!listIndexForFacebookAd.getText().toString().equals("") ) {
                        int listIndex = Integer.parseInt(listIndexForFacebookAd.getText().toString().trim());
                        if (listIndex < recipeList.size()) {
                            Toast.makeText(getBaseContext(), "Please Wait Loading Ad", Toast.LENGTH_SHORT).show();
                            hideKeyboard(ConsoliAdsListActivity.this);
                            listIndexForFacebookAd.setText("");
                            showOnIndex = listIndex;
                            //sceneIndex is a facebook ad position on consoliads Portal
                            ConsoliAds.Instance().addNativeAd(Integer.parseInt(sceneIndex.getText().toString()));
                        } else {
                            Toast.makeText(getBaseContext(), "Please enter index less than list size", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Please enter any index to add new facebook ad", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext() , "Still Consoliads Not Initialized" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        listIndexForAdmobAd = findViewById(R.id.et_list_index_for_ad_admob);
        add_new_Admob_ad = findViewById(R.id.btn_add_ad_admob);
        add_new_Admob_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConsoliadsInitialized)
                {
                    if(!listIndexForAdmobAd.getText().toString().equals("")) {
                        int listIndex = Integer.parseInt(listIndexForAdmobAd.getText().toString().trim());
                        if (listIndex < recipeList.size()) {
                            Toast.makeText(getBaseContext(), "Please Wait Loading Ad", Toast.LENGTH_SHORT).show();
                            hideKeyboard(ConsoliAdsListActivity.this);
                            listIndexForAdmobAd.setText("");
                            showOnIndex = listIndex;
                            //sceneIndex is a admob ad position on consoliads Portal
                            ConsoliAds.Instance().addNativeAd(Integer.parseInt(sceneIndex.getText().toString()));
                        } else {
                            Toast.makeText(getBaseContext(), "Please enter index less than list size", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Please enter any index to add new Admob ad", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext() , "Still Consoliads Not Initialized" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        listIndexForConsoliAd = findViewById(R.id.et_list_index_for_ad_consoli);
        add_new_consoli_ad = findViewById(R.id.btn_add_ad_consoli);
        add_new_consoli_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConsoliadsInitialized)
                {
                    if(!listIndexForConsoliAd.getText().toString().equals("")) {
                        int listIndex = Integer.parseInt(listIndexForConsoliAd.getText().toString().trim());
                        if (listIndex < recipeList.size()) {
                            Toast.makeText(getBaseContext(), "Please Wait Loading Ad", Toast.LENGTH_SHORT).show();
                            hideKeyboard(ConsoliAdsListActivity.this);
                            listIndexForConsoliAd.setText("");

                            Object iconAd = ConsoliAds.Instance().getIconAdView(Integer.parseInt(sceneIndex.getText().toString()) , ConsoliAdsListActivity.this);
                            if(iconAd != null)
                            {
                                Toast.makeText(getBaseContext() , "IconAd Displayed... " , Toast.LENGTH_SHORT).show();
                                recipeList.add(listIndex, iconAd);
                                listViewAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                Toast.makeText(getBaseContext() , "IconAd Failed To Load... " , Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getBaseContext(), "Please enter index less than list size", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Please enter any index to add new Admob ad", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext() , "Still Consoliads Not Initialized" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        listIndexForConsoliAdNative = findViewById(R.id.et_list_index_for_ad_consolaids_native);
        add_new_consoli_native = findViewById(R.id.btn_add_ad_consoliads_native);
        add_new_consoli_native.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConsoliadsInitialized)
                {
                    if(!listIndexForConsoliAdNative.getText().toString().equals("")) {
                        int listIndex = Integer.parseInt(listIndexForConsoliAdNative.getText().toString().trim());
                        if (listIndex < recipeList.size()) {
                            Toast.makeText(getBaseContext(), "Please Wait Loading Ad", Toast.LENGTH_SHORT).show();
                            hideKeyboard(ConsoliAdsListActivity.this);
                            listIndexForAdmobAd.setText("");
                            showOnIndex = listIndex;
                            //sceneIndex is a consoliads ad position on consoliads Portal
                            ConsoliAds.Instance().addNativeAd(Integer.parseInt(sceneIndex.getText().toString()));

                        } else {
                            Toast.makeText(getBaseContext(), "Please enter index less than list size", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Please enter any index to add new Consoliads Native ad", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getBaseContext() , "Still Consoliads Not Initialized" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("recipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public List<Object> getRecipeListFromJSON(String json) throws JSONException {
        JSONArray recipes = new JSONArray(json);
        List<Object> recipeList = new ArrayList<>();
        for (int i = 0; i < recipes.length(); i++) {
            JSONObject recipeObj = (JSONObject) recipes.get(i);
            ListModel listModel = new ListModel();
            listModel.setImagePath(recipeObj.getString("image_path"));
            listModel.setRecipeName(recipeObj.getString("name"));
            listModel.setDescription(recipeObj.getString("description"));

            JSONArray jsonCategories = recipeObj.getJSONArray("categories");
            ArrayList<String> categories = new ArrayList<>();
            for (int j = 0; j < jsonCategories.length(); j++) {
                categories.add((String) jsonCategories.get(j));
            }
            listModel.setCategories(categories);
            recipeList.add(listModel);
        }
        return recipeList;
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
            if (isListActivity) {
                Toast.makeText(getBaseContext(), "onNativeAdLoadedEvent " + adNetworkName.name() + " " + i, Toast.LENGTH_SHORT).show();
                if (adNetworkName == AdNetworkName.FACEBOOKNATIVE) {
                    NativeAd nativeAd = (NativeAd) ConsoliAds.Instance().getNativeAdAtIndex(Integer.parseInt(sceneIndex.getText().toString()), i);
                    if (nativeAd != null) {
                        recipeList.add(showOnIndex, nativeAd);
                        listViewAdapter.notifyDataSetChanged();
                    }
                } else if (adNetworkName == AdNetworkName.ADMOBNATIVEAD) {
                    UnifiedNativeAd unifiedNativeAd = (UnifiedNativeAd) ConsoliAds.Instance().getNativeAdAtIndex(Integer.parseInt(sceneIndex.getText().toString()), i);
                    if (unifiedNativeAd != null) {
                        recipeList.add(showOnIndex, unifiedNativeAd);
                        listViewAdapter.notifyDataSetChanged();
                    }
                } else if (adNetworkName == AdNetworkName.CONSOLIADSNATIVE) {
                    com.consoliads.sdk.nativeads.NativeAd nativeAd = (com.consoliads.sdk.nativeads.NativeAd) ConsoliAds.Instance().getNativeAdAtIndex(Integer.parseInt(sceneIndex.getText().toString()), i);
                    if (nativeAd != null) {
                        recipeList.add(showOnIndex, nativeAd);
                        listViewAdapter.notifyDataSetChanged();
                    }
                }
            }
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
            Toast.makeText(getBaseContext(), "onNativeAdFailedToLoadEvent " + adNetworkName.name() + " " + i, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConsoliAdsInitializationSuccess() {
            isConsoliadsInitialized = true;
            Toast.makeText(getBaseContext(), "onConsoliAdsInitializationSuccess", Toast.LENGTH_SHORT).show();
        }

    }
    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
