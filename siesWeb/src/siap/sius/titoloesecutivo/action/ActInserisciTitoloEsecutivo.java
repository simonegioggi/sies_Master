package siap.sius.titoloesecutivo.action;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.SIUSException;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento del Titolo Esecutivo associato al Fascicolo Sius corrente.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciTitoloEsecutivo extends ActionSiap implements ICostantiTitoloEsecutivo,
		ICostantiFascicoloSius {

	/**
	 * Azione di Inserimento del Titolo Esecutivo associato al Fascicolo Sius corrente. Il fascicolo SIEP
	 * scelto per l'assegnazione è stato posto in sessione.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIUS dalla sessione.
//		UtenteModel lUtenteMod = (UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		// Impostazione dei riferimenti del Titolo Esecutivo su FascicoloGPModel.
		lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(lFasSiepMod.getIdFascicoloSiep());
		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lFasSiepMod.getSogIdSoggetto());

		lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());

		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lFasSiepMod.getSogIdSoggetto());

		// Verifica del Titolo Esecutivo.
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveUfficio(getCodUfficioByCodTipoUfficioDescrComune(this
				.getRequestStringParameter(ICostantiTitoloEsecutivo.CAMPO_COD_TIPO_UFF_FASCICOLO_SIEP), this
				.getRequestStringParameter(ICostantiTitoloEsecutivo.CAMPO_SEDE_UFF_FASCICOLO_SIEP)
				.toUpperCase()));

		if (!isRequestParameterNullObj(CAMPO_ANNO_FASCICOLO_SIEP))
			lFasMod.setChiaveAnno(getRequestBigDecimalParameter(CAMPO_ANNO_FASCICOLO_SIEP));

		if (!isRequestParameterNullObj(CAMPO_PROGR_FASCICOLO_SIEP))
			lFasMod.setChiaveProgr(getRequestBigDecimalParameter(CAMPO_PROGR_FASCICOLO_SIEP));

		// Confronto della chiave del fascicolo SIEP.
		if ((lFasMod.getChiaveAnno().compareTo(lFasSiepMod.getChiaveAnno()) != 0)
				|| (lFasMod.getChiaveProgr().compareTo(lFasSiepMod.getChiaveProgr()) != 0)
				|| (lFasMod.getChiaveUfficio().compareTo(lFasSiepMod.getChiaveUfficio()) != 0))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Estremi del Titolo Esecutivo modificati: Effettuare prima la ricerca!");

		// Gestione SuperSoggetto:
		// E' necessario duplicare il soggetto del titolo esecutivo
		// Chiama il controller
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggDuplModel = new SoggettoModel();
		lSoggDuplModel = lSogCtrl.ExInserisciSoggetto(lFasSiepMod.getSoggetto());
		lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(lSoggDuplModel.getIdSoggetto());

		// Inserimento Titolo Esecutivo.
		ITitoloEsecutivo lCtrlTE = SIUSLookupRemote.getTitoloEsecutivoRemote();
		/*FascicoloGPModel lFasGPModel = */lCtrlTE.ExAssegnaTitoloEsecutivo(lFasGPMod, lFasSiepMod);

		setRequestAttribute("FascicoloSiusGP", lFasGPMod);

		// Metto in sessione il fascicolo SIUS per consentire la visualizzazione dei nuovi dati nel dettaglio.
		setSessionAttribute("fascicoloSiusGP", lFasGPMod);

		// Prepara la pagina di destinazione.
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIUS + "="
				+ lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius().toString();

		return lPage;
	}

}