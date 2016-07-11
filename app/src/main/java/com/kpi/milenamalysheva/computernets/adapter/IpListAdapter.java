package com.kpi.milenamalysheva.computernets.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kpi.milenamalysheva.computernets.R;
import com.kpi.milenamalysheva.computernets.view.IpEditView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by IvanPrymak on 7/11/2016.
 */
public class IpListAdapter extends BaseAdapter {

    private Long[] ips;
    private int prefix;

    public IpListAdapter(Long[] ips, int prefix){
        this.ips = ips;
        this.prefix = prefix;
    }
    @Override public int getCount() {
        return ips.length;
    }

    @Override public Object getItem(int position) {
        return ips[position];
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        Context context = parent.getContext();
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.ip_list_item, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.ip.setAddress(ips[position]);
        vh.prefix.setText(String.format("/%d", prefix));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.ip) IpEditView ip;
        @BindView(R.id.prefix) TextView prefix;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
