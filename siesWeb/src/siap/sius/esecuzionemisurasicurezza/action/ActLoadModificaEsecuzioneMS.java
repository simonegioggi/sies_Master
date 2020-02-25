package siap.sius.esecuzionemisurasicurezza.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.sanzionesostitutiva.controller.IPeriodoAltraSanzione;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;

/**
 * <p>
 * Title: Modifica Esecuzione Misura Sicurezza
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActLoadModificaEsecuzioneMS extends ActionSiap implements ICostantiEsecuzioneMS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione del Bottone di ritorno.
		this.gestioneRitorno();

		// Modifica Procedimento di esecuzione MS.
		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;
		if (isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIUS))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Misura Sicurezza non selezionato");
		else {
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIUS));

			// Riempio il model.
			FascicoloGPModel lFasGPMod = new FascicoloGPModel();
			lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicoloSius);

			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloSius);

			setRequestAttribute("fascicoloEsecuzione", lFasGPMod);
			// Metto in sessione il fascicolo SIUS.
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
		}

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO)) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Soggetto titolare di Esecuzione Misura Sicurezza non individuato");

		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();
		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFascicoloSius);

		if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! MISURA SICUREZZA SOSTITUTIVA Assente!");
		else
			setRequestAttribute("misuraSicurezza", lEMSModel);

		// se esiste un periodo altra misura non permetto la modifica dei quantum
		// nella maschera successiva - da modificare dopo la creazione del controller AltraMisura
		IPeriodoAltraSanzione lCtrllst = SIUSLookupRemote.getPeriodoAltraSanzioneRemote();
		List lListaSanzioniSius = lCtrllst.ExRicercaSanzioneSostitutivaByIdFascicolo(lIdFascicoloSius);
		if (lListaSanzioniSius != null)
			setRequestAttribute("listaSanzioniSius", lListaSanzioniSius);

		Vector lVect = lEMSCtrl.ExRicercaDettaglioEsecuzioneMS(lEMSModel.getIdEsecuzioneMisuraSicurezza(),
				lIdSoggetto, this.getCodUfficioUtenteConnesso());

		if (!lEMSModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl
					.ExRicercaFascicoloByGenProc(lEMSModel.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		setRequestAttribute("misure", lVect);

		// Lettura del fascicolo SIEP se il parametro passato è != null.
		BigDecimal lIdFascicoloSiep = null;
		DettaglioFascicoloModel lDettaglio = null;
		if (!(isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP))
				&& (getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)).length() > 4) {
			lIdFascicoloSiep = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP));

			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);
		}

		if (lDettaglio != null)
			setRequestAttribute("dettaglioFascSiep", lDettaglio);

		// Imposta Tipo Atto.
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAtto());
		setRequestAttribute("tipoAtto", "" + lOption);

		// Imposta Mittente Atto.
		lOption = new Option(DecodificheManager.getInstance().getMittenteAtto(), 36);
		setRequestAttribute("mittenteAtto", "" + lOption);

		// Imposta Contenuto.
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		// MERGE v10 COLLAUDO: aggiunta casistica per i minorenni
		if ("UDSM".equals(strCodTipoUfficio))
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDSM(), 75);
		else
			lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimentoUDS(), 75);
		setRequestAttribute("contenuto", "" + lOption);

		// Realizzazione filtro sui contenuti per "S09"
		FascicoloUtils lFascicoloUtils = new FascicoloUtils();
		// MEV63: aggiunto parametro di passaggio per distinzione ufficio minori
		String[] lFilter = lFascicoloUtils.filtraContenutiMS(strCodTipoUfficio);
		lOption.setFilter(lFilter);
		setRequestAttribute("contenutoEsecuzione", "" + lOption);

		// Imposta la Collection Contenuto.
		Collection lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
		setRequestAttribute("collContenuto", lCol);

		return PG_LOAD_MODIFICA_ESECUZIONE_MS; // restituisce la jsp di VIEW
	}

}