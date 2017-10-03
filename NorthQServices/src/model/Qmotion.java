package model;

import model.json.BinarySensor;

public class Qmotion extends Thing implements IThing {
    private BinarySensor bs;

    public Qmotion(BinarySensor bs) {
        super();
        this.bs = bs;
    }

    public BinarySensor getBs() {
        return bs;
    }

    public void setBs(BinarySensor bs) {
        this.bs = bs;
    }

    public String getNodeId() {
        return bs.node_id + "";
    }
}
