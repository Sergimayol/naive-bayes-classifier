import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import betterSwing.Section;
import betterSwing.Window;
import betterSwing.utils.DirectionAndPosition;

public class Main {

	private Window window;
	private NaiveBayesClassifier classifier;
	private final String TRAINED_MODEL_PATH = "./model.ser";

	public static void main(String[] args) {
		new Main().init();
	}

	private static Object[] createDataset() {
		List<String> trainingData = new ArrayList<>();
		List<String> labels = new ArrayList<>();

		String trainingDataPath = "./data";
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

	@SuppressWarnings("unchecked")
	public void train() {
		Object[] dataset = createDataset();
		List<String> trainingData = (List<String>) dataset[0];
		List<String> labels = (List<String>) dataset[1];

		NaiveBayesClassifier model = new NaiveBayesClassifier();
		model.train(trainingData, labels);
		try {
			model.saveModel(TRAINED_MODEL_PATH);
			System.out.println("Model saved successfully");
		} catch (IOException e) {
			System.out.println("Error while saving the model" + e.getMessage());
		}
	}

	public void init() {
		this.window = new Window("config.json");
		this.window.initConfig();
		try {
			if (!new File(TRAINED_MODEL_PATH).exists()) {
				System.out.println("Training the model...");
				this.train();
			}
			this.classifier = NaiveBayesClassifier.loadModel(TRAINED_MODEL_PATH);
			this.createWindowComponents();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Error while loading the model" + e.getMessage());
		}
		this.window.start();
	}

	@SuppressWarnings("unchecked")
	private void createWindowComponents() {
		Section section = new Section();
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		section.createFreeSection(panel);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setLayout(new BorderLayout());
		JLabel titleLabel = new JLabel("Language Guesser", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		panel.add(titleLabel, BorderLayout.NORTH);

		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(Color.WHITE);
		JLabel inputLabel = new JLabel("Input text:", SwingConstants.CENTER);
		JTextField inputField = new JTextField();
		inputPanel.add(inputLabel, BorderLayout.NORTH);
		inputPanel.add(inputField, BorderLayout.CENTER);
		panel.add(inputPanel, BorderLayout.SOUTH);
		JButton detect = new JButton("Detect Language");
		detect.setBounds(new Rectangle(new Dimension(10, 10)));
		// Log panel
		inputPanel.add(detect, BorderLayout.EAST);
		JPanel resultPanel = new JPanel();
		resultPanel.setBackground(Color.WHITE);
		resultPanel.setLayout(new BorderLayout());
		JLabel resultLabel = new JLabel(
				"Introduce a text and press the button to detect the language.",
				SwingConstants.CENTER);
		resultPanel.add(resultLabel, BorderLayout.CENTER);
		detect.addActionListener(e -> {
			// Crear el gráfico
			final String xAxisLabel = "Language";
			final String yAxisLabel = "Probability";
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			String input = inputField.getText();
			Object[] res = this.classifier.classify(input);
			String[] labels = getLabels((List<String>) res[0]);
			double[] probabilities = (double[]) res[1];
			for (int i = 0; i < labels.length; i++) {
				dataset.addValue(probabilities[i], labels[i], labels[i]);
			}

			// Find the max value
			int aux = findMaxValue(probabilities);
			JFreeChart chart = this.createBarPlotPanel("Language Probability", xAxisLabel, yAxisLabel, dataset);
			chart.getCategoryPlot().getRenderer().setSeriesPaint(aux, Color.RED);

			JPanel auxPanel = new JPanel();
			auxPanel.setLayout(new GridLayout(1, 1));
			auxPanel.add(new ChartPanel(chart));

			// Limpiar el panel y agregar el chartPanel al centro
			resultPanel.removeAll();
			resultPanel.add(auxPanel, BorderLayout.CENTER);
			resultPanel.revalidate();
			resultPanel.repaint();
		});
		panel.add(resultPanel, BorderLayout.CENTER);
		section.createFreeSection(panel);
		this.window.addSection(section, DirectionAndPosition.POSITION_CENTER, "Body");
	}

	private int findMaxValue(double[] probabilities) {
		double max = probabilities[0];
		int aux = 0;
		for (int i = 0; i < probabilities.length; i++) {
			if (probabilities[i] > max) {
				max = probabilities[i];
				aux = i;
			}
		}
		return aux;
	}

	private JFreeChart createBarPlotPanel(String title, String xAxisLabel, String yAxisLabel,
			DefaultCategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart(title,
				xAxisLabel,
				yAxisLabel,
				dataset,
				PlotOrientation.VERTICAL,
				false, true, false);

		for (int i = 0; i < 10; i++) {
			chart.getCategoryPlot().getRenderer().setSeriesPaint(i, Color.BLUE);
		}

		return chart;
	}

	private String[] getLabels(List<String> strings) {
		String[] labels = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			labels[i] = toCommonLabel(strings.get(i));
		}
		return labels;
	}

	private String toCommonLabel(String label) {
		return switch (label) {
			case "ca" -> "Catalan";
			case "da" -> "Danish";
			case "de" -> "German";
			case "en" -> "English";
			case "es" -> "Spanish";
			case "fr" -> "French";
			case "hr" -> "Croatian";
			case "hu" -> "Hungarian";
			case "it" -> "Italian";
			case "pt" -> "Portuguese";
			default -> "Unknown";
		};
	}
}
