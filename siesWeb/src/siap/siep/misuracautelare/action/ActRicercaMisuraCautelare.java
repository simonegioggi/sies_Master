package siap.siep.misuracautelare.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.altracausa.controller.IAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.luogodetenzione.controller.ILuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.siep.misuracautelare.controller.IMisuraCautelare;
import siap.siep.misuracautelare.model.MisuraCautelareModel;
//import siap.siep.misuracautelare.model.MisuraCautelareModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.CaricaHTML_Servlet;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActRicercaMisuraCautelare
 * </p>
 * <p>
 * Description: Classe Action per la ricerca di MisuraCautelare
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
public class ActRicercaMisuraCautelare extends ActionSiap implements ICostantiMisuraCautelare {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lIdFascicolo = this
				.getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
		Vector lVect = lCtrl.ExRicercaMisureCautelariByIdFascicoloNoDataNull(lIdFascicolo);
		Vector lVectSiDateNull = lCtrl.ExRicercaMisureCautelariByIdFascicoloSiDataNull(lIdFascicolo);
		Vector lVectSoloDataInizio = lCtrl.ExRicercaMisureCautelariByIdFascicoloSoloDataInizio(lIdFascicolo);
		lVectSiDateNull.addAll(lVectSoloDataInizio);
		// inizio MEV_10_S3
		// Quando 2 o più misure cautelari computabili sono continuative il sistema
		// non deve visualizzare nell'elenco delle misure il "totale dei giorni"
		Vector lMisureApp = new Vector();
		Date lDateFinePrec = null;
		// identifica il numero di riga del vettore
		int inc = -1;
		// indica i giorni che intercorrono tra 2 misure cautelari consecutiva
		int giorni = -1;

		Iterator lItMisure = lVect.iterator();
		while (lItMisure.hasNext()) {
			MisuraCautelareModel lMisCautModel = new MisuraCautelareModel(
					(MisuraCautelareModel) lItMisure.next());

			inc += 1;

			// misure cautelari computabili
			// se le misure sono continuative non deve essere visibile il totale dei giorni
			if (lMisCautModel.getFlagComputabile() != null && !lMisCautModel.getFlagComputabile().equals("")
					&& lMisCautModel.getFlagComputabile().equals("S")) {

				// aggiungo la misura cautelare al vettore di appoggio
				lMisureApp.add(lMisCautModel);

				// sono sul primo elemento del vettore, pertanto
				// recupero la data fine della misura cautelare corrente
				if (inc == 0) {
					lDateFinePrec = lMisCautModel.getDataFine();
					lMisCautModel.setMisCautContinuativa("N");
				} else {
					// quando sono sugli elementi successivi al primo, devo
					// recuperare la data fine della misura cautelare precedente
					MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
					lDateFinePrec = lMisCaut.getDataFine();
				}

				// si effettua la differenza tra la data fine della misura cautelare
				// precedente con la data inizio della misura corrente
				// (tranne per il primo elemento del vettore)
				if (inc > 0) {
					giorni = DateUtils.getIntervallo(lDateFinePrec, lMisCautModel.getDataInizio());
				}

				// se la differenza tra la data fine e la data inizio è maggiore
				// di 1, significa che le 2 misure cautelari non sono continuative
				if (giorni > 1) {
					// misure cautelari non continuative
					lMisCautModel.setMisCautContinuativa("N");

					// Recupero la misura cautelare precedente e imposto il flag
					// MisCautContinuativa uguale ad 'N' solo se quest'ultimo è diverso
					// da 'S'
					MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
					if (lMisCaut.getMisCautContinuativa() == null
							|| lMisCaut.getMisCautContinuativa().equals("")
							|| !lMisCaut.getMisCautContinuativa().equals("S")) {
						lMisCaut.setMisCautContinuativa("N");
					}

					// se la differenza tra la data fine e la data inizio è
					// uguale a 0 oppure uguale a 1 le misure cautelari
					// sono continuative
				} else if (giorni == 0 || giorni == 1) {
					// misure cautelari continuative
					lMisCautModel.setMisCautContinuativa("S");

					// Recupero la misura cautelare precedente e imposto il flag
					// MisCautContinuativa uguale ad 'S'
					MisuraCautelareModel lMisCaut = (MisuraCautelareModel) lMisureApp.get(inc - 1);
					lMisCaut.setMisCautContinuativa("S");
				} else if (inc > 0 && giorni < 0) {
					// caso in cui i periodi tra le misure si sovrappongono
					// es. periodo prima misura dal 01/01/2015 al 20/02/2015
					// periodo seconda misura dal 20/01/2015 al 25/01/2015

					// misure cautelari non continuative
					lMisCautModel.setMisCautContinuativa("N");
				}

				/*
				 * } else if (lMisCautModel.getFlagComputabile() != null &&
				 * !lMisCautModel.getFlagComputabile().equals("") &&
				 * lMisCautModel.getFlagComputabile().equals("S") && lMisCautModel.getCodTipoMisura() != null
				 * && !lMisCautModel.getCodTipoMisura().equals("") &&
				 * !lMisCautModel.getCodTipoMisura().equals("CL")){ // misure cautelari computabili // imposto
				 * il flag MisCautContinuativa uguale ad 'N' // perchè il totale dei giorni deve essere
				 * visibile lMisCautModel.setMisCautContinuativa("N"); lMisureApp.add(lMisCautModel);
				 */
			} else {
				// misure cautelari non computabili
				// imposto il flag MisCautContinuativa uguale ad 'S'
				// perchè il totale dei giorni non deve essere visibile
				lMisCautModel.setMisCautContinuativa("S");

				// aggiungo la misura cautelare al vettore di appoggio
				lMisureApp.add(lMisCautModel);

			}

		}

		// calcolo dei totali parziali
		// Se nell’elenco delle misure cautelari sono presenti 2 o più misure cautelari computabili
		// il sistema nella riga dell’ultima misura cautelare computabile continuativa
		// dovrà mostrare il totale espresso in Anni, Mesi e Giorni delle suddette
		// misure cautelari computabili continuative.
		int mNumTotAnniUltimaMisCauCom = 0;
		int mNumTotMesiUltimaMisCauCom = 0;
		int mNumTotGiorniUltimaMisCauCom = 0;
//		boolean misCautContinuativa = false;
//		BigDecimal idUltimaMisCautContinuativa = new BigDecimal(0);

		// inizio calcolo dei totali
		// popolo una tabella di sei colonne e n righe quanti sono i periodi
		// nomi colonne: Data-inizio | Data-finale |id Misura Cautelare | Periodi-continuativi | Misura
		// Cautelere Continuativa
		int nRighe = lMisureApp.size();
		int nRigheMatriceMisContinuativi = 0;
		int nColonne = 5;
		String[][] matricePeriodi = new String[nRighe][nColonne];
		int numPeriodiContinuativi = 0;
		boolean precedentePeriodoNonConsecutivo = false;

		for (int j = 0; j < lMisureApp.size(); j++) {
			// MisuraCautelareModel lMisCautAppModel = new MisuraCautelareModel((MisuraCautelareModel)
			// lItMisureApp.next());
			MisuraCautelareModel lMisCautAppModel = new MisuraCautelareModel(
					(MisuraCautelareModel) lMisureApp.get(j));
			if (lMisCautAppModel.getMisCautContinuativa().equalsIgnoreCase("S")
					&& lMisCautAppModel.getFlagComputabile().equalsIgnoreCase("S")) {
				// misCautContinuativa=true;

				// matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata,"dd/MM/yyyy" );
				Date datainizio = lMisCautAppModel.getDataInizio();
				Date datafine = lMisCautAppModel.getDataFine();
				matricePeriodi[nRigheMatriceMisContinuativi][0] = DateUtils.getDateToString(datainizio,
						"dd/MM/yyyy");
				matricePeriodi[nRigheMatriceMisContinuativi][1] = DateUtils.getDateToString(datafine,
						"dd/MM/yyyy");
				matricePeriodi[nRigheMatriceMisContinuativi][2] = lMisCautAppModel.getIdMisuraCautelare()
						.toString();
				// Periodi sono Continuativi
				if (precedentePeriodoNonConsecutivo) {
					// matricePeriodi[nRigheMatriceMisContinuativi][3] = Integer.toString(0);
					numPeriodiContinuativi = numPeriodiContinuativi + 1;
					matricePeriodi[nRigheMatriceMisContinuativi][3] = Integer
							.toString(numPeriodiContinuativi);
				} else {
					matricePeriodi[nRigheMatriceMisContinuativi][3] = Integer
							.toString(numPeriodiContinuativi);
				}
				matricePeriodi[nRigheMatriceMisContinuativi][4] = "S";
				// numPeriodiContinuativiRaggruppato = numPeriodiContinuativiRaggruppato +1;
				nRigheMatriceMisContinuativi = nRigheMatriceMisContinuativi + 1;
				precedentePeriodoNonConsecutivo = false;
			}
			if (lMisCautAppModel.getMisCautContinuativa().equalsIgnoreCase("N")) {
				Date datainizio = lMisCautAppModel.getDataInizio();
				Date datafine = lMisCautAppModel.getDataFine();
				matricePeriodi[nRigheMatriceMisContinuativi][0] = DateUtils.getDateToString(datainizio,
						"dd/MM/yyyy");
				matricePeriodi[nRigheMatriceMisContinuativi][1] = DateUtils.getDateToString(datafine,
						"dd/MM/yyyy");
				matricePeriodi[nRigheMatriceMisContinuativi][2] = lMisCautAppModel.getIdMisuraCautelare()
						.toString();
				if (j == 0) {
					matricePeriodi[nRigheMatriceMisContinuativi][3] = Integer.toString(0);
					numPeriodiContinuativi = numPeriodiContinuativi + 1;
				} else {
					numPeriodiContinuativi = numPeriodiContinuativi + 1;
					matricePeriodi[nRigheMatriceMisContinuativi][3] = Integer
							.toString(numPeriodiContinuativi);
					precedentePeriodoNonConsecutivo = true;
				}
				matricePeriodi[nRigheMatriceMisContinuativi][4] = "N";
				// numPeriodiContinuativiRaggruppato = numPeriodiContinuativiRaggruppato +1;
				nRigheMatriceMisContinuativi = nRigheMatriceMisContinuativi + 1;
			}
		}

		// popolo una tabella di tre colonne e n righe quanti sono i periodi
		// nomi colonne: Data-inizio | Data-finale |id Misura Cautelare
		int nRigheMatriceRaggruppamenteMisContinuativi = 0;
		int nColonneRaggruppamente = 3;
		String[][] matricePeriodiRaggruppamento = new String[nRighe][nColonneRaggruppamente];
		// numPeriodiContinuativi = 0;
		int numPeriodiContinuativiRaggruppato = 0;
		int rigaMatRag = 0;
		boolean incRigaMatRag = false;
		for (int i = 0; i < nRigheMatriceMisContinuativi; i++) {
			// if(Integer.parseInt(matricePeriodi[i][3])==numPeriodiContinuativiRaggruppato &&
			// matricePeriodi[i][4].equalsIgnoreCase("S")){
			if (matricePeriodi[i][4].equalsIgnoreCase("S")) {
				matricePeriodiRaggruppamento[nRigheMatriceRaggruppamenteMisContinuativi][0] = matricePeriodi[i][0];// data
																													// inizio
				numPeriodiContinuativiRaggruppato = Integer.parseInt(matricePeriodi[i][3]);
				matricePeriodi[i][0] = "*";
				matricePeriodi[i][1] = "*";
				matricePeriodi[i][2] = "*";
				matricePeriodi[i][3] = "*";
				matricePeriodi[i][4] = "*";

				// String idMisuraCautelareCorrente = matricePeriodi[i][2];

				for (int j = 0; j < nRigheMatriceMisContinuativi; j++) {
					// if(Integer.parseInt(matricePeriodi[i][3])==numPeriodiContinuativi &&
					// !idMisuraCautelareCorrente.equalsIgnoreCase(matricePeriodi[i][2])){
					if (matricePeriodi[j][3] != null && !matricePeriodi[j][3].equalsIgnoreCase("*")
							&& Integer.parseInt(matricePeriodi[j][3]) == numPeriodiContinuativiRaggruppato
							&& matricePeriodi[j][4].equalsIgnoreCase("S")) {
						matricePeriodiRaggruppamento[rigaMatRag][1] = matricePeriodi[j][1];// data fine
						matricePeriodiRaggruppamento[rigaMatRag][2] = matricePeriodi[j][2];// id misura da
																							// modificare
						matricePeriodi[j][0] = "*";
						matricePeriodi[j][1] = "*";
						matricePeriodi[j][2] = "*";
						matricePeriodi[j][3] = "*";
						matricePeriodi[j][4] = "*";
						incRigaMatRag = true;
						// rigaMatRag = rigaMatRag+1;
					}
				}
				if (incRigaMatRag) {
					rigaMatRag = rigaMatRag + 1;
					incRigaMatRag = false;
				}

				numPeriodiContinuativiRaggruppato = numPeriodiContinuativiRaggruppato + 1;
				nRigheMatriceRaggruppamenteMisContinuativi = nRigheMatriceRaggruppamenteMisContinuativi + 1;
			}
			// else if(Integer.parseInt(matricePeriodi[i][3])==numPeriodiContinuativiRaggruppato &&
			// matricePeriodi[i][4].equalsIgnoreCase("N")){
			// numPeriodiContinuativiRaggruppato = numPeriodiContinuativiRaggruppato +1;
			// }
		}

		BigDecimal idMisuraCautelareRaggruppata = new BigDecimal(0);
		for (int i = 0; i < rigaMatRag; i++) {
			Date datainizio = DateUtils.getDate(matricePeriodiRaggruppamento[i][0], "dd/MM/yyyy");
			Date datafine = DateUtils.getDate(matricePeriodiRaggruppamento[i][1], "dd/MM/yyyy");
			idMisuraCautelareRaggruppata = new BigDecimal(matricePeriodiRaggruppamento[i][2]);
			String ggInizio = DateUtils.getDayToString(datainizio);
			String mmInizio = DateUtils.getMonthToString(datainizio);
			String aaInizio = DateUtils.getYearToString(datainizio);
			String ggFine = DateUtils.getDayToString(datafine);
			String mmFine = DateUtils.getMonthToString(datafine);
			String aaFine = DateUtils.getYearToString(datafine);
			String calcoloGioniMesiAnni = "";
			try {
				calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo(ggInizio, mmInizio, aaInizio,
						ggFine, mmFine, aaFine);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String sep = "~#";
			String[] aPairs = new String[3];
			aPairs = calcoloGioniMesiAnni.split(sep);
			mNumTotAnniUltimaMisCauCom = Integer.parseInt(aPairs[0]);
			mNumTotMesiUltimaMisCauCom = Integer.parseInt(aPairs[1]);
			mNumTotGiorniUltimaMisCauCom = Integer.parseInt(aPairs[2]);
			for (int k = 0; k < lMisureApp.size(); k++) {
				// MisuraCautelareModel lMisCautM = new MisuraCautelareModel((MisuraCautelareModel)
				// lItMisureAppModificata.next());
				// if(lMisCautM.getIdMisuraCautelare().compareTo(idUltimaMisCautContinuativa)==0){
				MisuraCautelareModel lMisCautM = new MisuraCautelareModel(
						(MisuraCautelareModel) lMisureApp.get(k));
				if (lMisCautM.getIdMisuraCautelare().compareTo(idMisuraCautelareRaggruppata) == 0) {
					lMisCautM.setNumTotAnniUltimaMisCauCom(new BigDecimal(mNumTotAnniUltimaMisCauCom));
					lMisCautM.setNumTotMesiUltimaMisCauCom(new BigDecimal(mNumTotMesiUltimaMisCauCom));
					lMisCautM.setNumTotGiorniUltimaMisCauCom(new BigDecimal(mNumTotGiorniUltimaMisCauCom));
					lMisCautM.setFlagUltimaMisCauCommutabile("S");
					mNumTotAnniUltimaMisCauCom = 0;
					mNumTotMesiUltimaMisCauCom = 0;
					mNumTotGiorniUltimaMisCauCom = 0;
					lMisureApp.set(k, lMisCautM);
				}
			}
		}

		// aggiorno il vettore delle misure cautelari
		lVect = new Vector(lMisureApp);
		// fine MEV_10_S3

		if (lVect.isEmpty() && lVectSiDateNull.isEmpty()) {
			// throw new F3BException(F3BException.USER_MESSAGE, "Nessuna Misura Cautelare trovata");
			RedirectTo lRedirigi = new RedirectTo();
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Nessuna Misura Cautelare trovata");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "=" + lIdFascicolo);
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;

		}

		ILuogoDetenzione Iluo = SIEPLookupRemote.getLuogoDetenzioneRemote();
		LuogoDetenzioneModel Lluo = Iluo.ExRicercaLuogoDetByFascicolo(lIdFascicolo);

		IPosizioneGiuridica IPos = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPos = IPos.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		// inizio gestione desc posizione giuridica
		// I) se la posizione giuridica !="07" ==> parto dalla tabella POSIZIONE_GIURIDICA tramite
		// CG_REF_CODES prendo la DescrPosizioneGiuridica
		// II) se la posizione giuridica ="07" && COD_MASCHERA="L" non è presente altra causa la
		// DescrPosizioneGiuridica="Libero"
		// else la posizione giuridica ="07" ==> è presente altra causa la DescrPosizioneGiuridica si
		// considera
		// la desc di CG_REF_CODES tramite la tabella ALTRA_CAUSA partendo dalla tabella
		// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA
		// II.1) per la vecchia gestione posizione giuridica ="07" &&
		// POSIZIONE_GIURIDICA.ALT_CAU_ID_ALTRA_CAUSA=null && COD_MASCHERA=NULL
		// trovo la descrizione sulla tabella ALTRA_CAUSA tramite FAS_ID_FASCICOLO_SIEP se trovo ALTRA_CAUSA
		// metto la descrizione
		// di altra causa altrimenti libero
		IAltraCausa lAcCtrl = SIEPLookupRemote.getAltraCausa();
		if (lPos != null && lPos.getIdPosizioneGiuridica() != null
				&& !lPos.getCodPosizioneGiuridica().equalsIgnoreCase("07")) {
			// è già presente la descrizione giusta
		} else if (lPos != null && lPos.getIdPosizioneGiuridica() != null
				&& lPos.getCodPosizioneGiuridica().equalsIgnoreCase("07")
				&& lPos.getAltCauIdAltraCausa() == null) {
			AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByFascicolo(lPos
					.getFasSieIdFascicoloSiep());
			if (altraCausa != null && lPos.getCodMaschera() == null) {
				// vecchia gestione
				lPos.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
			} else if (lPos.getCodMaschera() == "L") {
				lPos.setDescrPosizioneGiuridica("Libero");
			}
		} else if (lPos != null && lPos.getIdPosizioneGiuridica() != null
				&& lPos.getCodPosizioneGiuridica().equalsIgnoreCase("07")
				&& lPos.getAltCauIdAltraCausa() != null) {
			// Ricerca Altra Causa
			AltraCausaModel altraCausa = lAcCtrl.ExRicercaAltraCausaIstitutoByKey(lPos
					.getAltCauIdAltraCausa());
			lPos.setDescrPosizioneGiuridica(altraCausa.getDescrTipoPosGiuridica());
		}
		// fine gestione desc posizione giuridica

		setRequestAttribute("misureCautelari", lVect);
		setRequestAttribute("misureCautelariDateNull", lVectSiDateNull);
		setRequestAttribute("PosizioneGiuridica", lPos);
		setRequestAttribute("LuogoDetenzione", Lluo);

		// richiesta asir a9/rr/075 04-06-2009 SIEP MEV - Warning sul primo calcolo della pena
		// paolo cherubini lunedi 11/10/2010
		// Ricerca l'ultima pena residua per quel fascicolo
		IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResMod = lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		if (lPenaResMod != null) {
			setRequestAttribute("lPenaResMod", lPenaResMod);
		}
		setLinkRitorno();
		// fine a9/rr/075

		// MERGE v10: ??? commentato codice sottostante ??? non ha senso!!!
//		String espiazionePenaIstitutoDetenzione = "";
//		if (!this
//				.isRequestParameterNullObj(ICostantiMisuraCautelare.ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO)
//				&& !"".equalsIgnoreCase(ICostantiMisuraCautelare.ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO)) {
//			espiazionePenaIstitutoDetenzione = this
//					.getRequestStringParameter(ICostantiMisuraCautelare.ESPIAZIONE_PENA_ISTITUTO_DETENZIONE_ALTRO_LUOGO);
//		}

//		String descEspiazionePenaIstitutoDetenzione = "";
//		if (espiazionePenaIstitutoDetenzione.equals("0")) {
//			espiazionePenaIstitutoDetenzione = "1";
//			descEspiazionePenaIstitutoDetenzione = "Istituto";
//		} else if (espiazionePenaIstitutoDetenzione.equals("1")) {
//			espiazionePenaIstitutoDetenzione = "1";
//			descEspiazionePenaIstitutoDetenzione = "no Istituto";
//		}

		return PG_RICERCAMISURACAUTELARE;
	}

}