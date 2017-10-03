package Services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class NorthNetworkServices {
    private NetworkUtils networkUtils = new NetworkUtils();

    // Requires:
    // Returns: a string representation of JSON object returned by northQ restful services
    public String getTokenJSON(String userId, String password) throws IOException, Exception {
        Form form = new Form();
        form.param("username", userId);
        form.param("password", password);
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/token/new.json", form);
        // Test success of request
        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            response.close();
            return json;
        } else {
            String json = response.readEntity(String.class);
            response.close();
            return json;
        }
    }

    // Requires:
    // Returns: a string representation of JSON object returned by northQ restful services
    public String getTokenJSON() throws Exception {
        Form form = new Form();
        ArrayList<String> info = logInInfo();
        form.param("username", info.get(0));
        form.param("password", info.get(1));
        Response response = networkUtils.getHttpPostResponse("https://homemanager.tv/token/new.json", form);
        // Test success of request
        if (response.getStatus() == 200) {
            String json = response.readEntity(String.class);
            response.close();
            return json;
        } else {
            response.close();
            throw new NullPointerException("token not recieved http error code: " + response.getStatus());
        }

    }

    // Requires:
    // Returns: An arraylist containing username and password
    public ArrayList<String> logInInfo() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\file.txt"));
        ArrayList<String> list = new ArrayList<String>();
        String line = br.readLine();
        list.add(line);
        line = br.readLine();
        list.add(line);
        br.close();
        return list;
    }

    // Requires:
    // Returns: An http response
    public String getTokenString(String userId, String password) throws IOException, Exception {
        return getJsonMap(getTokenJSON(userId, password)).get("token").toString();
    }

    // Requires:
    // Returns: Returns true/false depending on whether or not token is still valid
    public boolean verifyToken() {
        return false;
    }

    // Requires: gatewayId, userId and a token (all strings)
    // Returns: A http response
    public Response getGatawayStatus(String gatewayId, String userId, String token) throws IOException, Exception {
        String URL = "https://homemanager.tv/main/getGatewayStatus?gateway=" + gatewayId + "&user=" + userId + "&token="
                + token;
        Response response = networkUtils.getHttpGetResponse(URL);
        System.out.println(response.readEntity(String.class));
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

    // Requires: a JSON formatted string
    // Returns: A map consisting of objects translated from JSON
    public Map<String, Object> getJsonMap(String jsonString) throws IOException {
        return new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
        }.getType());
    }
}
