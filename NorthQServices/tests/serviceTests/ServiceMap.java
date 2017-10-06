package serviceTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.NorthNetwork;
import services.CredentialsService;
import services.NorthqServices;

public class ServiceMap {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void mapGenerationTest() throws Exception {
        NorthqServices ns = new NorthqServices();
        CredentialsService cs = new CredentialsService();
        ArrayList<String> user = cs.getUserCredentials();
        NorthNetwork network = ns.mapNorthQNetwork(user.get(0), user.get(1));
        assertEquals(network.getUserId(), "2166");
        assertTrue(network.getHouses().length >= 1);
        // assertTrue(network.getGateways().size() >= 1);

    }

}
