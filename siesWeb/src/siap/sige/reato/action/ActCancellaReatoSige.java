package siap.sige.reato.action;
import java.math.BigDecimal;

import siap.siep.reato.action.ICostantiReato;
import siap.siep.reato.controller.IReato;
import siap.siep.reato.model.ReatoModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.reato.model.ReatoSentenzaSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.web.ActionSige;


public class ActCancellaReatoSige extends ActionSige implements ICostantiReato
{
  public String processRequest() throws Exception
  {
	  
	  // lock
	  lockApplicativoSuReato();

	  IReato lReaCtrl = SIEPLookupRemote.getReatoRemote();
	  
	  // Ricerca del Reato da cancellare
      ReatoModel lReato = lReaCtrl.ExRicercaReatoByKey(getRequestBigDecimalParameter(CAMPO_ID_REATO));
	 
      // Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
	  BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);

      // Viene istanziato il ReatoSigeModel
	  ReatoSentenzaSigeModel lReatoSige =new ReatoSentenzaSigeModel(lReato);
	  lReatoSige.setFasSigeSenId(lIdFasSigeSen);
	  
	  // Cancellazione
	  lReaCtrl.ExCancellaReatoSige(lReatoSige);

	  String lRetPage = ritornoDopoCancellazione("Reato cancellato", null);

	  return lRetPage;
  }
}
