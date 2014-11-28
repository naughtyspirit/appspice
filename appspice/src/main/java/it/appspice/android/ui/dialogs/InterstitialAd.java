package it.appspice.android.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.Random;

import it.appspice.android.R;
import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants;
import it.appspice.android.models.Ad;
import it.appspice.android.providers.ads.AppSpiceAdProvider;

/**
 * Created by NaughtySpirit
 * Created on 22/Aug/2014
 */
public class InterstitialAd extends BaseAdDialog {

    private Ad ad;
    private Context ctx;

    public InterstitialAd(Context ctx, Ad ad) {
        super(ctx, R.style.FullscreenAdDialog);

        this.ad = ad;
        this.ctx = ctx;

        initUI();

        AppSpiceClient.sendAdImpressionEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.FullScreen.toString());
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_interstitial);
//        Random randNum = new Random();
        getWindow().setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.bg_snow));

        ((TextView) findViewById(R.id.title)).setText(ad.getName());

        Ion.with(getContext())
                .load(ad.getIconUrl())
                .intoImageView((ImageView) findViewById(R.id.icon));

        Button close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        final Button install = (Button) findViewById(R.id.install);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSpiceClient.sendAdClickEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.FullScreen.toString());

                try {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ad.getAppPackage())));
                } catch (android.content.ActivityNotFoundException e) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ad.getAppPackage())));
                }

                dismiss();
            }
        });

        final Animation wiggleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.wiggle);
        wiggleAnim.setFillAfter(true);

        final Animation showInstallBtnAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_bottom);
        showInstallBtnAnim.setFillAfter(true);
        showInstallBtnAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                install.setAnimation(wiggleAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        final Animation showIconAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        showIconAnim.setFillAfter(true);
        showIconAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                findViewById(R.id.icon).setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                install.setAnimation(showInstallBtnAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Animation showTitleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        showTitleAnim.setFillAfter(true);
        findViewById(R.id.title).setAnimation(showTitleAnim);
        showTitleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.icon).setAnimation(showIconAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }
}
