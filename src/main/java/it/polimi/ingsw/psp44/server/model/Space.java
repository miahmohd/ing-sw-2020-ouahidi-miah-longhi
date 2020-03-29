package it.polimi.ingsw.psp44.server.model;

public class Space {

    private int level;
    private boolean dome;
    private Worker worker;


    public Space(){
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

    public boolean isComplete(){
        return this.isFinalLevel() && this.isDome();
    }

    public boolean isFinalLevel(){
        return this.level==3;
    }

}
