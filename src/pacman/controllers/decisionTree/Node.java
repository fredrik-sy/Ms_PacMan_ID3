package pacman.controllers.decisionTree;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.view.mxGraph;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Node implements Serializable {
    public int m_AttributeOrLabel;
    public Map<Integer, Node> m_AttributeValuesAndChildren;

    public Node() {
        m_AttributeValuesAndChildren = new HashMap<>();
    }

    public Node(int attributeOrLabel) {
        m_AttributeOrLabel = attributeOrLabel;
        m_AttributeValuesAndChildren = new HashMap<>();
    }

    public int getMove(List<Integer> tuple) {
        if (m_AttributeValuesAndChildren.isEmpty()) {
            return m_AttributeOrLabel;
        }

        int attributeValue = tuple.get(m_AttributeOrLabel);
        Node child = m_AttributeValuesAndChildren.get(attributeValue);

        if (child == null)
            System.out.println(m_AttributeOrLabel);

        return child.getMove(tuple);
    }

    /**
     * Creates a graph starting from this node.
     */
    public void generateGraph() {
        mxGraph g = new mxGraph();
        g.getModel().beginUpdate();
        Object v0 = g.insertVertex(g.getDefaultParent(), null, m_AttributeOrLabel, 0, 0, 0, 0);
        g.updateCellSize(v0);
        generateGraph(g, this, v0);
        mxIGraphLayout l = new mxFastOrganicLayout(g);
        l.execute(g.getDefaultParent());
        g.getModel().endUpdate();

        try {
            ImageIO.write(mxCellRenderer.createBufferedImage(g, null, 1, Color.WHITE, true, null), "PNG", new File("myData/trainingGraph.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A recursive method to generate the graph with attributes, attribute values and labels.
     */
    private void generateGraph(mxGraph g, Node n, Object v0) {
        for (Entry<Integer, Node> attributeValueAndChild : n.m_AttributeValuesAndChildren.entrySet()) {
            Object v1 = g.insertVertex(g.getDefaultParent(), null, attributeValueAndChild.getValue().m_AttributeOrLabel, 0, 0, 0, 0);
            g.insertEdge(g.getDefaultParent(), null, attributeValueAndChild.getKey(), v0, v1);
            g.updateCellSize(v1);
            generateGraph(g, attributeValueAndChild.getValue(), v1);
        }
    }
}
