package siap.siep.sospensione.action;

import java.math.BigDecimal;

import siap.sico.cssa.model.CSSAModel;
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
import siap.siep.SIEPException;
import siap.siep.autoritaesterna.model.AutoritaEsternaModel;
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
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActLoadDettaglioDifferimentoOE
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio del Provvedimento dell'Esecuzione relativo al
 * Differimento. Viene caricato il dettaglio del provvedimento e le notifiche. I dati caricati variano in
 * funzione del tipo di Provvedimento Ordine di esecuzione, comunicazione , annotazione....
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

public class ActLoadDettaglioDifferimentoOE extends ActLoadInserisciDifferimentoMaster implements
		ICostantiSospensione, ICostantiEvento {
	public String processRequest() throws F3BException {
		/*
		 * I dati da restituire vengono visualizzati nella maschera che è identica a quella del dettaglio
		 * fatto salvo per la sezione dell'inserimento delle notifiche Quindi la prima parte di questo metodo
		 * è identica alla prima parte della ActLoadInsDifferimentoOE.
		 */

		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Id dell'evento (Provvedimento) emesso
		BigDecimal lIdEvento = getRequestBigDecimalParameter(CAMPO_ID_EVENTO);

		// ==========================================================================
		// Verifico l'esistenza della Posizione Giuridica da caricare in maschera
		// ==========================================================================
		/*
		 * REWORK DETTAGLIO IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new
		 * PosizioneGiuridicaLuogoDetenzioneAltraCausaModel(); lPos =
		 * lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo
		 * (lFascMod.getIdFascicoloSiep());
		 */
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = this
				.getPosizioneGiuridicaLuogoDetenzioneAltraCausa(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();

		// ==========================================================================
		// Verifico se Ergastolo recuperando il dato dalla Pena Complessiva
		// ==========================================================================
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

		// ==========================================================================
		// Recupero la Pena Residua (da caricare in maschera)
		// Dovrebbe recuperare l'ultima non validata, quella inserita con il nuovo
		// evento. (recupera l'ultima in ordine cronologico)
		// ==========================================================================

		/*
		 * REWORK DETTAGLIO IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		 * PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);
		 */
		PenaResiduaModel llPenMod = this.getPenaResidua(lIdEvento, lFascMod.getIdFascicoloSiep());
		setRequestAttribute("penaresidua", llPenMod);

		// =========================================================
		// Recupero i dati della Sospensione (pena espiata)
		// =========================================================
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp.ExRicercaSospensioneByIdPenaResidua(llPenMod
				.getIdPenaResidua());

		setRequestAttribute("sospensione", lSospensione);

		// ==========================================================================
		// Recupero i dati del Provvedimento e delle notifiche associate (se presenti)
		// ==========================================================================
		IEvento lCtrl = SICOLookupRemote.getEventoRemote();
		EventoNotificaModel lEveNotMod = lCtrl.ExRicercaEventoNotificaByKey(lIdEvento);

		setRequestAttribute("eventonotifica", lEveNotMod);

		// ==========================================================================
		// Recupero i dettagli dei destinatari delle notifiche
		// ==========================================================================
		NotificaModel[] lNotifiche = lEveNotMod.getNotifiche();
		IUfficio lUff = SICOLookupRemote.getUfficioRemote();
		IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
		for (int i = 0; i < lNotifiche.length; i++) {
			// TDS
//			if (lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("E")) {
			// MEV10-s3: modificato il valore del tipo notifica
			if (lNotifiche[i].getUffCodUfficio() != null && lNotifiche[i].getCodTipoNotifica().equals("TS")) {
				UfficioModel lUffModTDS = lUff
						.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
				setRequestAttribute("uffTDS", lUffModTDS);
			}

			// Istituto di Detenzione
			if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
					&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
				IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(lNotifiche[i]
						.getIstDetIdIstitutoDetenzione().toUpperCase());
				setRequestAttribute("Istituto", lModIst);
			}

			// Autorità competente
			if (lNotifiche[i].getAutoritaEsterna() != null && lNotifiche[i].getCodTipoNotifica().equals("E")) {
				AutoritaEsternaModel lModAut = lNotifiche[i].getAutoritaEsterna();
				setRequestAttribute("autorita", lModAut);
			}

			// CSSA
			if (lNotifiche[i].getCssIdCssa() != null && lNotifiche[i].getCodTipoNotifica().equals("C")) {
				CSSAModel lCSSA = lNotifiche[i].getCSSA();
				setRequestAttribute("CSSA", lCSSA);
			}
		}

		// ==========================================================================
		// Recupero i dati del MAGISTRATO COMPETENTE
		// ==========================================================================
		// Magistrato
		IMagistrato lCtrlM = SICOLookupRemote.getMagistratoRemote();
		MagistratoModel lMagi = lCtrlM.ExRicercaMagistratoByCod(lEveNotMod.getEvento().getCodMagistrato());
		setRequestAttribute("magistratocompetente", lMagi);

		// ==========================================================================
		// Recupero i dati della Misura Alternativa
		// n.b. non recupero i dati dalle tabelle DEPOSITO_ORDINANZA_PC,
		// DEPOSITO_DECRETO e TENORE in quanto non contengono dati da visualizzare
		// nella maschera di dettaglio o utilizzabili nei calcoli successivi
		// ==========================================================================
		BigDecimal lIdEventoProvvSorv = lEveNotMod.getEvento().getEveIdEvento();

		IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
		MisuraAlternativaModel lMisAltDiff = null;
		lMisAltDiff = lMisAltCtrl.ExRicercaMisuraAlternativaByIdEvento(lIdEventoProvvSorv);

		setRequestAttribute("misuraalternativa", lMisAltDiff);

		// ==========================================================================
		// Recupero i dati dell'ufficio che ha emesso il provvedimento (tipo, luogo...)
		// ==========================================================================
		if (lMisAltDiff != null) {
			UfficioModel lUffMod = new UfficioModel();
			IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
			lUffMod = lCtrlUffEmi.getUfficioByKey(lMisAltDiff.getChiaveUfficioFascicoloSius());

			setRequestAttribute("UfficioEmittente", lUffMod);
			// ==========================================================================
			// Setto il tipo di provvedimento
			// ==========================================================================
			String tipoProvvedimento = getTipoProvvedimento(lMisAltDiff);
			setRequestAttribute("tipoProvvedimento", tipoProvvedimento);
		}

		// MEV10-s3: aggiunta impostazione attributo nella richiesta
		UtenteModel lUtenteMod = new UtenteModel(
				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));
		String lCodTipoUfficio = lUtenteMod.getUfficioUtente().getCodTipoUfficio();
		setRequestAttribute("codiceTipoUfficio", lCodTipoUfficio);

		String lPage = IWebConstants.ROOT_DIR
				+ "/files/siap/siep/sospensione/LoadDettaglioDifferimentoOE.jsp";

		return lPage;
		// return PG_LOAD_DETTAGLIO_NOTIFICHE_DIFFERIMENTO;
	}

}