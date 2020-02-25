package siap.siep.misurasicurezza.action;

import java.math.BigDecimal;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.archiviazione.action.ActArchiviazione;
import siap.siep.archiviazione.action.ICostantiArchiviazione;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciArchiviazionePerProvvCumulo
 * </p>
 * <p>
 * Description: Classe per Inserimento di Archiviazione per Provvedimento di cumulo
 * </p>
 */
public class ActInserisciArchiviazionePerProvvCumulo extends ActArchiviazione implements
		ICostantiArchiviazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicoloSiep = lFascMod.getIdFascicoloSiep();

		// evento notifica da passare al metodo di inserimento
		EventoNotificaModel enm = new EventoNotificaModel();
		// evento model
		EventoModel em = new EventoModel();
		em.setCodTipoEvento("01");
		// em.setCodTipoProvvedimento("22"); // Tipo Provvedimento = Assorbimento in cumulo
		em.setCodTipoProvvedimento("25");
		em.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		em.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		em.setDataTrasmissioneAtti(getRequestDateParameter(
				ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO,
				ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO));
		// Ufficio Emittente = ufficio inserimento dell'evento
		em.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		em.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		em.setCodTipoUfficioDestinatario("-");
		em.setCodLuogoDestinatario("-");
		em.setCodUfficioDestinatario("-");
		em.setCodMagistrato(calcolaMagistrato());
		em.setCodEsito("-");
		em.setFasSieIdFascicoloSiep(idFascicoloSiep);
		em.setFlagVideoSiep("S");
		em.setFlagStampaSiep("S");
		em.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		em.setCodOperatoreInserimento(getCodUtenteConnesso());
		em.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		em.setDataInserimento(DateUtils.getSysDate());
		em.setDataAggiornamento(DateUtils.getSysDate());
		// setto l'evento dentro l'eventonotificaModel
		enm.setEvento(em);

		// archiviazione
		ArchiviazioneModel am = new ArchiviazioneModel();
		am.setCodTipoProvvedimento("22");
		am.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		am.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		am.setCodOperatoreInserimento(getCodUtenteConnesso());
		am.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		am.setDataInserimento(DateUtils.getSysDate());
		am.setDataAggiornamento(DateUtils.getSysDate());
		am.setFasSieIdFascicoloSiep(idFascicoloSiep);
		am.setNote(getRequestStringParameter(CAMPO_NOTE));

		// Notifiche
		// solo se Assorbimento cumulo altro ufficio setto le notifiche
		if ("1154".equals(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE)))
			enm.setNotifiche(loadNotifiche());

		// FASCICOLO
		FascicoloSiepModel fsm = new FascicoloSiepModel();
		fsm.setIdFascicoloSiep(idFascicoloSiep);
		fsm.setAnnoFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE));
		fsm.setNumFascicoloUnione(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE));
		fsm.setDataUnione(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE,
				ICostantiFascicoloSiep.CAMPO_MESE_UNIONE, ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE));
		String codUfficioUnione = null;
		if ("1154".equals(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE))) {
			String codUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE);
			String sedeUfficio = getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE);
			codUfficioUnione = getCodUfficioByCodTipoUfficioDescrComune(codUfficio, sedeUfficio);
		} else
			codUfficioUnione = getCodUfficioUtenteConnesso();
		fsm.setCodUfficioUnione(codUfficioUnione);
		fsm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		fsm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		fsm.setDataAggiornamento(DateUtils.getSysDate());

		// inserimento
		IArchiviazione iArchiviazione = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel amIns = iArchiviazione.ExInserisciEventoNotificaArchiviazione(enm, am, fsm);

		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misurasicurezza.action.ActLoadDettaglioArchiviazionePerProvvCumulo&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + amIns.getEveIdEvento();
	}

}