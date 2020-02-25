package siap.sius.misuraalternativa.action;

/**
* <p>Title: ActCancellaDataInizioMisuraAlternativa</p>
* <p>Description: Classe Action per l'inserimento di Verbale</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.action.ICostantiVerbale;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;
import siap.sius.ActionSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActCancellaDataInizioMisuraAlternativa extends ActionSius implements ICostantiVerbale
{
/**
* Azione di Inserimento del Verbale
* @return Nome della pagina JSP da visualizzare
* al termine dell'elaborazione
* @throws F3BException
*/
public String processRequest() throws Exception
{
      BigDecimal lIdFasSius = null;
      FascicoloGPModel lFasGPMod = null;

      // Fascicolo Sius
      lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
      lIdFasSius = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();

     VerbaleModel lVerMod = new VerbaleModel();
     lVerMod.setIdVerbale(getRequestBigDecimalParameter ("idVerbale"));
     lVerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
     lVerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
     lVerMod.setDataInserimento(DateUtils.getSysDate());

     //chiamata al controller aggiorna e cancella
     IVerbale lCtrl = SIEPLookupRemote.getVerbaleRemote();
     lCtrl.ExCancellaDataInizioMisuraAlternativa(lIdFasSius, lVerMod);

     String retPage = null;
     String nextAct = null;

     if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
       nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
     }
     retPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!",nextAct);
     return retPage;
 }

}
