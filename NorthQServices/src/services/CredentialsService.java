package services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class CredentialsService {

    public ArrayList<String> getUserCredentials() {

        ArrayList<String> credentials = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("C:\\file.txt"));
            credentials.add(br.readLine());
            credentials.add(br.readLine());
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not load credentials, create file.txt with username and password under C:\\");
        } catch (Exception e) {
            System.out.println("Problem occurred with loading credentials");
        }
        return credentials;
    }

}
