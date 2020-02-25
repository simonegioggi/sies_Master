package siap.siep.penapecuniaria.action;

/**
* <p>Title: ActInserisciFascicoloDaClasseIII</p>
* <p>Description: Classe Action </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Collection;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.model.EventoModel;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penapecuniaria.controller.IRichiestaConversione;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActInserisciFascicoloDaClasseVII extends ActionSiap
		implements ICostantiPenaPecuniaria, ICostantiFascicoloSiep {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/********************************************************************************
	 * Azione di Inserimento del fascicolo di classeI a partire da quello di cl. VII
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ********************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFasClasseVIIMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// AnnotazioneManualeModel lAnnMod= (AnnotazioneManualeModel) getRequestAttribute("annotazione");

		// Annotazione manuale
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();
		AnnotazioneManualeModel lAnnMod = lCtrlAnn
				.ExRicercaAnnotazioniManualiByIdEvento(getRequestBigDecimalParameter("IdEvento"));
		setRequestAttribute("annotazione", lAnnMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" AnnMan ----> " + lAnnMod);

		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		DettaglioFascicoloModel lDettaglioFascicolo = null;
		IFascicoloSiep lCtrlFas = SIEPLookupRemote.getFascicoloSiepRemote();
		lDettaglioFascicolo = lCtrlFas.ExDettaglioFascicoloSiep(lFasClasseVIIMod.getIdFascicoloSiep());

		// =====================================================
		// Evento di Annotazione per il fascicolo di classe VII
		// =====================================================
		EventoModel lEveMod = new EventoModel();
		lEveMod.setEveIdEvento(getRequestBigDecimalParameter("IdEvento")); // N.B. Si fa viaggiare l'IdEvento
																			// dell'Annotazione di
																			// Revoca/Conversione del
																			// fascicolo di classe VII per
																			// validare il provvedimento.
		lEveMod.setCodTipoEvento("01");
		lEveMod.setCodTipoProvvedimento("25");

		DecodificheModel lModel = new DecodificheModel();
		IDecodifiche lDecodifiche = SICOLookupRemote.getDecodificheRemote();
		lModel.setContesto("MOTIVO_PROVVEDIMENTO");
		Collection lColTipoMotivo = lDecodifiche.ExRicercaDecodifiche(lModel);
		String lMotivo = DecodificheUtils.getCodebyCodAlt2(lColTipoMotivo,
				getRequestStringParameter("Motivo"));

		lEveMod.setCodMotivo(lMotivo);
		setRequestAttribute("Motivo", getRequestStringParameter("Motivo"));
		lEveMod.setFlagDocumentoRegistrato("S");
		lEveMod.setFlagStampaSiep("S");
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFasSieIdFascicoloSiep(lFasClasseVIIMod.getIdFascicoloSiep());

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
		// Dati del nuovo fascicolo di classe I
		// ===================================================
		lFasMod.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		lFasMod.setChiaveUfficio(getCodUfficioUtenteConnesso());
		lFasMod.setChiaveProgr(null);
		lFasMod.setCodStatoFascicolo("03"); // Stato fascicolo validato
		lFasMod.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));

		lFasMod.setDataArchiviazione(null);
		lFasMod.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		lFasMod.setLetteraFascicolo(null);

		// ===============================================================================================================
		// 01/04/2015 Per la pena residua del nuovo fascicolo di classe I si riportano i dati impostati nella
		// FORM
		// Si utilizza lDettaglioFascicolo per originare la pena residua del classe I a partire dal classe VII
		// ===============================================================================================================
		lDettaglioFascicolo.getPenaResidua().setNumAnniReclusione(lAnnMod.getNumAnniReclusione());
		lDettaglioFascicolo.getPenaResidua().setNumMesiReclusione(lAnnMod.getNumMesiReclusione());
		lDettaglioFascicolo.getPenaResidua().setNumGiorniReclusione(lAnnMod.getNumGiorniReclusione());
		lDettaglioFascicolo.getPenaResidua().setNumAnniArresto(lAnnMod.getNumAnniArresto());
		lDettaglioFascicolo.getPenaResidua().setNumMesiArresto(lAnnMod.getNumMesiArresto());
		lDettaglioFascicolo.getPenaResidua().setNumGiorniArresto(lAnnMod.getNumGiorniArresto());

		// Si riportano i dati del fascicolo classe VII
		lFasMod.setNote(lFasClasseVIIMod.getNote());
		lFasMod.setCodTipoPosLibero(lFasClasseVIIMod.getCodTipoPosLibero());
		lFasMod.setFlagValidato(lFasClasseVIIMod.getFlagValidato());
		lFasMod.setSenIdSentenza(lFasClasseVIIMod.getSenIdSentenza());

		lFasMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lFasMod.setDataInserimento(DateUtils.getSysDate());
		lFasMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lFasMod.setCodOperatoreAggiornamento(null);
		lFasMod.setDataAggiornamento(null);
		lFasMod.setCodUfficioAggiornamento(null);

		lFasMod.setFasSieIdFascicoloSiep(lFasClasseVIIMod.getIdFascicoloSiep());

		lFasMod.setFlagAltraCausa("N"); // Il flag altra causa viene gestito nella gestione della posizione
										// giuridica.

		lFasMod.setDataIrrevocabilita(lFasClasseVIIMod.getDataIrrevocabilita());

		lFasMod.setDataArrivoAtto(lFasClasseVIIMod.getDataArrivoAtto());

		lFasMod.setFlagCumulante(null);
		lFasMod.setFlagCumulato(null);
		lFasMod.setFlagValidato("N");

		IRichiestaConversione lCtrlRic = SIEPLookupRemote.getRichiestaConversioneRemote();
		SoggettoModel lSogMod = new SoggettoModel();
		lSogMod = lFasClasseVIIMod.getSoggetto();
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lEveMod -> " + lEveMod);

		// 04/08/2015 ========= modifica dello stato del procedimento ==========
		// preparo il model da passare al controller per l'aggiornamento
		StatoProcedimentoModel lStatoProcMod = new StatoProcedimentoModel();

		// imposto i valori nel model
		lStatoProcMod.setProgressivo(new BigDecimal(1));
		// lStatoProcMod.setFasSieIdFascicoloSiep (lFasMod.getIdFascicoloSiep());
		lStatoProcMod.setFasSieIdFascicoloSiep(lFasClasseVIIMod.getIdFascicoloSiep());
		lStatoProcMod.setData(DateUtils.getSysDate());
		if (lEveMod.getCodMotivo() != null && lEveMod.getCodMotivo().compareTo("1011") == 0)
			lStatoProcMod.setCodStatoProcedimento("0262");
		else
			lStatoProcMod.setCodStatoProcedimento("0263");
		lStatoProcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lStatoProcMod.setDataInserimento(DateUtils.getSysDate());
		lStatoProcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

		lFasMod = lCtrlRic.ExInserisciFascicolodaClasseVII(lSogMod, lEveMod, lFasMod, lDettaglioFascicolo,
				lFasClasseVIIMod, lAnnMod, lStatoProcMod);

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
		String lStringNomeAzione = "&NomeAzione=siap.siep.penapecuniaria.action.ActInserisciFascicoloDaClasseVII";
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.fascicolo.action.ActLoadDettaglioFascicolo&" + CAMPO_ID_FASCICOLO_SIEP + "="
				+ lFasMod.getIdFascicoloSiep().toString() + lStringNomeAzione;

	}
}