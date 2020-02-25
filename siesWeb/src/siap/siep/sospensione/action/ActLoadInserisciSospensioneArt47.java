package siap.siep.sospensione.action;

/**
 * <p>Title: ActLoadInserisciSospensioneArt47</p>
 * <p>Description: Classe Action per la load inserimento di Sospensione Art. 47 </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Collection;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
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
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
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
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.sospensione.controller.ISospensione;
import siap.siep.sospensione.model.SospensioneModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciSospensioneArt47 extends ActionSiap implements ICostantiSospensione {
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");
		BigDecimal lIdFascicolo = lFascMod.getIdFascicoloSiep();

		// Controllo Validazione Fascicolo
		if (lFascMod.getFlagValidato().equalsIgnoreCase("N")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stato Validato. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		this.isFascicoloSiepDiCompetenza();

		// Controllo Fascicolo definito
		if (lFascMod.getDescrStatoFascicolo().equalsIgnoreCase("ARCHIVIATO/DEFINITO")) {
			RedirectTo lRedirigi = new RedirectTo();

			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Il Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " risulta Definito. Impossibile procedere!");
			lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		/******************************* Posizione Giuridica **********************************/
		// IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		// PosizioneGiuridicaModel lPosizione =
		// lCtrlPosGiu.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPos = new PosizioneGiuridicaLuogoDetenzioneAltraCausaModel();
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		lPos = lPosCtrl.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
				lFascMod.getIdFascicoloSiep());

		PosizioneGiuridicaModel lPosizione = lPos.getPosizioneGiuridica();
		setRequestAttribute("posizioneluogoaltra", lPos);

		setRequestAttribute("posizione", lPosizione);
		if (lPos.getPosizioneGiuridica() == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		/******************************* Pena Complessiva *****************************/
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
		/******************************* Fine Pena Complessiva ************************/

		/******************************* Pena Residua ***********************************/
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();
		// PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);
		// PenaResiduaModel lPenaResidua =
		// lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);

		// La Pena Residua deve essere presente e deve avere settate le date inizio/fine
		// (a meno del caso libero in cui le date non ci sono e puo' non essere validata)

		PenaResiduaModel lPenaResidua = lCtrlPenRes
				.ExRicercaPenaResiduaUltimaNonValidataSospesa(lIdFascicolo);
		setRequestAttribute("penaresidua", lPenaResidua);

		String lErrore = null;
		String lAzioneChiamante = null;
		if (lPenaResidua == null) {
			lErrore = "Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?";
			lAzioneChiamante = "siap.siep.calcolopena.action.ActLoadCalcoloPena";
		}

		if (lErrore != null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, lErrore);
			lRedirigi.setAction(lAzioneChiamante + "&" + ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "="
					+ getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// ** NOTA : mi aspetto un solo record di sopensione per pena residua
		ISospensione lCtrlSosp = SIEPLookupRemote.getSospensioneRemote();
		SospensioneModel lSospensione = lCtrlSosp
				.ExRicercaSospensioneByIdPenaResidua(lPenaResidua.getIdPenaResidua());

		setRequestAttribute("sospensione", lSospensione);

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaUltimaDecretoOrdinanzaSiepByDecretoOrdinanzaIdFascicolo(lIdFascicolo);

		setRequestAttribute("decretoordinanza", lDecOrd);
		String lFlagDec = (lDecOrd == null ? "N" : "S");
		setRequestAttribute("flagdecretoordinanza", lFlagDec);

		// TIPO REGISTRO ORDINANZA
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		// TIPO PROVVEDIMENTO
		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		if (lDecOrd != null) {
			lOption.setSelected(lDecOrd.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoUfficio(), "TDS");
		setRequestAttribute("tipoUfficio", "" + lOption);

		// MAGISTRATO COMPETENTE
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());
		if (lMagMod != null)
			setRequestAttribute("magistratocompetente", lMagMod);

		// evento già inserito
		EventoModel lEveRic = new EventoModel();

		lEveRic.setCodMotivo("0263");
		lEveRic.setCodTipoEvento("01");

		// Il Tipo Provvedimento dipende se detenuto già scarcerato.
		// Luigi 21-09-2006
		if (lDecOrd != null && lDecOrd.getFlagScarcerareScarcerato().equals("S")) {
			// Detenuto già scarcerato
			lEveRic.setCodTipoProvvedimento("12");
		} else {
			// Detenuto da scarcerare
			lEveRic.setCodTipoProvvedimento("09");
		}
		// lEveRic.setCodTipoProvvedimento("04");
		// I codici Motivo e Tipo Provvedimento vengono passati nella
		// request perchè serviranno nella fase di inserimento
		setRequestAttribute("CodMotivo", lEveRic.getCodMotivo());
		setRequestAttribute("CodTipoProvvedimento", lEveRic.getCodTipoProvvedimento());

		lEveRic.setFasSieIdFascicoloSiep(lFascMod.getIdFascicoloSiep());
		IEvento lCtrlEve = SICOLookupRemote.getEventoRemote();
		EventoModel lEve = new EventoModel();
		EventoNotificaModel lEveNot = new EventoNotificaModel();

		lEve = lCtrlEve.ExRicercaEventoNonRegistrato(lEveRic);
		if (lEve != null) {
			lEveNot = lCtrlEve.ExRicercaEventoNotificaByKey(lEve.getIdEvento());
			if (lEveNot != null) {
				setRequestAttribute("eventonotifica", lEveNot);

				IUfficio lUff = SICOLookupRemote.getUfficioRemote();

				IIstitutoDetenzione lCtrlIst = SIEPLookupRemote.getIstitutoDetenzioneRemote();
				NotificaModel[] lNotifiche = lEveNot.getNotifiche();
				for (int i = 0; i < lNotifiche.length; i++) {
					// magistratoSorv
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("C")) {

						UfficioModel lUffModMag = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffMagistrato", lUffModMag);

					}
					// TDS
					if (lNotifiche[i].getUffCodUfficio() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("E")) {

						UfficioModel lUffModTDS = lUff
								.getUfficioByKey(lNotifiche[i].getUffCodUfficio().toUpperCase());
						setRequestAttribute("uffTDS", lUffModTDS);

					}
					if (lNotifiche[i].getIstDetIdIstitutoDetenzione() != null
							&& lNotifiche[i].getCodTipoNotifica().equals("N")) {

						IstitutoDetenzioneModel lModIst = lCtrlIst.ExRicercaIstitutoDetenzioneByKey(
								lNotifiche[i].getIstDetIdIstitutoDetenzione().toUpperCase());
						setRequestAttribute("Istituto", lModIst);

					}

				}
			}

		}
		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.sospensione.action.ActLoadInserisciSospensioneArt47");

		return PG_LOAD_INSERISCI_SOSPENSIONE_ART47; // restituisce la jsp di VIEW
	}
}
