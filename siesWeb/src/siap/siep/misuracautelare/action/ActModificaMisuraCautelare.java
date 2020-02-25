package siap.siep.misuracautelare.action;

import java.math.BigDecimal;
import java.math.RoundingMode;

import siap.sico.calendar.model.CalendarModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.util.CalendarUtil;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.misuracautelarebdmc.controller.IMisuraCautelareBdmc;
import siap.siep.misuracautelarebdmc.model.MisuraCautelareBdmcModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActModificaMisuraCautelare
 * </p>
 * <p>
 * Description: Classe Action per la modifica di MisuraCautelare
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActModificaMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	/**
	 * Azione di Modifica del MisuraCautelare
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		// //=========================================================================================================
		// // Inizio Gestione verifica misure cautelari provenienti da BDMC
		// // - Ricerco la misura cautelare in modifica su tabella misura_cautelare_Bdmc
		// // - Se presente verifico che i nuovi estremi del periodo ricadano dell'intervallo del periodo
		// // prenotato su Bdmc altrimenti vado avanti come vecchia gestione
		// // - Se presente e nuovo intervallo corretto aggiorno la misura cautelare e la relativa riga su
		// // misura_cautelare_bdmc
		// // - Se presente e nuovo intervallo errato mostro messaggio di errore e non effetuo nessuna
		// modifica
		// //==========================================================================================================
		MisuraCautelareBdmcModel lModBdmc = new MisuraCautelareBdmcModel();
		IMisuraCautelareBdmc lCtrlBdmc = SIEPLookupRemote.getMisuraCautelareBdmcRemote();
		lModBdmc.setIdMisuraCautelare(getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE));
		lModBdmc.setFlagStato("I");
		/*Vector elencoBdmc = */lCtrlBdmc.ExRicercaMisuraCautelareBdmc(lModBdmc);
//		boolean flagAggiornaMisBdmc = false;
		//
		// if (elencoBdmc != null && elencoBdmc.size() != 0)
		// {
		// lModBdmc = (MisuraCautelareBdmcModel) elencoBdmc.get(0);
		// if(DateUtils.isGreater(lModBdmc.getDataInizio(),
		// DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO),getRequestStringParameter(CAMPO_MESE_DATA_INIZIO),getRequestStringParameter(CAMPO_GIORNO_DATA_INIZIO))))
		// {
		// setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
		// "Misura Cautelare importata da BDMC: Data Inizio fuori intervallo consentito ");
		// return ISIAPCostantiWeb.PG_MESSAGE;
		// }
		//
		// if(DateUtils.isGreater(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO),getRequestStringParameter(CAMPO_MESE_DATA_INIZIO),getRequestStringParameter(CAMPO_GIORNO_DATA_INIZIO)),lModBdmc.getDataFine()))
		// {
		// setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
		// "Misura Cautelare importata da BDMC: Data Fine fuori intervallo consentito ");
		// return ISIAPCostantiWeb.PG_MESSAGE;
		// }
		//
		// if(!getRequestStringParameter("dataFine").equals(""))
		// {
		// if(DateUtils.isGreater(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_FINE),getRequestStringParameter(CAMPO_MESE_DATA_FINE),getRequestStringParameter(CAMPO_GIORNO_DATA_FINE)),lModBdmc.getDataFine()))
		// {
		// setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
		// "Misura Cautelare importata da BDMC: Data Fine fuori intervallo consentito ");
		// return ISIAPCostantiWeb.PG_MESSAGE;
		// }
		//
		// if(DateUtils.isGreater(lModBdmc.getDataInizio(),
		// DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_FINE),getRequestStringParameter(CAMPO_MESE_DATA_FINE),getRequestStringParameter(CAMPO_GIORNO_DATA_FINE))))
		// {
		// setRequestAttribute(ISIAPCostantiWeb.MESSAGE_TEXT,
		// "Misura Cautelare importata da BDMC: Data Inizio fuori intervallo consentito ");
		// return ISIAPCostantiWeb.PG_MESSAGE;
		// }
		// flagAggiornaMisBdmc = true;
		// }
		// }
		//
		//
		// MisuraCautelareModel lMisMod = new MisuraCautelareModel();
		// CalendarModel lCalMod =new CalendarModel();
		//
		// lMisMod.setIdMisuraCautelare( getRequestBigDecimalParameter( CAMPO_ID_MISURA_CAUTELARE) );
		// lMisMod.setFasSieIdFascicoloSiep( getRequestBigDecimalParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP) );
		//
		// lMisMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		// lMisMod.setCodUfficioAggiornamento( getCodUfficioUtenteConnesso());
		//
		// lMisMod.setCodTipoMisura(getRequestStringParameter(CAMPO_COD_TIPO_MISURA));
		// String GiornoI = getRequestStringParameter(CAMPO_GIORNO_DATA_INIZIO);
		// String MeseI = getRequestStringParameter(CAMPO_MESE_DATA_INIZIO);
		// String AnnoI = getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO);
		//
		// lMisMod.setFlagComputabile("S");
		// lMisMod.setDataInizio(DateUtils.getDate(AnnoI, MeseI, GiornoI));
		//
		// if(!getRequestStringParameter("dataFine").equals(""))
		// {
		// String GiornoF = getRequestStringParameter(CAMPO_GIORNO_DATA_FINE);
		// String MeseF = getRequestStringParameter(CAMPO_MESE_DATA_FINE);
		// String AnnoF = getRequestStringParameter(CAMPO_ANNO_DATA_FINE);
		//
		// lMisMod.setDataFine(DateUtils.getDate(AnnoF, MeseF, GiornoF));
		//
		// //------presofferto---------
		// lCalMod.setDataInizio(DateUtils.getDate(AnnoI, MeseI, GiornoI));
		// lCalMod.setDataFine(DateUtils.getDate(AnnoF, MeseF, GiornoF));
		//
		// CalendarUtil lCalUtil=new CalendarUtil();
		// lCalMod=lCalUtil.CalcolaNumGiorniMesiAnni(lCalMod);
		//
		// lMisMod.setNumAnni(new BigDecimal(lCalMod.getNumAnni()));
		//
		// lMisMod.setNumMesi(new BigDecimal(lCalMod.getNumMesi()));
		// lMisMod.setNumGiorni(new BigDecimal(lCalMod.getNumGiorni()));
		// }
		//
		// //modifica relativa al tipo istituto
		// lMisMod.setIstDetIdIstitutoDetenzione(getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		// //
		// lMisMod.setCodTipoIstitutoDetenzione(getRequestStringParameter(CAMPO_COD_TIPO_ISTITUTO_DETENZIONE));
		// lMisMod.setAltroLuogoDetenzione(getRequestStringParameter(CAMPO_ALTRO_LUOGO_DETENZIONE));
		// // ComuneModel lComMod = new
		// ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_DETENZIONE)) );
		// // lMisMod.setCodLuogoDetenzione( lComMod.getCodComune());
		//
		// ComuneModel lComMod = new ComuneModel();
		//
		// if(getRequestStringParameter("computabile").equals("N"))
		// {
		// lMisMod.setCodMotivoNonComputabile(getRequestStringParameter(CAMPO_COD_MOTIVO_NON_COMPUTABILE));
		// lMisMod.setNumRifer(getRequestStringParameter(CAMPO_NUM_RIFER));
		// lMisMod.setCodTipoUfficioRifer(getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_RIFER));
		//
		// lComMod = new
		// ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_RIFER)) );
		//
		// lMisMod.setCodLuogoUfficioRifer(lComMod.getCodComune());
		//
		// String GiornoFung = getRequestStringParameter(CAMPO_GIORNO_DATA_FUNGIBILITA);
		// String MeseFung = getRequestStringParameter(CAMPO_MESE_DATA_FUNGIBILITA);
		// String AnnoFung = getRequestStringParameter(CAMPO_ANNO_DATA_FUNGIBILITA);
		//
		// lMisMod.setDataFungibilita(DateUtils.getDate(AnnoFung, MeseFung, GiornoFung));
		// lMisMod.setFlagComputabile("N");
		// lMisMod.setNote(getRequestStringParameter(CAMPO_NOTE));
		// }
		//
		// if(getRequestStringParameter("dataFine").equals(""))
		// {
		// lMisMod.setCodMotivoNonComputabile("-");
		// lMisMod.setCodTipoUfficioRifer("-");
		// lMisMod.setCodLuogoUfficioRifer("-");
		// }
		//
		// //----------------------------
		// //****************riempio model posizione giuridica*********
		// PosizioneGiuridicaModel lPosMod = new PosizioneGiuridicaModel();
		//
		// if(lMisMod.getCodTipoMisura().equals("CA"))
		// {
		// lPosMod.setCodPosizioneGiuridica("01");
		// }
		//
		// if(lMisMod.getCodTipoMisura().equals("AD"))
		// {
		// lPosMod.setCodPosizioneGiuridica("02");
		// }
		//
		//
		// lPosMod.setDataInizio(lMisMod.getDataInizio());
		// lPosMod.setFasSieIdFascicoloSiep(lMisMod.getFasSieIdFascicoloSiep());
		// lPosMod.setCodOperatoreInserimento(lMisMod.getCodOperatoreInserimento());
		// lPosMod.setCodUfficioInserimento(lMisMod.getCodUfficioInserimento());
		// lPosMod.setCodPosizioneProcessuale("-");
		// lPosMod.setDataInserimento(DateUtils.getSysDate());
		// //******************************************************************************
		//
		//
		// //inizio calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
		// if (lMisMod.getCodTipoMisura().equalsIgnoreCase("CL")){
		// int giorni = lCalMod.getNumGiorni();
		// int mesi = lCalMod.getNumMesi();
		// int anni = lCalMod.getNumAnni();
		//
		// int numGiorni = anni*360 + mesi*30 + giorni;
		// int numTotGiorni = numGiorni;
		// //numGiorni = numGiorni/3;
		// //numGiorni = 151;
		// //int periodiGiorniUnoTre = 2;
		// BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(numGiorni);
		// BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
		// BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
		// BigDecimal numDiff = new BigDecimal(0);
		// BigDecimal uno = new BigDecimal(1);
		// numGiorniAnniMesiGiorniDivTre=numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1,
		// RoundingMode.HALF_UP);
		// //int num = tot.intValue();
		// BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new
		// BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue()).negate();
		// //numDiff = numGiorniAnniMesiGiorniDivTre;
		// numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);
		//
		// //Se il decimale è >= 6 allora arrotondamento per eccesso.
		// //Se il decimale è <= 5 allora arrotondamento per difetto.
		// BigDecimal zero5 = new BigDecimal("0.5");
		// if(numDiff.compareTo(zero5)==0 || numDiff.compareTo(zero5)==-1){
		// //Se il decimale è <= 5 allora arrotondamento per difetto.
		// numGiorniAnniMesiGiorniDivTre = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue());
		// //numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
		// } else {
		// numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
		// }
		//
		// numGiorni = numGiorniAnniMesiGiorniDivTre.intValue();
		// mesi=0;
		// anni=0;
		// //I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM
		// if (numGiorni > 30)
		// {
		// int tmp = numGiorni/30 ;
		// mesi+=tmp;
		// numGiorni-=tmp*30;
		// }
		// if (numGiorni==30)
		// {
		// mesi++;
		// numGiorni=0;
		// }
		// if (mesi > 12)
		// {
		// int tmp=mesi/12;
		// anni+=tmp;
		// mesi-=tmp*12;
		// }
		//
		// if (mesi==12)
		// {
		// anni++;
		// mesi=0;
		// }
		// lMisMod.setNumAnni(new BigDecimal(anni));
		// lMisMod.setNumMesi(new BigDecimal(mesi));
		// lMisMod.setNumGiorni(new BigDecimal(numGiorni));
		// lMisMod.setGiorni(new BigDecimal(numTotGiorni));
		// }
		// //fine calcolo giorni/mesi/anni per misura cautelare computo periodo messa alla prova
		//
		// MisuraCautelareModel lMisModRet= null;
		// // chiama il controller
		// IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		// if(lMisMod.getDataFine()!= null)
		// {
		// lMisModRet = lCtrl.ExModificaMisuraCautelare(lMisMod);
		// }
		// else
		// {
		// lMisModRet = lCtrl.ExModificaMisuraCautelareModificaPosizioneGiuridica(lMisMod,lPosMod);
		// }
		// if (flagAggiornaMisBdmc ) {
		// lModBdmc.setDataFineUsata(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_FINE),getRequestStringParameter(CAMPO_MESE_DATA_FINE),getRequestStringParameter(CAMPO_GIORNO_DATA_FINE)));
		// lModBdmc.setDataInizioUsata(DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_INIZIO),getRequestStringParameter(CAMPO_MESE_DATA_INIZIO),getRequestStringParameter(CAMPO_GIORNO_DATA_INIZIO)));
		// lCtrlBdmc.ExModificaMisuraCautelareBdmc(lModBdmc,null);
		// }
		// setRequestAttribute("modalita", "M");
		// setRequestAttribute("misuracautelare", lMisModRet);
		//
		// String lPage = "";
		// lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
		// "=siap.siep.misuracautelare.action.ActLoadDettaglioMisuraCautelare&"+CAMPO_ID_MISURA_CAUTELARE+"="+lMisModRet.getIdMisuraCautelare().toString();
		//
		// return lPage;

		CalendarModel lCalMod = new CalendarModel();
		MisuraCautelareModel lMisMod = new MisuraCautelareModel();

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		lMisMod = lCtrl
				.ExRicercaMisuraCautelareByKey(getRequestBigDecimalParameter(CAMPO_ID_MISURA_CAUTELARE));

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
			} else if (lMisMod != null && lMisMod.getIstitutoDetenzione() != null) {
				istitutoDetenzione = lMisMod.getIstitutoDetenzione().getIdIstitutoDetenzione();
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
		// Vector lVectMod = new Vector();

//		String lTipoMis = "";

		// lMisMod = new MisuraCautelareModel();

		lMisMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lMisMod.setCodTipoMisura(campoMisura);
		lMisMod.setDataEmissioneOrdinanza(DateUtils.getDate(annoEmissioneOrdinanza, meseEmissioneOrdinanza,
				giornoEmissioneOrdinanza));
		lMisMod.setDataFungibilita(DateUtils.getDate(annoFung, meseFung, giornoFung));
		lMisMod.setNote(note);
		lMisMod.setAnnoRifer(annoRifer);
		lMisMod.setNumRifer(numRifer);

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
		// String flagComputabile = getRequestStringParameter("flagComputabile");
		// lMisMod.setFlagComputabile(flagComputabile);
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

		lMisMod.setCodiceUfficioPmSede(codiceUfficioPmSede);

		if (codiceUfficioTerritorioSede != null && !"".equalsIgnoreCase(codiceUfficioTerritorioSede)) {
			lComMod = new ComuneModel(getCodComuneByDescr(codiceUfficioTerritorioSede));
			lMisMod.setAutoritaCompetenteSede(lComMod.getCodComune());
		} else {
			lMisMod.setAutoritaCompetenteSede("");
		}

		// if(flagComputabile!=null && flagComputabile.equalsIgnoreCase("N")) {
		if (lMisMod.getFlagComputabile() != null && lMisMod.getFlagComputabile().equalsIgnoreCase("N")) {
			if (sedeProvvedimentoFungibilita != null && !"".equalsIgnoreCase(sedeProvvedimentoFungibilita)) {
				lComMod = new ComuneModel(getCodComuneByDescr(sedeProvvedimentoFungibilita));
				lMisMod.setCodLuogoUfficioRifer(lComMod.getCodComune());
			} else {
				lMisMod.setCodLuogoUfficioRifer("-");
			}
		}

		ufficio = "-";
		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_UFFICIO_RIFER)) {
			ufficio = getRequestStringParameter(CAMPO_COD_TIPO_UFFICIO_RIFER);
		}

		// Sede Sezione G
		sedeProvvedimentoFungibilita = "";
		if (!isRequestParameterNullObj(CAMPO_COD_LUOGO_UFFICIO_RIFER)) {
			sedeProvvedimentoFungibilita = getRequestStringParameter(CAMPO_COD_LUOGO_UFFICIO_RIFER);
		} else {
			sedeProvvedimentoFungibilita = "-";
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
		// lVectMod.add(lMisMod);

		// IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		// lCtrl.ExInserisciMisuraCautelare(lVectMod);
		lCtrl.ExModificaMisuraCautelare(lMisMod);

		setRequestAttribute("ComingFromInsert", "YES");

		// Invoca la action di caricamento dell'elenco misure
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuracautelare.action.ActRicercaMisuraCautelare&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFascicolo;

		return lPage;

	}

}