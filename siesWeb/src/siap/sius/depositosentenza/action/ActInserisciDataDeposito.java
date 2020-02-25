package siap.sius.depositosentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActInserisciDataDepositoDecreto;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 *
 * <p>Title: ActInserisciDataDeposito</p>
 * <p>Description: La classe viene ottenuta generalizzando la classe ActInserisciDataDepositoDecreto.
 *    Questo perchè l'inserimento dei destinatari è comune sia al Decreto, all'Ordinanza e alla Sentenza.</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
public class ActInserisciDataDeposito extends ActInserisciDataDepositoDecreto
	implements ICostantiDepositoSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  
  DepositoSentenzaModel mDepMod = null;
  
  ScadenzarioSiusModel lScadenzarioSiusModPrincipal = null; // Model Scadenzario
  ScadenzarioSiusModel lScadenzarioSiusModSecond = null;
  
  
  /**
  * Azione di Inserimento della data di Deposito Sentenza
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
	  return (super.processRequest());
  }


/**
 * Aggiornamento dell'Ordinanza
 * @throws Exception
 * Questa funzione rappresenta una riscrittura del metodo della classe padre.
 */
 public BigDecimal aggiornaProvvedimento() throws Exception
 {
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: inizio");
   
   // Lettura Deposito Sentenza dalla sessione
   if (isSessionAttributeNullObj("lDepositoSentenza"))
     throw new SIUSException(SIUSException.USER_MESSAGE,"Dati Sentenza non in sessione!");
   mDepMod = (DepositoSentenzaModel)getSessionAttribute("lDepositoSentenza");
 
   // Se la Sentenza non è già depositata bisogna lockare per evitare che a 2 Sentenza 
   // venga attribuito lo stesso progressivo.
   if(mDepMod.getNumSentenza() == null)
   {
	    // Lock
	    LockModel lck = LockController.lockIfNotLocked(getServletContext(),"DEPOSITO_SENTENZA", getCodUfficioUtenteConnesso(), getCodUtenteConnesso(), getSession().getId());
	    if (lck != null)
	    {
	    	setRequestAttribute(IWebConstants.MESSAGE_TEXT,  "Un altro utente dello stesso ufficio (" + lck.getIdEntity()+ ") sta effettuando un" + lck.getEntity() + " ! <BR>Riprovare subito!" );
	    	isLocked = true;
	    }
   }
    
   //Effettuo l'inserimento data deposito in DepositoSentenzaModel; carico i dati da aggiornare.
   mDepMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
   mDepMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
   mDepMod.setDataAggiornamento(DateUtils.getSysDate());
   mDepMod.setDataDeposito(getRequestDateParameter(ICostantiDepositoSentenza.CAMPO_ANNO_DATA_DEPOSITO, ICostantiDepositoSentenza.CAMPO_MESE_DATA_DEPOSITO, ICostantiDepositoSentenza.CAMPO_GIORNO_DATA_DEPOSITO));
   // mDepMod.setAnnoS3( new BigDecimal(DateUtils.getYearToString(DateUtils.getSysDate())));
   //Il campo Num_S3 viene valorizzato (nel controller) con l'ultimo valore presente + 1

   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: fine");
   return mDepMod.getIdEventoGenerato();
 }

 /**
  * La funzione effettua l'aggionamento dei dati nel DB
  * ed indirizza alla pagina di dettaglio
  * @param aEveNot
  * @return
  * @throws Exception
  * Questa funzione rappresenta una riscrittura del metodo della classe padre.
  */
 public String inserisciDati(EventoNotificaModel aEveNot) throws Exception
 {
   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
   siesLogger.debug( super.getClass().getName()+".inserisciDati: inizio");
   String lPage = null;
   String[] lCheck = null;
   
   if (isLocked)
   {
	   lPage =  IWebConstants.PG_MESSAGE;
   }
/*
   else
   {   
	   // #### Gestione SCADENZARIO Sanzione Sostitutiva
	   // --- Sospensione Esecuzione Sanzione Sostitutiva
	   if(mDepMod.getCodTipoOrdinanza() != null && mDepMod.getCodTipoOrdinanza().compareTo(SOSPENSIONE_ESECUZIONE_SS) == 0)
	   {
		   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		   siesLogger.debug( super.getClass().getName()+".inserisciDati: Inizio Carica Scadenzario - CodiceTipoOrdinanza: "+ mDepMod.getCodTipoOrdinanza());
    	
		   // Parametri da passare alla action di Util
		   // ---- ID delle Decreto e dell'Ordinanza
		   BigDecimal lIdEveDecOrd = mDepMod.getIdEventoGenerato();
		   // ---- Codice Tipo Scadenzario Principale
		   String lCodTipoScadenzarioPrincipal = "80";
		   // ---- Codice Tipo Scadenzario Secondario
		   String lCodTipoScadenzarioSecond = "81";
		   // Richiama la funzione di "util" 
		   InserisciPeriodoAltraSanzioneModificaESS lPASmESS = new InserisciPeriodoAltraSanzioneModificaESS();
		   // passa alla funzione di "util" i dati della Request e della Session
		   lPASmESS.setReqSes(this.getRequest(), this.getSession());

		   // esegue la funzione per popolare i model 
		   lScadenzarioSiusModSecond= lPASmESS.caricaScadenzarioSiusPerAltSanzModESS(mFasGP, lIdEveDecOrd, lCodTipoScadenzarioPrincipal, lCodTipoScadenzarioSecond);
		   // Carica model 
		   lScadenzarioSiusModPrincipal = lPASmESS.getScadenzarioPrincipal();
 		   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 		   siesLogger.debug( super.getClass().getName()+".inserisciDati: Fine Carica Scadenzario - CodiceTipoOrdinanza: "+ mDepMod.getCodTipoOrdinanza());
	   }
	   // #### FINE -- Gestione SCADENZARIO Sanzione Sostitutiva
 
	   // #### Gestione SCADENZARIO Misura Sicurezza
	   // --- Sospensione Esecuzione Misura Sicurezza
	   if(mDepMod.getCodTipoOrdinanza() != null && mDepMod.getCodTipoOrdinanza().compareTo(SOSPENSIONE_ESECUZIONE_MS) == 0)
	   {
		   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		   siesLogger.debug( super.getClass().getName()+".inserisciDati: Inizio Carica Scadenzario - CodiceTipoOrdinanza: "+ mDepMod.getCodTipoOrdinanza());
    	
		   // Parametri da passare alla action di Util
		   // ---- ID delle Decreto e dell'Ordinanza
		   BigDecimal lIdEveDecOrd = mDepMod.getIdEventoGenerato();
		   // ---- Codice Tipo Scadenzario Principale
		   String lCodTipoScadenzarioPrincipal = "83";
		   // ---- Codice Tipo Scadenzario Secondario
		   String lCodTipoScadenzarioSecond = "84";
		   // Richiama la funzione di "util" 
		   InserisciPeriodoAltraMisuraModificaEMS lPAMmEMS = new InserisciPeriodoAltraMisuraModificaEMS();
		   // passa alla funzione di "util" i dati della Request e della Session
		   lPAMmEMS.setReqSes(this.getRequest(), this.getSession());

		   // esegue la funzione per popolare i model 
		   lScadenzarioSiusModSecond= lPAMmEMS.caricaScadenzarioSiusPerAltMisModEMS(mFasGP, lIdEveDecOrd, lCodTipoScadenzarioPrincipal, lCodTipoScadenzarioSecond);
		   // Carica model 
		   lScadenzarioSiusModPrincipal = lPAMmEMS.getScadenzarioPrincipal();
 		   // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
 		   siesLogger.debug( super.getClass().getName()+".inserisciDati: Fine Carica Scadenzario - CodiceTipoOrdinanza: "+ mDepMod.getCodTipoOrdinanza());
	   }
	   // #### FINE -- Gestione SCADENZARIO Misura Sicurezza
*/
	   // Lettura dei Check
	   if (!isRequestParameterNullObj("lCheck")){
		   lCheck = getRequestStringParameters("lCheck");
	   }

	   //Il controller effettuerà tutte le operazioni sui dati.
	   IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
	   //DocumentoAllegatoModel lDocAllMod = lCtrlDS.ExInserisciDataDepositoOrdinanza(mFasGP, mDepMod, aEveNot,  lCheck, lScadenzarioSiusModPrincipal, lScadenzarioSiusModSecond);
	   DocumentoAllegatoModel lDocAllMod = lCtrlDS.ExInserisciDataDepositoSentenza(mFasGP, mDepMod, aEveNot,  lCheck, null, null);

	   //restituisce la jsp di VIEW.
	   lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositosentenza.action.ActLoadDettaglioDataDepositoSentenza&"+ ICostantiDepositoSentenza.CAMPO_ID_DOCUMENTO_ALLEGATO +"="+lDocAllMod.getIdDocumentoAllegato();
   //	}
   
   return lPage;
 }

}