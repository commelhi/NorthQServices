package model;

import model.json.BinarySwitch;

public class Qplug extends Thing implements IThing {
    private BinarySwitch bs;

    public Qplug(BinarySwitch bs) {
        super();
        this.bs = bs;
    }

    public boolean getStatus() {
        return (bs.pos != 0);
    }

    public BinarySwitch getBs() {
        return bs;
    }

    public void setBs(BinarySwitch bs) {
        this.bs = bs;
    }

    public String getNodeId() {
        return bs.node_id + "";
    }
}
