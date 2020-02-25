package f3b.web.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
//import java.util.Date;
import java.util.Hashtable;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.util.F3BProperties;
import f3b.util.Utils;

/**
 * Classe responsabile del parsing dei file passati in essa, per verifica 
 * formati ed estensioni.
 * <p>
 * @author user
 */
public class TikaParser 
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  private InputStream mInputStream = null;
  private String mExtension = null;
  private ServletContext mServletContext = null;
  private Metadata mMetadata = null;
  
  private static String[] mExtensions = null; 
  private static Hashtable<String, String> mExtMimeTypesMap = null; 
      
  /**
   * Costruttore di classe con parametri.
   * <p>
   * @param aInput Oggetto InputStream flusso di dati.  
   * @param aSCtx  Oggetto ServletContext.
   * @param aExt   Oggetto String estensione del file. 
   */
  public TikaParser(InputStream aInput, ServletContext aSCtx, String aExt ) 
  throws F3BException {
    // Inizializza gli attributi di classe delle estensioni e relativa mappatura 
    // con mime-type.
    init(aSCtx);
    mServletContext = aSCtx;
    mInputStream = aInput;
    mExtension = ( aExt != null ? aExt.toLowerCase() : null );
  } 
  
  /**
   * Costruttore di classe con parametri.
   * <p>
   * @param aBytes Array di byte, flusso di dati. 
   * @param aSCtx  Oggetto ServletContext.  
   * @param aExt   Oggetto String estensione delfile. 
   */
  public TikaParser(byte[] aBytes, ServletContext aSCtx, String aExt ) 
  throws F3BException {
    // Inizializza gli attributi di classe delle estensioni e relativa mappatura 
    // con mime-type.
    init(aSCtx);
    mServletContext = aSCtx;
    mInputStream = (InputStream)new ByteArrayInputStream(aBytes);
    mExtension = ( aExt != null ? aExt.toLowerCase() : null );
  } 
  
  /**
   * Metodo di autodetect.
   * <p>
   * Il metodo si occupa di parserizzare il flusso di dati, al fine di
   * identificarne il mime-type associato. 
   */
  public void autoDetect() throws Exception {       
    try 
    { 
      // Verifica se l'estensione del file è valida. 
      if ( mExtension != null && !isValidExt(mExtension) )
        throw new F3BException( F3BException.USER_MESSAGE , 
            "Estensione/formato del file non gestito dal sistema!" );
      
      // Parse del file.
      ContentHandler lTextHandler = new BodyContentHandler();
      mMetadata = new Metadata();
      AutoDetectParser lAutoParser = new AutoDetectParser();
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "-> Start Parser! " );
      
      lAutoParser.parse(mInputStream, lTextHandler, mMetadata);
      
      // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
      siesLogger.debug( "-> End Parser! " );
      
      // Se l'estensione è null non esegue controllo di formalità, il tipico
      // caso nella fase di download.
      if( mExtension != null )
        check( mMetadata ); 
    }
    catch( F3BException fex ){
      throw fex;
    }
    catch( IOException ioex ){
      throw ioex;
    }        
    catch( Exception ex ){
      throw new Exception( ex );
    }     
  }
  
  /** 
   * Il metodo ha la responsabilità di verificare se l'estensione del
   * file è conforme la contenuto stesso del flusso di dati.
   * <p>
   * @param aMeta Oggetto Metadata 
   * @throws Exception
   */
  private void check( Metadata aMeta ) throws Exception {
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "################# EXT : " + 
        mExtension.toUpperCase() + " ############# " );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "################# getMimeType : " + 
        mServletContext.getMimeType("." + mExtension) + " ############# " );

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "################# Metadata : CONTENT_TYPE : " + 
        aMeta.get(Metadata.CONTENT_TYPE) + " ############# " );    

    if( isValidExt(mExtension)) {
      if( aMeta.get(Metadata.CONTENT_TYPE).equalsIgnoreCase( mServletContext.getMimeType("." + mExtension) ) ) 
        return; 
      else
        throw new F3BException( F3BException.USER_MESSAGE , "Formato incongruente!" );      
    }
  }  
    
  /**
   * Metodo di verifica della validità dell'estensione del file.
   * <p>
   * @param aExt
   * @return
   */
  private boolean isValidExt( String aExt ) {
    boolean lflag = false;
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "################# EXT : " + 
        mExtension.toUpperCase() + " ############# " );
    
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( "################# getMimeType : " + 
        mServletContext.getMimeType("." + mExtension) + " ############# " );

    Arrays.sort(mExtensions);    
    if( Arrays.binarySearch(mExtensions, mExtension) > -1 && 
        mServletContext.getMimeType("." + mExtension) != null )
      lflag = true;

    return lflag;
  }
    
  /**
   * Il metodo ritorna oggetto Metadata.
   * <p>
   * @return oggetto Metadata.
   */
  public Metadata getMetadata() {
    return mMetadata;
  }
  
  /**
   * Metodo che ritorna l'estensione del file.
   * @param aContentType Oggetto String.
   * <p>
   * @return
   */
  public String getExtension( String aContentType ) {
    return (String)mExtMimeTypesMap.get(aContentType);
  }

  /**
   *  Metodo di inizializzazione degli attributi di classe.
   */
  private static synchronized void init(ServletContext aServCtx) 
  throws F3BException {
    try {      
      // Popola l'attributo di classe come array delle estensioni gestite dal sistema,
      // i valori sono prelevati dal file di properties f3b.properties, riferiti alla 
      // chiave TikaParser.extensions.
      if( mExtensions == null ) {
          mExtensions = 
            F3BProperties.getProperty("TikaParser.extensions").split(",");          
          // Ricontrolla se l'attributo di classe è popolato correttamente.
          if ( mExtensions == null ) 
            throw new Exception();
          else 
            // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
            siesLogger.debug( "#### mExtension : " + 
            		Utils.arrayToString( mExtensions,";" ) + " ############# " );
      }     
      // Popola l'attributo di classe come oggetto Hashtable, della mappatura 
      // MimeType = Estensione. 
      if( mExtMimeTypesMap == null ) {
        mExtMimeTypesMap = new Hashtable<String, String>();      
        for( int x=0; x<mExtensions.length; x++)
          mExtMimeTypesMap.put(aServCtx.getMimeType("." + mExtensions[x]), mExtensions[x]);        
        // Ricontrolla se l'attributo di classe è popolato correttamente.
        if ( mExtMimeTypesMap == null )
          throw new Exception();
        else 
          // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
          siesLogger.debug( "#### ExtMimeTypeMap : " + mExtMimeTypesMap + " ############# " );
      }            
    } catch (Exception e) {
        throw new F3BException( F3BException.USER_MESSAGE , 
          "Errore durante la fase di inizializzazione dei parametri del Tika Parser! " + "<BR>" +
          "Verificare la corretta impostazione dei valori nel file f3b.properties " + 
          "e nel file web.xml, sia del servlet container che della web application." );
    }
   
  }   
}