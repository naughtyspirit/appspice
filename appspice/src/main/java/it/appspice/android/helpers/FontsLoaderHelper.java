package it.appspice.android.helpers;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by NaughtySpirit
 * Created on 12/Dec/2014
 */
public class FontsLoaderHelper {

    public static Typeface getHero(Context ctx) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/Hero.otf");

        return font;
    }

    public static Typeface getHeroLight(Context ctx) {
        Typeface font = Typeface.createFromAsset(ctx.getAssets(), "fonts/HeroLight.ttf");

        return font;
    }
}