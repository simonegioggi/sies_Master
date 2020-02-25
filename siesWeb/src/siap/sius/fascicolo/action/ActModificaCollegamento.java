package siap.sius.fascicolo.action;

import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 *
 * <p>Title: ActModificaCollegamento</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ActModificaCollegamento extends ActionSius
    implements ICostantiFascicoloSius
{
  public String processRequest() throws Exception
  {
    //Recupero del Fascicolo in sessione.
    FascicoloGPModel lFasGPMod = new FascicoloGPModel();
    lFasGPMod = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");

    // Recupero del Codice Tipo Ufficio.
    String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO);
    String lDescrComuneUfficio = getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE);


    // Ricerca Nuovo Fascicolo Padre.
    FascicoloGPModel lFasGPPadre = new FascicoloGPModel();
    String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio ) ;

    IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
    try
    {
      lFasGPPadre = lCtrl.ExRicercaFascicoloByAnnoProgrCodUfficio(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO), getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR), lCodUfficio);
      if (lFasGPPadre == null)
      {
        // Fascicolo Inesistente.
        throw new F3BException(F3BException.USER_MESSAGE,"Fascicolo Inesistente! ");
      }
      // Il Nuovo Fascicolo Padre Esiste in Archivio. Si procede alla modifica del collegamento.
      lFasGPMod.getFascicoloSiusModel().setIdFascicoloSiusOrigine(lFasGPPadre.getFascicoloSiusModel().getIdFascicoloSius());
      lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento (getCodUtenteConnesso()); //Codice dell'operatore che inserisce
      lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); //Codice dell'operatore che inserisce
      lFasGPMod.getFascicoloSiusModel().setDataAggiornamento( DateUtils.getSysDate());
      lFasGPMod.setFascicoloSiusModel(lCtrl.ExModificaIdFascicoloSiusOrigine(lFasGPMod.getFascicoloSiusModel()) );
    }
    catch (Exception ex)
    {
      throw(ex);
    }

    //Si Mette in sessione il fascicolo SIUS col nuovo collegamento.
    setSessionAttribute("fascicoloSiusGP", lFasGPMod);

    //restituisce la jsp di VIEW
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sius.fascicolo.action.ActLoadDettaglioProcedimentoCollegato&"+CAMPO_ID_FASCICOLO_SIUS+"="+lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();
  }
}
