package siap.sige.titoloesecutivo.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.action.ICostantiFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.titoloesecutivo.controller.ITitoloEsecutivo;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActDeassegnaTitoloEsecutivo
 * </p>
 * <p>
 * Description: Classe Action DeassegnaTitoloEsecutivo associato al Fascicolo Sige corrente.
 * </p>
 * 
 * @version 1.0
 */
public class ActDeassegnaTitoloEsecutivo extends ActionSiap implements ICostantiTitoloEsecutivo,
		ICostantiFascicoloSige {

	/**
	 * Azione di Deassegnazione del Titolo Esecutivo associato al Fascicolo Sige corrente. Il fascicolo SIEP
	 * scelto prima assegnato è stato posto in sessione.
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIGE dalla sessione.
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
		FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		FascicoloSigeEstesoModel fascicoloSigeEsteso = null;

		if (isRequestChecked(CHECK_DEASSEGNA_TITOLO_ESECUTIVO)) {
			String chiaveAnnoSiep = lFasSigeEsteso.getFascicoloSiep().getChiaveAnno().toString();
			String chiaveProgSiep = lFasSigeEsteso.getFascicoloSiep().getChiaveProgr().toString();

			// Impostazione a null dei riferimenti del Titolo Esecutivo su FascicoloGPModel.
			lFasSigeEsteso.getFascicoloSiep().setIdFascicoloSiep(null);
			lFasSigeEsteso.getFascicoloSiep().setChiaveAnno(null);
			lFasSigeEsteso.getFascicoloSiep().setChiaveProgr(null);
			lFasSigeEsteso.getFascicoloSiep().setChiaveUfficio(null);

			// Il soggetto non è più quello del fascicolo SIEP
			// E' un nuovo soggetto anche nel caso abbia anagrafica identica a quella del fascicolo siep
			lFasSigeEsteso.getFascicoloSige().setSogIdSoggetto(getIdNuovoSoggettoSius());

			lFasSigeEsteso.getFascicoloSige().setCodOperatoreAggiornamento(getCodUtenteConnesso()); // Codice
																									// dell'operatore
																									// che
																									// inserisce
			lFasSigeEsteso.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); // Codice
																											// dell'operatore
																											// che
																											// inserisce
			lFasSigeEsteso.getFascicoloSige().setDataAggiornamento(DateUtils.getSysDate());

			// Deassegnazione del Titolo Esecutivo.
			ITitoloEsecutivo lCtrlTE = SIGELookupRemote.getTitoloEsecutivoRemote();
			fascicoloSigeEsteso = lCtrlTE.ExDeassegnaTitoloEsecutivo(lFasSigeEsteso, chiaveAnnoSiep,
					chiaveProgSiep);
		}

		// Metto in sessione il fascicolo SIGE per consentire la visualizzazione dei nuovi dati nel dettaglio.
		setSessionAttribute("fascicoloSigeEsteso", fascicoloSigeEsteso);

		// Prepara la pagina di destinazione.
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIGE + "="
				+ lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString();

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
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescr(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					getDatiComuneByDescrOmonimia(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
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
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO));
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