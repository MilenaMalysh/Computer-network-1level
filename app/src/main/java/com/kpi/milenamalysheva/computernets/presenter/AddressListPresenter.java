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
public class AddressListPresenter extends StateSavingPresenter<AddressListActivity> {

    public static final int SHOW_LIST = 1;
    @State Long[] ips;
    @State int prefix;

    @Override protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        restartableLatestCache(
                SHOW_LIST,
                ()-> Observable.just(ips),
                (activity, nullValue)-> activity.setIps(ips, prefix));
    }

    public void showList(Long[] ips, int prefix){
        this.ips = ips;
        this.prefix = prefix;
        start(SHOW_LIST);
    }
}
