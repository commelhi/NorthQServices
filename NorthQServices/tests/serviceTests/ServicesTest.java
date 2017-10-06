package serviceTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.NorthNetwork;
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
    public void motionArmTest() {

    }

    @Test
    public void motionDisarmTest() {

    }

    @Test
    public void notificanTest() {

    }

    @Test
    public void triggerTest() {

    }

}
