package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.util.Utils;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.archiviazione.controller.IArchiviazione;
import siap.siep.archiviazione.model.ArchiviazioneModel;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActInserisciPassaggioClasse - Classe per l'inserimento della definizione procedimento per passaggio di
 * classe
 *
 * @since MEV_2025-48
 * @author sgioggi
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciPassaggioClasse extends ActionSiap
		implements ICostantiArchiviazione, ICostantiEvento {

	public String processRequest() throws F3BException {

		// FASCICOLO IN SESSIONE
		FascicoloSiepModel fsm = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal idFascicolo = fsm.getIdFascicoloSiep();
		DettaglioFascicoloModel dfm = null;
		IFascicoloSiep ifs = SIEPLookupRemote.getFascicoloSiepRemote();
		dfm = ifs.ExDettaglioFascicoloSiep(fsm.getIdFascicoloSiep());

		// NUOVO FASCICOLO
		FascicoloSiepModel fsmNew = new FascicoloSiepModel();
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_NOTE))
			fsmNew.setNote(getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_NOTE));
		// ===================================================
		// Dati del nuovo fascicolo
		// ===================================================
		fsmNew.setChiaveAnno(new BigDecimal(DateUtils.getSysDate("yyyy"))); // Anno corrente
		fsmNew.setChiaveUfficio(getCodUfficioUtenteConnesso());
		fsmNew.setChiaveProgr(null);
		// il nuovo procedimento deve essere impostato ad iscritto, quindi
		// cod_stato_fascicolo = '02' e flag_validato a 'N'
		// fsmNew.setCodStatoFascicolo("03"); // Stato fascicolo validato
		fsmNew.setCodStatoFascicolo("02");
		fsmNew.setTipoProgressivo(getRequestIntParameter("tipoV"));
		fsmNew.setDataIscrizione(DateUtils.getDate(DateUtils.getSysDate("dd/MM/yyyy"), "dd/MM/yyyy"));
		fsmNew.setDataArchiviazione(null);
		fsmNew.setCodMotivoArchiviazione("-"); // Motivo di archiviazione '-' per le join
		fsmNew.setLetteraFascicolo(null);
		// fsmNew.setNote(fsm.getNote());
		fsmNew.setCodTipoPosLibero(fsm.getCodTipoPosLibero());
		fsmNew.setSenIdSentenza(fsm.getSenIdSentenza());
		fsmNew.setCodOperatoreInserimento(getCodUtenteConnesso());
		fsmNew.setDataInserimento(DateUtils.getSysDate());
		fsmNew.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		fsmNew.setCodOperatoreAggiornamento(null);
		fsmNew.setDataAggiornamento(null);
		fsmNew.setCodUfficioAggiornamento(null);
		fsmNew.setFasSieIdFascicoloSiep(fsm.getIdFascicoloSiep());
		// Il flag altra causa viene gestito nella gestione della posizione giuridica
		fsmNew.setFlagAltraCausa("N");
		fsmNew.setDataIrrevocabilita(
				getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA,
						ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA,
						ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA));
		fsmNew.setDataArrivoAtto(getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_MESE_ARRIVO_ATTO,
				ICostantiFascicoloSiep.CAMPO_GIORNO_ARRIVO_ATTO));
		fsmNew.setFlagCumulante(null);
		fsmNew.setFlagCumulato(null);
		fsmNew.setFlagValidato("N");
		FascicoloSiepModel fsmRet = ifs.ExInserisciFascicoloSiepPassaggioClasse(fsm.getSoggetto(), fsmNew,
				dfm);

		// EVENTO
		EventoModel em = new EventoModel();
		em.setCodTipoEvento("01"); // Tipo Evento = Provvedimento
		em.setCodTipoProvvedimento("25"); // Tipo Provvedimento = Annotazione
		// Codice Motivo = archiviazione fascicolo con contestuale iscrizione in altra classe di appartenenza
		em.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE)); // 0736
		em.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		em.setDataTrasmissioneAtti(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
				ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		em.setCodUfficioEmittente(getCodUfficioUtenteConnesso());
		em.setCodLuogoEmittente(getCodComuneUtenteConnesso());
		em.setCodTipoUfficioDestinatario("-");
		em.setCodLuogoDestinatario("-");
		em.setCodUfficioDestinatario("-");
		em.setCodEsito("-");
		em.setCodMagistrato(calcolaMagistrato());
		em.setFasSieIdFascicoloSiep(idFascicolo);
		em.setFlagVideoSiep("S");
		em.setFlagStampaSiep("S");
		em.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		em.setCodOperatoreInserimento(getCodUtenteConnesso());
		em.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		em.setDataInserimento(DateUtils.getSysDate());

		// NOTIFICHE
		List notifiche = new ArrayList();
		NotificaModel nm = new NotificaModel();
		nm.setCodTipoNotifica("E");
		nm.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
		nm.setCodEsito("-");
		nm.setCodOperatoreInserimento(getCodUtenteConnesso());
		nm.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		nm.setDataInserimento(DateUtils.getSysDate());

		// AUTORITA' ESTERNA
		AutoritaEsternaModel aem = new AutoritaEsternaModel();
		aem.setCodTipoAutorita("24"); // Casellario Giudiziale
		ComuneModel cm = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
		aem.setCodSede(cm.getCodComune());
		aem.setCodOperatoreInserimento(getCodUtenteConnesso());
		aem.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		aem.setDataInserimento(DateUtils.getSysDate());
		nm.setAutoritaEsterna(aem);

		// EVENTO-NOTIFICA
		EventoNotificaModel enm = new EventoNotificaModel();
		enm.setEvento(em);
		notifiche.add(nm);
		enm.setNotifiche((NotificaModel[]) notifiche.toArray(new NotificaModel[0]));

		// ARCHIVIAZIONE
		ArchiviazioneModel am = new ArchiviazioneModel();
		am.setCodTipoProvvedimento("14"); // PC
		am.setCodProvvedimento("-");
		am.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		am.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		am.setCodTipoEmittente("-");
		am.setCodTipoAutoritaEmittente("-");
		am.setCodLuogoEmittente("-");
		am.setCodOperatoreInserimento(getCodUtenteConnesso());
		am.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		am.setDataInserimento(DateUtils.getSysDate());
		am.setFasSieIdFascicoloSiep(idFascicolo);
		am.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));
		IArchiviazione ia = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel archm = ia.ExInserisciEventoNotificaArchiviazione(enm, am, fsm);
		
		// azione di ritorno
		return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.archiviazione.action.ActLoadDettaglioPassaggioClasse&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + archm.getEveIdEvento() + "&"
				+ ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP + "="
				+ fsmRet.getIdFascicoloSiep().toString();
	}

	/**
	 * calcolaMagistrato
	 *
	 * @return String
	 */
	protected String calcolaMagistrato() throws F3BException {

		String codiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);
		if (!Utils.isPresent(codiceMagistrato)) {
			MagistratoModel mm = new MagistratoModel();
			mm.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			mm.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());
			IMagistrato im = SICOLookupRemote.getMagistratoRemote();
			Vector v = new Vector();
			try {
				v = im.ExRicercaMagistrato(mm);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}
			mm = (MagistratoModel) v.firstElement();
			codiceMagistrato = mm.getCodMagistrato();
		}
		// valore di ritorno
		return codiceMagistrato;
	}

}