package com.naughtyspirit.appspice.client.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.naughtyspirit.appspice.client.R;
import com.naughtyspirit.appspice.client.client.AppspiceClient;
import com.naughtyspirit.appspice.client.helpers.Constants.AdTypes;
import com.naughtyspirit.appspice.client.models.Ad;
import com.naughtyspirit.appspice.client.models.Ads;
import com.naughtyspirit.appspice.client.providers.ads.AppSpiceAdProvider;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public class WallAdAdapter extends BaseAdapter {

    private Context ctx;
    private Ads ads;
    private Ad ad;

    private View view;
    private ViewHolder holder;

    public WallAdAdapter(Context ctx, Ads ads) {
        this.ctx = ctx;
        this.ads = ads;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {

        view = convertView;

        ad = ads.getData().get(pos);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_wall_item, viewGroup, false);

            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.install = (Button) view.findViewById(R.id.install);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        setData();

        return view;
    }

    private void setData() {
        holder.title.setText(ad.getName());

        Ion.with(ctx)
                .load(ad.getIconUrl())
                .intoImageView(holder.icon);

        holder.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppspiceClient.sendAdClickEvent(AppSpiceAdProvider.PROVIDER_NAME, AdTypes.Wall.toString());
            }
        });
    }

    @Override
    public int getCount() {
        return ads.getData().size();
    }

    @Override
    public Object getItem(int pos) {
        return ads.getData().get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    private static class ViewHolder {

        private TextView title;
        private ImageView icon;
        private Button install;

    }
}
