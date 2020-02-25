package siap.siep.richiesta.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.model.NotificaModel;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciRichiestaAttiExArt51Bis </p>
 * <p>Description: Trasferisce Il provvedimento </p> 
 * <p> 'ATTI di Richiesta Cessazione/Prosecuzione Mis. Alt (Ex art 51 Bis)'  </p>
 * <p> verso il tribunale/Magistrato di sorveglianza </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * @author C.Ambrosino
 * @version 1.0
 */
public class ActLoadTrasferisciRichiestaAttiExArt51Bis extends ActionSiap implements ICostantiRichiesta
{
	public String processRequest() throws Exception
	{

	BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);	
	
	IEvento lCtrl = SICOLookupRemote.getEventoRemote();
	EventoNotificaModel lEveMod = new EventoNotificaModel();
	lEveMod = lCtrl.ExRicercaEventoNotificaByKey(lEveId);

	setRequestAttribute("eventonotifica",lEveMod);

	//notifiche
	   for(int i=0;i<lEveMod.getNotifiche().length;i++)
	   {
		     NotificaModel lNotMod = new NotificaModel();
		     lNotMod = lEveMod.getNotifiche()[i];
	
		     if(lNotMod != null && lNotMod.getUffCodUfficio() != null  
		     	&& lNotMod.getUfficio().getCodTipoUfficio().equals("TDS") )
		     {
		    	 setRequestAttribute("ufficiotds", lNotMod.getUfficio());
		    	 setRequestAttribute("UfficioDestinatario", lNotMod.getUfficio());
		     }
		     else
		     if(lNotMod != null && lNotMod.getUffCodUfficio() != null && !lNotMod.getCodTipoNotifica().equals("T")  
		     	&& lNotMod.getUfficio().getCodTipoUfficio().equals("UDS"))
		     {
		    	 setRequestAttribute("ufficiouds", lNotMod.getUfficio());
		    	 setRequestAttribute("UfficioDestinatario", lNotMod.getUfficio());
		     }
		     
	   }
	
    // STUB 11/09/2006 Destinatari UEPE.
	   Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
	   setRequestAttribute("UEPE", "" + lOption);

   // String PG_LOAD_TRASFERISCI_CONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadTrasferisciConversione.jsp";
	return PG_LOAD_TRASM_ATTI_51_BIS;
	
	}

}
