package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciCollegamento</p>
* <p>Description: Classe Action per la load Inserisci del Procedimento Collegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciCollegamento extends ActionSius implements ICostantiFascicoloSius
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

      // Si Imposta il Tipo Ufficio.
      Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius(), lFasGPMod.getFascicoloSiusModel().getCodTipoUfficio(),46);
      // MEV63 AGGIUNGO GLUI UFFICI PER MINORENNI
      String[] lFilter = {"UDS","TDS", "UDSM", "TDSM"};
      lOption.setFilter(lFilter);
      setRequestAttribute("tipoUfficioSIUS", "" + lOption );
      setRequestAttribute("modalita", "I");
      // Impostazione della provenienza.
      setRequestAttribute("provenienza", "I");

      return PG_LOAD_MODIFICACOLLEGAMENTO;
    }
}
