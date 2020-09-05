package pacman.controllers.decisionTree;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * For more information about this class, please refer to the class documentation.
 */
public final class DataSet implements Serializable {
    public List<Integer> m_Attributes;
    public List<Set<Integer>> m_PossibleTupleValues;
    public List<int[]> m_Tuples;
    public int m_LabelIndex = 0;

    public DataSet(String pacManData) {
        m_PossibleTupleValues = new ArrayList<>();
        m_Tuples = new ArrayList<>();
        preProcessDataSet(pacManData.split("\n"));
    }

    /**
     * Returns true if all labels are identical.
     */
    public boolean allLabelValuesIdentical() {
        for (int i = 1; i < m_Tuples.size(); i++) {
            if (m_Tuples.get(i - 1)[m_LabelIndex] != m_Tuples.get(i)[m_LabelIndex]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if attribute list is empty.
     */
    public boolean isAttributeListEmpty() {
        return m_Attributes.isEmpty();
    }

    /**
     * Returns true if tuple list is empty.
     */
    public boolean isTupleListEmpty() {
        return m_Tuples.isEmpty();
    }

    /**
     * Finds the label with the highest occurrence among them.
     */
    public int findMajorityLabel() {
        return m_Tuples.stream()
                .map(e -> e[m_LabelIndex])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Entry.comparingByValue())
                .get()
                .getKey();
    }

    /**
     * Returns the result from the label value counting.
     */
    public List<Double> countByLabelValues() {
        return m_Tuples.stream()
                .map(e -> e[m_LabelIndex])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .values()
                .stream()
                .map(Long::doubleValue)
                .collect(Collectors.toList());
    }

    /**
     * Returns the result from the attribute value counting.
     */
    public List<Double> countByAttributeValues(int attribute) {
        return m_Tuples.stream()
                .map(e -> e[attribute])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .map(e -> e.getValue().doubleValue())
                .collect(Collectors.toList());
    }

    /**
     * Filter all of the tuples and remove non-matching attribute with the attribute value.
     * Returns a new data set with the results of the filtering.
     */
    public DataSet filterMatchTuples(int attribute, int attributeValue) {
        DataSet dataSet = deepCopy();
        assert dataSet != null;
        dataSet.m_Tuples = dataSet.m_Tuples
                .stream()
                .filter(e -> e[attribute] == attributeValue)
                .collect(Collectors.toList());

        return dataSet;
    }

    /**
     * Gets all the possible attribute values from the attribute.
     */
    public Set<Integer> getAttributeValues(int attribute) {
        return m_PossibleTupleValues.get(attribute);
    }

    /**
     * Pre-process the data set.
     */
    private void preProcessDataSet(String[] pacManData) {
        Arrays.stream(pacManData)
                .forEach(e -> {
                    int[] o = Arrays.stream(e.split(";"))
                            .mapToInt(Integer::parseInt)
                            .toArray();

                    processDataSet(IntStream.range(0, o.length)
                            .map(i -> i < m_LabelIndex ? o[i] : i < o.length - 1 ? o[i + 1] : o[m_LabelIndex])
                            .toArray());
                });

        m_Attributes = IntStream.range(0, m_PossibleTupleValues.size() - 1)
                .boxed()
                .collect(Collectors.toList());

        m_LabelIndex = m_Attributes.size() - 1;
    }

    /**
     * Process the tuple.
     */
    private void processDataSet(int[] pacManDataTuple) {
        m_Tuples.add(pacManDataTuple);

        while (m_PossibleTupleValues.size() < pacManDataTuple.length) {
            m_PossibleTupleValues.add(new HashSet<>());
        }

        for (int i = 0; i < pacManDataTuple.length; i++) {
            m_PossibleTupleValues.get(i).add(pacManDataTuple[i]);
        }
    }

    /**
     * Creates a deep copy of data set.
     */
    public DataSet deepCopy() {
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(this);

            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(byteInput);
            return (DataSet) objectInput.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
