package model;

import java.util.ArrayList;

import model.json.GatewayStatus;

public class NGateway {
    private String gatewayId;
    private ArrayList<Thing> things;

    public NGateway(String gatewayId, GatewayStatus gatewayStatus) {
        this.gatewayId = gatewayId;
        things = new ArrayList<>();
        for (int i = 0; i < gatewayStatus.BinarySwitches.size(); i++) {
            things.add(new Qplug(gatewayStatus.BinarySwitches.get(i)));
        }
        for (int i = 0; i < gatewayStatus.BinarySensors.size(); i++) {
            things.add(new Qmotion(gatewayStatus.BinarySensors.get(i)));
        }
        for (int i = 0; i < gatewayStatus.Thermostats.size(); i++) {
            things.add(new Qthermostat(gatewayStatus.Thermostats.get(i)));
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
