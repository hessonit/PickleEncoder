/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pickleencoder;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
 
import org.python.core.PyDictionary;
import org.python.core.PyFile;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.modules.cPickle;

 
public class PickleEncoder {
    
    public Date []dates;
    public Double [][] num1;
    public Double [][] num2;
//    public static void main(String[] args) {
//        
//        PickleEncoder loader = new PickleEncoder();
//        HashMap<String, ArrayList<String>> loadCnt1 =  loader.getData("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.pkl");
//
//        Date []dates = loader.getDates(loadCnt1.get("1"));
//        System.out.println(dates[0]);
////        Double []d = loader.getLine("[[0.0544911426809919, 0.002044]");
////        System.out.println(d[1]);
//        
//        Double [][] num = loader.getNumbers(loadCnt1.get("2"));
//        System.out.println(num[0][0]);
//        System.out.println(num[0][1]);
//        System.out.println(num[1][0]);
//        System.out.println(num[1][1]);
////        System.out.println(loadCnt1.get("1").toString().substring(0, 30));
//        System.out.println(loadCnt1.get("2").toString().substring(0, 100));
////        System.out.println(loadCnt1.get("3").toString().substring(0, 30));
//        
////        HashMap<String, ArrayList<String>> loadCnt2 =loader.getIdToCountriesFileStream();
////        System.out.println(loadCnt2.toString().substring(0, 30));
//    }
    
    public void loadData(String path){
        HashMap<String, ArrayList<String>> loadCnt1 = getData(path);
        dates = getDates(loadCnt1.get("1"));
        num1 = getNumbers(loadCnt1.get("2"));
        num2 = getNumbers(loadCnt1.get("3"));
    }
    
    public HashMap<String, ArrayList<String>> getData(String path) {
        HashMap<String, ArrayList<String>> idToCountries = new HashMap<String, ArrayList<String>>();
        File f = new File(path);
//        System.out.println(f.length());
        BufferedReader bufR;
        StringBuilder strBuilder = new StringBuilder();
        try {

            bufR = new BufferedReader(new FileReader(f));
            String aLine;

            while (null != (aLine = bufR.readLine())) {
                strBuilder.append(aLine).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PyString pyStr = new PyString(strBuilder.toString());
        PyDictionary idToCountriesObj = null;
        try{
            idToCountriesObj =  (PyDictionary) cPickle.loads(pyStr);
        } catch(Exception e){
            e.printStackTrace();
        }
        ConcurrentMap<PyObject, PyObject> aMap = idToCountriesObj.getMap();
        for (Map.Entry<PyObject, PyObject> entry : aMap.entrySet()) {
            String appId = entry.getKey().toString();
            PyList countryIdList = (PyList) entry.getValue();
            List<String> countryList = (List<String>) countryIdList.subList(0, countryIdList.size());
            ArrayList<String> countryArrList = new ArrayList<String>(countryList);
            idToCountries.put(appId, countryArrList);
        }
        return idToCountries;
    }
    
    
    public Date[] getDates(ArrayList<String> input){
        String datetime = input.toString();
        int size = datetime.length();
        datetime = datetime.substring(1, size-1);
        size = datetime.length();
        
        String []dates = datetime.split(", ");
        size = Array.getLength(dates);
        Date []result = new Date[size];
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        for(int i=0;i<size;i++){
            try {
                result[i] = formatter.parse(dates[i]);
            } catch (ParseException ex) {
                Logger.getLogger(PickleEncoder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Double[] getLine(String line){
        line = line.replace("[", "").replace("]", "");
        String []numbers = line.split(", ");
        int size = Array.getLength(numbers);
        Double []num = new Double[size];
        for(int i=0;i<size;i++){
            num[i] = Double.valueOf(numbers[i]);
        }
        return num;
    }
    
    public Double[][] getNumbers(ArrayList<String> input){
        String data = input.toString();
        int size = data.length();
        int index = 0;
        ArrayList<Double[]> result = new ArrayList<>();
        while(index >= 0){
            index = data.indexOf("],");
            String line;
            if(index < 0){ 
                line = data;
                result.add(getLine(line));
            }
            else {
                line = data.substring(0, index);
                data = data.substring(index+2);
                result.add(getLine(line));
            }
        }
//        System.out.println(result.size());
        Double res[][] = result.toArray(new Double[0][0]);
        return res;
        
    }
//    public HashMap<String, ArrayList<String>> getIdToCountriesFileStream() {
//        HashMap<String, ArrayList<String>> idToCountries = new HashMap<String, ArrayList<String>>();
//        File f = new File("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.pkl");
//        InputStream fs = null;
//        try {
//            fs = new FileInputStream(f);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        }
//        PyFile pyStr = new PyFile(fs);
//        PyDictionary idToCountriesObj =  (PyDictionary) cPickle.load(pyStr);
//        ConcurrentMap<PyObject, PyObject> aMap = idToCountriesObj.getMap();
//        for (Map.Entry<PyObject, PyObject> entry : aMap.entrySet()) {
//            String appId = entry.getKey().toString();
//            PyList countryIdList = (PyList) entry.getValue();
//            List<String> countryList = (List<String>) countryIdList.subList(0, countryIdList.size());
//            ArrayList<String> countryArrList = new ArrayList<String>(countryList);
//            idToCountries.put(appId, countryArrList);
//        }
////        System.out.println(idToCountries.toString());
//        return idToCountries;
//    }
}
