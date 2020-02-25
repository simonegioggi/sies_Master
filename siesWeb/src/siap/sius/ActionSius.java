package siap.sius;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.model.FascicoloSiusModel;
import siap.sius.tenore.model.TenoreModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

// Per la gestione della paginazione
/* import siap.web.LoadPaginazione;
import siap.web.PaginazioneModel;
import f3b.web.IWebConstants;
*/

/**
 * <p>Title: ActionSius </p>
 * <p>Description: Azione estensione della ActionSiap.
 * Questa classe mette a disposizione nuove funzioni specifiche dell'utente SIUS.
 * </p>
 * <p>Copyright: Bull Italia S.p.A.Copyright (c) 2002</p>
 * <p>Company: Bull Italia S.p.A.</p>
 * @version 1.0
 */
public class ActionSius extends ActionSiap
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  /**
   * Costruttore di classe.
   */
  public ActionSius() {}

  /**
   * Inserisce nella request l'elenco delle fuzioni
   * figlie della funzione indicata e
   * filtrate in base al profilo dell'utente connesso.
   * <p>
   * @return lRet Condizione di modificabilità del fascicolo.
   * @throws F3BException Propagazione errori di eccezione.
   */
  protected boolean IsFascicoloSiusModificabile()
    throws F3BException
  {
    boolean lRet = false;

    if (isSessionAttributeNullObj("fascicoloSiusGP"))
      throw new SIUSException(SIUSException.NULL_OBJECT_ERROR, "Dati FASCICOLO_SIUS non in sessione !");

    FascicoloGPModel lFascicolo = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
    lRet = IsFascicoloSiusModificabile(lFascicolo);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("IsModificabile : " + (lRet ? "true" : "false"));
    return lRet;
  }


  protected boolean IsFascicoloSiusModificabile(FascicoloGPModel aFascicolo)
    throws F3BException
  {
    boolean lRet = false;
    if (  aFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("01") == 0
          || aFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("04") == 0
          || aFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("05") == 0
          || aFascicolo.getFascicoloSiusModel().getCodStatoFascicolo().compareTo("99") == 0
          || getCodUfficioUtenteConnesso().compareTo(aFascicolo.getFascicoloSiusModel().getChiaveUfficio()) !=0)
      lRet = false;
    else
    {
      // Sul Fascicolo con il contenuto ESECUZIONE MISURA ALTERNATIVA non è possibile intervenire
// Eliminiamo il trattamento speciale per ESECUZ. M. A. Luigi 10-6-2005
/*      if (aFascicolo != null && aFascicolo.getGeneraleProcedimentoModel() != null && aFascicolo.getGeneraleProcedimentoModel().getCodOggettoProcedimento().compareTo(ICostantiFascicoloSius.COD_OGGETTO_PROCEDIMENTO_MA) == 0)
        lRet = false;
      else */
        lRet = true;

    }
    return lRet;
  }





  /** STUB: 15/04/2004
  * Esegue lo spipamento degli oggetti e dei relativi dettagli,
  * per inserirli in un array di tenori model.
  * <p>
  * @param aCodOggetti strainga inpipata dei codici oggetti.
  * @param aDescOggetti stringa inpipata delle descrizione oggetti.
  * @param aCodDettaglioOggetti stringa inpitata delle coppie codici oggetti+codici dettaglio oggetti.
  * @return array di tenori model.
  * @throws F3BException propaga errore di eccezione.
  */
  protected TenoreModel[] parseOggettiTenori(String aCodOggetti, String aDescOggetti, String aCodDettaglioOggetti)
  throws F3BException
  {
    StringTokenizer lStCodice = new StringTokenizer( aCodOggetti,"|");
    StringTokenizer lStDescr = new StringTokenizer( aDescOggetti,"\n");
    int lSizeVector = lStCodice.countTokens();
    TenoreModel lTenori[] = new TenoreModel[lSizeVector];
    int lIndex = 0;
    while (lStCodice.hasMoreTokens())
    {
      TenoreModel lTenModel =  new TenoreModel();

      lTenModel.setCodOggettoTenore( lStCodice.nextToken());
      lTenModel.setDescrOggettoTenore( lStDescr.nextToken());

      // Valorizzazione dell'eventuale Dettaglio Oggetto.
      if ( (aCodDettaglioOggetti).indexOf(lTenModel.getCodOggettoTenore()+"0")< 0 )
      {
        lTenModel.setCodDettaglioOggetto("-");
      }
      else
      {
        String lCodDettaglioCorrente = aCodDettaglioOggetti.substring(aCodDettaglioOggetti.indexOf(lTenModel.getCodOggettoTenore()+"0")+4,aCodDettaglioOggetti.indexOf(lTenModel.getCodOggettoTenore()+"0")+8);
        lTenModel.setCodDettaglioOggetto( lCodDettaglioCorrente);
      }

      lTenModel.setCodUfficioInserimento( getCodUfficioUtenteConnesso()); //Codice dell'ufficio dell'operatore che inserisce
      lTenModel.setCodOperatoreInserimento (getCodUtenteConnesso()); //Codice dell'operatore che inserisce
      lTenModel.setDataInserimento( DateUtils.getSysDate() );
      lTenModel.setProgrTenore(new BigDecimal((double)(lIndex+1)));
      lTenModel.setCodEsitoTenore("-");
      //Setto l'Array su GPtenoreModel
      lTenori[lIndex] = lTenModel;


      lIndex++;
   }
   return lTenori;
  }

  /** STUB: 19/04/2004
   * Esegue lo spipamento degli oggetti e dei relativi dettagli,
   * per inserirli in un array di tenori model.
   * <p>
   * @param aCodOggetti strainga inpipata dei codici oggetti.
   * @param aDescOggetti stringa inpitata delle descrizione oggetti.
   * @param aCodDettaglioOggetti stringa inpipata delle coppie codici oggetti+codici dettaglio oggetti.
   * @param aCodEsitoTenore Codice di Esito Tenore da impostare.
   * @param aCodMagistrato Codice del Magistrato selezionato.
   * @return array di tenori model.
   * @throws F3BException propaga errore di eccezione.
   */
  protected TenoreModel[] parseOggettiTenori(String aCodOggetti,
                                           String aDescOggetti,
                                           String aCodDettaglioOggetti,
                                           String aCodEsitoTenore,
                                           String aCodMagistrato )
  throws F3BException
  {
    StringTokenizer lStCodice = new StringTokenizer( aCodOggetti,"|");
          StringTokenizer lStDescr = new StringTokenizer( aDescOggetti,"\n");

                int lSizeVector = lStCodice.countTokens();
                TenoreModel lTenori[] = new TenoreModel[lSizeVector];
                int lIndex = 0;

                while (lStCodice.hasMoreTokens())
    {
      TenoreModel lTenModel =  new TenoreModel();

      lTenModel.setCodOggettoTenore( lStCodice.nextToken());
      lTenModel.setDescrOggettoTenore( lStDescr.nextToken());

      // Valorizzazione dell'eventuale Dettaglio Oggetto.
      if ( (aCodDettaglioOggetti).indexOf(lTenModel.getCodOggettoTenore()+"0")< 0 )
      {
        lTenModel.setCodDettaglioOggetto("-");
      }
      else
      {
        String lCodDettaglioCorrente = aCodDettaglioOggetti.substring(aCodDettaglioOggetti.indexOf(lTenModel.getCodOggettoTenore()+"0")+4,aCodDettaglioOggetti.indexOf(lTenModel.getCodOggettoTenore()+"0")+8);
        lTenModel.setCodDettaglioOggetto( lCodDettaglioCorrente);
      }

      lTenModel.setCodUfficioInserimento( getCodUfficioUtenteConnesso()); //Codice dell'ufficio dell'operatore che inserisce
      lTenModel.setCodOperatoreInserimento( getCodUtenteConnesso() ); //Codice dell'operatore che inserisce
      lTenModel.setDataInserimento( DateUtils.getSysDate() );
      lTenModel.setProgrTenore(new BigDecimal((double)(lIndex+1)));
      lTenModel.setCodEsitoTenore(aCodEsitoTenore);
      lTenModel.setCodMagistrato( aCodMagistrato );
      lTenori[lIndex] = lTenModel;

      lIndex++;
   }
   return lTenori;
  }
  
  /**
   * Questo metodo utilizza il LockController per lockare un'entità su cui operare.
   * Se un'altra funzione richiama lo stesso metodo sulla stessa 
   * entità prima che essa sia stata rilasciata, verrà lanciata una eccezione che segnala 
   * il lock per impedire che due funzioni operino simultaneamente sulla stessa entità dello 
   * sullo stesso procedimento SIUS.. 
   * Il nome della entità da lockare viene passato come argomento mentre l'ID utilizzato è 
   * quello del Fascicolo SIUS in sessione, questo per lockare l'entità a livello dell'intero Procedimento.
   * Se il Fascicolo SIUS non è in sessione viene lanciata un'eccezione.
   * @param aNomeEntita
   * @throws F3BException
   */
  protected void lockApplicativo(String aNomeEntita) throws F3BException
  {

	  // Preleva il fascicolo dalla sessione.
	  if (isSessionAttributeNullObj("fascicoloSiusGP"))
		  throw new SIUSException(SIUSException.USER_MESSAGE,    "Manca Fascicolo in sessione");
	  
	  FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
	
	  if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null || lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() == null)
		  	throw new SIUSException(SIUSException.USER_MESSAGE,    "Errore nei dati del Fascicolo SIUS in sessione");
		  
	  // Lock per evitare accesso contemporaneo alla funzione chiamante che operi sullo stesso fascicolo
	  LockModel lck = LockController.lockIfNotLocked(getServletContext(), aNomeEntita,  lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString(),getCodUtenteConnesso(),getSession().getId());
	  if (lck != null)
		  throw new SIUSException (F3BException.USER_MESSAGE, "La definizione della  "+lck.getEntity()+" per il Procedimento è in gestione ad un altro utente!");
	
	  // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
	  siesLogger.debug("Fissato il Lock su " + aNomeEntita  + " del Fascicolo con id:" + lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString()); 
  }


  /**
   * Metodo che verifica se lo stato del fascicolo SIEP è
   * archiviato/definito. Tale metodo a differenza di quello 
   * presente in ActionSIAP si occupa solo di restituire lo stato booleano.
   * ( false = non archiviato/definito; true = archiviato/definito ) 
   * @return stato booleano per occorrenza.
   * @throws F3BException propaga errore di eccezione
   */
  protected boolean isFascicoloSIEPArchiviatoDefinito() throws F3BException
  {
    boolean lB = false;
    
    if( !isSessionAttributeNullObj("fascicolo") )
    {
      FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSession().getAttribute("fascicolo");
      if (lFascMod.getCodStatoFascicolo().equals("01"))
        lB = true;
    }
    
    return lB;
  }

  /**
   * Il metodo qui presente, ha il compito di verificare se il Fascicolo SIEP caricato
   * in sessione sia archiviato ( Codice Stato 01 ) in caso positivo inserisce in 
   * request il messaggio di conferma. 
   * Questo metodo viene utilizzato esclusivamente nella gestione di emissione decreti 
   * ed ordinanze, infatti il messaggio è personalizzato pe l'emissione dei provvedimenti
   * Inoltre, il controllo non scatta se il metodo viene richiamato ricorsivamente dalla 
   * stessa    
   * <p>
   * @return
   * @throws F3BException propaga errore di eccezione
   */
  protected boolean checkFascicoloSIEPArchiviatoPerEmissioneProvvedimento()
  throws F3BException
  {
    boolean lB = false;
 
    // Esegue il controllo che il fascicolo SIEP sia Archiviato.
    // Nel caso di fascicolo archiviato il sistema presenta un messaggio 
    // di conferma. Il parametro WarningDecreto, proviene dal messaggio 
    // di conferma. 
    if (  isFascicoloSIEPArchiviatoDefinito() && 
          isRequestParameterNullObj("WarningDecreto") )
    {
      setRequestAttribute(IWebConstants.MESSAGE_TEXT, 
                          "Attenzione il procedimento SIEP di riferimento risulta Archiviato. \n" +
                          "Effettuare i necessari accertamenti prima di procedere. \n" +
                          "Si vuole continuare con l'emissione del provvedimento? ");
      
      lB = true; 
    } 
    
    return lB;
  }
  
  /**
   * 
   * @return
   * @throws F3BException
   */
  protected FascicoloSiusModel getFascicoloSiusModelInSessione()
  throws F3BException
  {
    // Preleva il fascicolo dalla sessione.
    if (isSessionAttributeNullObj("fascicoloSiusGP"))
      throw new SIUSException(SIUSException.USER_MESSAGE, "Manca Fascicolo in sessione");
    
    FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
    
    if (lFasGPMod == null || lFasGPMod.getFascicoloSiusModel() == null )
      throw new SIUSException(SIUSException.USER_MESSAGE, "Non è possibile recuperare dalla sessione i dati fascicolo");
    
    return lFasGPMod.getFascicoloSiusModel();
  }
  
  protected String getFiltroMinorenni() throws F3BException {
		String ret = "true";
		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		UfficioModel um = getUfficioUtenteConnesso();
		String tipoUfficio = um.getCodTipoUfficio();
		Set<String> elenco = new HashSet<String>();
		elenco.add("PMM");
		elenco.add("DIBM");
		elenco.add("GIPM");
		elenco.add("GUPM");
		elenco.add("CAPSM");
		elenco.add("TDSM");
		elenco.add("UDSM");
		
		if (!elenco.contains(tipoUfficio)) {
			ret = "false";
		}
		return ret;
 }
  
  protected boolean isUserTDSM () throws F3BException {
	  UfficioModel um = getUfficioUtenteConnesso();
	  String tipoUfficio = um.getCodTipoUfficio();
	  return tipoUfficio.equalsIgnoreCase("TDSM");
  }
  
  
  protected boolean isUserUDSM () throws F3BException {
	  UfficioModel um = getUfficioUtenteConnesso();
	  String tipoUfficio = um.getCodTipoUfficio();
	  return tipoUfficio.equalsIgnoreCase("UDSM");
  }
  

  
}