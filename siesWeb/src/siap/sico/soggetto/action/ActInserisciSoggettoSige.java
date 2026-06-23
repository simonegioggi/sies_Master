package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActInserisciSoggettoSige - Azione di Inserimento del Soggetto Sige, in presenza di Omonimia
 *
 * @version 1.0
 */
public class ActInserisciSoggettoSige extends ActionSiap implements ICostantiSoggetto {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPage = ""; // per passaggio a jsp
		String lPagina = "1"; // per contare ricerca

		SoggettoModel lSogMod = new SoggettoModel();

		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA))
			lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		if (!this.isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA))
			lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		if (!this.isRequestParameterNullObj(CAMPO_ANNO_DATA_NASCITA)
				&& !this.isRequestParameterNullObj(CAMPO_MESE_DATA_NASCITA)
				&& !this.isRequestParameterNullObj(CAMPO_GIORNO_DATA_NASCITA))
			lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
					CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));
		// MEV 57: aggiungo il set per il campo data commesso reato
		// 20181130 [SG]: segnalazione SIES 11.1.2: Errore nella protocollazione SIGE (PILLITTERI)
		// aggiunto controllo di consistenza x 5 (anche sotto)
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_COMMESSO_REATO)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_COMMESSO_REATO))
				&& !isRequestParameterNullObj(CAMPO_MESE_DATA_COMMESSO_REATO)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_MESE_DATA_COMMESSO_REATO))
				&& !isRequestParameterNullObj(CAMPO_GIORNO_DATA_COMMESSO_REATO)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_GIORNO_DATA_COMMESSO_REATO))) {
			lSogMod.setDataReatoSius(getRequestDateParameter(CAMPO_ANNO_DATA_COMMESSO_REATO,
					CAMPO_MESE_DATA_COMMESSO_REATO, CAMPO_GIORNO_DATA_COMMESSO_REATO));
		}

		// MEV 57: aggiungo il set per i campo eta_presunta_anni ed eta_presunta_mesi
		// 20181130 [SG]: segnalazione SIES 11.1.2: Errore nella protocollazione SIGE (PILLITTERI)
		if (!isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_ANNI)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_ANNI)))
			lSogMod.setEtaPresuntaAnni(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_ANNI));
		if (!isRequestParameterNullObj(CAMPO_ETA_PRESUNTA_MESI)
				&& !Utils.isNullObj(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_MESI)))
			lSogMod.setEtaPresuntaMesi(getRequestBigDecimalParameter(CAMPO_ETA_PRESUNTA_MESI));

		lSogMod.setCodComuneNascita(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA));
		lSogMod.setCodProvinciaNascita(getRequestStringParameter(CAMPO_COD_PROVINCIA_NASCITA));
		lSogMod.setCodComuneCasellario(getRequestStringParameter(CAMPO_COD_COMUNE_CASELLARIO));

		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		lSogMod.setFlagPresenzaFascicolo("N");

		lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lSogMod.setDataInserimento(DateUtils.getSysDate());
		lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();
		lSogMod.setDescrStatoNascita(lDescri);

		// lSogMod.setDescrComuneNascita(lComMod.getDescrizione());

		// Imposto la descrizione del comune
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("PROVINCIA");
		lDecMod.setCode(lSogMod.getCodProvinciaNascita());
		List lProvincie = (List) DecodificheManager.getInstance().getProvincie();
		lIndModel = lProvincie.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lProvincie.get(lIndModel)).getDescription();
		lSogMod.setDescrProvinciaNascita(lDescri);

		// Imposto la descrizione di Stato Cittadinanza (nel model sono NAZIONALITA e DESCRNAZIONALITA)
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getNazionalita());
		List lStatoCitt = (List) DecodificheManager.getInstance().getStatoCittadinanza();
		lIndModel = lStatoCitt.indexOf(lDecMod);
		lDescri = ((DecodificheModel) lStatoCitt.get(lIndModel)).getDescription();
		lSogMod.setDescrNazionalita(lDescri);

		// 20260415 [SG]: aggiunto controllo su CF che deve essere obbligatorio e conforme
		// SoggettoUtil.controllaCF(lSogMod);

		// Chiama il controller
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		IFascicoloSiep lFascSogCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
		SoggettoModel lSogRetMod = null;

		lSogRetMod = lSogCtrl.ExInserisciSoggetto(lSogMod);

		BigDecimal CountRisultati = null;

		if (lSogRetMod != null && lSogRetMod.getMessage().startsWith("Inserimento")) {
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
					+ lSogRetMod.getIdSoggetto().toString();

		} else if (lSogRetMod != null && lSogRetMod.getMessage().startsWith("Soggetto")) {

			SoggettoModel lSogCuiMod = new SoggettoModel();
			String uffcio = this.getCodUfficioUtenteConnesso();
			String distretto = this.getCodDistrettoUtenteConnesso();
			String TipoRicerca = "ufficio";
			lSogCuiMod.setCodAfis(lSogRetMod.getCodAfis());
			Vector lFascicoliSoggetti = null;
			try {
				lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascicoliBySoggettoPaged(lSogCuiMod, uffcio,
						Integer.parseInt(lPagina), distretto, TipoRicerca);
			} catch (Exception sEx) {
				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "="
						+ lSogRetMod.getIdSoggetto().toString();
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug("Fascicoli non trovati per quel soggetto");
			}

			if (lFascicoliSoggetti != null) {
				setRequestAttribute("fascicoli", lFascicoliSoggetti);
				if (isRequestParameterNullObj("CountRisultati")) {
					CountRisultati = lSogCtrl.ExGetCountSoggettiPerProcedimenti(lSogMod, uffcio, distretto,
							TipoRicerca);
				} else {
					CountRisultati = getRequestBigDecimalParameter("CountRisultati");
				}

				setRequestAttribute("soggetto", lSogRetMod);
				setRequestAttribute("CountRisultati", CountRisultati);
				setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

				lPage = PG_LOAD_INSERISCICUI;
				this.setFunctionsAvailableToRequest("siap.sico.soggetto.action.ActRicercaSoggetto");
			}
		}

		// pagina di ritorno
		return lPage;
	}

}