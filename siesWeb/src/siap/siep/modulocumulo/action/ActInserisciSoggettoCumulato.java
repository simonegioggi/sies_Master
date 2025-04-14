package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActInserisciSoggettoCumulato</p>
 * <p>Description: Azione di Inserimento del Soggetto Cumulato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
//import f3b.web.RedirectTo;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciSoggettoCumulato extends ActionModuloCumulo implements ICostantiSoggettoCumulato {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**************************************************************************************
	 * Azione di Inserimento del Soggetto Cumulato
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 **************************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		String lModo = getRequestStringParameter("modalita");

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("--XX-- ActInserisciSoggettoCumulato - Inizio - Modo = " + lModo);

		this.setLinkRitorno();
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		BigDecimal lIdTitolo = getRequestBigDecimalParameter(
				ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		SoggettoCumulatoModel lSogMod = null;

		if (lModo.compareTo("M") == 0) {
			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CUMULATO);
			ISoggettoCumulato lSogCtrlC = SIEPLookupRemote.getSoggettoCumuloRemote();
			SoggettoCumulatoModel lSoggetto = lSogCtrlC.ExRicercaSoggettoCumulatoByKey(lId);
			if (lSoggetto != null && lSoggetto.getIdSoggettoCumulato() != null)
				lSogMod = new SoggettoCumulatoModel(lSoggetto);
		} else {
			lSogMod = new SoggettoCumulatoModel();
		}

		lSogMod.setCodFiscale(getRequestStringParameter(CAMPO_COD_FISCALE).toUpperCase());
		lSogMod.setCodAfis(getRequestStringParameter(CAMPO_COD_AFIS).toUpperCase());
		lSogMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(CAMPO_NOME));
		lSogMod.setAnnoNascita(getRequestBigDecimalParameter(CAMPO_ANNO_DATA_NASCITA));
		lSogMod.setMeseNascita(getRequestBigDecimalParameter(CAMPO_MESE_DATA_NASCITA));
		lSogMod.setDataNascita(getRequestDateParameter(CAMPO_ANNO_DATA_NASCITA, CAMPO_MESE_DATA_NASCITA,
				CAMPO_GIORNO_DATA_NASCITA));
		lSogMod.setDataNascitaPresunta(getRequestStringParameter(CAMPO_DATA_NASCITA_PRESUNTA));

		// Recupero dati del Comune di nascita
		ComuneModel lComMod;
		if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
				&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
			// se presente dal codice comune (e descrizione)
			lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(
					getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
					getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
		} else {
			// altrimenti dalla sola descrizione (rischio omonimi)
			lComMod = new ComuneModel(
					// 20210521	MEV_Scheda-21 Correzione per la gestione delle Omonimie dei Comuni.
					//getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
					getDatiComuneByDescrOmonimia(getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));    	
		}

		lSogMod.setCodComuneNascita(lComMod.getCodComune());
		lSogMod.setCodProvinciaNascita(lComMod.getCodProvincia());

		// Casellario di ROMA (CodComuneCasellario = 342) per gli stranieri (CAMPO_COD_STATO_NASCITA != 039 )
		// Sede Giudiziaria del luogo di nascita per gli italiani (CAMPO_COD_STATO_NASCITA = 039 -->
		// CodComuneCasellario = sede giudiziaria comune di nascita)
		if (!this.isRequestParameterNullObj(CAMPO_COD_STATO_NASCITA)
				&& getRequestStringParameter(CAMPO_COD_STATO_NASCITA) != null
				&& !getRequestStringParameter(CAMPO_COD_STATO_NASCITA).equals("039")) {
			lSogMod.setCodComuneCasellario("342");
		} else {
			lSogMod.setCodComuneCasellario(lComMod.getCodSedeGiudiziaria());
		}

		lSogMod.setCodStatoNascita(getRequestStringParameter(CAMPO_COD_STATO_NASCITA));
		lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_NASCITA_ESTERO));
		lSogMod.setNazionalita(getRequestStringParameter(CAMPO_NAZIONALITA));
		lSogMod.setPaternita(getRequestStringParameter(CAMPO_PATERNITA));
		lSogMod.setCognomeMadre(getRequestStringParameter(CAMPO_COGNOME_MADRE));
		lSogMod.setNomeMadre(getRequestStringParameter(CAMPO_NOME_MADRE));
		lSogMod.setSesso(getRequestStringParameter(CAMPO_SESSO));
		lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA).toUpperCase());
		lSogMod.setNote(getRequestStringParameter(CAMPO_NOTE));

		lSogMod.setFlagStato("I"); // Inserita manualmente
		lSogMod.setTitIdTitoloCumulato(lIdTitolo);

		if (lModo.compareTo("I") == 0) {
			lSogMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lSogMod.setDataInserimento(DateUtils.getSysDate());
			lSogMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		} else if (lModo.compareTo("M") == 0) {
			lSogMod.setIdSoggettoCumulato(getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CUMULATO));
			lSogMod.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
			lSogMod.setDataAggiornamento(DateUtils.getSysDate());
			lSogMod.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

			if (getRequestStringParameter(CAMPO_FLAG_STATO).compareTo("I") != 0)
				lSogMod.setFlagStato("M");
		}

		// Imposto la descrizione della nazione
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSogMod.getCodStatoNascita());

		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);

		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();

		lSogMod.setDescrStatoNascita(lDescri);
		lSogMod.setDescrComuneNascita(lComMod.getDescrizione());

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

		String lDescriNazio = ((DecodificheModel) lStatoCitt.get(lIndModel)).getDescription();
		lSogMod.setDescrNazionalita(lDescriNazio);

		// Chiama il controller
		ISoggettoCumulato lSogCtrl = SIEPLookupRemote.getSoggettoCumuloRemote();
		SoggettoCumulatoModel lSogRetMod = new SoggettoCumulatoModel();

		if (lModo.compareTo("I") == 0)
			lSogRetMod = lSogCtrl.ExInserisciSoggetto_Cumulato(lSogMod);
		else if (lModo.compareTo("M") == 0)
			lSogRetMod = lSogCtrl.ExModificaSoggettoCumulato(lSogMod);

		String lPage = ""; // per passaggio a jsp

		if (lSogRetMod.getMessage().startsWith("Inserimento")
				|| lSogRetMod.getMessage().startsWith("Aggiornamento")) {
			// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
			// "=siap.sico.soggetto.action.ActLoadDettaglioSoggetto&" + CAMPO_ID_SOGGETTO + "=" +
			// lSogRetMod.getIdSoggetto().toString();
			lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
					+ "=siap.siep.modulocumulo.action.ActDettaglioSoggettoCumulato&"
					+ CAMPO_ID_SOGGETTO_CUMULATO + "=" + lSogRetMod.getIdSoggettoCumulato().toString();
		}

		return lPage;

	}
}