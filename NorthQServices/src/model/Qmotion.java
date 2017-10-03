package model;

import model.json.BinarySensor;

public class Qmotion extends Thing implements IThing {
    private BinarySensor bs;

    public Qmotion(BinarySensor bs) {
        super();
        this.bs = bs;
    }

}
