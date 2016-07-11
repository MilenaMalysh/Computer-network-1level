package com.kpi.milenamalysheva.computernets;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;


import com.kpi.milenamalysheva.computernets.adapter.IpListAdapter;
import com.kpi.milenamalysheva.computernets.presenter.AddressListPresenter;
import com.kpi.milenamalysheva.computernets.presenter.MainPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Ivan Prymak on 7/7/2016.
 * View to show address by list
 */
@RequiresPresenter(AddressListPresenter.class)
public class AddressListActivity extends NucleusAppCompatActivity<AddressListPresenter> {
    public static final String LIST_ITEMS = "list_items";
    public static final String TITLE = "title";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.address_list) ListView list;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra(TITLE));
        getPresenter().showList((Long[]) getIntent().getSerializableExtra(LIST_ITEMS));
    }

    public void setIps(Long[] ips) {
        list.setAdapter(new IpListAdapter(ips, 0));
    }

}
