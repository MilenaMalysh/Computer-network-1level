package com.kpi.milenamalysheva.computernets;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Ivan Prymak on 7/7/2016.
 * View to show address by list
 */
public class AddressListActivity extends NucleusAppCompatActivity {
    @BindView(R.id.toolbar) Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);
    }
}
