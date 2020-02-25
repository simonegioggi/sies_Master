package siap.sige.attiinarchivio.action;

import siap.sico.evento.model.EventoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

public class ActLoadDettaglioAttiInArchivio extends ActionSige implements ICostantiAttiInArchivio{
	 public String processRequest() throws Exception {
		 FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
		 IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		 EventoModel model=lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
		 
		 
		 if (model == null) {
			 RedirectTo lPage = new RedirectTo();
		     lPage.setPage(IWebConstants.PG_MAIN);
		     lPage.setAction("siap.sige.attiinarchivio.action.ActLoadModificaDataInvioAttiInArchivio");
		     return lPage.toString();
		 }
		 
		 super.setRequestAttribute("lEve", model);
		 return PG_DETTAGLIO_INSERISCI_DATA_INVIO_ATTI_IN_ARCHIVIO;
	  }
}
