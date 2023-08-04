package siap.siep.pagoPA.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import f3b.util.F3BException;

public class QuartzProperties {
    private static QuartzProperties mQuartsProperties = null;
    
    protected Properties mProps;
    protected String mFileProps = new String();
    
    protected QuartzProperties() {
        
    }
    public static QuartzProperties getInstance() throws F3BException {
        if (mQuartsProperties == null) {
            String lPathProp = System.getProperty("path.properties");
//            String lNameFile = lPathProp + System.getProperty("file.separator") + "quartzDF.properties";
            String lNameFile = lPathProp + System.getProperty("file.separator") + "quartz.properties";
            mQuartsProperties = new QuartzProperties();
            

            mQuartsProperties.mFileProps = lNameFile;
            mQuartsProperties.init();
        }

        return mQuartsProperties;
    }
    
    protected void init() throws F3BException {
        FileInputStream lFis = null;
        try {
            mProps = new Properties();
            lFis = new FileInputStream(mFileProps.equals("") ? "./quartzDF.properties" : mFileProps);
            this.mProps.load(lFis);
        } catch (IOException ioex) {
            throw new F3BException("Errore nella fase di load del file di properties di quartz");
        } finally {
            try {
                if (lFis != null)
                    lFis.close();
            } catch (IOException ioex) {
                throw new F3BException("Errore nella fase di chiusura del file di properties di quartz");
            }
        }
    }
    
    public Properties getProps() {
        return mProps;
    }
}
