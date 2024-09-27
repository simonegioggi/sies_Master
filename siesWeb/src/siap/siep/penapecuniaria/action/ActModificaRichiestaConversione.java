package siap.siep.penapecuniaria.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.penapecuniaria.model.RichiestaConversioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActModificaRichiestaConversione - Classe Action per la modifica di RichiestaConversione
 *
 * @version 1.0
 */
public class ActModificaRichiestaConversione extends ActionSiap implements ICostantiPenaPecuniaria {

	/*****************************************************************************
	 * Azione di Modifica del RichiestaConversione
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		IRichiestaConversione lCtrl = SIEPLookupRemote.getRichiestaConversioneRemote();
		RichiestaConversioneModel lRicMod = new RichiestaConversioneModel();
		lRicMod.setIdRichiestaConversione(getRequestBigDecimalParameter(CAMPO_ID_RICHIESTA_CONVERSIONE));
		lRicMod = lCtrl.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());

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
				getCodComuneByDescr(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
		lRicMod.setCodLuogoEmittente(lComMod.getCodComune());

		lRicMod.setDataRicezioneAtto(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE_ATTO,
				CAMPO_MESE_DATA_RICEZIONE_ATTO, CAMPO_GIORNO_DATA_RICEZIONE_ATTO));
		lRicMod.setDataIscrizioneAtto(getRequestDateParameter(CAMPO_ANNO_DATA_ISCRIZIONE_ATTO,
				CAMPO_MESE_DATA_ISCRIZIONE_ATTO, CAMPO_GIORNO_DATA_ISCRIZIONE_ATTO));
		lRicMod.setDataEsazione(getRequestDateParameter(CAMPO_ANNO_DATA_ESAZIONE, CAMPO_MESE_DATA_ESAZIONE,
				CAMPO_GIORNO_DATA_ESAZIONE));

		// Paolo Cherubini 04/05/2011 commento la modifica degli importi
		// 25/11/2014 Ripristinata la modifica degli importi
		if ((getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT") != null
				&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")).equals(""))
				|| (getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC") != null
						&& !(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")).equals(""))) {
			lRicMod.setImportoMulta(new BigDecimal(getRequestStringParameter(CAMPO_IMPORTO_MULTA + "INT")
					+ "." + getRequestStringParameter(CAMPO_IMPORTO_MULTA + "DEC")));
		}
		lRicMod.setDataPrescrizioneMulta(getRequestDateParameter(CAMPO_ANNO_DATA_PRESCRIZIONE_MULTA,
				CAMPO_MESE_DATA_PRESCRIZIONE_MULTA, CAMPO_GIORNO_DATA_PRESCRIZIONE_MULTA));
		if (isRequestChecked(CAMPO_FLAG_IMPRESCRITTIBILE_MULTA)) {
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
		// if (!isRequestParameterNullObj(CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA) && isRequestChecked (
		// CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA) )
		if (isRequestChecked(CAMPO_FLAG_IMPRESCRITTIBILE_AMMENDA)) {
			lRicMod.setFlagImprescrittibileAmmenda("S");
		} else {
			lRicMod.setFlagImprescrittibileAmmenda("N");
		}

		// lRicMod.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lRicMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lRicMod.setDataAggiornamento(DateUtils.getSysDate());
		lRicMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());

		// ====================================================================================
		// Effettua aggiornamento Richiesta Conv. (dal 07/09/2015 con Inserimento nota x SIUS)
		// ====================================================================================
		if (lRicMod.getFasSiuIdFascicoloSius() == null)
			lCtrl.ExModificaRichiestaConversione(lRicMod);
		else {
			FascicoloSiepModel lFasSiepMod = (FascicoloSiepModel) this.getSessionAttribute("fascicolo");
			lCtrl.ExModificaRichiestaConversione(lRicMod, lFasSiepMod);
		}

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena modificati
		// ======================================================================

		lRicMod = lCtrl.ExRicercaRichiestaConversioneById(lRicMod.getIdRichiestaConversione());
		setRequestAttribute("richiestaconversione", lRicMod);

		// pagina di ritorno
		return PG_LOAD_DETTAGLIO_RICHIESTA_CONVERSIONE;
	}

}