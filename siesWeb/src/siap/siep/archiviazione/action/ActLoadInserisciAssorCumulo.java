package siap.siep.archiviazione.action;

import java.math.BigDecimal;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.magistratocompetente.controller.IMagistratoCompetente;
import siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.annotazioneesitotrasmissione.action.ICostantiAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.controller.IAnnotazioneEsitoTrasmissione;
import siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

/**
 * <p>
 * Title: ActLoadInserisciAssorCumulo
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
 * @author unascribed
 * @version 1.0
 */
public class ActLoadInserisciAssorCumulo extends ActionSiap implements ICostantiArchiviazione {

	public String processRequest() throws Exception {

		// avviso di pagina in costruzione
		// return ISIAPCostantiWeb.PG_UNDER_CONSTRUCTION;

		// Controllo Presenza del Fascicolo in Sessione
		if (this.isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		this.isFascicoloSiepDiCompetenza();

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

		// se Provengo da Annotazione Esito per Assorbimento in Cumulo
		BigDecimal aIdEsiTra = null;
		String lStessoUfficio = "";
		AnnotazioneEsitoTrasmissioneModel lAnnEsiTrasModel = null;
		if (!isRequestParameterNullObj(ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ID_ESITO_TRASMISSIONE)) {
			IAnnotazioneEsitoTrasmissione CtrlAnn = SIEPLookupRemote.getAnnotazioneEsitoTrasmissioneRemote();
			aIdEsiTra = getRequestBigDecimalParameter(
					ICostantiAnnotazioneEsitoTrasmissione.CAMPO_ID_ESITO_TRASMISSIONE);
			lAnnEsiTrasModel = (AnnotazioneEsitoTrasmissioneModel) CtrlAnn.ExRicercaAnnotazioneEsitoTrasmissioneById(aIdEsiTra);
			setRequestAttribute("EsitoTrasmissione", lAnnEsiTrasModel);

			if (lAnnEsiTrasModel != null && lAnnEsiTrasModel.getIdEsitoTrasmissione() != null)
				if (lAnnEsiTrasModel.getChiaveUfficio() != null)
					if (!lAnnEsiTrasModel.getChiaveUfficio().equals(lFascMod.getChiaveUfficio()))
						lStessoUfficio = "NO";
					else if (lAnnEsiTrasModel.getChiaveUfficio().equals(lFascMod.getChiaveUfficio()))
						lStessoUfficio = "SI";
		}

		setRequestAttribute("StessoUfficio", lStessoUfficio);

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

		/*
		 * commentato a causa dei fascicoli migrati cumulati che non danno il parametro FlagCumulato in
		 * FascicoloSiep -- Dario -- Luciana 24-05-2005 if(!"S".equals(lFascMod.getFlagCumulato())) { throw
		 * new SIEPException(SIEPException.USER_MESSAGE, "Il Procedimento N." + lFascMod.getChiaveAnno() + "/"
		 * + lFascMod.getChiaveProgr() + " risulta non cumulato. Impossibile procedere!");
		 *
		 * }
		 */

		this.isEventoNonValidato();

		// Posizione Giuridica
		IPosizioneGiuridica lPosCtrl = SIEPLookupRemote.getPosizioneGiuridicaRemote();

		PosizioneGiuridicaLuogoDetenzioneAltraCausaModel lPosLuoAltra = lPosCtrl
				.ExRicercaPosizioneGiuridicaLuogoDetenzioneAltraCausaCorrentiByIdFascicolo(
						lFascMod.getIdFascicoloSiep());

		if (lPosLuoAltra == null || lPosLuoAltra.getPosizioneGiuridica() == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Al Procedimento N." + lFascMod.getChiaveAnno()
					+ "/" + lFascMod.getChiaveProgr() + " non è stata associata una Posizione Giuridica.");
			lRedirigi.setAction("siap.siep.posizione.action.ActLoadInserisciPosizioneGiuridica&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("posizioneluogoaltra", lPosLuoAltra);

		// Pena Complessiva
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

		// Pena Residua
		IPenaResidua lCtrlPenRes = SIEPLookupRemote.getPenaResiduaRemote();

		PenaResiduaModel lPenaResidua = lCtrlPenRes.ExRicercaPenaResiduaCorrenteByFascicoloSiep(lIdFascicolo);
		if (lPenaResidua == null) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Pena Residua da Espiare Inesistente. Eseguire Calcolo della pena?");
			lRedirigi.setAction("siap.siep.calcolopena.action.ActLoadCalcoloPena&"
					+ ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("penaresidua", lPenaResidua);

		// ricerca magistrato competente
		IMagistratoCompetente lMagComp = SICOLookupRemote.getMagistratoCompetenteRemote();
		MagistratoCompetenteMagistratoModel lMagMod = lMagComp
				.ExRicercaMagistratoCompetenteByFascicolo(lFascMod.getIdFascicoloSiep());

		setRequestAttribute("magistratocompetente", lMagMod);

		if (lAnnEsiTrasModel != null) {
			UfficioModel lUffMod = getUfficioByCodUfficio(lAnnEsiTrasModel.getChiaveUfficio());
			Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(),
					lUffMod.getCodTipoUfficio());
			setRequestAttribute("autoritaCumulo", "" + lOption);

			setRequestAttribute("sedeCumulo", lUffMod.getDescrComune());
		} else {
			if (lFascMod.getCodUfficioUnione() != null && !lFascMod.getCodUfficioUnione().equals("-")) {
				UfficioModel lUffMod = getUfficioByCodUfficio(lFascMod.getCodUfficioUnione());
				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo(),
						lUffMod.getCodTipoUfficio());
				setRequestAttribute("autoritaCumulo", "" + lOption);

				setRequestAttribute("sedeCumulo", lUffMod.getDescrComune());
			} else {
				Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaCumulo());
				setRequestAttribute("autoritaCumulo", "" + lOption);
			}
		}

		Option lOptionAutorita = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutorita", "" + lOptionAutorita);

		Option lOptionUffRecCrediti = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// MEV_66: aggiunti 4 uffici = "CAPSM", "DIBM", "GIPM", "GUPM"
		lOptionUffRecCrediti
				.setFilter(new String[] { "-", "DIB", "CAP", "TRIBSD", "CAPSM", "DIBM", "GIPM", "GUPM" });
		setRequestAttribute("uffrecrediti", "" + lOptionUffRecCrediti);

		// MEV_66: aggiunta combo di scelta tds + tdsm
		Option lOptionUffSorv = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM());
		lOptionUffSorv.setFilter(new String[] { "-", "TDS", "TDSM" });
		setRequestAttribute("ufficioTdS", "" + lOptionUffSorv);

		// MEV_66: aggiunta combo di scelta uds + udsm
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM());
		lOptionUffSIUS.setFilter(new String[] { "-", "UDS", "UDSM" });
		setRequestAttribute("ufficioMdS", "" + lOptionUffSIUS);

		return PG_LOAD_INSERISCI_ASSOR_CUMULO;
	}

}