package com.kpi.milenamalysheva.computernets.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import icepick.Icepick;
import nucleus.presenter.RxPresenter;

/**
 * Created by Ivan Prymak on 7/13/2016.
 * This class wraps up saving of all @State annotated fields of Presenter to be sure that parameters
 * provided from activity are always up-to date
 */
public abstract class StateSavingPresenter<View> extends RxPresenter<View> {
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        Icepick.restoreInstanceState(this, savedState);
    }

    @Override
    protected void onSave(@NonNull Bundle state) {
        super.onSave(state);
        Icepick.saveInstanceState(this, state);
    }
}
