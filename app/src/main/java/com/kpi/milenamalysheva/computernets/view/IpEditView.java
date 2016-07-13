package com.kpi.milenamalysheva.computernets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kpi.milenamalysheva.computernets.R;
import com.kpi.milenamalysheva.computernets.validator.BoundaryNumericValidator;

import java.nio.ByteBuffer;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;

/**
 * Created by Ivan Prymak on 7/3/2016.
 * Represents editable view for IP address in standard decimal notation to access and format IP
 * address from/to long (as we don't have uint here)
 */
public class IpEditView extends LinearLayout {
    @BindViews({R.id.byte_3, R.id.byte_2, R.id.byte_1, R.id.byte_0}) List<EditText> byteViews;
    @State long address;
    private boolean editable;

    public IpEditView(Context context) {
        this(context, null);
    }

    public IpEditView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IpEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IpEditView);
        editable = a.getBoolean(R.styleable.IpEditView_editable, true);
        a.recycle();
        init(editable);
    }

    private void init(boolean editable) {
        inflate(getContext(), R.layout.ip_editview_layout, this);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        ButterKnife.bind(this);
        for (EditText byteView : byteViews) {
            byteView.setClickable(editable);
            byteView.setFocusableInTouchMode(editable);
            byteView.setFocusable(editable);
            byteView.addTextChangedListener(new BoundaryNumericValidator(0, 255));
        }
    }

    public long getAddress() {
        long address = 0;
        for (EditText byteView : byteViews) {
            int bytedata;
            bytedata = 0;
            try {
                bytedata = Integer.valueOf(byteView.getText().toString());
            } catch (NumberFormatException ignored) {
            }
            address = address << 8 | bytedata;
        }
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
        byte[] bytes = ByteBuffer.allocate(8).putLong(address).array();
        for (int i = 0; i < 4; i++) {
            byteViews.get(i).setText(String.valueOf(bytes[i + 4] & 0xFF));
        }
    }

    @Override protected Parcelable onSaveInstanceState() {
        address = getAddress();
        return Icepick.saveInstanceState(this, super.onSaveInstanceState());
    }

    @Override protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(Icepick.restoreInstanceState(this, state));
        setAddress(address);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        for (EditText byteView : byteViews) {
            byteView.setClickable(editable);
            byteView.setFocusableInTouchMode(editable);
            byteView.setFocusable(editable);
            if (!editable) {
                byteView.setMovementMethod(null);
                byteView.setKeyListener(null);
            }
        }
    }

    @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
