package dataRecording;

import pacman.game.Constants;

import java.util.List;

public abstract class DataTuple {

    public enum DiscreteTag {
        VERY_LOW, LOW, MEDIUM, HIGH, VERY_HIGH, NONE;

        public static DiscreteTag DiscretizeDouble(double aux) {
            if (aux < 0.1)
                return DiscreteTag.VERY_LOW;
            else if (aux <= 0.3)
                return DiscreteTag.LOW;
            else if (aux <= 0.5)
                return DiscreteTag.MEDIUM;
            else if (aux <= 0.7)
                return DiscreteTag.HIGH;
            else
                return DiscreteTag.VERY_HIGH;
        }
    }

    public int DirectionChosen;

    // General game state this - not normalized!
    public int mazeIndex;
    public int currentLevel;
    public int pacmanPosition;
    public int pacmanLivesLeft;
    public int currentScore;
    public int totalGameTime;
    public int currentLevelTime;
    public int numOfPillsLeft;
    public int numOfPowerPillsLeft;

    // Ghost this, dir, dist, edible - BLINKY, INKY, PINKY, SUE
    public int isBlinkyEdible = 0;
    public int isInkyEdible = 0;
    public int isPinkyEdible = 0;
    public int isSueEdible = 0;

    public int blinkyDist = DiscreteTag.NONE.ordinal();
    public int inkyDist = DiscreteTag.NONE.ordinal();
    public int pinkyDist = DiscreteTag.NONE.ordinal();
    public int sueDist = DiscreteTag.NONE.ordinal();

    public int blinkyDir;
    public int inkyDir;
    public int pinkyDir;
    public int sueDir;

    // Util data - useful for normalization
    public int numberOfNodesInLevel;
    public int numberOfTotalPillsInLevel;
    public int numberOfTotalPowerPillsInLevel;
    private int maximumDistance = 150;

    public abstract String getSaveString();

    public abstract List<Integer> getDataList();

    /**
     * Used to normalize distances. Done via min-max normalization. Supposes
     * that minimum possible distance is 0. Supposes that the maximum possible
     * distance is 150.
     *
     * @param dist Distance to be normalized
     * @return Normalized distance
     */
    public double normalizeDistance(int dist) {
        return ((dist - 0) / (double) (this.maximumDistance - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeDistance(int dist) {
        if (dist == -1)
            return DiscreteTag.NONE;
        double aux = this.normalizeDistance(dist);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    public double normalizeLevel(int level) {
        return ((level - 0) / (double) (Constants.NUM_MAZES - 0)) * (1 - 0) + 0;
    }

    public double normalizePosition(int position) {
        return ((position - 0) / (double) (this.numberOfNodesInLevel - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizePosition(int pos) {
        double aux = this.normalizePosition(pos);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    public double normalizeBoolean(boolean bool) {
        if (bool) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    public double normalizeNumberOfPills(int numOfPills) {
        return ((numOfPills - 0) / (double) (this.numberOfTotalPillsInLevel - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeNumberOfPills(int numOfPills) {
        double aux = this.normalizeNumberOfPills(numOfPills);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    public double normalizeNumberOfPowerPills(int numOfPowerPills) {
        return ((numOfPowerPills - 0) / (double) (this.numberOfTotalPowerPillsInLevel - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeNumberOfPowerPills(int numOfPowerPills) {
        double aux = this.normalizeNumberOfPowerPills(numOfPowerPills);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    public double normalizeTotalGameTime(int time) {
        return ((time - 0) / (double) (Constants.MAX_TIME - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeTotalGameTime(int time) {
        double aux = this.normalizeTotalGameTime(time);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    public double normalizeCurrentLevelTime(int time) {
        return ((time - 0) / (double) (Constants.LEVEL_LIMIT - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeCurrentLevelTime(int time) {
        double aux = this.normalizeCurrentLevelTime(time);
        return DiscreteTag.DiscretizeDouble(aux);
    }

    /**
     * Max score value lifted from highest ranking PacMan controller on PacMan
     * vs Ghosts website: http://pacman-vs-ghosts.net/controllers/1104
     *
     * @param score
     * @return
     */
    public double normalizeCurrentScore(int score) {
        return ((score - 0) / (double) (82180 - 0)) * (1 - 0) + 0;
    }

    public DiscreteTag discretizeCurrentScore(int score) {
        double aux = this.normalizeCurrentScore(score);
        return DiscreteTag.DiscretizeDouble(aux);
    }

}
