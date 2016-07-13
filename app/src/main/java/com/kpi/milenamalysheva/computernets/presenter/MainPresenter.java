package com.kpi.milenamalysheva.computernets.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.kpi.milenamalysheva.computernets.AddressListActivity;
import com.kpi.milenamalysheva.computernets.MainActivity;
import com.kpi.milenamalysheva.computernets.model.Calculator;
import com.kpi.milenamalysheva.computernets.model.InputController;

import java.util.ArrayList;

import icepick.State;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ivan Prymak on 7/6/2016.
 * Presenter for {@link MainActivity}
 */
public class MainPresenter extends StateSavingPresenter<MainActivity> implements InputController {
    public static final int CALCULATE_ID = 1;
    @State long ipEndpoint;
    @State int subnetsAmount;
    @State int subnetIndex;
    @State int subnetNodeAmount;
    @State boolean isCisco;
    @State Long[] subnets;
    @State Long[] hosts;
    @State Long[] broadcasts;
    @State int prefix;

    @Override protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(
                CALCULATE_ID,
                () -> Observable.fromCallable(() -> {
                    Calculator calculator = new Calculator(this);
                    calculator.calculate();
                    return calculator;
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()),
                (view, calculator) -> {
                    view.showMask(calculator.getMask(), calculator.getPrefixSub());
                    prefix = calculator.getPrefixSub();
                    view.showNetType(calculator.getType());
                    view.showMaxes(calculator.getMaxSubnets(), calculator.getMaxHosts());
                    ArrayList<Long> subnets;
                    ArrayList<Long> hosts;
                    ArrayList<Long> broadcasts;
                    if(isCisco) {
                        subnets = calculator.getCisco();
                        hosts = calculator.getHostsCisco();
                        broadcasts = calculator.getBroadcastByCiscoHosts();
                    }else{
                        subnets = calculator.getClassic();
                        hosts = calculator.getHostsClassic();
                        broadcasts = calculator.getBroadcastByClassicHosts();
                    }
                    this.subnets = new Long[subnets.size()];
                    subnets.toArray(this.subnets);
                    this.hosts = new Long[hosts.size()];
                    hosts.toArray(this.hosts);
                    this.broadcasts = new Long[broadcasts.size()];
                    broadcasts.toArray(this.broadcasts);
                    view.showSubnetsPreview(subnets);
                    view.showHostsPreview(hosts);
                    view.showBroadcasts(calculator.getBroadcastByAll(),
                            calculator.getBroadcastBySubnets(),
                            prefix,
                            broadcasts);
                    view.dismissRefresh();
                },
                (view, e) -> {
                    e.printStackTrace();
                    view.showError("Calculation error");
                    view.dismissRefresh();
                }
        );
    }

    public void calculate(long ipEndpoint, int subnetsAmount, int subnetIndex, int subnetNodeAmount) {
        this.ipEndpoint = ipEndpoint;
        this.subnetsAmount = subnetsAmount;
        this.subnetIndex = subnetIndex;
        this.subnetNodeAmount = subnetNodeAmount;
        start(CALCULATE_ID);
    }

    public void showAllSubnets(MainActivity mainActivity) {
        startListActivity(mainActivity, subnets, prefix, "Subnets");
    }

    public void showAllHosts(MainActivity mainActivity) {
        startListActivity(mainActivity, hosts, prefix, "Hosts of "+subnetIndex+" subnet");
    }

    public void showAllBroadcastsByHosts(MainActivity mainActivity) {
        startListActivity(mainActivity, broadcasts, prefix, "Broadcasts of "+subnetIndex+" subnet");
    }


    private void startListActivity(MainActivity mainActivity, Long[] subnets, int prefix, String title) {
        Intent listAddressesIntent = new Intent(mainActivity, AddressListActivity.class);
        listAddressesIntent.putExtra(AddressListActivity.TITLE, title);
        listAddressesIntent.putExtra(AddressListActivity.LIST_ITEMS, subnets);
        listAddressesIntent.putExtra(AddressListActivity.PREFIX, prefix);
        mainActivity.startActivity(listAddressesIntent);
    }

    public void changeAddressMode(boolean isCisco) {
        this.isCisco = isCisco;
    }

    @Override public long getAdress() {
        return ipEndpoint;
    }

    @Override public int getAmount() {
        return subnetsAmount;
    }

    @Override public int getSubnetNumb() {
        return subnetIndex;
    }

    @Override public int getAmountOfAddrHosts() {
        return subnetNodeAmount;
    }
}
