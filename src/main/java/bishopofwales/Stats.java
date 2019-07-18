package bishopofwales;

import java.util.ArrayList;

public class Stats{
    public static double findAverage(ArrayList<Double> nums){
        double total = 0;
        for(Double num: nums){
            total += num;
        }
        return total/nums.size();
    }
    public static double findMedian(ArrayList<Double> nums){
        if (nums.size() == 0){
            return -1;
        }
        return nums.get(nums.size()/2);
    }
}