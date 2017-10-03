package test;

import model.NorthNetwork;
import services.NorthqServices;

public class test {

    public static void main(String[] args) throws Exception {
        NorthqServices ns = new NorthqServices();
        NorthNetwork nn = ns.mapNorthQNetwork("dtu3", "dtu3");
        System.out.println(nn.toString());
    }

}
