package in.databurn.openvpn;

public interface ClientSessionListener {
    void onSessionStart(ClientSession clientSession);
    void onSessionEnd(ClientSession clientSession);
}
