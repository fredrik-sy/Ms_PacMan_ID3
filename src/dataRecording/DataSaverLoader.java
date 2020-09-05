package dataRecording;

import pacman.game.util.*;

/**
 * This class uses the IO class in the PacMan framework to do the actual saving/loading of
 * training data.
 * @author andershh
 *
 */
public class DataSaverLoader {
	
	private static String FileName = "trainingData.txt";
	
	public static void SavePacManData(DataTuple data)
	{
		IO.saveFile(FileName, data.getSaveString(), true);
	}
}
