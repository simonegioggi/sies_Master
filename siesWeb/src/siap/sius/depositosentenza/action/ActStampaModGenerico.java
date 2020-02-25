package siap.sius.depositosentenza.action;

import java.io.ByteArrayOutputStream;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import siap.sius.SIUSException;
import siap.sius.depositosentenza.controller.IDepositoSentenza;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActStampaModGenerico extends ActionSiap implements ICostantiDepositoSentenza
{
/**
 * <p>Title: ActStampaModGenerico </p>
 * <p>Description: Classe Azione responsabile della stampa
 *  del modello Sentenza Generica
 * </p>
 * <p>Company: Engineering S.p.A.</p>
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
    lEveMod.setNomeTemplate(TEMPLATE_MOD_SENTENZA_MODGENERICO);

    IDepositoSentenza lCtrl = SIUSLookupRemote.getDepositoSentenzaRemote();
    ByteArrayOutputStream lReport = lCtrl.ExStampaDocumentoModello(lFasGPMod,lEveMod, super.getUtenteConnesso()); // setta la risposta nella request

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    return IWebConstants.PG_DOWNLOAD;

  }
}