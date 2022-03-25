package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.consoliads.sdk.PrivacyPolicy;
import com.consoliads.sdk.bannerads.ConsoliadsSdkBannerView;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.nativeads.ActionButton;
import com.consoliads.sdk.nativeads.ConsoliadsSdkNativeAd;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeList;
    private Context context;
    private static final int RECIPE = 0;
    private static final int CONSOLI_ICON_AD = 3;
    private static final int CONSOLI_NATIVE_AD = 4;
    private static final int BANNER_AD = 5;

    ListViewAdapter(Context context, List<Object> recipeList) {
        this.recipeList = recipeList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == RECIPE) {
            View recipeItem = inflater.inflate(R.layout.item_recipe, parent, false);
            return new RecipeViewHolder(recipeItem);
        }
        else if (viewType == CONSOLI_ICON_AD) {
            View iconLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.icon_ad_container,
                    parent, false);
            return new IconAdViewHolder(iconLayoutView);
        }
        else if (viewType == CONSOLI_NATIVE_AD) {
            View nativeAdItem = (View) inflater.inflate(R.layout.row_ca_mediated_native_view, parent, false);
            ConsoliadsNativeAdViewHolder holder = new ConsoliadsNativeAdViewHolder(nativeAdItem );
            return holder;
        }
       else if (viewType == BANNER_AD) {
            View bannerLayoutView = LayoutInflater.from(
                    parent.getContext()).inflate(R.layout.banner_ad_container,
                    parent, false);
            return new BannerAdViewHolder(bannerLayoutView);
        }
        else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        //holder.setIsRecyclable(false);

        if (itemType == RECIPE) {

        }
        else if (itemType == CONSOLI_ICON_AD) {
            IconAdViewHolder iconAdViewHolder = (IconAdViewHolder) holder;
            IconAdView adView = (IconAdView) recipeList.get(position);
            ViewGroup adCardView = (ViewGroup) iconAdViewHolder.itemView;
            // The AdViewHolder recycled by the RecyclerView may be a different
            // instance than the one used previously for this position. Clear the
            // AdViewHolder of any subviews in case it has a different
            // AdView associated with it, and make sure the AdView for this position doesn't
            // already have a parent of a different recycled AdViewHolder.
            if (adCardView.getChildCount() > 0) {
                adCardView.removeAllViews();
            }
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            // Add the banner ad to the ad view.
            adCardView.addView(adView);
        }
        else if (itemType == CONSOLI_NATIVE_AD) {

            ConsoliadsNativeAdViewHolder consoliadsNativeAdViewHolder = (ConsoliadsNativeAdViewHolder) holder;
            ConsoliadsSdkNativeAd nativeAd = (ConsoliadsSdkNativeAd) recipeList.get(position);

            if (nativeAd != null){
                consoliadsNativeAdViewHolder.nativeAdContainer.setVisibility(View.VISIBLE);
                consoliadsNativeAdViewHolder.adTitle.setText(nativeAd.getAdTitle());
                consoliadsNativeAdViewHolder.adSubTitle.setText(nativeAd.getAdSubTitle());
                consoliadsNativeAdViewHolder.adDescription.setText(nativeAd.getAdDescription());
                nativeAd.setAdPrivacyPolicy(consoliadsNativeAdViewHolder.privacyPolicy);
                nativeAd.loadAdImage(consoliadsNativeAdViewHolder.adImage);
                nativeAd.registerClickToAction(consoliadsNativeAdViewHolder.actionButton);
            }
        }
        else if (itemType == BANNER_AD) {

            BannerAdViewHolder bannerHolder = (BannerAdViewHolder) holder;
            ConsoliadsSdkBannerView adView = (ConsoliadsSdkBannerView) recipeList.get(position);
            ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;
            // The AdViewHolder recycled by the RecyclerView may be a different
            // instance than the one used previously for this position. Clear the
            // AdViewHolder of any subviews in case it has a different
            // AdView associated with it, and make sure the AdView for this position doesn't
            // already have a parent of a different recycled AdViewHolder.
            if (adCardView.getChildCount() > 0) {
                adCardView.removeAllViews();
            }
            if (adView.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            // Add the banner ad to the ad view.
            adCardView.addView(adView);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = recipeList.get(position);
        if (item instanceof ListModel) {
            return RECIPE;
        } else if (item instanceof ConsoliadsSdkNativeAd) {
            return CONSOLI_NATIVE_AD;
        }
        else if (item instanceof ConsoliadsSdkBannerView) {
            return BANNER_AD;
        }
        else if (item instanceof IconAdView) {
            return CONSOLI_ICON_AD;
        }
        else {
            return -1;
        }
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {

        RecipeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class IconAdViewHolder extends RecyclerView.ViewHolder {

        IconAdViewHolder(View view) {
            super(view);
        }
    }

    public class BannerAdViewHolder extends RecyclerView.ViewHolder {

        BannerAdViewHolder(View view) {
            super(view);
        }
    }

    private class ConsoliadsNativeAdViewHolder extends RecyclerView.ViewHolder {

        LinearLayout nativeAdContainer;
        PrivacyPolicy privacyPolicy;
        TextView adTitle;
        TextView adSubTitle;
        TextView adDescription;
        ImageView adImage;
        ActionButton actionButton;


        ConsoliadsNativeAdViewHolder(View itemView) {
            super(itemView);

            nativeAdContainer = (LinearLayout) itemView.findViewById(R.id.native_container);
            adTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            adSubTitle = (TextView) itemView.findViewById(R.id.tv_ad_sub_title);
            adDescription = (TextView) itemView.findViewById(R.id.tv_ad_description);
            adImage = (ImageView) itemView.findViewById(R.id.iv_ad_image);
            actionButton = (ActionButton) itemView.findViewById(R.id.native_action_button);
            privacyPolicy = (PrivacyPolicy) itemView.findViewById(R.id.native_privacy_policy);
        }
    }

}
