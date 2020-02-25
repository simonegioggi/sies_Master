package siap.regesies.action;

import siap.regesies.regesentenza.model.ProvvedimentoModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
 *
 * <p>Title: ActionRegeSiap</p>
 * <p>Description: Classe che realizza la generalizzazione delle
 *  Action del sistema RegeSies</p>
 */
public class ActionRegeSiap extends ActionSiap
{
  /**
   * Restuituisce il ProvvedimentoRege in sessione
   * @return il provvedimento Rege in sessione
   * @throws F3BException - se il Provvedimento non è in sessione
   */
  protected ProvvedimentoModel getProvvedimentoRegeInSession() throws F3BException
  {
    ProvvedimentoModel lProvv = (ProvvedimentoModel) getSession().getAttribute("provvedimentoRege");
    if (lProvv == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. " +
        "Non è stato selezionato nessun Provvedimento ReGe.");

    return lProvv;
  }

  /**
   * Verifica che il provvedimento Rege sia in sessione
   * @throws F3BException - se il Provvedimento non è in sessione
   */
  protected void isProvvedimentoRegeInSession() throws F3BException
  {
    ProvvedimentoModel lProvv = (ProvvedimentoModel) getSession().getAttribute("provvedimentoRege");
    if (lProvv == null)
      throw new F3BException(F3BException.USER_MESSAGE, "Impossibile eseguire l'operazione richiesta. " +
        "Non è stato selezionato nessun Provvedimento ReGe.");
  }

  /**
   * Restituisce vero se parametro di tipo int nella request esiste ed
   * è diverso da stringa vuota
   * @param aParameter
   * @return
   * @throws F3BException
   */
  protected boolean isIntParameter(String aParameter) throws F3BException
  {
    if (!isRequestParameterNullObj(aParameter) && !getRequestStringParameter(aParameter).equals(""))
      return true;
    else
      return false;
  }

  /**
   * Ritorna il valore di un parametro contenuto nella request come
   * <code>int</code>.
   * <p>
   * @param aParamName nome del parametro da prelevare.
   * @return il valore del campo come <code>int</code>.
   * @throws F3BException propaga l'errore di eccezione.
   */
  protected int getRequestIntParameter(String aParamName) throws F3BException
  {
    String lParamValue = null;
    int lRitorno = 0;

    lParamValue = this.getParameter(aParamName);

    if (lParamValue == null)
      throw new F3BException("Il parametro '" + aParamName + "' non esiste nella FORM");

    if (this.isIntParameter(aParamName))
      lRitorno = Integer.parseInt(lParamValue.trim());

    return lRitorno;

  }

  /**
   * Metodo per  interrogare il grado dell'ufficio dell'operatore collegato.
   * @return true se l'ufficio dell'operatore collegato è di secondo grado
   */
  protected boolean isUfficioSecondoGrado()
  throws F3BException
  {
    boolean lRitorno;

    UtenteModel lUtenteConnesso = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);

    String lTipoUfficio = lUtenteConnesso.getUfficioUtente().getCodTipoUfficio();

    if (lTipoUfficio.equals("CAP") || lTipoUfficio.equals("PGCAP")
      || lTipoUfficio.equals("CAPSM") || lTipoUfficio.equals("CASAP"))
      lRitorno = true;
    else
      lRitorno = false;

    return lRitorno;
  }

}