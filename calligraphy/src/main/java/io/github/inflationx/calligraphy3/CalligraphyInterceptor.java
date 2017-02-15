package io.github.inflationx.calligraphy3;

import android.view.View;

import io.github.inflationx.viewpump.InflateResult;
import io.github.inflationx.viewpump.Interceptor;

public class CalligraphyInterceptor implements Interceptor {

    private final Calligraphy calligraphy;

    public CalligraphyInterceptor(CalligraphyConfig calligraphyConfig) {
        this.calligraphy = new Calligraphy(calligraphyConfig);
    }

    @Override
    public InflateResult intercept(Chain chain) {
        InflateResult result = chain.proceed(chain.request());
        View viewWithTypeface = calligraphy.onViewCreated(result.view(), result.context(), result.attrs());
        return result.toBuilder().view(viewWithTypeface).build();
    }
}
