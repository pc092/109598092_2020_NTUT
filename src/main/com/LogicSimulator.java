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
            while ((fileString = br.readLine()) != null){
                inputPinNumber = Integer.parseInt(br.readLine());
                gateNumber = Integer.parseInt(br.readLine());

                String[] splitData = br.readLine().split(" ");
                if (splitData[0] == "1"){
                    circuits.add(new GateAND());
                }else if (splitData[0] == "2"){
                    circuits.add(new GateOR());
                }else if(splitData[0] == "3"){
                    circuits.add(new GateNOT());
                }

                for(int i=1; i < splitData.length; i++){
                    if(splitData[i].startsWith("-")){

                    }else if(splitData[i].contains(".")){

                    }else if(splitData[i].equals("0")){
                        break;
                    }
                }
            }
        }
        fr.close();

        String[] step = null;
        List<String[]> gateList = new ArrayList<>();
        for (int i=2; i< fileLine.size(); i++){
            gateList.add(fileLine.get(i).split(" "));
        }
    }

    public String getSimulationResult(Vector<Boolean> inputValues) {
        return "getSimulationResult";
    }

    public String getTruthTable(){
        return  "getTruthTable";
    }
    // complete this class by yourself
}