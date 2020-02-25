package siap.siep.jms.action;

import org.apache.log4j.Logger;

import siap.jms.SIAPSender;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.jms.controller.RicercaJMSController;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActRicercaEstesaFascicolo extends ActionSiap implements ICostantiSiepJMS
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  public String processRequest() throws Exception
  {
    //	BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    FascicoloSiepModel lFasMod = new FascicoloSiepModel();

    lFasMod.setChiaveAnno(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO));
    lFasMod.setChiaveProgr(getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR));

    String lTipoUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_TIPO_UFFICIO);
    String lSedeUff = getRequestStringParameter(ICostantiSiepJMS.CAMPO_SEDE_UFFICIO);

    String lCodiceUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lTipoUff, lSedeUff);
    lFasMod.setChiaveUfficio(lCodiceUfficio);

    UfficioModel lUfficioDestinatario = getUfficioByCodUfficio(lCodiceUfficio);
    UfficioModel lBDIDestinataria = getUfficioByCodUfficio(lUfficioDestinatario.getCodDistretto());

    UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("BDI MIttente = " + lBDIMittente);
    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("BDI Destinatario = " + lBDIDestinataria);

//******** Esegue tutta una serie di operazioni sul DB locale **********************

    //----//GDV temporaneo levato
    // IRicercaJMS lCtrlMess = SIEPLookupRemote.getTrasmissioneJMS();
    //----//GDV temporaneo levato

    RicercaJMSController lCtrlMess = new RicercaJMSController();
    MessaggioModel lMessage = lCtrlMess.ExSpedisciRichiestaRicerca(lFasMod);

    lMessage.setDescrBdiDestinataria(lBDIDestinataria.getDescrComune());
    lMessage.setCodBdiDestinataria(lBDIDestinataria.getCodUfficio());
    lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
    lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
    lMessage.setCodUfficioDestinatario(lCodiceUfficio);
    lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
    lMessage.setCodTipoMessaggio(RICHIESTA_RICERCA);
    lMessage.setCodTipoOperazione(RICERCA_FASCICOLO);
    lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
    lMessage.setDataInvio(DateUtils.getSysDate());
    lMessage.setChiaveAnnoSiep(lFasMod.getChiaveAnno());
    lMessage.setChiaveProgrSiep(lFasMod.getChiaveProgr());

    // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
    siesLogger.debug("MESSAGGIO DA SPEDIRE A " + lBDIDestinataria.getDescrComune() + " : " + lMessage.toString());

    SIAPSender lSender = new SIAPSender();
    lSender.send(lMessage);

    //setRequestAttribute("IDEvento", lEveId.toString());
    setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta di ricerca Procedimento sottomessa al Sistema!");

    return IWebConstants.PG_MESSAGE;

  }
}