import api.ApiInitializer;
import api.ApiInitializers;
import http.MyHttpClient;
import server.RedirectServer;

import java.util.List;

public class Launcher {

    public static void main(String[] args) throws Exception {
        ApiInitializers apiInitializers = new ApiInitializers(new MyHttpClient());

        List<ApiInitializer> initializerList = apiInitializers.getAll();
        if (initializerList.stream().allMatch(ApiInitializer::isInitialized)) {
            initializerList.forEach(ApiInitializer::launchApi);
        } else {
            new RedirectServer(apiInitializers);
        }

    }

}
