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
import model.Qplug;
import model.Thing;
import model.json.Notification;
import model.json.UserNotification;
import model.json.UserNotificationHolder;
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

    @Test
    public void plugOnTest() {
        Qplug plug = null;
        for (Thing t : network.getGateways().get(0).getThings()) {
            if (t instanceof Qplug) {
                plug = (Qplug) t;
            }
        }
        try {
            if (plug != null) {
                boolean res1 = ns.turnOffPlug(plug, network.getToken(), network.getUserId(),
                        network.getGateways().get(0).getGatewayId());
                boolean res2 = ns.turnOnPlug(plug, network.getToken(), network.getUserId(),
                        network.getGateways().get(0).getGatewayId());
                assertTrue(res1);
                assertTrue(res2);

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            NorthNetwork newNetwork = ns.mapNorthQNetwork(user.get(0), user.get(1));
            for (Thing t : newNetwork.getGateways().get(0).getThings()) {
                if (t instanceof Qplug) {
                    Qplug updatedPlug = (Qplug) t;
                    if (updatedPlug.getNodeID() == updatedPlug.getNodeID()) {
                        assertTrue(updatedPlug.getStatus());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    /**/
    @Test
    public void plugOffTest() {
        Qplug plug = null;
        for (Thing t : network.getGateways().get(0).getThings()) {
            if (t instanceof Qplug) {
                plug = (Qplug) t;
            }
        }
        try {
            if (plug != null) {
                boolean res2 = ns.turnOnPlug(plug, network.getToken(), network.getUserId(),
                        network.getGateways().get(0).getGatewayId());
                boolean res1 = ns.turnOffPlug(plug, network.getToken(), network.getUserId(),
                        network.getGateways().get(0).getGatewayId());

                assertTrue(res1); // prone to false negatives - rerun code likely an http issue
                assertTrue(res2);

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            NorthNetwork newNetwork = ns.mapNorthQNetwork(user.get(0), user.get(1));
            for (Thing t : newNetwork.getGateways().get(0).getThings()) {
                if (t instanceof Qplug) {
                    Qplug updatedPlug = (Qplug) t;
                    if (updatedPlug.getNodeID() == updatedPlug.getNodeID()) {
                        assertTrue(!updatedPlug.getStatus());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

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

        assertEquals(mot.getStatus(), false);
    }

    @Test
    public void notificationTest() throws IOException, Exception {
        UserNotificationHolder res = ns.getNotificationArray(network.getUserId(), network.getToken(),
                network.getHouses()[0].id + "", "1");
        assertTrue(res.UserNotifications.size() >= 1);
    }

    @Test
    public void triggerTest() {
        UserNotificationHolder mock = new UserNotificationHolder();
        UserNotification mockNot = new UserNotification();
        mockNot.notification = new Notification();
        mockNot.notification.timestamp = System.currentTimeMillis() / 1000;
        mock.UserNotifications = new ArrayList<UserNotification>();
        mock.UserNotifications.add(mockNot);
        assertTrue(ns.isTriggered(mock));

        mockNot.notification.timestamp = 5000000 / 1000;
        mock.UserNotifications.remove(0);
        mock.UserNotifications.add(mockNot);
        assertTrue(!ns.isTriggered(mock));
    }
}
