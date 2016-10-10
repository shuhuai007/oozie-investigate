package com.zhoujie;

import org.apache.hadoop.util.ToolRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

/**
 * Config global variables for oozie workflow.
 */
public class GlobalVariablesExecution extends OozieMainBase {

    public static final long DAY_MILLIS = 1000 * 60 * 60 * 24;

    @Override
    protected void runAction(String[] args) throws Exception {

        Calendar curDate = new GregorianCalendar();
        int year;
        int month;
        int date;
        String propKey;
        String propVal;

        String oozieProp = System.getProperty(OOZIE_ACTION_OUTPUT_PROPERTIES);
        if (oozieProp != null) {
            File propFile = new File(oozieProp);
            Properties props = new Properties();

            for (int i = 0; i < 8; ++i) {
                year = curDate.get(Calendar.YEAR);
                month = curDate.get(Calendar.MONTH) + 1;
                date = curDate.get(Calendar.DATE);
                propKey = i + "daysago";
                propVal = year + "-"
                        + (month < 10 ? "0" + month : month) + "-"
                        + (date < 10 ? "0" + date : date);
                props.setProperty(propKey, propVal);
                curDate.setTimeInMillis(curDate.getTimeInMillis() - DAY_MILLIS);
            }

            props.setProperty("user_agree_num", "10");
            OutputStream os = new FileOutputStream(propFile);
            props.store(os, "");
            os.close();
        } else {
            throw new RuntimeException(OOZIE_ACTION_OUTPUT_PROPERTIES
                    + " System p`roperty not defined");
        }
    }

    /**
     * Main method.
     * @param args the arguments
     * @throws Exception if execute {@link ToolRunner}
     */
    public static void main(String[] args) throws Exception {
        ToolRunner.run(new GlobalVariablesExecution(), args);
    }
}
