package com.kpi.milenamalysheva.computernets.model;


public class Main {

    public static void main(String[] args) {
        InputController inputCont = new InputController();
        inputCont.inputData();

        Calculator calculator = new Calculator(inputCont);
        calculator.calculate();

    }
    //2264924160 16 6 5
    //3327066112 8 1 5
    //319488000 256 4 5

}
