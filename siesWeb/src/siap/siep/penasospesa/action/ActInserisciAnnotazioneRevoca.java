package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * ActInserisciAnnotazioneRevoca - Classe di inserimento di annotazione revoca
 *
 * @version 1.0
 */
public class ActInserisciAnnotazioneRevoca extends ActionSiap {

	/**
	 * Action non inserisce nulla ma raccoglie i dati dalla request, li prepara per la pagina successiva e la
	 * richiama
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascicoloMod.getIdFascicoloSiep();

		if (getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("Sentenza"))
			setRequestAttribute("Motivo", "1100");
		else
			setRequestAttribute("Motivo", getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_MOTIVO));

		// Se non ho selezionato il provvedimento dalla lista, nel sistema non c'è e quindi
		// devo inserirlo a mano. Nel caso di sentenza inserisco una annotazione manuale
		AnnotazioneManualeModel lAnnotazione = new AnnotazioneManualeModel();
		if (getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("Sentenza")) {
			lAnnotazione = creaAnnotazione(lIdFascicolo);
		} else {
			lAnnotazione = creaAnnotazioneOrdinanza(lIdFascicolo);
		}
		setSessionAttribute("AnnotazioneMan", lAnnotazione);

		setRequestAttribute("DataArrivoAtto",
				getRequestDateParameter(ICostantiPenaSospesa.CAMPO_ANNO_DATA_ARRIVO_ATTO,
						ICostantiPenaSospesa.CAMPO_MESE_DATA_ARRIVO_ATTO,
						ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ARRIVO_ATTO));

		setRequestAttribute("DataIrrevocabilita",
				getRequestDateParameter("annoIrrevocabilita", "meseIrrevocabilita", "giornoIrrevocabilita"));

		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/penasospesa/LoadInserisciNuovoFascicoloSiep.jsp";
		return lPage;
	}

	/**
	 * Crea l'Annotazione Manuale che individua la sentenza di revoca
	 */

	private AnnotazioneManualeModel creaAnnotazione(BigDecimal aIdFascicolo) throws Exception {

		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		// Valorizzazione campi specifici
		lAnnMod.setCodTipoAnnotazione("016"); // Gestione Pene Sospese
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setFlagValidato("S");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());
		lAnnMod.setCodDpr("-");

		// I dati relativi alla nuova sentenza inserita vengono letti dalla request
		lAnnMod.setFasSieIdFascicoloSiep(aIdFascicolo);

		lAnnMod.setAnnoGe(getRequestBigDecimalParameter(ICostantiPenaSospesa.CAMPO_ANNO_RGNR_REVOCA));
		lAnnMod.setNumeroGe(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NUMERO_RGNR_REVOCA));

		// Metto nel campo Anno/Numero Rege i dati del RegGen perchè non so dove altro metterli!
		lAnnMod.setAnnoRege(getRequestBigDecimalParameter(ICostantiPenaSospesa.CAMPO_ANNO_RegGen_REVOCA));
		lAnnMod.setNumeroRege(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NUMERO_RegGen_REVOCA));

		lAnnMod.setAnnoSentenzaSiap(
				getRequestBigDecimalParameter(ICostantiPenaSospesa.CAMPO_ANNO_SENTENZA_REVOCA));
		lAnnMod.setNumeroSentenzaSiap(
				getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NUMERO_SENTENZA_REVOCA));

		lAnnMod.setDataSentenzaSiap(
				getRequestDateParameter(ICostantiPenaSospesa.CAMPO_ANNO_DATA_SENTENZA_REVOCA,
						ICostantiPenaSospesa.CAMPO_MESE_DATA_SENTENZA_REVOCA,
						ICostantiPenaSospesa.CAMPO_GIORNO_DATA_SENTENZA_REVOCA));

		lAnnMod.setCodTipoUfficioSiep(
				getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_SENTENZA_REVO));
		lAnnMod.setCodLuogoUfficioSiep(getCodComuneByDescr(
				getRequestStringParameter(ICostantiPenaSospesa.CAMPO_COD_LUOGO_SENTENZA_REVOCA))
						.getCodComune());

		if (isRequestChecked(ICostantiPenaSospesa.CAMPO_FLAG_SOSP_COND))
			lAnnMod.setMotivazioni("Sospensione Condizionale;");

		if (isRequestChecked(ICostantiPenaSospesa.CAMPO_FLAG_NON_MENZIONE))
			lAnnMod.setMotivazioni(lAnnMod.getMotivazioni() + "Non menzione;");

		// Si lega l'Annotazione del Provvedimento del PM all'evento di annotazione revoca
		// ma lo faccio nel controller quando ho inserito l'evento e quindi conosco l'id
		// lAnnMod.setEveIdEvento();

		return lAnnMod;
	}

	private AnnotazioneManualeModel creaAnnotazioneOrdinanza(BigDecimal aIdFascicolo) throws Exception {

		AnnotazioneManualeModel lAnnMod = new AnnotazioneManualeModel();

		// Valorizzazione campi specifici
		lAnnMod.setCodTipoAnnotazione("016"); // Gestione Pene Sospese
		lAnnMod.setFlagAppProvvisoria("-");
		lAnnMod.setFlagValidato("S");
		lAnnMod.setCodFonte("-");
		lAnnMod.setCodSottonumerazione("-");
		lAnnMod.setCodCausaleComputo("-");
		lAnnMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lAnnMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lAnnMod.setDataInserimento(DateUtils.getSysDate());
		lAnnMod.setCodDpr("-");
		lAnnMod.setFasSieIdFascicoloSiep(aIdFascicolo);

		// I dati relativi alla Ordinanza del GE vengono letti dalla request
		lAnnMod.setAnnoGe(getRequestBigDecimalParameter(ICostantiPenaSospesa.CAMPO_ANNO_ORDINANZA_REVOCA));
		lAnnMod.setNumeroGe(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NUMERO_ORDINANZA_REVOCA));

		lAnnMod.setDataGE(getRequestDateParameter(ICostantiPenaSospesa.CAMPO_ANNO_DATA_ORDINANZA_REVOCA,
				ICostantiPenaSospesa.CAMPO_MESE_DATA_ORDINANZA_REVOCA,
				ICostantiPenaSospesa.CAMPO_GIORNO_DATA_ORDINANZA_REVOCA));

		// Lettura dati dell'Autorità Emittente
		String lDescrComune = getRequestStringParameter(
				ICostantiPenaSospesa.CAMPO_COD_LUOGO_ORDINANZA_REVOCA);
		String lCodTipoUfficioEmi = getRequestStringParameter(
				ICostantiPenaSospesa.CAMPO_COD_TIPO_UFFICIO_ORDINANZA_REVO);
		lAnnMod.setCodTipoUfficioSiep(lCodTipoUfficioEmi);
		lAnnMod.setCodLuogoUfficioSiep(getCodComuneByDescr(lDescrComune).getCodComune());

		// Questo solo per controllo validità dati
		getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmi, lDescrComune);

		// Lettura Note
		lAnnMod.setMotivazioni(getRequestStringParameter(ICostantiPenaSospesa.CAMPO_NOTE));

		if (isRequestChecked(ICostantiPenaSospesa.CAMPO_FLAG_NON_MENZIONE))
			lAnnMod.setMotivazioni(lAnnMod.getMotivazioni() + "Non menzione;");

		return lAnnMod;
	}

}