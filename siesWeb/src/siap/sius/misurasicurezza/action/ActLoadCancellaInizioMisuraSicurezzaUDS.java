package siap.sius.misurasicurezza.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import siap.sico.evento.controller.IEvento;
import siap.sico.util.SICOLookupRemote;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.misurasicurezza.controller.IPeriodoAltraMisura;
import siap.sius.misurasicurezza.model.PeriodoAltraMisuraModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadCancellaInizioMisuraSicurezzaUDS extends ActMisuraSicurezza
		implements ICostantiSiusMisuraSicurezza {
	/*****************************************************************************
	 * Azione per la cancellazione dei dati.
	 *
	 * @return PG_MESSAGE di avvenuta cancellazione
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Recupera la key del record da Cancellare
		BigDecimal lIdPeriodoAltraMisura = getRequestBigDecimalParameter(CAMPO_ID_PERIODO_ALTRA_MISURA);
		PeriodoAltraMisuraModel lPerMod = new PeriodoAltraMisuraModel();
		lPerMod.setIdPeriodoAltraMisura(lIdPeriodoAltraMisura);

		// se ci sono giorni da recuperare occorre sottrarli alla data_termine_attuale
		IPeriodoAltraMisura lCtrl1 = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		lPerMod = lCtrl1.ExRicercaPeriodoAltraMisuraById(lIdPeriodoAltraMisura);

		// 15/05/2008 Controllo EVENTO. Si permette la cancellazione senza alcun messaggio, se l'evento è
		// annullato.
		// In caso di Decreto/Ordinanza attiva si consente la scelta se cancellare o meno.
		if (lPerMod.getEveIdEvento() != null) {
			// Preleva l'evento.
			IEvento lCtrl = SICOLookupRemote.getEventoRemote();
			/* EventoModel lEveMod = */lCtrl.ExRicercaEventoByKey(lPerMod.getEveIdEvento());

			// controllo evento
			/*
			 * if ( lPerMod.getEveIdEvento()!=null && lPerMod.getFlagMotivo().equals("03")) { if
			 * (lEveMod.getFlagDocumentoRegistrato().compareTo("A")!=0) { throw new
			 * SIUSException(SIUSException.USER_MESSAGE,
			 * "Attenzione. Periodo agganciato ad un decreto o ordinanza validata. Impossibile cancellare"); }
			 * }
			 */
		}

		// recupero ID fascicolo
		// ATTENZIONE in sessione potrebbe esserci un fascicolo figlio
		/**
		 * BigDecimal lIdFasSius = recuperoIdFascicolo(); lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);
		 **/
		BigDecimal lIdFasSius = lPerMod.getFasSiuIdFascicoloSius();

		// sottrai giorni da recuperare a data_termine_attuale
		// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFasSius);
		if (lPerMod.getDaRecuperare() != null && lPerMod.getDaRecuperare().equals("1")) {
			int GG = 0, MM = 0, AA = 0;
			if (lPerMod.getDaRecuperareGG() != null)
				GG = lPerMod.getDaRecuperareGG().intValue();
			if (lPerMod.getDaRecuperareMM() != null)
				MM = lPerMod.getDaRecuperareMM().intValue();
			if (lPerMod.getDaRecuperareAA() != null)
				AA = lPerMod.getDaRecuperareAA().intValue();
			int giorniDaRecuperare = DateUtils.getIntervallo(DateUtils.getDate(0, 0, 0),
					DateUtils.getDate(AA, MM, GG));

			Date lDataTermineAttuale = DateUtils.moveDateTo(lEMSModel.getDataTermineAttuale(),
					Calendar.DAY_OF_MONTH, giorniDaRecuperare);

			lEMSModel.setDataTermineAttuale(lDataTermineAttuale);
		}

		// ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
		IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		// lScaMod = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo
		// (lPerMod.getFasSiuIdFascicoloSius(),lTipoSca);

		// periodo da cancellare
		BigDecimal IdPerCanc = lPerMod.getIdPeriodoAltraMisura();
		String FlagMotivo = lPerMod.getFlagMotivo();

		// 83 tipo scadenzario inizio o ripresa
		// 84 tipo scadenzario sospensione
		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione

		// periodi per riaggiornare lo scadenzario
		IPeriodoAltraMisura lCtrllst = SIUSLookupRemote.getPeriodoAltraMisuraRemote();
		List lListaMisureSius = lCtrllst.ExRicercaMisuraSicurezzaByIdFascicolo(lIdFasSius);
		ScadenzarioSiusModel lScaMod83 = null;

		if (FlagMotivo.equals("01")) {
			// cancello periodo inizio quindi anche scadenzario ed evento
			// annullo le data dell'esecuzione
			lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(0);
			lScaMod83 = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),
					"83");
			lEMSModel.setDataInizioMisura(null);
			lEMSModel.setDataTermineIniziale(null);
			lEMSModel.setDataTermineAttuale(null);
			lCtrl1.ExCancellaPeriodoAltraMisura(lScaMod83, lPerMod, lEMSModel);
		}

		if (FlagMotivo.equals("02")) {
			/*****************************************************************************
			 * 7 cancella Periodo Altra Misura (ripresa misura) Modifica Ems eventuale cancella scadenzario
			 * eventuale modifiche di altri 2 scadenzari
			 ****************************************************************************/
			// cancello ripresa
			// ripristino valori nello scadenzario 83 (termine misura sicurezza)
			// ripristino i valori dell' Esecuzione (Data Termine Attuale

			// recupero il precedente periodo inizio o ripresa e salvo la data
			lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 3);
			Date lDataTermineIniziale = lPerMod.getDataScadenza();

			// recupero la precedente sospensione
			// se esistevano dei gg da recupare li sommo
			// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
			lEMSModel.setDataTermineAttuale(lDataTermineIniziale);
			lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 2);
			if (lPerMod.getDaRecuperare() != null && lPerMod.getDaRecuperare().equals("1")) {
				int GG = 0, MM = 0, AA = 0;
				if (lPerMod.getDaRecuperareGG() != null)
					GG = lPerMod.getDaRecuperareGG().intValue();
				if (lPerMod.getDaRecuperareMM() != null)
					MM = lPerMod.getDaRecuperareMM().intValue();
				if (lPerMod.getDaRecuperareAA() != null)
					AA = lPerMod.getDaRecuperareAA().intValue();
				int giorniDaRecuperare = DateUtils.getIntervallo(DateUtils.getDate(0, 0, 0),
						DateUtils.getDate(AA, MM, GG));

				Date lDataTermineAttuale = DateUtils.moveDateTo(lEMSModel.getDataTermineAttuale(),
						Calendar.DAY_OF_MONTH, giorniDaRecuperare);

				// ho ripristinato la data termina attuale dell'esecuzione
				lEMSModel.setDataTermineAttuale(lDataTermineAttuale);
			}

			lScaMod83 = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),
					"83");

			// ho ripristinato la data termine attuale dello scadenzario
			lScaMod83.setDataFineScadenza(lEMSModel.getDataTermineAttuale());

			// cancello periodo
			// modifico lo scadenzario 83 -> ripristino la Data Termine Attuale
			// modifico EMS -> ripristino la Data Termine Attuale
			lCtrl1.ExCancellaPeriodoAltraMisura(null, null, lScaMod83, IdPerCanc, lEMSModel);
		}

		ScadenzarioSiusModel lScaMod84c = null;
		ScadenzarioSiusModel lScaMod84m = null;

		if (FlagMotivo.equals("03")) {
			/*****************************************************************************
			 * 7 cancella Periodo Altra Misura (ripresa misura) Modifica Ems eventuale cancella scadenzario
			 * eventuale modifiche di altri 2 scadenzari
			 ****************************************************************************/
			if (lListaMisureSius.size() == 2) {
				// se cancello la prima sospensione e quindi l'unica:
				// (termine differimento) scadenzario 84 -> cancello
				// (termine misura sicurezza) scadenzario 83 -> ripristino la Data Termine Attuale
				// qualora fossero stati sommati dei gg da recuperare
				// modifico EMS -> ripristino la Data Termine Attuale
				lScaMod83 = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "83");
				lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(0);
				lScaMod83.setDataFineScadenza(lPerMod.getDataScadenza());
				lEMSModel.setDataTermineAttuale(lPerMod.getDataScadenza());
				lScaMod84c = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "84");
				lCtrl1.ExCancellaPeriodoAltraMisura(lScaMod84c, null, lScaMod83, IdPerCanc, lEMSModel);

			} else {
				// se cancello una sospensione:
				// (termine differimento) scadenzario 84 -> ripristino al precedente
				// (termine misura sicurezza) scadenzario 83 -> ripristino la Data Termine Attuale
				// qualora fossero stati sommati dei gg da recuperare

				lScaMod83 = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "83");
				lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 2);
				lScaMod83 = riempioScadenzario(lPerMod, lScaMod83, "83");
				lScaMod83.setDataFineScadenza(lEMSModel.getDataTermineAttuale());

				lScaMod84m = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "84");
				lPerMod = (PeriodoAltraMisuraModel) lListaMisureSius.get(lListaMisureSius.size() - 3);
				lScaMod84m = riempioScadenzario(lPerMod, lScaMod84m, "84");
				lCtrl1.ExCancellaPeriodoAltraMisura(null, lScaMod84m, lScaMod83, IdPerCanc, lEMSModel);
			}
		}

		// ===========================================================
		// Restituisce la pagina di Conferma avvenuta Cancellazione.
		// ===========================================================
		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Cancellazione effettuata");
		// Specificare eventualmente la jump page dove verrà ridirezionata la
		// PG_MESSAGE quando viene premuto OK. Se non viene attualizzato il
		// parametro GOTO_PAGE, la PG_MESSAGE effettua un history.go(-1)
		// n.b. il path da specificare nel parametro GOTO_PAGE deve essere completo
		// della root_dir es /siap/frame.htm
		setRequestAttribute(IWebConstants.GOTO_PAGE, IWebConstants.ROOT_DIR);

		// Prepara la "pagina" di destinAction
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);

		// se ho effettuato una cancellazione dalla lista misure torno al dettaglio del fascicolo
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS)) {
			lRedirigi.setAction("siap.sius.fascicolo.action.ActLoadDettaglioFascicolo&"
					+ ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS + "="
					+ getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS));
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE_POPUP; // restituisce la jsp di VIEW
		} else {
			// se ho effettuato una cancellazione dal dettaglio della validazione inizio/ripresa
			// torno all'inserimento della inizio/ripresa
			if (!FlagMotivo.equals("03")) {
				lRedirigi.setAction(
						"siap.sius.misurasicurezza.action.ActLoadInserisciInizioMisuraSicurezzaUDS");
			} else {
				// se ho effettuato una cancellazione dal dettaglio della sospensione
				// torno all'inserimento della sospensione
				lRedirigi.setAction(
						"siap.sius.misurasicurezza.action.ActLoadInserisciSospensioneMisuraSicurezzaUDS");
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}
	}

}