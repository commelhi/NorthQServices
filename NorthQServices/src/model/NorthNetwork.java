package model;

import java.util.ArrayList;

import model.json.House;

public class NorthNetwork {
    private String token;
    private String userId;
    private House[] houses;
    private ArrayList<NGateway> gateways;

    public NorthNetwork(String token, String userId, House[] houses, ArrayList<NGateway> gateways) {
        super();
        this.token = token;
        this.userId = userId;
        this.houses = houses;
        this.gateways = gateways;
    }

    public House[] getHouses() {
        return houses;
    }

    public void setHouses(House[] houses) {
        this.houses = houses;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<NGateway> getGateways() {
        return gateways;
    }

    public void setGateways(ArrayList<NGateway> gateways) {
        this.gateways = gateways;
    }

    @Override
    public String toString() {
        return "token: " + token + "\ngateway id: " + gateways.get(0).getGatewayId() + "\nthings: "
                + gateways.get(0).getThings().size();

    }
}
