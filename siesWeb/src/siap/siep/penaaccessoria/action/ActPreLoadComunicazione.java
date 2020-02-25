package siap.siep.penaaccessoria.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaaccessoria.controller.IPenaAccessoria;
import siap.siep.penaaccessoria.model.PenaAccessoriaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActPreLoadComunicazione
 * </p>
 * <p>
 * Description: Classe Action per la Preload di Comunicazione per PA
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */

public class ActPreLoadComunicazione extends ActionSiap implements ICostantiPenaAccessoria {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		// Data Fascicolo SIEP
		Date lDataInserimento = ((FascicoloSiepModel) getSessionAttribute("fascicolo")).getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);

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
		Collection lColTipoComunicazionePA = null;
		DecodificheModel lModel = new DecodificheModel();

		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("TIPO_COMUNICAZIONE_PA");
		lColTipoComunicazionePA = lDecodifiche.ExRicercaDecodifiche(lModel);
		Option lOptionTipoComunicazione = new Option(lColTipoComunicazionePA, "0", 50);
		setRequestAttribute("tipoComunicazione", "" + lOptionTipoComunicazione);

		setRequestAttribute("modalita", "I");

		// Lettura Pena Accessoria;
		PenaAccessoriaModel lPenMod = new PenaAccessoriaModel();
		lPenMod.setIdPenaAccessoria(new BigDecimal(idPenaAccessoria.trim()));
		// Si Invoca il controller
		IPenaAccessoria lCtrl = SIEPLookupRemote.getPenaAccessoriaRemote();
		lPenMod = lCtrl.ExRicercaPenaAccessoriaByKey(lPenMod);
		setRequestAttribute("penaaccessoria", lPenMod);

		return PG_PRELOAD_COMUNICAZIONE; // restituisce la jsp di VIEW
	}
}
