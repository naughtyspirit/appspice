package it.appspice.android.ui.dialogs;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import it.appspice.android.R;
import it.appspice.android.helpers.BlurBackgroundHelper;
import it.appspice.android.models.Ads;
import it.appspice.android.client.AppSpiceClient;
import it.appspice.android.helpers.Constants.AdTypes;
import it.appspice.android.providers.ads.AppSpiceAdProvider;
import it.appspice.android.ui.adapters.WallAdAdapter;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class WallAd extends BaseAdDialog {

    private Context ctx;
    private Ads ads;

    public WallAd(Context ctx, Ads ads) {
        super(ctx, R.style.WallAdDialog);

        this.ctx = ctx;
        this.ads = ads;

        initUI();
        AppSpiceClient.sendAdImpressionEvent(AppSpiceAdProvider.PROVIDER_NAME, AdTypes.Wall.toString());
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_wall);
        getWindow().setBackgroundDrawable(new BitmapDrawable(
                ctx.getResources(), BlurBackgroundHelper.getBlurredBackground((Activity) ctx)));

        ListView adsList = (ListView) findViewById(R.id.ads_list);
        adsList.setAdapter(new WallAdAdapter(getContext(), ads));

        Button close = (Button) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
