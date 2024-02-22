package f3b.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.jasper.JasperException;
import org.apache.jasper.runtime.JspRuntimeLibrary;

import f3b.security.model.FunctionModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.util.MultipartContent;

/**
 * <p>
 * Title: Action
 * </p>
 * <p>
 * Description: Azione padre delle classi figlie ActXxxx. Questa classe mette a disposizione alle classi
 * figlie i metodi per estrarre i dati dagli oggetti <code>session</code> e <code>request</code>, inoltre cosa
 * fondamentale ha la responsabilità di caricare dinamicamente la classe azione figlia, metodo direttamente
 * invocato dalla <code>Main.jsp</code>.
 * </p>
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public class Action {
  protected static ServletContext mApplication;

  private HttpSession         mSession;
  private HttpServletRequest  mRequest;
  private MultipartContent    mMultipart;

  /**
   * Costruttore di classe.
   */
  public Action(){
  }
 
  public void init() { 
  }
  
  /**
   * Costruttore con copia
	 * 
   * @param aAct
   */
  public Action(Action aAct){
	  mSession = aAct.mSession;
	  mRequest = aAct.mRequest;
	  mMultipart = aAct.mMultipart;
  }
  
 
  /**
   * Imposta la request e la session.
   * <p>
	 * 
	 * @param aRequest
	 *            Request della connessione web.
	 * @param aSession
	 *            Session della connessione web.
   */
	public void setReqSes(HttpServletRequest aRequest, HttpSession aSession) {
    mSession    = aSession;
    mRequest    = aRequest;
    mMultipart  = (MultipartContent)aRequest.getAttribute( "MultipartContent" );
  }

  /**
   * Imposta il ServletContext application.
   * <p>
	 * 
	 * @param aApplication
	 *            Oggetto ServletContext.
   */
	public static void setServletContext(ServletContext aApplication) {
    mApplication = aApplication;
  }

  /**
	 * Metodo da sovrascrivere nella classe figlia, ove verrà implementato il codice per accedere alla logica
	 * di business. Inoltre ritornerà sempre una stringa contenente il nome della pagina di vista che la
	 * <code>Main.jsp</code> dovrà chiamare.
   * <p>
	 * 
   * @return ritorna la pagina di destinazione.
	 * @throws Exception
	 *             propaga l'errore di eccezione.
   */
	public String processRequest() throws Exception {
    return "";
  }

  /**
   * Ritorna la sessione utente, come oggetto <code>HttpSession</code>.
   * <p>
	 * 
   * @return l'oggetto <code>HttpSession</code>.
   */
	protected HttpSession getSession() {
    return mSession;
  }

  /**
   * Ritorna la request, della connessione con il client.
   * <p>
	 * 
   * @return la request della connessione.
   */
	protected HttpServletRequest getRequest() {
    return mRequest;
  }

  /**
   * Ritorna la request di tipo multipart, della connessione con il client.
   * <p>
	 * 
   * @return la request di tipo multipart.
   */
	protected MultipartContent getRequestMultipart() {
    return mMultipart;
  }

  /**
   * Ritorna il <code>ServletContext</code>.
   * <p>
	 * 
   * @return il <code>ServletContext</code>.
   */
	protected ServletContext getServletContext() {
    return mApplication;
  }

  //
  // METODI DI GESTIONE DELLA REQUEST
  //

  /**
   * Ritorna il <code>ContextPath</code> della <code>Request</code> corrente.
   * <p>
	 * 
   * @return la contextpath corrente.
   */
	protected String getContextPath() {
    return mRequest.getContextPath();
  }

  /**
   * Ritorna dalla <code>Request</code>, l'insieme <code>Map</code> dei parametri.
   * <p>
	 * 
   * @return l'insieme di tutti i parametri prsenti nella request.
   */
	protected Map getRequestParameterMap() {
    return mRequest.getParameterMap();
  }

  /**
	 * Preleva dalla reguest l'oggetto corrispondente alla chiave passata come parametro e ritorna l'oggetto
	 * convertito come oggetto <code>String</code>.
   * <p>
	 * 
	 * @param aParamName
	 *            chiave del nome del parametro.
   * @return il valore corrispondente alla chiave come oggetto <code>String</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected String getRequestStringParameter(String aParamName) throws F3BException {
    String lParamValue = null;
    lParamValue = this.getParameter( aParamName );

    if (lParamValue == null)
      throw new F3BException("Il parametro '" + aParamName + "' non esiste nella FORM");

    return lParamValue.trim();
  }

  /**
   * Ritorna vaolore del parametro in formato <code>BigDecimal</code>.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro da prelevare.
   * @return valore del campo.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected BigDecimal getRequestBigDecimalParameter(String aParamName) throws F3BException {
    String lParamValue = null;
    BigDecimal lNumValue = null;

    lParamValue = this.getParameter( aParamName );

    if (lParamValue == null)
      throw new F3BException("Il parametro '" + aParamName + "' non esiste nella FORM");
    else {
    	// 20181130 [SG]: segnalazione SIES 11.1.2: Errore nella protocollazione SIGE (PILLITTERI)	
        if (!"".equals(lParamValue.trim()) && !"null".equals(lParamValue.trim()) && !"-".equals(lParamValue.trim()))
          lNumValue = new BigDecimal(lParamValue.trim());
    }

    return lNumValue;
  }


  /**
	 * Ritorna l'array di stringe di un parametro presente nella request. Utilizzato per campi a scelta
	 * multipla es.: checkbox e liste a scelta multipla.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro.
   * @return l'array dei valori.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected String[] getRequestStringParameters(String aParamName) throws F3BException {
    String[] lParamValues = null;
    lParamValues = this.getParameterValues(aParamName);

    if (lParamValues == null)
      throw new F3BException("Il parametro multiplo '" + aParamName + "' non esiste nella FORM");

    return lParamValues;
  }

	/*
	 * Vecchia versione che va in errore sulla stringa vuota Ritorna il valore di un parametro contenuto nella
	 * request come <code>int</code>. <p>
	 * 
   * @param aParamName nome del parametro da prelevare.
	 * 
   * @return il valore del campo come <code>int</code>.
	 * 
   * @throws F3BException propaga l'errore di eccezione.
	 * 
	 * protected int getRequestIntParameter(String aParamName) throws F3BException { String lParamValue =
	 * null;
	 * 
	 * lParamValue = this.getParameter(aParamName);
	 * 
	 * if(lParamValue == null) throw new F3BException("Il parametro '" + aParamName +
	 * "' non esiste nella FORM");
	 * 
	 * return Integer.parseInt(lParamValue.trim()); }
	 */

 /**
	 * Restituisce true se il parametro di tipo int nella request esiste ed è diverso da stringa vuota
	 * 
   * @param aParameter
   * @return
   * @throws F3BException
   */
	protected boolean isIntParameter(String aParameter) throws F3BException {
    if(!isRequestParameterNullObj(aParameter) && !getRequestStringParameter(aParameter).equals(""))
      return true;
    else
      return false;
  }

  /**
	 * Ritorna il valore di un parametro contenuto nella request come <code>int</code>.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro da prelevare.
   * @return il valore del campo come <code>int</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected int getRequestIntParameter(String aParamName) throws F3BException {
    String lParamValue  = null;
    int lRitorno = 0;

    lParamValue = this.getParameter(aParamName);

    if(lParamValue == null)
      throw new F3BException("Il parametro '" + aParamName + "' non esiste nella FORM");

    if (this.isIntParameter(aParamName))
       lRitorno =  Integer.parseInt(lParamValue.trim());

    return  lRitorno;

  }

  /**
	 * Ritorna l'oggetto data creato dal parse di tre campi contenuti nella request.
   * <p>
	 * 
	 * @param aYear
	 *            nome del campo anno.
	 * @param aMonth
	 *            nome del campo mese.
	 * @param aDay
	 *            nome del campo giorno,
   * @return l'oggetto data.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected Date getRequestDateParameter(String aYear, String aMonth, String aDay) throws F3BException {
    Date lDate = null;

    String lYear  = this.getRequestStringParameter(aYear);
    String lMonth = this.getRequestStringParameter(aMonth);
    String lDay   = this.getRequestStringParameter(aDay);

    lDate = DateUtils.getDate( lYear, lMonth, lDay );

    return lDate;
  }

  /**
	 * Ritorna l'array di date di un parametro multiplo presente nella request. Utilizzato per campi a scelta
	 * multipla es.: checkbox e liste a scelta multipla.
   * <p>
	 * 
	 * @param aYear
	 *            nome del campo multiplo anno.
	 * @param aMonth
	 *            nome del campo multiplo mese.
	 * @param aDay
	 *            nome del campo multiplo giorno,
   * @return l'array lDates.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected Date[] getRequestDateParameters(String aYear, String aMonth, String aDay) throws F3BException {
    Date[] lDates = null;
    int lung = 0;

    String[] lYears  = this.getRequestStringParameters(aYear);
    String[] lMonths = this.getRequestStringParameters(aMonth);
    String[] lDays   = this.getRequestStringParameters(aDay);

		if ((lYears.length != lMonths.length) || (lYears.length != lDays.length)
				|| (lDays.length != lMonths.length))
      throw new F3BException("Errore nel parametro di data multiplo ");

    lung = lYears.length;
    lDates = new Date[lung];
    for(int i = 0; i < lung; i ++)
      lDates[i] = DateUtils.getDate( lYears[i], lMonths[i], lDays[i] );

    return lDates;
  }

  /**
	 * Ritorna l'oggetto <code>Date</code>, accetta come parametri la chiave del campo contenente la data in
	 * formato Stringa, e il relativo formato ( es.: yyyy/MM/dd; dd-MM-yyyy ).
   * <p>
	 * 
	 * @param aDate
	 *            nome del campo data nella form.
	 * @param aPattern
	 *            formato della data da leggere.
   * @return l'oggetto <code>Date</code> corrispondente.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected Date getRequestDateParameter(String aDate, String aPattern) throws F3BException {
    Date lDate = null;
    String lDateStr = this.getRequestStringParameter( aDate );

    // Verifica se il formato della data è valido.
    if( !DateUtils.isValidDate( lDateStr, aPattern ) )
			throw new F3BException(F3BException.USER_MESSAGE, "Il campo " + aDate
					+ " contiene un formato data non valido !");

    lDate = DateUtils.getDate( lDateStr, aPattern );

    return lDate;
  }

  /**
	 * Ritorna l'oggetto data creato dal parse di tre campi contenuti nella request.
   * <p>
	 * 
	 * @param aYear
	 *            nome del campo anno.
	 * @param aMonth
	 *            nome del campo mese.
	 * @param aDay
	 *            nome del campo giorno,
	 * @param aHours
	 *            nome del cmapo ore.
	 * @param aMinutes
	 *            nome del campo minuti.
   * @return l'oggetto data.
	 * @throws F3BException
	 *             propaga l'errore di ecccezione.
   */
	protected Date getRequestDateTimeParameter(String aYear, String aMonth, String aDay, String aHours,
			String aMinutes) throws F3BException {
    Date lDate = null ;

    String lYear    = this.getRequestStringParameter( aYear );
    String lMonth   = this.getRequestStringParameter( aMonth );
    String lDay     = this.getRequestStringParameter( aDay );
    String lHours   = this.getRequestStringParameter( aHours );
    String lMinutes = this.getRequestStringParameter( aMinutes );

    lDate = DateUtils.getDate( lYear, lMonth, lDay, lHours, lMinutes );
    return lDate;
  }

  /**
	 * Verifica se l'oggetto prelevato dalla request è presente in essa. Questo metodo viene utilizzato per la
	 * gestione dei checkbox presenti nell'html.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro.
   * @return la condizione logica di esistenza valore.
   */
	protected boolean isRequestChecked(String aParamName) {
    String  lParameter  = null;
    boolean lFlag = false;

    lParameter = this.getParameter(aParamName);

    if (lParameter != null)
      lFlag = true;

    return lFlag;
  }

 /**
	 * Verifica se un parametro contenuto nella request è un oggetto null.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro da verificare.
   * @return la condizione logica.
   */
	protected boolean isRequestParameterNullObj(String aParamName) {
    String lParameter  = null;
    boolean lFlag = false;

    lParameter = this.getParameter(aParamName);

    if( lParameter == null )
      lFlag = true;

    return lFlag;
  }

  /**
	 * Verifica se un parametro contenuto nella request è un oggetto null o una stringa vuota
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro da verificare.
   * @return la condizione logica.
   */
	protected boolean isRequestParameterNullEmptyObj(String aParamName) {
    String lParameter  = null;
    boolean lFlag = false;

    lParameter = this.getParameter(aParamName);

    if( lParameter == null || "".equals(lParameter.trim()))
      lFlag = true;

    return lFlag;
  }
  
  /**
   * Verifica se l'oggetto richiesto attraverso la chiave è nullo.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro da verificare.
   * @return lo stato logico della verifica.
   */
	protected boolean isRequestAttributeNullObj(String aParamName) {
    Object lObj = null;
    boolean lReturn = false;

    lObj = mRequest.getAttribute(aParamName);

    if( lObj == null )
      lReturn = true;

    return lReturn;
  }

  /**
   * Ritorna l'oggetto desiderato prelevato dalla request.
   * <p>
	 * 
	 * @param aParamName
	 *            nome parametro.
   * @return l'oggetto prelevato dalla request.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected Object getRequestAttribute(String aParamName) throws F3BException {
    Object lObj  = null;
    lObj = mRequest.getAttribute(aParamName);

    if (lObj == null)
      throw new F3BException("L'attributo '" + aParamName + "' non esiste nella Richiesta");

    return lObj;
  }

  /**
   * Imposta l'oggetto desiderato nella request.
   * <p>
	 * 
	 * @param aAttributeName
	 *            Nome dell'attributo.
	 * @param aValue
	 *            valore da mettere in request.
   */
	protected void setRequestAttribute(String aAttributeName, Object aValue) {
    mRequest.setAttribute(aAttributeName, aValue);
  }

  /**
	 * Effettua l'introspezione dell'oggetto passato come parametro, precisamente una istanza della classe
	 * Model, la quale viene popolata dinamicamente con i relativi dati contenuti nella request.
   * <p>
	 * 
	 * @param aObj
	 *            istanza della classe model.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected void introspect(Object aObj) throws F3BException {
		try {
      if( MultipartContent.isMultipartContent( mRequest ) )
        throw new F3BException( "Funzione incompatibile in contesto MultipartContent !");

      JspRuntimeLibrary.introspect( aObj, this.mRequest );
		} catch (JasperException jex) {
      throw new F3BException( "Introspezione non riuscita!");
    }
  }

  //
  // METODI DI GESTIONE DELLA SESSION
  //

  /**
   * Ritorna l'identificativo di sessione.
   * <p>
	 * 
   * @return l'identificativo di sessione.
   */
	protected String getSessionId() {
    return mSession.getId();
  }

  /**
	 * Ritorna l'oggetto dalla session corrispondente al nome dell'attributo passato come parametro.
   * <p>
	 * 
	 * @param aAttributeName
	 *            nopme dell'attributo.
   * @return l'oggetto corrispondente alla chiave dell'attributo.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected Object getSessionAttribute(String aAttributeName) throws F3BException {
    Object lObj  = null;
    lObj = mSession.getAttribute(aAttributeName);

    if (lObj == null)
      throw new F3BException("L'attributo '" + aAttributeName + "' non esiste nella Sessione");

    return lObj;
  }

  /**
   * Verifica se l'oggetto in sessione richiesto attraverso la chiave, è nullo.
   * <p>
	 * 
	 * @param aAttributeName
	 *            nome del parametro da verificare.
   * @return lo stato logico della verifica.
   */
	protected boolean isSessionAttributeNullObj(String aAttributeName) {
    Object lObj = null;
    boolean lReturn = false;

    lObj = mSession.getAttribute(aAttributeName);

    if( lObj == null )
      lReturn = true;

    return lReturn;
  }

  /**
   * Imposta l'oggetto in sessione con la chiave corrispondente.
   * <p>
	 * 
	 * @param aAttributeName
	 *            nome dell'oggetto.
	 * @param aValue
	 *            oggetto da mettere in sessione.
   */
	protected void setSessionAttribute(String aAttributeName, Object aValue) {
    mSession.setAttribute(aAttributeName, aValue);
  }

  /**
   * Elimina l'oggetto in request con la chiave corrispondente.
   * <p>
	 * 
	 * @param aAttributeName
	 *            nome dell'oggetto.
   */
	protected void removeRequestAttribute(String aAttributeName) {
    mRequest.removeAttribute(aAttributeName);
  }

  /**
   * Elimina l'oggetto in sessione con la chiave corrispondente.
   * <p>
	 * 
	 * @param aAttributeName
	 *            nome dell'oggetto.
   */
	protected void removeSessionAttribute(String aAttributeName) {
    mSession.removeAttribute(aAttributeName);
  }

  /**
   * Invalida la sessione.
   */
	protected void invalidateSession() {
    mSession.invalidate();
  }

  /**
	 * Ritorna il valore del campo desiderato, preleva il valore dalla request o da un oggetto multipart.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del parametro nella request.
   * @return valore del parametro.
   * @throws F3BException
   */
	protected String getParameter(String aParamName) {
    String aValue = null;

		if (mRequest != null) {
      // Verifica se la request è una MultiPartContent
			if (!MultipartContent.isMultipartContent(mRequest)) {
        aValue = mRequest.getParameter( aParamName );
        return aValue;
			} else {
        aValue = mMultipart.getParameter( aParamName );
        return aValue;
      }
    }

    return aValue;
  }

  /**
	 * Ritorna il valore del campo desiderato, preleva il valore dalla request o da un oggetto multipart.
   * <p>
	 * 
	 * @param aParamName
	 *            nome del campo da leggere.
   * @return elenco dei valori in come array di stringhe.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected String[] getParameterValues(String aParamName) throws F3BException {
    String[] aValue;

		if (!MultipartContent.isMultipartContent(mRequest)) {
      aValue = mRequest.getParameterValues( aParamName );
      return aValue;
		} else {
      throw new F3BException("Modalità non supportata in MultipartContent ! ");
    }
  }

  /**
	 * Ritorna il contenuto del file presente nella request come oggetto InputStream.
   * <p>
	 * 
	 * @param aParamName
	 *            nome chiave del parametro nella request.
   * @return contenuto file come classe <code>InputStream</code>
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected InputStream getFile(String aParamName) throws F3BException {
    if( !MultipartContent.isMultipartContent( mRequest ) )
      throw new F3BException( "Funzione incompatibile in contesto non MultipartContent !");

    return mMultipart.getFile( aParamName );
  }

  /**
	 * Ritorna il contenuto del file presente nella request come oggetto ByteArrayInputStream.
   * <p>
	 * 
	 * @param aParamName
	 *            nome chiave del parametro nella request.
   * @return contenuto file come classe <code>ByteArrayInputStream</code>
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected ByteArrayInputStream getFileByteArrayInputStream(String aParamName) throws F3BException {
    if( !MultipartContent.isMultipartContent( mRequest ) )
      throw new F3BException( "Funzione incompatibile in contesto non MultipartContent !");

    return mMultipart.getFileByteArrayInputStream( aParamName );
  }

  /**
	 * Ritorna il contenuto del file presente nella request come oggetto InputStream.
   * <p>
	 * 
	 * @param aParamName
	 *            nome chiave del parametro nella request.
   * @return contenuto file come classe <code>InputStream</code>
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected byte[] getFileBytes(String aParamName) throws F3BException {
    if( !MultipartContent.isMultipartContent( mRequest ) )
      throw new F3BException( "Funzione incompatibile in contesto non MultipartContent !");

    return mMultipart.getBytes( aParamName );
  }

  
  
  /**
   * Carica ed invoca la classe figlia dell'azione.
   * <p>
	 * 
	 * @param aName
	 *            nome classe da caricare.
   * @return Istanza dell'azione catata al padre <code>Action</code>.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
	 * @throws Exception
	 *             propaga l'errore di eccezione.
   */
	public Action get(String aName) throws F3BException, Exception {
    Action lAction  = null;

    if ( aName == null || aName.equals("") )
        throw new F3BException("Il campo Azione non è stato definito nella FORM");
		else {
			try {
        Class lClass = Class.forName( aName );
        lAction = (Action) lClass.newInstance();
			} catch (ClassNotFoundException ex) {
				throw new F3BException(F3BException.USER_MESSAGE, "Funzione o classe non disponibile : "
						+ aName);
			} catch (InstantiationException ex) {
        throw new F3BException("Errore nell'istanziazione della classe : " + aName );
			} catch (IllegalAccessException ex) {
        throw new F3BException("Errore nell'inizializzazione della classe : " + aName );
      }
    }

    return lAction;
  }

  /**
	 * Metodo da sovrascrivere. Inserisce nella request l'elenco delle fuzioni figlie della funzione indicata
	 * e filtrate in base al profilo dell'utente connesso.
   * <p>
	 * 
	 * @param aIdFunction
	 *            id della funzione padre
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected void setFunctionsAvailableToRequest(BigDecimal aIdFunction) throws F3BException {
	}

  /**
	 * Metodo da sovrascrivere. Inserisce nella request l'elenco delle fuzioni figlie dell'azione indicata e
   * filtrate in base al profilo dell'utente connesso.
   * <p>
	 * 
	 * @param aNameAction
	 *            nome dell'azione padre
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	public void setFunctionsAvailableToRequest(String aNameAction) throws F3BException {
	}

  /**
	 * Metodo da sovrascrivere. Torna la funzione associata all'azione richiesta cercandola tra quelle in cui
	 * l'utente connesso è abilitato. In questo modo è posssible verificare se chi sta chiedendo una funzione
	 * è abilitato ad eseguirla.
   * <p>
	 * 
	 * @param aNameAction
	 *            nome dell'azione
   * @return ritorna la FunctionModel.
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	protected FunctionModel getFunctionByNameAction(String aNameAction) throws F3BException {
    return new FunctionModel();
  }

/**
	 * Metodo da sovrascrivere. Inserisce nella tabella di log tutti i dati della request utente
   * <p>
	 * 
	 * @param aNameAction
	 *            nome dell'azione padre
	 * @throws F3BException
	 *             propaga l'errore di eccezione.
   */
	public void WriteActivityLog(String aNameAction) throws F3BException {
}
}