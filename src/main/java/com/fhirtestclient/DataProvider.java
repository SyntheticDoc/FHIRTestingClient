package com.fhirtestclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class DataProvider {
    ArrayList<String> datasets = new ArrayList<>();
    private final String filename = "resources/datasets/ptbdb_normal.csv";
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
