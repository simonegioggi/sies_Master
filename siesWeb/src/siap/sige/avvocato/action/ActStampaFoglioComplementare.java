package siap.sige.avvocato.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.IWebConstants;

public class ActStampaFoglioComplementare extends ActionSiap implements ICostantiAvvocato
{
/**
  * Azione di Stampa dell'Avvocato
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws Exception
  */
  public String processRequest() throws Exception
  {
	  FascicoloSigeEstesoModel lFasSigeEsteso = new FascicoloSigeEstesoModel();
	  lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

	  
	String templateName = this.getParameter("IDTemplate");
	  
    BigDecimal lId = lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige();
    String lTipoUfficio = getUfficioUtenteConnesso().getCodUfficio();

    IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    ByteArrayOutputStream lByteArrayOut = lCtrl.ExStampaFoglioComplementare(lId, lTipoUfficio, super.getUtenteConnesso(), templateName );

    setRequestAttribute("report", lByteArrayOut );
    return IWebConstants.PG_DOWNLOAD;
  }
}