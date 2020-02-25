package siap.sius.decretounificazione.action;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import siap.sius.decretounificazione.controller.IDecretoUnificazione;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.provvedimento.util.RicercaProvvedimentiUtil;
import siap.sius.util.SIUSLookupRemote;

/**
* <p>Title: ActLoadInserisciDecretoUnificazione</p>
* <p>Description: Classe Action per la load inserisci di Decreto Unificazione</p>
* <p>Copyright: Copyright (c) 2003</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciDecretoUnificazione extends ActionSiap implements ICostantiDecretoUnificazione
{

  public String processRequest() throws Exception
  {
    //Recupero l'utente dalla sessione
    UtenteModel lUtenteMod = (UtenteModel)getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    // Verifica della Unificabilità dei procedimenti indicati.

    // Se Unificabili, i 2 procedimenti vengono letti e passati alla request per presentarli a confronto.
    IDecretoUnificazione lCtrl = SIUSLookupRemote.getDecretoUnificazioneRemote();
    FascicoloGPModel lFasDaUnificare = lCtrl.ExVerificaFascicoloDaUnificare(getRequestStringParameter( ICostantiDecretoUnificazione.CAMPO_ANNO_DA_UNIF), getRequestStringParameter( ICostantiDecretoUnificazione.CAMPO_NUMERO_DA_UNIF), lUtenteMod.getUfficioUtente().getCodUfficio() );
    FascicoloGPModel lFasUnificante = lCtrl.ExVerificaFascicoloUnificante(getRequestStringParameter( ICostantiDecretoUnificazione.CAMPO_ANNO_UNIFICANTE), getRequestStringParameter( ICostantiDecretoUnificazione.CAMPO_NUMERO_UNIFICANTE), lUtenteMod.getUfficioUtente().getCodUfficio() );

    //	Warning all'unificazione di un fascicolo con ordinanza di rimessione atti
    String 					lFascSospeso = "NO";
    RicercaProvvedimentiUtil lRicerca = new RicercaProvvedimentiUtil(lFasDaUnificare.getGeneraleProcedimentoModel().getIdGeneraleProcedimento());
    if (lRicerca.verificaEsistenzaSospensione()) 
	   		lFascSospeso = "SI";
    setRequestAttribute("fascSospeso", lFascSospeso);
    //	fine Warning!
    
    // Presentazione della FORM di inserimento Data di Unificazione.
    setRequestAttribute("fasDaUnificare", lFasDaUnificare);
    setRequestAttribute("fasUnificante", lFasUnificante);

    return PG_LOAD_INSERISCIDECRETOUNIFICAZIONE; //restituisce la jsp di VIEW
  }
}