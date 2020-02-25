package siap.sige.posizionematerialefascsige.action;

import java.math.BigDecimal;

import siap.sico.lock.controller.LockController;
import siap.sico.lock.model.LockModel;
import siap.siep.posizionematerialefasc.action.ICostantiPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.posizionematerialefascsige.controller.IPosizioneMaterialeFascSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;


/**
* <p>Title: ActCancellaPosizioneMaterialeFasc</p>
* <p>Description: Classe Action per la cancellazione Posizione Materiale Fascicolo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Engineering</p>
* @version 1.0
*/

public class ActCancellaPosizioneMaterialeFasc extends ActionSige implements ICostantiPosizioneMaterialeFasc
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloSigeEstesoModel lFascMod = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
      BigDecimal lIdFascicolo = lFascMod.getFascicoloSige().getIdFascicoloSige();

     // Lock per evitare inserimento e cancellazione contemporaneo per lo stesso fascicolo
      LockModel lck = LockController.lockIfNotLocked(getServletContext(),"PosizioneMaterialeFascicoloSige",lIdFascicolo.toString(),getCodUtenteConnesso(),getSession().getId());
      if (lck!=null)
      {
        throw new F3BException (F3BException.USER_MESSAGE, "La definizione della  "+lck.getEntity()+" è in gestione ad un altro utente!");
      }

      // valorizzazione del model
      PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel(PosizioneMaterialeFascModel.POSIZIONE_MATERIALE_FASCICOLO_SIGE);
      lPosMod.setFasSieIdFascicoloSiep(lIdFascicolo);
      lPosMod.setDataAggiornamento(DateUtils.getSysDate());
      lPosMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lPosMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

      // chiama il controller per la cancellazione
      IPosizioneMaterialeFascSige lCtrl = SIGELookupRemote.getPosizioneMaterialeFascSigeRemote();
      lCtrl.ExCancellaPosizioneMaterialeFasc(lPosMod);

      // Si passa al Dettaglio
      RedirectTo lPage = new RedirectTo();
      lPage.setPage(IWebConstants.PG_MAIN);
      lPage.setAction( "siap.sige.posizionematerialefascsige.action.ActLoadDettaglioPosizioneMaterialeFasc");
      lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP,  lPosMod.getFasSieIdFascicoloSiep().toString());

      return lPage.toString();
    }
}