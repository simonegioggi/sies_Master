package siap.siep.sospensione.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.decretoordinanza.action.ICostantiDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.notifica.model.NotificaModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActLoadInserisciNotificheDifferimento
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Sospensione Notifiche Differimento
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

public class ActLoadInserisciNotificheDifferimento extends ActionSiap
		implements ICostantiSospensione, ICostantiDecretoOrdinanzaSiep {
	FascicoloSiepModel mFascMod = null;
	DecretoOrdinanzaSiepModel mDecOrdMod = null;

	/**
	 * Carica i dati per la finestra di inserimento del Decreto di Sospensione e relative Notifiche
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {
		mFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = mFascMod.getIdFascicoloSiep();

		// ==========================================================================
		// Recupero i dati del Decreto Ordinanza Siep
		// ==========================================================================
		BigDecimal lIdDecOrd = getRequestBigDecimalParameter(CAMPO_ID_DECRETO_ORDINANZA_SIEP);
		IDecretoOrdinanzaSiep lCtrl = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		mDecOrdMod = lCtrl.ExRicercaDecretoOrdinanzaSiepByKey(lIdDecOrd);
		setRequestAttribute("decretoordinanza", mDecOrdMod);

		// ==========================================================================
		// Recupero la Posizione Giuridica/luogo di detenzione
		// ==========================================================================
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				mFascMod.getIdFascicoloSiep());
		setRequestAttribute("posizioneluogoaltra", lPos);

		// ==========================================================================
		// Recupero se Ergastolo da Pena Complessiva
		// ==========================================================================
		IPenaComplessiva ICtrlPenCom = SIEPLookupRemote.getPenaComplessivaRemote();
		PenaComplessivaModel lPenComMod = ICtrlPenCom.ExRicercaPenaComplessivaByIdFascicolo(lIdFascicolo);

		if (lPenComMod == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Pena Complessiva non presente. Impossibile eseguire la richiesta.");

		String lFlagErgastolo = "N";
		if (lPenComMod.getCodTipoPenaDetentiva() != null && lPenComMod.getCodTipoPenaDetentiva() != "") {
			if (lPenComMod.getCodTipoPenaDetentiva().equals("03")) {
				lFlagErgastolo = "S";
			} else if (lPenComMod.getCodTipoPenaDetentiva().equals("04")) {
				lFlagErgastolo = "D";
			}
		}
		setRequestAttribute("flagergastolo", lFlagErgastolo);

		// ==========================================================================
		// Recupero Pena Residua
		// ==========================================================================
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaPerFascicolo(lIdFascicolo);
		setRequestAttribute("penaresidua", lPenaResidua);

		// ==========================================================================
		// Recupero Sospensione
		// ==========================================================================
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp
				.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());
		setRequestAttribute("sospensione", lSospensione);

		// ==========================================================================
		// Carico i dati della Combo "Autorità Destinazione"
		// ==========================================================================
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOption);

		// ==========================================================================
		// Recupero gli Avvocati
		// ==========================================================================
		IAvvocato lAvvCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = lAvvCtrl.ExRicercaAvvocatiByFascicolo(mFascMod.getIdFascicoloSiep());
		setRequestAttribute("avvocati", lAvvocati);

		// ==========================================================================
		// Recupero il Magistrato Competente
		// ==========================================================================
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(mFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// ==========================================================================
		// Verifico se a sistema è presente già un evento legato all'emissione,
		// del Decreto di Sospensione SIES (sto in modifica evento), in questo caso
		// recupero i dati da precaricare in maschera.
		// ==========================================================================
		EventoModel lEveRic = creaEventoRicerca();
		EventoModel lEve = new EventoModel();

		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		lEve = lCtrlEve.ExRicercaEventoNonRegistrato(lEveRic);
		if (lEve != null) { // Esiste un evento non registrato
			EventoNotificaModel lEveNot = new EventoNotificaModel();
			lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(lEve.getIdEvento());
			if (lEveNot != null) {
				setRequestAttribute("eventonotifica", lEveNot);

				IUfficio lUff = SICOLookupRemote.getUfficioRemote();

				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				NotificaModel[] lNotifiche = lEveNot.getNotifiche();
				for (int i = 0; i < lNotifiche.length; i++) {
					// TDS
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
						UfficioModel lUffModTDS = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffTDS", lUffModTDS);
					}
					if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {
						IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
								lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
						setRequestAttribute("Istituto", lModIst);
					}
				}
			}
		}

		return PG_LOAD_INSERISCI_NOTIFICHE_DIFFERIMENTO;
	}

	// Luigi 20-09-2005
	/****************************************************************************
	 * La funzione crea l'Evento da ricercare in base al tipo decreto
	 */
	private EventoModel creaEventoRicerca() {
		EventoModel lEveRic = new EventoModel();

		lEveRic.setCodTipoEvento("01");
		lEveRic.setFasSieIdFascicoloSiep(mFascMod.getIdFascicoloSiep());

		if (mDecOrdMod.getCodTipoAutoritaEmittente().equals("UDS")) {
			// Differimento provvisorio
			if (mDecOrdMod.getFlagScarcerareScarcerato().equals("D")) {
				// Detenuto da scarcerare
				lEveRic.setCodMotivo("0274"); // Rinvio provvisorio dell'esecuzione
				lEveRic.setCodTipoProvvedimento("09"); // Ordine Scarcerazione
			} else {
				// Detenuto già scarcerato
				lEveRic.setCodMotivo("0274"); // Rinvio provvisorio dell'esecuzione
				lEveRic.setCodTipoProvvedimento("12"); // Comunicazione
			}
		} else /* if (mDecOrdMod.getCodTipoAutoritaEmittente().equals("TDS")) */
		{
			// Differimento definitivo
			if (mDecOrdMod.getFlagScarcerareScarcerato().equals("D")) {
				// Detenuto da scarcerare
				lEveRic.setCodMotivo("0221"); // rinvio dell'esecuzione ex art. 684 c.1 c.p.p.
				lEveRic.setCodTipoProvvedimento("09"); // Ordine Scarcerazione
			} else {
				// Detenuto già scarcerato
				lEveRic.setCodMotivo("0221");
				lEveRic.setCodTipoProvvedimento("12");
			}
		}
		// I codici Motivo e Tipo Provvedimento vengono passati nella
		// request perchè serviranno nella fase di inserimento
		setRequestAttribute("CodMotivo", lEveRic.getCodMotivo());
		setRequestAttribute("CodTipoProvvedimento", lEveRic.getCodTipoProvvedimento());
		return lEveRic;
	}

}