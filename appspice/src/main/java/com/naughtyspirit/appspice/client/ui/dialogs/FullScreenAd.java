package com.naughtyspirit.appspice.client.ui.dialogs;


import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.naughtyspirit.appspice.client.R;
import com.naughtyspirit.appspice.client.models.Ad;

/**
 * Created by NaughtySpirit
 * Created on 22/Aug/2014
 */
public class FullScreenAd extends BaseAdDialog {

    private Ad ad;

    public FullScreenAd(Context ctx, Ad ad) {
        super(ctx, R.style.FullscreenAdDialog);

        this.ad = ad;

        initUI();
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_fullscreen);

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
                // TODO: Send install event
            }
        });
    }
}
