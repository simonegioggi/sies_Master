package siap.siep.calcolopena.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata;
import siap.sico.libertaanticipata.action.ICostantiLicenzaLibanticipata;
import siap.sico.libertaanticipata.controller.ILicenzaPeriodiLibAnticipata;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel;
import siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * Action per l'inserimento del Ridimensionamento LA e della Revoca LA
 */
@SuppressWarnings("rawtypes")
public class ActInserisciRidimensionamentoLA extends ActionSiap implements ICostantiLibertaAnticipata {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 *    
	 */
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("INIZIO");

		this.removeSessionAttribute("ID_LASORV");

		Date mOggi = DateUtils.getSysDate(); // Data odierna

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String tipoCumputoLA = ""; // RIDIMENSIONAMENTO/COMPUTO

		if (!isRequestParameterNullObj(ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA)) {
			tipoCumputoLA = getRequestStringParameter(ICostantiLibertaAnticipata.CAMPO_TIPO_COMPUTO_LA);
		}

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("tipoCumputoLA = " + tipoCumputoLA);

		EventoModel lEveProvvedimentoMod = new EventoModel();

		lEveProvvedimentoMod.setCodTipoEvento("01");
		lEveProvvedimentoMod.setCodTipoProvvedimento("25");
		if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
			lEveProvvedimentoMod.setCodMotivo("0995");
		else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA))
			lEveProvvedimentoMod.setCodMotivo("1008");

		lEveProvvedimentoMod.setFlagDocumentoRegistrato(null);
		lEveProvvedimentoMod.setFlagStampaSiep("S");
		lEveProvvedimentoMod.setFlagVideoSiep("S");

		lEveProvvedimentoMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveProvvedimentoMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveProvvedimentoMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lEveProvvedimentoMod.setDataEmissione(getRequestDateParameter(
				ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE, ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		// lEveProvvedimentoMod.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
		// ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveProvvedimentoMod
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		lEveProvvedimentoMod.setCodEsito("-");
		lEveProvvedimentoMod.setCodTipoUfficioDestinatario("-");
		lEveProvvedimentoMod.setCodLuogoDestinatario("-");

		lEveProvvedimentoMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveProvvedimentoMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveProvvedimentoMod.setDataInserimento(DateUtils.getSysDate());

		// Se la pena non è in decorrenza inserisco l'evento come già validato
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lUltimaPenResVal = lPenResCtrl.ExRicercaPenaResiduaUltimaByDate(lIdFascicolo);

		// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.info("lUltimaPenResVal="+lUltimaPenResVal +" e data inizio pena
		// ="+lUltimaPenResVal.getDataInizio());
		// Se la pena non è in decorrenza ilprovvedimento viene inserito validato. Non è
		// possibile effettuare il calcolo pena ne emettere l'Ordine di scarcerazione
		if (lUltimaPenResVal == null || lUltimaPenResVal.getDataInizio() == null
		// MEV29 anche nel caso di ergastolo l'annotazione va inserita validata in quanto non si può
		// procedere all'emissione di ulteriori provvedimenti esecutivo
		// Soprattutto non si puà procedere al calcolo Pena.
				|| lUltimaPenResVal.isErgastolo()) {
			lEveProvvedimentoMod.setFlagDocumentoRegistrato("S");
		}

		// ===========================================================
		// Recupero le Note (se presenti)
		// ===========================================================
		CampoNotaModel lCampoNota = new CampoNotaModel();
		lCampoNota.setFasSieIdFascicoloSiep(lIdFascicolo);

		if (getRequestStringParameter("noteComputo") != null
				&& !getRequestStringParameter("noteComputo").equals(""))
			lCampoNota.setDescr(getRequestStringParameter("noteComputo"));
		else
			lCampoNota.setDescr("");

		lCampoNota.setProgressivo(new BigDecimal(1));

		lCampoNota.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lCampoNota.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lCampoNota.setDataInserimento(DateUtils.getSysDate());

		// ==========================================================================
		// Recupero le LA : parte comune per tutte le tipologie di L.A.
		// Dati del provvedimento di RIDIMENSIONAMENTO/REVOCA (Sorveglianza)
		// ==========================================================================
		String lCodTipoUfficioEmittente = null;
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE + "_AA"))
			lCodTipoUfficioEmittente = getRequestStringParameter(
					ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE + "_AA");

		String lCodUDS = null;
		String lCodComune = null;
		if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA")) {
			lCodComune = getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA");
			if (lCodComune != null && !lCodComune.equals("") && !lCodComune.equals("-"))
				lCodUDS = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficioEmittente, lCodComune);
		}

		// Se dati selezionati dalla lista
		String lIdSorveglianzaLa = getRequestStringParameter(
				ICostantiLicenzaLibanticipata.CAMPO_ID_LICENZA_LIBANTICIPATA);
		String idEve = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("lIdSorveglianzaLa = "+lIdSorveglianzaLa);
		String Concessione = "C"; // per default vengono aggiunti

		// Solo in caso di ridimensionamento passo in sessione l'idSorv per
		// consentire al modulo di Calcolo Pena di verificare come trattare
		// i dati. (sommare o detrarre)
		if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM)) {
			this.setSessionAttribute("ID_LASORV", lIdSorveglianzaLa);
		} else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)) {
			// nella revoca sono sempre Scomputate
		}

		// Determino se i gg computati sono da sottrarre o aggiungere.
		// n.b. Nel caso di Ridimensionamento, si seleziona dalla lista le LA già concesse
		// da Ridimensionare e non il provvedimento di ridimensionamento.
		// Se le LA concesse sono state già computate, allora l'utente deve indicare
		// i GG di LA da sottrarre: es concessi e computati 135 (selezionati dalla lista)
		// , da sottrarre 45 per un totale da applicare di 90 effettivi.
		// I 45 sono da sottrarre e vengono marcati FLAG_CONCESSO = 'S' (scomputo)
		//
		// Se le LA concesse (selezionate dalla lista) non sono ancora state computate
		// sul fine pena, l'utente deve indicare in maschera i GG effettivi concessi
		// che vanno quindi considerati da computare FLAG_CONCESSO = 'C' (computo).
		// es: 135 concessi (selezionati dalla lista) ma non ancora computati,
		// vengono ridimensionati di 45gg quindi l'utente deve indicare in
		// maschera 90gg quelli effettivamente da applicare.
		//
		//
		// In caso di REVOCA l'utente seleziona invece dalla lista il provvedimento
		// di revoca che indica i giorni revocati. I giorni indicati in maschera
		// vanno sempre considerati da detrarre: FLAG_CONCESSO = 'S' (scomputo)
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("lIdSorveglianzaLa = "+lIdSorveglianzaLa);
		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		// siesLogger.debug("tipoCumputoLA = "+tipoCumputoLA);
		if (!lIdSorveglianzaLa.equals("")) {
			// Provvedimento selezionato dalla lista
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ----- >  L.A. Da Lista : Evento = " + idEve);
			ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

			LicenzaLibAnticipataModel lSorvModel = lCtrlLib
					.ExRicercaLicenzaLibanticipataByKey(new BigDecimal(lIdSorveglianzaLa));

			if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM)) {
				if (lSorvModel.getFlagElaborato() == null)
					Concessione = "C"; // Ridimensionamento LA
				else { // 'E' Elaborato su PR non ancora validata. 'S' computate su PR validata (fine pena)
					if (!lSorvModel.getFlagElaborato().equals("E")
							&& !lSorvModel.getFlagElaborato().equals("S"))
						Concessione = "C"; // da Computare
					else
						Concessione = "S"; // da Scomputare
				}
			} else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA))
				Concessione = "S"; // da Scomputare
		} else {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Dati inseriti manualmente");
			// Se inserite a mano, nel caso di Ridimesionamento, i gg di LA vanno
			// sempre considerati come giorni da computare ovvero LA Concesse
			if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
				Concessione = "C"; // da Computare
			else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA))
				Concessione = "S"; // da Scomputare
		}

		// Solo per le stampe nel campo LICENZA_LIBANTICIPATA.ANNOTAZIONE viene salvato
		// il tipo di provvedimento
		String lAnnotazione = null;
		if ("02".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO + "_AA")))
			lAnnotazione = "DECRETO";
		else if ("03".equals(getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO + "_AA")))
			lAnnotazione = "ORDINANZA";

		// ==========================================================================
		// 20/05/2014 Nuova L.A. - DL 146/2013 - Gestione L.A., L.A. SPECIALE, L.A. INTEGRAZIONE
		// ==========================================================================
		// Gestione di L.A. ordinaria
		// ==============================================
		LicenzaPeriodiLibAnticipataModel lPeriodiLicenzaLA = null;

		if ((!isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA)
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA).equals("")
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA).equals("0"))
				|| (!lIdSorveglianzaLa.equals("") && !isRequestParameterNullObj("NumGiorniLibanticipataSorv")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv").equals("")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv").equals("0"))) {

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ----- >  L.A. ORDINARIA - GG = "
					+ getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA));
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ----- >  L.A. ORDINARIA - IdSorv = " + lIdSorveglianzaLa);

			lPeriodiLicenzaLA = new LicenzaPeriodiLibAnticipataModel();
			LicenzaLibAnticipataModel licLA = new LicenzaLibAnticipataModel();
			licLA.setAnnotazione(lAnnotazione);

			if (!this.isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA)
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA).equals("")
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA).equals("0")) {
				licLA.setNumeroGiorni(new BigDecimal(
						this.getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA)));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. Giorni - NumggLA = "
						+ this.getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA));
			} else {
				licLA.setNumeroGiorni(
						new BigDecimal(this.getRequestStringParameter("NumGiorniLibanticipataSorv")));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. Giorni - IdSorv = "
						+ this.getRequestStringParameter("NumGiorniLibanticipataSorv"));
			}

			licLA.setCodTipoLicenza("LA");
			licLA.setCodOperatoreInserimento(getCodUtenteConnesso());
			licLA.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			licLA.setDataInserimento(mOggi);
			licLA.setFasSieIdFascicoloSiep(lIdFascicolo);

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("-"))
				licLA.setAnnoSius(
						getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
							.equals("-"))
				licLA.setNumeroSius(
						getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				licLA.setAnnoOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				licLA.setNumeroOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

			licLA.setFlagConcesso(Concessione);
			licLA.setCodUfficioEmittente(lCodUDS);

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA")) {
				ComuneModel lComune = getCodComuneByDescr(
						getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA"));
				licLA.setCodLuogoEmittente(lComune.getCodComune());
			}

			licLA.setDataEmissioneOrdinanza(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			licLA.setDescrStatoPermesso("LA");
			lPeriodiLicenzaLA.setLicenza(licLA);

			// ========================================================================
			// Leggo i periodi di LA ordinaria
			// ========================================================================

			Date[] lDateInizio = null;
			Date[] lDateFine = null;

			lDateInizio = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO,
					ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO);
			lDateFine = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE, ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE);

			int num = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio[jj] != null && lDateFine[jj] != null)
					num++;

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("ci sono "+num+" periodi validi di L.A. ");

			if (num > 0) { // Presenti i periodi digitati a mano (DAL-AL)
				PeriodoLibAnticipataModel[] lPeriodoLibLA = new PeriodoLibAnticipataModel[num];
				for (int jj = 0, ii = 0; jj < 6; jj++) {
					if (lDateInizio[jj] != null && lDateFine[jj] != null) {
						lPeriodoLibLA[ii] = new PeriodoLibAnticipataModel();
						lPeriodoLibLA[ii].setDataInizio(lDateInizio[jj]);
						lPeriodoLibLA[ii].setDataFine(lDateFine[jj]);
						lPeriodoLibLA[ii].setCodOperatoreInserimento(getCodUtenteConnesso());
						lPeriodoLibLA[ii].setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lPeriodoLibLA[ii].setDataInserimento(mOggi);
						lPeriodoLibLA[ii].setFlagConcesso(Concessione);

						ii++;
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug(" XX  Ho riempito " + ii + " periodi validi di L.A.");
					}
				}

				lPeriodiLicenzaLA.setPeriodi(lPeriodoLibLA);
			}

		}

		// End L.A. Ordinaria

		// ==========================================================================
		// Gestione di L.A. SPECIALE
		// ==========================================================================
		LicenzaPeriodiLibAnticipataModel lPeriodiLicenzaLASPE = null;

		if ((!isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE)
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE).equals("")
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE).equals("0"))
				|| (!lIdSorveglianzaLa.equals("")
						&& !isRequestParameterNullObj("NumGiorniLibanticipataSorv_SPE")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv_SPE").equals("")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv_SPE").equals("0"))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ------------ >  L.A. SPECIALE - GG = "
					+ getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE));

			lPeriodiLicenzaLASPE = new LicenzaPeriodiLibAnticipataModel();
			LicenzaLibAnticipataModel licSPE = new LicenzaLibAnticipataModel();
			licSPE.setAnnotazione(lAnnotazione);

			if (!isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE)
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE).equals("")
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE).equals("0")) {
				licSPE.setNumeroGiorni(new BigDecimal(
						getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE)));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. SPECIALE Giorni - NumggLA_SPE = "
						+ getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_SPE));
			} else {
				licSPE.setNumeroGiorni(
						new BigDecimal(this.getRequestStringParameter("NumGiorniLibanticipataSorv_SPE")));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. SPECIALE Giorni - IdSorv_SPE = "
						+ getRequestStringParameter("NumGiorniLibanticipataSorv_SPE"));
			}

			licSPE.setCodTipoLicenza("LA");
			licSPE.setCodOperatoreInserimento(getCodUtenteConnesso());
			licSPE.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			licSPE.setDataInserimento(mOggi);
			licSPE.setFasSieIdFascicoloSiep(lIdFascicolo);

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("-"))
				licSPE.setAnnoSius(
						getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
							.equals("-"))
				licSPE.setNumeroSius(
						getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				licSPE.setAnnoOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				licSPE.setNumeroOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

			licSPE.setFlagConcesso(Concessione);
			licSPE.setCodUfficioEmittente(lCodUDS);

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA")) {
				ComuneModel lComune = getCodComuneByDescr(
						getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA"));
				licSPE.setCodLuogoEmittente(lComune.getCodComune());
			}

			licSPE.setDataEmissioneOrdinanza(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			licSPE.setDescrStatoPermesso("LS");
			lPeriodiLicenzaLASPE.setLicenza(licSPE);

			// ==========================================================================
			// LEggo i periodi di LA SPECIALE
			// ==========================================================================
			// __________________________
			Date[] lDateInizio_spe = null;
			Date[] lDateFine_spe = null;

			lDateInizio_spe = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_SPE,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_SPE,
					ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_SPE);
			lDateFine_spe = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_SPE,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_SPE,
					ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_SPE);

			int num_spe = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio_spe[jj] != null && lDateFine_spe[jj] != null)
					num_spe++;

			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("ci sono "+num_spe+" periodi validi di L.A. SPECIALE");

			if (num_spe > 0) {
				PeriodoLibAnticipataModel[] lPeriodoLibLASPE = new PeriodoLibAnticipataModel[num_spe];
				for (int jj = 0, ii = 0; jj < 6; jj++) {
					if (lDateInizio_spe[jj] != null && lDateFine_spe[jj] != null) {
						lPeriodoLibLASPE[ii] = new PeriodoLibAnticipataModel();
						lPeriodoLibLASPE[ii].setDataInizio(lDateInizio_spe[jj]);
						lPeriodoLibLASPE[ii].setDataFine(lDateFine_spe[jj]);
						lPeriodoLibLASPE[ii].setCodOperatoreInserimento(getCodUtenteConnesso());
						lPeriodoLibLASPE[ii].setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lPeriodoLibLASPE[ii].setDataInserimento(mOggi);
						lPeriodoLibLASPE[ii].setFlagConcesso(Concessione);

						ii++;
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Ho riempito " + ii + " periodi validi di L.A. SPECIALE");
					}
				}
				lPeriodiLicenzaLASPE.setPeriodi(lPeriodoLibLASPE);
			}

		}

		// End L.A. SPECIALE

		// ==========================================================================
		// Gestione di L.A. INTEGRAZIONE
		// ==============================================

		LicenzaPeriodiLibAnticipataModel lPeriodiLicenzaLAINT = null;

		if ((!isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT)
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT).equals("")
				&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT).equals("0"))
				|| (!lIdSorveglianzaLa.equals("")
						&& !isRequestParameterNullObj("NumGiorniLibanticipataSorv_INT")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv_INT").equals("")
						&& !getRequestStringParameter("NumGiorniLibanticipataSorv_INT").equals("0"))) {
			lPeriodiLicenzaLAINT = new LicenzaPeriodiLibAnticipataModel();
			LicenzaLibAnticipataModel licINT = new LicenzaLibAnticipataModel();
			licINT.setAnnotazione(lAnnotazione);

			if (!isRequestParameterNullObj(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT)
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT).equals("")
					&& !getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT).equals("0")) {
				licINT.setNumeroGiorni(new BigDecimal(
						getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT)));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. INTEGRAZIONE Giorni - NumggLA_INT = "
						+ getRequestStringParameter(ICostantiCalcoloPena.CAMPO_NUM_GIORNI_LA_INT));
			} else {
				licINT.setNumeroGiorni(
						new BigDecimal(this.getRequestStringParameter("NumGiorniLibanticipataSorv_INT")));
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" ----- >  L.A. INTEGRAZIONE Giorni - IdSorv_INT = "
						+ getRequestStringParameter("NumGiorniLibanticipataSorv_INT"));
			}

			licINT.setCodTipoLicenza("LA");
			licINT.setCodOperatoreInserimento(getCodUtenteConnesso());
			licINT.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			licINT.setDataInserimento(mOggi);
			licINT.setFasSieIdFascicoloSiep(lIdFascicolo);

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS).equals("-"))
				licINT.setAnnoSius(
						getRequestBigDecimalParameter(ICostantiLicenzaLibanticipata.CAMPO_ANNO_SIUS));

			if (!isRequestParameterNullObj(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS).equals("")
					&& !getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS)
							.equals("-"))
				licINT.setNumeroSius(
						getRequestStringParameter(ICostantiLicenzaLibanticipata.CAMPO_NUMERO_SIUS));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				licINT.setAnnoOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));
			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				licINT.setNumeroOrdinanza(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

			licINT.setFlagConcesso(Concessione);
			licINT.setCodUfficioEmittente(lCodUDS);

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA")) {
				ComuneModel lComune = getCodComuneByDescr(
						getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA"));
				licINT.setCodLuogoEmittente(lComune.getCodComune());
			}

			licINT.setDataEmissioneOrdinanza(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			licINT.setDescrStatoPermesso("LI");
			//
			lPeriodiLicenzaLAINT.setLicenza(licINT);

			// ==========================================================================
			// LEggo i periodi di LA INTEGRAZIONE
			// ==========================================================================
			// __________________________
			Date[] lDateInizio_int = null;
			Date[] lDateFine_int = null;

			lDateInizio_int = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_INIZIO_INT,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_INIZIO_INT,
					ICostantiCalcoloPena.CAMPO_GIORNO_DATA_INIZIO_INT);
			lDateFine_int = getRequestDateParameters(ICostantiCalcoloPena.CAMPO_ANNO_DATA_FINE_INT,
					ICostantiCalcoloPena.CAMPO_MESE_DATA_FINE_INT,
					ICostantiCalcoloPena.CAMPO_GIORNO_DATA_FINE_INT);

			int num_int = 0;
			for (int jj = 0; jj < 6; jj++)
				if (lDateInizio_int[jj] != null && lDateFine_int[jj] != null)
					num_int++;

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("ci sono " + num_int + " periodi validi di L.A. INTEGRAZIONE ");

			if (num_int > 0) {
				PeriodoLibAnticipataModel[] lPeriodoLibLAINT = new PeriodoLibAnticipataModel[num_int];
				for (int jj = 0, ii = 0; jj < 6; jj++) {
					if (lDateInizio_int[jj] != null && lDateFine_int[jj] != null) {
						lPeriodoLibLAINT[ii] = new PeriodoLibAnticipataModel();
						lPeriodoLibLAINT[ii].setDataInizio(lDateInizio_int[jj]);
						lPeriodoLibLAINT[ii].setDataFine(lDateFine_int[jj]);
						lPeriodoLibLAINT[ii].setCodOperatoreInserimento(getCodUtenteConnesso());
						lPeriodoLibLAINT[ii].setCodUfficioInserimento(getCodUfficioUtenteConnesso());
						lPeriodoLibLAINT[ii].setDataInserimento(mOggi);
						lPeriodoLibLAINT[ii].setFlagConcesso(Concessione);

						ii++;
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto
						// di LogF3B.getLogger()
						siesLogger.debug("Ho riempito " + ii + " periodi validi di L.A. INTEGRAZIONE");
					}
				}

				lPeriodiLicenzaLAINT.setPeriodi(lPeriodoLibLAINT);
			}

		}

		// End L.A. INTEGRAZIONE

		// -- -- -- --- --- ---- ----
		// ==========================================================================
		// ANNA 16/02/2011 per gestire i Dati del Provvedimento della Sorveglianza
		//
		// ==========================================================================

		// LicenzaPeriodiLibAnticipataModel lPeriodiLicenzaRet = null;
		BigDecimal lIdEventoSIEPInserito = null;

		EventoModel lEveAltroUff = null;

		// ==========================================================================
		// Se non ho selezionato dalla lista il provvedimento della sorveglianza
		// inserisco un evento [01-02/03-0076] decreto/ordinanza concessione LA Liberazione Anticipata
		// n.b. TipoOrd è sempre = altroUfficio
		if (getRequestStringParameter("TipoOrd").equals("altroUfficio") && (lIdSorveglianzaLa.equals(""))) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("Tipo ORD = altroufficio e lIdSorveglianza= '' ");

			lEveAltroUff = new EventoModel();

			lEveAltroUff.setFasSieIdFascicoloSiep(lIdFascicolo);
			lEveAltroUff.setCodTipoEvento("01");
			lEveAltroUff.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_TIPO_PROVVEDIMENTO + "_AA"));

			if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
				lEveAltroUff.setCodMotivo("0076");
			else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)) {
				// in caso di revoca s hanno 3 codici per TDS e 3 per MDS a seconda del peso
				if ("TDS".equals(lCodTipoUfficioEmittente))
					lEveAltroUff.setCodMotivo("0028"); // 0028-0620-0621 (LA,LS,LI)
				else if ("UDS".equals(lCodTipoUfficioEmittente))
					lEveAltroUff.setCodMotivo("2135"); // 2135-2136-2137 (LA,LS,LI)
				else
					lEveAltroUff.setCodMotivo("0076"); //
			}

			lEveAltroUff.setFlagDocumentoRegistrato("S"); // Per ora lo inserisco validato
			lEveAltroUff.setFlagStampaSiep("S");
			lEveAltroUff.setFlagVideoSiep("S");

			// Autorità emittente
			String lCodTipoUff = getRequestStringParameter(
					ICostantiEvento.CAMPO_COD_UFFICIO_EMITTENTE + "_AA");
			String lDescrComune = getRequestStringParameter(
					ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA");

			ComuneModel lComune = this.getCodComuneByDescr(
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_LUOGO_EMITTENTE + "_AA"));
			String lCodLuogoEmittente = lComune.getCodComune();

			String lCodUffEmittente = this.getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUff,
					lDescrComune);
			lEveAltroUff.setCodUfficioEmittente(lCodUffEmittente);
			lEveAltroUff.setCodLuogoEmittente(lCodLuogoEmittente);

			lEveAltroUff.setDataEmissione(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE + "_AA"));
			lEveAltroUff.setDataRicezioneAtti(
					getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_RICEZIONE_ATTI + "_AA",
							ICostantiEvento.CAMPO_MESE_DATA_RICEZIONE_ATTI + "_AA",
							ICostantiEvento.CAMPO_GIORNO_DATA_RICEZIONE_ATTI + "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"))
				lEveAltroUff.setAnnoProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ANNO_PROTOCOLLO + "_AA"));

			if (!isRequestParameterNullObj(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"))
				lEveAltroUff.setProgrProtocollo(
						getRequestBigDecimalParameter(ICostantiEvento.CAMPO_PROGR_PROTOCOLLO + "_AA"));

			//
			lEveAltroUff.setCodEsito("-");
			lEveAltroUff.setCodTipoUfficioDestinatario("-");
			lEveAltroUff.setCodLuogoDestinatario("-");

			lEveAltroUff.setCodOperatoreInserimento(getCodUtenteConnesso());
			lEveAltroUff.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lEveAltroUff.setDataInserimento(DateUtils.moveDateTo(lEveProvvedimentoMod.getDataInserimento(),
					java.util.Calendar.SECOND, -1));

			// Devo inserire il provv della sorveglianza che non è ancora stato inserito e quello
			// corrente.

			ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

			// 20/05/2013 Nuova L.A. - DL 146/2013
			// lPeriodiLicenza =
			// lCtrlLib.ExInserisciRidimLibanticipataSorv(lEveProvvedimentoMod,lEveAltroUff,lCampoNota,
			// lPeriodiLicenza);
			// Inserimento contestuale del Provvedimento Sorveglianza, del provvedimento SIEP dei dati LA
			lIdEventoSIEPInserito = lCtrlLib.ExInserisciRidimLibanticipataSorv(lEveProvvedimentoMod,
					lEveAltroUff, lCampoNota, lPeriodiLicenzaLA, lPeriodiLicenzaLASPE, lPeriodiLicenzaLAINT);
		} else {
			// Il provv della sorveglianza già c'è, devo inserire solo questo e devo associargli gli
			// stessi periodi di LA già inseriti
			// ==========================================================================
			// Inserimento dei dati
			// ==========================================================================
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug(" ---->  ELSE di ifTipo ORD = altroufficio e lIdSorveglianza= '' ");
			// int TotPeriodi=0;
			int TotPeriodiLA = 0;
			int TotPeriodiLAS = 0;
			int TotPeriodiLAI = 0;

			ILicenzaPeriodiLibAnticipata lCtrlLib = SICOLookupRemote.getLicenzaPeriodiLibAntRemote();

			if (!lIdSorveglianzaLa.equals("")) {
				// Evento Selezionato dalla lista, recupero le licenze concesse associate (e relativi periordi
				// se presenti)
				Vector llicenze = lCtrlLib.ExRicercaLicenzeLibanticipataByEve(new BigDecimal(idEve));
				Iterator ite1 = llicenze.iterator();

				while (ite1.hasNext()) {
					LicenzaPeriodiLibAnticipataModel Lmod = (LicenzaPeriodiLibAnticipataModel) ite1.next();
					ArrayList lperiodi = lCtrlLib
							.ExRicercaPeriodiByIdLA(Lmod.getLicenza().getIdLicenzaLibanticipata());

					if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM)) {
						// Ridimensionamento - Recupero il numero di periodi presenti se Computo (C)
						if (Lmod.getLicenza().getDescrStatoPermesso() != null) {
							if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LS")
									&& Lmod.getLicenza().getFlagConcesso().equals("C"))
								TotPeriodiLAS += lperiodi.size();
							else if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LI")
									&& Lmod.getLicenza().getFlagConcesso().equals("C"))
								TotPeriodiLAI += lperiodi.size();
							else if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LA")
									&& Lmod.getLicenza().getFlagConcesso().equals("C"))
								TotPeriodiLA += lperiodi.size();
						} else {
							if (Lmod.getLicenza().getFlagConcesso().equals("C"))
								TotPeriodiLA += lperiodi.size();
						}
					} else if (tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)) {
						// Revoca - Recupero il numero di periodi presenti se Scomputo (S)
						if (Lmod.getLicenza().getDescrStatoPermesso() != null) {
							if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LS")
									&& Lmod.getLicenza().getFlagConcesso().equals("S"))
								TotPeriodiLAS += lperiodi.size();
							else if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LI")
									&& Lmod.getLicenza().getFlagConcesso().equals("S"))
								TotPeriodiLAI += lperiodi.size();
							else if (Lmod.getLicenza().getDescrStatoPermesso().substring(0, 2).equals("LA")
									&& Lmod.getLicenza().getFlagConcesso().equals("S"))
								TotPeriodiLA += lperiodi.size();
						}
					}
				}

				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" tot periodi LA = " + TotPeriodiLA);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" tot periodi LAS = " + TotPeriodiLAS);
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.debug(" tot periodi LAI = " + TotPeriodiLAI);

				// PeriodoLibAnticipataModel[] periodo = new PeriodoLibAnticipataModel[TotPeriodi];
				PeriodoLibAnticipataModel[] periodoLA = new PeriodoLibAnticipataModel[TotPeriodiLA];
				PeriodoLibAnticipataModel[] periodoLAS = new PeriodoLibAnticipataModel[TotPeriodiLAS];
				PeriodoLibAnticipataModel[] periodoLAI = new PeriodoLibAnticipataModel[TotPeriodiLAI];

				// ====================================================================
				// Preparo i periodi per ogni tipo di L.A.: vengono ricopiati sul
				// provvedimento di ridimensionamento i periodi concessi che vanno
				// ridimensionati. Sono gli stessi.
				// n.b. vengono ricopiati solo se soggetti a ridimensonamento
				// ====================================================================
				// L.A. "normale"
				if (TotPeriodiLA > 0 && lPeriodiLicenzaLA != null) {
					Iterator ite2 = llicenze.iterator();
					int i = 0;
					while (ite2.hasNext()) {
						LicenzaPeriodiLibAnticipataModel Lmod = (LicenzaPeriodiLibAnticipataModel) ite2
								.next();
						if (Lmod.getLicenza().getDescrStatoPermesso() != null) {
							if (Lmod.getLicenza().getDescrStatoPermesso().equals("LA") && ((Lmod.getLicenza()
									.getFlagConcesso().equals("C")
									&& tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
									|| (Lmod.getLicenza().getFlagConcesso().equals("S") && tipoCumputoLA
											.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)))) {
								ArrayList lperiodi = lCtrlLib.ExRicercaPeriodiByIdLA(
										Lmod.getLicenza().getIdLicenzaLibanticipata());
								for (int k = 0; k < lperiodi.size(); k++) {
									periodoLA[i] = (PeriodoLibAnticipataModel) lperiodi.get(k);
									i++;
									// periodo = (PeriodoLibAnticipataModel[])lperiodi.get(k);
									// lPeriodiLicenzaLA.setPeriodi(periodo);
								}
							}
						} else {
							if (Lmod.getLicenza().getFlagConcesso().equals("C")) {
								ArrayList lperiodi = lCtrlLib.ExRicercaPeriodiByIdLA(
										Lmod.getLicenza().getIdLicenzaLibanticipata());
								for (int k = 0; k < lperiodi.size(); k++) {
									periodoLA[i] = (PeriodoLibAnticipataModel) lperiodi.get(k);
									i++;
									// periodo = (PeriodoLibAnticipataModel[])lperiodi.get(k);
									// lPeriodiLicenzaLA.setPeriodi(periodo);
								}
							}
						}
					}

					lPeriodiLicenzaLA.setPeriodi(periodoLA);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ----> Totale Periodi L.A. tovati da insertire  ----> "
							+ lPeriodiLicenzaLA.getPeriodi().length);

				} // Chiudo if(TotPeriodiLA > 0)

				// L.A. SPECIALE
				if (TotPeriodiLAS > 0 && lPeriodiLicenzaLASPE != null) {
					Iterator ite3 = llicenze.iterator();
					int i = 0;
					while (ite3.hasNext()) {
						LicenzaPeriodiLibAnticipataModel Lmod = (LicenzaPeriodiLibAnticipataModel) ite3
								.next();
						if (Lmod.getLicenza().getDescrStatoPermesso() != null) {
							if (Lmod.getLicenza().getDescrStatoPermesso().equals("LS") && ((Lmod.getLicenza()
									.getFlagConcesso().equals("C")
									&& tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
									|| (Lmod.getLicenza().getFlagConcesso().equals("S") && tipoCumputoLA
											.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)))) {
								ArrayList lperiodi = lCtrlLib.ExRicercaPeriodiByIdLA(
										Lmod.getLicenza().getIdLicenzaLibanticipata());
								for (int k = 0; k < lperiodi.size(); k++) {
									periodoLAS[i] = (PeriodoLibAnticipataModel) lperiodi.get(k);
									i++;
									// periodo = (PeriodoLibAnticipataModel[])lperiodi.get(k);
									// lPeriodiLicenzaLA.setPeriodi(periodo);
								}
							}
						}
					}

					lPeriodiLicenzaLASPE.setPeriodi(periodoLAS);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ----> Totale Periodi L.A. SPEC tovati da insertire  ----> "
							+ lPeriodiLicenzaLASPE.getPeriodi().length);

				} // Chiudo if(TotPeriodiLAS > 0)

				// L.A. INTEGRAZIONE
				if (TotPeriodiLAI > 0 && lPeriodiLicenzaLAINT != null) {
					Iterator ite4 = llicenze.iterator();
					int i = 0;
					while (ite4.hasNext()) {
						LicenzaPeriodiLibAnticipataModel Lmod = (LicenzaPeriodiLibAnticipataModel) ite4
								.next();
						if (Lmod.getLicenza().getDescrStatoPermesso() != null) {
							if (Lmod.getLicenza().getDescrStatoPermesso().equals("LI") && ((Lmod.getLicenza()
									.getFlagConcesso().equals("C")
									&& tipoCumputoLA.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_RIDIM))
									|| (Lmod.getLicenza().getFlagConcesso().equals("S") && tipoCumputoLA
											.equals(ICostantiLibertaAnticipata.TIPO_COMPUTO_LA_REVOCA)))) {
								ArrayList lperiodi = lCtrlLib.ExRicercaPeriodiByIdLA(
										Lmod.getLicenza().getIdLicenzaLibanticipata());
								for (int k = 0; k < lperiodi.size(); k++) {
									periodoLAI[i] = (PeriodoLibAnticipataModel) lperiodi.get(k);
									i++;
									// periodo = (PeriodoLibAnticipataModel[])lperiodi.get(k);
									// lPeriodiLicenzaLA.setPeriodi(periodo);
								}
							}
						}
					}

					lPeriodiLicenzaLAINT.setPeriodi(periodoLAI);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
					// LogF3B.getLogger()
					siesLogger.debug(" ----> Totale Periodi L.A. INTEGRAZ tovati da insertire  ----> "
							+ lPeriodiLicenzaLAINT.getPeriodi().length);

				} // Chiudo if(TotPeriodiLAS > 0)

				// Inserisce il solo provvedimento SIEP di Ridimensionamento con
				// le LA e relativi periodi (sono gli stessi della concessione)
				lIdEventoSIEPInserito = lCtrlLib.ExInserisciRidimLibanticipata(lEveProvvedimentoMod,
						lCampoNota, lPeriodiLicenzaLA, lPeriodiLicenzaLASPE, lPeriodiLicenzaLAINT);

			} // Chiude if (!lIdSorveglianzaLa.equals(""))

		} // chiude else

		// 20/05/2013 Nuova L.A. - DL 146/2013
		// BigDecimal lIdEvento= lPeriodiLicenza.getLicenza().getEveIdEvento();
		// BigDecimal lIdEvento = lPeriodiLicenzaRet.getLicenza().getEveIdEvento();

		String lPage = "";
		// lPage = IWebConstants.ROOT_DIR +"/files/siap/siep/calcolopena/DettaglioRidetPenaRidimLA.jsp";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.calcolopena.action.ActDettaglioRidimensionamentoLA&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lIdEventoSIEPInserito.toString();
		return lPage;
	}

}