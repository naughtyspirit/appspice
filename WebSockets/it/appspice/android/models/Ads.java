package it.appspice.android.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaughtySpirit
 * Created on 21/Aug/2014
 */
public class Ads {

    private List<Ad> data = new ArrayList<Ad>();

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        this.data = data;
    }
}
