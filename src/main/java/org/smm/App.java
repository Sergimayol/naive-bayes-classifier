package org.smm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static Object[] createDataset() {
        List<String> trainingData = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        String trainingDataPath = "./src/main/resources/training";
        File folder = new File(trainingDataPath);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    trainingData.add(line);
                    labels.add(file.getName().replace(".dic", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return new Object[] { trainingData, labels };
    }

    public static void main(String[] args) {

        Object[] dataset = createDataset();
        List<String> trainingData = (List<String>) dataset[0];
        List<String> labels = (List<String>) dataset[1];

        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.train(trainingData, labels);

        String text = "Hello, how are you?";
        Object[] res = classifier.classify(text);
        List<String> classes = (List<String>) res[0];
        double[] probabilities = (double[]) res[1];
        double max = 0;
        int maxIndex = 0;

        System.out.println("Results for \"" + text + "\"");
        for (int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i) + ": " + probabilities[i]);
            if (probabilities[i] > max) {
                max = probabilities[i];
                maxIndex = i;
            }
        }
        System.out.println("The sentence is classified as: " + classes.get(maxIndex));
    }

}
