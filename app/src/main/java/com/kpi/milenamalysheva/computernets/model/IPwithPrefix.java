package com.kpi.milenamalysheva.computernets.model;

import java.io.Serializable;

/**
 * Created by Ivan Prymak on 7/12/2016.
 * Model to bundle ip with prefix
 */
public class IPwithPrefix implements Serializable {
    final public long ip;
    final public int prefix;

    public IPwithPrefix(long ip, int prefix) {
        this.ip = ip;
        this.prefix = prefix;
    }
}
