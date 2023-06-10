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

Output:

```text
Results for "Hello, how are you?"
de: 0.058823529411764844
pt: 0.058823529411764844
en: 0.47058823529411636
hr: 0.058823529411764844
it: 0.058823529411764844
fr: 0.058823529411764844
da: 0.058823529411764844
hu: 0.058823529411764844
ca: 0.058823529411764844
es: 0.058823529411764844
The sentence is classified as: en
```

For a more detailed example, see the [App.java](./src/main/java/org/smm/App.java) file and see the `main` method.

If you want to see a more complex example with interface interaction, see the project in the examples folder: [NaiveBayesClassifierExample](./example/)

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details
