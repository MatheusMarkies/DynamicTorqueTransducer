package com.matheusmarkies.manager.utilities;

import java.io.*;

public class Save {

    public static String ApplicationFolder = System.getenv("APPDATA") + "\\Mouse Trap Car";

    //public static void saveCarSettings(Car car) throws IOException {
            //File selectedFile = new File(ApplicationFolder + "\\CarSettings.car");

            //FileOutputStream fileOutput = new FileOutputStream(selectedFile);
            //ObjectOutputStream objectStream = new ObjectOutputStream(fileOutput);

            //objectStream.writeObject(car);

            //objectStream.close();
            //fileOutput.close();
    //}

    //public static Car openCarPresets() throws IOException, ClassNotFoundException {
        //Car car;
        //File selectedFile = new File(ApplicationFolder + "\\CarSettings.car");

        //FileInputStream fileInput = new FileInputStream(selectedFile);
        //ObjectInputStream objectStream = new ObjectInputStream(fileInput);

        //car = (Car) objectStream.readObject();

        //objectStream.close();
        //fileInput.close();

        //return car;
    //}

}
