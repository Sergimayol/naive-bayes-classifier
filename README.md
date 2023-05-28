# Naive Bayes Classifier

This is an educational implementation of a Naive Bayes Classifier in Java. It is based on the [Naive Bayes Classifier](https://en.wikipedia.org/wiki/Naive_Bayes_classifier) Wikipedia article.

## Usage example

The classifier can be used as follows:

```java
Object[] dataset = createDataset();
List<String> trainingData = (List<String>) dataset[0];
List<String> labels = (List<String>) dataset[1];

NaiveBayesClassifier classifier = new NaiveBayesClassifier();
classifier.train(trainingData, labels);

String text = "Hello, how are you?";
Object[] res = classifier.classify(text);
List<String> classes = (List<String>) res[0];
double[] probabilities = (double[]) res[1];
```

For a more detailed example, see the [App.java](./src/main/java/org/smm/App.java) file and see the `main` method.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details
