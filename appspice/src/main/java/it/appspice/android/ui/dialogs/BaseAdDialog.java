package it.appspice.android.ui.dialogs;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by NaughtySpirit
 * Created on 23/Aug/2014
 */
public abstract class BaseAdDialog extends Dialog {

    public BaseAdDialog(Context ctx) {
        super(ctx);
    }

    public BaseAdDialog(Context ctx, int style) {
        super(ctx, style);
    }
}
