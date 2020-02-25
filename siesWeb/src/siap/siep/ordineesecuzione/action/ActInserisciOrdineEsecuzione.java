package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.bdmc.BDMCLookupRemote;
import siap.bdmc.fascicolosiepbdmc.controller.IFascicoloSiepBdmc;
import siap.bdmc.fascicolosiepbdmc.model.FascicoloSiepBdmcModel;
import siap.bdmc.notifichesies.controller.INotificheSies;
import siap.bdmc.notifichesies.model.NotificheSiesModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.ordineesecuzione.controller.IOrdineEsecuzione;
import siap.siep.penaresidua.action.ICostantiPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.util.SIEPLookupRemote;

/**
 *
 * <p>
 * Title: ActInserisciOrdineEsecuzione
 * </p>
 * <p>
 * Description: Inserimento degli Ordini di Esecuzione
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 */
public class ActInserisciOrdineEsecuzione extends ActOrdineEsecuzione implements ICostantiOrdineEsecuzione {

	/**
	 * Azione di Inserimento del Evento
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		/* PosizioneGiuridicaModel lPosizione = */lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());

		String lPage = "";
		// if ("S".equals(lFascicoloModel.getFlagAltraCausa()))
		// {
		// lPage = InserisciOEAltraCausa();
		// }
		// else
		// {
		// Integer lPosInt = new
		// Integer(getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
		Integer lPosInt = CercaPosizioneGiuridicaSeEsisteCercaAltraCausa();

		switch (lPosInt.intValue()) {
		case 1: {
			lPage = InserisciOEDetenutoQC();
			break;
		}
		case 2: {
			lPage = InserisciOEArrestiDomiciliari();
			break;
		}
		// --- Va in altre posizioni --- case 3: { lPage = InserisciOEDetenutoQC();break;}
		case 4: {
			lPage = InserisciOEArrestiDomiciliari();
			break;
		}
		case 7:
		case 16:
		case 20:
		case 46:
		case 47: {
			lPage = InserisciOELibero();
			break;
		}
		// --- Va in altre posizioni --- case 11:{ lPage = InserisciOEDetenutoQC();break;}
		case 10: {
			lPage = InserisciOELibero();
			break;
		}
		case 12: {
			lPage = InserisciOEDetDom();
			break;
		}
		case 70: {
			lPage = InserisciOEDetenutoQcAdPcCc("OE-QC-AD");
			break;
		}
		case 71: {
			lPage = InserisciOEDetenutoQcAdPcCc("OE-QC-PC");
			break;
		}
		case 72: {
			lPage = InserisciOEDetenutoQcAdPcCc("OE-QC-CC");
			break;
		}
		case 73: {
			lPage = InserisciOEDetenutoQCMSP();
			break;
		}
		case 74: {
			lPage = InserisciOEAltraCausa();
			break;
		}
		case 75: {
			lPage = InserisciOEAltraCausaMSD();
			break;
		}
		case 76: {
			lPage = InserisciOEAltraCausa();
			break;
		}
		case 77: {
			lPage = InserisciOEAltraCausaMSP();
			break;
		}
		case 78: {
			lPage = InserisciOEAltraCausaAdPcCcAddpr("OE-DET-AC-CC-AD");
			break;
		}
		case 79: {
			lPage = InserisciOEAltraCausaAdPcCcAddpr("OE-DET-AC-CC-PC");
			break;
		}
		case 80: {
			lPage = InserisciOEAltraCausaAdPcCcAddpr("OE-DET-AC-CC-CC");
			break;
		}
		case 81: {
			lPage = InserisciOEAltraCausaAdPcCcAddpr("OE-DET-AC-CC-ADDPR");
			break;
		}
		default: {
			lPage = InserisciOEAltrePosizioni();
			break;
		}
		}
		// }

		return lPage;
	}

	/**
	 * Inserisci l'ordine di Esecuzione per Libero
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOELibero() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-LIB");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		if (!isRequestParameterNullObj("annoBdmc") && getRequestStringParameter("annoBdmc") != null
				&& getRequestStringParameter("annoBdmc").length() != 0) {

			FascicoloSiepModel lFasModSies = (FascicoloSiepModel) this.getSession().getAttribute("fascicolo");
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("autoritaBdmc"), getRequestStringParameter("sedeBdmc")));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveAnnoSiep(lFasModSies.getChiaveAnno());
			lFasMod.setChiaveUfficioSiep(lFasModSies.getChiaveUfficio());
			lFasMod.setChiaveProgrSiep(lFasModSies.getChiaveProgr());
			lFasMod.setFlagTrasmissione("S");
			lFasMod.setIdEvento(lRetModel.getEvento().getIdEvento());
			lFasMod.setFlagOrdineEsecuzione("S");
			if (!isRequestParameterNullObj("ComuneBdmc") && getRequestStringParameter("ComuneBdmc") != null
					&& getRequestStringParameter("ComuneBdmc").length() != 0) {
				lFasMod.setIstitutoDetenzione(getRequestStringParameter("detenzioneBdmc"));
			}
			if (!isRequestParameterNullObj("altroLuogoBdmc")
					&& getRequestStringParameter("altroLuogoBdmc") != null
					&& getRequestStringParameter("altroLuogoBdmc").length() != 0) {
				lFasMod.setAltroLuogo(getRequestStringParameter("altroLuogoBdmc"));
				lFasMod.setCodComune(
						getCodComuneByDescr(getRequestStringParameter("comAltroLuogBdmc")).getCodComune());
			}
			lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setDataTrasmissione(DateUtils.getSysDate());

			// ===================================================
			// Recupera il controller ed effettua l'inserimento
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			// FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
			/* lFasRetMod = */lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

			// ===================================================
			//
			// Inserisco la realtiva riga per gestione notifiche
			// Verso Bdmc
			//
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(lFasMod.getChiaveAnnoSiep());
			lNotMod.setUfficioSiep(lFasMod.getChiaveUfficioSiep());
			lNotMod.setProgSiep(lFasMod.getChiaveProgrSiep());

			lNotMod.setAnnoFascBdmc(lFasMod.getChiaveAnnoBdmc());
			lNotMod.setUfficioFascBdmc(lFasMod.getChiaveUfficioBdmc());
			lNotMod.setNumeroFascBdmc(lFasMod.getChiaveProgrBdmc());

			lNotMod.setCodOperatoreInserimento(lFasMod.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lFasMod.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lFasMod.getDataInserimento());
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			// lNotMod.setTipoNotifica ("U");
			lNotMod.setTipoNotifica("E");
			lNotMod.setStatoTrasmissione("I");
			lNotMod.setIdEvento(lFasMod.getIdEvento());

			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/* lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);
		}

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOELibero&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'ordine di Esecuzione per Detenzione Domiciliare
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEDetDom() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DETD");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();

		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEDetenutoQC() throws Exception {

		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-QC");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEDetenutoQCMSP() throws Exception {
		// Espiazione Pena in Istituto di Detenzione
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-QC-MSP");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEDetenutoQC&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	// /**
	// * Cerca la Posizione Giuridica, Se Esiste Cerca Altra Causa
	// * @return
	// * @throws Exception
	// */
	// private Integer CercaPosizioneGiuridicaSeEsisteCercaAltraCausa() throws Exception
	// {
	//
	// //============================================================
	// // Cerca POSIZIONE_GIURIDICA corrente e se esiste ALTRA_CAUSA
	// //============================================================
	//
	// Integer lPosInt = new
	// Integer(getRequestStringParameter(ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA));
	//
	// FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel) getSessionAttribute("fascicolo");
	// PosizioneGiuridicaSqlDAO lPosSqlDao = null;
	// IPosizioneGiuridica lCtrlPosizioneGiuridica = SIEPLookupRemote.getPosizioneGiuridicaRemote();
	// PosizioneGiuridicaModel lPosMod =
	// lCtrlPosizioneGiuridica.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lFascicoloModel.getIdFascicoloSiep());
	//
	// AltraCausaModel lAcModel = new AltraCausaModel();
	// if (lPosInt==07 || lPosMod.getAltCauIdAltraCausa()!=null){
	// IAltraCausa lAC = SIEPLookupRemote.getAltraCausa();
	// lAcModel = lAC.ExRicercaAltraCausaByFascicolo(lFascicoloModel.getIdFascicoloSiep());
	// lPosInt = Integer.valueOf(lAcModel.getCodTipoPosGiuridica());
	// }
	//
	// return lPosInt;
	// }

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Arresti Domiciliari
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEArrestiDomiciliari() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-ARR");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Arresti Domiciliari
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEDetenutoQcAdPcCc(String ordineEsecuzione) throws Exception { // Espiazione Pena
																							// in Altro Luogo
		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setDescrMotivo("OE-QC-AD");
		lEve.getEvento().setDescrMotivo(ordineEsecuzione);

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEArrestiDomiciliari&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltraCausa() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-AC");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		if (!isRequestParameterNullObj("annoBdmc") && getRequestStringParameter("annoBdmc") != null
				&& getRequestStringParameter("annoBdmc").length() != 0) {

			FascicoloSiepModel lFasModSies = (FascicoloSiepModel) this.getSession().getAttribute("fascicolo");
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("autoritaBdmc"), getRequestStringParameter("sedeBdmc")));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveAnnoSiep(lFasModSies.getChiaveAnno());
			lFasMod.setChiaveUfficioSiep(lFasModSies.getChiaveUfficio());
			lFasMod.setChiaveProgrSiep(lFasModSies.getChiaveProgr());
			lFasMod.setFlagTrasmissione("S");
			lFasMod.setIdEvento(lRetModel.getEvento().getIdEvento());
			lFasMod.setFlagOrdineEsecuzione("S");
			if (!isRequestParameterNullObj("ComuneBdmc") && getRequestStringParameter("ComuneBdmc") != null
					&& getRequestStringParameter("ComuneBdmc").length() != 0) {
				lFasMod.setIstitutoDetenzione(getRequestStringParameter("detenzioneBdmc"));
			}
			if (!isRequestParameterNullObj("altroLuogoBdmc")
					&& getRequestStringParameter("altroLuogoBdmc") != null
					&& getRequestStringParameter("altroLuogoBdmc").length() != 0) {
				lFasMod.setAltroLuogo(getRequestStringParameter("altroLuogoBdmc"));
				lFasMod.setCodComune(
						getCodComuneByDescr(getRequestStringParameter("comAltroLuogBdmc")).getCodComune());
			}
			lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setDataTrasmissione(DateUtils.getSysDate());

			// ===================================================
			// Recupera il controller ed effettua l'inserimento
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			// FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
			/* lFasRetMod = */lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

			// ===================================================
			//
			// Inserisco la realtiva riga per gestione notifiche
			// Verso Bdmc
			//
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(lFasMod.getChiaveAnnoSiep());
			lNotMod.setUfficioSiep(lFasMod.getChiaveUfficioSiep());
			lNotMod.setProgSiep(lFasMod.getChiaveProgrSiep());

			lNotMod.setAnnoFascBdmc(lFasMod.getChiaveAnnoBdmc());
			lNotMod.setUfficioFascBdmc(lFasMod.getChiaveUfficioBdmc());
			lNotMod.setNumeroFascBdmc(lFasMod.getChiaveProgrBdmc());

			lNotMod.setCodOperatoreInserimento(lFasMod.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lFasMod.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lFasMod.getDataInserimento());
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			lNotMod.setTipoNotifica("E");
			lNotMod.setStatoTrasmissione("I");
			lNotMod.setIdEvento(lFasMod.getIdEvento());

			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/* lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);

		}

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausa&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltraCausaAdPcCcAddpr(String ordineEsecuzione) throws Exception { // Detenuto
																								// Altra Causa
																								// - Misura
																								// Cautelare
																								// in Altro
																								// Luogo
		EventoNotificaModel lEve = new EventoNotificaModel();
		// lEve.getEvento().setDescrMotivo("OE-DET-AC-CC-AD");
		lEve.getEvento().setDescrMotivo(ordineEsecuzione);

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		if (!isRequestParameterNullObj("annoBdmc") && getRequestStringParameter("annoBdmc") != null
				&& getRequestStringParameter("annoBdmc").length() != 0) {

			FascicoloSiepModel lFasModSies = (FascicoloSiepModel) this.getSession().getAttribute("fascicolo");
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("autoritaBdmc"), getRequestStringParameter("sedeBdmc")));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveAnnoSiep(lFasModSies.getChiaveAnno());
			lFasMod.setChiaveUfficioSiep(lFasModSies.getChiaveUfficio());
			lFasMod.setChiaveProgrSiep(lFasModSies.getChiaveProgr());
			lFasMod.setFlagTrasmissione("S");
			lFasMod.setIdEvento(lRetModel.getEvento().getIdEvento());
			lFasMod.setFlagOrdineEsecuzione("S");
			if (!isRequestParameterNullObj("ComuneBdmc") && getRequestStringParameter("ComuneBdmc") != null
					&& getRequestStringParameter("ComuneBdmc").length() != 0) {
				lFasMod.setIstitutoDetenzione(getRequestStringParameter("detenzioneBdmc"));
			}
			if (!isRequestParameterNullObj("altroLuogoBdmc")
					&& getRequestStringParameter("altroLuogoBdmc") != null
					&& getRequestStringParameter("altroLuogoBdmc").length() != 0) {
				lFasMod.setAltroLuogo(getRequestStringParameter("altroLuogoBdmc"));
				lFasMod.setCodComune(
						getCodComuneByDescr(getRequestStringParameter("comAltroLuogBdmc")).getCodComune());
			}
			lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setDataTrasmissione(DateUtils.getSysDate());

			// ===================================================
			// Recupera il controller ed effettua l'inserimento
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			// FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
			/* lFasRetMod = */lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

			// ===================================================
			//
			// Inserisco la realtiva riga per gestione notifiche
			// Verso Bdmc
			//
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(lFasMod.getChiaveAnnoSiep());
			lNotMod.setUfficioSiep(lFasMod.getChiaveUfficioSiep());
			lNotMod.setProgSiep(lFasMod.getChiaveProgrSiep());

			lNotMod.setAnnoFascBdmc(lFasMod.getChiaveAnnoBdmc());
			lNotMod.setUfficioFascBdmc(lFasMod.getChiaveUfficioBdmc());
			lNotMod.setNumeroFascBdmc(lFasMod.getChiaveProgrBdmc());

			lNotMod.setCodOperatoreInserimento(lFasMod.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lFasMod.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lFasMod.getDataInserimento());
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			lNotMod.setTipoNotifica("E");
			lNotMod.setStatoTrasmissione("I");
			lNotMod.setIdEvento(lFasMod.getIdEvento());

			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/* lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);

		}

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausa&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltraCausaMSD() throws Exception { // Detenuto Altra Causa - Definitivo in
																	// Istituto
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-AC-MSD");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		if (!isRequestParameterNullObj("annoBdmc") && getRequestStringParameter("annoBdmc") != null
				&& getRequestStringParameter("annoBdmc").length() != 0) {

			FascicoloSiepModel lFasModSies = (FascicoloSiepModel) this.getSession().getAttribute("fascicolo");
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("autoritaBdmc"), getRequestStringParameter("sedeBdmc")));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveAnnoSiep(lFasModSies.getChiaveAnno());
			lFasMod.setChiaveUfficioSiep(lFasModSies.getChiaveUfficio());
			lFasMod.setChiaveProgrSiep(lFasModSies.getChiaveProgr());
			lFasMod.setFlagTrasmissione("S");
			lFasMod.setIdEvento(lRetModel.getEvento().getIdEvento());
			lFasMod.setFlagOrdineEsecuzione("S");
			if (!isRequestParameterNullObj("ComuneBdmc") && getRequestStringParameter("ComuneBdmc") != null
					&& getRequestStringParameter("ComuneBdmc").length() != 0) {
				lFasMod.setIstitutoDetenzione(getRequestStringParameter("detenzioneBdmc"));
			}
			if (!isRequestParameterNullObj("altroLuogoBdmc")
					&& getRequestStringParameter("altroLuogoBdmc") != null
					&& getRequestStringParameter("altroLuogoBdmc").length() != 0) {
				lFasMod.setAltroLuogo(getRequestStringParameter("altroLuogoBdmc"));
				lFasMod.setCodComune(
						getCodComuneByDescr(getRequestStringParameter("comAltroLuogBdmc")).getCodComune());
			}
			lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setDataTrasmissione(DateUtils.getSysDate());

			// ===================================================
			// Recupera il controller ed effettua l'inserimento
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			// FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
			/* lFasRetMod = */lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

			// ===================================================
			//
			// Inserisco la realtiva riga per gestione notifiche
			// Verso Bdmc
			//
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(lFasMod.getChiaveAnnoSiep());
			lNotMod.setUfficioSiep(lFasMod.getChiaveUfficioSiep());
			lNotMod.setProgSiep(lFasMod.getChiaveProgrSiep());

			lNotMod.setAnnoFascBdmc(lFasMod.getChiaveAnnoBdmc());
			lNotMod.setUfficioFascBdmc(lFasMod.getChiaveUfficioBdmc());
			lNotMod.setNumeroFascBdmc(lFasMod.getChiaveProgrBdmc());

			lNotMod.setCodOperatoreInserimento(lFasMod.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lFasMod.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lFasMod.getDataInserimento());
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			lNotMod.setTipoNotifica("E");
			lNotMod.setStatoTrasmissione("I");
			lNotMod.setIdEvento(lFasMod.getIdEvento());

			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/* lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);

		}

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausa&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per un Detenuto Questa Causa
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltraCausaMSP() throws Exception { // Detenuto Altra Causa - Misura Cautelare in
																	// Istituto
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("OE-DET-AC-MSP");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		if (!isRequestParameterNullObj("annoBdmc") && getRequestStringParameter("annoBdmc") != null
				&& getRequestStringParameter("annoBdmc").length() != 0) {

			FascicoloSiepModel lFasModSies = (FascicoloSiepModel) this.getSession().getAttribute("fascicolo");
			FascicoloSiepBdmcModel lFasMod = new FascicoloSiepBdmcModel();

			lFasMod.setChiaveAnnoBdmc(getRequestBigDecimalParameter("annoBdmc"));
			lFasMod.setChiaveUfficioBdmc(getCodUfficioByCodTipoUfficioDescrComune(
					getRequestStringParameter("autoritaBdmc"), getRequestStringParameter("sedeBdmc")));
			lFasMod.setChiaveProgrBdmc(getRequestBigDecimalParameter("numeroBdmc"));
			lFasMod.setChiaveAnnoSiep(lFasModSies.getChiaveAnno());
			lFasMod.setChiaveUfficioSiep(lFasModSies.getChiaveUfficio());
			lFasMod.setChiaveProgrSiep(lFasModSies.getChiaveProgr());
			lFasMod.setFlagTrasmissione("S");
			lFasMod.setIdEvento(lRetModel.getEvento().getIdEvento());
			lFasMod.setFlagOrdineEsecuzione("S");
			if (!isRequestParameterNullObj("ComuneBdmc") && getRequestStringParameter("ComuneBdmc") != null
					&& getRequestStringParameter("ComuneBdmc").length() != 0) {
				lFasMod.setIstitutoDetenzione(getRequestStringParameter("detenzioneBdmc"));
			}
			if (!isRequestParameterNullObj("altroLuogoBdmc")
					&& getRequestStringParameter("altroLuogoBdmc") != null
					&& getRequestStringParameter("altroLuogoBdmc").length() != 0) {
				lFasMod.setAltroLuogo(getRequestStringParameter("altroLuogoBdmc"));
				lFasMod.setCodComune(
						getCodComuneByDescr(getRequestStringParameter("comAltroLuogBdmc")).getCodComune());
			}
			lFasMod.setCodOperatoreInserimento(this.getUtenteConnesso().getUserId());
			lFasMod.setCodUfficioInserimento(this.getUfficioUtenteConnesso().getCodUfficio());

			lFasMod.setDataInserimento(DateUtils.getSysDate());
			lFasMod.setDataTrasmissione(DateUtils.getSysDate());

			// ===================================================
			// Recupera il controller ed effettua l'inserimento
			// ===================================================
			IFascicoloSiepBdmc lCtrl1 = BDMCLookupRemote.getFascicoloSiepBdmcRemote();
			// FascicoloSiepBdmcModel lFasRetMod = new FascicoloSiepBdmcModel();
			/* lFasRetMod = */lCtrl1.ExInserisciFascicoloSiepBdmc(lFasMod);

			// ===================================================
			//
			// Inserisco la realtiva riga per gestione notifiche
			// Verso Bdmc
			//
			// ===================================================
			NotificheSiesModel lNotMod = new NotificheSiesModel();

			lNotMod.setAnnoSiep(lFasMod.getChiaveAnnoSiep());
			lNotMod.setUfficioSiep(lFasMod.getChiaveUfficioSiep());
			lNotMod.setProgSiep(lFasMod.getChiaveProgrSiep());

			lNotMod.setAnnoFascBdmc(lFasMod.getChiaveAnnoBdmc());
			lNotMod.setUfficioFascBdmc(lFasMod.getChiaveUfficioBdmc());
			lNotMod.setNumeroFascBdmc(lFasMod.getChiaveProgrBdmc());

			lNotMod.setCodOperatoreInserimento(lFasMod.getCodOperatoreInserimento());
			lNotMod.setCodUfficioInserimento(lFasMod.getCodUfficioInserimento());
			lNotMod.setDataInserimento(lFasMod.getDataInserimento());
			lNotMod.setDataNotifica(DateUtils.getSysDate());
			lNotMod.setTipoNotifica("E");
			lNotMod.setStatoTrasmissione("I");
			lNotMod.setIdEvento(lFasMod.getIdEvento());

			INotificheSies lCtrl2 = BDMCLookupRemote.getNotificheSiesRemote();
			// NotificheSiesModel lNotRetMod = new NotificheSiesModel();
			/* lNotRetMod = */lCtrl2.ExInserisciNotificheSies(lNotMod);
		}

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActLoadDettaglioOEAltraCausa&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

	/**
	 * Inserisci l'Ordine di Esecuzione per Altre posizioni giuridiche
	 *
	 * @return
	 * @throws Exception
	 */
	private String InserisciOEAltrePosizioni() throws Exception {
		EventoNotificaModel lEve = new EventoNotificaModel();
		lEve.getEvento().setDescrMotivo("0000");

		lEve.setEvento(setEventoOrdineEsecuzione(lEve.getEvento()));
		lEve.getMagistrato()
				.setCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));
		// lEve.getMagistrato().getMagistratoCompetente().setMagCodMagistrato(getRequestStringParameter(ICostantiEvento.CAMPO_COD_MAGISTRATO));

		// Inserisco l'array di Notifiche nell'Evento
		NotificaModel[] lNotifiche = this.setNotificheOrdineEsecuzione();
		lEve.setNotifiche(lNotifiche);

		BigDecimal lIdPenaRes = getRequestBigDecimalParameter(ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA);
		PenaResiduaModel lPenaRes = new PenaResiduaModel();
		lPenaRes.setIdPenaResidua(lIdPenaRes);
		if (!isRequestParameterNullObj(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE))
			lPenaRes.setDataFine(getRequestDateParameter(ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE,
					ICostantiPenaResidua.CAMPO_MESE_DATA_FINE, ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE));

		// Cerca un Ordine di Esecuzione, se non presente viene inserito, altrimenti viene modificato
		IOrdineEsecuzione lCtrlOrdineEsecuzione = SIEPLookupRemote.getOrdineEsecuzioneRemote();
		EventoNotificaModel lRetModel = lCtrlOrdineEsecuzione.ExInserisciOModificaOENotifica(lEve, lPenaRes);

		String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD
				+ "=siap.siep.ordineesecuzione.action.ActDettaglioOEAltrePosizioni&"
				+ ICostantiEvento.CAMPO_ID_EVENTO + "=" + lRetModel.getEvento().getIdEvento() + "&modalita=I";

		return lPage;
	}

}