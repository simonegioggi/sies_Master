package siap.sius.depositoordinanzapc.action;

/**
* <p>Title: ActModificaDataDepositoDecreto</p>
* <p>Description: Classe Action per l'inserimento della data di Deposito Decreto</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sius.SIUSException;
import siap.sius.depositodecreto.action.ActModificaDataDepositoDecreto;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActModificaDataDepositoOrdinanza extends ActModificaDataDepositoDecreto implements ICostantiDepositoOrdinanzaPc
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  DepositoOrdinanzaPcModel mOrdinanza = null;
  /**
  * Azione di Inserimento della data di Deposito Ordinanza
  * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    return super.processRequest();
  }

  /**
   * Metodo ridefinito per aggiornare l'Ordinanza.
   * @return
   * @throws Exception
   */
  public BigDecimal  aggiornaProvvedimento() throws Exception
  {
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: inizio");

    if (isSessionAttributeNullObj("lDepositoOrdinanza"))
      throw new SIUSException(SIUSException.USER_MESSAGE,"Dati Ordinanza non in sessione!");

    // Lettura Deposito Ordinanza dalla sessione
    mOrdinanza = (DepositoOrdinanzaPcModel)getSessionAttribute("lDepositoOrdinanza");

    //Effettuo l'inserimento data deposito in DepositoDecretoModel; carico i dati da aggiornare.
    mOrdinanza.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso());
    mOrdinanza.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    mOrdinanza.setDataAggiornamento(DateUtils.getSysDate());
    mOrdinanza.setDataDeposito(getRequestDateParameter(ICostantiDepositoOrdinanzaPc.CAMPO_ANNO_DATA_DEPOSITO,ICostantiDepositoOrdinanzaPc.CAMPO_MESE_DATA_DEPOSITO,ICostantiDepositoOrdinanzaPc.CAMPO_GIORNO_DATA_DEPOSITO));

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".aggiornaProvvedimento: fine");
    return mOrdinanza.getIdEventoGenerato();
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

    // Recupero  il Fascicolo e l'Ordinanza dalla sessione.
    FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute( "fascicoloSiusGP");

    //Il controller effettuerà tutte le operazioni sui dati.
    IDepositoOrdinanzaPc lCtrlDD = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
    DocumentoAllegatoModel lDocAllMod = lCtrlDD.ExModificaDataDepositoOrdinanza(lFasGPMod, mOrdinanza, aEveNot,mCheck);

    //restituisce la jsp di VIEW.
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.depositoordinanzapc.action.ActLoadDettaglioDataDepositoOrdinanza&"+ ICostantiDepositoOrdinanzaPc.CAMPO_ID_DOCUMENTO_ALLEGATO +"="+lDocAllMod.getIdDocumentoAllegato();
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug( super.getClass().getName()+".inserisciDati: fine");

    return lPage;
  }
}
