package com.kpi.milenamalysheva.computernets.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kpi.milenamalysheva.computernets.R;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by Ivan Prymak on 7/3/2016.
 */
public class IpEditView extends LinearLayout{
    @BindViews({R.id.byte_0, R.id.byte_1,  R.id.byte_2,  R.id.byte_3}) List<EditText> byteViews;
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

    private void init(){
        inflate(getContext(), R.layout.ip_editview_layout, this);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        ButterKnife.bind(this);
        for(EditText byteView: byteViews){
            byteView.addTextChangedListener(new TextWatcher() {
                CharSequence mBeforeChange;
                int start;
                int end;
                int oldValue;
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mBeforeChange = s.subSequence(start, start + count);
                    this.start = start;
                    this.end = start+after;
                    }

                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override public void afterTextChanged(Editable s) {
                    if(s.length()>0) {
                        int byteValue = Integer.parseInt(s.toString());
                        if (byteValue > 255 || byteValue < 0)
                            s.replace(start, end, mBeforeChange);
                        else if(oldValue!=byteValue){
                            oldValue = byteValue;
                            s.replace(0, s.length(), String.valueOf(byteValue));
                        }
                    } else{
                        s.replace(0, 0, "0");
                    }
                }
            });
        }
    }
}
