package com;

public class GateAND extends Device {

    @Override
    public boolean getOutput()
    {
        boolean ouputValue = this.iPins.get(0).getOutput();
        for(int i=0; i<iPins.size(); i++){
            ouputValue = ouputValue & this.iPins.get(i).getOutput();
        }
        return ouputValue;
        // complete this method by yourself
    }
}
