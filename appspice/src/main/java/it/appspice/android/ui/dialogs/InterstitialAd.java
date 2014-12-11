package it.appspice.android.ui.dialogs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import it.appspice.android.R;
import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants;
import it.appspice.android.helpers.FontsLoaderHelper;
import it.appspice.android.models.Ad;
import it.appspice.android.providers.ads.AppSpiceAdProvider;

/**
 * Created by NaughtySpirit
 * Created on 22/Aug/2014
 */
public class InterstitialAd extends BaseAdDialog implements OnClickListener {

    private Ad ad;
    private Context ctx;

    private ImageView icon;
    private RelativeLayout window;
    private RelativeLayout dialog;

    private Animation slideInTop;
    private Animation slideInBottom;
    private Animation slideOutTop;
    private Animation slideOutBottom;
    private Animation alphaIn;
    private Animation alphaOut;

    public InterstitialAd(Context ctx, Ad ad) {
        super(ctx, R.style.FullscreenAdDialog);

        this.ad = ad;
        this.ctx = ctx;

        initAnims();
        initUI();

        AppSpiceClient.sendAdImpressionEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.FullScreen.toString());
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_interstitial);
        setCancelable(false);

        Typeface hero = FontsLoaderHelper.getHero(ctx);

        window = (RelativeLayout) findViewById(R.id.window);
        dialog = (RelativeLayout) findViewById(R.id.dialog);

        icon = (ImageView) findViewById(R.id.icon);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText(ad.getName());
        title.setTypeface(hero);

        Ion.with(getContext())
                .load(ad.getIconUrl())
                .intoImageView(icon);

        Button install = (Button) findViewById(R.id.install);
        install.setTypeface(hero);
        install.setOnClickListener(this);

        Button noInstall = (Button) findViewById(R.id.no_install);
        noInstall.setTypeface(hero);
        noInstall.setOnClickListener(this);

        alphaIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                icon.setVisibility(View.VISIBLE);
                dialog.setVisibility(View.VISIBLE);

                icon.setAnimation(slideInTop);
                dialog.setAnimation(slideInBottom);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        window.setAnimation(alphaIn);
    }

    private void initAnims() {
        slideInTop = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_top);
        slideInBottom = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
        slideOutTop = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_top);
        slideOutBottom = AnimationUtils.loadAnimation(ctx, R.anim.slide_out_bottom);
        alphaIn = AnimationUtils.loadAnimation(ctx, R.anim.alpha_in);
        alphaOut = AnimationUtils.loadAnimation(ctx, R.anim.alpha_out);

        slideInTop.setFillAfter(true);
        slideInBottom.setFillAfter(true);
        slideOutTop.setFillAfter(true);
        slideOutBottom.setFillAfter(true);
    }

    private void transitToMarket() {
        AppSpiceClient.sendAdClickEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.Interstitial.toString());

        try {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ad.getAppPackage())));
        } catch (android.content.ActivityNotFoundException e) {
            ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ad.getAppPackage())));
        }
    }

    private void closeAd(final boolean isInstall) {
        icon.startAnimation(slideOutTop);
        dialog.startAnimation(slideOutBottom);

        slideOutBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                window.startAnimation(alphaOut);
                alphaOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        InterstitialAd.this.dismiss();

                        if (isInstall) {
                            transitToMarket();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.install) {
            closeAd(true);
        }

        if (view.getId() == R.id.no_install) {
            closeAd(false);
        }
    }
}
