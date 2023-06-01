package com.fhirtestclient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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

    public String getDataset_ID(int num) {
        String targetFile = foldername + "/id_" + num + "_1.txt";

        try(Scanner sc = new Scanner(new File(targetFile))) {
            while(sc.hasNextLine()) {
                String s = sc.nextLine();
                if(!s.isBlank()) {
                    return s.replace(",", " ");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public String getMimicDat(String filenum) {
        String sourceFile = foldername + "/mimic/mimic_perform_af_csv/mimic_" + filenum + "_data.txt";

        try(Scanner sc = new Scanner(new File(sourceFile))) {
            while(sc.hasNextLine()) {
                String s = sc.nextLine();

                if(!s.isBlank()) {
                    return s;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void processMimicDat(String filenum) {
        String sourceFile = foldername + "/mimic/mimic_perform_af_csv/mimic_perform_af_" + filenum + "_data.csv";
        boolean isFirstLine = true;
        ArrayList<String> data = new ArrayList<>();

        try(Scanner sc = new Scanner(new File(sourceFile))) {
            while(sc.hasNextLine()) {
                String s = sc.nextLine();

                if(isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                if(!s.isBlank()) {
                    data.add(s.split(",")[2]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String targetFile = foldername + "/mimic/mimic_perform_af_csv/mimic_" + filenum + "_data.txt";

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            StringBuilder fileData = new StringBuilder();

            for(int i = 0; i < data.size(); i++) {
                fileData.append(data.get(i));

                if(i < (data.size() - 1)) {
                    fileData.append(" ");
                }
            }

            writer.write(fileData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
