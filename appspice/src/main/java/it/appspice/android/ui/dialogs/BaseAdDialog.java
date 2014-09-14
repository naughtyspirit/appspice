package it.appspice.android.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

import it.appspice.android.R;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public abstract class BaseAdDialog extends Dialog {

    protected int[] adBackgrounds = {R.drawable.bg_1, R.drawable.bg_2, R.drawable.bg_3};

    public BaseAdDialog(Context ctx) {
        super(ctx);
    }

    public BaseAdDialog(Context ctx, int style) {
        super(ctx, style);
    }
}
