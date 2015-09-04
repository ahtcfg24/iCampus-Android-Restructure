package org.iflab.icampus.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import org.iflab.icampus.R;

/**
 * 自定义progressDialog
 *
 * @date 2015/9/3
 * @time 21:32
 */

public class MyProgressDialog extends Dialog {
    /**
     * @param context            要显示ProgressDialog的环境
     * @param progressDialogText 要显示的文字
     */
    public MyProgressDialog(Context context, String progressDialogText) {
        this(context, R.style.CustomProgressDialog, progressDialogText);
    }

    public MyProgressDialog(Context context, int theme, String progressDialogText) {
        super(context, theme);
        this.setContentView(R.layout.progress_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        TextView progressDialogTextView = (TextView) this.findViewById(R.id.progressDialog_textView);
        if (progressDialogTextView != null) {
            progressDialogTextView.setText(progressDialogText);
        }
        show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (! hasFocus) {
            dismiss();
        }
    }

}

