package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.calendar.model.CalendarModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.CalendarUtil;
import siap.sico.util.SICOLookupRemote;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.misurasicurezza.model.PeriodoEsecuzioneMisuraModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActInserisciSospensioneMisuraSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {

	public String processRequest() throws F3BException {

		// 83 tipo scadenzario inizio o ripresa
		// 84 tipo scadenzario sospensione
		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione

		// 29/05/2008 Controllo Esistenza Ufficio.
		// Modifica del 12/09/2013 mev "Revisione Misure Sicurezza SIUS"
		// Eliminata obbligatorietà ufficio pertanto il metodo
		// getUfficioByCodTipoUffDescrComune viene invocato solo se l'ufficio è diverso da "-"
		IUfficio lCtrlUfficio = SICOLookupRemote.getUfficioRemote();
		// UfficioModel lUfficio = null;
		String codTipoUfficio = this.getParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_UFFICIO);
		if (codTipoUfficio != null && !codTipoUfficio.equals("-")) {
			if (!this.isRequestParameterNullObj(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_UFFICIO)) {
				/* lUfficio = */lCtrlUfficio.getUfficioByCodTipoUffDescrComune(
						this.getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_TIPO_UFFICIO),
						this.getRequestStringParameter(ICostantiSiusMisuraSicurezza.CAMPO_COD_LUOGO_AUTORITA)
								.toUpperCase());
			}
		}

		// recupero ID fascicolo
		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		lPerMod = recuperoDatiMaschera(lPerMod);
		BigDecimal lIdFasSius = recuperoIdFascicolo();
		BigDecimal lIdFasSiep = recuperoIdFascicoloSiep();
		lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);
		lPerMod.setFasSieIdFascicoloSiep(lIdFasSiep);

		// setto i dati dell'utente che effettua l'inserimento
		lPerMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPerMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lPerMod.setDataInserimento(DateUtils.getSysDate());

		// ricerca record misura sicurezza per controllo data termine
		// attuale ed eventualmente aggiunta dei giorni da recuperare
		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);

		// calcola da data scadenza del periodo qualora siano stati digitati i quantum
		// calcola la nuova data termine attuale qualora ci siano de gg da recuperare
		// salva il tutto nel modello comune lPerEse
		PeriodoEsecuzioneMisuraModel lPerEse = calcoloDataScadenza(lPerMod, lEMSModel);
		lPerMod = lPerEse.getPeriodoAltraMisura();

		// calcolo dei quantum misura sicurezza espiata
		//
		CalendarModel lCalMode = new CalendarModel();
		lCalMode = quantumEspiata(lPerMod, lEMSModel);

		// calcolo dei quantum misura sicurezza totale
		CalendarModel lCalModr = new CalendarModel();
		lCalModr = quantumTotale(lEMSModel);

		lEMSModel = lPerEse.getEsecuzioneMisuraSicurezza();
		// per completare il calcolo della misura residua da espiare
		// sottrarre quella gia espiata
		CalendarUtil lCalUtil = new CalendarUtil();
		CalendarModel lCalModre = new CalendarModel();
		lCalModre = lCalUtil.sottraiGiorni(lCalModr, lCalMode);

		lPerMod = riempioquantumEspiataeResidua(lPerMod, lCalMode, lCalModre);

		// se devo aggiornare il record EMS setto i parametri dell'aggiornante
		if (lPerMod.getDaRecuperare().equals("1")) {
			lEMSModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
			lEMSModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
			lEMSModel.setDataAggiornamento(DateUtils.getSysDate());
		}

		// Recupera il controller ed effettua l'inserimento periodo, modifica EMS e Scadenzario
		IPeriodoAltraMisura lCtrl = SIUSLookupRemote.getPeriodoAltraMisuraRemote();

		// controllo che lo scadenzario 84 gia esiste
		IScadenzarioSius lCtrlSca = SIUSLookupRemote.getScadenzarioRemote();
		ScadenzarioSiusModel lScaMod84 = lCtrlSca
				.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "84");

		String flScaMod84 = "";
		if (lScaMod84 == null) {
			flScaMod84 = "SI"; // se lo scadenzario 84 non esiste mi segno che devo crearlo
			lScaMod84 = new ScadenzarioSiusModel();
		}

		/*****************************************************************************
		 * 5 Inserimento Periodo Altra Misura e Modifica EMS inserimento scadenzario eventuale modifica altro
		 * scadenzario
		 *****************************************************************************/
		// inserire la sospensione se lo scadenzario 84 termine differimento non esiste devo crearlo
		// se non esistono gg da recuperare non è necessario modificare lo scadenzario
		// di tipo 83, ossia "termine misura sicurezza", quindi passo il relativo
		// model a null

		// ricerco in ogni caso lo scadenzario 83 per recuperare l'id evento
		// avvaloro la Data Termine Attuale qualora ci fossere da aggiungere i gg da recuperare
		ScadenzarioSiusModel lScaMod83 = lCtrlSca
				.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "83");
		lScaMod83.setDataFineScadenza(lEMSModel.getDataTermineAttuale());

		// riempio i campi dello scadenzario 84 con l'id evento e i valori del periodo
		lScaMod84.setEveIdEvento(lScaMod83.getEveIdEvento());
		lScaMod84 = riempioScadenzario(lPerMod, lScaMod84, "84");

		if (lScaMod84.getDataFineScadenza() == null)
			lScaMod84.setDataFineScadenza(lEMSModel.getDataTermineAttuale());

		// se non esistono gg da recuperare lo scadenzario 83 non va modificato quindi lo metto a null
		if (!lPerMod.getDaRecuperare().equals("1"))
			lScaMod83 = null;

		if (flScaMod84.equals("SI")) {
			// inserisco il periodo, inserisco lo scadenzario 84
			// eventualmente aggiorno la data termine attuale sull'esecuzione e sullo scadenzario 83
			lPerMod = lCtrl.ExInserisciPeriodoAltraMisura(lPerMod, lScaMod84, lEMSModel, lScaMod83);
		} else {
			// inserisco il periodo, modifico lo scadenzario 84
			// eventualmente aggiorno la data termine attuale sull'esecuzione e sullo scadenzario 83
			lPerMod = lCtrl.ExInserisciPeriodoAltraMisura(lPerMod, lEMSModel, lScaMod84, lScaMod83);
		}

		setRequestAttribute("lCalMode", lCalMode);
		setRequestAttribute("lCalModre", lCalModre);
		setRequestAttribute("lEMSModel", lEMSModel);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sius.misurasicurezza.action.ActLoadDettaglioSospensioneMisuraSicurezzaUDS";
		lPage += "&" + CAMPO_ID_PERIODO_ALTRA_MISURA + "=" + lPerMod.getIdPeriodoAltraMisura().toString();
		return lPage;
	}

}