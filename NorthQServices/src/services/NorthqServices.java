package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import model.Qmotion;
import model.Qplug;

public class NorthqServices {
    private NetworkUtils networkUtils = new NetworkUtils();

    // Requires: user name and password for login
    // Returns: a string representation of JSON object returned by northQ restful services
    public Response postLogin(String username, String password) throws Exception {
        Form form = new Form();
        form.param("username", username);
        form.param("password", password);
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/token/new.json", form);
        // Test success of request
        if (response.getStatus() == 200) {
            return response;
        } else {
            response.close();
            throw new NullPointerException("token not recieved http error code: " + response.getStatus());
        }

    }
  

    // Requires: gatewayId, userId and a token (all strings)
    // Returns: A http response
    public Response getGatawayStatus(String gatewayId, String userId, String token) throws IOException, Exception {
        String URL = "https://homemanager.tv/main/getGatewayStatus?gateway=" + gatewayId + "&user=" + userId + "&token="
                + token;
        Response response = networkUtils.getHttpGetResponse(URL);
        return response;
    }

    // Requires: gatewayId, userId and a token (all strings)
    // Returns: A http response
    public String getGatewayStatusJSON(String gatewayId, String userId, String token) throws IOException, Exception {
        String URL = "https://homemanager.tv/main/getGatewayStatus?gateway=" + gatewayId + "&user=" + userId + "&token="
                + token;
        Response response = networkUtils.getHttpGetResponse(URL);
        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            response.close();
            return json;
        } else {
            response.close();
            return "";
        }
    }

    // Requires: a userId and a token both strings
    // Returns: A http response
    public Response getCurrentUserHouses(String userId, String token) throws IOException, Exception {
        String url = "https://homemanager.tv/main/getCurrentUserHouses?user=" + userId + "&token=" + token;
        return networkUtils.getHttpGetResponse(url);
    }

    // Requires: a houseId, a userId and a token (all strings)
    // Returns: An http response
    public Response getHouseGateways(String houseId, String userId, String token) throws IOException, Exception {
        String url = "https://homemanager.tv/main/getHouseGateways?house_id=" + houseId + "&user=" + userId + "&token="
                + token;
        return networkUtils.getHttpGetResponse(url);
    }

    // Requires: string representations of userId,token, houseId,page
    // Returns: An http response
    public Response getNotifications(String userId, String token, String houseId, String page)
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
    public boolean updateQplugStatus(Qplug plug, String token, String user, String gatewayId, int status)
            throws IOException, Exception {
        Form form = new Form();
        form.param("user", user);
        form.param("token", token);
        form.param("gateway", gatewayId);
        form.param("node_id", plug.getNodeId());
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

}
