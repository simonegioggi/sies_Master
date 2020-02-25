package siap.sius.unificazione.action;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.unificazione.controller.IUnificazione;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActUnificazioneSoggetti</p>
* <p>Description: Classe Action per la Unificazione Soggetti</p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: Bull S.p.A.</p>
* @version 1.0
*/

public class ActUnificazioneSoggetti extends ActionSiap implements ICostantiUnificazione
{
  public String processRequest() throws Exception
  {
    //Recupero l'utente dalla sessione
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    // Verifica della Unificabilità dei procedimenti indicati.

    // Se Unificabili, i 2 procedimenti vengono letti e passati alla request per presentarli a confronto.
    IUnificazione lCtrl = SIUSLookupRemote.getUnificazioneRemote();
    FascicoloGPModel lFasDaUnificare = lCtrl.ExVerificaFascicoloDaUnificare(getRequestStringParameter( ICostantiUnificazione.CAMPO_ANNO_DA_UNIF), getRequestStringParameter( ICostantiUnificazione.CAMPO_NUMERO_DA_UNIF), lUtenteMod.getUfficioUtente().getCodUfficio() );
    FascicoloGPModel lFasUnificante = lCtrl.ExVerificaFascicoloUnificante(getRequestStringParameter( ICostantiUnificazione.CAMPO_ANNO_UNIFICANTE), getRequestStringParameter( ICostantiUnificazione.CAMPO_NUMERO_UNIFICANTE), lUtenteMod.getUfficioUtente().getCodUfficio() );

    // Presentazione della FORM di inserimento Data di Unificazione.
    setRequestAttribute("fasDaUnificare", lFasDaUnificare);
    setRequestAttribute("fasUnificante", lFasUnificante);

    return PG_LOAD_INSERISCIUNIFICAZIONE; //restituisce la jsp di VIEW
  }
}