package com.sxonecard.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.sxonecard.R;
import com.sxonecard.base.BaseFragment;

import butterknife.Bind;

/**
 * Created by pc on 2017-09-29.
 */

public class ConnException extends BaseFragment {

    @Bind(R.id.conexcp)
    TextView conExcp;
    @Override
    public int getLayoutId() {
        return R.layout.conn_excp;
    }

    @Override
    public void initView() {
        Bundle bundle = getArguments();
        if(bundle != null){
            String tip = bundle.getString("msg");
            if(!TextUtils.isEmpty(tip))
                conExcp.setText(tip);
        }
    }

    @Override
    public void loadData() {

    }
}
