package siap.sige.attiinarchivio.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

public class ActLoadModificaDataInvioAttiInArchivio extends ActionSige implements ICostantiAttiInArchivio, ICostantiEvento {
    public String processRequest() throws Exception {
	    gestioneRitorno();
	    Option lOption = new Option(DecodificheManager.getInstance().getTipoAttiInArchivio());
	    lOption.setAddBlankItem(true);
	    lOption.setValueBlankItem("-");
		
	    
	    FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
		IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
	    EventoModel evento = lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
	    if (evento != null){
	        lOption.setSelected(evento.getCodTipologiaInvioAtti());
	    }
	    setRequestAttribute("tipoAtti", lOption.toString());
	    super.setRequestAttribute("lEve", evento);
	    return PG_LOAD_INSERISCI_DATA_INVIO_ATTI_IN_ARCHIVIO;
    }
}
