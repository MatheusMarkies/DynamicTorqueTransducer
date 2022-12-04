package com.matheusmarkies.manager.analysis;

import com.matheusmarkies.manager.DataManager;
import com.matheusmarkies.manager.utilities.Vector2D;
import com.matheusmarkies.manager.utilities.Vector3D;
import com.matheusmarkies.objects.ColorStats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class SampleAnalysis {

    //Color Sample Filters
    // {
    public static List<Vector3D> getChangeRateList(List<ColorStats> history) {
        List<Vector3D> changeRateList = new ArrayList<>();
        if (history.size() > 2) {
            for(int i = 1;i<history.size();i++) {
                for (int k = 0; k<=2; k++) {
                    Vector3D changeVector = Vector3D.zeroVector;
                    double historyCompareSample = 0;
                    double currentStatsSample = 0;

                    if (k == 0) {
                        historyCompareSample = history.get(i - 1).getBlue();
                        currentStatsSample = history.get(i).getBlue();
                        changeVector.x(historyCompareSample - currentStatsSample);
                    } else if (k == 1) {
                        historyCompareSample = history.get(i - 1).getRed();
                        currentStatsSample = history.get(i).getRed();
                        changeVector.y(historyCompareSample - currentStatsSample);
                    } else {
                        historyCompareSample = history.get(i - 1).getGreen();
                        currentStatsSample = history.get(i).getGreen();
                        changeVector.z(historyCompareSample - currentStatsSample);
                    }
                    changeRateList.add(changeVector);
                }
            }
        }
        return changeRateList;
    }

    public static Vector3D getChangeRate(ColorStats compareSample, ColorStats currentSample) {
        Vector3D changeRate = new Vector3D();
        for (int k = 0; k <= 2; k++) {
            double historyCompareSample = 0;
            double currentStatsSample = 0;

            if (k == 0) {
                historyCompareSample = compareSample.getBlue();
                currentStatsSample = currentSample.getBlue();
                changeRate.x(Math.abs(historyCompareSample - currentStatsSample));
            } else if (k == 1) {
                historyCompareSample = compareSample.getRed();
                currentStatsSample = currentSample.getRed();
                changeRate.y(Math.abs(historyCompareSample - currentStatsSample));
            } else {
                historyCompareSample = compareSample.getGreen();
                currentStatsSample = currentSample.getGreen();
                changeRate.z(Math.abs(historyCompareSample - currentStatsSample));
            }
        }
        return changeRate;
    }

    public static boolean rangeCheck(double a, double b, double percent){
        //if((b * percent) < a && a < (b * (1+percent)))
        System.out.println("Change Rate: "+a +" / "+ Math.max(b * (1 + percent / 100),10)+" / "+ b);
            return true;
        //return false;
    }

    public static ColorStats hardShiftFilter(List<ColorStats> history, ColorStats current, double percent) {
        ColorStats filteredStats = current;

        if (history.size() > 2) {
            int historyLength = history.size();
            Vector3D oldChangeRate = getChangeRate(history.get(historyLength - 2), history.get(historyLength - 1));
            Vector3D newChangeRate = getChangeRate(history.get(historyLength - 1), current);
            oldChangeRate = Vector3D.oneVector;
            System.out.println("oldChangeRate: " + oldChangeRate.toString());
            System.out.println("newChangeRate: " + newChangeRate.toString());

            if (rangeCheck(newChangeRate.x(), oldChangeRate.x(), percent)
                    && rangeCheck(newChangeRate.y(), oldChangeRate.y(), percent)
                    && rangeCheck(newChangeRate.z(), oldChangeRate.z(), percent))
                filteredStats = current;
            else
                filteredStats = history.get(historyLength - 1);
        }
        System.out.println(history.size());
        return filteredStats;
    }

    public static List<ColorStats> movingAverageFilter(List<ColorStats> history, int samples) {
        List<ColorStats> filteredStats = history;

        if (history.size() > samples) {
            filteredStats = new ArrayList<>();
            int filterLength = Math.round(history.size()/samples);
            for(int i =0;i<filterLength;i++){
                float redValue = 0;
                float greenValue = 0;
                float blueValue = 0;
                for(int u = (i * samples);u<((i+1)*samples);u++) {
                    redValue+=history.get(u).getRed()/samples;
                    greenValue+=history.get(u).getGreen()/samples;
                    blueValue+=history.get(u).getBlue()/samples;
                }
                filteredStats.add(new ColorStats(redValue,greenValue,blueValue));
            }
        }
        return filteredStats;
    }

    public static Vector2D getSensorParameters(List<ColorStats> history, ColorStats stats) {
        double color = 0;
        double discrepacy = 0;

        ArrayList<ColorStats> historyClone = new ArrayList<>();
        for (ColorStats stats1: history)
            historyClone.add(stats1);

        try {
            if (historyClone.size() > 0) {
                double maxValue = getSensorMaxValue(historyClone, 0);
                double redValue = Math.min(stats.getRed(), maxValue) / maxValue
                        , greenValue = Math.min(stats.getGreen(), maxValue) / maxValue
                        , blueValue = Math.min(stats.getBlue(), maxValue) / maxValue;

                double dL = 0;
                double dR = 0;
                double av = 0;
                if (redValue > greenValue && redValue > blueValue) {
                    color = 1.0;
                    dR = blueValue;
                    dL = greenValue;

                    av = redValue;
                }
                if (greenValue > redValue && greenValue > blueValue) {
                    color = 2.0;
                    dR = redValue;
                    dL = blueValue;

                    av = greenValue;
                }
                if (blueValue > redValue && blueValue > greenValue) {
                    color = 0.0;
                    dR = greenValue;
                    dL = redValue;

                    av = blueValue;
                }
                //dL > dR mais a esquerda caso contrario mais a direita
                int normalize = (int) Math.round(Math.abs(dL-dR)/(dL-dR));

                discrepacy =
                        Vector2D.lerp(
                                new Vector2D(0.0,0.0), new Vector2D(0.5,0.5),
                                    Math.max(0, Math.min(Math.abs((dL-dR)/av), 1))
                        ).x() * normalize;
            }
        }catch (Exception e){System.err.println(e);}
        return new Vector2D(color, discrepacy);
    }

    public static Vector3D getSensorMaxValue(List<ColorStats> history){
        Vector3D maxValues = new Vector3D();

        for (int i =0;i< history.size();i++){
            if(history.get(i).getRed() > maxValues.x())
                maxValues.x(history.get(i).getRed());
            if(history.get(i).getGreen() > maxValues.y())
                maxValues.y(history.get(i).getGreen());
            if(history.get(i).getBlue() > maxValues.z())
                maxValues.z(history.get(i).getBlue());
        }

        return maxValues;
    }

    public static double getSensorMaxValue(List<ColorStats> history, int sampleRange){
        int minHistoryIndex = Math.max(history.size() - sampleRange,0);
        if(sampleRange==0) minHistoryIndex = 0;

        double redValue = 0,greenValue = 0,blueValue = 0;
        for(int i = Math.max((history.size()-1),0);i>minHistoryIndex;i--){
            if(redValue<history.get(i).getRed()) redValue = history.get(i).getRed();
            if(greenValue<history.get(i).getGreen()) greenValue = history.get(i).getGreen();
            if(blueValue<history.get(i).getBlue()) blueValue = history.get(i).getBlue();
        }

        if(redValue > blueValue) {
            if (redValue > greenValue) {
                return redValue;
            }
        }else if(blueValue > greenValue){ return blueValue; }
        else{ return greenValue; }
     return redValue;
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
