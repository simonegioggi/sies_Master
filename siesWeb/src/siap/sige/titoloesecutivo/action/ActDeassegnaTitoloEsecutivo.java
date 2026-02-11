package siap.sige.titoloesecutivo.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
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

/**
 * ActDeassegnaTitoloEsecutivo - Classe Action DeassegnaTitoloEsecutivo associato al Fascicolo Sige corrente
 *
 * @version 1.0
 */
public class ActDeassegnaTitoloEsecutivo extends ActionSiap
		implements ICostantiTitoloEsecutivo, ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Deassegnazione del Titolo Esecutivo associato al Fascicolo Sige corrente. Il fascicolo SIEP
	 * scelto prima assegnato è stato posto in sessione.
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): INIZIO!");

		// Recupero dell'utente, del Fascicolo SIEP e del Fascicolo SIGE dalla sessione.
		// UtenteModel lUtenteMod = (UtenteModel)
		// getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO);
		// FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
		FascicoloSigeEstesoModel fsem = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		FascicoloSigeEstesoModel fascicoloSigeEsteso = null;

		if (isRequestChecked(CHECK_DEASSEGNA_TITOLO_ESECUTIVO)) {
			String chiaveAnnoSiep = fsem.getFascicoloSiep().getChiaveAnno().toString();
			String chiaveProgSiep = fsem.getFascicoloSiep().getChiaveProgr().toString();

			// Impostazione a null dei riferimenti del Titolo Esecutivo su FascicoloGPModel.
			fsem.getFascicoloSiep().setIdFascicoloSiep(null);
			fsem.getFascicoloSiep().setChiaveAnno(null);
			fsem.getFascicoloSiep().setChiaveProgr(null);
			fsem.getFascicoloSiep().setChiaveUfficio(null);

			// Il soggetto non è più quello del fascicolo SIEP
			// E' un nuovo soggetto anche nel caso abbia anagrafica identica a quella del fascicolo siep
			fsem.getFascicoloSige().setSogIdSoggetto(getIdNuovoSoggettoSius());
			// Codice dell'operatore che aggiorna
			fsem.getFascicoloSige().setCodOperatoreAggiornamento(getCodUtenteConnesso());
			// Codice dell'ufficio che aggiorna
			fsem.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			fsem.getFascicoloSige().setDataAggiornamento(DateUtils.getSysDate());

			// Deassegnazione del Titolo Esecutivo.
			ITitoloEsecutivo lCtrlTE = SIGELookupRemote.getTitoloEsecutivoRemote();
			// 20251128 : gestito ERRORE nella funzione "De-assegnazione Procedimento SIEP"
			try {
				fascicoloSigeEsteso = lCtrlTE.ExDeassegnaTitoloEsecutivo(fsem, chiaveAnnoSiep,
						chiaveProgSiep);
			} catch (Exception ex) {
				if (ex.getMessage().contains("FAS_SIGE_SEN_UK")) {
					RedirectTo rt = new RedirectTo();
					rt.setPage(IWebConstants.PG_MAIN);
					setRequestAttribute(IWebConstants.MESSAGE_TEXT,
							"Attenzione: esiste una sentenza del fascicolo SIEP collegata al procedimento "
									+ "SIGE come Titolo Esecutivo!" + getTestoErrore());
					rt.setAction("siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&"
							+ ICostantiFascicoloSige.CAMPO_ID_FASCICOLO_SIGE + "="
							+ fsem.getFascicoloSige().getIdFascicoloSige());
					setRequestAttribute(IWebConstants.GOTO_PAGE, "" + rt);
					return IWebConstants.PG_MESSAGE;
				} else
					throw new F3BException(F3BException.USER_MESSAGE, ex.getMessage());
			}
		}

		// Metto in sessione il fascicolo SIGE per consentire la visualizzazione dei nuovi dati nel dettaglio.
		setSessionAttribute("fascicoloSigeEsteso", fascicoloSigeEsteso);

		// Prepara la pagina di destinazione.
		String page = "";
		page = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIGE + "="
				+ fsem.getFascicoloSige().getIdFascicoloSige().toString();

		// pagina di ritorno
		return page;
	}

	public BigDecimal getIdNuovoSoggettoSius() throws F3BException {

		SoggettoModel sm = new SoggettoModel();

		sm.setCodFiscale(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_FISCALE).toUpperCase());
		sm.setCodAfis(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS).toUpperCase());
		sm.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		sm.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		sm.setAnnoNascita(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA));
		sm.setMeseNascita(getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA));
		sm.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
				ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));
		sm.setDataNascitaPresunta(getRequestStringParameter(ICostantiSoggetto.CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel cm;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			cm = new ComuneModel(
					getDatiComuneByCodDescr(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			cm = new ComuneModel(getDatiComuneByDescrOmonimia(
					getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
		}

		sm.setCodComuneNascita(cm.getCodComune());
		sm.setCodProvinciaNascita(cm.getCodProvincia());

		if (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NAZIONALITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA) != null
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA).equals("E")) {
			sm.setCodComuneCasellario("342");
		} else {
			sm.setCodComuneCasellario(cm.getCodSedeGiudiziaria());
		}

		sm.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));
		sm.setDescComuneNascitaEstero(
				getRequestStringParameter(ICostantiSoggetto.CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		sm.setNazionalita(getRequestStringParameter(ICostantiSoggetto.CAMPO_NAZIONALITA));
		sm.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		sm.setCognomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME_MADRE));
		sm.setNomeMadre(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME_MADRE));
		sm.setSesso(getRequestStringParameter(ICostantiSoggetto.CAMPO_SESSO));
		sm.setAttoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_ATTO_NASCITA).toUpperCase());
		sm.setNote(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOTE));
		sm.setFlagPresenzaFascicolo("S"); // Michele 10/06/2009

		sm.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		sm.setDataInserimento(DateUtils.getSysDate());
		sm.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Se non c'è il flag si fa il controllo
		// if(isRequestParameterNullObj(ICostantiSoggetto.FLAG_OMONIMI))
		// sm.setMessage("omonimi");

		// Chiama il controller
		ISoggetto is = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel smRet = new SoggettoModel();
		smRet = is.ExInserisciSoggetto(sm);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest(): FINE!");

		// id del soggetto
		return smRet.getIdSoggetto();
	}

	/**
	 * 20251128 : gestito ERRORE nella funzione "De-assegnazione Procedimento SIEP"
	 *
	 * @author sgioggi
	 * @return String
	 */
	private String getTestoErrore() {

		return "<br>###WORKAROUND###<br>DALLA PAGINA DI DETTAGLIO PROCEDIMENTO SIGE:"
				+ "<br>CLICCARE SU (-) IN TITOLO ESECUTIVO DI COMPETENZA;"
				+ "<br>CLICCARE SU ICONA DI CANCELLAZIONE X;<br>CLICCARE SUL NUMERO "
				+ "(che contiene la sentenza) IN ALTRI TITOLI ESECUTIVI;<br>CLICCARE SU ICONA MODIFICA;"
				+ "<br>SCEGLIERE 'Titolo che definisce la competenza del Procedimento' E CONFERMARE;"
				+ "<br>TORNARE NEL DETTAGLIO FASCICOLO SIGE;"
				+ "<br>CLICCARE SU (-) IN TITOLO ESECUTIVO DI COMPETENZA;"
				+ "<br>CLICCARE SU ICONA DI CANCELLAZIONE X;<br>TORNARE NEL DETTAGLIO FASCICOLO SIGE;"
				+ "<br>SCEGLIERE NEL MENU' ORIZZONTALE LA VOCE DE-ASSEGNAZIONE PROCEDIMENTO SIEP"
				+ "<br>E 'Selezionare la seguente casella di controllo' E CONFERMARE.";
	}

}