package siap.sius.depositosentenza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActModificaDataDepositoDecreto;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.depositosentenza.model.DepositoSentenzaModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActModificaDataDepositoSentenza</p>
* <p>Description: Classe Action per l'inserimento della data di Deposito Sentenza</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActModificaDataDepositoSentenza extends ActModificaDataDepositoDecreto implements ICostantiDepositoSentenza
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  DepositoSentenzaModel mSentenza = null;
  
  /**
  * Azione di Inserimento della data di Deposito Sentenza
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    return super.processRequest();
  }

  /**
   * Metodo ridefinito per aggiornare la Sentenza.
   * @return
   * @throws Exception
   */
  public BigDecimal aggiornaProvvedimento() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: inizio");

    if (isSessionAttributeNullObj("lDepositoSentenza"))
      throw new SIUSException(SIUSException.USER_MESSAGE,"Dati Sentenza non in sessione!");

    // Lettura Deposito Sentenza dalla sessione
    mSentenza = (DepositoSentenzaModel) getSessionAttribute("lDepositoSentenza");

    // Effettuo l'inserimento data deposito in DepositoSentenzaModel; 
    // carico i dati da aggiornare.
    mSentenza.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso());
    mSentenza.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    mSentenza.setDataAggiornamento(DateUtils.getSysDate());
    mSentenza.setDataDeposito(getRequestDateParameter(ICostantiDepositoSentenza.CAMPO_ANNO_DATA_DEPOSITO, ICostantiDepositoSentenza.CAMPO_MESE_DATA_DEPOSITO, ICostantiDepositoSentenza.CAMPO_GIORNO_DATA_DEPOSITO));

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: fine");
    
    return mSentenza.getIdEventoGenerato();
  }
  
  /**
   * Metodo ridefinito per inserire le Modifiche ai dati.
   * @param aEveNot
   * @return
   * @throws Exception
   */
  public String inserisciDati(EventoNotificaModel aEveNot) throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".inserisciDati: inizio");

    // Recupero  il Fascicolo e la Sentenza dalla sessione.
    FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute( "fascicoloSiusGP");

    //Il controller effettuerà tutte le operazioni sui dati.
    IDepositoSentenza lCtrlDS = SIUSLookupRemote.getDepositoSentenzaRemote();
    DocumentoAllegatoModel lDocAllMod = lCtrlDS.ExModificaDataDepositoSentenza(lFasGPMod, mSentenza, aEveNot, mCheck);

    //restituisce la jsp di VIEW.
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositosentenza.action.ActLoadDettaglioDataDepositoSentenza&"+ ICostantiDepositoSentenza.CAMPO_ID_DOCUMENTO_ALLEGATO +"="+lDocAllMod.getIdDocumentoAllegato();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".inserisciDati: fine");

    return lPage;
  }
}
