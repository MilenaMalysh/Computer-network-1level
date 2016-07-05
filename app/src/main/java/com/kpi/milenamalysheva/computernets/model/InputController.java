package com.kpi.milenamalysheva.computernets.model;

import java.util.Scanner;

/**
 * Created by Milena on 30.06.2016.
 */
public class InputController {
    private Scanner in;
    //private List<Short> adress;
    private long adress;
    private int amount;
    private int amountOfAddrSubn;
    private int subnetNumb;
    private int amountOfAddrHosts;


    public InputController() {
        in = new Scanner(System.in);
    }

    public void inputData(){

        //adress = Arrays.asList(in.nextShort(), in.nextShort(), in.nextShort(), in.nextShort());
        adress = in.nextLong();
        //Subnets amount
        amount = in.nextInt();
        //We should evaluate addresses of certain amount of subNets
        //amountOfAddrSubn = in.nextByte();
        //We should evaluate addresses of certain amount of hosts in certain subnet
        subnetNumb = in.nextInt();
        amountOfAddrHosts = in.nextInt();
    }

    public long getAdress() {
        return adress;
    }

    public int getAmount() {
        return amount;
    }

    public int getSubnetNumb() {
        return subnetNumb;
    }

    public int getAmountOfAddrHosts() {
        return amountOfAddrHosts;
    }
}
