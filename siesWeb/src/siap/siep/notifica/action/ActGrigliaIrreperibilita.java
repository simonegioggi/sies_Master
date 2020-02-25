package siap.siep.notifica.action;

import siap.sico.evento.controller.IEventoSimeone;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
* <p>Title: ActGrigliaIrreperibilita</p>
* <p>Description: Classe Action per la load </p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActGrigliaIrreperibilita extends ActionSiap implements ICostantiNotifica
{

  public String processRequest() throws Exception
  {
    if (this.isSessionAttributeNullObj("fascicolo"))
     {
       return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
     }
    else
    {  
      FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
  
      // Ricerca un decreto di Irreperibilità (l'ultimo validato inserito per data inserimento)
      EventoNotificaModel lEveNot = new EventoNotificaModel();
      IEventoSimeone lCtrl = SICOLookupRemote.getEventoSimeoneRemote();
      lEveNot = lCtrl.ExRicercaEventoNotificaByIdFascicoloDescrMotivo(lFascMod.getIdFascicoloSiep(), "8BIS");

      setRequestAttribute("eventonotifica", lEveNot);
    }

    return PG_LOAD_GRIGLIA_IRREPERIBILITA;
  }
}



