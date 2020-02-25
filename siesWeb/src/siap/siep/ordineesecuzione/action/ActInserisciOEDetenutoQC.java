package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.controller.IDecodifiche;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;

/**
 * <p>
 * Title: ActInserisciOEDetenutoQC
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

public class ActInserisciOEDetenutoQC extends ActionSiap implements ICostantiOrdineEsecuzione {
	/**
	 * Azione di Inserimento del Evento
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		EventoNotificaModel lEve = new EventoNotificaModel();

		lEve.getEvento().setCodTipoEvento("01"); // Tipo Evento = Provvedimento
		lEve.getEvento().setCodTipoProvvedimento("-"); // Tipo Provvedimento = Ordinanza

		// Codice motivo da CG_REF_CODES....
		IDecodifiche lDec = SICOLookupRemote.getDecodificheRemote();
		DecodificheModel lDecMod = lDec.ExRicercaDecodificheByHighValue("OE-DET-QC");
		lEve.getEvento().setCodMotivo(lDecMod.getCode());

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		lEve.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
		Date lDataDecorrenza = getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_INIZIO,
				ICostantiPenaResidua.CAMPO_MESE_DATA_INIZIO, ICostantiPenaResidua.CAMPO_GIORNO_DATA_INIZIO);

		Date lDataEmissione = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEve.getEvento().setDataEmissione(lDataEmissione);

		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		String lCodiceOperatore = lUtenteMod.getUserId();
		String lCodiceUfficio = lUtenteMod.getUfficioUtente().getCodUfficio();

		lEve.getEvento().setDataRicezioneAtti(lDataDecorrenza);

		lEve.getEvento().setCodOperatoreInserimento(lCodiceOperatore);
		lEve.getEvento().setCodLuogoEmittente(lUtenteMod.getUfficioUtente().getCodComune());
		lEve.getEvento().setCodUfficioEmittente(lCodiceUfficio);
		lEve.getEvento().setCodUfficioInserimento(lCodiceUfficio);
		lEve.getEvento().setDataInserimento(DateUtils.getSysDate());
		lEve.getEvento().setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		lEve.getEvento().setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));

		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato( getRequestStringParameter(
		// ICostantiEvento.CAMPO_COD_MAGISTRATO));

		lEve.getEvento().setCodEsito("-");
		lEve.getEvento().setCodLuogoDestinatario("-");
		lEve.getEvento().setCodTipoUfficioDestinatario("-");

		// lEve.getEvento().setFlagDocumentoRegistrato("N");

		String lDestinatari = this
				.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA);

		String lSedeDestinatari = this.getRequestStringParameter(ICostantiAutoritaEsterna.CAMPO_COD_SEDE);
		// String[] lArrayTipoNotifica =
		// this.getRequestStringParameters(ICostantiNotifica.CAMPO_COD_TIPO_NOTIFICA);
		String lNote = this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE);

		// int lIndMisura = 0;
		// int lNumNotifiche = 2;

		NotificaModel lNotifiche[] = new NotificaModel[1];

		NotificaModel lNot = new NotificaModel();

		lNot.setCodTipoNotifica("N");

		lNot.setNote(lNote);
		lNot.setDataInvio(lDataEmissione);
		lNot.setCodEsito("-");
		lNot.setCodOperatoreInserimento(lCodiceOperatore);
		lNot.setDataInserimento(DateUtils.getSysDate());
		lNot.setCodUfficioInserimento(lCodiceUfficio);

		AutoritaEsternaModel lAut = new AutoritaEsternaModel();

		// getCodUfficioByCodTipoUfficioDescrComune(lArrayDestinatari[lIndMisura],lArraySedeDestinatari[lIndMisura]);

		lAut.setCodTipoAutorita(lDestinatari);

		ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedeDestinatari));
		lAut.setCodSede(lComMod.getCodComune());

		lAut.setCodOperatoreInserimento(lCodiceOperatore);
		lAut.setCodUfficioInserimento(lCodiceUfficio);
		lAut.setDataInserimento(DateUtils.getSysDate());

		// Setto l'Autorita Esterna per la notifica corrente
		lNot.setAutoritaEsterna(lAut);

		lNotifiche[0] = lNot;

		// Inserisco l'array di Notifiche nell'Evento
		lEve.setNotifiche(lNotifiche);

		// ---MANCA la PENA_RESIDUA---

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lRetModel = lCtrl.ExInserisciEventoNotifica(lEve);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;

	}

}
