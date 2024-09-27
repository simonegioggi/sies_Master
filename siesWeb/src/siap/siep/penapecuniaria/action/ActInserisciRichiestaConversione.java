package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActInserisciRichiestaConversione - Classe Action per l'inserimento di RichiestaConversione
 *
 * @version 1.0
 */
public class ActInserisciRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Inserimento del RichiestaConversione
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		FascicoloSiepModel lFasMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		DettaglioFascicoloModel lDettaglioFascicolo = null;
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		lDettaglioFascicolo = lCtrlFas.ExDettaglioFascicoloSiep(lFasMod.getIdFascicoloSiep());

		// 20/02/2015 per il fascicolo di classe VII si imposta il FasSieIdFascicoloSiep.
		// BigDecimal lIdFascicoloClasseI = null;
		String nomeIdFascClasseI = "idFascicoloClasseI";
		if (!isRequestParameterNullObj(nomeIdFascClasseI)
				&& getRequestStringParameter("idFascicoloClasseI").compareTo("null") != 0) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("lIdFascicoloClasseI = " + getRequestStringParameter("idFascicoloClasseI"));
			// lIdFascicoloClasseI = (BigDecimal)this.getRequestBigDecimalParameter("idFascicoloClasseI");
			lDettaglioFascicolo.getFascicoloSiep()
					.setFasSieIdFascicoloSiep(getRequestBigDecimalParameter("idFascicoloClasseI"));
		}

		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		lRicMod.setAnnoPartita(getRequestBigDecimalParameter(CAMPO_ANNO_PARTITA));
		lRicMod.setNumPartita(getRequestBigDecimalParameter(CAMPO_NUM_PARTITA));
		lRicMod.setNumExCampione(getRequestStringParameter(CAMPO_NUM_EX_CAMPIONE));
		// lRicMod.setProtCircosrizioneDoganale ( getRequestStringParameter (
		// CAMPO_PROT_CIRCOSRIZIONE_DOGANALE) );
		lRicMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

		ComuneModel lComMod = new ComuneModel(
				getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lRicMod.setCodLuogoEmittente(lComMod.getCodComune());

		lRicMod.setDataRicezioneAtto(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTO,
				CAMPO_MESE_DATA_RICEZIONE_ATTO, CAMPO_GIORNO_DATA_RICEZIONE_ATTO));
		lRicMod.setDataIscrizioneAtto(getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE_ATTO,
				CAMPO_MESE_DATA_ISCRIZIONE_ATTO, CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO));
		lRicMod.setDataEsazione(getRequestDateParameter(CAMPO_ANNO_DATA_ESAZIONE, CAMPO_MESE_DATA_ESAZIONE,
				CAMPO_GIORNO_DATA_ESAZIONE));
		if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals(""))) {
			lRicMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")
					+ "." + getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")));
		}
		lRicMod.setDataPrescrizioneMulta(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA,
				CAMPO_MESE_DATA_PRESCRIZIONE_MULTA, CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA));
		if (!isRequestParameterNullObj(CAMPO_FLAG_IMPRESCRITTIBILE_MULTA)
				&& isRequestChecked(CAMPO_FLAG_IMPRESCRITTIBILE_MULTA)) {
			lRicMod.setFlagImprescrittibileMulta("S");
		} else {
			lRicMod.setFlagImprescrittibileMulta("N");
		}

		if ((getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")).equals(""))) {
			lRicMod.setImportoAmmenda(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "INT")
					+ "." + getRequestStringParameter(CAMPO_IMPORTO_AMMENDA + "DEC")));
		}
		lRicMod.setDataPrescrizioneAmmenda(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE_AMMENDA,
				CAMPO_MESE_DATA_PRESCRIZIONE_AMMENDA, CAMPO_GIORNO_DATA_PRESCRIZIONE_AMMENDA));
		if (!isRequestParameterNullObj(CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA)
				&& isRequestChecked(CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA)) {
			lRicMod.setFlagImprescrittibileAmmenda("S");
		} else {
			lRicMod.setFlagImprescrittibileAmmenda("N");
		}

		lRicMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lRicMod.setDataInserimento(DateUtils.getSysDate());
		lRicMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		RichiestaConversioneModel lRicRetMod = new RichiestaConversioneModel();

		EventoModel lEveMod = new EventoModel();
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("12");
		lEveMod.setCodMotivo("0942");
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
		lEveMod.setDataEmissione((getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTO,
				CAMPO_MESE_DATA_RICEZIONE_ATTO, CAMPO_GIORNO_DATA_RICEZIONE_ATTO)));

		lEveMod.setCodLuogoEmittente(lRicMod.getCodLuogoEmittente());
		lEveMod.setCodUfficioEmittente(getCodUfficioUtenteConnesso());

		lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		lEveMod.setCodEsito("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");

		// Si rende il provvedimento visibile nello stato di esecuzione
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");

		// ===================================================
		// Recupera il controller ed effettua l'inserimento
		// ===================================================
		int lFascProg = lFasMod.getChiaveProgr().intValue();
		if (lFascProg > 70000 && lFascProg < 80001) {
			// fascicolo di conversione delle pene pecuniare (Classe VII)
			// sono in fase di iscrizione e inserisco solo la richiesta
			IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
			lRicMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
			lRicRetMod = lCtrlRic.ExInserisciRichiestaConversione(lRicMod, lEveMod, lDettaglioFascicolo);
		} else {
			// fascicolo normale (Classe I)
			// sono in fase di conversione per cui iscrivo:
			// duplico soggetto per non incorrere nell'errore sulla chiave univoca
			// fascicolo-soggetto-sentenza e comunque in linea con le nuove disposizione del SuperSoggetto.
			// evento conversione per entrami i fascicoli
			// nuovo fascicolo di conversione in classe VII
			// richiesta
			lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
			lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
			lFasMod.setChiaveProgr(null);
			lFasMod.setCodStatoFascicolo("03"); // Stato fascicolo validato
			// 09/07/2015 lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"),
			// "dd/MM/yyyy"));
			lFasMod.setDataIscrizione(lRicMod.getDataIscrizioneAtto()); // 09/07/2015
			lFasMod.setDataArrivoAtto(lRicMod.getDataRicezioneAtto()); // 09/07/2015

			lFasMod.setDataArchiviazione(null);
			lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
			lFasMod.setLetteraFascicolo(null);
			// lFasMod.setNote() mi riporto quelle del fascicolo classe I
			// lFasMod.setCodTipoPosLibero("-"); mi riporto quelle del fascicolo classe I
			// lFasMod.setFlagValidato("N"); mi riporto quelle del fascicolo classe I
			lFasMod.setCodOperatoreInserimento(getCodUtenteConnesso());
			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
			lFasMod.setCodOperatoreAggiornamento(null);
			lFasMod.setDataAggiornamento(null);
			lFasMod.setCodUfficioAggiornamento(null);
			// lFasMod.setSogIdSoggetto(); mi riporto quelle del fascicolo classe I
			// lFasMod.setSenIdSentenza(); mi riporto quelle del fascicolo classe I
			lFasMod.setFasSieIdFascicoloSiep(lFasMod.getIdFascicoloSiep());
			lRicMod.setFasSieIdFascicoloSiep(null);
			lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della
											// posizione giuridica
			lFasMod.setDataIrrevocabilita(lFasMod.getDataIrrevocabilita());
			lFasMod.setFlagCumulante(null);
			lFasMod.setFlagCumulato(null);
			lFasMod.setFlagValidato("S");

			IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
			SoggettoModel lSogMod = new SoggettoModel();
			lSogMod = lFasMod.getSoggetto();

			lRicRetMod = lCtrlRic.ExInserisciRichiestaConversionedaClasseI(lSogMod, lEveMod, lFasMod,
					lDettaglioFascicolo, lRicMod);
			setSessionAttribute("fascicolo", lFasMod);
			setSessionAttribute("Soggetto", lSogMod);
		}

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.penapecuniaria.action.ActLoadDettaglioRichiestaConversione";
		lPage += "&" + CAMPO_ID_RICHIESTA_CONVERSIONE + "="
				+ lRicRetMod.getIdRichiestaConversione().toString();

		// pagina di ritorno
		return lPage;
	}

}