package siap.sius.esecuzionemisuraalternativa.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActDettaglioEsecuzioneMAbyFascicolo
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActDettaglioEsecuzioneMAbyFascicolo extends ActionSiap implements ICostantiEsecuzioneMA {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIUS))
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIUS));
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Misura Alternativa non selezionato");

		if (!isRequestParameterNullObj("hIdSoggetto")) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter("hIdSoggetto"));
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

		// if (lVect.isEmpty())
		// throw new SIUSException( SIUSException.USER_MESSAGE,
		// "Esecuzione Misura Alternativa priva di procedimenti" );

		if (!lEMAModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl.ExRicercaFascicoloByGenProc(lEMAModel
					.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		if (!lVect.isEmpty())
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

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MA;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}