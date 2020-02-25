package siap.sius.esecuzionemisurasicurezza.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActDettaglioEsecuzioneMSbyFascicolo
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
public class ActDettaglioEsecuzioneMSbyFascicolo extends ActionSiap implements ICostantiEsecuzioneMS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIUS))
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIUS));
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Misura Sicurezza non selezionato");

		if (!isRequestParameterNullObj("hIdSoggetto")) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter("hIdSoggetto"));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Soggetto titolare di Esecuzione Misura Sicurezza non individuato");

		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();

		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByIdFascicolo(lIdFascicoloSius);

		if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");
		else
			setRequestAttribute("misuraSicurezza", lEMSModel);

		Vector lVect = lEMSCtrl.ExRicercaDettaglioEsecuzioneMS(lEMSModel.getIdEsecuzioneMisuraSicurezza(),
				lIdSoggetto, this.getCodUfficioUtenteConnesso());

		// if (lVect.isEmpty())
		// throw new SIUSException( SIUSException.USER_MESSAGE,
		// "Esecuzione Misura Sicurezza priva di procedimenti" );

		if (!lEMSModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl.ExRicercaFascicoloByGenProc(lEMSModel
					.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		if (!lVect.isEmpty())
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

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}