package siap.sico.evento.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.siep.cumulo.model.CumuloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;

/**
 * Title: EventoController
 * Description: Classe Controller per Evento
 *
 * @version 1.0
 */

@SuppressWarnings("rawtypes")
public interface IEventoSimeone {

	public EventoNotificaModel ExRicercaEventoNotificaByIdFascicoloDescrMotivo(BigDecimal aKeyFasc,
			String aMotivo) throws F3BException;

	public EventoNotificaModel ExInseriscioModificaEventoNotificaCumulo(FascicoloSiepModel aFascicolo,
			EventoNotificaModel aEvento, CumuloModel aCumulo) throws F3BException;

	public Vector ExRicercaEventoNotificaByFascicoloSiepTipEventoNOTTipProvNONAnnullati(BigDecimal aFascKey,
			String[] aTipoEvento, String[] aTipoProv) throws F3BException;

	public EventoModel ExRicercaEventoByEveIdEvento(BigDecimal aEventoKey) throws F3BException;

	public EventoNotificaModel ExRicercaEventoNotificaByIdFascicoloCodiceMotivo(BigDecimal aKeyFasc,
			String[] aMotivo) throws F3BException;

	public Vector ExRicercaOrdinanzeDecretiSiep(BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv)
			throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvCodMotivo(BigDecimal aFascKey,
			String aTipoEvento, String aTipoProv, String aMotivo) throws F3BException;

	public Vector ExRicercaEventiByFascicoloSiepTipEventoTipProvCodMotivo(BigDecimal aFascKey,
			String aTipoEvento, String aTipoProv, String aMotivo) throws F3BException;

	public Vector ExRicercaUltimoEventoByIdFascicolo(BigDecimal aKeyFasc) throws F3BException;

	public Vector ExRicercaEventoStatoEsecuzioneByFascicoloSiepPaged(BigDecimal aIdFascicolo, int aPage)
			throws F3BException;

	public BigDecimal ExGetCountEventiPaged(BigDecimal aKeyFascicolo) throws F3BException;

	public Vector ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv, int aPage,
			String aOrdinamento) throws F3BException;

	public BigDecimal ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			String aCodUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv) throws F3BException;

	// Ticket#202101270113 - si adeguano le condizione della count alle condizioni della select
	// impostando il filtro sull'ufficio + accorpati
	public BigDecimal ExGetCountEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			// String aCodUfficioUtenteConnesso,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv,
			String[] aCodMotivo) throws F3BException;
	// FINE Ticket#202101270113

	public Vector ExRicercaEventoByFascicoloSiepTipEventoNOTTipProvPaged(BigDecimal aFascKey,
			UfficioModel aUfficioUtenteConnesso, String[] aTipoEvento, String[] aTipoProv,
			String[] aCodMotivo, int aPage, String aOrdinamento) throws F3BException;

	public Vector ExRicercaProvvedimentiOnViewPaged(EventoModel aModel, int aPage) throws F3BException;

	public BigDecimal ExGetCountProvvedimentiPerOmesseNotifiche(EventoModel aEvento,
			String aCodUfficioUtenteConnesso) throws F3BException;

	public BigDecimal ExGetCountEventiNonValidatiPaged(EventoModel aModel) throws F3BException;

	public List ExRicercaProvvedimentiPerOmesseNotificheOnViewPaged(EventoModel aModel,
			String aCodUfficioUtenteConnesso, int aPage) throws F3BException;

	public EventoModel ExRicercaEventoByEveIdEventoTipoProvCodMotivo(BigDecimal aEventoKey,
			String aTipoEvento, String aTipoProvv, String aMotivo) throws F3BException;
	
	// MEV_2019-09
	public EventoModel ExRicercaEventoByEveIdEventoTipoProvCodMotivo(BigDecimal aEventoKey,
			String aTipoEvento, String aTipoProvv, String aMotivo, String flagDocRegistrato) throws F3BException;
	
	public EventoModel ExRicercaEventoByFascicoloSiepDesc(BigDecimal aKey) throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepDescUfficioConnesso(BigDecimal aKey, String aCodUfficio)
			throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
			BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv, String aOrdinamento)
			throws F3BException;

	public EventoModel ExRicercaEventoByFascicoloSiepTipEventoTipProvPerEventoDaAnnullareCancellare(
			BigDecimal aFascKey, String[] aTipoEvento, String[] aTipoProv, String[] aCodMotivo,
			String aOrdinamento) throws F3BException;

	public Vector ExRicercaEventoByFascicoloSiepTipProv(BigDecimal aFascKey, String[] aTipoProv)
			throws F3BException;

}