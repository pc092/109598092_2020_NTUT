package com;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;

import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class TextUI {

    private LogicSimulator logicSimulator;


    public TextUI(){
        logicSimulator = new LogicSimulator();
    }

    public void displayMenu(){
        System.out.println("1. Load logic circuit file");
        System.out.println("2. Simulation");
        System.out.println("3. Display truth table");
        System.out.println("4. Exit");
        System.out.println("Command:");
    }

    public void processCommand() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String command = null;
        boolean isQuit = false;

        while (!isQuit){
            displayMenu();
            command = scanner.nextLine();
            switch (command){
                case "1":
                    System.out.println(("Please key in a file path:"));
                    command = scanner.nextLine();
                    if (logicSimulator.load(command)){
                        System.out.format(
                                "Circuit: %d input pins, %d output pins and %d gates\n"
                                ,this.logicSimulator.getiPins().size()
                                ,this.logicSimulator.getoPins().size()
                                ,this.logicSimulator.getCircuits().size());
                    }else {
                        System.out.println("File not found or file format error!!");
                    }
                    break;
                case "2":
                    Vector<Boolean> userInputValues = new Vector<>();
                    if(logicSimulator.getLoadFileExist()){
                        for (int i=0; i<this.logicSimulator.getiPins().size(); i++) {
                            System.out.format("Please key in the value of input pin "+ (i+1) +":");
                            command = scanner.nextLine();
                            while (!command.equals("0") && !command.equals("1")) {
                                System.out.println("The value of input pin must be 0/1");
                                System.out.format("Please key in the value of input pin "+ (i+1) +":");
                                command = scanner.nextLine();
                            }
                            //put user input into userInputValues
                            userInputValues.add(command.equals("1"));
                        }
                        System.out.println(logicSimulator.getSimulationResult(userInputValues));
                    } else {
                        System.out.println("Please load an lcf file, before using this operation.");
                    }
                    break;
                case "3":
                    if(logicSimulator.getLoadFileExist()){
                        System.out.println(logicSimulator.getTruthTable());
                    } else {
                        System.out.println("Please load an lcf file, before using this operation.");
                    }
                    break;
                case "4":
                    System.out.println("Goodbye, thanks for using LS.");
                    isQuit = true;
                    break;
                default:
                    System.out.println("Please give me a correct command!!!");
                    break;
            }
        }
    }
}
