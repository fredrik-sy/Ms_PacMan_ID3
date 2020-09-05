package pacman.controllers.decisionTree;

import pacman.game.util.IO;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ID3 {
    private DataSet m_DataSet;
    private ExecutorService m_Service;

    public ID3() {
        m_DataSet = new DataSet(IO.loadFile("trainingData.txt"));
        m_Service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    /**
     * Creates a decision tree from data set.
     */
    public Node generateDecisionTree() {
        return generateDecisionTree(m_DataSet);
    }

    /**
     * A recursive method to generate the decision tree from data set.
     */
    private Node generateDecisionTree(DataSet D) {
        Node n = new Node();

        if (D.allLabelValuesIdentical()) {
            n.m_AttributeOrLabel = D.m_Tuples.get(0)[D.m_LabelIndex];
            return n;
        } else if (D.isAttributeListEmpty()) {
            n.m_AttributeOrLabel = D.findMajorityLabel();
            return n;
        } else {
            int attribute = getMaxInformationGainAttribute(D);
            n.m_AttributeOrLabel = attribute;
            D.m_Attributes.remove(Integer.valueOf(attribute));
            Map<Integer, Future<DataSet>> futures = new HashMap<>();

            for (int attributeValue : D.getAttributeValues(attribute)) {
                Future<DataSet> future = m_Service.submit(() -> D.filterMatchTuples(attribute, attributeValue));
                futures.put(attributeValue, future);
            }

            try {
                for (Entry<Integer, Future<DataSet>> future : futures.entrySet()) {
                    DataSet Dj = future.getValue().get();

                    if (Dj.isTupleListEmpty()) {
                        n.m_AttributeValuesAndChildren.put(future.getKey(), new Node(D.findMajorityLabel()));
                    } else {
                        n.m_AttributeValuesAndChildren.put(future.getKey(), generateDecisionTree(Dj));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            return n;
        }
    }

    /**
     * Calculates the attribute's information gain from data set.
     */
    private double getInformationGain(DataSet dataSet, int attribute) {
        List<Double> attributeValueCount = dataSet.countByAttributeValues(attribute);
        double averageEntropy = 0;
        double sum = attributeValueCount.stream().mapToDouble(Double::doubleValue).sum();

        for (int attributeValue = 0; attributeValue < attributeValueCount.size(); attributeValue++) {
            DataSet attributeValueDataSet = dataSet.filterMatchTuples(attribute, attributeValue);
            averageEntropy += (attributeValueCount.get(attributeValue) / sum) * getEntropy(attributeValueDataSet);
        }

        return getEntropy(m_DataSet) - averageEntropy;
    }

    /**
     * Calculates the entropy from data set.
     */
    private double getEntropy(DataSet dataSet) {
        List<Double> labelValuesCount = dataSet.countByLabelValues();
        double entropy = 0;
        double sum = labelValuesCount.stream().mapToDouble(Double::doubleValue).sum();

        for (double count : labelValuesCount) {
            entropy += -(count / sum) * log2(count / sum);
        }

        return entropy;
    }

    /**
     * Returns the base 2 logarithm of a double value.
     */
    private double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    /**
     * Gets the max information gain attribute by finding the highest information gain value.
     */
    private int getMaxInformationGainAttribute(DataSet dataSet) {
        return dataSet.m_Attributes
                .stream()
                .collect(Collectors.toMap(Function.identity(), e -> getInformationGain(dataSet, e)))
                .entrySet()
                .stream()
                .max(Entry.comparingByValue())
                .get()
                .getKey();
    }
}
