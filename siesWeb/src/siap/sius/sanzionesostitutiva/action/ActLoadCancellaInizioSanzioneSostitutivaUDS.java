package siap.sius.sanzionesostitutiva.action;

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
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.sanzionesostitutiva.model.PeriodoAltraSanzioneModel;
import siap.sius.scadenzario.controller.IScadenzarioSius;
import siap.sius.scadenzario.model.ScadenzarioSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadCancellaInizioSanzioneSostitutivaUDS extends ActSanzioneSostitutiva
		implements ICostantiSanzioneSostitutiva {

	/*****************************************************************************
	 * Azione per la cancellazione dei dati.
	 *
	 * @return PG_MESSAGE di avvenuta cancellazione
	 * @throws F3BException
	 *****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Recupera la key del record da Cancellare
		BigDecimal lIdPeriodoAltraSanzione = getRequestBigDecimalParameter(CAMPO_ID_PERIODO_ALTRA_SANZIONE);
		PeriodoAltraSanzioneModel lPerMod = new PeriodoAltraSanzioneModel();
		lPerMod.setIdPeriodoAltraSanzione(lIdPeriodoAltraSanzione);

		// se ci sono giorni da recuperare occorre sottrarli alla data_termine_attuale
		IPeriodoAltraSanzione lCtrl1 = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		lPerMod = lCtrl1.ExRicercaPeriodoAltraSanzioneById(lIdPeriodoAltraSanzione);

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
		BigDecimal lIdFasSius = recuperoIdFascicolo();
		lPerMod.setFasSiuIdFascicoloSius(lIdFasSius);

		// sottrai giorni da recuperare a data_termine_attuale
		// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();
		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFasSius);
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

			Date lDataTermineAttuale = DateUtils.moveDateTo(lESSModel.getDataTermineAttuale(),
					Calendar.DAY_OF_MONTH, giorniDaRecuperare);

			lESSModel.setDataTermineAttuale(lDataTermineAttuale);
		}

		// ScadenzarioSiusModel lScaMod = new ScadenzarioSiusModel();
		IScadenzarioSius lCtrl = SIUSLookupRemote.getScadenzarioRemote();
		// lScaMod = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo
		// (lPerMod.getFasSiuIdFascicoloSius(),lTipoSca);

		// periodo da cancellare
		BigDecimal IdPerCanc = lPerMod.getIdPeriodoAltraSanzione();
		String FlagMotivo = lPerMod.getFlagMotivo();

		// 80 tipo scadenzario inizio o ripresa
		// 81 tipo scadenzario sospensione
		// 01 Flag Motivo Inizio
		// 02 Flag Motivo Ripresa
		// 03 Flag Motivo Sospensione

		// periodi per riaggiornare lo scadenzario
		IPeriodoAltraSanzione lCtrllst = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		List lListaSanzioniSius = lCtrllst.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFasSius);
		ScadenzarioSiusModel lScaMod80 = null;

		if (FlagMotivo.equals("01")) {
			// cancello periodo inizio quindi anche scadenzario ed evento
			// annullo le data dell'esecuzione
			lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(0);
			lScaMod80 = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),
					"80");
			lESSModel.setDataInizioSanzione(null);
			lESSModel.setDataTermineIniziale(null);
			lESSModel.setDataTermineAttuale(null);
			lCtrl1.ExCancellaPeriodoAltraSanzione(lScaMod80, lPerMod, lESSModel);
		}

		if (FlagMotivo.equals("02")) {
			/*****************************************************************************
			 * 7 cancella Periodo Altra Sanzione (ripresa sanzione) Modifica Ess eventuale cancella
			 * scadenzario eventuale modifiche di altri 2 scadenzari
			 ****************************************************************************/
			// cancello ripresa
			// ripristino valori nello scadenzario 80 (termine sanzione sostitutiva)
			// ripristino i valori dell' Esecuzione (Data Termine Attuale

			// recupero il precedente periodo inizio o ripresa e salvo la data
			lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 3);
			Date lDataTermineIniziale = lPerMod.getDataScadenza();

			// recupero la precedente sospensione
			// se esistevano dei gg da recupare li sommo
			// 12-05-2008 Modifica Periodo da Recuperare in GG, MM, AA
			lESSModel.setDataTermineAttuale(lDataTermineIniziale);
			lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 2);
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

				Date lDataTermineAttuale = DateUtils.moveDateTo(lESSModel.getDataTermineAttuale(),
						Calendar.DAY_OF_MONTH, giorniDaRecuperare);

				// ho ripristinato la data termina attuale dell'esecuzione
				lESSModel.setDataTermineAttuale(lDataTermineAttuale);
			}

			lScaMod80 = lCtrl.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(),
					"80");

			// ho ripristinato la data termine attuale dello scadenzario
			lScaMod80.setDataFineScadenza(lESSModel.getDataTermineAttuale());

			// cancello periodo
			// modifico lo scadenzario 80 -> ripristino la Data Termine Attuale
			// modifico ESS -> ripristino la Data Termine Attuale
			lCtrl1.ExCancellaPeriodoAltraSanzione(null, null, lScaMod80, IdPerCanc, lESSModel);
		}

		ScadenzarioSiusModel lScaMod81c = null;
		ScadenzarioSiusModel lScaMod81m = null;

		if (FlagMotivo.equals("03")) {
			/*****************************************************************************
			 * 7 cancella Periodo Altra Sanzione (ripresa sanzione) Modifica Ess eventuale cancella
			 * scadenzario eventuale modifiche di altri 2 scadenzari
			 ****************************************************************************/
			if (lListaSanzioniSius.size() == 2) {
				// se cancello la prima sospensione e quindi l'unica:
				// (termine differimento) scadenzario 81 -> cancello
				// (termine sanzione sostitutiva) scadenzario 80 -> ripristino la Data Termine Attuale
				// qualora fossero stati sommati dei gg da recuperare
				// modifico ESS -> ripristino la Data Termine Attuale
				lScaMod80 = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "80");
				lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(0);
				lScaMod80.setDataFineScadenza(lPerMod.getDataScadenza());
				lESSModel.setDataTermineAttuale(lPerMod.getDataScadenza());
				lScaMod81c = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "81");
				lCtrl1.ExCancellaPeriodoAltraSanzione(lScaMod81c, null, lScaMod80, IdPerCanc, lESSModel);

			} else {
				// se cancello una sospensione:
				// (termine differimento) scadenzario 81 -> ripristino al precedente
				// (termine sanzione sostitutiva) scadenzario 80 -> ripristino la Data Termine Attuale
				// qualora fossero stati sommati dei gg da recuperare

				lScaMod80 = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "80");
				lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 2);
				lScaMod80 = riempioScadenzario(lPerMod, lScaMod80, "80");
				lScaMod80.setDataFineScadenza(lESSModel.getDataTermineAttuale());

				lScaMod81m = lCtrl
						.ExRicercaScadenzarioSiusByIdFascicoloTipo(lPerMod.getFasSiuIdFascicoloSius(), "81");
				lPerMod = (PeriodoAltraSanzioneModel) lListaSanzioniSius.get(lListaSanzioniSius.size() - 3);
				lScaMod81m = riempioScadenzario(lPerMod, lScaMod81m, "81");
				lCtrl1.ExCancellaPeriodoAltraSanzione(null, lScaMod81m, lScaMod80, IdPerCanc, lESSModel);
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

		// se ho effettuato una cancellazione dalla lista sanzioni torno al dettaglio del fascicolo
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
						"siap.sius.sanzionesostitutiva.action.ActLoadInserisciInizioSanzioneSostitutivaUDS");
			} else {
				// se ho effettuato una cancellazione dal dettaglio della sospensione
				// torno all'inserimento della sospensione
				lRedirigi.setAction(
						"siap.sius.sanzionesostitutiva.action.ActLoadInserisciSospensioneSanzioneSostitutivaUDS");
			}
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);
			return IWebConstants.PG_MESSAGE; // restituisce la jsp di VIEW
		}
	}

}