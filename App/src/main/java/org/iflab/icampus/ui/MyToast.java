package org.iflab.icampus.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.iflab.icampus.R;
import org.iflab.icampus.application.MyApplication;

/**
 * 自定义的Toast类,直接建立对象就可以显示
 *
 * @date 2015/9/4
 * @time 20:11
 */
public class MyToast extends Toast {
    private TextView toastTextView;
    private View toastView;

    /**
     * 自定义显示文字的toast
     * @param toastString 要显示的文字
     */
    public MyToast(String toastString) {
        this(MyApplication.getContext(), toastString);
    }

    /**
     * 短时间显示的toast
     *
     * @param context     必须的上下文环境
     * @param toastString 要显示的文字
     */
    public MyToast(Context context, String toastString) {
        this(context, toastString, LENGTH_SHORT);
    }

    /**
     * 自定义显示时间的toast
     *
     * @param context     必须的上下文环境
     * @param toastString 要显示的文字
     * @param showTime    要显示的时间
     */
    public MyToast(Context context, String toastString, int showTime) {
        super(context);
        setDuration(showTime);
        initToast(context, toastString);
    }

    /**
     * 初始化toast
     *
     * @param context     必须的上下文环境
     * @param toastString 要显示的文字
     */
    private void initToast(Context context, String toastString) {
        toastView = LayoutInflater.from(context).inflate(R.layout.toast, null);//自定义布局
        toastTextView = (TextView) toastView.findViewById(R.id.toast_textView);
        toastTextView.setText(toastString);
        setView(toastView);
        show();
    }


}
