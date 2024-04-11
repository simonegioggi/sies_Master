package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * ActStampaMAAmmProvvisoria - Produce il documento
 *
 * @version 1.0
 */

public class ActStampaMAAmmProvvisoria extends ActionSiap implements ICostantiMisuraAlternativa {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();
		String lTipoMisura = this.getRequestStringParameter("tipoMisura");

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());
		String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();
		lPosGiu.setCodPosizioneGiuridica(lPosizioneGiu);

		String lCodTipoPosGiuridicaAltraCausa = "";
		if (lPosAltra.getAltraCausa() != null && lPosAltra.getAltraCausa().getCodTipoPosGiuridica() != null
				&& !lPosAltra.getAltraCausa().getCodTipoPosGiuridica().equals("")) {
			lCodTipoPosGiuridicaAltraCausa = lPosAltra.getAltraCausa().getCodTipoPosGiuridica();
		}

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		String lMotivo = lEventoModel.getCodMotivo();
		// Ambrosino a9-rr-077
		String lTipoProvv = lEventoModel.getCodTipoProvvedimento();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());
		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		// ricerca misura alternativa
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = new MisuraAlternativaModel();

		lMisAlModConcessa = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());
		String lflagScarcerato = lMisAlModConcessa.getCodTipoUfficioScarcerazione();

		// ricerca del template
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		TemplateModel lTemMod = new TemplateModel();
		String flagTemplate = null;

		if (lTipoMisura.equals("AFFIDAMENTO")) {
			 if (lEventoModel.getCodMotivo().equals("2008")){
				if (lflagScarcerato.equals("PROC")) {
					if (lPosizioneGiu.equals("01") || lPosizioneGiu.equals("14") || lPosizioneGiu.equals("03")
							|| lPosizioneGiu.equals("73") || lPosizioneGiu.equals("74")
							|| lPosizioneGiu.equals("75") || lPosizioneGiu.equals("76")
							|| lPosizioneGiu.equals("77"))
						flagTemplate = "1"; // Detenuto O Semilibertà
					else if (lPosizioneGiu.equals("02") || lPosizioneGiu.equals("04")
							|| lPosizioneGiu.equals("50") || lPosizioneGiu.equals("53")
							|| lPosizioneGiu.equals("70") || lPosizioneGiu.equals("71")
							|| lPosizioneGiu.equals("72") || lPosizioneGiu.equals("78")
							|| lPosizioneGiu.equals("79") || lPosizioneGiu.equals("80")
							|| lPosizioneGiu.equals("81") || lPosizioneGiu.equals("82")
							|| lPosizioneGiu.equals("83") || lPosizioneGiu.equals("84"))
						flagTemplate = "2"; // Arresti Domiciliari
					else if (lPosizioneGiu.equals("12") || lPosizioneGiu.equals("25")
							|| lPosizioneGiu.equals("29") || lPosizioneGiu.equals("41")
							|| lPosizioneGiu.equals("44"))
						flagTemplate = "6"; // Detenzione Domiciliari (anche provvisoria 51bis)
					else if (lPosizioneGiu.equals("54"))
						// 54 - In Ammissione Provvisoria ovvero dopo la registrazione del verbale inizio
						// misura
						// Sto stampando la 'Comunicazione' decorrenza/scadenza
						flagTemplate = "0";
					else if (lPosGiu.isLibero())
						// Teoricamente impossibile. Il provvedimento deve essere il 5421 e non il 2008
						flagTemplate = null;
					else
						flagTemplate = null; // forzo il template Vuoto
				} else if (lflagScarcerato.equals("SORV")) {
					if (lPosGiu.isLibero())
						flagTemplate = "5";
					else
						flagTemplate = "3";
				}
			} else if (lEventoModel.getCodMotivo().equals("5421")) {
				// Libero Esegue Procura
				flagTemplate = "4";
			} else if (lEventoModel.getCodMotivo().equals("5420")) {
				// New: Affidamento Terapeutico oggetto 2006 in caso di Libero esegue Procura
				// il motivo evento non è più il 2006 ma il 5420
				flagTemplate = "4";
			}
			// MEV_9-SIEP si aggiungono gli ulteriori codici per le richieste verbale
			else if (lEventoModel.getCodMotivo().equals("5422") || lEventoModel.getCodMotivo().equals("5423")
					|| lEventoModel.getCodMotivo().equals("5424")
					|| lEventoModel.getCodMotivo().equals("5425")
					|| lEventoModel.getCodMotivo().equals("5426")) {
				flagTemplate = "4";
			}
			else if (lEventoModel.getCodMotivo().equals("1400") || lEventoModel.getCodMotivo().equals("1401")
					|| lEventoModel.getCodMotivo().equals("1410")
					|| lEventoModel.getCodMotivo().equals("1411")
					|| lEventoModel.getCodMotivo().equals("1412")) {
				flagTemplate = "5";
			}
		  // MEV_9-SIEP - FINE
			else if (lEventoModel.getCodMotivo().equals("2006")) { // Vecchia gestione per il codice 2006
																		// affidamento Terapeutico
				if (lflagScarcerato.equals("SORV")) {
					if (lPosizioneGiu.equals("07") || lPosizioneGiu.equals("10")) {
						flagTemplate = "5"; // da libero
					} else {
						flagTemplate = "3"; // da detenuto
					}
				} else if (lflagScarcerato.equals("PROC")) {
					if (lPosizioneGiu.equals("03") || lPosizioneGiu.equals("14"))
						flagTemplate = "1";
					else if (lPosizioneGiu.equals("04") || lPosizioneGiu.equals("82")
							|| lPosizioneGiu.equals("83") || lPosizioneGiu.equals("84"))
						flagTemplate = "2";
					// 24/03/2011 per SIEP_MA_AMMPRO_AFFI_DETD
					else if (lPosizioneGiu.equals("12") || lPosizioneGiu.equals("29")
							|| lPosizioneGiu.equals("25") || lPosizioneGiu.equals("41")
							|| lPosizioneGiu.equals("44"))
						flagTemplate = "6";

					// AMBROSINO AFFIDAMENTO in Prova - AMMISSIONE PROVVISORIA - esegue PROC - Libero
					// Codice PosGiu cambiato in Gennaio 2011 da 51 a 54
					else if (lPosGiu.isLibero())
						flagTemplate = "4";
					else if (lPosizioneGiu.equals("54") || lPosizioneGiu.equals("13"))
						flagTemplate = "0";
					else if (lPosizioneGiu.equals("01") || lPosizioneGiu.equals("73"))
						flagTemplate = "7";
					else if (lPosizioneGiu.equals("02") || lPosizioneGiu.equals("70")
							|| lPosizioneGiu.equals("71") || lPosizioneGiu.equals("72"))
						flagTemplate = "8";
				}
			}

		} else if (lTipoMisura.equals("DETENZIONE")) {
		  // MEV_9-SIEP si aggiungono gli ulteriori codici
			if (lEventoModel.getCodMotivo().equals("1402") || lEventoModel.getCodMotivo().equals("1413")) {
				if (lflagScarcerato.equals("PROC"))
					flagTemplate = "3";
				else if (lflagScarcerato.equals("SORV"))
					flagTemplate = "5";
			}			
		  // MEV_9-SIEP - FINE
			else if (lflagScarcerato.equals("SORV") && (lPosizioneGiu.equals("03"))) {
				flagTemplate = "1";
			} else if (lflagScarcerato.equals("PROC") && (lPosizioneGiu.equals("03"))) {
				flagTemplate = "0";
			} else if (lflagScarcerato.equals("PROC")
					&& ((lPosizioneGiu.equals("02")) || lPosizioneGiu.equals("70")
							|| lPosizioneGiu.equals("71") || lPosizioneGiu.equals("72"))) {
				flagTemplate = "6";
			} else if (lflagScarcerato.equals("SORV")
					&& (lPosizioneGiu.equals("02") || lPosizioneGiu.equals("70") || lPosizioneGiu.equals("71")
							|| lPosizioneGiu.equals("72"))) {
				flagTemplate = "7";
			} else if (lflagScarcerato.equals("PROC") && lPosizioneGiu.equals("07")
					&& !lCodTipoPosGiuridicaAltraCausa.equals("")
					&& (lCodTipoPosGiuridicaAltraCausa.equals("78")
							|| lCodTipoPosGiuridicaAltraCausa.equals("79")
							|| lCodTipoPosGiuridicaAltraCausa.equals("80")
							|| lCodTipoPosGiuridicaAltraCausa.equals("81"))) {
				flagTemplate = "6";
			} else if (lflagScarcerato.equals("SORV") && lPosizioneGiu.equals("07")
					&& !lCodTipoPosGiuridicaAltraCausa.equals("")
					&& (lCodTipoPosGiuridicaAltraCausa.equals("78")
							|| lCodTipoPosGiuridicaAltraCausa.equals("79")
							|| lCodTipoPosGiuridicaAltraCausa.equals("80")
							|| lCodTipoPosGiuridicaAltraCausa.equals("81"))) {
				flagTemplate = "7";
			} else if (lflagScarcerato.equals("PROC")
					&& (lPosizioneGiu.equals("04") || lPosizioneGiu.equals("82") || lPosizioneGiu.equals("83")
							|| lPosizioneGiu.equals("84"))) {
				flagTemplate = "8";
			} else if (lflagScarcerato.equals("SORV")
					&& (lPosizioneGiu.equals("04") || lPosizioneGiu.equals("82") || lPosizioneGiu.equals("83")
							|| lPosizioneGiu.equals("84"))) {
				flagTemplate = "9";
			} else if (lPosizioneGiu.equals("29")) {
				flagTemplate = "4";
			} else if (lflagScarcerato.equals("PROC") && lPosGiu.isLibero()
					&& lCodTipoPosGiuridicaAltraCausa.equals("")) {
				flagTemplate = "3";
			} else if (lflagScarcerato.equals("SORV") && lPosGiu.isLibero()
					&& lCodTipoPosGiuridicaAltraCausa.equals("")) {
				flagTemplate = "5";
			}
		}

		// Tutti i template sono registrati come COD_TIPO_PROVVEDIMENTO = '03' indipendentemente
		// dall'effettivo tipo provvedimento dell'evento SIEP.
		// Questo ad eccezione
		// if (!"26".equals(lTipoProvv))
		lTipoProvv = "03";

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("flagTemplate = " + flagTemplate);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("lMotivo = " + lMotivo);

		if (flagTemplate != null && lMotivo != null && !lMotivo.equals("0000")) {
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate("01", lTipoProvv,
					lMotivo, flagTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.debug("lTemMod = " + lTemMod);
		} else {
			lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod);

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}