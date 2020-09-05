package pacman.controllers.decisionTree;

import dataRecording.DataTuple;
import dataRecording.FredrikDataTuple;
import dataRecording.HugoDataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.io.*;

public class DTController extends Controller<MOVE> {
    public static final String FILE_NAME = "myData/decisionTree.dat";

    private Node m_RootNode;

    public DTController() {
        //m_RootNode = new ID3().generateDecisionTree();
        //writeToFile();
        readFromFile();
        //m_RootNode.generateGraph();
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        DataTuple tuple = new FredrikDataTuple(game, MOVE.NEUTRAL);
        return MOVE.values()[m_RootNode.getMove(tuple.getDataList())];
    }

    private void writeToFile() {
        try {
            FileOutputStream fileOutput = new FileOutputStream(FILE_NAME);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(m_RootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromFile() {
        try {
            FileInputStream fileInput = new FileInputStream(FILE_NAME);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            m_RootNode = (Node) objectInput.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
