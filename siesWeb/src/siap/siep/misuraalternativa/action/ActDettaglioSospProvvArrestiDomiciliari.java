package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Hashtable;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.MinorMask;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioMASospProvv
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Sospensione Provvisoria
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

public class ActDettaglioSospProvvArrestiDomiciliari extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// COD_MOTIVO da Oggetto Decreto
		String codMotivo = null;
		
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO)) {
			codMotivo = this.getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO);
		}

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// //la posizione giuridica cambia in funzione dell'Oggetto Decreto COD_MOTIVO
		// //Oggetto Decreto COD_MOTIVO=2756 ==> POSIZIONE_GIURIDICA=62
		// //Oggetto Decreto COD_MOTIVO=2741 ==> POSIZIONE_GIURIDICA=63
		// //Oggetto Decreto COD_MOTIVO=2742 ==> POSIZIONE_GIURIDICA=64
		// //Oggetto Decreto COD_MOTIVO=2743 ==> POSIZIONE_GIURIDICA=65
		// PosizioneGiuridicaModel posGiuridicaModel = new PosizioneGiuridicaModel();
		// if (codMotivo!=null){
		// if(codMotivo.equalsIgnoreCase("2756")){
		// posGiuridicaModel.setCodPosizioneGiuridica("62");
		// } else if(codMotivo.equalsIgnoreCase("2741")){
		// posGiuridicaModel.setCodPosizioneGiuridica("63");
		// } else if(codMotivo.equalsIgnoreCase("2742")){
		// posGiuridicaModel.setCodPosizioneGiuridica("64");
		// } else if(codMotivo.equalsIgnoreCase("2743")){
		// posGiuridicaModel.setCodPosizioneGiuridica("65");
		// }
		// }
		// posGiuridicaModel.setDataInizio(DateUtils.getSysDate());
		// posGiuridicaModel.setCodOperatoreInserimento (lFascMod.getCodOperatoreAggiornamento());
		// posGiuridicaModel.setDataInserimento (lFascMod.getDataAggiornamento());
		// posGiuridicaModel.setCodUfficioInserimento (lFascMod.getCodUfficioAggiornamento());
		// posGiuridicaModel.setCodPosizioneProcessuale("-");
		// posGiuridicaModel.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		// posGiuridicaModel.setIdEventoRiferimento(lIdEvento);
		// //posizione giuridica inserita alla convalida
		// lPos.setPosizioneGiuridica(posGiuridicaModel);
		// setRequestAttribute("posizioneluogoaltra", lPos);

		// ricerca evento notifica
		IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveMod = new EventoNotificaModel();
		lEveMod = lCtrlEvento.ExRicercaEventoNotificaByKey(lIdEvento);
		this.setRequestAttribute("eventonotifica", lEveMod);
		Hashtable lTable = this.ricercaNotifiche(lEveMod.getNotifiche());

		// ricerca misura per il fascicolo
		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lDecreto = new MisuraAlternativaModel();
		lDecreto = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lEveMod.getEvento().getEveIdEvento());
		setRequestAttribute("misuraalternativa", lDecreto);

		if (lDecreto != null && lDecreto.getCodTipoMisura() != null) {
			if (lDecreto.getCodTipoMisura().equals("2756")) {
				setRequestAttribute("tipoMisura", "PROVVISORIAARRESTIDOMICILIARI");
			} else if (lDecreto.getCodTipoMisura().equals("2741")) {
				setRequestAttribute("tipoMisura", "PROVVISORIADOMICILIARI");
			} else if (lDecreto.getCodTipoMisura().equals("2742")) {
				setRequestAttribute("tipoMisura", "PROVVISORIAPERMANENZA");
			} else if (lDecreto.getCodTipoMisura().equals("2743")) {
				setRequestAttribute("tipoMisura", "PROVVISORIACOLLOCAMENTO");
			// Inizio MAC 2016/10/21
			// Gestione codice MOTIVO_PROVVEDIMENTO = 2291
			// Il codice 2291 sostituisce i codici (2741,2742,2743,2756) 
			// eliminati dalla base dati
			// MAC 2017/04/01 Ripristinati codici (2741,2742,2743,2756)	
			
			//m_dg.DOG07.01-10-2018.0030206.U (deve essere censito anche i codice 2291)
			}else if (lDecreto.getCodTipoMisura().equals("2291")) {
				setRequestAttribute("tipoMisura", "PROVVISORIAARRESTIDOM");
			}			
		}

		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = new UfficioModel();
		lUffMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("UfficioEmittente", lUffMod);

		// istituto
		IstitutoDetenzioneModel lIstMod = null;
		if (lTable.get("lNotIstituto") != null) {
			lIstMod = ((NotificaModel) lTable.get("lNotIstituto")).getIstitutoDetenzione();
			setRequestAttribute("lIstMod", lIstMod);
		}

		// Autorità esterna E
		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
		}

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		// Ufficio TDS
		String UffTDS = null;
		String NoteTDS = null;
		if (lTable.get("UffTDS") != null) {
			// UffTDS = ((NotificaModel)lTable.get("UffTDS")).getUfficio().getDescrComune();
			// NoteTDS = ((NotificaModel)lTable.get("UffTDS")).getNote();
			NotificaModel nm = (NotificaModel) lTable.get("UffTDS");
			UffTDS = MinorMask.dettaglioMagistrato(nm.getUfficio(), lCodTipoUfficio);
			NoteTDS = nm.getNote();
			setRequestAttribute("UffTDS", UffTDS);
			setRequestAttribute("NoteTDS", NoteTDS);
		}

		// Ufficio UDS
		String UffUDS = null;
		String NoteUDS = null;

		if (lTable.get("UffUDS") != null) {
			// UffUDS = ( (NotificaModel) lTable.get("UffUDS")).getUfficio().getDescrComune();
			// NoteUDS = ( (NotificaModel) lTable.get("UffUDS")).getNote();
			NotificaModel nm = (NotificaModel) lTable.get("UffUDS");
			UffUDS = MinorMask.dettaglioTribunale(nm.getUfficio());
			// MEV10-s3: effettuo modifica per etichetta nella pagina
	    	if (("PM".equals(lCodTipoUfficio) || "PMM".equals(lCodTipoUfficio) || "PGCAP".equals(lCodTipoUfficio)) &&
	    			"UDSM".equals(lUffMod.getCodTipoUfficio()))
	    		UffUDS = UffUDS.replace("Ufficio di Sorveglianza presso il Tribunale per minorenni", "Magistrato di Sorveglianza per i Minorenni");
			NoteUDS = nm.getNote();
			setRequestAttribute("UffUDS", UffUDS);
			setRequestAttribute("NoteUDS", NoteUDS);
		}

		// Ricerca Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveMod.getEvento().getCodMagistrato());
		;
		setRequestAttribute("magistrato", lMagi);

		// Sede Ufficio di Sorveglianza
		UfficioModel lUffEmiMod = new UfficioModel();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());

		setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute(ICostantiEvento.CAMPO_COD_MOTIVO, codMotivo);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		if (lIstMod != null)
			setRequestAttribute("notificaE", "istituto");
		if (lAutE != null)
			setRequestAttribute("notificaE", "autorita");

		return PG_DETTAGLIO_SOSP_PROVV_ARRESTI_DOMICILIARI;
	}
}