package com.kpi.milenamalysheva.computernets.model;

import java.nio.ByteBuffer;
import java.util.*;


/**
 * Created by Milena on 30.06.2016.
 */
public class Calculator {
    private final int amountOfAddrHosts;
    private long adress;
    private long mask;
    private int amount;
    private int type, subnetNumb;
    private int maxSubnets;
    private int maxHosts;
    private BitSet subNets, hosts;
    private int prefixSub;
    private int offset;
    private ArrayList<Long> subnetsClassic;
    private ArrayList<Long> subnetsCisco;
    private ArrayList<Long> hostsClassic;
    private ArrayList<Long> hostsCisco;
    private long broadcastBySubnets;
    private long broadcastByAll;
    private ArrayList<Long> broadcastByCiscoHosts;
    private ArrayList<Long> broadcastByClassicHosts;


    public Calculator(InputController inputContr) {
        this.adress = inputContr.getAdress();
        this.amount = inputContr.getAmount();
        this.subnetNumb = inputContr.getSubnetNumb();
        this.amountOfAddrHosts = inputContr.getAmountOfAddrHosts();
    }

    public void calculate(){
        System.out.println("Original adress");

        printAppropriateFormat(adress);
        System.out.println("\n");

        if ((adress>>24>=1) && (adress>>24<=126)){
            type = 1;
        }
        else if ((adress>>24>=128) && (adress>>24<=191)){
            type = 2;
        }
        else{
            type = 3;
        }
        long shAddr = adress;
        offset = 32-type*8;
        while (((shAddr&0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00000001)==0)&&(offset !=0)){
            shAddr = shAddr>>>1;
            offset--;
        }

        int powerSub = (int)(Math.ceil(Math.log(amount+2) / Math.log(2)));
        maxSubnets = (int) Math.pow(2, powerSub) - 2;
        //System.out.format("Max. amount of subnets %d\n",maxSubnets);
        maxHosts = (int) Math.pow(2, ((4-type)*8 - offset - powerSub))- 2;
        //System.out.format("Max. amount of hosts %d\n",maxHosts);


        prefixSub = type*8+ powerSub+ offset;
        System.out.format("Prefix %d\n", prefixSub);
        mask = (long)(Math.pow(2, prefixSub)-1)<<((4-type)*8 - powerSub- offset);

        //System.out.println("Mask");
        printAppropriateFormat(mask);
        System.out.println("\n");


        subnetsClassic = classic(adress, type*8+offset, this.amount);
        System.out.print("Subnets by classic method");
        for (Long i: subnetsClassic){
            printAppropriateFormat(i);
        }

        System.out.print("Subnets by cisco method");
        subnetsCisco = cisco(adress, prefixSub, this.amount);
        for (Long i: subnetsCisco){
            printAppropriateFormat(i);
        }

        System.out.format("Subnet number %d \n", subnetNumb);
        System.out.format("Classic method  \n");
        printAppropriateFormat(subnetsClassic.get(subnetNumb-1));
        System.out.format("Cisco method \n");
        printAppropriateFormat(subnetsCisco.get(subnetNumb-1));

        hostsClassic = cisco(subnetsClassic.get(subnetNumb-1), 64, amountOfAddrHosts);
        /*System.out.print("Hosts by classic method");
        for (Long i: hostsClassic){
            printAppropriateFormat(i);
        }*/

        System.out.print("Hosts by cisco method");
        //HARDCODE
        hostsCisco = cisco(subnetsCisco.get(subnetNumb-1), 64, amountOfAddrHosts);
        /*for (Long i: hostsCisco){
            printAppropriateFormat(i);
        }*/

        broadcastBySubnets = adress|((long)(Math.pow(2, powerSub)-1)<<(32-8*type-powerSub- offset));
        System.out.format("Broadcast on subnets \n");
        printAppropriateFormat(broadcastBySubnets);



        System.out.format("Broadcast on hosts \n");

        System.out.print("By classic method");
        broadcastByClassicHosts = new ArrayList<>(subnetsClassic.size());
        for (Long i: subnetsClassic){
            long result = i|(long)(Math.pow(2,(32-8*type-powerSub- offset))-1);
            broadcastByClassicHosts.add(result);
            printAppropriateFormat(result);
        }

        System.out.print("By cisco method");
        broadcastByCiscoHosts = new ArrayList<>(subnetsCisco.size());
        for (Long i: subnetsCisco){
            long result = i|(long)(Math.pow(2,(32-8*type-powerSub- offset))-1);
            broadcastByCiscoHosts.add(result);
            printAppropriateFormat(result);
        }


        printAppropriateFormat(broadcastBySubnets);


        broadcastByAll = adress|((long)(Math.pow(2, 32-8*type- offset)-1));
        System.out.format("Broadcast on all \n");
        printAppropriateFormat(broadcastByAll);

    }

    public ArrayList<Long> getCisco() {
        return subnetsCisco;
    }

    public ArrayList<Long> getClassic() {
        return subnetsClassic;
    }

    public ArrayList<Long> getHostsClassic() {
        return hostsClassic;
    }

    public ArrayList<Long> getHostsCisco() {
        return hostsCisco;
    }

    public long getBroadcastBySubnets() {
        return broadcastBySubnets;
    }

    public ArrayList<Long> getBroadcastByCiscoHosts(){
        return broadcastByCiscoHosts;
    }

    public ArrayList<Long> getBroadcastByClassicHosts() {
        return broadcastByClassicHosts;
    }

    public long getBroadcastByAll() {
        return broadcastByAll;
    }

    public ArrayList<Long> classic (long mask, int begin, int amount){
        ArrayList<Long> result = new ArrayList<>();
        for (int i=1; i<amount+1; i++){
            long revI = Long.reverse(i);
            long maskModif = mask+ (revI>>>(32+begin));//HARDCODE!
            result.add(maskModif);
        }
        return result;
    }

    public ArrayList<Long> cisco (long mask, int end, int amount){
        ArrayList<Long> result = new ArrayList<>();
        for (int i=1; i<amount+1; i++){
            long maskModif = mask+ (i<<(32-end));
            result.add(maskModif);
        }
        return result;
    }

    public void printAppropriateFormat(long address){
        byte[] bytes = ByteBuffer.allocate(8).putLong(address).array();

        for (byte b : bytes) {

            System.out.format("%d. ",b&0xFF);
        }
        System.out.print("\n");
    }

    public long getMask() {
        return mask;
    }

    public int getPrefixSub() {
        return prefixSub;
    }

    public int getMaxSubnets() {
        return maxSubnets;
    }

    public int getMaxHosts() {
        return maxHosts;
    }

    public BitSet getSubNets() {
        return subNets;
    }

    public int getType() {
        return type;
    }

    public BitSet getHosts() {
        return hosts;
    }
}
