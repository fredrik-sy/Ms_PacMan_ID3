package dataRecording;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;

public class HugoDataTuple extends DataTuple {
    //HUGOS GLORIUS SHIT
    //Enums ----------------------------------------------------
    public enum Quad {TR, TL, BR, BL}

    public enum Closeness {closeAF, close, medium, far, farAF}

    //Pac stuff -----------------------------------------------
    public int pacDir;
    public int pacQuad;

    //Ghost stuff ---------------------------------------------
    //pinky
    public int pinkyQuad;
    public int pinkyCloseness;

    //blinky
    public int blinkyQuad;
    public int blinkyCloseness;

    //inky
    public int inkyQuad;
    public int inkyCloseness;

    //clyde
    public int clydeQuad;
    public int clydeCloseness;

    //closest alive ghost
    public int closestDir;
    public int closestMove;
    public int closestQuad;
    public int closestEatable;
    public int closestCloseness;

    //Pill stuff ------------------------------------------------------
    //pill
    public int closestPillMove;
    public int closestPillCloseness;

    //powerPill
    public int closestPowerPillMove;
    public int closestPowerPillCloseness;

    private int GetQuad(int x, int y) {
        if (x < 108 / 2 && y < 116 / 2) {
            return Quad.TL.ordinal();
        }
        if (x > 108 / 2 && y < 116 / 2) {
            return Quad.TR.ordinal();
        }
        if (x < 108 / 2 && y > 116 / 2) {
            return Quad.BL.ordinal();
        } else {
            return Quad.BR.ordinal();
        }
    }

    private int GetCloseness(Game game, int index1, int index2) {
        int dist = game.getShortestPathDistance(index1, index2);

        if (dist == -1) {
            return Closeness.farAF.ordinal();
        } else if (dist < 5) {
            return Closeness.closeAF.ordinal();
        } else if (dist < 10) {
            return Closeness.close.ordinal();
        } else if (dist < 15) {
            return Closeness.medium.ordinal();
        } else if (dist < 20) {
            return Closeness.far.ordinal();
        } else {
            return Closeness.farAF.ordinal();
        }
    }

    private GHOST GetClosestGhost(Game game) {
        GHOST ghost = GHOST.PINKY;
        int dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY));

        if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY)) < dist) {
            dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY));
            ghost = GHOST.BLINKY;
        } else if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY)) < dist) {
            dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY));
            ghost = GHOST.INKY;
        } else if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE)) < dist) {
            dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE));
            ghost = GHOST.SUE;
        }
        return ghost;
    }

    private int GetClosestPill(Game game) {
        int dist = 1000000;
        int best = game.getPacmanCurrentNodeIndex();

        for (int index : game.getPillIndices()) {
            if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index) < dist) {
                best = index;
                dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
            }
        }

        return best;
    }

    private int GetClosestPowerPill(Game game) {
        int dist = 1000000;
        int best = game.getPacmanCurrentNodeIndex();

        for (int index : game.getPowerPillIndices()) {
            if (game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index) < dist) {
                best = index;
                dist = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
            }
        }

        return best;
    }

    public HugoDataTuple(Game game, MOVE move) {
        //Pac stuff
        if (move == MOVE.NEUTRAL) {
            pacDir = game.getPacmanLastMoveMade().ordinal();
        } else {
            pacDir = move.ordinal();
        }
        pacQuad = GetQuad(game.getNodeXCood(game.getPacmanCurrentNodeIndex()), game.getNodeYCood(game.getPacmanCurrentNodeIndex()));

        //Ghost stuff
        pinkyQuad = GetQuad(game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.PINKY)), game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.PINKY)));
        pinkyCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.PINKY));

        blinkyQuad = GetQuad(game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.BLINKY)), game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.BLINKY)));
        blinkyCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.BLINKY));

        inkyQuad = GetQuad(game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.INKY)), game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.INKY)));
        inkyCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.INKY));

        clydeQuad = GetQuad(game.getNodeXCood(game.getGhostCurrentNodeIndex(GHOST.SUE)), game.getNodeYCood(game.getGhostCurrentNodeIndex(GHOST.SUE)));
        clydeCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(GHOST.SUE));

        //Closest ghost stuff
        GHOST ghost = GetClosestGhost(game);
        closestDir = game.getGhostLastMoveMade(ghost).ordinal();
        closestMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), DM.PATH).ordinal();
        closestQuad = GetQuad(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)), game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)));
        closestEatable = game.isGhostEdible(ghost) ? 1 : 0;
        closestCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));

        //Pill stuff
        closestPillMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), GetClosestPill(game), DM.PATH).ordinal();
        closestPillCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), GetClosestPill(game));

        closestPowerPillMove = game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), GetClosestPowerPill(game), DM.PATH).ordinal();
        closestPowerPillCloseness = GetCloseness(game, game.getPacmanCurrentNodeIndex(), GetClosestPowerPill(game));
    }

    @Override
    public String getSaveString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(this.pacDir + ";");
        stringbuilder.append(this.pacQuad + ";");
        stringbuilder.append(this.pinkyQuad + ";");
        stringbuilder.append(this.pinkyCloseness + ";");
        stringbuilder.append(this.blinkyQuad + ";");
        stringbuilder.append(this.blinkyCloseness + ";");
        stringbuilder.append(this.inkyQuad + ";");
        stringbuilder.append(this.inkyCloseness + ";");
        stringbuilder.append(this.clydeQuad + ";");
        stringbuilder.append(this.clydeCloseness + ";");
        stringbuilder.append(this.closestDir + ";");
        stringbuilder.append(this.closestMove + ";");
        stringbuilder.append(this.closestQuad + ";");
        stringbuilder.append(this.closestEatable + ";");
        stringbuilder.append(this.closestCloseness + ";");
        stringbuilder.append(this.closestPillMove + ";");
        stringbuilder.append(this.closestPillCloseness + ";");
        stringbuilder.append(this.closestPowerPillMove + ";");
        stringbuilder.append(this.closestPowerPillCloseness + ";");

        return stringbuilder.toString();
    }

    @Override
    public List<Integer> getDataList() {
        List<Integer> list = new ArrayList<>();

        list.add(this.pacQuad);
        list.add(this.pinkyQuad);
        list.add(this.pinkyCloseness);
        list.add(this.blinkyQuad);
        list.add(this.blinkyCloseness);
        list.add(this.inkyQuad);
        list.add(this.inkyCloseness);
        list.add(this.clydeQuad);
        list.add(this.clydeCloseness);
        list.add(this.closestDir);
        list.add(this.closestMove);
        list.add(this.closestQuad);
        list.add(this.closestEatable);
        list.add(this.closestCloseness);
        list.add(this.closestPillMove);
        list.add(this.closestPillCloseness);
        list.add(this.closestPowerPillMove);
        list.add(this.closestPowerPillCloseness);

        return list;
    }
}