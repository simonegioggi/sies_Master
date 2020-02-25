package siap.regesies.regesentenza.action;

import siap.regesies.util.RegeDecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaRegeSentenza</p>
 * <p>Description: Classe Action per la load ricerca dei Provvedimenti provenienti
 * da ReGe per estremi</p>
 */

public class ActLoadRicercaRegeSentenzaPerEstremi extends ActionSiap
implements ICostantiRegeSentenza
{
  public String processRequest() throws F3BException
  {
    UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    setRequestAttribute("tipoUfficio", lUtenteConnesso.getUfficioUtente().getCodTipoUfficio());
    setRequestAttribute("descrComune", lUtenteConnesso.getUfficioUtente().getDescrComune());

    Option lOption = new Option(RegeDecodificheManager.getInstance().getUfficiRege());
    setRequestAttribute("autoritaEsterna", "" + lOption );


    return PG_LOAD_RICERCAREGESENTENZA; //restituisce la jsp di VIEW

  }

}