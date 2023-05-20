package com.fhirtestclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class DataProvider {
    ArrayList<String> datasets = new ArrayList<>();
    private final String filename = "resources/datasets/ptbdb_normal.csv";
    private final String foldername = "resources/datasets";
    private int lastDataset;

    public DataProvider() {
        readDatasets();
    }

    public String getRandomDataset() {
        int fetchDataset;

        do {
            fetchDataset = ThreadLocalRandom.current().nextInt(0, datasets.size());
        } while(fetchDataset == lastDataset);

        lastDataset = fetchDataset;

        System.out.println("Reading dataset " + fetchDataset);

        return datasets.get(fetchDataset);
    }

    public String getIliData(int filenum) {
        String path = foldername + "/";

        if(filenum == 1) {
            path += "Ili_ecg_1.05-2023-Ecg-m";
        } else if(filenum == 2)  {
            path += "Ili_ecg_2.05-2023-Ecg-m";
        } else {
            throw new IllegalArgumentException("DataProvider.getIliData(): Unknown filenum " + filenum);
        }

        ArrayList<String> dat = new ArrayList<>();

        try(Scanner sc = new Scanner(new File(path))) {
            while(sc.hasNextLine()) {
                String s = sc.nextLine();
                if(!s.isBlank()) {
                    dat.add(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        }

        StringBuilder result = new StringBuilder();

        for(int i = 1; i < dat.size(); i++) {
            result.append(dat.get(i).replace("{", "").replace("}", " "));
        }

        return result.toString();
    }

    private void readDatasets() {
        int counter = 0;

        try(Scanner sc = new Scanner(new File(filename))) {
            while(sc.hasNextLine()) {
                String s = sc.nextLine();
                if(!s.isBlank()) {
                    datasets.add(s);
                    counter++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("DataProvider: " + counter + " datasets read");
    }
}
