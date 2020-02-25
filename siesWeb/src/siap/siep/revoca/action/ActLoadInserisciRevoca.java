package siap.siep.revoca.action;

/**
 * <p>Title: ActLoadInserisciRevoca</p>
 * <p>Description: Azione Load della Revoca Sospensione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import siap.siep.SIEPException;
import siap.siep.decretoordinanza.controller.IDecretoOrdinanzaSiep;
import siap.siep.decretoordinanza.model.DecretoOrdinanzaSiepModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaModel;
import siap.siep.penaresidua.controller.IPenaResidua;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.posizione.controller.IPosizioneGiuridica;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import siap.siep.util.SIEPLookupRemote;
import siap.web.ISIAPCostantiWeb;

public class ActLoadInserisciRevoca extends ActionSiap implements ICostantiRevoca {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

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

		this.isEventoNonValidato();

		/******************************* Posizione Giuridica **************************/
		IPosizioneGiuridica lCtrlPosGiu = SIEPLookupRemote.getPosizioneGiuridicaRemote();
		PosizioneGiuridicaModel lPosizione = lCtrlPosGiu
				.ExRicercaPosizioneGiuridicaCorrenteByIdFascicolo(lIdFascicolo);

		if (lPosizione == null)
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"Inserire prima la Posizione Giuridica. Impossibile eseguire la richiesta.");

		setRequestAttribute("posizione", lPosizione);
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
		// PenaResiduaModel lPenaResiduaSospesaValidata =
		// lCtrlPenRes.ExRicercaPenaResiduaUltimaValidataSospesa(lIdFascicolo);
		PenaResiduaModel lPenaResiduaSospesaValidata = lCtrlPenRes
				.ExRicercaPenaResiduaUltimaValidata(lIdFascicolo);

		if (lPenaResiduaSospesaValidata == null || lPenaResiduaSospesaValidata.getFlagPenaSospesa() == null
				|| lPenaResiduaSospesaValidata.getFlagPenaSospesa().equals("N"))
			throw new SIEPException(SIEPException.USER_MESSAGE,
					"La pena non è sospesa. Impossibile eseguire la richiesta.");

		setRequestAttribute("penaresidua", lPenaResiduaSospesaValidata);

		/****************************** Evento ****************************************/
		// IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
		// EventoModel lEventoModel =

		// ** Ricerca su DECRETO_ORDINANZA_SIEP **
		IDecretoOrdinanzaSiep lCtrlDecOrd = SIEPLookupRemote.getDecretoOrdinanzaSiepRemote();
		DecretoOrdinanzaSiepModel lDecOrd = lCtrlDecOrd
				.ExRicercaUltimaDecretoOrdinanzaSiepByDecretoOrdinanzaIdFascicolo(lIdFascicolo);

		setRequestAttribute("decretoordinanza", lDecOrd);
		String lFlagDec = (lDecOrd == null ? "N" : "S");
		setRequestAttribute("flagdecretoordinanza", lFlagDec);

		// TIPO REGISTRO ORDINANZA
		Collection lCollTipoReg = DecodificheManager.getInstance().getTipoRegistroOrdinanza();
		// 20190419 [SG]: gestione come le sospensioni GE x MEV_66
		Iterator iter = lCollTipoReg.iterator();
		lCollTipoReg = new ArrayList();
		while (iter.hasNext()) {
			DecodificheModel lDecMod = (DecodificheModel) iter.next();
			if (!"0001".equals(lDecMod.getCodiceAlternativo())) // SIUS
				lCollTipoReg.add(lDecMod);
		}
		setRequestAttribute("tiporegistroordinanza", lCollTipoReg);

		// TIPO PROVVEDIMENTO
		Option lOption = new Option(DecodificheManager.getInstance().getTipoProvvedimenti());
		lOption.setFilter(new String[] { "02", "03" }); // solo DECRETO o ORDINANZA
		if (lDecOrd != null) {
			lOption.setSelected(lDecOrd.getCodTipoProvvedimento());
		}
		setRequestAttribute("tipoprovvedimento", "" + lOption);

		// AUTORITA' EMITTENTE (TIPO_UFFICIO_SOSP)
		ArrayList lListAutoritaSospensione = (ArrayList) DecodificheManager.getInstance()
				.getListaAutoritaSospensione();
		setRequestAttribute("autoritaemittente", lListAutoritaSospensione);

		// CONTENUTO DECISIONE
		ArrayList lContenutoDecisione = (ArrayList) DecodificheManager.getInstance().getListaOggettiRevoca();
		setRequestAttribute("contenutodecisione", lContenutoDecisione);

		// OGGETTO DECISIONE
		ArrayList lMotiviProvvedimento = (ArrayList) DecodificheManager.getInstance()
				.getListaMotiviProvvedimentoRevoca();
		setRequestAttribute("oggettodecisione", lMotiviProvvedimento);

		// TIPOLOGIA DECISIONE
		ArrayList lEsitiTenore = (ArrayList) DecodificheManager.getInstance().getListaEsitiTenoreRevoca();
		setRequestAttribute("tipologiadecisione", lEsitiTenore);

		setRequestAttribute(ISIAPCostantiWeb.CAMPO_AZIONE_CHIAMANTE,
				"siap.siep.revoca.action.ActLoadInserisciRevoca");

		return PG_LOAD_INSERISCI_REVOCA; // restituisce la jsp di VIEW
	}

}