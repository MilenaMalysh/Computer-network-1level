package com.kpi.milenamalysheva.computernets.view;

import android.content.Context;
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

/**
 * Created by Ivan Prymak on 7/3/2016.
 * Represents editable view for IP address in standard decimal notation to access and format IP
 * address from/to long (as we don't have uint here)
 */
public class IpEditView extends LinearLayout {
    @BindViews({R.id.byte_3, R.id.byte_2, R.id.byte_1, R.id.byte_0})
    List<EditText> byteViews;

    public IpEditView(Context context) {
        super(context);
        init();
    }

    public IpEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IpEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ip_editview_layout, this);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        ButterKnife.bind(this);
        for (EditText byteView : byteViews) {
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
            } catch (NumberFormatException ignored){}
            address = address << 8 | bytedata;
        }
        return address;
    }

    public void setAddress(long address) {
        byte[] bytes = ByteBuffer.allocate(8).putLong(address).array();
        for (int i = 0; i < 4; i++) {
            byteViews.get(i).setText(String.valueOf(bytes[i+4]&0xFF));
        }
    }
}
