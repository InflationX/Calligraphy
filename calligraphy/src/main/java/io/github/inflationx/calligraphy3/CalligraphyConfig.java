package io.github.inflationx.calligraphy3;

import android.os.Build;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by chris on 20/12/2013
 * Project: Calligraphy
 */
public class CalligraphyConfig {

    /**
     * The default styles for the factory to lookup. The builder builds an extended immutable
     * map of this with any additional custom styles.
     */
    private static final Map<Class<? extends TextView>, Integer> DEFAULT_STYLES = new HashMap<>();

    static {
        {
            DEFAULT_STYLES.put(TextView.class, android.R.attr.textViewStyle);
            DEFAULT_STYLES.put(Button.class, android.R.attr.buttonStyle);
            DEFAULT_STYLES.put(EditText.class, android.R.attr.editTextStyle);
            DEFAULT_STYLES.put(AutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(MultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
            DEFAULT_STYLES.put(CheckBox.class, android.R.attr.checkboxStyle);
            DEFAULT_STYLES.put(RadioButton.class, android.R.attr.radioButtonStyle);
            DEFAULT_STYLES.put(ToggleButton.class, android.R.attr.buttonStyleToggle);
            if (CalligraphyUtils.canAddV7AppCompatViews()) {
                addAppCompatViews();
            }
        }
    }

    /**
     * AppCompat will inflate special versions of views for Material tinting etc,
     * this adds those classes to the style lookup map
     */
    private static void addAppCompatViews() {
        DEFAULT_STYLES.put(AppCompatTextView.class, android.R.attr.textViewStyle);
        DEFAULT_STYLES.put(AppCompatButton.class, android.R.attr.buttonStyle);
        DEFAULT_STYLES.put(AppCompatEditText.class, android.R.attr.editTextStyle);
        DEFAULT_STYLES.put(AppCompatAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
        DEFAULT_STYLES.put(AppCompatMultiAutoCompleteTextView.class, android.R.attr.autoCompleteTextViewStyle);
        DEFAULT_STYLES.put(AppCompatCheckBox.class, android.R.attr.checkboxStyle);
        DEFAULT_STYLES.put(AppCompatRadioButton.class, android.R.attr.radioButtonStyle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DEFAULT_STYLES.put(AppCompatCheckedTextView.class, android.R.attr.checkedTextViewStyle);
        }
    }

    /**
     * Is a default font set?
     */
    private final boolean mIsFontSet;
    /**
     * The default Font Path if nothing else is setup.
     */
    private final String mFontPath;
    /**
     * Default Font Path Attr Id to lookup
     */
    private final int mAttrId;
    /**
     * Use Reflection to try to set typeface for custom views if they has setTypeface method
     */
    private final boolean mCustomViewTypefaceSupport;
    /**
     * Class Styles. Build from DEFAULT_STYLES and the builder.
     */
    private final Map<Class<? extends TextView>, Integer> mClassStyleAttributeMap;
    /**
     * Collection of custom non-{@code TextView}'s registered for applying typeface during inflation
     * @see CalligraphyConfig.Builder#addCustomViewWithSetTypeface(Class)
     */
    private final Set<Class<?>> hasTypefaceViews;
    /**
     * An object that can map a resolved font name to another font name.
     */
    private final FontMapper mFontMapper;

    private CalligraphyConfig(Builder builder) {
        mIsFontSet = builder.isFontSet;
        mFontPath = builder.fontAssetPath;
        mAttrId = builder.attrId;
        mCustomViewTypefaceSupport = builder.customViewTypefaceSupport;
        final Map<Class<? extends TextView>, Integer> tempMap = new HashMap<>(DEFAULT_STYLES);
        tempMap.putAll(builder.mStyleClassMap);
        mClassStyleAttributeMap = Collections.unmodifiableMap(tempMap);
        hasTypefaceViews = Collections.unmodifiableSet(builder.mHasTypefaceClasses);
        mFontMapper = builder.fontMapper;
    }

    /**
     * @return mFontPath for text views might be null
     */
    public String getFontPath() {
        return mFontPath;
    }

    /**
     * @return true if set, false if null|empty
     */
    boolean isFontSet() {
        return mIsFontSet;
    }

    public boolean isCustomViewTypefaceSupport() {
        return mCustomViewTypefaceSupport;
    }

    public boolean isCustomViewHasTypeface(View view) {
        return hasTypefaceViews.contains(view.getClass());
    }

    /* default */ Map<Class<? extends TextView>, Integer> getClassStyles() {
        return mClassStyleAttributeMap;
    }

    /**
     * @return the custom attrId to look for, -1 if not set.
     */
    public int getAttrId() {
        return mAttrId;
    }

    public FontMapper getFontMapper() {
        return mFontMapper;
    }

    public static class Builder {
        /**
         * Default AttrID if not set.
         */
        public static final int INVALID_ATTR_ID = -1;
        /**
         * Use Reflection during view creation to try change typeface via setTypeface method if it exists
         */
        private boolean customViewTypefaceSupport = false;
        /**
         * The fontAttrId to look up the font path from.
         */
        private int attrId = R.attr.fontPath;
        /**
         * Has the user set the default font path.
         */
        private boolean isFontSet = false;
        /**
         * The default fontPath
         */
        private String fontAssetPath = null;
        /**
         * Additional Class Styles. Can be empty.
         */
        private Map<Class<? extends TextView>, Integer> mStyleClassMap = new HashMap<>();

        private Set<Class<?>> mHasTypefaceClasses = new HashSet<>();

        private FontMapper fontMapper;

        /**
         * This defaults to R.attr.fontPath. So only override if you want to use your own attrId.
         *
         * @param fontAssetAttrId the custom attribute to look for fonts in assets.
         * @return this builder.
         */
        public Builder setFontAttrId(int fontAssetAttrId) {
            this.attrId = fontAssetAttrId;
            return this;
        }

        /**
         * Set the default font if you don't define one else where in your styles.
         *
         * @param defaultFontAssetPath a path to a font file in the assets folder, e.g. "fonts/Roboto-light.ttf",
         *                             passing null will default to the device font-family.
         * @return this builder.
         */
        public Builder setDefaultFontPath(String defaultFontAssetPath) {
            this.isFontSet = !TextUtils.isEmpty(defaultFontAssetPath);
            this.fontAssetPath = defaultFontAssetPath;
            return this;
        }

        /**
         * Add a custom style to get looked up. If you use a custom class that has a parent style
         * which is not part of the default android styles you will need to add it here.
         *
         * The Calligraphy inflater is unaware of custom styles in your custom classes. We use
         * the class type to look up the style attribute in the theme resources.
         *
         * So if you had a {@code MyTextField.class} which looked up it's default style as
         * {@code R.attr.textFieldStyle} you would add those here.
         *
         * {@code builder.addCustomStyle(MyTextField.class,R.attr.textFieldStyle}
         *
         * @param styleClass             the class that related to the parent styleResource. null is ignored.
         * @param styleResourceAttribute e.g. {@code R.attr.textFieldStyle}, 0 is ignored.
         * @return this builder.
         */
        public Builder addCustomStyle(final Class<? extends TextView> styleClass, final int styleResourceAttribute) {
            if (styleClass == null || styleResourceAttribute == 0) return this;
            mStyleClassMap.put(styleClass, styleResourceAttribute);
            return this;
        }

        /**
         * Register custom non-{@code TextView}'s which implement {@code setTypeface} so they can have the Typeface applied during inflation.
         */
        public Builder addCustomViewWithSetTypeface(Class<?> clazz) {
            customViewTypefaceSupport = true;
            mHasTypefaceClasses.add(clazz);
            return this;
        }

        public Builder setFontMapper(FontMapper fontMapper) {
            this.fontMapper = fontMapper;
            return this;
        }

        public CalligraphyConfig build() {
            this.isFontSet = !TextUtils.isEmpty(fontAssetPath);
            return new CalligraphyConfig(this);
        }
    }
}
