package serviceTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import model.NGateway;
import model.NorthNetwork;
import model.Qmotion;
import model.Thing;
import services.CredentialsService;
import services.NorthqServices;

public class ServicesTest {
    NorthqServices ns;
    CredentialsService cs;
    ArrayList<String> user;
    NorthNetwork network;

    @Before
    public void setUp() throws Exception {
        ns = new NorthqServices();
        cs = new CredentialsService();
        user = cs.getUserCredentials();
        network = ns.mapNorthQNetwork(user.get(0), user.get(1));
    }

    @Test
    public void mapGenerationTest() throws Exception {
        assertEquals(network.getUserId(), "2166");
        assertTrue(network.getHouses().length >= 1);
        // assertTrue(network.getGateways().size() >= 1);

    }

    /**/
    @Test
    public void plugOnTest() {

    }

    /**/
    @Test
    public void plugOffTest() {

    }

    @Test
    public void motionArmTest() throws IOException, Exception {
        // get devices find correct device and turn on:
        NGateway gateway = network.getGateways().get(0);
        ArrayList<Thing> testThings = network.getGateways().get(0).getThings();
        Qmotion mot = null;
        for (int i = 0; i < testThings.size(); i++) {
            if (testThings.get(i) instanceof Qmotion) {
                mot = (Qmotion) testThings.get(i);
            }
        }
        if (mot == null) {
            fail("no motion was detected in network!");
        }
        Response res1 = ns.disarmMotion(network.getUserId(), network.getToken(), gateway.getGatewayId(), mot);
        Response res2 = ns.armMotion(network.getUserId(), network.getToken(), gateway.getGatewayId(), mot);

        assertEquals(res1.getStatus(), 200);
        assertEquals(res2.getStatus(), 200);

        // fetch new status:
        network = ns.mapNorthQNetwork(user.get(0), user.get(1));
        gateway = network.getGateways().get(0);
        testThings = network.getGateways().get(0).getThings();
        mot = null;
        for (int i = 0; i < testThings.size(); i++) {
            if (testThings.get(i) instanceof Qmotion) {
                mot = (Qmotion) testThings.get(i);
            }
        }
        if (mot == null) {
            fail("no motion was detected in network!");
        }

        assertEquals(mot.getStatus(), true);
    }

    @Test
    public void motionDisarmTest() throws IOException, Exception {
        // get devices find correct device and turn on:
        NGateway gateway = network.getGateways().get(0);
        ArrayList<Thing> testThings = network.getGateways().get(0).getThings();
        Qmotion mot = null;
        for (int i = 0; i < testThings.size(); i++) {
            if (testThings.get(i) instanceof Qmotion) {
                mot = (Qmotion) testThings.get(i);
            }
        }
        if (mot == null) {
            fail("no motion was detected in network!");
        }
        Response res2 = ns.armMotion(network.getUserId(), network.getToken(), gateway.getGatewayId(), mot);
        Response res1 = ns.disarmMotion(network.getUserId(), network.getToken(), gateway.getGatewayId(), mot);

        assertEquals(res1.getStatus(), 200);
        assertEquals(res2.getStatus(), 200);

        // fetct new status:
        network = ns.mapNorthQNetwork(user.get(0), user.get(1));
        gateway = network.getGateways().get(0);
        testThings = network.getGateways().get(0).getThings();
        mot = null;
        for (int i = 0; i < testThings.size(); i++) {
            if (testThings.get(i) instanceof Qmotion) {
                mot = (Qmotion) testThings.get(i);
            }
        }
        if (mot == null) {
            fail("no motion was detected in network!");
        }

        assertEquals(mot.getStatus(), false);
    }

    @Test
    public void notificanTest() {

    }

    @Test
    public void triggerTest() {

    }

}
