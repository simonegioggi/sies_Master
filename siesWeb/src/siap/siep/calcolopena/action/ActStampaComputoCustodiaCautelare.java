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
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title:
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
public class ActStampaComputoCustodiaCautelare extends ActionSiap {

	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();
		UfficioModel lUff = this.getUfficioUtenteConnesso();

//		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascicoloModel
						.getIdFascicoloSiep());
//		String Posizione = lPosAltra.getPosizioneGiuridica().getCodPosizioneGiuridica();
		EventoModel lEve = null;
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();

		/****************************** Ricerca evento 06 *****************************/
		EventoModel lEveModelEve = new EventoModel();
		lEveModelEve.setCodTipoEvento("01");
		lEveModelEve.setCodTipoProvvedimento("06");
		lEveModelEve.setFlagDocumentoRegistrato("S");
		try {
			lEveModelEve.setFasSieIdFascicoloSiep(lFascicoloModel.getIdFascicoloSiep());
			Vector eventi = lCtrl.ExRicercaEvento(lEveModelEve);

			lEve = (EventoModel) eventi.get(0);
			this.setRequestAttribute("evento06", lEve);
		} catch (Exception ex) {
		}
		/******************************************************************************/

		String lId = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		EventoModel lEventoModel = lCtrl.ExRicercaEventoByKey(new BigDecimal(lId));

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

		// ** Gestione POSIZIONE GIURIDICA di tipo MIS_ALT in modo centralizzato
		PosizioneGiuridicaModel lPosizione = lPosAltra.getPosizioneGiuridica();
		// if (Posizione.equals("11") || Posizione.equals("12") || Posizione.equals("13") ||
		// Posizione.equals("14") || Posizione.equals("19") && annoGE.equals("N"))

		/*******************************************/
		/*
		 * if ( lPosizione.isMisAlt() && annoGE.equals("N") ) { flagTemplate = "1"; } //else if
		 * (Posizione.equals("11") || Posizione.equals("12") || Posizione.equals("13") ||
		 * Posizione.equals("14") || Posizione.equals("19") && annoGE.equals("S")) else if
		 * (lPosizione.isMisAlt() && annoGE.equals("S")) { flagTemplate = "4"; } else if
		 * (Posizione.equals("07") || Posizione.equals("10") && lEve != null && annoGE.equals("S")) // libero
		 * { flagTemplate = "2"; } else if (Posizione.equals("07") || Posizione.equals("10") && lEve != null
		 * && annoGE.equals("N")) { flagTemplate = "3"; } else if (Posizione.equals("07") ||
		 * Posizione.equals("10") && lEve == null && annoGE.equals("S")) { flagTemplate = "6"; } else if
		 * (Posizione.equals("07") || Posizione.equals("10") && lEve == null && annoGE.equals("N")) {
		 * flagTemplate = "7"; } else //!LIBERO { if (annoGE.equals("N")) { flagTemplate = "0"; } else {
		 * flagTemplate = "5"; } }
		 */

		/******************* GESTIONE TEMPLATE ************************/
		flagTemplate = "0"; // N.B. inizializzato nel caso le if successive falliscano

		if (lPosizione.isMisAlt()) // IN MISURA ALTERNATIVA
		{
			if (annoGE.equals("N")) {
				flagTemplate = "1";
			} else {
				flagTemplate = "4";
			}
		} else if (!lPosizione.isLibero() // NON LIBERO OPPURE ALTRA CAUSA
				|| (lFascicoloModel.getFlagAltraCausa() != null && lFascicoloModel.getFlagAltraCausa()
						.equals("S"))) {
			if (annoGE.equals("N")) {
				flagTemplate = "0";
			} else {
				flagTemplate = "5";
			}
		} else if (lPosizione.isLibero()) // LIBERO
		{
			if (lEve != null) {
				if (annoGE.equals("N")) {
					flagTemplate = "3";
				} else {
					flagTemplate = "2";
				}
			} else {
				if (annoGE.equals("N")) {
					flagTemplate = "7";
				} else {
					flagTemplate = "6";
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

		IAnnotazioneManuale lCtrlAnn = SIEPLookupRemote.getAnnotazioneManualeRemote();

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