package com;

public class OPin extends Device {


    @Override
    public boolean getOutput()
    {
        return this.iPins.get(0).getOutput();
        // complete this method by yourself
    }
}