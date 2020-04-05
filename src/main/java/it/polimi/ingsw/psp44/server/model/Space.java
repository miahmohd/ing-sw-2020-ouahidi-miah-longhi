package it.polimi.ingsw.psp44.server.model;

/**
 * The atomic part of the board
 */
public class Space {
    /**
     * Contains information about what is built in the specific Space
     * his range is from 0 (ground) to 3 (maximum number of block that can be placed in a space)
     */
    private int level;

    /**
     * indicate presence of a dome in a space
     */
    private boolean dome;

    /**
     * if isn't null indicate the worker in a space
     */
    private Worker worker;


    public Space() {
        this.level = 0;
        this.dome = false;
        this.worker = null;
    }


    public Space(int level, boolean hasDome, Worker worker) {
        this.level = level;
        this.dome = hasDome;
        this.worker = worker;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isDome() {
        return dome;
    }

    public void setDome(boolean dome) {
        this.dome = dome;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    /**
     * checks if the building in this space is complate
     * @return true if there are 3 block and the dome, false otherwise
     */
    public boolean isComplete() {
        return this.isFinalLevel() && this.isDome();
    }

    /**
     * checks if all the three blocks have been built in a space
     * @return true if level 3 have been reached, false otherwise
     */
    public boolean isFinalLevel() {
        return this.level == 3;
    }

    /**
     * checks if no blocks have been built in a space
     * @return true if no blocks have been built, false otherwise
     */
    public boolean isGroundLevel() {
        return this.level == 0;
    }

}
