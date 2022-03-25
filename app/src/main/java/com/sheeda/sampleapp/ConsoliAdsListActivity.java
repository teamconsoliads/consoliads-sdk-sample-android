package com.sheeda.sampleapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.consoliads.sdk.ConsoliadsSdk;
import com.consoliads.sdk.PlaceholderName;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerAdListener;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerSize;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerView;
import com.consoliads.sdk.iconads.ConsoliadsSdkIconAdListener;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.iconads.ConsoliadsSdkIconSize;
import com.consoliads.sdk.nativeads.ConsoliadsSdkNativeAd;
import com.consoliads.sdk.nativeads.ConsoliadsSdkNativeAdListener;

public class ConsoliAdsListActivity extends Activity {

    private static final String TAG = "ConsoliAdsListners";
    private String listIndex ;
    private EditText etPlaceholder , et_list_index;
    private Spinner spinnerAdType;
    private AdType selectedAdType;

    private List<Object> recipeList;
    private ListViewAdapter listViewAdapter;

    private ProgressDialog proDialog;

    PlaceholderName nativePlaceholderName = PlaceholderName.Default;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consoli_ads_list);

        hideKeyboard();

        try {
            recipeList = getRecipeListFromJSON(loadJSONFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        etPlaceholder = findViewById(R.id.et_scene_index);
        et_list_index = findViewById(R.id.et_list_index);
        etPlaceholder.setText(nativePlaceholderName.name());
        etPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceHolderSelector();
            }
        });

        spinnerAdType  = findViewById(R.id.spinner_ad_type);
        spinnerAdType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAdType = AdType.fromInteger(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayList<String> adTypeList = new ArrayList<>();
        for (AdType adType : AdType.values()) {
            adTypeList.add(adType.name());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, adTypeList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdType.setAdapter(dataAdapter);

        findViewById(R.id.btn_load_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showPlaceHolderSelector();

                hideKeyboard();

                listIndex = et_list_index.getText().toString();

                et_list_index.setText("");

                if(listIndex.equals(""))
                {
                    Toast.makeText(getBaseContext() , "FILL ALL VALUES FIRST",Toast.LENGTH_SHORT).show();
                    return;
                }

                showProgress();

                if(selectedAdType == AdType.ICON)
                {
                    loadIconAd();
                }
                else if(selectedAdType == AdType.NATIVE)
                {
                    loadNativeAd();
                }
                else if(selectedAdType == AdType.BANNER)
                {
                    loadBannerAd();
                }
            }
        });

        RecyclerView rvRecipes = findViewById(R.id.list_recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rvRecipes.addItemDecoration(dividerItemDecoration);
        listViewAdapter = new ListViewAdapter(this, recipeList);
        rvRecipes.setAdapter(listViewAdapter);
        rvRecipes.setLayoutManager(layoutManager);
    }

    private void loadNativeAd() {

        if(!listIndex.equals(""))
        {
            ConsoliadsSdkNativeAdListener consoliadsSdkNativeAdListener = new ConsoliadsSdkNativeAdListener() {
                @Override
                public void onAdLoaded(ConsoliadsSdkNativeAd nativeAd) {
                    if (nativeAd != null){
                        Log.i("ConsoliAdsListners","onNativeAdLoaded");
                        recipeList.add(Integer.parseInt(listIndex), nativeAd);
                        listViewAdapter.notifyDataSetChanged();
                        hideProgess();
                    }
                }

                @Override
                public void onAdFailedToLoad(PlaceholderName placeholderName, String error) {
                    Log.i("ConsoliAdsListners","onNativeAdLoadFailed");
                    hideProgess();
                }

                @Override
                public void onAdClicked(String ProductId) {

                }

                @Override
                public void onLoggingImpression() {

                }

                @Override
                public void onAdClosed() {

                }
            };
            ConsoliadsSdk.getInstance().showNative(nativePlaceholderName , consoliadsSdkNativeAdListener);
        }
        else
        {
            Toast.makeText(getBaseContext() , "FILL ALL VALUES FIRST" , Toast.LENGTH_SHORT).show();
        }
    }

    private void loadIconAd()
    {
        hideProgess();
        final IconAdView iconAdView = new IconAdView(getApplicationContext());
        ConsoliadsSdk.getInstance().showIconAd(nativePlaceholderName, ConsoliAdsListActivity.this, iconAdView, new ConsoliadsSdkIconAdListener() {
            @Override
            public void onIconAdShown(PlaceholderName placeholderName) {
                Log.i(TAG,"onIconAdShownEvent");
                recipeList.add(Integer.parseInt(listIndex), iconAdView);
                listViewAdapter.notifyDataSetChanged();
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
                Log.i(TAG, "onIconAdClicked for placeholderName : " + placeholderName.name());
            }

            @Override
            public void onIconAdRefreshed(PlaceholderName placeholderName) {
                Log.i(TAG, "onIconAdRefreshed for placeholderName : " + placeholderName.name());
            }
        } , ConsoliadsSdkIconSize.MEDIUMICON);

    }

    public void loadBannerAd()
    {
        showProgress();
        LayoutInflater inflater = LayoutInflater.from(ConsoliAdsListActivity.this);
        final ConsoliadsSdkBannerView mediatedBannerView = (ConsoliadsSdkBannerView) inflater.inflate(R.layout.row_mediated_banner, null, false);
        ConsoliadsSdk.getInstance().showBanner(nativePlaceholderName, ConsoliAdsListActivity.this, ConsoliadsSdkBannerSize.Banner, mediatedBannerView, new ConsoliadsSdkBannerAdListener() {
            @Override
            public void onBannerAdLoaded(PlaceholderName placeholderName) {
                recipeList.add(Integer.parseInt(listIndex) , mediatedBannerView);
                listViewAdapter.notifyDataSetChanged();
                hideProgess();
                Log.i(TAG, "onBannerAdLoaded for placeholderName : " + placeholderName.name());
            }

            @Override
            public void onBannerAdFailedToLoad(PlaceholderName placeholderName, String s) {
                hideProgess();
                Log.i(TAG, "onBannerAdFailedToLoad for placeholderName : " + placeholderName.name());
            }

            @Override
            public void onBannerAdRefreshed(PlaceholderName placeholderName) {
                Log.i(TAG, "onBannerAdRefreshed for placeholderName : " + placeholderName.name());
            }

            @Override
            public void onBannerAdClicked(PlaceholderName placeholderName, String s) {
                Log.i(TAG, "onBannerAdClicked for placeholderName : " + placeholderName.name());
            }

            @Override
            public void onBannerAdClosed(PlaceholderName placeholderName) {
                Log.i(TAG, "onBannerAdClosed for placeholderName : " + placeholderName.name());
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

    private void showProgress()
    {
        if(proDialog != null)
        {
            proDialog.hide();
            proDialog = null;
        }
        proDialog =
                ProgressDialog.show(this, "Please wait", "Loading AdView ...");
    }

    private void hideProgess()
    {
        if(proDialog != null)
        {
            proDialog.hide();
            proDialog = null;
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private enum AdType{

        ICON(0),
        BANNER(1),
        NATIVE(2);

        int i;

        AdType(int i) {
            this.i = i;
        }

        public int getValue()
        {
            return this.i;
        }

        public static AdType fromInteger(int _id)
        {
            AdType[] As = AdType.values();
            for(int i = 0; i < As.length; i++)
            {
                if((As[i].getValue())==_id)
                    return As[i];
            }
            return AdType.ICON;
        }
    }

    private void showPlaceHolderSelector(){

        List<String> placeholderNames = new ArrayList<String>();
        final List<PlaceholderName> placeholderValues = new ArrayList<>();

        for (PlaceholderName nativePlaceholderName : PlaceholderName.values()){
            placeholderNames.add(nativePlaceholderName.name());
            placeholderValues.add(nativePlaceholderName);
        }

        CharSequence items[] = placeholderNames.toArray(new CharSequence[placeholderNames.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(ConsoliAdsListActivity.this);
        builder.setTitle("Select Placeholder");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("value is", "" + which);
                nativePlaceholderName = PlaceholderName.fromInteger(placeholderValues.get(which).getValue());
                etPlaceholder.setText(nativePlaceholderName.name());
            }
        });
        builder.show();
    }
}
