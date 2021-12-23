package com.plugin.sequentialcommands;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class SSHConnect {

    private String userName;
    private String sshKeyPass;
    private String hostName;
    boolean usePrivKey;

    public SSHConnect(String userName, String sshKeyPass, String hostName, boolean usePrivKey) {

        this.userName = userName;
        this.sshKeyPass = sshKeyPass;
        this.hostName = hostName;
        this.usePrivKey = usePrivKey;

    }

    public Session connect() throws JSchException {

        JSch jsch = new JSch();
        Session session = jsch.getSession(userName, hostName, 22);

        //TODO: Remove this when deploying for prod use. Or add plugin input-option...
        session.setConfig("StrictHostKeyChecking", "no");

        //jsch.setKnownHosts("~/.ssh/known_hosts");
        if(usePrivKey) {
//            sshKeyPass = sshKeyPass.replace("-----BEGIN RSA PRIVATE KEY-----", "-----BEGIN RSA PRIVATE KEY-----\r\n").replace("-----END RSA PRIVATE KEY-----", "\r\n-----END RSA PRIVATE KEY-----");
            byte[] privKey = sshKeyPass.getBytes(StandardCharsets.US_ASCII);
            jsch.addIdentity("privKey",privKey, null, null);
        } else {
            session.setPassword(sshKeyPass);
        }
        session.connect();

        return session;
    }

    public PrintStream printStream(Channel channel) throws Exception {

        OutputStream ops = channel.getOutputStream();
        PrintStream ps = new PrintStream(ops, true);
        channel.connect();

        return ps;
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
            } if(channel.isClosed()) {
                break;
            }

            Thread.sleep(300);
        }
    }

}
