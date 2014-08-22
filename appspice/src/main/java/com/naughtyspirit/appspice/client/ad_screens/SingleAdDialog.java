package com.naughtyspirit.appspice.client.ad_screens;


import android.app.Dialog;
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
public class SingleAdDialog extends Dialog {

    private Context ctx;
    private Ad ad;

    public SingleAdDialog(Context ctx, Ad ad) {
        super(ctx, R.style.SingleAppDialog);

        this.ctx = ctx;
        this.ad = ad;

        initUI();
    }

    public void initUI() {
        setContentView(R.layout.dialog_single_ad);

        ((TextView) findViewById(R.id.title)).setText(ad.getName());

        Ion.with(ctx)
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
