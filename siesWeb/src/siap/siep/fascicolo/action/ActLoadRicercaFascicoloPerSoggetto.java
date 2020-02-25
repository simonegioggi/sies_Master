package siap.siep.fascicolo.action;

import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.jms.controller.IRicercaSICOJMS;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadRicercaFascicoloPerSoggetto extends ActionSiap implements ICostantiFascicoloSiep {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni());
		setRequestAttribute("nazioni", "" + lOption);

		// STUB 09/06/2005 Aggiunta lista BDI destinatarie della richiesta.
		IRicercaSICOJMS lCrtl = SICOLookupRemote.getRicercaSICOJMSRemote();
		Vector allBDI = new Vector(lCrtl.ExRicercaAllBDI());

		setRequestAttribute("ListaBDI", allBDI);

		return PG_LOAD_RICERCAFASCICOLO_SIEP_PER_SOGGETTO; // restituisce la jsp di VIEW
	}

}