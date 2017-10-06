package model;

import java.util.ArrayList;

public class NorthNetwork {
    private String token;
    private String userId;
    private String houseId;
    private ArrayList<NGateway> gateways;

    public NorthNetwork(String token, String userId, String houseId, ArrayList<NGateway> gateways) {
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

    public ArrayList<NGateway> getGateways() {
        return gateways;
    }

    public void setGateways(ArrayList<NGateway> gateways) {
        this.gateways = gateways;
    }

    @Override
    public String toString() {
        return "token: "+ token+
        		"\ngateway id: "+gateways.get(0).getGatewayId()+
        		"\nthings: "+gateways.get(0).getThings().size();
        
    }
}
