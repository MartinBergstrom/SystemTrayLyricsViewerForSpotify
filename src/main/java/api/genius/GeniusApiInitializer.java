package api.genius;

import api.ApiInitializer;
import http.MyHttpClient;

public class GeniusApiInitializer implements ApiInitializer {

    private MyHttpClient client;

    public GeniusApiInitializer(MyHttpClient client) throws Exception {
        this.client = client;
    }


    @Override
    public boolean isInitialized() {
        return true;
    }

    @Override
    public void authorizeUser() {

    }

    @Override
    public void launchApi() {

    }

    @Override
    public void launchApi(String accessToken) {

    }
}
