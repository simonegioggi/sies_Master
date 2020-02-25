package siap.sius.avvocato.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActStampaAvvocato extends ActionSiap implements ICostantiAvvocato
{
/**
  * Azione di Stampa dell'Avvocato
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    BigDecimal lId = getRequestBigDecimalParameter( ICostantiAvvocato.CAMPO_ID_AVVOCATO);

    UfficioModel lUfficio     = getUfficioUtenteConnesso();

    ByteArrayOutputStream lByteArrayOut = null;

    // Generazione documento di stampa
    IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
    lByteArrayOut = lCtrlSta.ExPreStampaAvvocato(lId, lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius() ,lUfficio.getCodUfficio() ,"SIUS_ST_002", super.getUtenteConnesso());

    setRequestAttribute("report", lByteArrayOut );

    return IWebConstants.PG_DOWNLOAD;

  }

}