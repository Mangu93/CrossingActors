package com.mangu.crossingactors.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mangu.crossingactors.R;

public class UtilsFactory {
    public static final int START_MAIN_ACTIVITY_FROM_COMPARATION = 1982;

    public static Button createRetryButton(@NonNull Context context) {
        Button btn = new Button(context);
        btn.setText(R.string.retry_btn);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        btn.setLayoutParams(layoutParams);
        return btn;
    }
    public static Toast createZoomInToast(@NonNull Context context, boolean zoomIn) {
        if(zoomIn) {
            return Toast.makeText(context, context.getText(R.string.tap_to_in), Toast.LENGTH_SHORT);
        }
        return Toast.makeText(context, context.getText(R.string.tap_to_out), Toast.LENGTH_SHORT);

    }

}
