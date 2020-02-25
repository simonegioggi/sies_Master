package siap.sige.attiinarchivio.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;


public class ActCancellaDataInvioAttiInArchivio extends ActionSige {
	public String processRequest() throws Exception {
		BigDecimal idEvento=super.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
        IFascicoloSige iCtrl=SIGELookupRemote.getFascicoloSigeRemote();
        iCtrl.ExCancellaDataInvioAttiInArchivio (idEvento);
		String lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", "siap.sige.attiinarchivio.action.ActRicercaFSPAttiInArchivio&noQuery=OK");
		return lRectPage;
	}
}
