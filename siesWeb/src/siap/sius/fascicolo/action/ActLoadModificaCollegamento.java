package siap.sius.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sius.ActionSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaCollegamento</p>
* <p>Description: Classe Action per la load Modifica del Procedimento Collegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadModificaCollegamento extends ActionSius implements ICostantiFascicoloSius
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

      // Fascicolo Collegato (Padre)
      IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();

      FascicoloGPModel lFasPadre = null;
      if(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine()!=null )
        lFasPadre = lCtrl.ExRicercaFascicoloByKey(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSiusOrigine());

      // Si Imposta il Tipo Ufficio.
      Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSius(), lFasPadre.getFascicoloSiusModel().getCodTipoUfficio(),46);
      // MEV63 AGGIUNGO GLUI UFFICI PER MINORENNI
      String[] lFilter = {"UDS","TDS", "UDSM", "TDSM"};
      lOption.setFilter(lFilter);
      setRequestAttribute("tipoUfficioSIUS", "" + lOption );

      setRequestAttribute("fascicoloPadre", lFasPadre);
      setRequestAttribute("modalita", "M");

      // Parametro per individuare la provenienza.
      String aProvenienza = getRequestStringParameter("provenienza");
      setRequestAttribute("provenienza", aProvenienza);

      return PG_LOAD_MODIFICACOLLEGAMENTO;
    }
}
