package de.unistuttgart.iste.ese.api.todo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.LoadingModelEvaluatorBuilder;
import org.jpmml.evaluator.OutputField;
import org.jpmml.evaluator.TargetField;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBException;


/**
 * A class for loading and using a PMML-based Todo classification model.
 */
public class TodoModel {
    private final String MODEL_PATH;
    private Evaluator evaluator;

    /**
     * Constructs a TodoModel with the specified PMML model file path.
     *
     * @param pathname The path to the PMML model file.
     */
    public TodoModel(String pathname) {
        ClassLoader classLoader = getClass().getClassLoader();
        String modelPath = classLoader.getResource(pathname).getFile();
        this.MODEL_PATH = modelPath;
        loadModel();
    }

    /**
     * Loads the PMML model from the specified file path and initializes the model
     * evaluator.
     * If the loading process encounters any exceptions, the evaluator is set to
     * null.
     */
    public void loadModel() {
        // Building a model evaluator from a PMML file
        try {
            Evaluator evaluator = new LoadingModelEvaluatorBuilder()
                    .load(new File(this.MODEL_PATH))
                    .build();
            this.evaluator = evaluator;
        } catch (IOException | ParserConfigurationException | SAXException | JAXBException e) {
            e.printStackTrace();
            this.evaluator = null;
        }
    }

    /**
     * Parses the model's output to retrieve the predicted class/category.
     *
     * @param results The results obtained from evaluating the model on input data.
     * @return The predicted class/category based on the model's output.
     */
    private String parseModelOutput(Map<String, ?> results) {
        // Primary result (y) field(s)
        List<TargetField> targetFields = this.evaluator.getTargetFields();
        // Secondary result (eg. probability(y), decision(y)) fields
        List<OutputField> outputFields = this.evaluator.getOutputFields();

        int predictedLabelIndex = Integer.parseInt(results.get(outputFields.get(2).getName()).toString());
        String[] targetLabels = targetFields.get(0).getName().replaceAll("\\[|'|\\]", "").split(" ");
        String predictedLabel = targetLabels[predictedLabelIndex];

        return predictedLabel;
    }

    /**
     * Predicts the class/category of a given input text.
     *
     * @param inputString The input text to be classified.
     * @return The predicted class/category for the input text.
     */
    public String predictClass(String inputString) {
        // Prepare the input data
        Map<String, Object> input = new HashMap<>();
        input.put("text", inputString);

        // Predict class and parse results
        Map<String, ?> results = this.evaluator.evaluate(input);
        String predictedClassString = parseModelOutput(results);

        return predictedClassString;
    }

    /**
     * Unloads the loaded PMML model, releasing resources.
     */
    public void unloadModel() {
        this.evaluator = null;
    }
}
