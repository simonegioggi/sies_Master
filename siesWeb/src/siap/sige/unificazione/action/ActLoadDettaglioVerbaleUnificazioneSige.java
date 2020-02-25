package siap.sige.unificazione.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.SIGEException;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.tenore.controller.ITenoreSige;
import siap.sige.tenore.model.TenoreSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;

/**
 * <p>
 * Title: ActLoadDettaglioVerbaleUnificazioneSige
 * </p>
 * <p>
 * Description: Classe Action per la load di Dettaglio Verbale Unificazione Sige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadDettaglioVerbaleUnificazioneSige extends ActionSiap implements
		ICostantiVerbaleUnificazioneSige {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		this.setLinkRitorno(); // STUB
		// UtenteModel lUtenteMod = new UtenteModel(
		// (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		EventoModel lEveMod = new EventoModel();
		ProvvedimentoSigeEventoModel lProvvEveMod = new ProvvedimentoSigeEventoModel();
		FascicoloSigeEstesoModel lFasUnificante = new FascicoloSigeEstesoModel();
		FascicoloSigeEstesoModel lFasUnificato = new FascicoloSigeEstesoModel();

		// Lettura dell'ID del Provvedimento di Unificazione.
		String lId = "";
		if (!isRequestParameterNullObj(CAMPO_ID_VERBALE_UNIFICAZIONE))
			lId = getRequestStringParameter(CAMPO_ID_VERBALE_UNIFICAZIONE);
		if ((lId.compareTo("") == 0)
				&& (!isRequestParameterNullObj(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE)))
			lId = getRequestStringParameter(ICostantiProvvedimentoSige.CAMPO_ID_PROVVEDIMENTO_SIGE);
		if (lId.compareTo("") == 0)
			throw new SIGEException(SIGEException.USER_MESSAGE, "Verbale di Unificazione inesistente.");

		String flagIns = "";
		if (!isRequestParameterNullObj("FlagIns"))
			flagIns = getRequestStringParameter("FlagIns");

		// IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		// lEveMod = lEveCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		IProvvedimentoSige lProvvCtrl = SIGELookupRemote.getProvvedimentoRemote();
		lProvvEveMod = lProvvCtrl.ExRicercaProvvedimentoById(new BigDecimal(lId));

		IEvento lEveCtrl = SICOLookupRemote.getEventoRemote();
		lEveMod = lEveCtrl.ExRicercaEventoByKey(lProvvEveMod.getProvvedimento().getIdEventoGenerato());

		// Lettura dei fascicoli SIGE Unificante e Unificato.
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		lFasUnificato = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lProvvEveMod.getProvvedimento()
				.getFasIdFascicoloSige());
		lFasUnificante = lCtrl.ExRicercaEstesaFascicoloSigeByKey(lFasUnificato.getFascicoloSige()
				.getFasSigIdFascicoloSige());

		// Lettura dei Tenori
		TenoreSigeModel lTenMod = new TenoreSigeModel();
		lTenMod.setFasIdFascicoloSige(lFasUnificante.getFascicoloSige().getIdFascicoloSige());
		ITenoreSige lCtrlTS = SIGELookupRemote.getTenoreSigeRemote();
		Vector lTenori = lCtrlTS.ExRicercaTenoriAttivi(lTenMod);

		// Si Passa nella request l'Evento di Unificazione.
		setRequestAttribute("eventoUnificazione", lEveMod);

		// Si Passa nella request il Provvedimento di Unificazione.
		setRequestAttribute("provvedimentoUnificazione", lProvvEveMod);

		// Si Passa nella request il Procedimento unificante.
		setRequestAttribute("fascicoloUnificante", lFasUnificante);

		// Si Passa nella request il vettore dei Tenori
		setRequestAttribute("tenoriFascicoloSige", lTenori);

		// Si Passa nella request l'id, anno/numero del fascicolo SIGE Unificato e dataUnificazione.
		setRequestAttribute("idFascicoloUnificato", lFasUnificato.getFascicoloSige().getIdFascicoloSige()
				.toString());
		setRequestAttribute("annoFascicoloUnificato", lFasUnificato.getFascicoloSige().getChiaveAnno()
				.toString());
		setRequestAttribute("progrFascicoloUnificato", lFasUnificato.getFascicoloSige().getChiaveProgr()
				.toString());
		setRequestAttribute("dataUnificazione", DateUtils.getDateToString(lFasUnificato.getFascicoloSige()
				.getDataDefinizione(), "dd/MM/yyyy"));

		// Variabile che indica che provengo dalla pagina di inserimento
		setRequestAttribute("flagIns", flagIns);

		return PG_DETTAGLIOVERBALEUNIFICAZIONESIGE;
	}

}