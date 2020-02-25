package siap.sius.esecuzionemisuraalternativa.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.fascicolo.util.FascicoloUtils;
import siap.sius.util.SIUSLookupRemote;
import f3b.web.html.Option;
/**
 * <p>Title: Modifica Esecuzione Misura Alternativa</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class ActLoadModificaEsecuzioneMA extends ActionSiap implements ICostantiEsecuzioneMA {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// Gestione del Bottone di ritorno.
		this.gestioneRitorno();

		// Modifica Procedimento di esecuzione MA.
		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;
		if (isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIUS))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Misura Alternativa non selezionato");
		else {
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIUS));

			// Riempio il model.
			FascicoloGPModel lFasGPMod = new FascicoloGPModel();
			lFasGPMod.getFascicoloSiusModel().setIdFascicoloSius(lIdFascicoloSius);

			IFascicoloSius lCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			lFasGPMod = lCtrl.ExRicercaFascicoloByKey(lIdFascicoloSius);

			setRequestAttribute("fascicoloEsecuzione", lFasGPMod);
			// Metto in sessione il fascicolo SIUS.
			// setSessionAttribute("fascicoloEsecuzione", lFasGPMod);
			setSessionAttribute("fascicoloSiusGP", lFasGPMod);
		}

		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ID_SOGGETTO)) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Soggetto titolare di Esecuzione Misura Alternativa non individuato");

		IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();
		EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
				.ExRicercaEsecuzioneMisuraAlternativaByIdFascicolo(lIdFascicoloSius);

		if (lEMAModel == null || lEMAModel.getIdEsecuzioneMisuraAlternati().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA ALTERNATIVA Assente!");
		else
			setRequestAttribute("misuraAlternativa", lEMAModel);

		Vector lVect = lEMACtrl.ExRicercaDettaglioEsecuzioneMA(lEMAModel.getIdEsecuzioneMisuraAlternati(),
				lIdSoggetto, this.getCodUfficioUtenteConnesso());

		if (!lEMAModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl.ExRicercaFascicoloByGenProc(lEMAModel
					.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		setRequestAttribute("misure", lVect);

		// Lettura del fascicolo SIEP se il parametro passato è != null.
		BigDecimal lIdFascicoloSiep = null;
		DettaglioFascicoloModel lDettaglio = null;
		if (!(isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP))
				&& (getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)).length() > 4) {
			lIdFascicoloSiep = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP));

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

		// STUB 01/07/2004 Realizzazione filtro sui contenuti per "S22"
		FascicoloUtils lFascicoloUtils = new FascicoloUtils();
		// 20170320: aggiunto parametro di passaggio
		String[] lFilter = lFascicoloUtils.filtraContenutiMA(strCodTipoUfficio);
		lOption.setFilter(lFilter);
		setRequestAttribute("contenutoEsecuzione", "" + lOption);

		// Imposta la Collection Contenuto.
		Collection lCol = (DecodificheManager.getInstance()).getOggettoProcedimentoUDS();
		setRequestAttribute("collContenuto", lCol);

		return PG_LOAD_MODIFICA_ESECUZIONE_MA; // restituisce la jsp di VIEW
	}

}