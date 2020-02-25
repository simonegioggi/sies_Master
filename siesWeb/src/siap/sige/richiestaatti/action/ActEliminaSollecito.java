package siap.sige.richiestaatti.action;

import java.math.BigDecimal;

import siap.sige.documentoallegato.controller.IDocumentoAllegato;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import siap.sius.documentoallegato.action.ICostantiDocumentoAllegato;

public class ActEliminaSollecito extends ActionSige {
	 public String processRequest() throws Exception {
		 BigDecimal idDocumento=super.getRequestBigDecimalParameter(ICostantiDocumentoAllegato.CAMPO_ID_DOCUMENTO_ALLEGATO);
		 
		 IDocumentoAllegato ctrl=SIGELookupRemote.getDocumentoAllegatoController();
		 ctrl.ExEliminaSollecito (idDocumento); 
		 return  ritornoDopoCancellazione("Cancellazione Sollecito Avvenuta Correttamente!", null);
	  }
}
