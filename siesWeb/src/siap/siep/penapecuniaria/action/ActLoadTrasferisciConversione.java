package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciConversione Pena Pecuniaria</p>
 * <p>Description: Trasferisce Il provvedimento di </p> 
 * <p> 'ATTI di Conversione Pene Pecuniarie'  </p>
 * <p> verso il tribunale di sorveglianza </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciConversione extends ActionSiap implements ICostantiEvento
{

	public String processRequest() throws Exception
	{

	BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);	
	IEvento lCtrl = SICOLookupRemote.getEventoRemote();
	EventoNotificaModel lEveNotMod = new EventoNotificaModel();
	lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lEveId);


    //Insieme degli uffici destinatari
	Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio() , "TDS");
    // 21/06/2007 Trasferimento anche all'Ufficio.
    lOption.setFilter( new String[] {"-", "TDS", "UDS"} );

	setRequestAttribute("uffici", "" + lOption);
	setRequestAttribute("eventonotifica",lEveNotMod);

    // STUB 11/09/2006 Destinatari UEPE.
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
    setRequestAttribute("UEPE", "" + lOption);

    // 28/07/2015 Tipo UDS per la comunicazione
    lOption = new Option(DecodificheManager.getInstance().getTipoUfficioPerCodice());
    lOption.setFilter(new String[]{"-", "UDS", "UDSM"});
    lOption.setSelected(lEveNotMod.getNotifiche()[0].getUfficio().getCodTipoUfficio() );
    setRequestAttribute("tipoUDS","" + lOption);
    String PG_LOAD_TRASFERISCI_CONVERSIONE   = IWebConstants.ROOT_DIR + "files/siap/siep/penapecuniaria/LoadTrasferisciConversione.jsp";
	return PG_LOAD_TRASFERISCI_CONVERSIONE;
	}

}