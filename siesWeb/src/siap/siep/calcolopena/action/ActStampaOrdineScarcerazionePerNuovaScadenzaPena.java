package siap.siep.calcolopena.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.template.controller.ITemplate;
import siap.sico.template.model.TemplateModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.annotazionemanuale.controller.IAnnotazioneManuale;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;

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

public class ActStampaOrdineScarcerazionePerNuovaScadenzaPena extends ActionSiap {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		BigDecimal lIdEvento = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		UtenteModel lUtenteMod = this.getUtenteConnesso();

		EventoModel lEventoMod = new EventoModel();

		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		lEventoMod = lCtrl.ExRicercaEventoByKey(lIdEvento);

		if (lEventoMod == null)
			throw new F3BException(F3BException.USER_MESSAGE, "Evento non Registrato");

		// POSIZIONE GIURIDICA
		PosizioneGiuridicaModel lPos = new PosizioneGiuridicaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		// CERCA LE ANNOTAZIONI MANUALI LEGATE AL PROVVEDIMENTO
		IAnnotazioneManuale IAnnCtrl = SIEPLookupRemote.getAnnotazioneManualeRemote();
		Vector lListAnnMod = IAnnCtrl.ExRicercaAnnotazioneManualeByIdEvento(lEventoMod.getEveIdEvento());

		// Stabilisce se esiste almeno un'annotazione di tipo D (Difforme)
		boolean lFlagDifforme = false;
		for (int i = 0; i < lListAnnMod.size(); i++) {
			AnnotazioneManualeModel lAnnMan = (AnnotazioneManualeModel) lListAnnMod.get(i);
			if (lAnnMan != null && lAnnMan.getFlagAppProvvisoria() != null) {
				if (lAnnMan.getFlagConforme().equals("D")) {
					lFlagDifforme = true;
					break;
				}
			}
		}

		// ==========================================================================
		// Imposta il flag_template in funzione della posizione giuridica (isMisAlt)
		// e del tipo di decisione (conforme/difforme)
		// - 0 Non difforme, non in mis alt
		// - 1 Non difforme, in mis alt
		// - 2 Difforme, non in mis alt
		// - 3 Difforme, in mis alt
		// ==========================================================================
		boolean lFlagMisAlt = false;
		if (lPos.isMisAlt()) {
			lFlagMisAlt = true;
		}

		String lFlagTemplate = "0";
		if (!lFlagDifforme) {
			if (lFlagMisAlt) {
				lFlagTemplate = "1";
			}
		} else {
			if (!lFlagMisAlt) {
				lFlagTemplate = "2";
			} else {
				lFlagTemplate = "3";
			}
		}

		// RICERCA TEMPLATE
		TemplateModel lTempMod = new TemplateModel();
		ITemplate lCtrlTem = SICOLookupRemote.getTemplateRemote();
		lTempMod = lCtrlTem.ExRicercaTemplateByTipEveTipoProvCodMotivoFlagTemplate(
				lEventoMod.getCodTipoEvento(), lEventoMod.getCodTipoProvvedimento(),
				lEventoMod.getCodMotivo(), lFlagTemplate);
		String lIdTemplate = "";
		lIdTemplate = lTempMod.getIdTemplate();

		EventoNotificaModel lEveMod = new EventoNotificaModel();

		lEventoMod.setDescrLuogoEmittente(lUtenteMod.getUfficioUtente().getDescrComune());
		lEventoMod.setDescrUfficioEmittente(lUtenteMod.getUfficioUtente().getDescrTipoUfficio());

		lEventoMod.setDataAggiornamento(DateUtils.getSysDate());
		lEventoMod.setCodUfficioAggiornamento(lUtenteMod.getUfficioUtente().getCodUfficio());
		lEventoMod.setCodOperatoreAggiornamento(lUtenteMod.getUserId());
		lEventoMod.setFlagDocumentoRegistrato("N");

		lEveMod.setEvento(lEventoMod);
		lEveMod.setNomeTemplate(lIdTemplate);

		ByteArrayOutputStream lReport = IAnnCtrl.ExStampaDocumentoXAnnotazioni(lEveMod, lUtenteMod); // setta
																										// la
																										// risposta
																										// nella
																										// request

		// Prepara la pagina di destinazione
		setRequestAttribute("report", lReport);

		return IWebConstants.PG_DOWNLOAD;
	}
}
