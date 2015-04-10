import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class Predictor{
    private ArrayList<NeuralNetwork> myVoters;
    private ArrayList<Double> inputs;
    
    public static void main(String[] args){
        Predictor myPredictor = new Predictor(args);
    }
    
    public Predictor(String[] args){
        try{
            InputStream file = new FileInputStream("BBBPredictor.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
            myVoters = (ArrayList<NeuralNetwork>)input.readObject();
        }
        catch(ClassNotFoundException ex){System.out.println("The class wasn't found!");}
        catch(IOException ex){System.out.println("There was an error!");}
        
        //Need to modify BCParser to save max and min for each input into a text file
        
        //Need to read in properties from arguments
        IOTuple smilesInput = createIO(args);
        
        //Need to predict passage through BBB and print "yes" or "no" to the screen
        if (predict(smilesInput)){
            System.out.println("yes");
        } else{
            System.out.println("no");
        }
    }
    
    private IOTuple createIO(String[] inputs){
        Input currentInput = processInput(inputs);
        Output currentOutput = new Output(null);
        IOTuple currentIO = new IOTuple(currentInput, currentOutput);
        
        return currentIO;
    }
    
    private Input processInput(String[] smilesInputs){
        inputs = new ArrayList<Double>();
        
        for (String input : smilesInputs){
            inputs.add(Double.parseDouble(input));
        }
        
        Input currentInput = new Input(inputs);
        normalizeInput(currentInput);
        
        return currentInput;
    }
    
    private void normalizeInput(Input currentInput){
        ArrayList<Double> inputMax = new ArrayList<Double>();
        ArrayList<Double> inputMin = new ArrayList<Double>();
        String delimiter = " ";
        String lineIn;
        
        try{
            BufferedReader br = new BufferedReader(new FileReader("inputMaxMin.txt"));
            while((lineIn = br.readLine()) != null){
                String[] splitMaxMin = lineIn.split(delimiter);
                inputMin.add(Double.parseDouble(splitMaxMin[0]));
                inputMax.add(Double.parseDouble(splitMaxMin[1]));
            }
        } catch (FileNotFoundException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        
        for (int i = 0; i < inputMax.size(); i++){
            currentInput.normalizeValue(inputMin.get(i), inputMax.get(i), i);
        }
    }
    
    private boolean predict(IOTuple query){
        int yes = 0; 
        int no = 0;
        
        for (NeuralNetwork currentNN : myVoters){
            currentNN.newIO(query);
            currentNN.feedForward();
            double calculated = currentNN.getCalculatedValue();
            
            if (calculated >= .5){
                yes++;
            } else{
                no++;
            }
        }
        
        if (yes > no){
            return true;
        } else{
            return false;
        }
    }
}
