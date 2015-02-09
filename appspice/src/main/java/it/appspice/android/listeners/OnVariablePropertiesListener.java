package it.appspice.android.listeners;

import it.appspice.android.api.models.VariableProperties;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/6/15.
 */
public interface OnVariablePropertiesListener<T> {
    void onPropertiesReady(T variable);
}
