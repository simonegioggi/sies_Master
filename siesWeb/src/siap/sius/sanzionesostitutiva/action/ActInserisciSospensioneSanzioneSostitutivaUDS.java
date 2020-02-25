package siap.sius.sanzionesostitutiva.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.sanzionesostitutiva.model.PeriodoEsecuzioneSanzioneModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciSospensioneSanzioneSostitutivaUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws F3BException {

		// 80 tipo scadenzario inizio o ripresa
		// 81 tipo scadenzario sospensione
		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione

		// 29/05/2008 Controllo Esistenza Ufficio.
		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
		// UfficioModel lUfficio = null;
		if (!this.isRequestParameterNullObj(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO)) {
			/* lUfficio = */lCtrlUfficio.getUfficioByCodTipoUffDescrComune(
					this.getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_COD_TIPO_UFFICIO),
					this.getRequestStringParameter(ICostantiSanzioneSostitutiva.CAMPO_COD_LUOGO_AUTORITA)
							.toUpperCase());
		}

		// recupero ID fascicolo
		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		lPerMod = recuperoDatiMaschera(lPerMod);
		BigDecimal lIdFasSius = recuperoIdFascicolo();
		BigDecimal lIdFasSiep = recuperoIdFascicoloSiep();
		lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);
		lPerMod.setFasSieIdFascicoloSiep(lIdFasSiep);

		// setto i dati dell'utente che effettua l'inserimento
		lPerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPerMod.setDataInserimento(DateUtils.getSysDate());

		// ricerca record sanzione sostitutiva per controllo data termine
		// attuale ed eventualmente aggiunta dei giorni da recuperare
		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);

		// calcola da data scadenza del periodo qualora siano stati digitati i quantum
		// calcola la nuova data termine attuale qualora ci siano de gg da recuperare
		// salva il tutto nel modello comune lPerEse
		PeriodoEsecuzioneSanzioneModel lPerEse = calcoloDataScadenza(lPerMod, lESSModel);
		lPerMod = lPerEse.getPeriodoAltraSanzione();

		// calcolo dei quantum sanzione sostitutiva espiata
		//
		CalendarModel lCalMode = new CalendarModel();
		lCalMode = quantumEspiata(lPerMod, lESSModel);

		// calcolo dei quantum sanzione sostitutiva totale
		CalendarModel lCalModr = new CalendarModel();
		lCalModr = quantumTotale(lESSModel);

		lESSModel = lPerEse.getEsecuzioneSanzioneSostitutiva();
		// per completare il calcolo della sanzione residua da espiare
		// sottrarre quella gia espiata
		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalModre = new CalendarModel();
		lCalModre = lCalUtil.sottraiGiorni(lCalModr, lCalMode);

		lPerMod = riempioquantumEspiataeResidua(lPerMod, lCalMode, lCalModre);

		// se devo aggiornare il record ESS setto i parametri dell'aggiornante
		if (lPerMod.getDaRecuperare().equals("1")) {
			lESSModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lESSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lESSModel.setDataAggiornamento(DateUtils.getSysDate());
		}

		// Recupera il controller ed effettua l'inserimento periodo, modifica ESS e Scadenzario
		IPeriodoAltraSanzione lCtrl = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();

		// controllo che lo scadenzario 81 gia esiste
		IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
		ScadenzarioSiusModel lScaMod81 = lCtrlSca
				.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "81");

		String flScaMod81 = "";
		if (lScaMod81 == null) {
			flScaMod81 = "SI"; // se lo scadenzario 81 non esiste mi segno che devo crearlo
			lScaMod81 = new ScadenzarioSiusModel();
		}

		/*****************************************************************************
		 * 5 Inserimento Periodo Altra Sanzione e Modifica ESS inserimento scadenzario eventuale modifica
		 * altro scadenzario
		 *****************************************************************************/
		// inserire la sospensione se lo scadenzario 81 termine differimento non esiste devo crearlo
		// se non esistono gg da recuperare non è necessario modificare lo scadenzario
		// di tipo 80, ossia "termine sanzione sostitutiva", quindi passo il relativo
		// model a null

		// ricerco in ogni caso lo scadenzario 80 per recuperare l'id evento
		// avvaloro la Data Termine Attuale qualora ci fossere da aggiungere i gg da recuperare
		ScadenzarioSiusModel lScaMod80 = lCtrlSca
				.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "80");
		lScaMod80.setDataFineScadenza(lESSModel.getDataTermineAttuale());

		// riempio i campi dello scadenzario 81 con l'id evento e i valori del periodo
		lScaMod81.setEveIdEvento(lScaMod80.getEveIdEvento());
		lScaMod81 = riempioScadenzario(lPerMod, lScaMod81, "81");

		if (lScaMod81.getDataFineScadenza() == null)
			lScaMod81.setDataFineScadenza(lESSModel.getDataTermineAttuale());

		// se non esistono gg da recuperare lo scadenzario 80 non va modificato quindi lo metto a null
		if (!lPerMod.getDaRecuperare().equals("1"))
			lScaMod80 = null;

		if (flScaMod81.equals("SI")) {
			// inserisco il periodo, inserisco lo scadenzario 81
			// eventualmente aggiorno la data termine attuale sull'esecuzione e sullo scadenzario 80
			lPerMod = lCtrl.ExInserisciPeriodoAltraSanzione(lPerMod, lScaMod81, lESSModel, lScaMod80);
		} else {
			// inserisco il periodo, modifico lo scadenzario 81
			// eventualmente aggiorno la data termine attuale sull'esecuzione e sullo scadenzario 80
			lPerMod = lCtrl.ExInserisciPeriodoAltraSanzione(lPerMod, lESSModel, lScaMod81, lScaMod80);
		}

		setRequestAttribute("lCalMode", lCalMode);
		setRequestAttribute("lCalModre", lCalModre);
		setRequestAttribute("lESSModel", lESSModel);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.sanzionesostitutiva.action.ActLoadDettaglioSospensioneSanzioneSostitutivaUDS";
		lPage += "&" + CAMPO_ID_PERIODO_ALTRA_SANZIONE + "=" + lPerMod.getIdPeriodoAltraSanzione().toString();
		return lPage;
	}

}