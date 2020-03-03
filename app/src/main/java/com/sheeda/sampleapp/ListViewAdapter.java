package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.consoliads.mediation.bannerads.CAMediatedBannerView;
import com.consoliads.mediation.nativeads.CAAdChoicesView;
import com.consoliads.mediation.nativeads.CAAppIconView;
import com.consoliads.mediation.nativeads.CACallToActionView;
import com.consoliads.mediation.nativeads.CAMediaView;
import com.consoliads.mediation.nativeads.CANativeAdView;
import com.consoliads.mediation.nativeads.MediatedNativeAd;
import com.consoliads.sdk.iconads.IconAdBase;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.iconads.IconAnimationConstant;

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
            View iconAdItem = (View) inflater.inflate(R.layout.row_consoli_icon_ad, parent, false);
            return new ConsoliadsIconAdViewHolder(iconAdItem);
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
            ConsoliadsIconAdViewHolder consoliadsIconAdViewHolder = (ConsoliadsIconAdViewHolder) holder;
            IconAdBase iconAdBase = (IconAdBase) recipeList.get(position);
            consoliadsIconAdViewHolder.iconAdView.setIconAd(iconAdBase , IconAnimationConstant.PULSE);
        }
        else if (itemType == CONSOLI_NATIVE_AD) {

            ConsoliadsNativeAdViewHolder consoliadsNativeAdViewHolder = (ConsoliadsNativeAdViewHolder) holder;
            MediatedNativeAd mediatedNativeAd = (MediatedNativeAd) recipeList.get(position);

            consoliadsNativeAdViewHolder.actionView.setTextColor("#ffffff");
            consoliadsNativeAdViewHolder.actionView.setTextSize_UNIT_SP(12);

            mediatedNativeAd.setSponsered(consoliadsNativeAdViewHolder.sponsered);
            mediatedNativeAd.setAdTitle(consoliadsNativeAdViewHolder.title);
            mediatedNativeAd.setAdSubTitle(consoliadsNativeAdViewHolder.subtitle);
            mediatedNativeAd.setAdBody(consoliadsNativeAdViewHolder.body);
            mediatedNativeAd.registerViewForInteraction((Activity) context, consoliadsNativeAdViewHolder.appIconView , consoliadsNativeAdViewHolder.mediaView , consoliadsNativeAdViewHolder.actionView , consoliadsNativeAdViewHolder.adView,consoliadsNativeAdViewHolder.adChoicesView);

        }
        else if (itemType == BANNER_AD) {

            BannerAdViewHolder bannerHolder = (BannerAdViewHolder) holder;
            CAMediatedBannerView adView = (CAMediatedBannerView) recipeList.get(position);
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
        } else if (item instanceof MediatedNativeAd) {
            return CONSOLI_NATIVE_AD;
        }
        else if (item instanceof CAMediatedBannerView) {
            return BANNER_AD;
        }
        else if (item instanceof IconAdBase) {
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

    private class ConsoliadsIconAdViewHolder extends RecyclerView.ViewHolder {

        IconAdView iconAdView;
        ImageView close;

        ConsoliadsIconAdViewHolder(View itemView) {
            super(itemView);
            iconAdView = itemView.findViewById(R.id.icon_adview);
            close = itemView.findViewById(R.id.close_icon__ad);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(iconAdView != null)
                    {
                        iconAdView.hideAd();
                        recipeList.remove(getAdapterPosition());
                        ListViewAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public class BannerAdViewHolder extends RecyclerView.ViewHolder {

        BannerAdViewHolder(View view) {
            super(view);
        }
    }

    private class ConsoliadsNativeAdViewHolder extends RecyclerView.ViewHolder {

        TextView title , subtitle , body , sponsered;
        CANativeAdView adView;
        CAAdChoicesView adChoicesView;
        CAAppIconView appIconView;
        CAMediaView mediaView;
        CACallToActionView actionView;

        ConsoliadsNativeAdViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.native_ad_title);
            subtitle = itemView.findViewById(R.id.native_ad_sub_title);
            body = itemView.findViewById(R.id.native_ad_body);
            sponsered = itemView.findViewById(R.id.native_ad_sponsored_label);

            adView = itemView.findViewById(R.id.native_ad_frame);
            adChoicesView = itemView.findViewById(R.id.ad_choices_container);
            appIconView = itemView.findViewById(R.id.native_ad_icon);
            mediaView = itemView.findViewById(R.id.native_ad_media);
            actionView = itemView.findViewById(R.id.native_ad_call_to_action);
        }

        public void registerView(MediatedNativeAd mediatedNativeAd , Activity activity)
        {
            mediatedNativeAd.registerViewForInteraction(activity, appIconView , mediaView , actionView , adView,adChoicesView);
        }
    }

}
