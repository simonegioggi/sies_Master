package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActStampaFungibilita
 * </p>
 * <p>
 * Description:
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
@SuppressWarnings("rawtypes")
public class ActStampaFungibilita extends ActionSiap {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

//		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel
						.getIdFascicoloSiep());

		PosizioneGiuridicaModel lPosizioneModel = lPosAltra.getPosizioneGiuridica();

//		String Posizione = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();
		EventoModel lEve = null;
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		/************************************* ricerca evento 06 ************************************************************/
		EventoModel lEveModelEve = new EventoModel();
		lEveModelEve.setCodTipoEvento("01");
		lEveModelEve.setCodTipoProvvedimento("06");
		lEveModelEve.setFlagDocumentoRegistrato("S");
		try {
			lEveModelEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			Vector eventi = lCtrl.ExRicercaEvento(lEveModelEve);

			lEve = (EventoModel) eventi.get(0);
		} catch (Exception ex) {

		}
		/**************************************************************************************/

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));
		/***************************** Anotazione manuale *********************************************************/
		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

		/*Vector lAnnMod = */lCtrlAnn.ExRicercaAnnotazioneManualeByIdEvento(lEventoModel.getIdEvento());

		/**************************************************************************************/
		/********* Pena Residua ***************/
		IPenaResidua lCtrlPosPena = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPosModPen = lCtrlPosPena
				.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		/*************************************************************************/

		String lMotivo = lEventoModel.getCodMotivo();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEveMod.getEvento().setIdEvento(new BigDecimal(lId));
		lEveMod.getEvento().setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());

		lEveMod.getEvento().setDescrLuogoEmittente(lUff.getDescrComune());
		lEveMod.getEvento().setDescrUfficioEmittente(lUff.getDescrTipoUfficio());

		lEveMod.getEvento().setDataAggiornamento(DateUtils.getSysDate());
		lEveMod.getEvento().setCodUfficioAggiornamento(lUff.getCodUfficio());
		lEveMod.getEvento().setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lEveMod.getEvento().setFlagDocumentoRegistrato("N");

		String annoGE = this.getRequestStringParameter("annoGE");
		String flagTemplate = null;
		/*
		 * // libero if (lFascicoloModel.getFlagAltraCausa().equals("S") && annoGE.equals("N")) { flagTemplate
		 * = "A"; } else if (lFascicoloModel.getFlagAltraCausa().equals("S") && annoGE.equals("S")) {
		 * flagTemplate = "B"; } else if (!Posizione.equals("07") && !Posizione.equals("10") &&
		 * annoGE.equals("S")) // libero { flagTemplate = "9"; } else if (!Posizione.equals("07") &&
		 * !Posizione.equals("10") && annoGE.equals("N")) { flagTemplate = "8"; } else if
		 * (Posizione.equals("07") || Posizione.equals("10")) { if (lEve != null) { if (
		 * (lPosModPen.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0 &&
		 * lPosModPen.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0 &&
		 * lPosModPen.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0 &&
		 * lPosModPen.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0 &&
		 * lPosModPen.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0 &&
		 * lPosModPen.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) { if (annoGE.equals("N")) {
		 * flagTemplate = "G"; } else { flagTemplate = "H"; } } else { if (annoGE.equals("N")) { flagTemplate
		 * = "E"; } else { flagTemplate = "F"; } } } else { if (annoGE.equals("N")) { flagTemplate = "C"; }
		 * else { flagTemplate = "D"; }
		 * 
		 * } }
		 */

		/******************* GESTIONE TEMPLATE ************************/
		if (lFascicoloModel.getFlagAltraCausa() != null && lFascicoloModel.getFlagAltraCausa().equals("S")) // IN
																											// ALTRA
																											// CAUSA
		{
			if (annoGE.equals("N")) {
				flagTemplate = "A";
			} else {
				flagTemplate = "B";
			}
		} else if (!lPosizioneModel.isLibero()) // NO LIBERO
		{
			if (annoGE.equals("N")) {
				flagTemplate = "8";
			} else {
				flagTemplate = "9";
			}
		} else // LIBERO
		{
			if (lEve != null) {
				if ((lPosModPen.getNumAnniArresto().compareTo(new BigDecimal(0)) == 0
						&& lPosModPen.getNumMesiArresto().compareTo(new BigDecimal(0)) == 0
						&& lPosModPen.getNumGiorniArresto().compareTo(new BigDecimal(0)) == 0
						&& lPosModPen.getNumAnniReclusione().compareTo(new BigDecimal(0)) == 0
						&& lPosModPen.getNumMesiReclusione().compareTo(new BigDecimal(0)) == 0 && lPosModPen
						.getNumGiorniReclusione().compareTo(new BigDecimal(0)) == 0)) {
					if (annoGE.equals("N")) {
						flagTemplate = "G";
					} else {
						flagTemplate = "H";
					}
				} else {
					if (annoGE.equals("N")) {
						flagTemplate = "E";
					} else {
						flagTemplate = "F";
					}
				}
			} else {
				if (annoGE.equals("N")) {
					flagTemplate = "C";
				} else {
					flagTemplate = "D";
				}
			}
		}
		/**************************************************************/

		TemplateModel lModTem = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lModTem = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoModel.getCodTipoEvento(), lEventoModel.getCodTipoProvvedimento(), lMotivo,
				flagTemplate);

		lEveMod.setNomeTemplate(lModTem.getIdTemplate());

		ByteArrayOutputStream lReport = lCtrlAnn.ExStampaDocumentoXAnnotazioni(lEveMod, lUtenteMod); // setta
																										// la
																										// risposta
																										// nella
																										// request

		// Prepara la pagina di destinazione
		// if (lReport != null)
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}

}