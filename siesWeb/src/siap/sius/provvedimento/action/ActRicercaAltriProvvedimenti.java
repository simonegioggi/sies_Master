package siap.sius.provvedimento.action;

import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

/**
 * <p>
 * Title: ActRicercaProvvedimentii
 * </p>
 * <p>
 * Description: Azione specializzazione per la ricerca dei Provvedimenti legati al fascicolo SIUS.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActRicercaAltriProvvedimenti extends ActionSiap implements ICostantiProvvedimento {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno();

		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		// Nota: ExRicercaProvvedimentiByFascicoloSius decodifica cod_esito come 'ESITO_TENORE
		// Vector lVect =
		// mCtrl.ExRicercaProvvedimentiByFascicoloSius(getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS),COD_EVENTO_PROVVEDIMENTO);
		Vector lVect = mCtrl.ExRicercaAltroEventoByFascicoloSius(
				getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS),
				COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);
		return PG_ELENCOALTRIPROVVEDIMENTI;
	}

}