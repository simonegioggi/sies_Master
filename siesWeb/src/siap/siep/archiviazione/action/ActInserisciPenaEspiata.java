package siap.siep.archiviazione.action;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActInserisciPenaEspiata
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
public class ActInserisciPenaEspiata extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// evento notifica da passare al metodo di inserimento
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		// evento
		EventoModel lEveMod = new EventoModel();

		// commentato il 15-04-2005 -- viviana -- dario altrimenti nn si vede nello stato esecuzione,elenco
		// prov, ecc.
		// lEveMod.setCodTipoEvento("15"); // Tipo Evento = Definizione Procedimento
		lEveMod.setCodTipoEvento("01");
		// Cod Tipo Provvedimento passa da 21 a 25
		lEveMod.setCodTipoProvvedimento("25"); // Tipo Provvedimento = Fine Espiazione
		lEveMod.setCodMotivo(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lEveMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");
		lEveMod.setCodEsito("-");
		lEveMod.setCodMagistrato(calcolaMagistrato());
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setFlagVideoSiep("S"); //
		lEveMod.setFlagStampaSiep("S"); //

		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento dentro l'eventonotificaModel
		lEveNotMod.setEvento(lEveMod);

		// archiviazione
		ArchiviazioneModel lArcMod = new ArchiviazioneModel();

		lArcMod.setCodTipoProvvedimento("21");
		lArcMod.setCodOggettoDefinizione(getRequestStringParameter(CAMPO_COD_OGGETTO_DEFINIZIONE));
		lArcMod.setDataDefinizione(getRequestDateParameter(CAMPO_ANNO_DATA_DEFINIZIONE,
				CAMPO_MESE_DATA_DEFINIZIONE, CAMPO_GIORNO_DATA_DEFINIZIONE));
		lArcMod.setNumNota(getRequestStringParameter(CAMPO_NUM_NOTA));
		lArcMod.setDataEmissione(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));
		lArcMod.setDataRicezione(getRequestDateParameter(CAMPO_ANNO_DATA_RICEZIONE, CAMPO_MESE_DATA_RICEZIONE,
				CAMPO_GIORNO_DATA_RICEZIONE));

		lArcMod.setCodTipoEmittente("-");
		if (!this.isRequestParameterNullObj(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE)
				&& getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE) != null
				&& !getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE).equals("")) {
			lArcMod.setCodTipoEmittente("01");
			lArcMod.setIstDetIdIstitutoDetenzione(
					getRequestStringParameter(CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
		}

		if (!this.isRequestParameterNullObj(CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
				&& this.getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE) != null
				&& !this.getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE).equals("-")) {
			lArcMod.setCodTipoEmittente("02");
			lArcMod.setCodTipoAutoritaEmittente(getRequestStringParameter(CAMPO_COD_TIPO_AUTORITA_EMITTENTE));

			ComuneModel lCom = new ComuneModel(
					getCodComuneByDescrFlagVal(getRequestStringParameter(CAMPO_COD_LUOGO_EMITTENTE)));
			lArcMod.setCodLuogoEmittente(lCom.getCodComune());
			lArcMod.setIndirizzoEmittente(getRequestStringParameter(CAMPO_INDIRIZZO_EMITTENTE));
		}

		lArcMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lArcMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lArcMod.setDataInserimento(DateUtils.getSysDate());
		lArcMod.setFasSieIdFascicoloSiep(lIdFascicolo);

		// notifica
		NotificaModel lNotMod = new NotificaModel();
		lNotMod.setCodEsito("-");
		lNotMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lNotMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lNotMod.setDataInserimento(DateUtils.getSysDate());
		lNotMod.setCodTipoNotifica("E");
		lNotMod.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE, CAMPO_MESE_DATA_EMISSIONE,
				CAMPO_GIORNO_DATA_EMISSIONE));

		AutoritaEsternaModel lAut = new AutoritaEsternaModel();
		lAut.setCodTipoAutorita("24"); // casellario giudiziale

		// lAut.setCodSede(getRequestStringParameter("codicecomcas"));
		ComuneModel lComCasellarioMod = new ComuneModel(
				getCodComuneByDescr(getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE_CAS)));
		lAut.setCodSede(lComCasellarioMod.getCodComune());

		lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lAut.setDataInserimento(DateUtils.getSysDate());

		lNotMod.setAutoritaEsterna(lAut);

		// setto la notifica dentro l'eventonotificaModel
		ArrayList lNotifiche = new ArrayList();
		lNotifiche.add(lNotMod);

		// istituto per conoscenza
		if (!isRequestParameterNullObj(
				ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_CONOSCENZA")
				&& getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_CONOSCENZA") != null
				&& !getRequestStringParameter(
						ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_CONOSCENZA")
								.equals("")) {
			NotificaModel lNotModIst = new NotificaModel();
			String lIstituto = this.getRequestStringParameter(
					ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE + "_CONOSCENZA");
			lNotModIst.setCodEsito("-");
			lNotModIst.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lNotModIst.setDataInserimento(DateUtils.getSysDate());
			lNotModIst.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lNotModIst.setCodTipoNotifica("C");
			lNotModIst.setDataInvio(getRequestDateParameter(CAMPO_ANNO_DATA_EMISSIONE,
					CAMPO_MESE_DATA_EMISSIONE, CAMPO_GIORNO_DATA_EMISSIONE));
			lNotModIst.setIstDetIdIstitutoDetenzione(lIstituto);
			lNotifiche.add(lNotModIst);
		}

		lEveNotMod.setNotifiche((NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]));

		// inserimento
		IArchiviazione lCtrl = SIEPLookupRemote.getArchiviazioneRemote();
		ArchiviazioneModel lArcModRes = lCtrl.ExInserisciEventoNotificaArchiviazione(lEveNotMod, lArcMod,
				lFascMod);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.archiviazione.action.ActLoadDettaglioPenaEspiata&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lArcModRes.getEveIdEvento();

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