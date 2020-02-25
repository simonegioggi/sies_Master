package siap.siep.misuracautelare.action;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

public class ActInserisciMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	/**
	 * Azione di Inserimento del MisuraCautelare
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws F3BException {

		CalendarModel lCalMod = new CalendarModel();

		MisuraCautelareModel lMisMod = null;

		BigDecimal lIdFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"))
				.getIdFascicoloSiep();

		String lUtenteNome = getCodUtenteConnesso();
		String lUtenteUfficio = getCodUfficioUtenteConnesso();

		// ==========================================================================
		// n.b. Sulla form sono presenti più misure con campi dello stesso nome.
		// Carico i campi negli array
		// ==========================================================================

		// Espiazione pena in istituto di detenzione = istitutoDetenzione
		// Espiazione pena in altro luogo = altroLuogo
		String EspiazionePenaIstDeteAltroLuogo = null;
		if (!isRequestParameterNullObj(ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO)) {
			EspiazionePenaIstDeteAltroLuogo = this
					.getRequestStringParameter(ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO);
		}

		String campoMisura = null;
		String istitutoDetenzione = "";
		if (EspiazionePenaIstDeteAltroLuogo != null
				&& EspiazionePenaIstDeteAltroLuogo.equalsIgnoreCase("istitutoDetenzione")) {
			// Espiazione pena in istituto di detenzione
			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_MISURA_DETENTIVA)) {
				campoMisura = getRequestStringParameter(CAMPO_COD_TIPO_MISURA_DETENTIVA);
			}
			// Sezione E
			if (!isRequestParameterNullObj(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
					&& !CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE.equalsIgnoreCase(this
							.getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE))) {
				istitutoDetenzione = getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);// non
																											// computabile
			}
		} else if (EspiazionePenaIstDeteAltroLuogo != null
				&& EspiazionePenaIstDeteAltroLuogo.equalsIgnoreCase("altroLuogo")) {
			// Espiazione pena in altro luogo
			if (!isRequestParameterNullObj(CAMPO_COD_TIPO_MISURA_NON_DETENTIVA)) {
				campoMisura = getRequestStringParameter(CAMPO_COD_TIPO_MISURA_NON_DETENTIVA);
			}
		}

		String autoritaEmittente = null;
		if (!isRequestParameterNullObj(AUTORITA_EMITTENTE)) {
			autoritaEmittente = getRequestStringParameter(AUTORITA_EMITTENTE);
		}

		// controllo se esiste il Luogo Emittente in funzione dell'Autorità Emittente
		/*String lCodice = */getCodUfficioByCodTipoUfficioDescrComune(
				getRequestStringParameter(ICostantiMisuraCautelare.AUTORITA_EMITTENTE),
				getRequestStringParameter(ICostantiMisuraCautelare.AUTORITA_EMITTENTE_LUOGO));

		String lCodiceUffPM = "";
		// Controllo esistenza Sede dell'ufficio per il "Tipo Ufficio PM"
		if (!isRequestParameterNullObj(ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE)
				&& getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE) != null
				&& !getRequestStringParameter(
						ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE).equals("-")
				&& !isRequestParameterNullObj(ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM)
				&& getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM) != null) {

			lCodiceUffPM = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE),
					getRequestStringParameter(ICostantiMisuraCautelare.CAMPO_COD_SEDE_UFFICIO_PM));
		}

		// String autoritaCompPerTerritorio = null;
		// if (!isRequestParameterNullObj(AUTORITA_COMPETENTE_PER_TERRITORIO)){
		// autoritaCompPerTerritorio = getRequestStringParameter(AUTORITA_COMPETENTE_PER_TERRITORIO);
		// }

		// String[] lArrayMisura = getRequestStringParameters(CAMPO_COD_TIPO_MISURA);

		// Dal
		String giornoI = getRequestStringParameter(CAMPO_GIORNO_DATA_INIZIO);
		String meseI = getRequestStringParameter(CAMPO_MESE_DATA_INIZIO);
		String annoI = getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO);

		// Al
		String giornoF = getRequestStringParameter(CAMPO_GIORNO_DATA_FINE);
		String meseF = getRequestStringParameter(CAMPO_MESE_DATA_FINE);
		String annoF = getRequestStringParameter(CAMPO_ANNO_DATA_FINE);

		String altroLuogoEspiazioneDetenzione = "";
		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_DI_ESPIAZIONE)) {
			altroLuogoEspiazioneDetenzione = getRequestStringParameter(CAMPO_COD_LUOGO_DI_ESPIAZIONE);
		}

		String giornoEmissioneOrdinanza = this
				.getRequestStringParameter(CAMPO_GIORNO_DATA_EMISSIONE_ORDINANZA);
		String meseEmissioneOrdinanza = getRequestStringParameter(CAMPO_MESE_DATA_EMISSIONE_ORDINANZA);
		String annoEmissioneOrdinanza = getRequestStringParameter(CAMPO_ANNO_DATA_EMISSIONE_ORDINANZA);

		// Non Computabili
		String motivo = "-";
		if (!isRequestParameterNullObj(CAMPO_COD_MOTIVO_NON_COMPUTABILE)) {
			motivo = getRequestStringParameter(CAMPO_COD_MOTIVO_NON_COMPUTABILE);
		}

		String numRifer = null;
		if (!isRequestParameterNullObj(NUMERO_SIEP)) {
			numRifer = getRequestStringParameter(NUMERO_SIEP);
		}

		String ufficio = "-";
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_UFFICIO_RIFER)) {
			ufficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_RIFER);
		}

		// Sede Sezione G
		String sedeProvvedimentoFungibilita = "";
		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_UFFICIO_RIFER)) {
			sedeProvvedimentoFungibilita = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_RIFER);
		} else {
			sedeProvvedimentoFungibilita = "-";
		}

		String autoritaEmittenteLuogo = null;
		if (!isRequestParameterNullObj(AUTORITA_EMITTENTE_LUOGO)) {
			autoritaEmittenteLuogo = getRequestStringParameter(AUTORITA_EMITTENTE_LUOGO);
		}

		String giornoFung = null;
		if (!isRequestParameterNullObj(CAMPO_GIORNO_DATA_FUNGIBILITA)) {
			giornoFung = getRequestStringParameter(CAMPO_GIORNO_DATA_FUNGIBILITA);
		}

		String meseFung = null;
		if (!isRequestParameterNullObj(CAMPO_MESE_DATA_FUNGIBILITA)) {
			meseFung = getRequestStringParameter(CAMPO_MESE_DATA_FUNGIBILITA);
		}

		String annoFung = null;
		if (!isRequestParameterNullObj(CAMPO_ANNO_DATA_FUNGIBILITA)) {
			annoFung = getRequestStringParameter(CAMPO_ANNO_DATA_FUNGIBILITA);
		}

		String note = null;
		if (!isRequestParameterNullObj(CAMPO_NOTE)) {
			note = getRequestStringParameter(CAMPO_NOTE);
		}

		BigDecimal annoFascBdmc = null;
		if (!isRequestParameterNullObj(ANNO_FASC_BDMC)) {
			annoFascBdmc = getRequestBigDecimalParameter(ANNO_FASC_BDMC);
		}

		BigDecimal numeFascBdmc = null;
		if (!isRequestParameterNullObj(NUME_FASC_BDMC)) {
			numeFascBdmc = getRequestBigDecimalParameter(NUME_FASC_BDMC);
		}

		// String codiceUfficioPmSede = null;
		// if (!isRequestParameterNullObj(CAMPO_COD_SEDE_UFFICIO_PM)){
		// codiceUfficioPmSede = getRequestStringParameter(CAMPO_COD_SEDE_UFFICIO_PM);//giusto
		// }
		String codiceUfficioPmSede = lCodiceUffPM;

		String codiceUfficioTerritorioSede = null;
		if (!isRequestParameterNullObj(CAMPO_COD_SEDE_UFFICIO_PER_TERRITORIO)) {// giusto
			codiceUfficioTerritorioSede = this
					.getRequestStringParameter(CAMPO_COD_SEDE_UFFICIO_PER_TERRITORIO);
		}

		BigDecimal annoRgnr = null;
		if (!isRequestParameterNullObj(ANNO_RGNR)) {
			annoRgnr = getRequestBigDecimalParameter(ANNO_RGNR);
		}

		BigDecimal numeroRgnr = null;
		if (!isRequestParameterNullObj(NUMERO_RGNR)) {
			numeroRgnr = getRequestBigDecimalParameter(NUMERO_RGNR);
		}

		BigDecimal annoRegGen = null;
		if (!isRequestParameterNullObj(ANNO_REG_GEN)) {
			annoRegGen = getRequestBigDecimalParameter(ANNO_REG_GEN);
		}

		BigDecimal numeroRegGen = null;
		if (!isRequestParameterNullObj(NUMERO_REG_GEN)) {
			numeroRegGen = getRequestBigDecimalParameter(NUMERO_REG_GEN);
		}

		String tipoUfficioRegGen = null;
		if (!isRequestParameterNullObj(TIPO_UFFICIO_REG_GEN)) {
			tipoUfficioRegGen = getRequestStringParameter(TIPO_UFFICIO_REG_GEN);
		}

		String autoritaCompetente = null;
		if (!isRequestParameterNullObj(AUTORITA_COMPETENTE)) {
			autoritaCompetente = getRequestStringParameter(AUTORITA_COMPETENTE);
		}

//		String autoritaCompetenteSede = null;
//		if (!isRequestParameterNullObj(AUTORITA_COMPETENTE_SEDE)) {// giusto
//			autoritaCompetenteSede = getRequestStringParameter(AUTORITA_COMPETENTE_SEDE);
//		}

		String autoritaCompetenteIndirizzo = null;
		if (!isRequestParameterNullObj(AUTORITA_COMPETENTE_INDIRIZZO)) {
			autoritaCompetenteIndirizzo = getRequestStringParameter(AUTORITA_COMPETENTE_INDIRIZZO);
		}

//		BigDecimal annoRiferNonComp = null;
//		if (!isRequestParameterNullObj(ANNO_RIFER)) {
//			annoRiferNonComp = getRequestBigDecimalParameter(ANNO_RIFER);
//		}

		BigDecimal annoRifer = null;
		if (!isRequestParameterNullObj(ANNO_SIEP)) {
			annoRifer = getRequestBigDecimalParameter(ANNO_SIEP);
		}

//		String codTipoUfficioMisuraCautelare = null;
//		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE)) {
//			codTipoUfficioMisuraCautelare = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_MISURA_CAUTELARE);
//		}

		ComuneModel lComMod = null;
		// PosizioneGiuridicaModel lPosMod = null;
		Vector lVectMod = new Vector();

//		String lTipoMis = "";

		lMisMod = new MisuraCautelareModel();

		lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		lMisMod.setCodTipoMisura(campoMisura);

		lMisMod.setDataEmissioneOrdinanza(DateUtils.getDate(annoEmissioneOrdinanza, meseEmissioneOrdinanza,
				giornoEmissioneOrdinanza));

		lMisMod.setDataInizio(DateUtils.getDate(annoI, meseI, giornoI));
		lMisMod.setDataFine(DateUtils.getDate(annoF, meseF, giornoF));

		lMisMod.setIstDetIdIstitutoDetenzione(istitutoDetenzione);

		lMisMod.setAltroLuogoDetenzione(altroLuogoEspiazioneDetenzione);

		lMisMod.setCodTipoUfficioRifer("-");
		lMisMod.setCodLuogoUfficioRifer("-");
		lMisMod.setCodMotivoNonComputabile("-");

		lMisMod.setCodOperatoreInserimento(lUtenteNome);
		lMisMod.setCodUfficioInserimento(lUtenteUfficio);
		lMisMod.setDataInserimento(DateUtils.getSysDate());

		lCalMod.setDataInizio(lMisMod.getDataInizio());
		lCalMod.setDataFine(lMisMod.getDataFine());

		CalendarUtil lCalUtil = new CalendarUtil();
		lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
		lCalMod = lCalUtil.ricalcolaGAM(lCalMod);

		lMisMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
		lMisMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
		lMisMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));

		// inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
		if (campoMisura.equalsIgnoreCase("CL")) {
			int giorni = lCalMod.getNumGiorni();
			int mesi = lCalMod.getNumMesi();
			int anni = lCalMod.getNumAnni();

			int numGiorni = anni * 360 + mesi * 30 + giorni;
			int numTotGiorni = numGiorni;
			// numGiorni = numGiorni/3;
			// numGiorni = 151;
			// int periodiGiorniUnoTre = 2;
			BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
			BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
			BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
			BigDecimal numDiff = new BigDecimal(0);
			BigDecimal uno = new BigDecimal(1);
			numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1,
					RoundingMode.HALF_UP);
			// int num = tot.intValue();
			BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(
					numGiorniAnniMesiGiorniDivTre.intValue()).negate();
			// numDiff = numGiorniAnniMesiGiorniDivTre;
			numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);

			// Se il decimale è >= 6 allora arrotondamento per eccesso.
			// Se il decimale è <= 5 allora arrotondamento per difetto.
			BigDecimal zero5 = new BigDecimal("0.5");
			if (numDiff.compareTo(zero5) == 0 || numDiff.compareTo(zero5) == -1) {
				// Se il decimale è <= 5 allora arrotondamento per difetto.
				numGiorniAnniMesiGiorniDivTre = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue());
				// numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
			} else {
				numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
			}

			numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
			mesi = 0;
			anni = 0;
			// I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM
			if (numGiorni > 30) {
				int tmp = numGiorni / 30;
				mesi += tmp;
				numGiorni -= tmp * 30;
			}
			if (numGiorni == 30) {
				mesi++;
				numGiorni = 0;
			}
			if (mesi > 12) {
				int tmp = mesi / 12;
				anni += tmp;
				mesi -= tmp * 12;
			}

			if (mesi == 12) {
				anni++;
				mesi = 0;
			}
			lMisMod.setNumAnni(new BigDecimal(anni));
			lMisMod.setNumMesi(new BigDecimal(mesi));
			lMisMod.setNumGiorni(new BigDecimal(numGiorni));
			lMisMod.setGiorni(new BigDecimal(numTotGiorni));
		}
		// fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova

		// inizio non computabile
		// Misura Cautelare Cessate al momento del passaggio in giudicato computabili "S"
		// Misura Cautelare Cessate al momento del passaggio in giudicato non computabili "N"
		String flagComputabile = getRequestStringParameter("flagComputabile");
		lMisMod.setFlagComputabile(flagComputabile);
		lMisMod.setCodMotivoNonComputabile(motivo);
		lMisMod.setNumRifer(numRifer);
		lMisMod.setCodTipoUfficioRifer(ufficio);

		if (autoritaEmittenteLuogo != null && !"".equalsIgnoreCase(autoritaEmittenteLuogo)
				&& !AUTORITA_EMITTENTE_LUOGO.equalsIgnoreCase(autoritaEmittenteLuogo)) {
			lComMod = new ComuneModel(getCodComuneByDescr(autoritaEmittenteLuogo));
			lMisMod.setAutoritaEmittenteLuogo(lComMod.getCodComune());
		} else {
			lMisMod.setAutoritaEmittenteLuogo("");
		}

		// if(codiceUfficioPmSede!=null && !"".equalsIgnoreCase(codiceUfficioPmSede)){
		// lComMod = new ComuneModel(getCodComuneByDescr(codiceUfficioPmSede));
		// lMisMod.setCodiceUfficioPmSede(lComMod.getCodComune());
		// }else{
		// lMisMod.setCodiceUfficioPmSede("");
		// }

		lMisMod.setCodiceUfficioPmSede(codiceUfficioPmSede);

		if (codiceUfficioTerritorioSede != null && !"".equalsIgnoreCase(codiceUfficioTerritorioSede)) {
			lComMod = new ComuneModel(getCodComuneByDescr(codiceUfficioTerritorioSede));
			lMisMod.setAutoritaCompetenteSede(lComMod.getCodComune());
		} else {
			lMisMod.setAutoritaCompetenteSede("");
		}

		if (flagComputabile != null && flagComputabile.equalsIgnoreCase("N")) {
			if (sedeProvvedimentoFungibilita != null && !"".equalsIgnoreCase(sedeProvvedimentoFungibilita)) {
				lComMod = new ComuneModel(getCodComuneByDescr(sedeProvvedimentoFungibilita));
				lMisMod.setCodLuogoUfficioRifer(lComMod.getCodComune());
			} else {
				lMisMod.setCodLuogoUfficioRifer("-");
			}
		}

		lMisMod.setDataFungibilita(DateUtils.getDate(annoFung, meseFung, giornoFung));
		lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lMisMod.setNote(note);

		lMisMod.setAnnoFascBdmc(annoFascBdmc);
		lMisMod.setNumeFascBdmc(numeFascBdmc);
		lMisMod.setAnnoRgnr(annoRgnr);
		lMisMod.setNumeroRgnr(numeroRgnr);
		lMisMod.setAnnoRegGen(annoRegGen);
		lMisMod.setNumeroRegGen(numeroRegGen);
		lMisMod.setAnnoRifer(annoRifer);
		lMisMod.setTipoUfficioRegGen(tipoUfficioRegGen);
		lMisMod.setAutoritaEmittente(autoritaEmittente);
		lMisMod.setAutoritaCompetente(autoritaCompetente);
		lMisMod.setAutoritaCompetenteIndirizzo(autoritaCompetenteIndirizzo);
		// fine non computabile
		lVectMod.add(lMisMod);

		// =========================================
		// Riempio il model di posizione giuridica
		// =========================================
		// lPosMod = new PosizioneGiuridicaModel();
		//
		// if (lMisMod.getCodTipoMisura().equals("CA"))
		// {
		// lPosMod.setCodPosizioneGiuridica("01"); // Custodia Cautelare per Questa Causa in Regime di
		// Detenzione
		// }
		//
		// if (lMisMod.getCodTipoMisura().equals("AD"))
		// {
		// lPosMod.setCodPosizioneGiuridica("02"); // Custodia Cautelare per Questa Causa in Regime di Arresti
		// Domiciliari
		// }
		//
		//
		// lPosMod.setDataInizio(lMisMod.getDataInizio());
		// lPosMod.setFasSieIdFascicoloSiep(lMisMod.getFasSieIdFascicoloSiep());
		// lPosMod.setCodPosizioneProcessuale("-");
		//
		// lPosMod.setCodOperatoreInserimento(lMisMod.getCodOperatoreInserimento());
		// lPosMod.setCodUfficioInserimento(lMisMod.getCodUfficioInserimento());
		// lPosMod.setDataInserimento(DateUtils.getSysDate());

		// **********************presofferto***************
		// lCalMod.setDataInizio(lMisMod.getDataInizio());
		// lCalMod.setDataFine(lMisMod.getDataFine());
		//
		// CalendarUtil lCalUtil = new CalendarUtil();
		// lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
		// lCalMod = lCalUtil.ricalcolaGAM(lCalMod);
		//
		// lMisMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
		// lMisMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
		// lMisMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));
		// *************************************************************

		// lMisMod.setCodOperatoreInserimento(lUtenteNome);
		// lMisMod.setCodUfficioInserimento(lUtenteUfficio);
		// lMisMod.setDataInserimento(DateUtils.getSysDate());

		// if(lArrayIstitutoDetenzione!=null && lArrayIstitutoDetenzione.length>0 &&
		// !"".equalsIgnoreCase(lArrayIstitutoDetenzione[lIndMisuraSecondo]))
		// lMisMod.setIstDetIdIstitutoDetenzione(lArrayIstitutoDetenzione[lIndMisuraSecondo]);
		// lMisMod.setIstDetIdIstitutoDetenzione("");

		// if (lArrayAltroLuogo!=null && lArrayAltroLuogo[lIndMisuraSecondo]!=null)
		// if(lArrayAltroLuogo!=null && lIndMisuraSecondo<lArrayAltroLuogo.length &&
		// lArrayAltroLuogo[lIndMisuraSecondo]!=null)
		// lMisMod.setAltroLuogoDetenzione(lArrayAltroLuogo[lIndMisuraSecondo].toUpperCase());

		// lMisMod.setCodTipoUfficioRifer("-");
		// lMisMod.setCodLuogoUfficioRifer("-");
		// lMisMod.setCodMotivoNonComputabile("-");
		// lMisMod.setFlagComputabile("S");

		// lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// Inserisco la Misura Cautelare

		// lVectMod.add(lMisMod);
		// }
		// conta5++;
		// lIndMisuraSecondo++;
		// }

		// ==========================================================================
		// Misure Cautelari NON COMPUTABILI
		// ==========================================================================
		// int lIndMisuraTerzo = 3;
		//
		// if (primaSezione.equals("S"))
		// {
		// lIndMisuraTerzo = 2;
		// }
		//
		// conta5 = 0;
		// while (conta5 < 2)
		// {
		// if(lIndMisuraTerzo<lArrayMisura.length) {
		// lTipoMis = lArrayMisura[lIndMisuraTerzo];
		// }
		//
		// if (lTipoMis!=null && !lTipoMis.equals("-"))
		// {
		// lMisMod = new MisuraCautelareModel();
		// if(lIndMisuraTerzo<lArrayMisura.length) {
		// lMisMod.setCodTipoMisura(lArrayMisura[lIndMisuraTerzo]);
		// }
		// if(lArrayAnnoI!=null && lIndMisuraTerzo<lArrayAnnoI.length &&
		// lArrayMeseI!=null && lIndMisuraTerzo<lArrayMeseI.length &&
		// lArrayGiornoI!=null && lIndMisuraTerzo<lArrayGiornoI.length) {
		// lMisMod.setDataInizio(DateUtils.getDate(lArrayAnnoI[lIndMisuraTerzo],
		// lArrayMeseI[lIndMisuraTerzo],
		// lArrayGiornoI[lIndMisuraTerzo]));
		// if (primaSezione.equals("S"))
		// {
		// lMisMod.setDataFine(DateUtils.getDate(lArrayAnnoF[lIndMisuraTerzo],
		// lArrayMeseF[lIndMisuraTerzo],
		// lArrayGiornoF[lIndMisuraTerzo]));
		// }
		// else
		// {
		// lMisMod.setDataFine(DateUtils.getDate(lArrayAnnoF[lIndMisuraTerzo - 1],
		// lArrayMeseF[lIndMisuraTerzo - 1],
		// lArrayGiornoF[lIndMisuraTerzo - 1]));
		// }
		// }
		// **********************presofferto***************
		// lCalMod.setDataInizio(lMisMod.getDataInizio());
		// lCalMod.setDataFine(lMisMod.getDataFine());
		// CalendarUtil lCalUtil = new CalendarUtil();
		// lCalMod = lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
		// lCalMod = lCalUtil.ricalcolaGAM(lCalMod);
		//
		// lMisMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
		// lMisMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
		// lMisMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));
		// *************************************************************
		// lMisMod.setCodOperatoreInserimento(lUtenteNome);
		// lMisMod.setCodUfficioInserimento(lUtenteUfficio);
		// lMisMod.setDataInserimento(DateUtils.getSysDate());

		// if(lArrayIstitutoDetenzione!=null && lIndMisuraTerzo<lArrayIstitutoDetenzione.length) {
		// lMisMod.setIstDetIdIstitutoDetenzione(lArrayIstitutoDetenzione[lIndMisuraTerzo]);
		// lMisMod.setAltroLuogoDetenzione(lArrayAltroLuogo[lIndMisuraTerzo].toUpperCase());
		// }

		// lMisMod.setFlagComputabile("N");
		//
		// if((lArrayMotivo!=null && (lIndMisuraTerzo-3)<lArrayMotivo.length && (lIndMisuraTerzo-3)>=0) &&
		// (lArrayNumRes!=null && (lIndMisuraTerzo-3)<lArrayNumRes.length && (lIndMisuraTerzo-3)>=0) &&
		// (lArrayUfficio!=null && (lIndMisuraTerzo-3)<lArrayUfficio.length && (lIndMisuraTerzo-3)>=0) &&
		// (lArrayLuogoUfficio!=null && (lIndMisuraTerzo-3)<lArrayLuogoUfficio.length &&
		// (lIndMisuraTerzo-3)>=0) &&
		// (lArrayAnnoFung!=null && (lIndMisuraTerzo-3)<lArrayAnnoFung.length && (lIndMisuraTerzo-3)>=0) &&
		// (lArrayMeseFung!=null && (lIndMisuraTerzo-3)<lArrayMeseFung.length && (lIndMisuraTerzo-3)>=0) &&
		// (lArrayGiornoFung!=null && (lIndMisuraTerzo-3)<lArrayGiornoFung.length && (lIndMisuraTerzo-3)>=0)
		// &&
		// (lArrayNote!=null && (lIndMisuraTerzo-3)<lArrayNote.length && (lIndMisuraTerzo-3)>=0)) {
		//
		// if (primaSezione.equals("N"))
		// {
		// lMisMod.setCodMotivoNonComputabile(lArrayMotivo[lIndMisuraTerzo - 3]);
		//
		// lMisMod.setNumRifer(lArrayNumRes[lIndMisuraTerzo - 3]);
		// lMisMod.setCodTipoUfficioRifer(lArrayUfficio[lIndMisuraTerzo - 3]);
		//
		// lComMod = new ComuneModel(getCodComuneByDescr(lArrayLuogoUfficio[lIndMisuraTerzo - 3]));
		//
		// lMisMod.setCodLuogoUfficioRifer(lComMod.getCodComune());
		//
		// lMisMod.setDataFungibilita(DateUtils.getDate(lArrayAnnoFung[lIndMisuraTerzo - 3],
		// lArrayMeseFung[lIndMisuraTerzo - 3],
		// lArrayGiornoFung[lIndMisuraTerzo - 3])
		// );
		// lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisMod.setNote(lArrayNote[lIndMisuraTerzo - 3]);
		// }
		// else
		// {
		// lMisMod.setCodMotivoNonComputabile(lArrayMotivo[lIndMisuraTerzo - 2]);
		// lMisMod.setNumRifer(lArrayNumRes[lIndMisuraTerzo - 2]);
		// lMisMod.setCodTipoUfficioRifer(lArrayUfficio[lIndMisuraTerzo - 2]);
		//
		// lComMod = new ComuneModel(getCodComuneByDescr(lArrayLuogoUfficio[lIndMisuraTerzo - 2]));
		//
		// lMisMod.setCodLuogoUfficioRifer(lComMod.getCodComune());
		//
		// lMisMod.setDataFungibilita(DateUtils.getDate(lArrayAnnoFung[lIndMisuraTerzo -2],
		// lArrayMeseFung[lIndMisuraTerzo - 2],
		// lArrayGiornoFung[lIndMisuraTerzo - 2])
		// );
		// lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisMod.setNote(lArrayNote[lIndMisuraTerzo - 2]);
		// }
		// }
		// lVectMod.add(lMisMod);
		// }
		//
		// conta5++;
		// lIndMisuraTerzo++;
		// }

		// ************************************************************************************
		// Carico i periodi di presofferto computabili prenotati da Bdmc
		// ************************************************************************************

		// Vector elencoPeriodi = new Vector();
		// Vector elencoProcPena = new Vector();
		// Vector lProvv = (Vector) getSessionAttribute("lProvvSession");
		// if (lProvv != null && lProvv.size() != 0) {
		// for (int i=0;i<lProvv.size();i++){
		// ProvvedimentoModelBDMC lProvvMod = (ProvvedimentoModelBDMC) lProvv.get(i);
		// Vector vectPeriPren = lProvvMod.getSbPeriPren();
		// if (vectPeriPren != null && vectPeriPren.size() != 0) {
		// for (int ii=0;ii<vectPeriPren.size();ii++){
		// if (((SbPeriprenModel)vectPeriPren.get(ii)).getCodStatPrenPeri().compareTo("0") == 0) {
		// elencoPeriodi.add(vectPeriPren.get(ii));
		// elencoProcPena.add(lProvvMod.getSbViewProcpena().get(0));
		// }
		// }
		// }
		// }
		// }
		// FascicoloSiepModel fascicolo = (FascicoloSiepModel)getSessionAttribute("fascicolo");
		// String[] periodi = null;
		// if (!isRequestParameterNullObj("CheckPeriodi")) {
		// periodi=getRequestStringParameters("CheckPeriodi");
		// }
		//
		// MisuraCautelareModel lMisCautelareMod = null;
		// MisuraCautelareBdmcModel lMisCautBdmcMod = null;
		// SbPeriprenModel lPeriPren = null;
		// SbViewProcpenaModel procPena=null;
		// Vector lVectModBdmc = new Vector();
		// Vector lVectMisCautBdmc = new Vector();
		// if (periodi != null){
		// for (int i=0;i<periodi.length;i++) {
		// procPena=(SbViewProcpenaModel)elencoProcPena.get(Integer.parseInt(periodi[i]));
		// lPeriPren = (SbPeriprenModel) elencoPeriodi.get(Integer.parseInt(periodi[i]));
		// lPeriPren.setDataDaPeri(getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_DALLA_DATA+Integer.parseInt(periodi[i]),
		// ICostantiSbPren.CAMPO_MESE_DALLA_DATA+Integer.parseInt(periodi[i]),
		// ICostantiSbPren.CAMPO_GIORNO_DALLA_DATA+Integer.parseInt(periodi[i])));
		// lPeriPren.setDataAPeri(getRequestDateParameter(ICostantiSbPren.CAMPO_ANNO_ALLA_DATA+Integer.parseInt(periodi[i]),
		// ICostantiSbPren.CAMPO_MESE_ALLA_DATA+Integer.parseInt(periodi[i]),
		// ICostantiSbPren.CAMPO_GIORNO_ALLA_DATA+Integer.parseInt(periodi[i])));
		//
		// lMisCautelareMod = new MisuraCautelareModel();
		// lMisCautelareMod.setAltroLuogoDetenzione(procPena.getDescLuog());
		// lMisCautelareMod.setCodLuogoUfficioRifer("-");
		// lMisCautelareMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lMisCautelareMod.setCodTipoMisura("-");
		//
		// if (lPeriPren.getDescPeri()!= null && lPeriPren.getDescPeri().indexOf("IN CARCERE") >0)
		// lMisCautelareMod.setCodTipoMisura("CA");
		// if (lPeriPren.getDescPeri()!= null && lPeriPren.getDescPeri().indexOf("DOMICILIARI") >0)
		// lMisCautelareMod.setCodTipoMisura("AD");
		//
		// // Calcolo numero giorni mesi anni della custodia
		//
		// CalendarModel lCalPenaEspiata = new CalendarModel();
		// CalendarUtil lCalUtil = new CalendarUtil();
		// lCalPenaEspiata.setDataInizio(lPeriPren.getDataDaPeri());
		// lCalPenaEspiata.setDataFine(lPeriPren.getDataAPeri());
		// lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata,false);
		// lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);
		//
		// lMisCautelareMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
		// lMisCautelareMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
		// lMisCautelareMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));
		//
		// //lMisCautelareMod.setCodTipoMisura(ProcPena.getCodiMisuCust());
		// lMisCautelareMod.setCodTipoUfficioRifer("-");
		// //lMisCautelareMod.setCodUfficioAggiornamento(aValore);
		// lMisCautelareMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// //lMisCautelareMod.setDataAggiornamento(aValore);
		// lMisCautelareMod.setDataFine(lPeriPren.getDataAPeri());
		// // lMisCautelareMod.setDataFungibilita(aValore);
		// lMisCautelareMod.setDataInizio(lPeriPren.getDataDaPeri());
		// lMisCautelareMod.setDataInserimento(DateUtils.getSysDate());
		// //lMisCautelareMod.setDescrLuogoUfficioRifer(aValore);
		// lMisCautelareMod.setCodMotivoNonComputabile("-");
		// lMisCautelareMod.setDescrTipoMisura(procPena.getDescriMisuCust());
		// lMisCautelareMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisCautelareMod.setFlagComputabile("S");
		//
		// lMisCautelareMod.setIstDetIdIstitutoDetenzione(procPena.getCodiIstiPena());
		// lVectModBdmc.add(lMisCautelareMod);
		//
		//
		// //Carico il vettore mis_caut_bdmc
		// lMisCautBdmcMod = new MisuraCautelareBdmcModel();
		//
		// lMisCautBdmcMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
		// lMisCautBdmcMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
		// lMisCautBdmcMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));
		//
		//
		// lMisCautBdmcMod.setProgPeriPres(lPeriPren.getProgPeriPres());
		// //lMisCautBdmcMod.setIdMisuraCautelare(lIdMisCau);
		// lMisCautBdmcMod.setAltroLuogoDetenzione(procPena.getDescLuog());
		// lMisCautBdmcMod.setIstDetIdIstitutoDetenzione(procPena.getCodiIstiPena());
		// //lMisCautBdmcMod.setAnnoFascSiep(lPeriPren.getAnnoFascSiep());
		// lMisCautBdmcMod.setNumeFascSiep(fascicolo.getChiaveProgr());
		// lMisCautBdmcMod.setAnnoFascSiep(fascicolo.getChiaveAnno());
		//
		// lMisCautBdmcMod.setAnnoFascBdmc(lPeriPren.getAnnoFascBdmc());
		// lMisCautBdmcMod.setNumeFascBdmc(lPeriPren.getNumeFascBdmc());
		// lMisCautBdmcMod.setCodUfficioBdmc(lPeriPren.getCodiSedeInst());
		// lMisCautBdmcMod.setCodTipoMisura(lMisCautelareMod.getCodTipoMisura());
		// lMisCautBdmcMod.setDataFine(lPeriPren.getDataFinePeri());
		// lMisCautBdmcMod.setDataInizio(lPeriPren.getDataInizPeri());
		// lMisCautBdmcMod.setIdPren(lPeriPren.getIdPren());
		// lMisCautBdmcMod.setFlagStato("I");
		// lMisCautBdmcMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisCautBdmcMod.setSogIdSoggetto(fascicolo.getSoggetto().getIdSoggetto());
		// lMisCautBdmcMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lMisCautBdmcMod.setDataInserimento(DateUtils.getSysDate());
		// lMisCautBdmcMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// lMisCautBdmcMod.setFlagComputabile(lPeriPren.getCodStatPrenPeri());
		// lMisCautBdmcMod.setFlagCaricamento("BDMC");
		// lMisCautBdmcMod.setStatoTrasmissioneIsc("N");
		// lMisCautBdmcMod.setDataFineUsata(lPeriPren.getDataAPeri());
		// lMisCautBdmcMod.setDataInizioUsata(lPeriPren.getDataDaPeri());
		// lVectMisCautBdmc.add(lMisCautBdmcMod);
		//
		// //fine carico vettore mis_caut_bdmc
		// }
		// }

		// ************************************************************************************

		// ************************************************************************************
		// Carico i periodi di presofferto non computabili prenotati da Bdmc
		// ************************************************************************************

		// elencoPeriodi = new Vector();
		// elencoProcPena = new Vector();
		// if (lProvv != null && lProvv.size() != 0) {
		// for (int i=0;i<lProvv.size();i++){
		// ProvvedimentoModelBDMC lProvvMod = (ProvvedimentoModelBDMC) lProvv.get(i);
		// Vector vectPeriPren = lProvvMod.getSbPeriPren();
		// if (vectPeriPren != null && vectPeriPren.size() != 0) {
		// for (int ii=0;ii<vectPeriPren.size();ii++){
		// if (((SbPeriprenModel)vectPeriPren.get(ii)).getCodStatPrenPeri().compareTo("0") != 0) {
		// elencoPeriodi.add(vectPeriPren.get(ii));
		// elencoProcPena.add(lProvvMod.getSbViewProcpena().get(0));
		// }
		// }
		// }
		// }
		// }
		//
		// periodi = null;
		// if (!isRequestParameterNullObj("CheckPeriodiNC")) {
		// periodi=getRequestStringParameters("CheckPeriodiNC");
		// }
		//
		// if (periodi != null){
		// for (int i=0;i<periodi.length;i++) {
		// procPena=(SbViewProcpenaModel)elencoProcPena.get(Integer.parseInt(periodi[i]));
		// lPeriPren = (SbPeriprenModel) elencoPeriodi.get(Integer.parseInt(periodi[i]));
		//
		// lMisCautelareMod = new MisuraCautelareModel();
		// lMisCautelareMod.setAltroLuogoDetenzione(procPena.getDescLuog());
		// lMisCautelareMod.setCodLuogoUfficioRifer("-");
		// lMisCautelareMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lMisCautelareMod.setCodTipoMisura("-");
		//
		// if (lPeriPren.getDescPeri()!= null && lPeriPren.getDescPeri().indexOf("IN CARCERE") >0)
		// lMisCautelareMod.setCodTipoMisura("CA");
		// if (lPeriPren.getDescPeri()!= null && lPeriPren.getDescPeri().indexOf("DOMICILIARI") >0)
		// lMisCautelareMod.setCodTipoMisura("AD");
		//
		// // Calcolo numero giorni mesi anni della custodia
		//
		// CalendarModel lCalPenaEspiata = new CalendarModel();
		// CalendarUtil lCalUtil = new CalendarUtil();
		// lCalPenaEspiata.setDataInizio(lPeriPren.getDataInizPeri());
		// lCalPenaEspiata.setDataFine(lPeriPren.getDataFinePeri());
		// lCalPenaEspiata = lCalUtil.CalcolaNumGiorniMesiAnni(lCalPenaEspiata,false);
		// lCalPenaEspiata = lCalUtil.ricalcolaGAM(lCalPenaEspiata);
		//
		// lMisCautelareMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
		// lMisCautelareMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
		// lMisCautelareMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));
		//
		// //lMisCautelareMod.setCodTipoMisura(ProcPena.getCodiMisuCust());
		// lMisCautelareMod.setCodTipoUfficioRifer("-");
		// //lMisCautelareMod.setCodUfficioAggiornamento(aValore);
		// lMisCautelareMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// //lMisCautelareMod.setDataAggiornamento(aValore);
		// lMisCautelareMod.setDataFine(lPeriPren.getDataFinePeri());
		// // lMisCautelareMod.setDataFungibilita(aValore);
		// lMisCautelareMod.setDataInizio(lPeriPren.getDataInizPeri());
		// lMisCautelareMod.setDataInserimento(DateUtils.getSysDate());
		// //lMisCautelareMod.setDescrLuogoUfficioRifer(aValore);
		// lMisCautelareMod.setCodMotivoNonComputabile("-");
		// lMisCautelareMod.setDescrTipoMisura(procPena.getDescriMisuCust());
		// lMisCautelareMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisCautelareMod.setFlagComputabile("N");
		//
		// lMisCautelareMod.setIstDetIdIstitutoDetenzione(procPena.getCodiIstiPena());
		// lVectModBdmc.add(lMisCautelareMod);
		//
		//
		// //Carico il vettore mis_caut_bdmc
		// lMisCautBdmcMod = new MisuraCautelareBdmcModel();
		//
		// lMisCautBdmcMod.setNumAnni(new BigDecimal(lCalPenaEspiata.getNumAnni()));
		// lMisCautBdmcMod.setNumGiorni(new BigDecimal(lCalPenaEspiata.getNumGiorni()));
		// lMisCautBdmcMod.setNumMesi(new BigDecimal(lCalPenaEspiata.getNumMesi()));
		//
		//
		// lMisCautBdmcMod.setProgPeriPres(lPeriPren.getProgPeriPres());
		// //lMisCautBdmcMod.setIdMisuraCautelare(lIdMisCau);
		// lMisCautBdmcMod.setAltroLuogoDetenzione(procPena.getDescLuog());
		// lMisCautBdmcMod.setIstDetIdIstitutoDetenzione(procPena.getCodiIstiPena());
		// //lMisCautBdmcMod.setAnnoFascSiep(lPeriPren.getAnnoFascSiep());
		// lMisCautBdmcMod.setNumeFascSiep(fascicolo.getChiaveProgr());
		// lMisCautBdmcMod.setAnnoFascSiep(fascicolo.getChiaveAnno());
		//
		// lMisCautBdmcMod.setAnnoFascBdmc(lPeriPren.getAnnoFascBdmc());
		// lMisCautBdmcMod.setNumeFascBdmc(lPeriPren.getNumeFascBdmc());
		// lMisCautBdmcMod.setCodUfficioBdmc(lPeriPren.getCodiSedeInst());
		// lMisCautBdmcMod.setCodTipoMisura(lMisCautelareMod.getCodTipoMisura());
		// lMisCautBdmcMod.setDataFine(lPeriPren.getDataFinePeri());
		// lMisCautBdmcMod.setDataInizio(lPeriPren.getDataInizPeri());
		// lMisCautBdmcMod.setIdPren(lPeriPren.getIdPren());
		// lMisCautBdmcMod.setFlagStato("I");
		// lMisCautBdmcMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		// lMisCautBdmcMod.setSogIdSoggetto(fascicolo.getSoggetto().getIdSoggetto());
		// lMisCautBdmcMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		// lMisCautBdmcMod.setDataInserimento(DateUtils.getSysDate());
		// lMisCautBdmcMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		// lMisCautBdmcMod.setFlagComputabile(lPeriPren.getCodStatPrenPeri());
		// lMisCautBdmcMod.setFlagCaricamento("BDMC");
		// lMisCautBdmcMod.setStatoTrasmissioneIsc("N");
		// lMisCautBdmcMod.setDataFineUsata(lPeriPren.getDataFinePeri());
		// lMisCautBdmcMod.setDataInizioUsata(lPeriPren.getDataInizPeri());
		// lVectMisCautBdmc.add(lMisCautBdmcMod);
		//
		// //fine carico vettore mis_caut_bdmc
		// }
		// }

		// ************************************************************************************

		// String lTipoMisPrimo = lArrayMisura[0];
		// IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		//
		// if (lTipoMisPrimo.equals("-"))
		// {
		// lCtrl.ExInserisciMisuraCautelare(lVectMod);
		// }
		// else
		// {
		// lCtrl.ExInserisciMisuraCautelareInserisciPosizioneGiuridica(lVectMod, lPosMod);
		// }

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();

		// if (campoMisura.equals("-"))
		// {
		lCtrl.ExInserisciMisuraCautelare(lVectMod);
		// }
		// else
		// {
		// lCtrl.ExInserisciMisuraCautelareInserisciPosizioneGiuridica(lVectMod, lPosMod);
		// }

		// Bdmc NON HA MAI FUNZIONATO!
		// oggi è cambiata tutta la logica... quindi Bdmc deve essere totalmente rifatto...
		// decisione presa di comune accordo con Salvatore S. 21/11/2014
		// A.S.
		// lCtrl.ExInserisciMisuraCautelareByBdmc(lVectModBdmc, lVectMisCautBdmc);

		setRequestAttribute("ComingFromInsert", "YES");

		// Invoca la action di caricamento dell'elenco misure
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuracautelare.action.ActRicercaMisuraCautelare&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFascicolo;

		return lPage;
	}
}