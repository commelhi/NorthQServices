package services;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import model.NGateway;
import model.NorthNetwork;
import model.Qmotion;
import model.Qplug;
import model.json.Gateway;
import model.json.GatewayStatus;
import model.json.House;
import model.json.User;
import model.json.UserNotification;
import model.json.UserNotificationHolder;

public class NorthqServices {
    private NetworkUtils networkUtils = new NetworkUtils();
    private Gson gson = new Gson();

    // Requires: a username and password
    // Returns: returns the NorthNetwork object
    public NorthNetwork mapNorthQNetwork(String username, String password) throws Exception {
        /*
         * WARNING: THIS DOES NOT TAKE INTO ACCOUNT WHICH GATEWAYS BELONGS TO WHICH HOUSE
         * TODO: Consider refactoring along the line.
         */
        ArrayList<NGateway> gateways = new ArrayList<NGateway>();

        User user = postLogin(username, password);

        // Gets houses.
        Response houseResponse = getCurrentUserHouses(user.user + "", user.token);
        House[] houseHolder = gson.fromJson(houseResponse.readEntity(String.class), House[].class);

        // Get gateway for each house
        for (int i = 0; i < houseHolder.length; i++) {
            House house = houseHolder[i];
            Response gatewaysResponse = getHouseGateways(house.id + "", user.user + "", user.token);
            Gateway[] gatewayArray = gson.fromJson(gatewaysResponse.readEntity(String.class), Gateway[].class);

            // Get gateway status for each gateway.
            for (int j = 0; j < gatewayArray.length; j++) {
                Gateway gateway = gatewayArray[j];
                Response gatewayStatusResponse = getGatewayStatus(gateway.serial_nr, user.user + "", user.token);
                GatewayStatus gatewayStatus = gson.fromJson(gatewayStatusResponse.readEntity(String.class),
                        GatewayStatus.class);
                NGateway nGateway = new NGateway(gateway.serial_nr, gatewayStatus);
                gateways.add(nGateway);
            }
        }

        NorthNetwork network = new NorthNetwork(user.token, user.user + "", houseHolder, gateways);

        return network;
    }

    // Requires: user name and password for login
    // Returns: a string representation of JSON object returned by northQ restful services
    public User postLogin(String username, String password) throws Exception {
        Form form = new Form();
        form.param("username", username);
        form.param("password", password);
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/token/new.json", form);
        // Test success of request
        if (response.getStatus() == 200) {
            User user = gson.fromJson(response.readEntity(String.class), User.class);
            response.close();
            return user;
        } else {
            response.close();
            throw new NullPointerException("token not recieved http error code: " + response.getStatus());
        }

    }

    // Requires: gatewayId, userId and a token (all strings)
    // Returns: A http response
    public Response getGatewayStatus(String gatewayId, String userId, String token) throws IOException, Exception {
        String URL = "https://homemanager.tv/main/getGatewayStatus?gateway=" + gatewayId + "&user=" + userId + "&token="
                + token;
        Response response = networkUtils.getHttpGetResponse(URL);
        return response;
    }

    // Requires: a userId and a token both strings
    // Returns: A http response
    private Response getCurrentUserHouses(String userId, String token) throws IOException, Exception {
        String url = "https://homemanager.tv/main/getCurrentUserHouses?user=" + userId + "&token=" + token;
        return networkUtils.getHttpGetResponse(url);
    }

    // Requires: a houseId, a userId and a token (all strings)
    // Returns: An http response
    private Response getHouseGateways(String houseId, String userId, String token) throws IOException, Exception {
        String url = "https://homemanager.tv/main/getHouseGateways?house_id=" + houseId + "&user=" + userId + "&token="
                + token;
        return networkUtils.getHttpGetResponse(url);
    }

    // Requires: string representations of userId,token, houseId,page
    // Returns: An http response
    private Response getNotifications(String userId, String token, String houseId, String page)
            throws IOException, Exception {
        String url = "https://homemanager.tv/main/getUserNotifications?user=" + userId + "&token=" + token
                + "&house_id=" + houseId + "&page=" + page;
        return networkUtils.getHttpGetResponse(url);
    }

    // Requires: A q-plug, the token, user and gatewayId
    // Returns: returns true if successfully turned on
    public boolean turnOnPlug(Qplug plug, String token, String user, String gatewayId) throws IOException, Exception {
        return updateQplugStatus(plug, token, user, gatewayId, 1);
    }

    // Requires: A q-plug, the token, user and gatewayId
    // Returns: returns true if successfully turned off
    public boolean turnOffPlug(Qplug plug, String token, String user, String gatewayId) throws IOException, Exception {
        return updateQplugStatus(plug, token, user, gatewayId, 0);
    }

    // Requires: A q-plug, the token, user and gatewayId, boolean on/off
    // Returns: returns true if successfully turned on
    private boolean updateQplugStatus(Qplug plug, String token, String user, String gatewayId, int status)
            throws IOException, Exception {
        Form form = new Form();
        form.param("user", user);
        form.param("token", token);
        form.param("gateway", gatewayId);
        form.param("node_id", plug.getNodeID());
        String stat = "0";

        if (status > 0) {
            stat = "255";
        }
        form.param("pos", stat);

        String response = networkUtils.getHttpPostResponse("https://homemanager.tv/main/setBinaryValue", form)
                .readEntity(String.class);
        return networkUtils.getJsonMap(response).get("success").toString().equals("1.0");
    }

    // Requires: requires a token
    // Returns: returns the http response
    public Response armMotion(String user, String token, String gatewayId, Qmotion motion)
            throws IOException, Exception {
        Form form = new Form();
        form.param("user", user);
        form.param("token", token);
        form.param("gateway_id", gatewayId);
        form.param("node_id", motion.getNodeID());
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/main/reArmUserComponent", form);
        return response;
    }

    // Requires: requires a token
    // Returns: returns the http response
    public Response disarmMotion(String user, String token, String gatewayId, Qmotion motion)
            throws IOException, Exception {
        Form form = new Form();
        form.param("user", user);
        form.param("token", token);
        form.param("gateway_id", gatewayId);
        form.param("node_id", motion.getNodeID());
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/main/disArmUserComponent", form);
        return response;
    }

    // Requires: a user, token,houseId and a pagenumber as strings
    // Returns: an UserNotificationHolder
    public UserNotificationHolder getNotificationArray(String user, String token, String houseId, String pageNum) {
        try {
            Response response = getNotifications(user, token, houseId, pageNum);
            String jsonString = response.readEntity(String.class);
            UserNotificationHolder notifications = gson.fromJson(jsonString, UserNotificationHolder.class);
            return notifications;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Requires: a UserNotificationsHolder object
    // Returns: Whether or nor motions beeen detected in the last 30min
    public boolean isTriggered(UserNotificationHolder notifications) {
        UserNotification latestNotification = notifications.UserNotifications.get(0);
        long latestNotifTimestamp = latestNotification.notification.timestamp * 1000;
        long diff = System.currentTimeMillis() - latestNotifTimestamp;
        if (diff < (30 * 60 * 1000)) { // 30 min
            return true;

        }
        return false;
    }

}
