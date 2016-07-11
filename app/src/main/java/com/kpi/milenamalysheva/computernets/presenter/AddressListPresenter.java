package com.kpi.milenamalysheva.computernets.presenter;


import android.os.Bundle;

import com.kpi.milenamalysheva.computernets.AddressListActivity;


import icepick.Icepick;
import icepick.State;
import nucleus.presenter.RxPresenter;
import rx.Observable;

/**
 * Created by Ivan Prymak on 7/6/2016.
 * Presenter for {@link AddressListActivity}
 */
public class AddressListPresenter extends RxPresenter<AddressListActivity> {

    public static final int SHOW_LIST = 1;
    @State Long[] ips;

    @Override protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(
                SHOW_LIST,
                ()-> Observable.just(ips),
                (activity, nullValue)-> activity.setIps(ips));
    }

    public void showList(Long[] ips){
        this.ips = ips;
        start(SHOW_LIST);
    }
}
