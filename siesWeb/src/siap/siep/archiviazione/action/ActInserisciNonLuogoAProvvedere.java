package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciNonLuogoAProvvedere
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di Evento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciNonLuogoAProvvedere extends ActionSiap
		implements ICostantiArchiviazione, ICostantiEvento {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// EVENTO
		EventoModel lEveMod = new EventoModel();

		// lEveMod.setCodTipoEvento("15"); // Tipo Evento = Definizione Procedimento
		lEveMod.setCodTipoEvento("01");
		// Cod Tipo Provvedimento da 20 passa a 25. Luigi 13-10-2005
		lEveMod.setCodTipoProvvedimento("25"); // Tipo Provvedimento = Non luogo a provvedere (NLP)
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));
		lEveMod.setDataTrasmissioneAtti(
				getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI,
						ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");

		lEveMod.setCodMagistrato(calcolaMagistrato());

		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S");
		lEveMod.setFlagStampaSiep("S");

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		List lNotifiche = new ArrayList();

		// NOTIFICHE
		NotificaModel lNotMod = new NotificaModel();

		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
				CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

		// Paolo Cherubini 18/01/2011 questo note sono relative all'evento non alla notifica e vanno inserite
		// nel campo lArcMod.setNote vedi sotto
		// lNotMod.setNote( getRequestStringParameter( ICostantiNotifica.CAMPO_NOTE_E) );

		lNotMod.setCodEsito("-");

		lNotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotMod.setDataInserimento(DateUtils.getSysDate());

		AutoritaEsternaModel lAutMod = new AutoritaEsternaModel();
		lAutMod.setCodTipoAutorita("24"); // Casellario Giudiziale

		// lAutMod.setCodSede( getRequestStringParameter("codicecomunecasellario") );
		ComuneModel lComCasellarioMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
		lAutMod.setCodSede(lComCasellarioMod.getCodComune());

		lAutMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lAutMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAutMod.setDataInserimento(DateUtils.getSysDate());

		lNotMod.setAutoritaEsterna(lAutMod);

		// SETTO ISTITUTO DETENZIONE
		if (!this.isRequestParameterNullObj(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
						.equals("")) {
			NotificaModel lNotModIst = new NotificaModel();

			String lIstituto = getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
			// String lNoteIstituto = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_NOTE);

			lNotModIst.setCodTipoNotifica("C");
			lNotModIst.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
			lNotModIst.setCodEsito("-");
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);

			lNotModIst.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());

			lNotifiche.add(lNotModIst);
		}

		if (!this.isRequestParameterNullObj("UfficioRecuperCrediti")
				&& this.getRequestStringParameter("UfficioRecuperCrediti") != null
				&& !this.getRequestStringParameter("UfficioRecuperCrediti").equals("-")) {
			String lUfficio = this.getRequestStringParameter("UfficioRecuperCrediti");
			String lSedeUfficio = this.getRequestStringParameter("SedeUfficioRecuperCrediti");
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));

			String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA altra AUTORITA
		if (!this.isRequestParameterNullObj(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
				&& this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA) != null
				&& !this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA)
						.equals("-")) {
			String lPolizia = this
					.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);
			String lSedePolizia = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
			NotificaModel lNotModPol = new NotificaModel();
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)) {
				String lNotePolizia = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);
				lNotModPol.setNote(lNotePolizia);
			}

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_TRASMISSIONE_ATTI,
					CAMPO_MESE_DATA_TRASMISSIONE_ATTI, CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI));
			AutoritaEsternaModel lAut = new AutoritaEsternaModel();

			lAut.setCodTipoAutorita(lPolizia);

			ComuneModel lComMod = new ComuneModel(getCodComuneByDescrFlagVal(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// EVENTO-NOTIFICA
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveNotMod.setEvento(lEveMod);

		lNotifiche.add(lNotMod);
		lEveNotMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// ARCHIVIAZIONE
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();

		lArcMod.setCodTipoProvvedimento("20"); // NLP
		lArcMod.setCodProvvedimento("-");
		lArcMod.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));

		lArcMod.setCodTipoEmittente("-");
		lArcMod.setCodTipoAutoritaEmittente("-");
		lArcMod.setCodLuogoEmittente("-");

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// Paolo Cherubini 18/01/2011 questo note sono relative all'evento non alla notifica e vanno inserite
		// nel seguente campo (vedi sopra)
		lArcMod.setNote(getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE_E));

		IArchiviazione lCtrlArc = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRet = lCtrlArc.ExInserisciEventoNotificaArchiviazione(lEveNotMod, lArcMod,
				lFascMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.archiviazione.action.ActLoadDettaglioNonLuogoAProvvedere&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRet.getEveIdEvento();

		return lPage;
	}

	/**
	 * calcolaMagistrato
	 * 
	 * @return
	 */
	protected String calcolaMagistrato() throws F3BException {

		String lCodiceMagistrato = getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO);

		if (lCodiceMagistrato.compareTo("") == 0) {
			MagistratoModel lMagMod = new MagistratoModel();
			lMagMod.setCognome(getRequestStringParameter(ICostantiMagistrato.CAMPO_COGNOME).toUpperCase());
			lMagMod.setNome(getRequestStringParameter(ICostantiMagistrato.CAMPO_NOME).toUpperCase());

			IMagistrato lCtrl = SICOLookupRemote.getMagistratoRemote();
			Vector lVect = new Vector();
			try {
				lVect = lCtrl.ExRicercaMagistrato(lMagMod);
			} catch (F3BException exF3b) {
				throw new F3BException(F3BException.USER_MESSAGE, "Magistrato Inesistente");
			}

			lMagMod = (MagistratoModel) lVect.firstElement();
			lCodiceMagistrato = lMagMod.getCodMagistrato();
		}

		return lCodiceMagistrato;
	}

}