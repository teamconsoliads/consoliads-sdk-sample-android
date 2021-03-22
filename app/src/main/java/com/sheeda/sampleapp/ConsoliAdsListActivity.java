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

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.bannerads.CAMediatedBannerView;
import com.consoliads.mediation.constants.IconSize;
import com.consoliads.mediation.constants.PlaceholderName;
import com.consoliads.mediation.listeners.ConsoliAdsBannerListener;
import com.consoliads.mediation.listeners.ConsoliAdsIconListener;
import com.consoliads.mediation.nativeads.ConsoliAdsNativeListener;
import com.consoliads.mediation.nativeads.MediatedNativeAd;
import com.consoliads.sdk.iconads.IconAdView;

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
            ConsoliAds.Instance().loadNativeAd(ConsoliAdsListActivity.this, new ConsoliAdsNativeListener() {
                @Override
                public void onNativeAdLoaded(MediatedNativeAd mediatedNativeAd) {
                    Log.i("ConsoliAdsListners","onNativeAdLoaded");
                    recipeList.add(Integer.parseInt(listIndex), mediatedNativeAd);
                    listViewAdapter.notifyDataSetChanged();
                    hideProgess();
                }
                @Override
                public void onNativeAdLoadFailed() {
                    Log.i("ConsoliAdsListners","onNativeAdLoadFailed");
                    hideProgess();
                }
            });
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
        ConsoliAds.Instance().showIconAd(ConsoliAdsListActivity.this,iconAdView, IconSize.SmallIcon, new ConsoliAdsIconListener() {
            @Override
            public void onIconAdShownEvent() {
                Log.i(TAG,"onIconAdShownEvent");
                recipeList.add(Integer.parseInt(listIndex), iconAdView);
                listViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onIconAdFailedToShownEvent() {
                Log.i(TAG,"onIconAdFailedToShownEvent");
            }

            @Override
            public void onIconAdRefreshEvent() {
                Log.i(TAG,"onIconAdRefreshEvent");
            }

            @Override
            public void onIconAdClosedEvent() {
                Log.i(TAG,"onIconAdClosedEvent");
            }

            @Override
            public void onIconAdClickEvent() {
                Log.i(TAG,"onIconAdClickEvent");
            }
        });
    }

    public void loadBannerAd()
    {
        LayoutInflater inflater = LayoutInflater.from(ConsoliAdsListActivity.this);
        final CAMediatedBannerView mediatedBannerView = (CAMediatedBannerView) inflater.inflate(R.layout.row_mediated_banner, null, false);
        mediatedBannerView.setBannerListener(new ConsoliAdsBannerListener() {
            @Override
            public void onBannerAdShownEvent() {
                recipeList.add(Integer.parseInt(listIndex) , mediatedBannerView);
                listViewAdapter.notifyDataSetChanged();
                hideProgess();
                Toast.makeText(getBaseContext() , "onBannerAdShownEvent" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerAdRefreshEvent() {
                Toast.makeText(getBaseContext() , "onBannerAdRefreshEvent" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerAdFailToShowEvent() {
                hideProgess();
                Toast.makeText(getBaseContext() , "onBannerAdFailToShowEvent" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBannerAdClickEvent() {
                hideProgess();
                Toast.makeText(getBaseContext() , "onBannerAdClickEvent" , Toast.LENGTH_SHORT).show();
            }
        });
        showProgress();
        ConsoliAds.Instance().ShowBanner(nativePlaceholderName, ConsoliAdsListActivity.this , mediatedBannerView);

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
