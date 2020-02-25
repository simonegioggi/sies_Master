package siap.sius.titoloesecutivo.action;

/**
* <p>Title: ActInserisciTitoloEsecutivo</p>
* <p>Description: Classe Action per l'inserimento del Titolo Esecutivo associato al Fascicolo Sius corrente.</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sius.util.SIUSLookupRemote;

public class ActDeassegnaTitoloEsecutivo extends ActionSiap
		implements ICostantiTitoloEsecutivo, ICostantiFascicoloSius {

	/**
	 * Azione di Deassegnazione del Titolo Esecutivo associato al Fascicolo Sius corrente. Il fascicolo SIEP
	 * scelto prima assegnato è stato posto in sessione.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIUS dalla sessione.
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");

		if (isRequestChecked(CHECK_DEASSEGNA_TITOLO_ESECUTIVO)) {
			// Impostazione a null dei riferimenti del Titolo Esecutivo su FascicoloGPModel.
			// lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep( lFasSiepMod.getIdFascicoloSiep() );
			lFasGPMod.getFascicoloSiusModel().setFasSieIdFascicoloSiep(null);
			lFasGPMod.getFascicoloSiusModel().setChiaveAnnoSIEP(null);
			lFasGPMod.getFascicoloSiusModel().setChiaveProgrSIEP(null);
			lFasGPMod.getFascicoloSiusModel().setChiaveUfficioSIEP(null);

			// Il soggetto non è più quello del fascicolo SIEP
			// E' un nuovo soggetto anche nel caso abbia anagrafica identica a quella del fascicolo siep
			// BigDecimal idSoggettoSius = getIdNuovoSoggettoSius();
			// lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto( lFasSiepMod.getSogIdSoggetto() );
			lFasGPMod.getFascicoloSiusModel().setSogIdSoggetto(getIdNuovoSoggettoSius());

			lFasGPMod.getFascicoloSiusModel().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																									// dell'operatore
																									// che
																									// inserisce
			lFasGPMod.getFascicoloSiusModel().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																											// dell'operatore
																											// che
																											// inserisce
			lFasGPMod.getFascicoloSiusModel().setDataAggiornamento(DateUtils.getSysDate());

			// Deassegnazione del Titolo Esecutivo.
			ITitoloEsecutivo lCtrlTE = SIUSLookupRemote.getTitoloEsecutivoRemote();
			/* FascicoloGPModel lFasGPModel = */lCtrlTE.ExDeassegnaTitoloEsecutivo(lFasGPMod, lFasSiepMod);
		}

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

	public BigDecimal getIdNuovoSoggettoSius() throws F3BException {
		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setCodFiscale(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
				ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(
				getRequestStringParameter(ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(getDatiComuneByDescrOmonimia(
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		}

		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());

		if (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NAZIONALITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA) != null
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA).equals("E")) {
			lSogMod.setCodComuneCasellario("342");
		} else {
			lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		}

		lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(
				getRequestStringParameter(ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME_MADRE));
		lSogMod.setSesso(getRequestStringParameter(ICostantiSoggetto.CAMPO_SESSO));
		lSogMod.setAttoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOTE));
		lSogMod.setFlagPresenzaFascicolo("S"); // Michele 10/06/2009

		lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Se non c'è il flag si fa il controllo
		// if(isRequestParameterNullObj(ICostantiSoggetto.FLAG_OMONIMI))
		// lSogMod.setMessage("omonimi");

		// Chiama il controller
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSogRetMod = new SoggettoModel();
		lSogRetMod = lSogCtrl.ExInserisciSoggetto(lSogMod);

		return lSogRetMod.getIdSoggetto();
	}

}