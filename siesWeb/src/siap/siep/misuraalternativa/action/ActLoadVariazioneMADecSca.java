package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.camponota.controller.ICampoNota;
import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.SIEPException;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import siap.siep.verbale.controller.IVerbale;
import siap.siep.verbale.model.VerbaleModel;

/**
 * <p>
 * Title: ActLoadVariazioneMAAmmProvDetDom
 * </p>
 * <p>
 * Description: Classe Action per la load Variazione data di MisuraAlternativa
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

public class ActLoadVariazioneMADecSca extends ActAmmissioneProvvisoria

{
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// Ricerca Misura Alternativa
		String lRitorno = this.getAmmissioneProvvisoria();
		if (!lRitorno.equals(""))
			return lRitorno;

		MisuraAlternativaModel lMisAlModAMM = new MisuraAlternativaModel();

		if (!this.isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModAMM);
			}
		}

		// Ricerca Evento scrittoin fase di variazione
		EventoModel lEveMod = new EventoModel();
		BigDecimal lIdEvento = new BigDecimal(this.getRequestStringParameter("idevento"));

		// BigDecimal lIdEvento = new
		// BigDecimal(this.getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO));
		IEvento lCtrlEve1 = SICOLookupRemote.getEventoRemote();
		lEveMod = lCtrlEve1.ExRicercaEventoByKey(lIdEvento);
		setRequestAttribute("evento", lEveMod);

		// Campo Nota
		// campo note
		ICampoNota lCtrlCam = SICOLookupRemote.getCampoNotaRemote();
		CampoNotaModel lCamMod = lCtrlCam.ExRicercaCampoNotaByIdEvento(lEveMod.getIdEvento());
		setRequestAttribute("camponota", lCamMod);

		// Ricerca Verbale :
		// Il Verbale potrebbe anche non essere presente nel procedimento; comunque il Model lVerMod
		// viene passato alla Form, che a seconda se il lModel è pieno o vuoto, mostra o meno alcuni campi
		//
		VerbaleModel lVerMod = new VerbaleModel();
		BigDecimal lKeyVer = null;

		if (!this.isRequestAttributeNullObj("idverbale") && getRequestAttribute("idverbale") != null) {
			lKeyVer = new BigDecimal(this.getRequestStringParameter("idverbale"));
			IVerbale lCtrlVer = SIEPLookupRemote.getVerbaleRemote();
			lVerMod = lCtrlVer.ExRicercaVerbaleByKey(lKeyVer);
		} else {
			lVerMod = null;
		}

		setRequestAttribute("verbale", lVerMod);

		// Ufficio
		UfficioModel lUffEmiMod = new UfficioModel();
		IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
		lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModAMM.getChiaveUfficioFascicoloSius());
		setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
		} catch (SIEPException e) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					e.getMessage() + " Impossibile eseguire l'Ordine di Esecuzione.");
			lRedirigi.setAction("siap.siep.avvocato.action.ActLoadInserisciAvvocato&"
					+ ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" + getClass().getName());
			setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

			return IWebConstants.PG_MESSAGE;
		}

		// Autorità esterna E
		Option lOptionAutoritaE = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("codiceAutoritaE", "" + lOptionAutoritaE);

		// setto il campo codice motivo
		Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvDetDom());
		setRequestAttribute("motivoProvv", "" + lOption);

		// Riempimento ComboBoX
		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);

		setRequestAttribute("tipomisura", "DETENZIONE");
		setRequestAttribute("avvocati", lAvvocati);

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_VARIAZIONE_MA_DEC_SCA;

	}
}
