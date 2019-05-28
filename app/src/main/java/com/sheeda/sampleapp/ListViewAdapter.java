package com.sheeda.sampleapp;

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

import com.consoliads.sdk.iconads.IconAdBase;
import com.consoliads.sdk.iconads.IconAdView;
import com.consoliads.sdk.nativeads.ActionButton;
import com.consoliads.sdk.nativeads.AdChoices;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdIconView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> recipeList;
    private Context context;
    private static final int RECIPE = 0;
    private static final int FACEBOOK_NATIVE_AD = 1;
    private static final int ADMOB_NATIVE_AD = 2;
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
        } else if (viewType == FACEBOOK_NATIVE_AD) {
            View nativeAdItem = inflater.inflate(R.layout.item_facebook_native_ad, parent, false);
            return new FacebookNativeAdViewHolder(nativeAdItem);
        }
        else if (viewType == ADMOB_NATIVE_AD) {
            UnifiedNativeAdView nativeAdItem = (UnifiedNativeAdView)inflater.inflate(R.layout.item_admob_native_ad_updated, parent, false);
            return new AdmobNativeAdViewHolder(nativeAdItem);
        }
        else if (viewType == CONSOLI_ICON_AD) {
            View iconAdItem = (View) inflater.inflate(R.layout.row_consoli_icon_ad, parent, false);
            return new ConsoliadsIconAdViewHolder(iconAdItem);
        }
        else if (viewType == CONSOLI_NATIVE_AD) {
            View nativeAdItem = (View) inflater.inflate(R.layout.item_consoli_native_ad, parent, false);
            return new ConsoliadsNativeAdViewHolder(nativeAdItem);
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
        }else if (itemType == FACEBOOK_NATIVE_AD) {

            FacebookNativeAdViewHolder facebookNativeAdViewHolder = (FacebookNativeAdViewHolder) holder;
            NativeAd nativeAd = (NativeAd) recipeList.get(position);

            AdIconView adIconView = facebookNativeAdViewHolder.adIconView;
            TextView tvAdTitle = facebookNativeAdViewHolder.tvAdTitle;
            TextView tvAdBody = facebookNativeAdViewHolder.tvAdBody;
            Button btnCTA = facebookNativeAdViewHolder.btnCTA;
            LinearLayout adChoicesContainer = facebookNativeAdViewHolder.adChoicesContainer;
            MediaView mediaView = facebookNativeAdViewHolder.mediaView;
            TextView sponsorLabel = facebookNativeAdViewHolder.sponsorLabel;

            tvAdTitle.setText(nativeAd.getAdvertiserName());
            tvAdBody.setText(nativeAd.getAdBodyText());
            btnCTA.setText(nativeAd.getAdCallToAction());
            sponsorLabel.setText(nativeAd.getSponsoredTranslation());

            adChoicesContainer.removeAllViews();
            AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
            adChoicesContainer.addView(adChoicesView);

            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(btnCTA);
            clickableViews.add(mediaView);
            nativeAd.registerViewForInteraction(facebookNativeAdViewHolder.container, mediaView, adIconView, clickableViews);
        }
        else if (itemType == CONSOLI_NATIVE_AD) {

            ConsoliadsNativeAdViewHolder consoliadsNativeAdViewHolder = (ConsoliadsNativeAdViewHolder) holder;
            com.consoliads.sdk.nativeads.NativeAd nativeAd = (com.consoliads.sdk.nativeads.NativeAd) recipeList.get(position);

            consoliadsNativeAdViewHolder.adTitle.setText(nativeAd.getAdTitle());
            consoliadsNativeAdViewHolder.adSubTitle.setText(nativeAd.getAdSubTitle());
            consoliadsNativeAdViewHolder.adDescription.setText(nativeAd.getAdDescription());
            nativeAd.setAdChoices(consoliadsNativeAdViewHolder.adChoices);
            nativeAd.registerClickToAction(consoliadsNativeAdViewHolder.actionButton);
            nativeAd.loadAdImage(consoliadsNativeAdViewHolder.adImage);
        }
        else if(itemType == ADMOB_NATIVE_AD)
        {
            AdmobNativeAdViewHolder admobNativeAdViewHolder = (AdmobNativeAdViewHolder) holder;
            UnifiedNativeAd unifiedNativeAd = (UnifiedNativeAd) recipeList.get(position);
            populateUnifiedNativeAdView(unifiedNativeAd, (UnifiedNativeAdView) admobNativeAdViewHolder.adView);
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
        } else if (item instanceof Ad) {
            return FACEBOOK_NATIVE_AD;
        }
        else if (item instanceof UnifiedNativeAd) {
            return ADMOB_NATIVE_AD;
        }
        else if (item instanceof IconAdBase) {
            return CONSOLI_ICON_AD;
        }
        else if (item instanceof com.consoliads.sdk.nativeads.NativeAd) {
            return CONSOLI_NATIVE_AD;
        }
        else {
            return -1;
        }
    }

    private static class RecipeViewHolder extends RecyclerView.ViewHolder {
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

    private static class ConsoliadsIconAdViewHolder extends RecyclerView.ViewHolder {

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

    private static class ConsoliadsNativeAdViewHolder extends RecyclerView.ViewHolder {

        AdChoices adChoices;
        TextView adTitle;
        TextView adSubTitle;
        TextView adDescription;
        ImageView adImage;
        ActionButton actionButton;

        ConsoliadsNativeAdViewHolder(View itemView) {
            super(itemView);
            adTitle = (TextView) itemView.findViewById(R.id.tv_ad_title);
            adSubTitle = (TextView) itemView.findViewById(R.id.tv_ad_sub_title);
            adDescription = (TextView) itemView.findViewById(R.id.tv_ad_description);
            adImage = (ImageView) itemView.findViewById(R.id.iv_ad_image);
            adChoices = (AdChoices) itemView.findViewById(R.id.native_adchoices_main);
            actionButton = (ActionButton) itemView.findViewById(R.id.native_action_button);
        }
    }

    private static class FacebookNativeAdViewHolder extends RecyclerView.ViewHolder {
        AdIconView adIconView;
        TextView tvAdTitle;
        TextView tvAdBody;
        Button btnCTA;
        View container;
        TextView sponsorLabel;
        LinearLayout adChoicesContainer;
        MediaView mediaView;

        FacebookNativeAdViewHolder(View itemView) {
            super(itemView);
            this.container = itemView;
            adIconView = (AdIconView) itemView.findViewById(R.id.adIconView);
            tvAdTitle = (TextView) itemView.findViewById(R.id.tvAdTitle);
            tvAdBody = (TextView) itemView.findViewById(R.id.tvAdBody);
            btnCTA = (Button) itemView.findViewById(R.id.btnCTA);
            adChoicesContainer = (LinearLayout) itemView.findViewById(R.id.adChoicesContainer);
            mediaView = (MediaView) itemView.findViewById(R.id.mediaView);
            sponsorLabel = (TextView) itemView.findViewById(R.id.sponsored_label);
        }
    }

    private static class AdmobNativeAdViewHolder extends RecyclerView.ViewHolder {
        UnifiedNativeAdView adView;

        AdmobNativeAdViewHolder(View itemView) {
            super(itemView);
            this.adView = (UnifiedNativeAdView) itemView;
        }
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
        // VideoController will call methods on this object when events occur in the video
        // lifecycle.
        vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
            public void onVideoEnd() {
                // Publishers should allow native ads to complete video playback before refreshing
                // or replacing them with another ad in the same UI location.
                //refresh.setEnabled(true);
                //videoStatus.setText("Video status: Video playback has ended.");
                super.onVideoEnd();
            }
        });

        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(com.consoliads.mediation.R.id.ad_media);
        ImageView mainImageView = adView.findViewById(com.consoliads.mediation.R.id.ad_image);

        // Apps can check the VideoController's hasVideoContent property to determine if the
        // NativeAppInstallAd has a video asset.
        if (vc.hasVideoContent()) {
            adView.setMediaView(mediaView);
            mainImageView.setVisibility(View.GONE);
            /*
            videoStatus.setText(String.format(Locale.getDefault(),
                    "Video status: Ad contains a %.2f:1 video asset.",
                    vc.getAspectRatio()));*/
        } else {
            adView.setImageView(mainImageView);
            mediaView.setVisibility(View.GONE);

            // At least one image is guaranteed.
            List<com.google.android.gms.ads.formats.NativeAd.Image> images = nativeAd.getImages();
            mainImageView.setImageDrawable(images.get(0).getDrawable());

            /*
            refresh.setEnabled(true);
            videoStatus.setText("Video status: Ad does not contain a video asset.");*/
        }

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
