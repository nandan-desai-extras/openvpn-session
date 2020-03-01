# OpenVPN-Session

This is a small java library to manage client sessions in OpenVPN community server. It parses logs in /var/log/syslog and has callback methods when a clients connects and disconnects from the server.


```java 
SessionMonitor monitor=new SessionMonitor();  
monitor.start(new ClientSessionListener() {  
    @Override  
  public void onSessionStart(ClientSession clientSession) {  
        System.out.println("client session started!!");  
        System.out.println(clientSession);  
    }  
  
    @Override  
  public void onSessionEnd(ClientSession clientSession) {  
        System.out.println("client session stopped!!");  
        System.out.println(clientSession);  
    }  
});
```

## License
MIT
