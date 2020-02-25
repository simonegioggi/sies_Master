package siap.sius.depositoordinanzapc.action;
import java.io.ByteArrayOutputStream;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.SIUSException;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActStampaModRigReclamo41b extends ActionSiap implements ICostantiDepositoOrdinanzaPc
{
/**
 * <p>Title: ActStampaModRigReclamo41b </p>
 * <p>Description: Classe Azione responsabile della stampa
 *  del modello Ordinanza Affidamento.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
  public String processRequest() throws Exception
  {
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

    EventoNotificaModel lEveMod = new EventoNotificaModel();

    lEveMod.getEvento().setFasSieIdFascicoloSiep(lFasGPMod.getFascicoloSiusModel().getFasSieIdFascicoloSiep());

    UfficioModel lUff = new UfficioModel(this.getUfficioUtenteConnesso());

    lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
    lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());


    // Costruisce l'array NotificaModel per evitare errori in fase di PrelevaDati
    lEveMod.setNotifiche(new NotificaModel[0]);

    // Setta il file RTF relativo al documento selezionato
    lEveMod.setNomeTemplate(TEMPLATE_MOD_ORDINANZA_RIGRECLAMO41B);

    IDepositoOrdinanzaPc lCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoModello(lFasGPMod,lEveMod, super.getUtenteConnesso());		 // setta la risposta nella request

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    return IWebConstants.PG_DOWNLOAD;

  }
}