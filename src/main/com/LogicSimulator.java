package com;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class LogicSimulator
{
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;

    public LogicSimulator(){
        circuits = new Vector<Device>();
        iPins = new Vector<Device>();
        oPins = new Vector<Device>();
    }




    public void load(String file1Path) throws IOException {
        FileReader fr = new FileReader(file1Path);
        BufferedReader br = new BufferedReader(fr);
        String fileString = null;
        int inputPinNumber, gateNumber;
        Vector<String>  fileLine = new Vector<>();

        while (br.ready()) {

            inputPinNumber = Integer.parseInt(br.readLine());
            gateNumber = Integer.parseInt(br.readLine());

            System.out.println(inputPinNumber);
            System.out.println(gateNumber);

            createIPinsAndGate(inputPinNumber, gateNumber);

            while ((fileString = br.readLine()) != null){
                System.out.println(fileString);
                String[] splitData = fileString.split(" ");
                if (splitData[0].equals("1")){
                    circuits.add(new GateAND());
                }else if (splitData[0].equals("2")){
                    circuits.add(new GateOR());
                }else if(splitData[0].equals("3")){
                    circuits.add(new GateNOT());
                }else {

                }

                System.out.println("size="+circuits.size());

                for(int i=1; i < splitData.length; i++){
                    if(splitData[i].startsWith("-")){
                        System.out.println("-");
                        circuits.get(circuits.size()-1).addInputPin(iPins.get(Integer.parseInt(splitData[i].substring(1))-1));
                    }else if(splitData[i].contains(".")){
                        System.out.println(".");
                        circuits.get(circuits.size()-1).addInputPin(oPins.get(Integer.parseInt(splitData[i].substring(0,1))-1));
                    }else if(splitData[i].equals("0")){
                        System.out.println("0");
                        break;
                    }
                }
            }
        }
        fr.close();
    }

    private void createIPinsAndGate(int inputPinNumber, int gateNumber){
        for(int i=0 ; i<inputPinNumber ; i++){
            this.iPins.add(new IPin());
        }
        for(int i=0 ; i<gateNumber ; i++){
            this.oPins.add(new OPin());
            //this.oPinUsedTimes.add(0);
        }
    }

    public String getSimulationResult(Vector<Boolean> inputValues) {
        for(int i=0 ; i<inputValues.size() ; i++){
            iPins.get(i).setInput(inputValues.get(i));
        }
        StringBuilder res = new StringBuilder();
        res.append("Simulation Result:\n");
        for(int i=0; i<inputValues.size(); i++){
            res.append("i ");
        }
        res.append("| o\n");
        for(int i=0; i<inputValues.size(); i++){
            res.append(i+1 + " ");
        }
        res.append("| 1\n");
        for(int i=0; i<inputValues.size(); i++){
            res.append("--");
        }
        res.append("+--\n");
        for(int i=0; i<inputValues.size(); i++){
            res.append(inputValues.get(i) ? 1:0);
            res.append(" ");
        }
        res.append("| ");
        res.append("0\n");
        //res.append(oPins.get(0).getOutput()+" ");

        return res.toString();
    }

    public String getTruthTable(){
        int inputPinNumber = 3;
        StringBuilder res = new StringBuilder();
        res.append("Truth table:\n");
        for(int i=0; i<iPins.size(); i++){
            res.append("i ");
        }
        res.append("| o\n");
        for(int i=0; i<iPins.size(); i++){
            res.append(i+1 + " ");
        }
        res.append("| 1\n");
        for(int i=0; i<iPins.size(); i++){
            res.append("--");
        }
        res.append("+--\n");
        

        return  res.toString();
    }
    // complete this class by yourself
}