package it.appspice.android.api.errors;

/**
 * Created by Naughty Spirit <hi@naughtyspirit.co>
 * on 2/10/15.
 */
public class AppSpiceError extends RuntimeException {
    public AppSpiceError(String message, Throwable cause) {
        super(message, cause);
    }
}
