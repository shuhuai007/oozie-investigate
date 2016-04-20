package com.zhoujie;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;

public class SSHExecution {
  private Connection conn;
  private String ip;
  private String usr;
  private String password;
  private String charset = Charset.defaultCharset().toString();
  private String lineSeparator = System.getProperty("line.separator");

  private static final int TIME_OUT = 1;

  public SSHExecution(String ip, String usr, String password) {
    this.ip = ip;
    this.usr = usr;
    this.password = password;
  }

  /**
   * Login
   *
   * @return True if succeed to login, or False
   * @throws IOException
   */
  private boolean login() throws IOException {
    conn = new Connection(ip);
    conn.connect();
    return conn.authenticateWithPassword(usr, password);
  }

  /**
   * Execute the command remotely
   *
   * @param command
   * @return
   * @throws Exception
   */
  public int exec(String command) throws Exception {
    InputStream stdOut = null;
    InputStream stdErr = null;
    String outStr = "";
    String outErr = "";
    int ret = -1;
    try {
      if (login()) {
        Session session = conn.openSession();
        session.execCommand(command);

        stdOut = new StreamGobbler(session.getStdout());
        outStr = processStream(stdOut, charset);

        stdErr = new StreamGobbler(session.getStderr());
        outErr = processStream(stdErr, charset);

        session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

        System.out.println("outStr=" + outStr);
        System.out.println("outErr=" + outErr);

        ret = session.getExitStatus();
      } else {
        throw new Exception("Failed to login remote " + ip);
      }
    } finally {
      if (conn != null) {
        conn.close();
      }
      IOUtils.closeQuietly(stdOut);
      IOUtils.closeQuietly(stdErr);
    }
    return ret;
  }

  /**
   * @param in
   * @param charset
   * @return
   * @throws IOException
   */
  private String processStream(InputStream in, String charset) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader br = new BufferedReader(new InputStreamReader(in, charset));
    while(true) {
      String line = br.readLine();
      if (line == null) {
        break;
      } else {
        sb.append(line).append(lineSeparator);
      }
    }
    if(sb.length() == 0) {
      return "";
    } else {
      return sb.toString().substring(0, sb.lastIndexOf(lineSeparator));
    }
  }

  public static void main(String args[]) throws Exception {
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
