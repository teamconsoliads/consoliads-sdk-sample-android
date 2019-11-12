package com.sheeda.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.consoliads.mediation.nativeads.CAAdChoicesView;
import com.consoliads.mediation.nativeads.CAAppIconView;
import com.consoliads.mediation.nativeads.CACallToActionView;
import com.consoliads.mediation.nativeads.CAMediaView;
import com.consoliads.mediation.nativeads.CANativeAdView;
import com.consoliads.mediation.nativeads.MediatedNativeAd;
import com.consoliads.sdk.iconads.IconAdBase;
import com.consoliads.sdk.iconads.IconAdView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeList;
    private Context context;
    private static final int RECIPE = 0;
    private static final int CONSOLI_ICON_AD = 3;
    private static final int CONSOLI_NATIVE_AD = 4;

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
        }else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);

        if (itemType == RECIPE) {
            RecipeViewHolder recipeViewHolder = (RecipeViewHolder) holder;
            ListModel listModel = (ListModel) recipeList.get(position);
            SimpleDraweeView ivFood = recipeViewHolder.ivFood;
            TextView tvDescription = recipeViewHolder.tvDescription;
            TextView tvRecipeName = recipeViewHolder.tvRecipeName;
            LinearLayout llCategories = recipeViewHolder.llCategories;
            llCategories.removeAllViews(); // Clear the categories.
            ivFood.setImageURI("asset:///" + listModel.getImagePath());
            tvRecipeName.setText(listModel.getRecipeName());
            tvDescription.setText(listModel.getDescription());
            addFoodCategories(llCategories, listModel.getCategories());
        }
        else if (itemType == CONSOLI_ICON_AD) {
            ConsoliadsIconAdViewHolder consoliadsIconAdViewHolder = (ConsoliadsIconAdViewHolder) holder;
            IconAdBase iconAdBase = (IconAdBase) recipeList.get(position);
            consoliadsIconAdViewHolder.iconAdView.setIconAd(iconAdBase);
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
    }

    private void addFoodCategories(LinearLayout categoriesContainer, List<String> categories) {
        for (String category : categories) {
            ImageView iv = new ImageView(context);
            switch (category) {
                case "Meat":
                    iv.setImageResource(R.drawable.meat);
                    break;
                case "Carbs":
                    iv.setImageResource(R.drawable.carbs);
                    break;
                case "Veggie":
                    iv.setImageResource(R.drawable.veggie);
                    break;
                default:
                    continue;
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(70, 70);
            iv.setLayoutParams(layoutParams);
            categoriesContainer.addView(iv);
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
        else {
            return -1;
        }
    }

    private class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        TextView tvRecipeName;
        LinearLayout llCategories;
        SimpleDraweeView ivFood;

        RecipeViewHolder(View itemView) {
            super(itemView);
            tvRecipeName = (TextView) itemView.findViewById(R.id.tvRecipeName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            llCategories = (LinearLayout) itemView.findViewById(R.id.categories_container);
            ivFood = (SimpleDraweeView) itemView.findViewById(R.id.ivFood);
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
                    }
                }
            });
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
