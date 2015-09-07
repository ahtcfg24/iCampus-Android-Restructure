package org.iflab.icampus.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;

import org.iflab.icampus.R;
import org.iflab.icampus.model.YellowPageDepartBranch;

import java.util.List;

/**
 * @date 2015/9/7
 * @time 23:41
 */
public class YellowPageDialog extends Dialog {
    private Button dialButton, insertButton, cancelButton;
    private int position;
    private Context activity;
    private List<YellowPageDepartBranch> yellowPageDepartBranchList;

    public YellowPageDialog(Context context, String title, int position) {
        super(context);
        activity = context;
        this.position = position;
        init(title);//初始化布局
        setListener();

    }


    private void init(String title) {
        setContentView(R.layout.dial_dialog);
        dialButton = (Button) findViewById(R.id.dial_button);
        insertButton = (Button) findViewById(R.id.insert_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        setTitle(title);//设置dialog标题
    }

    private void setListener() {
        dialButton.setOnClickListener(new ButtonClickListener());
        insertButton.setOnClickListener(new ButtonClickListener());
        insertButton.setOnClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dial_button:
                    Uri uri = Uri.parse("tel:" + yellowPageDepartBranchList.get(position).getTelephoneNumber());
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    activity.startActivity(intent);
                    hide();
                    break;
                case R.id.insert_button:
                    Intent intent1 = new Intent(Intent.ACTION_INSERT);
                    intent1.setType("vnd.android.cursor.dir/person");
                    intent1.setType("vnd.android.cursor.dir/contact");
                    intent1.setType("vnd.android.cursor.dir/raw_contact");
                    intent1.putExtra(ContactsContract.Intents.Insert.NAME, yellowPageDepartBranchList.get(position).getBranchName());
                    intent1.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
                    intent1.putExtra(ContactsContract.Intents.Insert.SECONDARY_PHONE, yellowPageDepartBranchList.get(position).getTelephoneNumber());
                    activity.startActivity(intent1);
                    hide();
                    break;
                case R.id.cancel_button:
                    hide();
                    break;
                default:
                    break;
            }
        }
    }

}
