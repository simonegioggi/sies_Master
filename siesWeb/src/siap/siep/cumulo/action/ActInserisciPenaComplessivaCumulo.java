package siap.siep.cumulo.action;

import org.apache.log4j.Logger;
/**
 * <p>Title: ActInserisciPenaComplessivaCumulo</p>
 * <p>Description: Classe Action per l'inserimento di Cumulo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.cumulo.controller.ICumulo;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacumulo.action.ICostantiPenaCumulo;
import siap.siep.penacumulo.model.PenaCumuloModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciPenaComplessivaCumulo extends ActionSiap implements ICostantiCumulo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	// private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	/**
	 * Azione di Inserimento dei Dati finali del Cumulo (pena cumulo). Viene invocata due volte: - la prima in
	 * fase di inserimento dei dati del Cumulo - la seconda dopo la modifica della posizione giuridica
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione dipende dalla chiamata: -
	 *         pagina di cambio posizione giuridica - pagina di calcolo della pena
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMOd = (FascicoloSiepModel) (getSessionAttribute("fascicolo"));
		BigDecimal lFascID = lFascMOd.getIdFascicoloSiep();

		// ========================================================================
		// Se provengo dalla finestra di inserimento dei dati della Pena Cumulo
		// (cumulowiz assente), devo inserire:
		// - CUMULO
		// - PENA_CUMULO
		// - LIBERAZIONE ANTICIPATA
		// ========================================================================
		if (isSessionAttributeNullObj("cumulowiz") || getSessionAttribute("cumulowiz").equals("")) {
			// CUMULO
			CumuloModel lCumMod = new CumuloModel();
			lCumMod.setDataCumulo(getRequestDateParameter("Yprov", "Mprov", "Dprov"));
			lCumMod.setFasSieIdFascicoloSiep(lFascID);
			lCumMod.setSenIdSentenza(lFascMOd.getSenIdSentenza()); // ??? perchè quella del cumulante?
			lCumMod.setFlagTipoStampa("-");

			lCumMod.setCodUfficioAggiornamento(getUfficioUtenteConnesso().getCodUfficio());
			lCumMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lCumMod.setDataAggiornamento(DateUtils.getSysDate());

			// PENA CUMULO SI INSERISCE SEMPRE
			PenaCumuloModel lPenCumMod = new PenaCumuloModel();

			if (!isRequestParameterNullObj("ArrAnni"))
				lPenCumMod.setNumAnniArresto(getRequestBigDecimalParameter("ArrAnni"));

			if (!isRequestParameterNullObj("ArrMesi"))
				lPenCumMod.setNumMesiArresto(getRequestBigDecimalParameter("ArrMesi"));

			if (!isRequestParameterNullObj("ArrGiorni"))
				lPenCumMod.setNumGiorniArresto(getRequestBigDecimalParameter("ArrGiorni"));

			if (!isRequestParameterNullObj("RecAnni"))
				lPenCumMod.setNumAnniReclusione(getRequestBigDecimalParameter("RecAnni"));

			if (!isRequestParameterNullObj("RecMesi"))
				lPenCumMod.setNumMesiReclusione(getRequestBigDecimalParameter("RecMesi"));

			if (!isRequestParameterNullObj("RecGiorni"))
				lPenCumMod.setNumGiorniReclusione(getRequestBigDecimalParameter("RecGiorni"));

			String multa = getRequestStringParameter("MultaInt");
			String multa_dec = getRequestStringParameter("MultaDec");

			if (!multa.equals("")) {
				if (!multa_dec.equals(""))
					lPenCumMod.setImportoMulta(new BigDecimal(multa + "." + multa_dec));
				else
					lPenCumMod.setImportoMulta(new BigDecimal(multa));
			} else if (!multa_dec.equals(""))
				lPenCumMod.setImportoMulta(new BigDecimal("0." + multa_dec));

			String ammenda = getRequestStringParameter("AmmendaInt");
			String ammenda_dec = getRequestStringParameter("AmmendaDec");
			if (!ammenda.equals("")) {
				if (!ammenda_dec.equals(""))
					lPenCumMod.setImportoAmmenda(new BigDecimal(ammenda + "." + ammenda_dec));
				else
					lPenCumMod.setImportoAmmenda(new BigDecimal(ammenda));
			} else if (!multa_dec.equals(""))
				lPenCumMod.setImportoAmmenda(new BigDecimal("0." + ammenda_dec));

			if (!(getRequestStringParameter("FlagErgastolo").equals("-"))) {
				if (getRequestStringParameter("FlagErgastolo").equals("E"))
					lPenCumMod.setFlagErgastolo("S");
				else if (getRequestStringParameter("FlagErgastolo").equals("I"))
					lPenCumMod.setFlagErgastolo("D");

				if (!isRequestParameterNullObj("IsDiuGiorni"))
					lPenCumMod.setNumGiorniIsolamentoDiurno(getRequestBigDecimalParameter("IsDiuGiorni"));

				if (!isRequestParameterNullObj("IsDiuMesi"))
					lPenCumMod.setNumMesiIsolamentoDiurno(getRequestBigDecimalParameter("IsDiuMesi"));

				if (!isRequestParameterNullObj("IsDiuAnni"))
					lPenCumMod.setNumAnniIsolamentoDiurno(getRequestBigDecimalParameter("IsDiuAnni"));
			} else {
				lPenCumMod.setFlagErgastolo("N");
			}

			lPenCumMod.setCodTipoPenaDetentiva(getRequestStringParameter("FlagErgastolo"));

			// if (!isRequestParameterNullObj("idpenacumulo"))
			// lPenCumMod.setCumIdCumulo(getRequestBigDecimalParameter("idpenacumulo"));

			lPenCumMod.setDataDecorrenzaPena(
					getRequestDateParameter(ICostantiPenaCumulo.CAMPO_ANNO_DATA_DECORRENZA_PENA,
							ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA,
							ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA));
			lPenCumMod.setMotivazioni(getRequestStringParameter(ICostantiPenaCumulo.CAMPO_MOTIVAZIONI));
			lPenCumMod.setEstremiOrdinanza(
					getRequestStringParameter(ICostantiPenaCumulo.CAMPO_ESTREMI_ORDINANZA));

			lPenCumMod.setNumAnniArrestoSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_ANNI_ARRESTO_SOSP));
			lPenCumMod.setNumGiorniArrestoSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_GIORNI_ARRESTO_SOSP));
			lPenCumMod.setNumMesiArrestoSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_MESI_ARRESTO_SOSP));
			lPenCumMod.setNumAnniReclusioneSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_ANNI_RECLUSIONE_SOSP));
			lPenCumMod.setNumGiorniReclusioneSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_SOSP));
			lPenCumMod.setNumMesiReclusioneSosp(
					getRequestBigDecimalParameter(ICostantiPenaCumulo.CAMPO_NUM_MESI_RECLUSIONE_SOSP));

			/*
			 * lPenCumMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());
			 * lPenCumMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			 * lPenCumMod.setDataInserimento(DateUtils.getSysDate());
			 */

			// AGGIUNTI DOPO
			// lPenCumMod.setMisuraSicurezza(getRequestStringParameter(ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA));
			// lPenCumMod.setPenaAccessoria(getRequestStringParameter(ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA));

			lPenCumMod.setMisuraSicurezza(null);
			lPenCumMod.setPenaAccessoria(null);

			/*
			 * --------------------------------------------------------------------------------- LIBERAZIONE
			 * ANTICIPATA ---------------------------------------------------------------------------------
			 */
			// 20/05/2014 - Nuova L.A. - decreto 2013/146 - Liberazione Anticipata diventa:
			// Liberazione Anticipata (L.A.), L.A. Speciale , Integrazione L.A.

			boolean lEsisteGiorniLib = false;

			BigDecimal ggLibAnt = new BigDecimal(0);
			if (getRequestStringParameter(ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA) != null
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA) != ""
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA) != "0") {
				lEsisteGiorniLib = true;
				// if (!isRequestParameterNullObj("LibAntGiorni"))
				ggLibAnt = getRequestBigDecimalParameter(
						ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA);
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("LIBERAZIONE ANTICIPATA - ggLibAnt = "+ggLibAnt.intValue());
			BigDecimal ggLibAntSpe = new BigDecimal(0);
			if (getRequestStringParameter(
					ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE) != null
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE) != ""
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE) != "0") {
				lEsisteGiorniLib = true;
				ggLibAntSpe = getRequestBigDecimalParameter(
						ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE);
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("LIBERAZIONE ANTICIPATA - ggLibAntSpe = "+ggLibAntSpe.intValue());
			BigDecimal ggLibAntInt = new BigDecimal(0);
			if (getRequestStringParameter(
					ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT) != null
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT) != ""
					&& getRequestStringParameter(
							ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT) != "0") {
				lEsisteGiorniLib = true;
				ggLibAntInt = getRequestBigDecimalParameter(
						ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT);
			}
			// // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			// siesLogger.debug("LIBERAZIONE ANTICIPATA - ggLibAntInt = "+ggLibAntInt.intValue());

			//
			BigDecimal ggRisarcimentoDanni = new BigDecimal(0);
			if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92)
					&& getRequestStringParameter(
							ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92).length() > 0) {
				ggRisarcimentoDanni = getRequestBigDecimalParameter(
						ICostantiPenaResidua.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92);
			}

			// In PENA_CUMULO viene cmq anche gestito il Tot. delle 3 L.A., che viene considerato per il
			// calcolo pena
			int ggTOT = 0;
			ggTOT = ggLibAnt.intValue() + ggLibAntSpe.intValue() + ggLibAntInt.intValue();// +ggRisarcimentoDanni.intValue();
			BigDecimal ggLibAntTOT = new BigDecimal(ggTOT);

			LicenzaLibAnticipataModel libAntMod = new LicenzaLibAnticipataModel();
			libAntMod.setFasSieIdFascicoloSiep(lFascID);
			libAntMod.setFlagConcesso("C");
			libAntMod.setFlagElaborato("N");
			libAntMod.setCodTipoLicenza("LA");

			libAntMod.setNumeroGiorni(ggLibAntTOT);

			// ========================================================================
			// ========================================================================
			// lPenCumMod.setNumGiorniLibAnticipata(ggLibAnt);
			lPenCumMod.setNumGiorniLibAnticipata(ggLibAntTOT);
			lPenCumMod.setNumGiorniLibAnticipataLA(ggLibAnt);
			lPenCumMod.setNumGiorniLibAnticipataSPE(ggLibAntSpe);
			lPenCumMod.setNumGiorniLibAnticipataINT(ggLibAntInt);
			lPenCumMod.setNumGiorniRiduzionePena(ggRisarcimentoDanni);

			libAntMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			libAntMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			libAntMod.setDataInserimento(DateUtils.getSysDate());

			// End Nuova L.A. -

			// ==================================
			// Inserimento i dati
			// ==================================
			PenaCumuloModel lPenaCumulo = new PenaCumuloModel();
			ICumulo iCum = SIEPLookupRemote.getCumuloRemote();
			lPenaCumulo = iCum.ExInserisciCumuloPenaCumuloLibAnt(lPenCumMod, lCumMod, libAntMod,
					lEsisteGiorniLib);

			setRequestAttribute("penacumulo", lPenaCumulo);

			PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
			IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);
			if (lPG == null)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");

			setRequestAttribute("PosizioneGiuridica", lPG);

			// Il flag cumulowiz serve alla ActInserisciPosizioneGiuridica per effettuare
			// la redirect a questa pagina
			setSessionAttribute("cumulowiz", "PG");
			return REDIRECT_CAMBIO_POS_GIURIDICA;

		} // Fine Step 1..

		// ==========================================================================
		// Chiamata proveniente da aggiornamento Posizione Giuridica, richiamo la
		// pagina per il calcolo della pena
		// ==========================================================================
		if (getSessionAttribute("cumulowiz").equals("PG")) {
			PosizioneGiuridicaModel lPG = new PosizioneGiuridicaModel();
			IPosizioneGiuridica iPG = SIEPLookupRemote.getPosizioneGiuridicaRemote();
			lPG = iPG.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascID);
			if (lPG == null)
				throw new SIEPException(SIEPException.USER_MESSAGE, "Posizione Giuridica Inesistente");

			Collection lTipologia = DecodificheManager.getInstance().getStampeCumulo();
			setRequestAttribute("tipologia", lTipologia);

			setRequestAttribute("PosizioneGiuridica", lPG);
			setSessionAttribute("cumulowiz", ""); // ripulisco il campo
			return REDIRECT_CALCOLO_PENA;
		}
		return "";
	}
}

