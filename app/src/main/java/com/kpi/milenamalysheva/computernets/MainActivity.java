package com.kpi.milenamalysheva.computernets;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.TextView;

import com.kpi.milenamalysheva.computernets.presenter.MainPresenter;
import com.kpi.milenamalysheva.computernets.validator.BoundaryNumericValidator;
import com.kpi.milenamalysheva.computernets.view.IpEditView;
import com.kpi.milenamalysheva.computernets.view.IpListPreviewLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends NucleusAppCompatActivity<MainPresenter> {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.ip_endpoint) IpEditView ipEndpoint;
    @BindView(R.id.subnet_amount) EditText subnetAmount;
    @BindView(R.id.subnet_index) EditText subnetIndex;
    @BindView(R.id.subnet_nodes_amount) EditText subnetNodesAmount;
    @BindView(R.id.net_type) TextView netType;
    @BindView(R.id.mask) IpEditView mask;
    @BindView(R.id.prefix) TextView prefix;
    @BindView(R.id.max_subnets) TextView maxSubnets;
    @BindView(R.id.max_hosts) TextView maxHosts;
    @BindView(R.id.swipe_refresh_wrapper) SwipeRefreshLayout swipeWrapper;
    @BindView(R.id.subnet_preview) IpListPreviewLayout subnetPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);
        subnetAmount.addTextChangedListener(new BoundaryNumericValidator(2,0xffff));
        subnetIndex.addTextChangedListener(new BoundaryNumericValidator(1, 0xffff-1));
        subnetNodesAmount.addTextChangedListener(new BoundaryNumericValidator(2,0xffff));
        swipeWrapper.setOnRefreshListener(()-> getPresenter().calculate(ipEndpoint.getAddress(),
                Integer.valueOf(subnetAmount.getText().toString()),
                Integer.valueOf(subnetIndex.getText().toString()),
                Integer.valueOf(subnetNodesAmount.getText().toString())
                ));
    }

    @SuppressWarnings("unused")
    @OnCheckedChanged(R.id.address_mode) void changeAddressMode(boolean checked){
        getPresenter().changeAddressMode(checked);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.subnet_preview) void showAllAddress(){
        getPresenter().showAllSubnets(this);
    }

    public void showError(String msg){
        Snackbar.make(getWindow().getDecorView().getRootView(), msg, Snackbar.LENGTH_LONG).show();
    }

    @SuppressLint("DefaultLocale") public void showMask(long mask, int prefix){
        this.mask.setAddress(mask);
        this.prefix.setText(String.format("/%d", prefix));
    }

    public void showSubnetsPreview(ArrayList<Long> ips){
        subnetPreview.setIps(ips);
    }

    public void showNetType(int type){
        netType.setText(getResources().getStringArray(R.array.net_type)[type]);
    }

    @SuppressLint("SetTextI18n") public void showMaxes(int maxSubnets, int maxHosts){
        this.maxSubnets.setText(Integer.toString(maxSubnets));
        this.maxHosts.setText(Integer.toString(maxHosts));
    }

    public void dismissRefresh() {
        swipeWrapper.setRefreshing(false);
    }
}
