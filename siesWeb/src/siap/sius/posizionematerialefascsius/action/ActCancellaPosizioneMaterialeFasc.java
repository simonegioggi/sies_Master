package siap.sius.posizionematerialefascsius.action;

import java.math.BigDecimal;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.sico.web.ActionSiap;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.posizionematerialefascsius.controller.IPosizioneMaterialeFascSius;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActLoadDettaglioPosizioneMaterialeFasc</p>
* <p>Description: Classe Action per la load dettaglio di PosizioneMaterialeFasc</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActCancellaPosizioneMaterialeFasc extends ActionSiap implements ICostantiPosizioneMaterialeFasc
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      BigDecimal lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

     // Lock per evitare inserimento e cancellazione contemporaneo per lo stesso fascicolo
      LockModel lck = LockController.lockIfNotLocked(getServletContext(),"PosizioneMaterialeFascicoloSius",lIdFascicolo.toString(),getCodUtenteConnesso(),getSession().getId());
      if (lck!=null)
      {
        throw new F3BException (F3BException.USER_MESSAGE, "La definizione della  "+lck.getEntity()+" è in gestione ad un altro utente!");
      }

      // valorizzazione del model
      PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIUS);
      lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      lPosMod.setDataAggiornamento(DateUtils.getSysDate());
      lPosMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lPosMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

      // chiama il controller per la cancellazione

      IPosizioneMaterialeFascSius lCtrl = SIUSLookupRemote.getPosizioneMaterialeFascSiusRemote();
      lCtrl.ExCancellaPosizioneMaterialeFasc(lPosMod);

      // Si passa al Dettaglio
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction( "siap.sius.posizionematerialefascsius.action.ActLoadDettaglioPosizioneMaterialeFasc");
      lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP,  lPosMod.getFasSieIdFascicoloSiep().toString());

      return lPage.toString();
    }
}