package com;

public class IPin extends Device {

    private boolean inputValue;

    @Override
    public void setInput(boolean inputValue)
    {
        this.inputValue = inputValue;
        // complete this method by yourself
    }

    @Override
    public boolean getOutput()
    {
        return this.inputValue;
        // complete this method by yourself
    }
}