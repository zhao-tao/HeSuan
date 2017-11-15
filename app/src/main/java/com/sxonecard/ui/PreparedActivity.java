package com.sxonecard.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sxonecard.R;
import com.sxonecard.base.RxBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pc on 2017-10-26.
 */

public class PreparedActivity extends FragmentActivity {
    private static String TAG = "PreparedActivity";

    @Bind(R.id.start_check)
    Button startCheck;
    @Bind(R.id.result_query)
    Button resultQuery;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prepared);
        ButterKnife.bind(this);

        startCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"starting checking...");
                startActivity(new Intent(getApplicationContext(), HeSuanActivity.class));
            }
        });

        resultQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"query result...");
                Toast.makeText(getApplicationContext(), "暂未开通", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
