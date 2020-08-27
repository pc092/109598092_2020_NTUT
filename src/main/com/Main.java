package com;

import java.io.IOException;

public class Main
{
    public static void main(String args[])
    {
        System.out.println("TEST");
        TextUI textUI = new TextUI();
        try {
            textUI.processCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
