package com.kpi.milenamalysheva.computernets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kpi.milenamalysheva.computernets.R;

import java.util.ArrayList;

import icepick.Icepick;
import icepick.State;

/**
 * Created by Ivan Prymak on 7/11/2016.
 * View to preview N of the IP addresses
 */
public class IpListPreviewLayout extends LinearLayout {
    private static final String TAG = "IpListPreviewLayout";
    @State int n;
    @State ArrayList<Long> ips;

    public IpListPreviewLayout(Context context) {
        this(context, null);
    }

    public IpListPreviewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IpListPreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IpListPreviewLayout);
        n = a.getInt(R.styleable.IpListPreviewLayout_max_preview, 0);
        a.recycle();
        init();
    }

    private void init() {
        for (int i = 0; i < n; i++) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            IpEditView child = new IpEditView(getContext());
            child.setVisibility(GONE);
            child.setEditable(false);
            addView(child, params);
        }
    }

    public ArrayList<Long> getIps() {
        return ips;
    }

    public void setIps(ArrayList<Long> ips) {
        this.ips = ips;
        if (ips.size() > n) {
            Log.d(TAG, String.format("sent more ips than we can show(max:%d)", n));
            for (int i = 0; i < n; i++) {
                IpEditView child = (IpEditView) getChildAt(i);
                child.setAddress(ips.get(i));
                child.setVisibility(VISIBLE);
            }
        } else {
            for (int i = 0; i < ips.size(); i++) {
                IpEditView child = (IpEditView) getChildAt(i);
                child.setAddress(ips.get(i));
                child.setVisibility(VISIBLE);
                //child.invalidate();
                //child.requestLayout();
            }
            for (int i = ips.size(); i < n; i++) {
                getChildAt(i).setVisibility(GONE);
            }
        }

    }

    @Override protected Parcelable onSaveInstanceState() {
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
    }
}
