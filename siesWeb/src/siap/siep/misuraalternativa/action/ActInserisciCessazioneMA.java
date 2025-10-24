package siap.siep.misuraalternativa.action;

/**
 * <p>Title: ActInserisciRevocaMA</p>
 * <p>Description: Classe Action per l'inserimento di MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.action.ICostantiMagistrato;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.action.ICostantiPosizioneGiuridica;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.tenore.model.TenoreModel;

public class ActInserisciCessazioneMA extends ActMisuraAlternativa implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento della Cessazione MisuraAlternativa per: - DETENZIONE DOMICILIARE - SEMILIBERTA' -
	 * DETENZIONE DOMICILIARE A TERMINE
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);
		this.setRequestAttribute("posizionegiuridica", PosizioneGiu);

		// =============================================
		// Recupero i dati del provvedimento SIEP
		// =============================================
		EventoNotificaModel lEveNot = null;
		lEveNot = this.getEventoNotifica();

		// ==========================================================================
		// Provo a recuperare il provvedimento della Sorveglianza (Ordinanza/Decreto)
		// se selezionato dalla lista e non modificato
		// ==========================================================================
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lrevoca = null;
		BigDecimal lIdOrdinanza = getRequestBigDecimalParameter(
				ICostantiMisuraAlternativa.CAMPO_ID_DOCUMENTO_SIUS);
		if (lIdOrdinanza != null && !lIdOrdinanza.toString().equals(""))
			lrevoca = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdOrdinanza);

		// Non c'è un decreto di sospensione in misura alternativa
		if (lrevoca == null) {
			// INSERISCO EVENTO E NOTIFICA DEL TDS/MDS
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MA assente la inserisco");
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

			String lTipoDecisione = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_TIPO_DECISIONE);

			lEveMod.setEvento(setEventoOrdinazaDecretoMisuraAlternativa(lEveMod.getEvento(), lTipoDecisione,
					lCodiceUffEmi, lComModAutEmi, lDataEmisTras));

			// setto il deposito ordinanza
			DepositoOrdinanzaPcModel lDepOrdMod = setDepositoOrdinanzaPc(lCodiceUffEmi);

			// setto il tenore // Daniela 15.12.2010 modificato da 0006 a 0018
			// TenoreModel lTenMod = setTenore(new BigDecimal(1),"0018"); // FIXME DL 146 verificare il TENORE
			// 0010 se MDS
			// setto il tenore // Daniela 16.12.2010 modificato da 0006 a 0018
			// ESITO_TENORE
			// TDS = 0018 = Dichiara Non Validamente Espiata la Pena
			// MDS (51bis) = 0010 = Dichiara inefficace/cessata la misura
			// TDS (51bis) = 0348 - Accoglie il reclamo del PM e dichiara cessata la misura
			TenoreModel lTenMod = null;
			String lTipoUffSorv = getRequestStringParameter(
					ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA);
			if (lTipoUffSorv.startsWith("UDS")) {
				lTenMod = setTenore(new BigDecimal(1), "0010");
			} else if (lTipoUffSorv.startsWith("TDS")) {
				List<String> lCodiciTDS51Bis = Arrays.asList("1200", "1201", "1202", "1203", "1204", "1205",
						"1206", "1207", "1208", "1209", "1211");

				if (lCodiciTDS51Bis.contains(lEveMod.getEvento().getCodMotivo()))
					lTenMod = setTenore(new BigDecimal(1), "0348");
				else
					lTenMod = setTenore(new BigDecimal(1), "0018");
			}

			// misura alternativa
			MisuraAlternativaModel lMisMod = new MisuraAlternativaModel();
			lMisMod = setMisuraAlternativa(lTipoDecisione, "CE", lCodiceUffEmi,
					getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO), "-");
			lMisMod.setDataInizioMisura(getRequestDateParameter(ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE,
					ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE, ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE));

			// ADD DL146 gestione detenuto/non detenuto per Detenzione DOmiciliare + a termine
			if (!isRequestParameterNullObj("tipo")) {
				// add DL 146
				String lTipo = getRequestStringParameter("tipo");
				if (lTipo.equals("nondetenuto")) {
					lMisMod.setCodTipoUfficioScarcerazione("PROC");
				} else if (lTipo.equals("detenuto")) {
					lMisMod.setCodTipoUfficioScarcerazione("SORV");
					if (!isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)
							&& !getRequestStringParameter(
									ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO).trim()
											.equals("")) {
						lMisMod.setDataIngressoIstituto(getRequestDateParameter(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
					}
				}
			}

			// ========================================================================
			// INSERISCO LA MA con Decreto/Ordinanza
			// ========================================================================
			IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
			MisuraAlternativaModel lMisuraModel = lCtrlMisura
					.ExInserisciMisuraAlternativaEventoNotifica(lEveMod, lDepOrdMod, lTenMod, lMisMod);

			// ========================================================================
			// Lego il Provvedimento SIEP al'ordinanza
			// ========================================================================
			lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());
		} else // la misura esiste
		{
			// Dovrei andare in aggiornamento delle Note, Scarcerata/da scarcerare, data ingresso in istituto
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("MA presente");

			lEveNot.getEvento().setEveIdEvento(lIdOrdinanza);

			// ===================================
			// Aggiorno i campi della MA
			// ===================================
			if (!this.isRequestParameterNullObj(ICostantiMisuraAlternativa.CAMPO_NOTE))
				lrevoca.setNote(getRequestStringParameter(ICostantiMisuraAlternativa.CAMPO_NOTE));

			if (!isRequestParameterNullObj("tipo")) {
				// add DL 146
				String lTipo = getRequestStringParameter("tipo");
				if (lTipo.equals("nondetenuto")) {
					lrevoca.setCodTipoUfficioScarcerazione("PROC");
					lrevoca.setDataIngressoIstituto(null);
				} else if (lTipo.equals("detenuto")) {
					lrevoca.setCodTipoUfficioScarcerazione("SORV");
					if (!isRequestParameterNullObj(
							ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO)
							&& !getRequestStringParameter(
									ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO).trim()
											.equals("")) {
						lrevoca.setDataIngressoIstituto(getRequestDateParameter(
								ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_MESE_DATA_INGRESSO_ISTITUTO,
								ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_INGRESSO_ISTITUTO));
					} else {
						lrevoca.setDataIngressoIstituto(null);
					}
				}
			}
		}

		// ==========================================================================
		//
		// ==========================================================================
		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// ==========================================================================
		// Inserimento Evento SIEP e Inserimento/aggiornamento provvedimento SIUS
		// ==========================================================================
		EventoNotificaModel lRetModel = new EventoNotificaModel();
		IMisuraAlternativa lCtrlMisura = SICOLookupRemote.getMisuraAlternativaRemote();
		lRetModel = lCtrlMisura.ExInserisciOModificaMANotifica(lEveNot, lPenaRes, lrevoca, null);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.misuraalternativa.action.ActDettaglioCessazioneMA" + "&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento();

		return lPage;
	}

	/**
	 * Restituisce EventoNotificaModel del provvedimento dell'esecuzione (SIEP) opportunamente valorizzato
	 * 
	 * @return EventoNotificaModel
	 */
	private EventoNotificaModel getEventoNotifica() throws F3BException {

		String PosizioneGiu = getRequestStringParameter(
				ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA);

		EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEveNot.getEvento().setCodTipoProvvedimento("06");

		lEveNot.setEvento(setEventoProvvedimentoMisuraAlternativa(lEveNot.getEvento()));

		// Posizioni gestite: Libero, In Misura, In sospensione della Misura
		// 07-10 = Libero
		// 12 - Espiazione Pena in Regime di Detenzione Domiciliare
		// 14 - Espiazione Pena in Regime di Semiliberta'
		// 29 - Detenzione Domiciliare Provvisoria (leggi det dom termine)
		// 31 - Sospensione Cautelativa 51 Ter (di Det. Domiciliare)
		// 33 - Sospensione Cautelativa 51 Ter (di Semiliberta')
		// 36 - Sospensione Provvisoria 51 Bis (di Det. Domiciliare)
		// 38 - Sospensione Provvisoria 51 Bis (di Semiliberta')
		if (PosizioneGiu.equals("29") || PosizioneGiu.equals("31") || PosizioneGiu.equals("33")
				|| PosizioneGiu.equals("36") || PosizioneGiu.equals("38") || PosizioneGiu.equals("12")
				|| PosizioneGiu.equals("14") || PosizioneGiu.equals("10") || PosizioneGiu.equals("07")) {
			String codiceMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);

			// =====================================
			// Rimappatura Codici MDS DL 146/2013
			// =====================================
			if (codiceMotivo.equals("2284"))
				lEveNot.getEvento().setCodMotivo("5433"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("2285"))
				lEveNot.getEvento().setCodMotivo("5434"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("2287"))
				lEveNot.getEvento().setCodMotivo("5435"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("2288"))
				lEveNot.getEvento().setCodMotivo("5436"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("2286"))
				lEveNot.getEvento().setCodMotivo("5439"); // Det Dom Termine
			else if (codiceMotivo.equals("2283"))
				lEveNot.getEvento().setCodMotivo("5437"); // Semilibertà
			// =================================================
			// Rimappatura Codici TDS su Reclamo DL 146/2013
			// =================================================
			else if (codiceMotivo.equals("1203"))
				lEveNot.getEvento().setCodMotivo("5453"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("1205"))
				lEveNot.getEvento().setCodMotivo("5454"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("1206"))
				lEveNot.getEvento().setCodMotivo("5455"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("1207"))
				lEveNot.getEvento().setCodMotivo("5456"); // Detenzione Domiciliare
			else if (codiceMotivo.equals("1204"))
				lEveNot.getEvento().setCodMotivo("5459"); // Det Dom Termine
			else if (codiceMotivo.equals("1208"))
				lEveNot.getEvento().setCodMotivo("5457"); // Semilibertà
			// =============================================
			// Vecchi Codici TDS si mantiene la mappatura
			// con gli Oggetti della Sorveglianza
			// =============================================
			else
				lEveNot.getEvento().setCodMotivo(codiceMotivo);
		} else
			lEveNot.getEvento().setCodMotivo("0000");

		// lEveNot.getEvento().setEveIdEvento(lMisuraModel.getEveIdEvento());

		lEveNot.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));

		// Notifiche
		NotificaModel[] lNotificheMod = this.setNotificheMisuraAlternativa();
		lEveNot.setNotifiche(lNotificheMod);

		return lEveNot;

	}

}