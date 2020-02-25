package siap.sius.provvedimento.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.ActionSius;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.impugnazione.controller.IImpugnazione;
import siap.sius.util.SIUSLookupRemote;

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
public class ActRicercaProvvedimenti extends ActionSius implements ICostantiProvvedimento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		// Gestione del punto di ritorno
		this.setLinkRitorno();

		Vector lVect = null;
		BigDecimal lIdFascicolo = null;

		// Cambia la gestione del ritorno Luigi 30-4-2004
		// if (isRequestParameterNullObj("noQuery"))
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);
		} else {
			FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
			lIdFascicolo = lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius();
		}
		IEvento mCtrl = SICOLookupRemote.getEventoRemote();
		lVect = mCtrl.ExRicercaEventoByFascicoloSius(lIdFascicolo, COD_EVENTO_PROVVEDIMENTO);
		setRequestAttribute("provvedimenti", lVect);

		IImpugnazione oCtrl = SIUSLookupRemote.getImpugnazioneRemote();
		Vector aData = new Vector();
		if (lVect.size() > 0) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				EventoModel lProv = (EventoModel) itx.next();
				aData.add(oCtrl.ExRicercaDataRicorso(lProv.getIdEvento(), lProv.getCodTipoProvvedimento()));
			}
		}
		setRequestAttribute("provvedimentiDataRicorso", aData);

		// Controlla se il fascicolo e' modificabile
		String lModificabile = "NO";
		if (IsFascicoloSiusModificabile() == true)
			lModificabile = "SI";
		else
			lModificabile = "NO";
		setRequestAttribute("isModificabile", lModificabile);

		return PG_ELENCOPROVVEDIMENTI;
	}

}