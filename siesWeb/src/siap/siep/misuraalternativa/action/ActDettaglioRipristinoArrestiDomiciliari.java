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
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.MinorMask;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActDettaglioMARipristino
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Ripristino
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

public class ActDettaglioRipristinoArrestiDomiciliari extends ActMisuraAlternativa implements
		ICostantiMisuraAlternativa {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		// id dell'evento inserito
		BigDecimal lIdEvento = this.getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// COD_MOTIVO da Oggetto Decisione
		// String codMotivo = this.getRequestStringParameter( ICostantiEvento.CAMPO_COD_MOTIVO );
		String codMotivo = "";
		if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE) != null)
			codMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_TRIBUNALE);
		else if (!this.isRequestParameterNullObj(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO)
				&& getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO) != null)
			codMotivo = getRequestStringParameter(ICostantiEvento.CAMPO_COD_MOTIVO_MAGISTRATO);

		// //la posizione giuridica cambia in funzione dell'Oggetto Decreto COD_MOTIVO
		// //Oggetto Decreto COD_MOTIVO=2748 ==> POSIZIONE_GIURIDICA=62
		// //Oggetto Decreto COD_MOTIVO=2749 ==> POSIZIONE_GIURIDICA=67
		// //Oggetto Decreto COD_MOTIVO=2758 ==> POSIZIONE_GIURIDICA=64
		// //Oggetto Decreto COD_MOTIVO=2759 ==> POSIZIONE_GIURIDICA=69
		// //Oggetto Decreto COD_MOTIVO=2752 ==> POSIZIONE_GIURIDICA=4
		// //Oggetto Decreto COD_MOTIVO=2753 ==> POSIZIONE_GIURIDICA=63
		// //Oggetto Decreto COD_MOTIVO=2754 ==> POSIZIONE_GIURIDICA=68
		// //Oggetto Decreto COD_MOTIVO=2755 ==> POSIZIONE_GIURIDICA=65
		// PosizioneGiuridicaModel posGiuridicaModel = new PosizioneGiuridicaModel();
		// if (codMotivo!=null){
		// if(codMotivo.equalsIgnoreCase("2748")){
		// posGiuridicaModel.setCodPosizioneGiuridica("62");
		// } else if(codMotivo.equalsIgnoreCase("2749")){
		// posGiuridicaModel.setCodPosizioneGiuridica("67");
		// } else if(codMotivo.equalsIgnoreCase("2758")){
		// posGiuridicaModel.setCodPosizioneGiuridica("64");
		// } else if(codMotivo.equalsIgnoreCase("2759")){
		// posGiuridicaModel.setCodPosizioneGiuridica("69");
		// } else if(codMotivo.equalsIgnoreCase("2752")){
		// posGiuridicaModel.setCodPosizioneGiuridica("4");
		// } else if(codMotivo.equalsIgnoreCase("2753")){
		// posGiuridicaModel.setCodPosizioneGiuridica("63");
		// } else if(codMotivo.equalsIgnoreCase("2754")){
		// posGiuridicaModel.setCodPosizioneGiuridica("68");
		// } else if(codMotivo.equalsIgnoreCase("2755")){
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

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

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

		PenaResiduaModel lPenaResMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", lPenaResMod);

		if (lDecreto != null && lDecreto.getCodTipoMisura() != null) {
			if (lDecreto.getCodTipoMisura().equals("2748")) {
				setRequestAttribute("tipoMisura", "REVPRO2748");
			} else if (lDecreto.getCodTipoMisura().equals("2749")) {
				setRequestAttribute("tipoMisura", "REVPRO2749");
			} else if (lDecreto.getCodTipoMisura().equals("2758")) {
				setRequestAttribute("tipoMisura", "REVPRO2758");
			} else if (lDecreto.getCodTipoMisura().equals("2759")) {
				setRequestAttribute("tipoMisura", "REVPRO2759");
			} else if (lDecreto.getCodTipoMisura().equals("2752")) {
				setRequestAttribute("tipoMisura", "EFFSOS2752");
			} else if (lDecreto.getCodTipoMisura().equals("2753")) {
				setRequestAttribute("tipoMisura", "EFFSOS2753");
			} else if (lDecreto.getCodTipoMisura().equals("2754")) {
				setRequestAttribute("tipoMisura", "EFFSOS2754");
			} else if (lDecreto.getCodTipoMisura().equals("2755")) {
				setRequestAttribute("tipoMisura", "EFFSOS2755");
			}
		}

		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		UfficioModel lUffMod = new UfficioModel();
		lUffMod = lCtrlUffEmi.getUfficioByKey(lDecreto.getChiaveUfficioFascicoloSius());
		setRequestAttribute("UfficioEmittente", lUffMod);

		// istituto
		// IstitutoDetenzioneModel lIstMod= null;
		// if(lTable.get("lNotIstituto")!= null)
		// {
		// lIstMod = ((NotificaModel)lTable.get("lNotIstituto")).getIstitutoDetenzione();
		// setRequestAttribute("lIstMod", lIstMod);
		// }

		// Autorità esterna E
		AutoritaEsternaModel lAutE = null;
		String NoteAutE = null;
		if (lTable.get("AutE") != null) {
			lAutE = ((NotificaModel) lTable.get("AutE")).getAutoritaEsterna();
			NoteAutE = ((NotificaModel) lTable.get("AutE")).getNote();
			setRequestAttribute("NoteAutE", NoteAutE);
			setRequestAttribute("autoritaEsternaE", lAutE);
		}

		// Cssa
		String lCssa = null;
		String NoteCssa = null;
		if (lTable.get("NotCssa") != null) {
			lCssa = ((NotificaModel) lTable.get("NotCssa")).getCSSA().getComune() + " "
					+ ((NotificaModel) lTable.get("NotCssa")).getCSSA().getIndirizzo();
			NoteCssa = ((NotificaModel) lTable.get("NotCssa")).getNote();
			setRequestAttribute("NoteCssa", NoteCssa);
			setRequestAttribute("Cssa", lCssa);
			setRequestAttribute("daticssa", ((NotificaModel) lTable.get("NotCssa")).getCSSA());
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

		// Autorità esterna C
		AutoritaEsternaModel lAutC = null;
		String NoteAutC = null;

		if (lTable.get("AutC") != null) {
			lAutC = ((NotificaModel) lTable.get("AutC")).getAutoritaEsterna();
			NoteAutC = ((NotificaModel) lTable.get("AutC")).getNote();
			setRequestAttribute("NoteAutC", NoteAutC);
			setRequestAttribute("autoritaEsternaC", lAutC);

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

		// PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento,lFascMod.getIdFascicoloSiep());
		// setRequestAttribute("penaresidua", llPenMod);

		setRequestAttribute(ICostantiEvento.CAMPO_COD_MOTIVO, codMotivo);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoIstituto());
		setRequestAttribute("tipoIstituto", "" + lOption);

		// if(lIstMod!= null)
		// setRequestAttribute("notificaE","istituto");

		if (lDecreto.getCodTipoUfficioScarcerazione().equalsIgnoreCase("PROC")) {
			// "Istituto di detenzione" esiste solo per "Eseguita Procura"
			setRequestAttribute("notificaE", "istituto");
		}
		if (lAutE != null)
			setRequestAttribute("notificaE", "autorita");

		return PG_DETTAGLIO_RIPRISTINO_ARRESTI_DOMICILIARI;
	}
}