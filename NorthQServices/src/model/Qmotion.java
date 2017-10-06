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

    public boolean getStatus() {
        return (bs.armed != 0);
    }

    public float getHumidity() {
        if (bs != null) {
            return bs.sensors.get(2).value;
        }
        return 0;
    }

    public float getLight() {
        if (bs != null) {
            return bs.sensors.get(1).value;
        }
        return 0;
    }

    public float getTmp() {
        if (bs != null) {
            return bs.sensors.get(0).value;
        }
        return 0;
    }
}
