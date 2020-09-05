package dataRecording;

import pacman.controllers.examples.StarterPacMan;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * The DataCollectorHumanController class is used to collect training data from playing PacMan.
 * Data about game state and what MOVE chosen is saved every time getMove is called.
 *
 * @author andershh
 */
public class StarterDataCollectorController extends StarterPacMan {
    public List<DataTuple> pacManData = new ArrayList<>();

    public StarterDataCollectorController() {
        super();
    }

    @Override
    public MOVE getMove(Game game, long dueTime) {
        if (game.getNumberOfActivePills() == game.getNumberOfPills())
			savePacManData();

        MOVE move = super.getMove(game, dueTime);

        DataTuple data = new FredrikDataTuple(game, move);

        pacManData.add(data);
        return move;
    }

    public void savePacManData() {
        for (DataTuple data : pacManData) {
            DataSaverLoader.SavePacManData(data);
        }

        pacManData.clear();
    }
}
