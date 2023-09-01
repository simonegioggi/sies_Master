package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.rinnovo.action.ICostantiRinnovo;
import siap.siep.rinnovo.controller.IRinnovo;
import siap.siep.rinnovo.model.RinnovoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActLoadDettaglioRichInfoComma5 extends ActionSiap implements ICostantiSanzioneSostitutiva  {
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	
	public String processRequest() throws F3BException {

		String lIdRinnovo = this.getRequestStringParameter(ICostantiRinnovo.CAMPO_ID_RINNOVO);
		
    RinnovoModel lRinMod = new RinnovoModel();
    IRinnovo lCtrl = SIEPLookupRemote.getRinnovoRemote();
    lRinMod = lCtrl.ExRicercaRinnovoByKey(new BigDecimal(lIdRinnovo));
    setRequestAttribute("rinnovo",lRinMod);
        
    
    INotifica lCtrlNotifica = SIEPLookupRemote.getNotificaRemote();
    NotificaModel lNotifica = lCtrlNotifica.ExRicercaNotificaByKey(lRinMod.getNotIdNotifica());
    
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoNotificaModel lEveNotMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lNotifica.getEveIdEvento());
    setRequestAttribute("ordineIngiunzione", lEveNotMod);
   
    if ("D".equals(lRinMod.getCodTipoRinnovo())){
    	// Recupera l'avvocato
	    IAvvocato lCtrlAvv = SIEPLookupRemote.getAvvocatoRemote();
    	AvvocatoSiepModel lAvvFascModel = lCtrlAvv.ExRicercaAvvocatoByKeyAvvocatoFasSiep(lNotifica.getAvvIdAvvocatoFascicoloSiep());
    	setRequestAttribute("avvocato",lAvvFascModel.getAvvocato());
    }

    return PG_DETTAGLIO_RICH_COMMA5;
	}
}
