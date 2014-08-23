package com.naughtyspirit.appspice.client.ui.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.naughtyspirit.appspice.client.R;
import com.naughtyspirit.appspice.client.models.Ads;
import com.naughtyspirit.appspice.client.ui.adapters.WallAdAdapter;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class WallAd extends BaseAdDialog {

    private Ads ads;

    public WallAd(Context ctx, Ads ads) {
        super(ctx, R.style.WallAdDialog);

        this.ads = ads;

        initUI();
    }

    public void initUI() {
        setContentView(R.layout.dialog_ad_wall);

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
