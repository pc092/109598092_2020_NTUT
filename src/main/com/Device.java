package com;

import java.util.Vector;

public class Device{

    //protected Vector<Device> iPins = new Vector<>();

    protected Vector<Device> iPins;

    public Device()
    {
        iPins = new Vector<>();
    }

    public void addInputPin(Device iPin)
    {
        iPins.add(iPin);
        // complete this method by yourself
    }

    public void setInput(boolean inputValue)
    {
        throw new RuntimeException("This device is not allowed to call setInput method");
        // complete this method by yourself
    }

    public boolean getOutput()
    {
        throw new RuntimeException("This device is not allowed to call getOutput method");
        // complete this method by yourself
    }
}