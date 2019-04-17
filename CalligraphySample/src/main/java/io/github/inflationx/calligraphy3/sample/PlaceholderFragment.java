package io.github.inflationx.calligraphy3.sample;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.inflationx.calligraphy3.TypefaceUtils;
import java.io.File;
import java.util.Random;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    public static Fragment getInstance() {
        return new PlaceholderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        ViewStub stub = view.findViewById(R.id.stub);
        stub.inflate();

        ViewStub stubWithFontPath = view.findViewById(R.id.stub_with_font_path);
        stubWithFontPath.inflate();
    }

    @OnClick({R.id.button_bold})
    public void onClickBoldButton() {
        Toast.makeText(getActivity(), "Custom Typeface toast text", Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.button_default})
    public void onClickDefaultButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Custom Typeface Dialog");
        builder.setTitle("Sample Dialog");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * Demo showing usage of {@link TypefaceUtils#load(AssetManager, String)} with a custom
     * absolute path.
     */
    @SuppressLint("SetTextI18n")
    @OnClick({R.id.load_absolute})
    public void onLoadAbsolute() {
        // Pick a random system font
        File file = new File("/system/fonts/");
        File[] files = file.listFiles();
        String firstFilePath = files[new Random().nextInt(files.length)].getAbsolutePath();
        Typeface typeface = TypefaceUtils.load(getResources().getAssets(), firstFilePath);

        // Use it on a textview
        TextView textView = new TextView(requireContext());
        textView.setText("\nDemo of loading a font from an absolute path.\nLoaded from \"" + firstFilePath + "\"");
        textView.setTypeface(typeface);

        // Display it in the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(textView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
