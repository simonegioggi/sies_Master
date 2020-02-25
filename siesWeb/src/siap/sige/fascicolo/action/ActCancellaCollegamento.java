package siap.sige.fascicolo.action;

import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActCancellaCollegamento</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:Engineering S.p.A. </p>
 * @version 1.0
 */
public class ActCancellaCollegamento extends ActionSige implements ICostantiFascicoloSige
{
  public String processRequest() throws Exception
  {
      // Si ricava il Fascicolo dalla sessione
      FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

      IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
      try
      {
	      // Il Nuovo Fascicolo Padre Esiste in Archivio. Si procede alla modifica del collegamento.
    	  lFasSigeEsteso.getFascicoloSige().setIdFascicoloSigeOrigine(null);
    	  lFasSigeEsteso.getFascicoloSige().setCodOperatoreAggiornamento (getCodUtenteConnesso()); //Codice dell'operatore che inserisce
    	  lFasSigeEsteso.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); //Codice dell'operatore che inserisce
    	  lFasSigeEsteso.getFascicoloSige().setDataAggiornamento( DateUtils.getSysDate());
    	  lFasSigeEsteso.setFascicoloSige(lCtrl.ExModificaIdFascicoloSigeOrigine(lFasSigeEsteso.getFascicoloSige()) );
      }
      catch (Exception ex)
      {
    	  throw(ex);
      }

      //Si Mette in sessione il fascicolo SIGE col nuovo collegamento.
      setSessionAttribute("FascicoloSigeEsteso", lFasSigeEsteso);

      //restituisce la jsp di VIEW
      return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.fascicolo.action.ActLoadDettaglioProcedimentoCollegato&"+CAMPO_ID_FASCICOLO_SIGE+"="+lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString();
  }
}
