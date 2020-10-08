package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistrato.controller.IMagistrato;
import siap.sico.magistrato.model.MagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.web.ActSIESDettaglioProvvedimento;
import siap.sius.documentoallegato.controller.IDocumentoAllegato;
import siap.sius.documentoallegato.model.DocumentoAllegatoModel;
import siap.sius.util.SIUSLookupRemote;
/**
 * <p>
 * Title: ActLoadDettaglioSospensioneEsecPenaDispPm
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Sospensione
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
public class ActLoadDettaglioSospensioneEsecPenaDispPm extends ActSIESDettaglioProvvedimento
		implements ICostantiSospensione {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();
		BigDecimal lIdEvento = null;
		DecretoOrdinanzaSiepModel lDecOrdMod = null;

		if (!this.isRequestParameterNullObj("IdEvento")) {
			lIdEvento = getRequestBigDecimalParameter("IdEvento");
		}

		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		lDecOrdMod = lCtrl.ExRicercaUltimaDecretoOrdinanzaSiepByIdEvento(lIdEvento);
		setRequestAttribute("decretoordinanza", lDecOrdMod);

		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(lFascMod.
		 * getIdFascicoloSiep());
		 */

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lIdFascicolo);

		setRequestAttribute("posizioneluogoaltra", lPos);

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrlEve.ExRicercaEventoNotificaByKey(lIdEvento);
		setRequestAttribute("eventonotifica", lEveNotMod);

		IUfficio lUff = SICOLookupRemote.getUfficioRemote();

		IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		for (int i = 0; i < lNotifiche.length; i++) {
			// MEV_66: aggiunto campo in visualizzazione udsm + tdsm
			// TDS
			if (lNotifiche[i].getUffCodUfficio() != null && "NC".equals(lNotifiche[i].getCodTipoNotifica())
					&& lNotifiche[i].getUfficio() != null
					&& lNotifiche[i].getUfficio().getCodTipoUfficio() != null
					&& ("TDS".equals(lNotifiche[i].getUfficio().getCodTipoUfficio())
							|| "TDSM".equals(lNotifiche[i].getUfficio().getCodTipoUfficio()))) {
				UfficioModel lUffModTDS = lUff
						.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
				setRequestAttribute("uffTDS", lUffModTDS);
			}

			// UDS
			if (lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("NC")
					&& lNotifiche[i].getUfficio() != null
					&& lNotifiche[i].getUfficio().getCodTipoUfficio() != null
					&& ("UDS".equals(lNotifiche[i].getUfficio().getCodTipoUfficio())
							|| "UDSM".equals(lNotifiche[i].getUfficio().getCodTipoUfficio()))) {
				UfficioModel lUffModUDS = lUff
						.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
				setRequestAttribute("uffUDS", lUffModUDS);
			}
			// FINE MEV_66

			// ISTITUTO
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
					&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
				IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
						lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
				setRequestAttribute("Istituto", lModIst);
			}

			// UFFICIALI GIUDIZIARI PER CONDANNATO
			if (lNotifiche[i].getAutoritaEsterna() != null
					&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
				setRequestAttribute("autoritaUGC", lNotifiche[i].getAutoritaEsterna());
				setRequestAttribute("NoteUGC", lNotifiche[i].getNote());
			}

			// AUTORITA DI POLIZIA
			if (lNotifiche[i].getAutoritaEsterna() != null
					&& lNotifiche[i].getCodTipoNotifica().equals("ND")) {
				setRequestAttribute("autorita", lNotifiche[i].getAutoritaEsterna());
				setRequestAttribute("Noteautorita", lNotifiche[i].getNote());
			}

			// GE
			if (lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("NG")) {
				UfficioModel lUffModTDS = lUff
						.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
				setRequestAttribute("uffGE", lUffModTDS);
			}

			// CSSA
			if (lNotifiche[i].getCSSA() != null)
				setRequestAttribute("cssa", lNotifiche[i].getCSSA());
		}

		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		// se la Pena Complessiva è un ergastolo o ergastolo con isolamento diurno
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}

		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// pena residua
		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		 * PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);
		 */

		PenaResiduaModel lPenaResMod = this.getPenaResidua(lIdEvento, lIdFascicolo);

		setRequestAttribute("penaresidua", lPenaResMod);

		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp
				.ExRicercaSospensioneByIdPenaResidua(lPenaResMod.getIdPenaResidua());
		setRequestAttribute("sospensione", lSospensione);

		/*
		 * REWORK //MAGISTRATO COMPETENTE IMagistratoCompetente lMagComp =
		 * SICOLookupRemote.getMagistratoCompetenteRemote(); MagistratoCompetenteMagistratoModel lMagMod =
		 * lMagComp.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep()); if (lMagMod !=
		 * null) setRequestAttribute("magistratocompetente", lMagMod);
		 */

		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		if (lMagi != null)
			setRequestAttribute("magistratocompetente", lMagi);

		// provvedimento:
		// se il provvedimento è stato annullato (EVENTO.FLAG_DOCUMENTO_REGISTRATO=A || null),
		// il bottone "FC" non deve essere visibile
		// *****************************************
		// foglio complementare:
		// ricerca nella tabella Documento_Allegato
		// se il foglio complementare non esiste o è stato annullato tramite il bottone "FC" si deve
		// permettere l'inserimento
		// altrimenti si va in modifica
		DocumentoAllegatoModel mDocAll = null;
		IDocumentoAllegato mDocAllCtrl = null;
		mDocAllCtrl = SIUSLookupRemote.getDocumentoAllegatoRemote();
		mDocAll = mDocAllCtrl
				.ExRicercaDocumentoAllegatoByIdEventoCodTipo(lEveNotMod.getEvento().getIdEvento(), "06");
		setRequestAttribute("documentoAllegato", mDocAll);

		// Imposta la Combo contenente le Motivazioni non Inviato FC.
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoNonInvio());
		setRequestAttribute("motivoNonInvio", "" + lOption);

		// ricerca Evento OE (l'ultimo inserito per data inserimento)
		EventoModel lEveModRic = new EventoModel();

		lEveModRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		lEveModRic.setCodTipoProvvedimento("06");
		lEveModRic.setCodTipoEvento("01");

		// EventoModel lEveOE = null;
		try {
			List lEveVect = lCtrlEve.ExRicercaEventoTipoEveTipoProvMot(lEveModRic, "S");

			if (!lEveVect.isEmpty()) {
				setRequestAttribute("eventoOE", lEveVect.get(0));
			}
		} catch (Exception e) {
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
			// LogF3B.getLogger()
			siesLogger.warn("Evento OE non trovato");
		}

		return PG_LOAD_DETTAGLIO_SOSPENSIONE_ESEC_PM;
	}

}