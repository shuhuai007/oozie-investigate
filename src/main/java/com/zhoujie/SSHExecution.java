package com.zhoujie;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * SSH class for implements ssh action.
 */
public class SSHExecution {
    private Connection mConnection;
    private String mIP;
    private String mUser;
    private String mPassword;
    private String mCharset = Charset.defaultCharset().toString();
    private String mLineSeparator = System.getProperty("line.separator");

    private static final int TIME_OUT = 1;

    /**
     * Construct method.
     * @param ip ip
     * @param user username
     * @param password password
     */
    public SSHExecution(String ip, String user, String password) {
        mIP = ip;
        mUser = user;
        mPassword = password;
    }

    /**
     * Login.
     *
     * @return True if succeed to login, or False
     * @throws IOException
     */
    private boolean login() throws IOException {
        mConnection = new Connection(mIP);
        mConnection.connect();
        return mConnection.authenticateWithPassword(mUser, mPassword);
    }

    /**
     * Execute the command remotely.
     *
     * @param command The command will be executed
     * @return execution code
     * @throws Exception when execute command
     */
    public int exec(String command) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                Session session = mConnection.openSession();
                session.execCommand(command);

                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, mCharset);

                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, mCharset);

                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

                System.out.println("outStr=" + outStr);
                System.out.println("outErr=" + outErr);

                ret = session.getExitStatus();
            } else {
                throw new Exception("Failed to login remote " + mIP);
            }
        } finally {
            if (mConnection != null) {
                mConnection.close();
            }
            IOUtils.closeQuietly(stdOut);
            IOUtils.closeQuietly(stdErr);
        }
        return ret;
    }

    private String processStream(InputStream in, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
        while (true) {
            String line = br.readLine();
            if (line == null) {
                break;
            } else {
                sb.append(line).append(mLineSeparator);
            }
        }
        if (sb.length() == 0) {
            return "";
        } else {
            return sb.toString().substring(0, sb.lastIndexOf(mLineSeparator));
        }
    }

    /**
     * Main endpoint.
     * @param args input arguments array
     * @throws Exception when execute command
     */
    public static void main(String[] args) throws Exception {
        String ip = "127.0.0.1";
        ip = args[0];
        String username = "jiezhou";
        username = args[1];
        String password = "LingQin2014$";
        password = args[2];
        String command = "sh /Users/jiezhou/OpenSource/test1/test.sh";
        command = args[3];
        SSHExecution exe = new SSHExecution(ip, username, password);
        System.out.println(exe.exec(command));
    }
}
