package model;

import java.util.ArrayList;

public class NorthNetwork {
    private String token;
    private String userId;
    private String houseId;
    private ArrayList<Gateway> gateways;

    public NorthNetwork(String token, String userId, String houseId, ArrayList<Gateway> gateways) {
        super();
        this.token = token;
        this.userId = userId;
        this.houseId = houseId;
        this.gateways = gateways;
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

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public ArrayList<Gateway> getGateways() {
        return gateways;
    }

    public void setGateways(ArrayList<Gateway> gateways) {
        this.gateways = gateways;
    }

}
