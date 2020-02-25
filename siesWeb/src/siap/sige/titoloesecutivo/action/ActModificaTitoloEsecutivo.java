package siap.sige.titoloesecutivo.action;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action per la ridefinizione del Titolo Esecutivo associato al Fascicolo Sige corrente.
 * </p>
 * <p>
 * Copyright: Copyright (c)
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActModificaTitoloEsecutivo extends ActionSiap implements ICostantiTitoloEsecutivo,
		ICostantiFascicoloSige {

	/**
	 * Azione di Modifica del Titolo Esecutivo associato al Fascicolo Sige corrente. Il fascicolo SIEP scelto
	 * per l'assegnazione è stato posto in sessione. Al controller viene passato anche il vecchio
	 * fascicoloSIEP.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIGE dalla sessione.
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		// Lettura del fascicolo SIEP - in Precedenza Titolo Esecutivo.
		FascicoloSiepModel lFasSiepOld = new FascicoloSiepModel();
		IFascicoloSiep lCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		lFasSiepOld = lCtrl.ExRicercaFascicoloByKey(lFasSigeEsteso.getFascicoloSiep().getIdFascicoloSiep());

		lFasSigeEsteso.getFascicoloSige().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																								// dell'operatore
																								// che
																								// inserisce
		lFasSigeEsteso.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																										// dell'operatore
																										// che
																										// inserisce
		lFasSigeEsteso.getFascicoloSige().setDataAggiornamento(DateUtils.getSysDate());

		lFasSigeEsteso.getFascicoloSige().setSogIdSoggetto(lFasSiepMod.getSogIdSoggetto());

		// Verifica del Titolo Esecutivo.
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		lFasMod.setChiaveUfficio(this.getCodUfficioByCodTipoUfficioDescrComune(this
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
			throw new SIGEException(SIGEException.USER_MESSAGE,
					"Estremi del Titolo Esecutivo modificati: Effettuare prima la ricerca!");

		// Gestione SuperSoggetto:
		// E' necessario duplicare il soggetto del nuovo titolo esecutivo
		// Chiama il controller
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggDuplModel = new SoggettoModel();
		lSoggDuplModel = lSogCtrl.ExInserisciSoggetto(lFasSiepMod.getSoggetto());
		lFasSigeEsteso.getFascicoloSige().setSogIdSoggetto(lSoggDuplModel.getIdSoggetto());

		// Inserimento Titolo Esecutivo.
		ITitoloEsecutivo lCtrlTE = SIGELookupRemote.getTitoloEsecutivoRemote();
		FascicoloSigeEstesoModel lFasSigeEstesoModel = lCtrlTE.ExAssegnaTitoloEsecutivo(lFasSigeEsteso,
				lFasSiepMod, lFasSiepOld);

		// setRequestAttribute("FascicoloSiusGP", lFasGPMod);
		setRequestAttribute("FascicoloSigeEsteso", lFasSigeEstesoModel);

		// Metto in sessione il fascicolo SIGE per consentire la visualizzazione dei nuovi dati nel dettaglio.
		setSessionAttribute("FascicoloSigeEsteso", lFasSigeEstesoModel);

		// Prepara la pagina di destinazione.
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIGE + "="
				+ lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString();

		return lPage;
	}

}