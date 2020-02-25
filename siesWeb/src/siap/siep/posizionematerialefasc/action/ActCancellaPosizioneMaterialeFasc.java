package siap.siep.posizionematerialefasc.action;

import java.math.BigDecimal;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionematerialefasc.controller.IPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaPosizioneMaterialeFasc</p>
* <p>Description: Classe Action per la Cancellazione della PosizioneMaterialeFasc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaPosizioneMaterialeFasc extends ActionSiap implements ICostantiPosizioneMaterialeFasc
{
    public String processRequest() throws Exception
    {
      BigDecimal lIdFascicolo = ( (FascicoloSiepModel) getSessionAttribute("fascicolo")).getIdFascicoloSiep();

      
     // Lock per evitare inserimento e cancellazione contemporaneo per lo stesso fascicolo
      LockModel lck = LockController.lockIfNotLocked(getServletContext(),"PosizioneMateriale",lIdFascicolo.toString(),getCodUtenteConnesso(),getSession().getId());
      if (lck!=null)
      {
        throw new F3BException (F3BException.USER_MESSAGE, "La definizione della Posizione Materiale per questo fascicolo è in gestione ad un altro utente ["+lck.getCodOperatore()+"]!");
      }

      // valorizzazione del model
      PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel();
      lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      lPosMod.setDataAggiornamento(DateUtils.getSysDate());
      lPosMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lPosMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

      // chiama il controller per la cancellazione
      IPosizioneMaterialeFasc lCtrl = SIEPLookupRemote.getPosizioneMaterialeFascRemote();
      lCtrl.ExCancellaPosizioneMaterialeFasc(lPosMod);

      // Si passa al Dettaglio
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction( "siap.siep.posizionematerialefasc.action.ActLoadDettaglioPosizioneMaterialeFasc");
      lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP,  lPosMod.getFasSieIdFascicoloSiep().toString());
      /* Il Ritorno non viene gestito
      if (!isRequestParameterNullObj(IWebConstants.LINK_RITORNO))
      {
        lPage.setParameter(IWebConstants.LINK_RITORNO, getRequestStringParameter(IWebConstants.LINK_RITORNO).trim());
      }
      else
      if (!isRequestParameterNullObj(IWebConstants.FLAG_RITORNO))
      {
        lPage.setParameter(IWebConstants.FLAG_RITORNO, getRequestStringParameter(IWebConstants.FLAG_RITORNO).trim());
      }
       */
      return lPage.toString();
    }
}