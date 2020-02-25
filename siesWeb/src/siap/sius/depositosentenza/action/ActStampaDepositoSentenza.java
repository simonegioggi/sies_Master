package siap.sius.depositosentenza.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActStampaDepositoSentenza extends ActionSiap implements ICostantiDepositoSentenza
{
 /**
  * Azione di Stampa del Deposito Sentenza
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    String lId = getRequestStringParameter( ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);

    DocumentoAllegatoModel lDAMod = new DocumentoAllegatoModel();
    // chiama il controller di Documento Allegato.
    IDocumentoAllegato lCtrlDA = SIUSLookupRemote.getDocumentoAllegatoRemote();
    lDAMod = lCtrlDA.ExRicercaDocumentoAllegatoByKey(new BigDecimal(lId));

    lDAMod.setIdDocumentoAllegato(new BigDecimal(lId));

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lDAMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
    lDAMod.setCodUfficioAggiornamento(lUff.getCodUfficio());
    lDAMod.setDataAggiornamento(DateUtils.getSysDate());
    lDAMod.setFlagDocumentoRegistrato("N");

    lDAMod.setDescrUfficioAggiornamento(lUff.getDescrTipoUfficio());

    IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoAllegato(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius(), lDAMod, lUff.getCodUfficio(), super.getUtenteConnesso());

    setRequestAttribute("documentoAllegato", lDAMod);
    setRequestAttribute("report", lReport);


    return IWebConstants.PG_DOWNLOAD_NEW;

  }

}