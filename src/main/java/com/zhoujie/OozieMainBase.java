package com.zhoujie;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;


public abstract class OozieMainBase extends Configured implements Tool {
    
    protected static final String OOZIE_ACTION_OUTPUT_PROPERTIES =
        "oozie.action.output.properties";    

    public final int run(String[] args) throws Exception {
        String oozieConfFile = System.getProperty("oozie.action.conf.xml");
        if(oozieConfFile != null) {
            this.getConf().addResource(new Path("file:///", oozieConfFile));
            //Configuration.dumpConfiguration(this.getConf(), new PrintWriter(System.out));
        }

        runAction(args);
        return 0;
    }
    
    public String getActionId() {
        return this.getConf().get("oozie.action.id");
    }
    
    public String getOozieJobId() {
        return this.getConf().get("oozie.job.id");
    }

    protected abstract void runAction(String[] args) throws Exception;
}
