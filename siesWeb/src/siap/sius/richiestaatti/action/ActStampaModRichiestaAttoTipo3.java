package siap.sius.richiestaatti.action;

import java.io.ByteArrayOutputStream;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
// Import per Utente
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.SIUSException;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.stampa.controller.IStampaSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.IWebConstants;

public class ActStampaModRichiestaAttoTipo3 extends ActionSiap implements ICostantiRichiestaAtti
{
/**
 * <p>Title: ActStampaModAffidamentoTD </p>
 * <p>Description: Classe Azione responsabile della
 *  Stampa Modello ATTI ISTRUTTORI TIPO 2
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

    UfficioModel lUfficio     = getUfficioUtenteConnesso();
    //Si Recupera l'utente dalla sessione.
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    // Crea il ByteArrayOutputStream
    ByteArrayOutputStream lReport = null;

    // Generazione documento di stampa
    IStampaSius lCtrlSta = SIUSLookupRemote.getStampaRemote();
    lReport = lCtrlSta.ExPreStampaModelliAttiIstruttori(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius()  , lUfficio.getCodUfficio() ,lUtenteMod,TEMPLATE_MOD_RICHIESTAATTI_TIPO3);

    //Prepara la pagina di destinazione
    if (lReport != null)
      setRequestAttribute("report", lReport);
    else
      throw new SIUSException(SIUSException.USER_MESSAGE, "Nessun documento è stato generato!");

    return IWebConstants.PG_DOWNLOAD;

  }
}