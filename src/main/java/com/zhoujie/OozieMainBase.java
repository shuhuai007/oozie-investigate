package com.zhoujie;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;

/**
 * Abstract class for oozie client.
 */
public abstract class OozieMainBase extends Configured implements Tool {
    protected static final String OOZIE_ACTION_OUTPUT_PROPERTIES =
        "oozie.action.output.properties";

    /**
     * Implements @{@link Tool#run(String[])}.
     * @param args arguments
     * @return 0 always
     * @throws Exception if resolve the xml
     */
    public final int run(String[] args) throws Exception {
        String oozieConfFile = System.getProperty("oozie.action.conf.xml");
        if (oozieConfFile != null) {
            getConf().addResource(new Path("file:///", oozieConfFile));
            //Configuration.dumpConfiguration(this.getConf(), new PrintWriter(System.out));
        }

        runAction(args);
        return 0;
    }

    /**
     * Gets oozie action id of a workflow.
     * @return action id
     */
    public String getActionId() {
        return getConf().get("oozie.action.id");
    }

    /**
     * Gets oozie job id of workflow.
     * @return job id
     */
    public String getOozieJobId() {
        return getConf().get("oozie.job.id");
    }

    /**
     * Runs action.
     * @param args arguments array
     * @throws Exception
     */
    protected abstract void runAction(String[] args) throws Exception;
}
