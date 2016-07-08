package com.kpi.milenamalysheva.computernets.presenter;

import android.os.Bundle;

import com.kpi.milenamalysheva.computernets.MainActivity;
import com.kpi.milenamalysheva.computernets.model.Calculator;
import com.kpi.milenamalysheva.computernets.model.InputController;

import icepick.State;
import nucleus.presenter.RxPresenter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Ivan Prymak on 7/6/2016.
 * Presenter for {@link MainActivity}
 */
public class MainPresenter extends RxPresenter<MainActivity> implements InputController {
    @State long ipEndpoint;
    @State int subnetsAmount;
    @State int subnetIndex;
    @State int subnetNodeAmount;
    @State boolean isCisco;

    @Override protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(
                1,
                () -> Observable.fromCallable(() -> {
                    Calculator calculator = new Calculator(this);
                    calculator.calculate();
                    return calculator;
                }).subscribeOn(AndroidSchedulers.mainThread()),
                (view, calculator) -> {
                    view.showMask(calculator.getMask(), calculator.getPrefixSub());
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
        start(1);
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
