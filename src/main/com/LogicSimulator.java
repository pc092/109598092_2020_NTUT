package com;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LogicSimulator
{
    private Vector<Device> circuits;
    private Vector<Device> iPins;
    private Vector<Device> oPins;
    private List<String> lcfStringList;
    private Boolean loadFileExist;

    public LogicSimulator(){
        circuits = new Vector<>();
        iPins = new Vector<>();
        oPins = new Vector<>();
        lcfStringList = new ArrayList<>();
        loadFileExist = false;
    }

    public boolean load(String file1Path) throws IOException {
        FileReader fr = new FileReader(file1Path);
        BufferedReader br = new BufferedReader(fr);
        boolean loadResult = false;

        if(this.loadFileExist) {
            clearLoadFile();
        }

        while (br.ready()) {

            lcfStringList.add(br.readLine());
        }
        fr.close();


        if(detectLcfFormat(this.lcfStringList)) {

            constructDevice(this.lcfStringList);

            connectDevice(this.lcfStringList);

            this.loadFileExist = true;

            loadResult = true;
        }
        return loadResult;
    }

    public boolean detectLcfFormat(List<String> lcfData) {

        String iPinsAmount = lcfData.get(0);
        String gatesAmount = lcfData.get(1);

        if(!isPositiveInteger(iPinsAmount)) {
            System.out.println("iPinsAmount error");
            return false;
        }
        if(!isPositiveInteger(gatesAmount)) {
            System.out.println("gatesAmount error");
            return false;
        }
        //檢查設定gate數量與輸入數量是否一致
        if(lcfData.size()-2 != Integer.parseInt(gatesAmount)) {
            System.out.println("設定gate數量與輸入數量是不一致");
            return false;
        }


        for(int i=2; i<lcfData.size(); i++){
            List<String> circuitInfo = new ArrayList<>(Arrays.asList(lcfData.get(i).split(" ")));
            //gate type
            if(isPositiveInteger(circuitInfo.get(0))){
                int type = Integer.parseInt(circuitInfo.get(0));

                if(type<1 || type>3) {
                    System.out.println("gate type error");
                    return false;
                }
            } else {
                System.out.println("gate type is not number");
                return false;
            }

            for(int j=1; j<circuitInfo.size(); j++){
                String circuitIPin = circuitInfo.get(j);
                if(circuitIPin.equals("0")) {
                    //end with 0
                    if(j != circuitInfo.size()-1) {
                        System.out.println("0 is not EOF");
                        return false;
                    }
                } else if(circuitIPin.startsWith("-")) {
                    //circuitIPin.matches("^-\\d{1,2}$")
                    if(isPositiveInteger(circuitIPin.substring(circuitIPin.indexOf('-')+1))){
                        int num = Integer.parseInt(circuitIPin.substring(circuitIPin.indexOf('-')+1));

                        if(num<1 || num>16) {
                            System.out.println("circuitIPin error with '-' ");
                            return false;
                        }
                    }
                    else {
                        System.out.println("circuitIPin is not number");
                        return false;
                    }
                } else if(circuitIPin.contains(".")) {
                    //circuitIPin.matches("^\\d{1,4}\\.1$")
                    if(isPositiveInteger(circuitIPin.substring(0, circuitIPin.indexOf('.')))){
                        int num = Integer.parseInt(circuitIPin.substring(0, circuitIPin.indexOf('.')));

                        if(num<1 || num>1000) {
                            System.out.println("circuitIPin error with '.' ");
                            System.out.println("circuitIPin error");
                            return false;
                        }
                    }
                    else {
                        System.out.println("circuitIPin is not number");
                        return false;
                    }
                } else {
                    System.out.println("circuitIPin error");
                    return false;
                }
            }
        }

//        if(iPinsCount!=Integer.parseInt(iPinsAmount)) {
//            System.out.println("iPinsCount != iPinsAmount");
//            return false;
//        }

        return true;
    }

    private boolean isPositiveInteger(String iPinsAmount) {
        return Integer.parseInt(iPinsAmount) > 0;
    }

    public void constructDevice(List<String> lcfStringList){
        int iPinAmount = Integer.parseInt(lcfStringList.get(0));
        int circuitsAmount = Integer.parseInt(lcfStringList.get(1));

        for(int i=0; i<iPinAmount; i++){
            this.iPins.add(new IPin());
        }

        for(int i=0; i<circuitsAmount; i++){
            Device circuit;
            String circuitInfo = lcfStringList.get(i+2);
            char type = circuitInfo.charAt(0);
            switch (type) {
                case '1':
                    circuit = new GateAND();
                    break;
                case '2':
                    circuit = new GateOR();
                    break;
                case '3':
                    circuit = new GateNOT();
                    break;
                default:
                    throw new RuntimeException("constructDevice type error");
            }
            this.circuits.add(circuit);
        }
    }

    public void connectDevice(List<String> lcfStringList){

        int circuitsAmount = Integer.parseInt(lcfStringList.get(1));

        boolean[] oPinGateNum = new boolean[circuitsAmount];//true: oPinGate, false: otherGate

        //每個都可能是output gate
        for(int i=0; i<circuitsAmount; i++){
            oPinGateNum[i] = true;
        }

        for(int circuitsNum=0; circuitsNum<circuitsAmount; circuitsNum++){
            //??????? Device circuit 要幹嘛
            Device circuit = this.circuits.get(circuitsNum);
            String circuitInfo = lcfStringList.get(circuitsNum+2);
            List<String> circuitIPinsInfo = new ArrayList<>(Arrays.asList(circuitInfo.split(" ")));

            for(int circuitIPinsNum=1; circuitIPinsNum<circuitIPinsInfo.size(); circuitIPinsNum++){

                String iPinString = circuitIPinsInfo.get(circuitIPinsNum);

                if(iPinString.equals("0")) {
                    break;
                } else if(iPinString.contains("-")) {
                    //ex:取-2的2
                    int iPinNum = Integer.parseInt(iPinString
                            .substring(iPinString.indexOf('-')+1));

                    //第二個iPin的index=2-1
                    iPinNum = iPinNum - 1;

                    circuit.addInputPin(this.iPins.get(iPinNum));

                } else if(iPinString.contains(".")) {

                    //ex:取3.1的3
                    int circuitNum = Integer.parseInt(iPinString
                            .substring(0, iPinString.indexOf('.')));

                    //第三個gate的index=3-1
                    circuitNum = circuitNum - 1;

                    //此gate的output是別人的input所以不會是最終output gate
                    oPinGateNum[circuitNum] = false;

                    circuit.addInputPin(this.circuits.get(circuitNum));
                }
            }
        }

        for(int i=0; i<circuitsAmount; i++){
            if(oPinGateNum[i]) {
                this.oPins.add(new OPin());
                this.oPins.get(this.oPins.size()-1).addInputPin(this.circuits.get(i));
            }
        }
    }

    public String getSimulationString(Vector<Boolean> inputValues){
        StringBuilder stringBuilder = new StringBuilder();

        for(int i=0; i<inputValues.size(); i++){
            this.iPins.get(i).setInput(inputValues.get(i));
        }

        for(int i=0; i<inputValues.size(); i++){
            if(i<9){
                stringBuilder.append(inputValues.get(i)?"1":"0").append(" ");
            } else {
                stringBuilder.append(inputValues.get(i)?"1":"0").append("  ");;
            }
        }

        stringBuilder.append("|");
        for(int i=0; i<this.oPins.size(); i++){
            System.out.println(this.oPins.get(i));
            stringBuilder.append(" ").append(this.oPins.get(i).getOutput()?"1":"0");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public StringBuilder getTableHead(String title, int size){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(title).append("\n");

        for(int i=0; i<size; i++){
            if(i<9){
                stringBuilder.append("i").append(" ");
            } else {
                stringBuilder.append("i").append("  ");
            }
        }

        stringBuilder.append("|");
        for(int i=0; i<this.oPins.size(); i++){
            stringBuilder.append(" ").append("o");
        }
        stringBuilder.append("\n");

        for(int i=0; i<size; i++){
            if(i<9){
                stringBuilder.append(i+1).append(" ");
            } else {
                stringBuilder.append(i+1).append("  ");
            }
        }

        stringBuilder.append("|");
        for(int i=0; i<this.oPins.size(); i++){
            stringBuilder.append(" ").append(i+1);
        }
        stringBuilder.append("\n");

        for(int i=0; i<size; i++){
            if(i<9){
                stringBuilder.append("-").append("-");
            } else {
                stringBuilder.append("-").append("--");;
            }
        }

        stringBuilder.append("+");
        for(int i=0; i<this.oPins.size(); i++){
            stringBuilder.append("-").append("-");
        }
        stringBuilder.append("\n");

        return stringBuilder;
    }

    public String getSimulationResult(Vector<Boolean> inputValues) {
        String simulationResult;

        StringBuilder stringBuilder = getTableHead("Simulation Result:", this.iPins.size());

        stringBuilder.append(getSimulationString(inputValues));

        simulationResult = stringBuilder.toString();

        return simulationResult;
    }

    public String getTruthTable(){
        String truthTable;

        int binarySize = this.iPins.size();
        int binaryAmount = (int) Math.pow(2,binarySize);

        StringBuilder stringBuilder = getTableHead("Truth table:", this.iPins.size());

        for(int i=0; i<binaryAmount; i++){
            Vector<Boolean> inputValues = new Vector<>();

            String byteNumber = Integer.toBinaryString(i);

            while (byteNumber.length() < iPins.size()){
                byteNumber = "0" + byteNumber;
            }

            for(char byteChar : byteNumber.toCharArray()){
                inputValues.add(byteChar == '1');
            }

            stringBuilder.append(getSimulationString(inputValues));
        }

        truthTable = stringBuilder.toString();

        return truthTable;
    }

    public boolean getLoadFileExist() {
        return loadFileExist;
    }

    public Vector<Device> getoPins(){
        return this.oPins;
    }

    public Vector<Device> getiPins(){
        return this.iPins;
    }

    public Vector<Device> getCircuits(){
        return this.circuits;
    }

    public void clearLoadFile(){
        this.iPins.clear();
        this.oPins.clear();
        this.circuits.clear();
        this.lcfStringList.clear();
        this.loadFileExist = false;
    }

}