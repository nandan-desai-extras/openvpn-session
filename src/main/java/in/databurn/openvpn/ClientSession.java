package in.databurn.openvpn;

public class ClientSession {
    private String username;
    private String originalAddress;
    private Integer clientPort;
    private String virtualAddress;
    private Long timestamp;

    public ClientSession(){}

    public String getUsername() {
        return username;
    }

    public String getOriginalAddress() {
        return originalAddress;
    }

    public int getClientPort() {
        return clientPort;
    }

    public String getVirtualAddress() {
        return virtualAddress;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setOriginalAddress(String originalAddress) {
        this.originalAddress = originalAddress;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public void setVirtualAddress(String virtualAddress) {
        this.virtualAddress = virtualAddress;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "Client Username: "+username+"\n"
                +"Client Original Public IP Addr: "+ originalAddress +"\n"
                +"Client Port number: "+clientPort+"\n"
                +"Assigned Private IP Addr: "+ virtualAddress +"\n"
                +"Timestamp: "+timestamp+"\n";
    }
}
