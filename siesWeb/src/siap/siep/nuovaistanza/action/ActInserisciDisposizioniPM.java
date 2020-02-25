package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.web.ActionSiap;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.nuovaistanza.controller.INuovaIstanza;
import siap.siep.nuovaistanza.model.NuovaIstanzaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActInserisciDisposizioniPM extends ActionSiap {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		// String lMagistrato = this.getRequestStringParameter("CodMagistrato");
		BigDecimal lIstanza = this.getRequestBigDecimalParameter("IdNuovaIstanza");
		BigDecimal lEventoIstanza = this.getRequestBigDecimalParameter("IdEventoNuovaIstanza");

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		if (lFascMod.getCodStatoFascicolo().compareTo("01") == 0)
			throw new F3BException(F3BException.USER_MESSAGE,
					"Attenzione : Il fascicolo indicato è già archiviato! Impossibile procedere.");

		EventoModel lEveMod = new EventoModel();
		EventoNotificaModel lEveNotMod = new EventoNotificaModel();

		lEveMod.setCodEsito("-");
		lEveMod.setFasSieIdFascicoloSiep(lIdFascicolo);
		lEveMod.setCodTipoEvento("03");
		lEveMod.setCodTipoProvvedimento("08");
		lEveMod.setCodMotivo("1002");

		lEveMod.setEveIdEvento(lEventoIstanza);

		lEveMod.setDataEmissione(getRequestDateParameter(ICostantiNuovaIstanza.CAMPO_ANNO_DISPOSIZIONE,
				ICostantiNuovaIstanza.CAMPO_MESE_DISPOSIZIONE,
				ICostantiNuovaIstanza.CAMPO_GIORNO_DISPOSIZIONE));
		lEveMod.setCodUfficioEmittente(this.getCodUfficioUtenteConnesso());
		lEveMod.setCodLuogoEmittente(this.getCodComuneUtenteConnesso());

		lEveMod.setCodTipoUfficioDestinatario("-");
		lEveMod.setCodLuogoDestinatario("-");
		lEveMod.setCodUfficioDestinatario("-");

		// lEveMod.setCodMagistrato(lMagistrato);
		// Gestisco l'inserimento del firmatario
		if (!isRequestParameterNullObj("selFirm")) {
			if (getRequestStringParameter("selFirm").equalsIgnoreCase("mag")) {
				lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
			} else if (getRequestStringParameter("selFirm").equalsIgnoreCase("fun")) {
				lEveMod.setNomeSoggettoPresentante(getRequestStringParameter("NomeFunzionario"));
				lEveMod.setCognomeSoggettoPresentante(getRequestStringParameter("CognomeFunzionario"));
				lEveMod.setCodMagistrato("-");
			}
		} else {
			lEveMod.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		}

		// lEveMod.setFlagVideoSiep("S");
		// lEveMod.setFlagStampaSiep("S");

		lEveMod.setFlagDocumentoRegistrato("N");

		lEveMod.setFlagDocumentoRegistrato("N");
		lEveMod.setAnnoProtocollo(new BigDecimal(DateUtils.getSysDate("yyyy")));
		lEveMod.setCodOperatoreInserimento(this.getCodUtenteConnesso());
		lEveMod.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
		lEveMod.setDataInserimento(DateUtils.getSysDate());

		// setto l'evento dentro l'eventonotificaModel
		lEveNotMod.setEvento(lEveMod);

		// setto le notifiche

		lEveNotMod.setNotifiche(this.loadNotifiche());

		// setto il campo note
		if (getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_NOTE) != null
				&& !getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_NOTE).equals("")) {
			CampoNotaModel lCamNot = new CampoNotaModel();
			lCamNot.setCodOperatoreInserimento(this.getCodUtenteConnesso());
			lCamNot.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
			lCamNot.setDataInserimento(DateUtils.getSysDate());
			lCamNot.setFasSieIdFascicoloSiep(lIdFascicolo);
			lCamNot.setDescr(getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_NOTE));
			lCamNot.setProgressivo(new BigDecimal(1));

			Vector lCampNote = new Vector();
			lCampNote.add(lCamNot);
			lEveNotMod.setCampoNote((CampoNotaModel[]) lCampNote.toArray(new CampoNotaModel[0]));
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("EventoNotificheTOTALE" + lEveNotMod);

		NuovaIstanzaModel lNuoMod = new NuovaIstanzaModel();
		lNuoMod.setIdNuovaIstanza(lIstanza);
		lNuoMod.setDataAggiornamento(DateUtils.getSysDate());
		lNuoMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		lNuoMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		lNuoMod.setCodStatoIstanza(
				this.getRequestStringParameter(ICostantiNuovaIstanza.CAMPO_COD_STATO_ISTANZA));

		INuovaIstanza lCtrl = SIEPLookupRemote.getNuovaIstanzaRemote();
		EventoNotificaModel lEveRet = lCtrl.ExDisposizioneNuovaIstanza(lNuoMod, lEveNotMod);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("EventoNotifiche Correttamente Inserito");

		String lPage = "";
		lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.nuovaistanza.action.ActLoadDettaglioNuovaIstanza";
		lPage += "&" + "IdEvento" + "=" + lEveRet.getEvento().getIdEvento();
		lPage += "&" + "TipoVis" + "=Disposizione";
		lPage += "&AzioneChiamante=siap.siep.nuovaistanza.action.ActInserisciDisposizioniPM";

		return lPage;
	}

	/******************************************************************/
	protected NotificaModel[] loadNotifiche() throws F3BException
	/******************************************************************/
	{
		String lCodiceOperatore = this.getCodUtenteConnesso();
		String lCodiceUfficio = this.getCodUfficioUtenteConnesso();

		String annorif = null;
		String numerorif = null;

		Date lDataEmissione = getRequestDateParameter(ICostantiNuovaIstanza.CAMPO_ANNO_DISPOSIZIONE,
				ICostantiNuovaIstanza.CAMPO_MESE_DISPOSIZIONE,
				ICostantiNuovaIstanza.CAMPO_GIORNO_DISPOSIZIONE);
		ArrayList lNotifiche = new ArrayList();

		// SETTO NOTIFICA ISTITUTO DI DETENZIONE
		if (!this.isRequestParameterNullObj("IstitutoDet")
				&& !getRequestStringParameter("IstitutoDet").equals("")) {
			String lIdIstituto = this.getRequestStringParameter("IstitutoDet");

			NotificaModel lNotMod = new NotificaModel();
			lNotMod.setCodTipoNotifica("C");
			lNotMod.setIstDetIdIstitutoDetenzione(lIdIstituto);
			lNotMod.setDataInvio(lDataEmissione);
			lNotMod.setCodEsito("-");
			lNotMod.setCodOperatoreInserimento(lCodiceOperatore);
			lNotMod.setDataInserimento(DateUtils.getSysDate());
			lNotMod.setCodUfficioInserimento(lCodiceUfficio);

			lNotifiche.add(lNotMod);
		}

		// SETTO NOTIFICA UFFICIO GE
		if (!this.isRequestParameterNullObj("ufficioGE")
				&& this.getRequestStringParameter("ufficioGE") != null
				&& !this.getRequestStringParameter("ufficioGE").equals("-")) {
			String lUfficio = this.getRequestStringParameter("ufficioGE");
			String lSedeUfficio = this.getRequestStringParameter("sedeGE");
			NotificaModel lNotModPol = new NotificaModel();

			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("NG");
			lNotModPol.setDataInvio(lDataEmissione);

			annorif = this.getRequestStringParameter("ARG_ge");
			numerorif = this.getRequestStringParameter("NRG_ge");

			if (!annorif.equals("") && !numerorif.equals("")) {
				// String lCodUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
				lNotModPol.setNote("in riferimento al procedimento numero " + annorif + "/" + numerorif + " "
				// +getUfficioByCodUfficio(lCodUfficio).getDescrTipoUfficio() + " di "+lSedeUfficio
				);
			}
			String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);

			lNotifiche.add(lNotModPol);
		}

		// SETTO NOTIFICA Magistrato di Sorveglianza
		if (!this.isRequestParameterNullObj("ufficioMds")
				&& this.getRequestStringParameter("ufficioMds") != null
				&& !this.getRequestStringParameter("ufficioMds").equals("-")) {
			String lUfficio = this.getRequestStringParameter("ufficioMds");
			String lSedeUfficio = this.getRequestStringParameter("sedeMds");
			NotificaModel lNotModPol = new NotificaModel();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("SETTO NOTIFICA UFFICIO MDS");
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("MS");
			lNotModPol.setDataInvio(lDataEmissione);

			annorif = this.getRequestStringParameter("ARG_mds");
			numerorif = this.getRequestStringParameter("NRG_mds");
			// String lCodUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setNote("in riferimento al procedimento numero " + annorif + "/" + numerorif + " "
			// +getUfficioByCodUfficio(lCodUfficio).getDescrTipoUfficio() + " di "+lSedeUfficio
			);

			String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);
			lNotifiche.add(lNotModPol);
		}

		// SETTO TDS
		if (!this.isRequestParameterNullObj("ufficioTds") && getRequestStringParameter("ufficioTds") != null
				&& !getRequestStringParameter("ufficioTds").equals("-")) {
			NotificaModel lNotModTDS = new NotificaModel();
			String lSedeTribunale = this.getRequestStringParameter("sedeTds");
			String lUfficio = this.getRequestStringParameter("ufficioTds");

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("SETTO NOTIFICA UFFICIO TDS");
			lNotModTDS.setCodEsito("-");
			lNotModTDS.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModTDS.setDataInserimento(DateUtils.getSysDate());
			lNotModTDS.setCodUfficioInserimento(lCodiceUfficio);
			lNotModTDS.setCodTipoNotifica("TS");
			lNotModTDS.setDataInvio(lDataEmissione);

			annorif = this.getRequestStringParameter("ARG_tds");
			numerorif = this.getRequestStringParameter("NRG_tds");
			// String lCodUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeTribunale);
			lNotModTDS.setNote("in riferimento al procedimento numero " + annorif + "/" + numerorif + " "
			// +getUfficioByCodUfficio(lCodUfficio).getDescrTipoUfficio() + " di "+lSedeTribunale
			);

			String lCodiceUff = getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeTribunale);
			lNotModTDS.setUffCodUfficio(lCodiceUff);

			lNotifiche.add(lNotModTDS);
		}

		// SETTO NOTIFICA PM
		if (!this.isRequestParameterNullObj("ufficioPM")
				&& this.getRequestStringParameter("ufficioPM") != null
				&& !this.getRequestStringParameter("ufficioPM").equals("-"))

		{
			String lUfficio = this.getRequestStringParameter("ufficioPM");
			String lSedeUfficio = this.getRequestStringParameter("sedePM");
			NotificaModel lNotModPol = new NotificaModel();

			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("SETTO NOTIFICA UFFICIO PM");
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("E");
			lNotModPol.setDataInvio(lDataEmissione);

			annorif = this.getRequestStringParameter("ARG_pm");
			numerorif = this.getRequestStringParameter("NRG_pm");
			// String lCodUfficio = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setNote("in riferimento al procedimento numero " + annorif + "/" + numerorif + " "
			// +getUfficioByCodUfficio(lCodUfficio).getDescrTipoUfficio() + " di "+lSedeUfficio
			);

			String lUff = this.getCodUfficioByCodTipoUfficioDescrComune(lUfficio, lSedeUfficio);
			lNotModPol.setUffCodUfficio(lUff);

			lNotifiche.add(lNotModPol);

		}

		// SETTO NOTIFICA ALTRA AUTORITA NC
		if (!this.isRequestParameterNullObj("AutE_condannato")
				&& this.getRequestStringParameter("AutE_condannato") != null
				&& !this.getRequestStringParameter("AutE_condannato").equals("-")
				&& !this.getRequestStringParameter("sedeAutE_Condannato").equals(""))

		{
			String lPolizia = this.getRequestStringParameter("AutE_condannato");
			String lSedePolizia = this.getRequestStringParameter("sedeAutE_Condannato");
			NotificaModel lNotModPol = new NotificaModel();
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.info("SETTO NOTIFICA Altra Autorità");
			if (!this.isRequestParameterNullObj("IndAutE_Condannato")) {
				String lNotePolizia = this.getRequestStringParameter("IndAutE_Condannato");
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("NC");
			lNotModPol.setDataInvio(lDataEmissione);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		// SETTO Le NOTIFICHE ai Difensori

		// Avvocato
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();
		try {
			lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(lIdFascicolo);
		} catch (Exception ex) {
			// setRequestAttribute("avvocati", lAvvocati);
		}
		int lIdxAvv = 1;
		Iterator lItxAvv = lAvvocati.iterator();
		while (lItxAvv.hasNext()) {
			AvvocatoSiepModel lAvv = (AvvocatoSiepModel) lItxAvv.next();
			if (!this.isRequestParameterNullObj("AutE_" + lIdxAvv)
					&& this.getRequestStringParameter("AutE_" + lIdxAvv) != null
					&& !this.getRequestStringParameter("AutE_" + lIdxAvv).equals("-")) {
				// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
				// LogF3B.getLogger()
				siesLogger.info("SETTO NOTIFICA avvocato " + lIdxAvv);
				NotificaModel lNotModPol = new NotificaModel();

				lNotModPol.setAvvSiep(lAvv);
				if (!this.isRequestParameterNullObj("IndAutE_" + lIdxAvv)) {
					String lNote = this.getRequestStringParameter("IndAutE_" + lIdxAvv);
					lNotModPol.setNote(lNote);
				}
				lNotModPol.setCodEsito("-");
				lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
				lNotModPol.setDataInserimento(DateUtils.getSysDate());

				lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
				lNotModPol.setCodTipoNotifica("ND");
				lNotModPol.setDataInvio(lDataEmissione);
				String lPolizia = this.getRequestStringParameter("AutE_" + lIdxAvv);
				String lSedePolizia = this.getRequestStringParameter("sedeAutE_" + lIdxAvv);
				String lNoteAvvocato = this.getRequestStringParameter("IndAutE_" + lIdxAvv);
				lNotModPol.setNote(lNoteAvvocato);

				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
				AutoritaEsternaModel lAut = new AutoritaEsternaModel();
				lAut.setCodTipoAutorita(lPolizia);
				lAut.setCodSede(lComMod.getCodComune());
				lAut.setCodOperatoreInserimento(this.getCodUtenteConnesso());
				lAut.setCodUfficioInserimento(this.getCodUfficioUtenteConnesso());
				lAut.setDataInserimento(DateUtils.getSysDate());
				lNotModPol.setAutoritaEsterna(lAut);

				lNotifiche.add(lNotModPol);

			}
			lIdxAvv++;
		}

		// ALTRO DESTINATARIO
		if (!this.isRequestParameterNullObj("AutE_altro")
				&& this.getRequestStringParameter("AutE_altro") != null
				&& !this.getRequestStringParameter("AutE_altro").equals("-")
				&& !this.getRequestStringParameter("sedeAutE_altro").equals(""))

		{
			String lPolizia = this.getRequestStringParameter("AutE_altro");
			String lSedePolizia = this.getRequestStringParameter("sedeAutE_altro");
			NotificaModel lNotModPol = new NotificaModel();
			//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			//// LogF3B.getLogger()
			// siesLogger.info("SETTO NOTIFICA Altro Destinatario");
			if (!this.isRequestParameterNullObj("IndAutE_altro")) {
				String lNotePolizia = this.getRequestStringParameter("IndAutE_altro");
				lNotModPol.setNote(lNotePolizia);
			}
			lNotModPol.setCodEsito("-");
			lNotModPol.setCodOperatoreInserimento(lCodiceOperatore);
			lNotModPol.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setCodUfficioInserimento(lCodiceUfficio);
			lNotModPol.setCodTipoNotifica("AA");
			lNotModPol.setDataInvio(lDataEmissione);

			AutoritaEsternaModel lAut = new AutoritaEsternaModel();
			lAut.setCodTipoAutorita(lPolizia);
			ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lSedePolizia));
			lAut.setCodSede(lComMod.getCodComune());
			lAut.setCodOperatoreInserimento(lCodiceOperatore);
			lAut.setCodUfficioInserimento(lCodiceUfficio);
			lAut.setDataInserimento(DateUtils.getSysDate());
			lNotModPol.setAutoritaEsterna(lAut);

			lNotifiche.add(lNotModPol);
		}

		return (NotificaModel[]) lNotifiche.toArray(new NotificaModel[0]);
	}

}