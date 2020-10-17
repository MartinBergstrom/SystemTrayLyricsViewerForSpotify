package api;

public interface ApiInitializer {

    boolean isInitialized();

    void authorizeUser();

    void launchApi();

    void launchApi(String authCode);

}
