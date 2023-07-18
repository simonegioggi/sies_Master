package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciRigetto</p>
 * <p>Description: Classe Action per l'inserimento di Rigetto</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Date;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciRigetto extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {
	/**
	 * Azione di Inserimento del MisuraAlternativa
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {
		MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
		EventoNotificaModel lRetModel = new EventoNotificaModel();

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		String PosizioneGiu = this
				.getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		setRequestAttribute("posizionegiuridica", PosizioneGiu);

		String lPage = null;

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisuraMod = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lMisuraMod = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// se non è presente la misuralaternativa concessa la inserisco
		if (lMisuraMod == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS
			EventoNotificaModel lEveMod = new EventoNotificaModel();
			lEveMod.getEvento().setCodMotivo(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO));
			String lCodiceUffEmi = getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA),
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT));
			ComuneModel lComModAutEmi = new ComuneModel(getCodComuneByDescr(
					getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT)));
			Date lDataEmisTras = getRequestDateParameter(ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE,
					ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE);
			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), "03",
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));
			if (PosizioneGiu != null && !PosizioneGiu.equals("03"))
				lEveMod.getEvento().setFlagDocumentoRegistrato("N");
			else
				lEveMod.getEvento().setFlagDocumentoRegistrato("S");

			// setto il deposito ordinanza pc
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore
			TenoreModel lTenMod = setTenore(new BigDecimal(1), "0002");

			IPenaResidua lPenResCtrl = SIEPLookupRemote.getPenaResiduaRemote();
			lPenResCtrl.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

			// Misura alternativa
			lMisMod = setMisuraAlternativa("03", "RG", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");
			MisuraAlternativaModel lMisuraModel = new MisuraAlternativaModel();
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			lMisuraModel = lCtrlMisura.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod,
					lTenMod, lMisMod);

			if (PosizioneGiu != null && PosizioneGiu.equals("03")) {

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMARigetto&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lMisuraModel.getEveIdEvento();
			}

			// INSERISCO EVENTO E NOTIFICA DELL'UFFICIO EMITTENTE
			if (PosizioneGiu != null && !PosizioneGiu.equals("03")) {
				EventoNotificaModel lEveNot = new EventoNotificaModel();
				lEveNot.getEvento().setCodTipoProvvedimento("06");
				switch (Integer.parseInt(PosizioneGiu)) {
				case 29: // Detenzione Domiciliare Provvisoria
				{
					lEveNot.getEvento().setCodMotivo("0218");
					break;
				}
				case 46: // LIBERO in Sospensione
				case 47: // LIBERO in Sospensione
				{
					// Ticket#20230717016 - SIEP - Validazione provvedimento
					// il codice 0217 èì già in uso dalla "Sospensioni/Interruzioni del PM - Revoca Sospensioni"
					// si sostituisce con il nuovo codice 1099
					//lEveNot.getEvento().setCodMotivo("0217");
					lEveNot.getEvento().setCodMotivo("1099");
					// Ticket#20230717016 - FINE
					break;
				}
				default: {
					lEveNot.getEvento().setCodTipoProvvedimento("04");
					lEveNot.getEvento().setCodMotivo("0000");
					break;
				}
				}
				lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));
				lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

				// notifiche
				NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
				lEveNot.setNotifiche(lNotificheMod);

				BigDecimal lIdPenaRes = getRequestBigDecimalParameter(
						ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
				PenaResiduaModel lPenaRes = new PenaResiduaModel();
				lPenaRes.setIdPenaResidua(lIdPenaRes);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				IMisuraAlternativa lCtrlMisuraAlt = SICOLookupRemote.getMisuraAlternativaRemote();
				EventoNotificaModel lEveNotModel = lCtrlMisuraAlt.ExInserisciOModificaMANotifica(lEveNot,
						lPenaRes, null, null);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMARigetto&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lEveNotModel.getEvento().getIdEvento();
			}
		} else {
			// la misura alternativa esiste
			if (!PosizioneGiu.equals("03")) {
				EventoNotificaModel lEve = new EventoNotificaModel();
				lEve.getEvento().setCodTipoProvvedimento("06");
				switch (Integer.parseInt(PosizioneGiu)) {
				case 29: // Detenzione Domiciliare Provvisoria
				{
					lEve.getEvento().setCodMotivo("0218");
					break;
				}
				case 46: // LIBERO in Sospensione
				case 47: // LIBERO in Sospensione
				{
					// Ticket#20230717016 - SIEP - Validazione provvedimento
					// il codice 0217 èì già in uso dalla "Sospensioni/Interruzioni del PM - Revoca Sospensioni"
					// si sostituisce con il nuovo codice 1099
					//lEveNot.getEvento().setCodMotivo("0217");					
					lEve.getEvento().setCodMotivo("1099");
					// Ticket#20230717016 - fine
					break;
				}
				default: {
					lEve.getEvento().setCodTipoProvvedimento("04");
					lEve.getEvento().setCodMotivo("0000");
					break;
				}
				}

				lEve.setEvento(setEventoProvvedimentoMisuraAlternativa(lEve.getEvento()));
				lEve.getEvento().setEveIdEvento(lIdOrdinanza);

				// Inserisco l'array di Notifiche nell'Evento
				NotificaModel[] lNotifiche = this.setNotificheMisuraAlternativa();
				lEve.setNotifiche(lNotifiche);

				BigDecimal lIdPenaRes = getRequestBigDecimalParameter(
						ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
				PenaResiduaModel lPenaRes = new PenaResiduaModel();
				lPenaRes.setIdPenaResidua(lIdPenaRes);
				if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
					lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
							ICostantiPenaResidua.CAMPO_MESE_DATA_FINE,
							ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

				lRetModel = lMisAltCtrl.ExInserisciOModificaMANotifica(lEve, lPenaRes, null, null);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMARigetto&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();
			} else {
				if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
					lMisuraMod.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));
				lMisAltCtrl.ExModificaMisuraAlternativa(lMisuraMod);

				lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
						+ "=siap.siep.misuraalternativa.action.ActDettaglioMARigetto&"
						+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lMisuraMod.getEveIdEvento();
			}
		}

		return lPage;
	}
}