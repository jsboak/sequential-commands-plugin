package com.plugin.sequentialcommands;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/*
Created by Jake Cohen on 3/20/2022
This Rundeck plugin is used to send sequential commands via SSH.
 */
public class SSHConnect {

    private String userName;
    private String sshKeyPass;
    private String hostName;
    private String strictHostKey;
    boolean usePrivKey;

    public SSHConnect(String userName, String sshKeyPass, String hostName, boolean usePrivKey, String strictHostKey) {

        this.userName = userName;
        this.sshKeyPass = sshKeyPass;
        this.hostName = hostName;
        this.usePrivKey = usePrivKey;
        this.strictHostKey = strictHostKey;

    }

    public Session connect() throws JSchException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(userName, hostName, 22);

        if (strictHostKey.equals("true")) {
            session.setConfig("strictHostKeyChecking", "yes");
        } else {
            session.setConfig("StrictHostKeyChecking", "no");
        }

        if(usePrivKey) {
            byte[] privKey = sshKeyPass.getBytes(StandardCharsets.US_ASCII);
            jsch.addIdentity("privKey",privKey, null, null);
        } else {
            session.setPassword(sshKeyPass);
        }
        session.connect();

        return session;
    }

    static void printResult(InputStream input,
                            Channel channel) throws Exception {
        int SIZE = 1024;
        byte[] tmp = new byte[SIZE];
        while (true) {
            while (input.available() > 0)
            {
                int i = input.read(tmp, 0, SIZE);
                if(i < 0)
                    break;
                System.out.print(new String(tmp, 0, i));
            }

            channel.disconnect();

            if(channel.isClosed()) {
                break;
            }

            Thread.sleep(500);
        }
    }

}
