package spotifyApi;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Properties;

public class CredentialsObtainer {
    private static final String CLIENT_ID_KEY = "clientID";
    private static final String SECRET_KEY = "secret";
    private String clientId;
    private String secret;

    public CredentialsObtainer() throws IOException{
        Properties prop = new Properties();
        InputStream input = new FileInputStream("D:\\Progg\\Workarea\\spotifylyricssystemtray\\credentials.txt");
        prop.load(input);
        input.close();
        clientId = prop.getProperty(CLIENT_ID_KEY);
        secret = prop.getProperty(SECRET_KEY);
    }

    public String getBase64encodedCredentials() {
            String toEncode = clientId + ":" + secret;
            return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    public String getClientId() {
        return clientId;
    }

}
