package siap.siep.penasospesa.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penasospesa.controller.IPenaSospesa;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActInserisciFascicoloDaClasseIII - Classe Action per inserimento nuovo procedimento da classe III
 *
 * @version 1.0
 */
public class ActInserisciFascicoloDaClasseIII extends ActionSiap
		implements ICostantiPenaSospesa, ICostantiFascicoloSiep {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/*****************************************************************************
	 * Azione di Inserimento del fascicolo di classeIII
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFasClasseIIIMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		AnnotazioneManualeModel lAnnMod = (AnnotazioneManualeModel) getSessionAttribute("AnnotazioneMan");
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		DettaglioFascicoloModel lDettaglioFascicolo = null;
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		lDettaglioFascicolo = lCtrlFas.ExDettaglioFascicoloSiep(lFasClasseIIIMod.getIdFascicoloSiep());

		// ===================================================
		// Evento di Annotazione per il fascicolo di classe III
		// ===================================================
		EventoModel lEveMod = new EventoModel();
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");
		lEveMod.setCodMotivo(getRequestStringParameter("Motivo"));
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFasSieIdFascicoloSiep(lFasClasseIIIMod.getIdFascicoloSiep());
		// 11/07/2011 La Data Emissione va inserita nel formato dd/MM/yyyy
		// lEveModel.setDataEmissione(DateUtils.getSysDate());
		lEveMod.setDataEmissione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lEveMod.setCodUfficioEmittente(getUfficioUtenteConnesso().getCodUfficio());
		lEveMod.setCodLuogoEmittente(getUfficioUtenteConnesso().getCodComune());
		lEveMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());
		lEveMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lEveMod.setCodEsito("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodTipoUfficioDestinatario("-");

		// ===================================================
		// Dati del nuovo fascicolo
		// ===================================================
		lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lFasMod.setChiaveProgr(null);
		// MEV_2025-48: il nuovo procedimento deve essere impostato ad iscritto, quindi
		// cod_stato_fascicolo = '02' e flag_validato a 'N'
		// lFasMod.setCodStatoFascicolo("03"); // Stato fascicolo validato
		lFasMod.setCodStatoFascicolo("02");
		// MEV_2025-48: consento la scelta della classe tranne quella da cui provengo
		lFasMod.setTipoProgressivo(getRequestIntParameter("tipoV"));
		lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		lFasMod.setDataArchiviazione(null);
		lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFasMod.setLetteraFascicolo(null);
		// mi riporto i dati del fascicolo classe III
		lFasMod.setNote(lFasClasseIIIMod.getNote());
		lFasMod.setCodTipoPosLibero(lFasClasseIIIMod.getCodTipoPosLibero());
		// lFasMod.setFlagValidato(lFasClasseIIIMod.getFlagValidato()); // poi viene impostato fisso ad "N"
		lFasMod.setSenIdSentenza(lFasClasseIIIMod.getSenIdSentenza());
		lFasMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lFasMod.setCodOperatoreAggiornamento(null);
		lFasMod.setDataAggiornamento(null);
		lFasMod.setCodUfficioAggiornamento(null);
		lFasMod.setFasSieIdFascicoloSiep(lFasClasseIIIMod.getIdFascicoloSiep());
		// Il flag altra causa viene gestito nella gestione della posizione giuridica
		lFasMod.setFlagAltraCausa("N");
		// lFasMod.setDataIrrevocabilita(lFasClasseIIIMod.getDataIrrevocabilita());
		lFasMod.setDataIrrevocabilita(
				getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA,
						ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA,
						ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA));
		lFasMod.setDataArrivoAtto(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO));
		lFasMod.setFlagCumulante(null);
		lFasMod.setFlagCumulato(null);
		lFasMod.setFlagValidato("N");

		IPenaSospesa lCtrlPSosp = SIEPLookupRemote.getPenaSospesaRemote();
		SoggettoModel lSogMod = new SoggettoModel();
		lSogMod = lFasClasseIIIMod.getSoggetto();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveMod -> " + lEveMod);
		lFasMod = lCtrlPSosp.ExInserisciFascicolodaClasseIII(lSogMod, lEveMod, lFasMod, lDettaglioFascicolo,
				lFasClasseIIIMod, lAnnMod);

		lAnnMod = null;
		setSessionAttribute("AnnotazioneMan", lAnnMod);
		setSessionAttribute("fascicolo", lFasMod);
		setSessionAttribute("Soggetto", lSogMod);

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================

		// restituisce la jsp di VIEW
		// String lStringNomeAzione = "&NomeAzione=siap.siep.fascicolo.action.ActInserisciFascicolo";
		String lStringNomeAzione = "&NomeAzione=siap.siep.penasospesa.action.ActInserisciFascicoloDaClasseIII";
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasMod.getIdFascicoloSiep().toString() + lStringNomeAzione;
	}

}