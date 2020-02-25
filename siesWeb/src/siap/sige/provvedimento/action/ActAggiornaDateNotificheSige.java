package siap.sige.provvedimento.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.controller.INotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.scadenzario.controller.IScadenzarioSige;
import siap.sige.scadenzario.model.ScadenzarioSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActAggiornaDateNotificheSige
 * </p>
 * </p> La Action esegue l'aggiornamento delle date di avvenuta notifica per più notifiche.
 * <p>
 * Copyright: Bull Italia S.p.A.Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull Italia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActAggiornaDateNotificheSige extends ActionSiap implements ICostantiProvvedimentoSige {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private FascicoloSigeEstesoModel lFasSigeEstMod = null;

	// Valorizzazione del record da inserire nello scadenzario
	private ScadenzarioSigeModel valorizzaScadenzarioSigeInserimento(Date adata) throws F3BException {
		ScadenzarioSigeModel lScad = null;

		lScad = new ScadenzarioSigeModel();
		lScad.setCodTipoScadenzario(SCADENZARIO_SIGE_IRREVOCABILITA);
		lScad.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lScad.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		// 02/08/2004 Aggiunto EveIdEvento.
		lScad.setEveIdEvento(getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO));

		if (adata != null) {
			lScad.setDataInizioScadenza(adata);
			lScad.setDataFineScadenza(DateUtils.moveDateTo(adata, java.util.Calendar.DAY_OF_MONTH, 15));
		} else {
			// lScad.setDataInizioScadenza(null);
			lScad.setDataFineScadenza(null);
		}
		lScad.setDataInserimento(DateUtils.getSysDate());
		lScad.setFlagVisto("N");
		lScad.setFasIdFascicoloSige(lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige());
		return lScad;
	}

	// Valorizzazione del record da aggiornare nello scadenzario
	private ScadenzarioSigeModel valorizzaScadenzarioSigeAggiornamento(ScadenzarioSigeModel aScad, Date adata)
			throws F3BException {

		ScadenzarioSigeModel lScad = null;

		lScad = new ScadenzarioSigeModel(aScad);
		lScad.setCodTipoScadenzario(SCADENZARIO_SIGE_IRREVOCABILITA);
		lScad.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lScad.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());

		if (adata != null) {
			lScad.setDataInizioScadenza(adata);
			lScad.setDataFineScadenza(DateUtils.moveDateTo(adata, java.util.Calendar.DAY_OF_MONTH, 15));
		} else {
			// lScad.setDataInizioScadenza(null);
			lScad.setDataFineScadenza(null);
		}
		lScad.setDataAggiornamento(DateUtils.getSysDate());
		lScad.setFlagVisto("N");
		lScad.setDataVisto(null);
		lScad.setFasIdFascicoloSige(lFasSigeEstMod.getFascicoloSige().getIdFascicoloSige());
		return lScad;
	}

	public String processRequest() throws Exception {
		ScadenzarioSigeModel lScad = null;

		NotificaModel[] lListaNot;

		// Lista degli IDNotifica
		String[] lListaID;

		// Lista delle date da aggiornare nelle Notifiche
		String[] lListaAnno;
		String[] lListaMese;
		String[] lListaGiorno;

		BigDecimal IdEve = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// pagina view
		// String lPage = new String();
		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.sige.provvedimento.action.ActDettaglioNotificheEventoSige&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + IdEve;

		int i; // indice dell'array di Notifiche
		int ii; // indice dell'array delle solo Notifiche da aggiornare
		int lung = 0; // numero complessivo di Notifiche
		int lNumRec = 0; // numero delle Notifiche da aggiornare

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.warn("-------- ActAggiornaDateNotifiche: inizio");
		// Costruzione elenco ID dei record da aggiornare
		lListaID = getRequestStringParameters(ICostantiNotifica.CAMPO_ID_NOTIFICA);
		lung = lListaID.length;

		// Costruzione elenco date
		lListaAnno = getRequestStringParameters(ICostantiNotifica.CAMPO_ANNO_DATA_AVVENUTA_NOTIFICA);
		lListaMese = getRequestStringParameters(ICostantiNotifica.CAMPO_MESE_DATA_AVVENUTA_NOTIFICA);
		lListaGiorno = getRequestStringParameters(ICostantiNotifica.CAMPO_GIORNO_DATA_AVVENUTA_NOTIFICA);

		// Calcolo del numero di record da aggiornare
		for (i = 0; i < lung; i++) {
			if (!lListaID[i].equals(""))
				lNumRec++;
		}

		if (lNumRec > 0) {
			// Si istanzia l'array atto a contenere i record da aggiornare
			lListaNot = new NotificaModel[lNumRec];

			// costruzione elenco di Notifiche da aggiornare
			ii = 0;
			for (i = 0; i < lung; i++) {
				// i record da aggiornare sono solo quelli per cui sono presenti gli ID
				if (!lListaID[i].equals("")) {
					lListaNot[ii] = new NotificaModel();
					lListaNot[ii].setCodEsito(NOTIFICA_SIGE_ESEGUITA);
					lListaNot[ii].setDataAvvenutaNotifica(DateUtils.getDate(lListaAnno[i], lListaMese[i],
							lListaGiorno[i]));
					lListaNot[ii].setCodiceOperatoreAggiornamento(getCodUtenteConnesso());
					lListaNot[ii].setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
					lListaNot[ii].setDataAggiornamento(DateUtils.getSysDate());
					lListaNot[ii].setIdNotifica(new BigDecimal(lListaID[i]));
					ii++;
				}
			}
			// Viene effettuato l'aggiornamento
			INotifica lCtrlNt = SIEPLookupRemote.getNotificaRemote();
			lCtrlNt.ExAggiornaDateNotifica(lListaNot);

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.warn("-------- ActAggiornaDateNotificheSige aggiornati ->: " + lNumRec);

			// Ricerca Provvedimento per controllare motivo
			IEvento mCtrl = SICOLookupRemote.getEventoRemote();
			EventoModel lEvento = mCtrl.ExRicercaEventoByKey(IdEve);

			// Il Decreto di Fissazione Udienza non richiede SCADENZARIO
			// NUOVA INFRASTRUTTURA: il CodMotivo può essere NULL!!!
			if (lEvento.getCodMotivo() != null && !(MOTIVO_FISSAZIONE_UDIENZA.equals(lEvento.getCodMotivo()))) {
				// si risale al fascicolo in sessione
				lFasSigeEstMod = (FascicoloSigeEstesoModel) this.getSessionAttribute("FascicoloSigeEsteso");

				// ricerca del record di Scadenzario
				IScadenzarioSige lCtrlSc = SIGELookupRemote.getScadenzarioSigeRemote();
				lScad = lCtrlSc.ExRicercaScadenzarioSigeByIdFascicoloTipo(lFasSigeEstMod.getFascicoloSige()
						.getIdFascicoloSige(), SCADENZARIO_SIGE_IRREVOCABILITA);

				// Se sono state notificate tutte (le notifiche di tipo N) si inserisce
				// un record nello SCADENZARIO
				if (lCtrlNt.ExSonoNotificateSige(IdEve)) {
					// data di notifica
					Date lData = lCtrlNt.ExRicercaDataNotificaSige(IdEve);
					if (lData != null) {

						// Se non esiste lo Scadenzario viene inserito
						if (lScad == null) {
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.warn(
									"-------- ActAggiornaDateNotificheSige: inserimento scadenziario");
							lScad = valorizzaScadenzarioSigeInserimento(lData);
							// inserimento
							lCtrlSc.ExInserisciScadenzarioSige(lScad);
						} else {
							// Se lo Scadenzario esiste va aggiornato
							// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
							siesLogger.warn(
									"-------- ActAggiornaDateNotificheSige: aggiornamento scadenziario");
							lScad = valorizzaScadenzarioSigeAggiornamento(lScad, lData);
							lCtrlSc.ExModificaScadenzario(lScad);
						}
					}
				} else // Non sono tutte notificate
				{
					// Se esiste lo Scadenzario occorre annullare le date
					if (lScad != null) {
						// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
						siesLogger.warn(
								"-------- ActAggiornaDateNotificheSige: aggiornamento scadenziario");
						lScad = valorizzaScadenzarioSigeAggiornamento(lScad, null);
						lCtrlSc.ExModificaScadenzario(lScad);
					}
				}
			} // endif Fissazione Udienza
		} // endif NumRec
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.warn("-------- ActAggiornaDateNotificheSige: fine");
		return lPage;
	}

}