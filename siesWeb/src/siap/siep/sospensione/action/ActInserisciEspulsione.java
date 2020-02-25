package siap.siep.sospensione.action;

/**
 * <p>Title: ActInserisciEspulsione</p>
 * <p>Description: Classe Action per l'inserimento di Espulsione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misuraalternativa.action.ActMisuraAlternativa;
import siap.siep.notifica.action.ICostantiNotifica;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.model.VerbaleModel;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciEspulsione extends ActMisuraAlternativa implements ICostantiSospensione {

	/**
	 * Azione di Inserimento del Sospensione Espulsione
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		// Prendo i model passati dal jsp in sessione
		VerbaleModel lVerMod = (VerbaleModel) getSessionAttribute("verbale");
		SospensioneModel lSospensione = (SospensioneModel) getSessionAttribute("sospensione");
		PenaResiduaModel lPenaNuova = (PenaResiduaModel) getSessionAttribute("penaresiduanuova");
		EventoModel lEveVer = (EventoModel) getSessionAttribute("evento");

		// Evento Provvedimento
		EventoModel lEvePro = new EventoModel();

		lEvePro.setCodTipoEvento("01");
		lEvePro.setCodTipoProvvedimento("12");
		lEvePro.setCodMotivo("2141");
		lEvePro.setFlagStampaSiep("S");
		lEvePro.setFlagVideoSiep("S");
		lEvePro.setCodUfficioEmittente(lCodiceUfficio);
		lEvePro.setFasSieIdFascicoloSiep(lIdFascicolo);
		Date lDataEmi = getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
				ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE);
		lEvePro.setDataEmissione(lDataEmi);
		lEvePro.setCodOperatoreInserimento(lCodiceOperatore);
		lEvePro.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());
		lEvePro.setDataInserimento(DateUtils.getSysDate());
		lEvePro.setCodUfficioInserimento(lCodiceUfficio);
		lEvePro.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEvePro.setCodEsito("-");

		lEvePro.setCodLuogoDestinatario("-");
		lEvePro.setCodTipoUfficioDestinatario("-");
		lEvePro.setCodMagistrato(calcolaMagistrato());

		EventoNotificaModel lEveNot = new EventoNotificaModel();
		lEveNot.setEvento(lEvePro);

		// notifiche
		// ufficio recupero crediti
		Date lDataTrasmissione = getRequestDateParameter(ICostantiNotifica.CAMPO_ANNO_DATA_INVIO,
				ICostantiNotifica.CAMPO_MESE_DATA_INVIO, ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO);
		ArrayList lNotifiche = new ArrayList();

		if (!this.isRequestParameterNullObj("UfficioRecuperCrediti")
				&& this.getRequestStringParameter("UfficioRecuperCrediti") != null
				&& !this.getRequestStringParameter("UfficioRecuperCrediti").equals("-")) {
			String lUfficio = this.getRequestStringParameter("UfficioRecuperCrediti");
			String lSedeUfficio = this.getRequestStringParameter("SedeUfficioRecuperCrediti");
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("C");
			lNotModPol.setDataInvio(lDataTrasmissione);

			// SETTO le note delle sanzione alternative
			if (!this.isRequestParameterNullObj(ICostantiNotifica.CAMPO_NOTE)
					&& this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE) != null
					&& !this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE).equals(""))

			{
				lNotModPol.setNote(this.getRequestStringParameter(ICostantiNotifica.CAMPO_NOTE));
			}

			String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);

			lNotifiche.add(lNotModPol);
		}

		NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
		List lNotDin = new ArrayList(Arrays.asList(lNotificheMod));

		lNotDin.addAll(lNotifiche);
		lEveNot.setNotifiche((NotificaModel[]) lNotDin.toArray(new NotificaModel[0]));

		// Inserimento model
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		EventoModel lEveMod = lCtrlSosp.ExInserisciEventoNotVerbaleSospPena(lEveVer, lVerMod, lSospensione,
				lPenaNuova, lEveNot);

		// Prepara la pagina di destinazione
		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.sospensione.action.ActLoadDettaglioEspulsione&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveMod.getIdEvento().toString();

		// rimuovo gli oggetti dalla sessione
		removeSessionAttribute("verbale");
		removeSessionAttribute("sospensione");
		removeSessionAttribute("penaresiduanuova");
		removeSessionAttribute("evento");

		return lPage;
	}

}