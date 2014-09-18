package it.appspice.android.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
import it.appspice.android.helpers.BlurBackgroundHelper;
import it.appspice.android.helpers.Constants;
import it.appspice.android.models.Ad;
import it.appspice.android.providers.ads.AppSpiceAdProvider;

/**
 * Created by NaughtySpirit
 * Created on 22/Aug/2014
 */
public class FullScreenAd extends BaseAdDialog {

    private Ad ad;
    private Context ctx;

    public FullScreenAd(Context ctx, Ad ad) {
        super(ctx, R.style.FullscreenAdDialog);

        this.ad = ad;
        this.ctx = ctx;

        initUI();

        AppSpiceClient.sendAdImpressionEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.FullScreen.toString());
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_fullscreen);
        Random randNum = new Random();
        getWindow().setBackgroundDrawable(ctx.getResources().getDrawable(adBackgrounds[randNum.nextInt(adBackgrounds.length)]));

//        getWindow().setBackgroundDrawable(new BitmapDrawable(
//                ctx.getResources(), BlurBackgroundHelper.getBlurredBackground((Activity) ctx)));

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

        Button install = (Button) findViewById(R.id.install);
        install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppSpiceClient.sendAdClickEvent(AppSpiceAdProvider.PROVIDER_NAME, Constants.AdTypes.FullScreen.toString());

                try {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ad.getAppPackage())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + ad.getAppPackage())));
                }

                dismiss();
            }
        });

        Animation wiggleAnim = AnimationUtils.loadAnimation(ctx, R.anim.wiggle);
        install.setAnimation(wiggleAnim);
    }
}
