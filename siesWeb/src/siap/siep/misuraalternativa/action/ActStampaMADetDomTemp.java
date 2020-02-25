package siap.siep.misuraalternativa.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

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
 * <p>
 * Title: ActStampaMADetDomTemp
 * </p>
 * <p>
 * Description: Produceil documento
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public class ActStampaMADetDomTemp extends ActionSiap implements ICostantiMisuraAlternativa {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

		// BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		lPosAltra = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascicoloModel.getIdFascicoloSiep());
		String lPosizioneGiu = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();

		// PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		PosizioneGiuridicaModel lPosPrec = new PosizioneGiuridicaModel();
		lPosPrec = lPosCtrl
				.ExRicercaPosizioneGiuridicaPrecedenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

		// ricerca mA
		IMisuraAlternativa lCtrlMis = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAlModConcessa = lCtrlMis
				.ExRicercaMisuraAlternativaByIdEvento(lEventoModel.getEveIdEvento());

		Date lDataInizioMisura = lMisAlModConcessa.getDataInizioMisura();

		String lMotivo = lEventoModel.getCodMotivo();
		String lTipoEvento = "01";
		String lTipoProv = "03";
		String lFlagTemplate = null;

		// ricerca del template tramite codice motivo e flag template = 1
		// Arresti Domiciliari
		TemplateModel lTemMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");
    
    // mev_62
    if(lFlagTemplate == null){
	    if(lMisAlModConcessa != null && lMisAlModConcessa.getCodTipoUfficioScarcerazione() != null && lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("PROC")) // da scarcerare
	    {
	      lFlagTemplate = "1";
	    }
	    else if(lMisAlModConcessa != null && lMisAlModConcessa.getCodTipoUfficioScarcerazione() != null && lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("SORV")) // già scarcerato
	    {
	      lFlagTemplate = "2";
	    }
    }
		// libero
		if (lMotivo != null && lMotivo.equals("0011")) {
			if (lPosPrec != null && lPosPrec.getCodPosizioneGiuridica() != null && lPosPrec.isLibero()
					&& lDataInizioMisura != null && lPosizioneGiu.equals("12")) {
				lFlagTemplate = "1";
			} else {
				switch (Integer.parseInt(lPosizioneGiu)) {
				case 7: // LIBERO
				case 10: // LIBERO
				case 16: // LIBERO IN DIFFERIMENTO PENA
				case 17: // LIBERO IN DIFFERIMENTO PENA (PROVVISORIA)
				case 20: // EVASO
				case 26: // ESPULSO
				case 30: // ESTRADATO
				case 46: // LIBERO in Sospensione
				case 47: // LIBERO in Sospensione
				{
					lFlagTemplate = "0";
					break;
				}
				case 3: // Espiazione Pena in Regime Carcerario
				case 14: // Espiazione Pena in Semiliberta
				{
					if (lMisAlModConcessa != null
							&& lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("PROC")) // da
																									// scarcerare
					{
						lFlagTemplate = "2";
					} else if (lMisAlModConcessa != null
							&& lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("SORV")) // già
																									// scarcerato
					{
						lFlagTemplate = "3";
					}
					break;
				}
				case 13: {
					lFlagTemplate = "6";
					break;
				}
				case 12: {
					lFlagTemplate = "7";
					break;
				}
				case 29: {
					lFlagTemplate = "5";
					break;
				}
				}
			}
		} else if (lPosizioneGiu != null && lPosizioneGiu.equals("12") && lMotivo != null
				&& lMotivo.equals("0197")) {
			lFlagTemplate = "4";
		} else if (lMotivo != null && lMotivo.equals("2340")) // proroga provvisoria
		{
			if ("03".equals(lPosizioneGiu)) {
				if (lMisAlModConcessa != null
						&& lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("PROC")) // da scarcerare
				{
					lFlagTemplate = "1";
				} else if (lMisAlModConcessa != null
						&& lMisAlModConcessa.getCodTipoUfficioScarcerazione().equals("SORV")) // già
																								// scarcerato
				{
					lFlagTemplate = "2";
				}
			} else if ("12".equals(lPosizioneGiu)) {
				lFlagTemplate = "0";
			}

		}

		if (lFlagTemplate != null && lMotivo != null && !lMotivo.equals("0000")) {
			lTemMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(lTipoEvento, lTipoProv,
					lMotivo, lFlagTemplate);
			lEveMod.setNomeTemplate(lTemMod.getIdTemplate());
		} else {
			lEveMod.setNomeTemplate(TEMPLATE_VUOTO);
		}

		ByteArrayOutputStream lReport = lCtrl.ExStampaDocumento(lEveMod, lUtenteMod); // setta la risposta
																						// nella request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}
}