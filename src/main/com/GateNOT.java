package com;

public class GateNOT extends Device {
    @Override
    public boolean getOutput()
    {
        return !this.iPins.get(0).getOutput();
        // complete this method by yourself
    }
}
