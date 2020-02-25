package siap.siep.notifica.action;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;

/**
* <p>Title: ActGrigliaDecretoSospensione</p>
* <p>Description: Classe Action per la load ricerca di Omesse Notifica</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActGrigliaDecretoSospensione extends ActionSiap 
                                          implements ICostantiNotifica
{
  public String processRequest() throws F3BException
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
    {
      //return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      setRequestAttribute("fascicoloNotInSession", "S");
    }
    else
    {  
      FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
  
      EventoNotificaModel lEveNot = new EventoNotificaModel();
      IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
      lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "LS");
  
      setRequestAttribute("eventonotifica", lEveNot);

      lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "8BIS");

      setRequestAttribute("eventonotificaIrreperibilità", lEveNot);
    }
    
    return PG_LOAD_GRIGLIA_DECRETO;
  }
}