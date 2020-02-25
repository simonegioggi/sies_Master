package siap.sico.help.config;

//import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.PropertiesMgr;



public class HELPDESKProperties extends PropertiesMgr
{
  private static HELPDESKProperties mHelpDeskProperties = null;
  //private static Vector mAllBDI;

  protected HELPDESKProperties(){}

  /**
   * Ritorna l'istanza come risorsa statica, della classe
   * <code>PropertiesMgr</code>.
   * <p>
   * @return l'istanza di <code>PropertiesMgr</code>.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static HELPDESKProperties getInstance() throws F3BException
  {
    if ( mHelpDeskProperties == null )
    {
      String lPathProp = System.getProperty("path.properties");
      //String lNameFile = lPathProp + System.getProperty("file.separator")  + "helpdesk.properties";
      String lNameFile = lPathProp + System.getProperty("file.separator")  + "idHelp.properties";

      mHelpDeskProperties = new HELPDESKProperties();
      mHelpDeskProperties.setFileProps( lNameFile );
      
      mHelpDeskProperties.init();
    }

    return mHelpDeskProperties;
  }

  /**
   * Ritorna il valore corrispondente alla chiave.
   * <p>
   * @param aName nome chiave del valore desiderato.
   * @return valore corrispondente alla chiave.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static String getProperty( String aName ) throws F3BException
  {
    return getInstance().readProperty( aName );
  }

  /**
   * Ritorna il valore corrispondente alla chiave.
   * <p>
   * @param aName nome chiave del valore desiderato.
   * @param aDefault valore di default nel caso in cui il valore
   * è un <code>null</code>.
   * @return valore corrispondente alla chiave.
   * @throws F3BException propaga l'errore di eccezione.
   */
  public static String getProperty( String aName, String aDefault ) throws F3BException
  {
    return getInstance().readProperty( aName, aDefault );
  }

  /**
   * Ritorna
   * <p>
   * @param aName
   * @return
   * @throws F3BException
   */
  public static int getIntProperty( String aName ) throws F3BException
  {
    return getInstance().readIntProperty( aName );
  }

  /**
   * Ritorna
   * <p>
   * @param aName
   * @param aDefaultValue
   * @return
   * @throws F3BException
   */
  public static int getIntProperty( String aName, int aDefaultValue ) throws F3BException
  {
    return getInstance().readIntProperty( aName, aDefaultValue );
  }


/*
  public static Vector getAllBDI() throws F3BException
  {
    return mAllBDI;
  }
*/
}
