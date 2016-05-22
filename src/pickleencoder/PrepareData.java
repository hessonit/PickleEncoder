/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pickleencoder;
import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import org.apache.mahout.math.*;
import org.apache.mahout.math.jet.random.Normal;

/**
 *
 * @author Adam Laskowski
 */
public class PrepareData {
    
    public PickleEncoder gmmLoader = new PickleEncoder();
    public PickleEncoder statLoader = new PickleEncoder();
    
    class Pair{
        public Double[][] first;
        public Double[][] second;
        public Pair(Double[][] f, Double[][]s){
            first = f;
            second = s;
        }
    }
    
    class PreparedData{
        
        public ArrayList<Date> dates;
        public ArrayList<Integer> labels;
        public ArrayList<Double[]> gmm_hist_buy;
        public ArrayList<Double[]> gmm_hist_sell;
        public PreparedData(ArrayList<Date> d, ArrayList<Integer> l, ArrayList<Double[]> f, ArrayList<Double[]> s){
            dates = d;
            labels = l;
            gmm_hist_buy = f;
            gmm_hist_sell = s;
        }
    }
    
    public static void main(String[] args) {
//        System.out.println("Prepare Data for classification");
        PrepareData prepare = new PrepareData();
//        System.out.println("Loading...");
//        prepare.loadGmm("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.pkl");
//        prepare.loadStatistics("/home/adam/Pulpit/hdidm/OBS_Statistics.9061.pkl");
//        System.out.println("Loaded");
//        try
//          {
//             FileOutputStream fileOut =
//             new FileOutputStream("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.cer");
//             ObjectOutputStream out = new ObjectOutputStream(fileOut);
//             out.writeObject(prepare.gmmLoader.dates);
//             out.writeObject(prepare.gmmLoader.num1);
//             out.writeObject(prepare.gmmLoader.num2);
//             out.close();
//             fileOut.close();
//             System.out.printf("Serialized data is saved in /home/adam/Pulpit/hdidm/OBS_GMM.V2.9061");
//        }catch(IOException i) {
//              i.printStackTrace();
//        }
//        
//        try
//          {
//             FileOutputStream fileOut =
//             new FileOutputStream("/home/adam/Pulpit/hdidm/OBS_Statistics.9061.cer");
//             ObjectOutputStream out = new ObjectOutputStream(fileOut);
//             out.writeObject(prepare.statLoader.dates);
//             out.writeObject(prepare.statLoader.num1);
//             out.writeObject(prepare.statLoader.num2);
//             out.close();
//             fileOut.close();
//             System.out.printf("Serialized data is saved in /home/adam/Pulpit/hdidm/OBS_Statistics.9061.");
//        }catch(IOException i) {
//              i.printStackTrace();
//        }

/////////////////////// LOAD DATA FROM SER /////////////////

//      Date []gmm_dates = null;
//      Double [][] gmm_num1 = null;
//      Double [][] gmm_num2 = null;
//      Date []stat_dates = null;
//      Double [][] stat_num1 = null;
//      Double [][] stat_num2 = null;
//      try
//      {
//         FileInputStream fileIn = new FileInputStream("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.cer");
//         ObjectInputStream in = new ObjectInputStream(fileIn);
//         gmm_dates = (Date[]) in.readObject();
//         gmm_num1 = (Double[][]) in.readObject();
//         gmm_num2 = (Double[][]) in.readObject();
//         in.close();
//         fileIn.close();
//         fileIn = new FileInputStream("/home/adam/Pulpit/hdidm/OBS_Statistics.9061.cer");
//         in = new ObjectInputStream(fileIn);
//         stat_dates = (Date[]) in.readObject();
//         stat_num1 = (Double[][]) in.readObject();
//         stat_num2 = (Double[][]) in.readObject();
//         in.close();
//         fileIn.close();
//      }catch(Exception i){i.printStackTrace();}
//      System.out.println("DATA LOADED "+stat_num1[0][0]);
//      for(int i=1470;i<1510;i++){
//          System.out.println(stat_num1[i][5]);
//      }
//        Pair p = prepare.prepare_gmm_histograms(gmm_num1, -350.0, 350.0, 300);
//        for(int i=0;i<10;i++) System.out.println(gmm_dates[i]);
//        Date d = gmm_dates[0];
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(d);
//        cal.add(Calendar.MINUTE, 60);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//        String res = formatter.format(cal.getTime());
//        System.out.println(res);
//        System.out.println(cal.getTime());
        
//        for(int i=0;i<1;i++){
//            for(int j=120;j<130;j++){
//                System.out.println(j+" "+p.first[i][j] + " ");
//            }
//            System.out.println();
//        }

    
    ///////////////////// TESTS /////////////////
    
//    create_gmm_histogram(Double[] p, Double[] m, Double[] s, Double[] bin_edges)

//        Double []bin_e = {1.0, 2.0, 3.0, 4.0, 5.0};
//        Double []p = {1.0, 2.0, 1.0};
//        Double []m = {0.0, 1.0, 2.0};
//        Double []s = {1.0, 2.0, 1.0};
//        Double [] res = prepare.create_gmm_histogram(p, m, s, bin_e);
//        for(int i=0;i<Array.getLength(res);i++){
//            System.out.print(res[i] + " ");
//        }
//        DataFrame d = new DataFrame(gmm_dates, stat_num1, stat_num2, gmm_num2);
//        d.showInputData(1910, 1920);

       PreparedData pd = prepare.prepare_train_data_V2("/home/adam/Pulpit/hdidm/", "OBS_GMM.V2.9061.pkl", "OBS_Statistics.9061.pkl");
       for(int i=0;i<30;i++){
           System.out.print(i + ": ");
           System.out.print(pd.dates.get(i) + " ");
           System.out.print(pd.labels.get(i) + " ");
           System.out.print(pd.gmm_hist_buy.get(i)[0] + " ");
           System.out.print(pd.gmm_hist_sell.get(i)[0] + "\n");
       } 
       
       
    }
    
    public void loadGmm(String path){
        gmmLoader.loadData(path); 
    }
    
    public void loadStatistics(String path){
        statLoader.loadData(path);
    }
    
    public Double[] linspace(double left, double right, int amount){
        Double[] result = new Double[amount];
        double jump = (Math.abs(left) + Math.abs(right))/(1.0*amount-1.0);
        for(int i=0;i<amount;i++){
            result[i] = left + i*jump;
        }
        return result;
    }
    
    public Double[] create_gmm_histogram(Double[] p, Double[] m, Double[] s, Double[] bin_edges){

        int bins = Array.getLength(bin_edges);
        int size = Array.getLength(p);
//        hist = np.zeros(bin_edges.shape[0]+1)
        Double[] hist = new Double[bins+1];
        Double[] temp = new Double[bins+1];
//    hist[:-1] = (norm.cdf(bin_edges[np.newaxis].T, loc = m, scale = np.sqrt(s)) * p).sum(axis = 1)
        for(int i=0;i<bins;i++){
            temp[i] = 0.0;
//              System.out.println("i" + i);
            for(int j=0;j<size;j++){
                Normal n = new Normal(m[j],Math.sqrt(s[j]),null);
                temp[i] += n.cdf(bin_edges[i])*p[j];
            }
//            System.out.println(temp[i]);
//            norm[i] = Gaussian.cdf(bin_edges[i], m[i], Math.sqrt(s[i]));
        }

//    hist[-1] = 1.0
        temp[bins] = 1.0;
        hist[0] = temp[0];
//    hist[1:] = (hist[1:] - hist[:-1])
        for(int i=1;i<=bins;i++){
//            System.out.println(temp[i] + " - " + temp[i-1] + " = " + (temp[i] - temp[i-1]));
            hist[i] = temp[i] - temp[i-1];
        }
        return hist;
    }
    
    public Double[][] create_gmm_histograms(Double [][]p, Double [][]m, Double [][]s, double histogram_left_edge, double histogram_right_edge, int histogram_size){
//        N, K = p.shape
        int N = Array.getLength(p);
        int K = Array.getLength(p[0]);
//        bin_edges = np.linspace(histogram_left_edge, histogram_right_edge, histogram_size)
        Double[] bin_edges = linspace(histogram_left_edge, histogram_right_edge, histogram_size);
//        hist = np.zeros([N, bin_edges.shape[0]+1])
        Double[][] hist = new Double[N][histogram_size+1];
//        for n in xrange(N):
        for(int n=0;n<N;n++){
//          hist[n, :] = create_gmm_histogram(p[n, :], m[n, :], s[n, :], bin_edges)
            hist[n] = create_gmm_histogram(p[n], m[n], s[n], bin_edges);
        }
        return hist;
    }
    
    private Double[][] getColumns(Double[][] table, int begin, int end){
        int n = Array.getLength(table);
        Double [][] result = new Double[n][end-begin];
        for(int i=0;i<n;i++){
            for(int j=begin;j<end;j++){
                result[i][j-begin] = table[i][j];
            }
        }
        return result;
    }
    
    public Pair prepare_gmm_histograms(Double [][] order_book_gmms, double histogram_left_edge, double histogram_right_edge, int histogram_size){
        //K = np.floor(order_book_gmms.shape[1]/6).astype(np.int32)
        int K = Array.getLength(order_book_gmms[0]) / 6;
        //gmm_hist_buy = create_gmm_histograms(order_book_gmms[:, :K], order_book_gmms[:, K:2*K], order_book_gmms[:, 2*K:3*K], histogram_left_edge, histogram_right_edge, histogram_size)
        Double [][]gmm_hist_buy = create_gmm_histograms(getColumns(order_book_gmms, 0, K),
                                                        getColumns(order_book_gmms, K, 2*K),
                                                        getColumns(order_book_gmms, 2*K, 3*K),
                                                        histogram_left_edge,
                                                        histogram_right_edge,
                                                        histogram_size);
        
//    gmm_hist_sell = create_gmm_histograms(order_book_gmms[:, 3*K:4*K], order_book_gmms[:, 4*K:5*K], order_book_gmms[:, 5*K:], histogram_left_edge, histogram_right_edge, histogram_size)
        Double [][]gmm_hist_sell = create_gmm_histograms(getColumns(order_book_gmms, 3*K, 4*K),
                                                        getColumns(order_book_gmms, 4*K, 5*K),
                                                        getColumns(order_book_gmms, 5*K, Array.getLength(order_book_gmms[0])),
                                                        histogram_left_edge,
                                                        histogram_right_edge,
                                                        histogram_size);
        
    //    return gmm_hist_buy, gmm_hist_sell
        return new Pair(gmm_hist_buy, gmm_hist_sell);
    }
    public PreparedData prepare_train_data_V2(String directory, String gmm, String stat){
        
        System.out.println("LOADING DATA");
        //*
            Date []gmm_dates = null;
            Double [][] gmm_num1 = null;
            Double [][] gmm_num2 = null;
            Date []stat_dates = null;
            Double [][] stat_num1 = null;
            Double [][] stat_num2 = null;
            try
            {
               FileInputStream fileIn = new FileInputStream("/home/adam/Pulpit/hdidm/OBS_GMM.V2.9061.cer");
               ObjectInputStream in = new ObjectInputStream(fileIn);
               gmm_dates = (Date[]) in.readObject();
               gmm_num1 = (Double[][]) in.readObject();
               gmm_num2 = (Double[][]) in.readObject();
               in.close();
               fileIn.close();
               fileIn = new FileInputStream("/home/adam/Pulpit/hdidm/OBS_Statistics.9061.cer");
               in = new ObjectInputStream(fileIn);
               stat_dates = (Date[]) in.readObject();
               stat_num1 = (Double[][]) in.readObject();
               stat_num2 = (Double[][]) in.readObject();
               in.close();
               fileIn.close();
            }catch(Exception i){i.printStackTrace();}

            gmmLoader.dates = gmm_dates;
            gmmLoader.num1 = gmm_num1;
            gmmLoader.num2 = gmm_num2;
            statLoader.dates = stat_dates;
            statLoader.num1 = stat_num1;
            statLoader.num2 = stat_num2;

        /*/
        loadGmm(directory+gmm);
        loadStatistics(directory+stat);
        //*/
        System.out.println("PREPARING DF");
        DataFrame d = new DataFrame(gmmLoader.dates, statLoader.num1, statLoader.num2, gmmLoader.num2);
        System.out.println("PREPARING HISTOGRAMS");
        Pair p = prepare_gmm_histograms(gmmLoader.num1, -350.0, 350.0, 3000);
        System.out.println("FINISHING TOUCHES");
        ArrayList<Integer> selected_ids = new ArrayList<>();
        ArrayList<Date> date_time = new ArrayList<>();
        ArrayList<Integer> labels = new ArrayList<>();
        ArrayList<Double[]> gmm_hist_buy = new ArrayList<>();
        ArrayList<Double[]> gmm_hist_sell = new ArrayList<>();
        for(int i=0;i<d.size;i++){
            if(d.selected.get(i)){
//                if(selected_ids.isEmpty() || !Objects.equals(selected_ids.get(selected_ids.size()-1), d.ID.get(i))){
                    selected_ids.add(d.ID.get(i));
                    date_time.add(d.dates.get(d.ID.get(i)));
                    labels.add(d.labels.get(i));
                    gmm_hist_buy.add(p.first[d.ID.get(i)]);
                    gmm_hist_sell.add(p.second[d.ID.get(i)]);
                    
//                } 
            }
        }
        System.out.println("DONE");
        return new PreparedData(date_time, labels, gmm_hist_buy, gmm_hist_sell);
    }
    
    
    
    
    
    
}
