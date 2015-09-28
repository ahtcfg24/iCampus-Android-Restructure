package org.iflab.icampus.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.iflab.icampus.R;
import org.iflab.icampus.model.YellowPageDepartBranch;

import java.util.List;

/**
 * 点击黄页中的号码后弹出的Dialog
 * @date 2015/9/7
 * @time 23:41
 */
public class YellowPageDialog extends Dialog {
    private Button dialButton, insertButton, cancelButton;
    private TextView titleTextVeiw,numTextView;
    private int position;
    private Context context;
    private List<YellowPageDepartBranch> yellowPageDepartBranchList;

    public YellowPageDialog(Context context, String title,String num
            , List<YellowPageDepartBranch> yellowPageDepartBranchList
            , int position) {
        super(context,R.style.YellowPageDialog);
        this.context = context;
        this.position = position;
        this.yellowPageDepartBranchList=yellowPageDepartBranchList;
        init(title,num);//初始化布局
        setListener();//设置监听器
        show();//显示Dialog
    }


    private void init(String title,String num) {
        setContentView(R.layout.dial_dialog);
        dialButton = (Button) findViewById(R.id.dial_button);
        insertButton = (Button) findViewById(R.id.insert_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        titleTextVeiw=(TextView)findViewById(R.id.dialog_title_textView);
        numTextView = (TextView)findViewById(R.id.dialog_num_textView);
        titleTextVeiw.setText(title);
        numTextView.setText(num);
    }

    private void setListener() {
        dialButton.setOnClickListener(new ButtonClickListener());
        insertButton.setOnClickListener(new ButtonClickListener());
        cancelButton.setOnClickListener(new ButtonClickListener());
    }

    /**
     * 监听器，监听Dialog的三个按钮
     */
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dial_button://拨号
                    Uri uri = Uri.parse("tel:" + yellowPageDepartBranchList.get(position).getTelephoneNumber());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    context.startActivity(intent);
                    hide();
                    break;
                case R.id.insert_button://加入通讯录
                    Intent intent1 = new Intent(Intent.ACTION_INSERT);
                    intent1.setType("vnd.android.cursor.dir/person");
                    intent1.setType("vnd.android.cursor.dir/contact");
                    intent1.setType("vnd.android.cursor.dir/raw_contact");
                    intent1.putExtra(ContactsContract.Intents.Insert.NAME, yellowPageDepartBranchList.get(position).getBranchName());
                    intent1.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                    intent1.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, yellowPageDepartBranchList.get(position).getTelephoneNumber());
                    context.startActivity(intent1);
                    dismiss();
                    break;
                case R.id.cancel_button://取消
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }

}
