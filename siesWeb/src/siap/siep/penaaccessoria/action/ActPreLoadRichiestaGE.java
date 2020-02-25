package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActPreLoadRichiestaGE
 * </p>
 * <p>
 * Description: Classe Action per la Preload di Richieste al GE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActPreLoadRichiestaGE extends ActionSiap implements ICostantiPenaAccessoria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Identificativi di Eventuale Pena accessoria di partenza.
		String idPenaAccessoria = getRequestStringParameter(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA);
		String codTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA);
		String descrTipoPenaAccessoria = getRequestStringParameter(
				ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA);

		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_ID_PENA_ACCESSORIA, "" + idPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_COD_TIPO_PENA_ACCESSORIA,
				"" + codTipoPenaAccessoria);
		setRequestAttribute(ICostantiPenaAccessoria.CAMPO_DESCR_TIPO_PENA_ACCESSORIA,
				"" + descrTipoPenaAccessoria);

		// Configurazione combo Tipi Richieste al GE in base all'eventuale tipo Pena Accessoria.
		Collection lColTipoRichiestaGE = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_RICHIESTA_GE");
		lColTipoRichiestaGE = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTipoRichiestaGE = new Option(lColTipoRichiestaGE, "0", 50);
		// Trattamento delle possibili richieste.
		if (idPenaAccessoria != null && idPenaAccessoria.length() > 1) {
			String[] lStringFilter = { "-", "03", "02", "05", "06", "07", "08" };
			lOptionTipoRichiestaGE.setFilter(lStringFilter);
		} else {
			String[] lStringFilter = { "-", "04", "01" };
			lOptionTipoRichiestaGE.setFilter(lStringFilter);
		}
		setRequestAttribute("tipoRichiestaGE", "" + lOptionTipoRichiestaGE);

		setRequestAttribute("modalita", "I");

		// Lettura Pena Accessoria;
		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
		lPenMod.setIdPenaAccessoria(new BigDecimal(idPenaAccessoria.trim()));
		// Si Invoca il controller
		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		lPenMod = lCtrl.ExRicercaPenaAccessoriaByKey(lPenMod);
		setRequestAttribute("penaaccessoria", lPenMod);

		return PG_PRELOAD_RICHIESTAGE; // restituisce la jsp di VIEW
	}
}
