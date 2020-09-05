package dataRecording;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

public class FredrikDataTuple extends DataTuple {
    public int blinkyPosition;
    public int inkyPosition;
    public int pinkyPosition;
    public int suePosition;

    public int nextMoveTowardsPill;
    public int nextMoveTowardsPowerPill;
    public int nextMoveTowardsGhost;

    public FredrikDataTuple(Game game, Constants.MOVE move) {
        if (move == Constants.MOVE.NEUTRAL) {
            move = game.getPacmanLastMoveMade();
        }

        this.DirectionChosen = move.ordinal();

        this.mazeIndex = game.getMazeIndex();
        this.currentLevel = game.getCurrentLevel();
        this.pacmanPosition = discretizeNodeIndex(game.getPacmanCurrentNodeIndex(), game.getNumberOfNodes());
        this.pacmanLivesLeft = game.getPacmanNumberOfLivesRemaining();
        this.currentScore = game.getScore();
        this.totalGameTime = game.getTotalTime();
        this.currentLevelTime = game.getCurrentLevelTime();
        this.numOfPillsLeft = discretizeNumberOfPills(game.getNumberOfActivePills()).ordinal();
        this.numOfPowerPillsLeft = discretizeNumberOfPowerPills(game.getNumberOfActivePowerPills()).ordinal();

        if (game.getGhostLairTime(Constants.GHOST.BLINKY) == 0) {
            this.isBlinkyEdible = game.isGhostEdible(Constants.GHOST.BLINKY) ? 1 : 0;
            this.blinkyDist = discretizeDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY))).ordinal();
        }

        if (game.getGhostLairTime(Constants.GHOST.INKY) == 0) {
            this.isInkyEdible = game.isGhostEdible(Constants.GHOST.INKY) ? 1 : 0;
            this.inkyDist = discretizeDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY))).ordinal();
        }

        if (game.getGhostLairTime(Constants.GHOST.PINKY) == 0) {
            this.isPinkyEdible = game.isGhostEdible(Constants.GHOST.PINKY) ? 1 : 0;
            this.pinkyDist = discretizeDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY))).ordinal();
        }

        if (game.getGhostLairTime(Constants.GHOST.SUE) == 0) {
            this.isSueEdible = game.isGhostEdible(Constants.GHOST.SUE) ? 1 : 0;
            this.sueDist = discretizeDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE))).ordinal();
        }

        this.blinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), Constants.DM.PATH).ordinal();
        this.inkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), Constants.DM.PATH).ordinal();
        this.pinkyDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), Constants.DM.PATH).ordinal();
        this.sueDir = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), Constants.DM.PATH).ordinal();
        this.blinkyPosition = discretizeNodeIndex(game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY), game.getNumberOfNodes());
        this.inkyPosition = discretizeNodeIndex(game.getGhostCurrentNodeIndex(Constants.GHOST.INKY), game.getNumberOfNodes());
        this.pinkyPosition = discretizeNodeIndex(game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY), game.getNumberOfNodes());
        this.suePosition = discretizeNodeIndex(game.getGhostCurrentNodeIndex(Constants.GHOST.SUE), game.getNumberOfNodes());

        this.nextMoveTowardsPill = game.getNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePillsIndices(), Constants.DM.PATH),
                Constants.DM.PATH).ordinal();

        this.nextMoveTowardsPowerPill = game.getNumberOfActivePills() > 0 ? game.getNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), game.getActivePowerPillsIndices(), Constants.DM.PATH),
                Constants.DM.PATH).ordinal() : nextMoveTowardsPill;

        List<Integer> activeGhostsIndices = new ArrayList<>();

        if (game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY) > -1)
            activeGhostsIndices.add(game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY));

        if (game.getGhostCurrentNodeIndex(Constants.GHOST.INKY) > -1)
            activeGhostsIndices.add(game.getGhostCurrentNodeIndex(Constants.GHOST.INKY));

        if (game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY) > -1)
            activeGhostsIndices.add(game.getGhostCurrentNodeIndex(Constants.GHOST.PINKY));

        if (game.getGhostCurrentNodeIndex(Constants.GHOST.SUE) > -1)
            activeGhostsIndices.add(game.getGhostCurrentNodeIndex(Constants.GHOST.SUE));

        this.nextMoveTowardsGhost = activeGhostsIndices.size() > 0 ? game.getNextMoveTowardsTarget(
                game.getPacmanCurrentNodeIndex(),
                game.getClosestNodeIndexFromNodeIndex(game.getPacmanCurrentNodeIndex(), activeGhostsIndices.stream().mapToInt(Integer::intValue).toArray(), Constants.DM.PATH),
                Constants.DM.PATH).ordinal() : nextMoveTowardsPill;

        this.numberOfNodesInLevel = game.getNumberOfNodes();
        this.numberOfTotalPillsInLevel = game.getNumberOfPills();
        this.numberOfTotalPowerPillsInLevel = game.getNumberOfPowerPills();
    }

    public int discretizeNodeIndex(double currentNodeIndex, double numberOfNodes) {
        double aux = currentNodeIndex / (numberOfNodes - 1);

        if (aux < 0.25)
            return 0;
        else if (aux < 0.5)
            return 1;
        else if (aux < 0.75)
            return 2;
        else
            return 3;
    }

    @Override
    public String getSaveString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(this.DirectionChosen + ";");
        //stringbuilder.append(this.mazeIndex + ";");
        //stringbuilder.append(this.currentLevel + ";");
        stringbuilder.append(this.pacmanPosition + ";");
        //stringbuilder.append(this.pacmanLivesLeft + ";");
        //stringbuilder.append(this.currentScore + ";");
        //stringbuilder.append(this.totalGameTime + ";");
        //stringbuilder.append(this.currentLevelTime + ";");
        //stringbuilder.append(this.numOfPillsLeft + ";");
        //stringbuilder.append(this.numOfPowerPillsLeft + ";");
        stringbuilder.append(this.blinkyPosition + ";");
        stringbuilder.append(this.inkyPosition + ";");
        stringbuilder.append(this.pinkyPosition + ";");
        stringbuilder.append(this.suePosition + ";");
        stringbuilder.append(this.isBlinkyEdible + ";");
        stringbuilder.append(this.isInkyEdible + ";");
        stringbuilder.append(this.isPinkyEdible + ";");
        stringbuilder.append(this.isSueEdible + ";");
        //stringbuilder.append(this.blinkyDist + ";");
        //stringbuilder.append(this.inkyDist + ";");
        //stringbuilder.append(this.pinkyDist + ";");
        //stringbuilder.append(this.sueDist + ";");
        stringbuilder.append(this.blinkyDir + ";");
        stringbuilder.append(this.inkyDir + ";");
        stringbuilder.append(this.pinkyDir + ";");
        stringbuilder.append(this.sueDir + ";");
        //stringbuilder.append(this.numberOfNodesInLevel + ";");
        //stringbuilder.append(this.numberOfTotalPillsInLevel + ";");
        //stringbuilder.append(this.numberOfTotalPowerPillsInLevel + ";");
        stringbuilder.append(this.nextMoveTowardsPill + ";");
        stringbuilder.append(this.nextMoveTowardsPowerPill + ";");
        stringbuilder.append(this.nextMoveTowardsGhost + ";");

        return stringbuilder.toString();
    }

    @Override
    public List<Integer> getDataList() {
        List<Integer> list = new ArrayList<>();

        //list.add(this.mazeIndex);
        list.add(this.pacmanPosition);
        list.add(this.blinkyPosition);
        list.add(this.inkyPosition);
        list.add(this.pinkyPosition);
        list.add(this.suePosition);
        list.add(this.isBlinkyEdible);
        list.add(this.isInkyEdible);
        list.add(this.isPinkyEdible);
        list.add(this.isSueEdible);
        //list.add(this.blinkyDist);
        //list.add(this.inkyDist);
        //list.add(this.pinkyDist);
        //list.add(this.sueDist);
        list.add(this.blinkyDir);
        list.add(this.inkyDir);
        list.add(this.pinkyDir);
        list.add(this.sueDir);
        list.add(this.nextMoveTowardsPill);
        list.add(this.nextMoveTowardsPowerPill);
        list.add(this.nextMoveTowardsGhost);

        return list;
    }
}
