package siap.siep.misuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;
import siap.sico.cssa.controller.ICSSA;
import siap.sico.cssa.model.CSSAModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.controller.IEventoSimeone;
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
 * Title: ActLoadInserisciMAAmmProvAffi
 * Description: Classe Action per la load inserisci di MisuraAlternativa
 *
 * Questa Action viene richiamata in due casi: - in fase di registrazione dell'Ammissione provvisoria alla
 * misura - dopo la registrazione del Verbale di sottoscrizione agli obblighi in fase di emissione dell'OS
 * (09)
 * Nel primo caso la chiamata avviene dalla 'griglia' delle funzioni
 *
 * @version 1.0
 */

public class ActLoadInserisciMAAmmProvAffi extends ActAmmissioneProvvisoria {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// tutti i controlli e la maggior parte delle request si trovano nel padre
		String lRitorno = this.getAmmissioneProvvisoria();
		if (!lRitorno.equals(""))
			return lRitorno;

		// AMBROSINO - 15/10/2010

		MisuraAlternativaModel lMisAlModAMM = new MisuraAlternativaModel();

		if (!this.isRequestParameterNullObj(CAMPO_ID_MISURA_ALTERNATIVA)) {
			BigDecimal lIdMisuraAlternativa = this.getRequestBigDecimalParameter(CAMPO_ID_MISURA_ALTERNATIVA);
			if (lIdMisuraAlternativa != null) {
				IMisuraAlternativa lMisAltCtrl = SICOLookupRemote.getMisuraAlternativaRemote();
				lMisAlModAMM = lMisAltCtrl.ExRicercaMisuraAlternativaByKey(lIdMisuraAlternativa);
				setRequestAttribute("misuraalternativa", lMisAlModAMM);

				// Data Sottoscrizione Verbale Obblighi

				EventoModel lEveVer = new EventoModel();
				IEventoSimeone lCtrlEven = SICOLookupRemote.getEventoSimeoneRemote();
				lEveVer = lCtrlEven.ExRicercaEventoByEveIdEventoTipoProvCodMotivo(
						lMisAlModAMM.getEveIdEvento(), "07", "18", "0314");

				IVerbale lCtrlVe = SIEPLookupRemote.getVerbaleRemote();
				VerbaleModel lVerbMod = lCtrlVe.ExRicercaVerbaleObblighiByIdEvento(lEveVer.getIdEvento());
				setRequestAttribute("verbale", lVerbMod);

				// Recupero il CSSA
				if (lVerbMod.getCssIdCssa() != null) {
					ICSSA lCSSACtl = SICOLookupRemote.getCSSARemote();
					CSSAModel lCSSAModel = lCSSACtl.getCSSAByKey(lVerbMod.getCssIdCssa());
					setRequestAttribute("daticssa", lCSSAModel);
				}

				UfficioModel lUffEmiMod = new UfficioModel();
				IUfficio lCtrlUffEmi = SICOLookupRemote.getUfficioRemote();
				lUffEmiMod = lCtrlUffEmi.getUfficioByKey(lMisAlModAMM.getChiaveUfficioFascicoloSius());

				setRequestAttribute("sedeUfficioEmittente", lUffEmiMod);
			}
		}

		// Controllo esistenza almeno un avvocato per fascicolo.
		IAvvocato lAvv = SIEPLookupRemote.getAvvocatoRemote();
		Vector lAvvocati = null;
		FascicoloSiepModel lFascMod = (FascicoloSiepModel) getSessionAttribute("fascicolo");

		try {
			lAvvocati = lAvv.ExRicercaAvvocatiByFascicolo(lFascMod.getIdFascicoloSiep());
			setRequestAttribute("avvocati", lAvvocati);
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

		// Riempimento ComboBoX
		Option lOptionAvv = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaAvv", "" + lOptionAvv);
		// END AMBROSINO

		// new d.f. DL 146/2013
		Option lOptionUffSIUS = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
		setRequestAttribute("comboTipoUfficioSIUS", "" + lOptionUffSIUS);

		// new d.f. DL 146/2013
		Vector<DecodificheModel> lTipoProvvSorv = new Vector<>();
		lTipoProvvSorv.add(new DecodificheModel("-", "-", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("02", "Decreto", "", "", "", "", "", "", ""));
		lTipoProvvSorv.add(new DecodificheModel("03", "Ordinanza", "", "", "", "", "", "", ""));
		Option lOptionTipoProvvSorv = new Option(lTipoProvvSorv);
		lOptionTipoProvvSorv.setSelected("-");
		setRequestAttribute("comboTipoProvvSorv", "" + lOptionTipoProvvSorv);

		// setto il campo codice motivo
		// MEV_9
		// Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
		Option lOption = null;
		if (isUfficioMinorenni())
			lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffiPmm());
		else
			lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoAmmProvAffi());
		// MEV_9 - FINE

		setRequestAttribute("motivoProvv", "" + lOption);

		setRequestAttribute("tipomisura", "AFFIDAMENTO");

		// MEV 10 - filtro sui minorenni
		setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());

		return PG_LOAD_INSERISCI_MA_AMM_PROVVISORIA;
	}

}