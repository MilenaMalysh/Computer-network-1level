package com.kpi.milenamalysheva.computernets.validator;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Ivan Prymak on 7/5/2016.
 * Accepts only numeric and empty input
 */
public class BoundaryNumericValidator implements TextWatcher {
    private int maxValue;
    private int minValue;
    CharSequence mBeforeChange;
    int start;
    int end;
    int oldValue;

    public BoundaryNumericValidator(int minValue, int maxValue) {
        if(minValue>maxValue)
            throw new IllegalArgumentException("maxValue should be greater than minValue");
        this.maxValue = maxValue;
        this.minValue = minValue;
    }


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
            if (byteValue > maxValue || byteValue < minValue)
                s.replace(start, end, mBeforeChange);
            else if(oldValue!=byteValue){
                oldValue = byteValue;
                s.replace(0, s.length(), String.valueOf(byteValue));
            }
        } else{
            s.replace(0, 0, String.valueOf(minValue));
        }
    }
}
