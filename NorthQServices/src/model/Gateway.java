package model;

import java.util.ArrayList;

import model.json.BinarySensor;
import model.json.BinarySwitch;
import model.json.Thermostat;

public class Gateway {
    private String gatewayId;
    private ArrayList<Thing> things;

    public Gateway(String gatewayId, ArrayList<BinarySwitch> bSwitch, ArrayList<BinarySensor> bSensor,
            ArrayList<Thermostat> termostat) {
        this.gatewayId = gatewayId;
        things = new ArrayList<Thing>();
        for (int i = 0; i < bSwitch.size(); i++) {
            things.add(new Qplug(bSwitch.get(i)));
        }
        for (int i = 0; i < bSensor.size(); i++) {
            things.add(new Qmotion(bSensor.get(i)));
        }
        for (int i = 0; i < termostat.size(); i++) {
            things.add(new Qthermostat(termostat.get(i)));
        }
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public ArrayList<Thing> getThings() {
        return things;
    }

    public void setThings(ArrayList<Thing> things) {
        this.things = things;
    }

}
