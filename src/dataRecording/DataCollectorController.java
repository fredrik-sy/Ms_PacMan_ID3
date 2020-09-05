package dataRecording;

import pacman.controllers.*;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

import java.util.ArrayList;
import java.util.List;

/**
 * The DataCollectorHumanController class is used to collect training data from playing PacMan.
 * Data about game state and what MOVE chosen is saved every time getMove is called.
 *
 * @author andershh
 */
public class DataCollectorController extends HumanController {

    public DataCollectorController(KeyBoardInput input) {
        super(input);
    }

    @Override
    public Constants.MOVE getMove(Game game, long dueTime) {
        Constants.MOVE move = super.getMove(game, dueTime);

        DataTuple data = new FredrikDataTuple(game, move);

        DataSaverLoader.SavePacManData(data);
        return move;
    }
}
