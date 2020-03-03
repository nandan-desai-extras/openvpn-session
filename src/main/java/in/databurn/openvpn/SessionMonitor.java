package in.databurn.openvpn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SessionMonitor {
    private boolean sessionMonitorFlag = false;


    public void start(ClientSessionListener clientSessionListener) {
        sessionMonitorFlag=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String[] command = { "/bin/sh","-c", "tail -f /var/log/syslog | grep \"openvpn\""};
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            process.getInputStream()));
                    String cmdOutput = null;

                    HashMap<String, ClientSession> sessionRecords = new HashMap<>();

                    while ((cmdOutput = reader.readLine()) != null && sessionMonitorFlag) {
                        if (cmdOutput.contains("Peer Connection Initiated")) {
                            ClientSession clientSession = new ClientSession();
                            String[] parts = cmdOutput.split(": ");
                            String[] rParts = parts[1].split(" ");
                            String ipAddressWithPort = rParts[0];
                            String clientUsername = rParts[1].replace("[", "").replace("]", "");
                            String ipAddress = ipAddressWithPort.split(":")[0];
                            int port = Integer.parseInt(ipAddressWithPort.split(":")[1]);
                            //System.out.println("username: "+clientUsername+", ip: "+ipAddress+", port: "+port);
                            clientSession.setOriginalAddress(ipAddress);
                            clientSession.setUsername(clientUsername);
                            clientSession.setClientPort(port);
                            sessionRecords.put(clientUsername, clientSession);
                        } else if (cmdOutput.contains("primary virtual IP")) {
                            String[] parts = cmdOutput.split(": ");
                            String usernameIpParts = parts[1].split(" ")[0];
                            String username = usernameIpParts.split("/")[0];

                            ClientSession clientSession = sessionRecords.get(username);
                            String privateIpAddress = parts[3];
                            clientSession.setVirtualAddress(privateIpAddress);
                            sessionRecords.put(username, clientSession);
                        } else if (cmdOutput.contains("Data Channel Decrypt")) {
                            String[] parts = cmdOutput.split(": ");
                            String usernameIpParts = parts[1].split(" ")[0];
                            String username = usernameIpParts.split("/")[0];

                            ClientSession clientSession = sessionRecords.get(username);
                            clientSession.setTimestamp(System.currentTimeMillis());
                            sessionRecords.put(username, clientSession);

                            clientSessionListener.onSessionStart(clientSession);
                        } else if (cmdOutput.contains("client-instance exiting")) {
                            String[] parts = cmdOutput.split(": ");
                            String usernameIpParts = parts[1].split(" ")[0];
                            String username = usernameIpParts.split("/")[0];

                            ClientSession clientSession = sessionRecords.get(username);
                            clientSession.setTimestamp(System.currentTimeMillis());
                            sessionRecords.remove(username);

                            clientSessionListener.onSessionEnd(clientSession);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void stop() {
        sessionMonitorFlag = false;
    }
}
