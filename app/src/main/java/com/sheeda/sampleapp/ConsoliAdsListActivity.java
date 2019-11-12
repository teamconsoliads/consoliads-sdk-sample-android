package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.consoliads.mediation.ConsoliAds;
import com.consoliads.mediation.nativeads.ConsoliAdsNativeListener;
import com.consoliads.mediation.nativeads.MediatedNativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ConsoliAdsListActivity extends Activity {

    private EditText et_scene_index , et_list_index;
    private String listIndex ,sceneIndex;

    private List<Object> recipeList;
    private ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consoli_ads_list);

        et_list_index = findViewById(R.id.et_list_index_for_ad_consolaids_native);
        et_scene_index = findViewById(R.id.et_scene_index);

        try {
            recipeList = getRecipeListFromJSON(loadJSONFromAsset(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RecyclerView rvRecipes = findViewById(R.id.list_recipes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rvRecipes.addItemDecoration(dividerItemDecoration);
        listViewAdapter = new ListViewAdapter(this, recipeList);
        rvRecipes.setAdapter(listViewAdapter);
        rvRecipes.setLayoutManager(layoutManager);

        findViewById(R.id.btn_add_ad_consoliads_native).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listIndex = et_list_index.getText().toString();
                sceneIndex = et_scene_index.getText().toString();
                loadNativeAd();
            }
        });

    }

    private void loadNativeAd() {

        if(!listIndex.equals("") && !sceneIndex.equals(""))
        {
            ConsoliAds.Instance().loadNativeAd(Integer.parseInt(sceneIndex), ConsoliAdsListActivity.this, new ConsoliAdsNativeListener() {
                @Override
                public void onNativeAdLoaded(MediatedNativeAd mediatedNativeAd) {
                    Log.i("ConsoliAdsListners","onNativeAdLoaded");
                    recipeList.add(Integer.parseInt(listIndex), mediatedNativeAd);
                    listViewAdapter.notifyDataSetChanged();
                    listIndex = "";
                    sceneIndex = "";

                }
                @Override
                public void onNativeAdLoadFailed() {
                    Log.i("ConsoliAdsListners","onNativeAdLoadFailed");
                }
            });
        }
        else
        {
            Toast.makeText(getBaseContext() , "FILL ALL VALUES FIRST" , Toast.LENGTH_SHORT).show();
        }
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
}
