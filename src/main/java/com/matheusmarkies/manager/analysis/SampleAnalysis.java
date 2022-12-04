package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.manager.utilities.Vector2D;
import com.matheusmarkies.manager.utilities.Vector3D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleAnalysis {

    //Color Sample Filters
    // {
    public static boolean rangeCheck(double a, double b, double percent){
        //if((b * percent) < a && a < (b * (1+percent)))
        System.out.println("Change Rate: "+a +" / "+ Math.max(b * (1 + percent / 100),10)+" / "+ b);
            return true;
        //return false;
    }

    public static  List<Double> movingAverageFilter( List<Double> history, int samples) {
        List<Double> filteredStats = history;

        if (history.size() > samples) {
            filteredStats = new ArrayList<>();
            int filterLength = Math.round(history.size()/samples);
            for(int i =0;i<filterLength;i++){
                double value = 0;
                for(int u = (i * samples);u<((i+1)*samples);u++) {
                    value+=history.get(u)/samples;
                }
                filteredStats.add(value);
            }
        }
        return filteredStats;
    }

    public static double getSensorParameters(List<Double> history) {
        double discrepacy = 0;

                double dL = 0;
                double dR = 0;
                double av = 0;
return 0;
    }

    public static double getSensorMaxValue(List<Double> history, int sampleRange){
        int minHistoryIndex = Math.max(history.size() - sampleRange,0);
        if(sampleRange==0) minHistoryIndex = 0;

        double maxValue = 0;
        for(int i = Math.max((history.size()-1),0);i>minHistoryIndex;i--){
            if(maxValue<history.get(i)) maxValue = history.get(i);
        }
     return maxValue;
    }
    // }
    //Chart Sample Filters
    //{
    public static List<Vector2D> averageSampleFilter(List<Vector2D> data, int filterRange) {
        List<Vector2D> filteredRotations = new ArrayList<>();

        filteredRotations.add(new Vector2D(
                data.get(0).x(),
                data.get(0).y()
        ));

        for(int i = 0; i < data.size();) {
            int sampleHighIndex = i + filterRange;

            if(sampleHighIndex >= data.size()) {
                sampleHighIndex = (data.size() - 1);

                filteredRotations.add(new Vector2D(
                        data.get((data.size() - 1)).x(),
                        data.get((data.size() - 1)).y()
                ));

                break;
            }

            double xAverage = 0;
            double yAverage = 0;

            try {
                for (int u = i; u < (sampleHighIndex); u++) {
                    xAverage += data.get(u).x() / filterRange;
                    yAverage += data.get(u).y() / filterRange;
                }
            }catch (Exception exception){System.err.println(exception);}

            filteredRotations.add(
                    new Vector2D(
                    (double) Math.round(xAverage*1000)/1000,
                            (double) Math.round(yAverage*1000)/1000)
            );

            if(sampleHighIndex < data.size())
                i = sampleHighIndex;
            else
                break;
        }

        return getReorderedList(filteredRotations);
    }

    public static List<Vector2D> getSmoothChart(List<Vector2D> rotations){
        List<Vector2D> smoothedRotations = new ArrayList<>();

        for(int i = 0; i < rotations.size();) {
            int sampleHighIndex = i + 6;

            if (sampleHighIndex > rotations.size())
                break;

            Vector2D A = new Vector2D(rotations.get(i).x(),rotations.get(i).y());
            Vector2D B = new Vector2D(rotations.get(i+5).x(),rotations.get(i+5).y());
            Vector2D C = new Vector2D(rotations.get(i+3).x(),rotations.get(i+3).y());

            for(float k =0;k<1;k+=0.1f){
                Vector2D curve = Vector2D.bezierCurve(A,C,B,k);
                smoothedRotations.add(curve);
            }

            if(sampleHighIndex < rotations.size())
                i = sampleHighIndex;
            else
                break;
        }
        return getReorderedList(smoothedRotations);
    }

    public static List<Vector2D> getReorderedList(List<Vector2D> list){
        Vector2D[] vectorArray = new Vector2D[list.size()];
        list.toArray(vectorArray);

        Vector2D aux = new Vector2D(0,0);
        int i = 0;

        for(i = 0; i< vectorArray.length; i++){
            for(int j = 0; j<(vectorArray.length-1); j++){
                if(vectorArray[j].x() > vectorArray[j + 1].x()){
                    aux = vectorArray[j];
                    vectorArray[j] = vectorArray[j+1];
                    vectorArray[j+1] = aux;
                }
            }
        }

        return Arrays.asList(vectorArray);
    }
    // }

}
