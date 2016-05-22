/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pickleencoder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Adam Laskowski
 */
public class DataFrame{
        public ArrayList<Date> dates;
        public ArrayList<Double> buy_price;
        public ArrayList<Double> sell_price;
        public ArrayList<Double> mean_price;
        public ArrayList<Double> buy_gmm_error;
        public ArrayList<Double> sell_gmm_error;
        public ArrayList<Boolean> selected;
        public ArrayList<Integer> ID;
        
        public ArrayList<Double> roll_min;
        public ArrayList<Double> roll_max;
        public ArrayList<Double> returns_min;
        public ArrayList<Double> returns_max;
        public ArrayList<Integer> labels;
        
        public int size;
        
        public DataFrame(Date[] date_time, 
                Double[][] buy_orders_statistics, 
                Double[][] sell_orders_statistics, 
                Double[][] order_book_errors){
            initData(date_time, buy_orders_statistics, sell_orders_statistics, order_book_errors, 0.20, 5, -0.0015, 0.0015);
        }
        
        public DataFrame(Date[] date_time, 
                Double[][] buy_orders_statistics, 
                Double[][] sell_orders_statistics, 
                Double[][] order_book_errors,
                double GMM_ERROR_THRESHOLD,
                int RETURNS_LAG,
                double RETURN_MIN_THRESHOLD,
                double RETURN_MAX_THRESHOLD){
            initData(date_time, 
                    buy_orders_statistics, 
                    sell_orders_statistics, 
                    order_book_errors,
                    GMM_ERROR_THRESHOLD,
                    RETURNS_LAG, 
                    RETURN_MIN_THRESHOLD,
                    RETURN_MAX_THRESHOLD);
        }
        
        private void initData(Date[] date_time, 
                Double[][] buy_orders_statistics, 
                Double[][] sell_orders_statistics, 
                Double[][] order_book_errors,
                double GMM_ERROR_THRESHOLD,
                int RETURNS_LAG,
                double RETURN_MIN_THRESHOLD,
                double RETURN_MAX_THRESHOLD){
            
            dates = new ArrayList<>();
            buy_price = new ArrayList<>();
            sell_price = new ArrayList<>();
            mean_price = new ArrayList<>();
            buy_gmm_error = new ArrayList<>();
            sell_gmm_error = new ArrayList<>();
            selected = new ArrayList<>();
            ID = new ArrayList<>();
            
            size = Array.getLength(date_time);
            Date start = date_time[0];
            Date end = date_time[size-1];
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);
            int index = 0;
            while(start.before(end)){
                if(!date_time[index+1].after(start)){
                    index++;
                }

                dates.add(start);
                buy_price.add(buy_orders_statistics[index][5]);
                sell_price.add(sell_orders_statistics[index][5]);
                mean_price.add((buy_orders_statistics[index][5] + sell_orders_statistics[index][5])*0.5);
                ID.add(index);
                buy_gmm_error.add(order_book_errors[index][3]);
                sell_gmm_error.add(order_book_errors[index][7]);
                selected.add( (order_book_errors[index][3]<GMM_ERROR_THRESHOLD && order_book_errors[index][7]<GMM_ERROR_THRESHOLD) );
                
                cal.add(Calendar.MINUTE, 1);
                start = cal.getTime();
            }
            dates.add(start);
            buy_price.add(buy_orders_statistics[index][5]);
            sell_price.add(sell_orders_statistics[index][5]);
            mean_price.add((buy_orders_statistics[index][5] + sell_orders_statistics[index][5])*0.5);
            ID.add(index);
            buy_gmm_error.add(order_book_errors[index][3]);
            sell_gmm_error.add(order_book_errors[index][7]);
            selected.add( (order_book_errors[index][3]<GMM_ERROR_THRESHOLD && order_book_errors[index][7]<GMM_ERROR_THRESHOLD) );
            
            size = dates.size();

            roll_min = new ArrayList<>();
            roll_max = new ArrayList<>();
            returns_min = new ArrayList<>();
            returns_max = new ArrayList<>();
            labels = new ArrayList<>();
            
            
            for(int i=0;i<size;i++){
                if(i < RETURNS_LAG-1) {
                    roll_min.add(null);
                    roll_max.add(null);
                }
                else {
                    roll_min.add(rolling_min(i, RETURNS_LAG));
                    roll_max.add(rolling_max(i, RETURNS_LAG));
                }
            }
            for(int i=RETURNS_LAG-1;i<size-1;i++){
                returns_min.add(roll_min.get(i+1)/mean_price.get(i-(RETURNS_LAG-1)) - 1.0);
                returns_max.add(roll_max.get(i+1)/mean_price.get(i-(RETURNS_LAG-1)) - 1.0);
            }
            
            for(int i=0;i<RETURNS_LAG;i++) {
                returns_min.add(null);
                returns_min.add(null);
            }
            
            for(int i=0;i<size;i++){
                if(returns_min.get(i) == null || returns_max.get(i) == null) {
                    labels.add(0);
                } else {
                    if(returns_max.get(i) > RETURN_MAX_THRESHOLD){
                        labels.add(2);
                    } else if(returns_min.get(i) < RETURN_MIN_THRESHOLD){
                        labels.add(1);
                    } else labels.add(0);
                }
            }
            



            
            
        }
        
        private Double rolling_min(int index, int RETURNS_LAG){
            Double min = mean_price.get(index);
            for(int i=index;i>index-RETURNS_LAG;i--){
                min = Math.min(min, mean_price.get(i));
            }
            return min;
        }
        
        private Double rolling_max(int index, int RETURNS_LAG){
            Double max = mean_price.get(index);
            for(int i=index;i>index-RETURNS_LAG;i--){
                max = Math.max(max, mean_price.get(i));
            }
            return max;
        }
        
        public void showInputData(int start, int end){
            System.out.println("SIZE "+size);
            for(int i=start;i<end;i++){

                System.out.print(dates.get(i) + " ");
                System.out.print(ID.get(i) + " ");
                System.out.print(buy_price.get(i) + " ");
                System.out.print(sell_price.get(i) + " ");
                System.out.print(mean_price.get(i) + " ");
                
                System.out.print(buy_gmm_error.get(i) + " ");
                System.out.print(sell_gmm_error.get(i) + " ");
                System.out.print(selected.get(i) + " ");
                
                System.out.print(roll_min.get(i) + " ");
                System.out.print(roll_max.get(i) + " ");
                System.out.print(returns_min.get(i) + " ");
                System.out.print(returns_max.get(i) + " ");
                System.out.print(labels.get(i) + " ");
                
                System.out.println();
            }
//            System.out.println(labels.size() + " " + ID.size());
        }
        
        
        
        
    }