package com.kpi.milenamalysheva.computernets.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.kpi.milenamalysheva.computernets.AddressListActivity;
import com.kpi.milenamalysheva.computernets.MainActivity;
import com.kpi.milenamalysheva.computernets.model.Calculator;
import com.kpi.milenamalysheva.computernets.model.InputController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import icepick.State;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Ivan Prymak on 7/6/2016.
 * Presenter for {@link MainActivity}
 */
public class MainPresenter extends RxPresenter<MainActivity> implements InputController {
    public static final int CALCULATE_ID = 1;
    @State long ipEndpoint;
    @State int subnetsAmount;
    @State int subnetIndex;
    @State int subnetNodeAmount;
    @State boolean isCisco;
    @State Long[] subnets;
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
                    ArrayList<Long> ips;
                    if(isCisco) {
                        ips = calculator.getCisco();
                    }else{
                        ips = calculator.getClassic();
                    }
                    subnets = new Long[ips.size()];
                    ips.toArray(subnets);
                    view.showSubnetsPreview(ips);
                    view.showNetType(calculator.getType());
                    view.showMaxes(calculator.getMaxSubnets(), calculator.getMaxHosts());
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
        Intent listAddressesIntent = new Intent(mainActivity, AddressListActivity.class);
        listAddressesIntent.putExtra(AddressListActivity.TITLE, "Subnets");
        listAddressesIntent.putExtra(AddressListActivity.LIST_ITEMS, subnets);
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
