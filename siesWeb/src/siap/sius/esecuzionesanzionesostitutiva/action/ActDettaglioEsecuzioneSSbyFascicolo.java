package siap.sius.esecuzionesanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;

/**
 * <p>
 * Title: ActDettaglioEsecuzioneSSbyFascicolo
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
public class ActDettaglioEsecuzioneSSbyFascicolo extends ActionSiap implements ICostantiEsecuzioneSS {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		BigDecimal lIdFascicoloSius = null;
		BigDecimal lIdSoggetto = null;

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIUS))
			lIdFascicoloSius = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIUS));
		else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Fascicolo di Esecuzione Misura Alternativa non selezionato");

		if (!isRequestParameterNullObj("hIdSoggetto")) {
			lIdSoggetto = new BigDecimal(getRequestStringParameter("hIdSoggetto"));
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Soggetto titolare di Esecuzione Misura Alternativa non individuato");

		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();

		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByIdFascicolo(lIdFascicoloSius);

		if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost().equals(null))
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
		else
			setRequestAttribute("sanzioneSostitutiva", lESSModel);

		Vector lVect = lESSCtrl.ExRicercaDettaglioEsecuzioneSS(lESSModel.getIdEsecuzioneSanzioneSost(),
				lIdSoggetto, this.getCodUfficioUtenteConnesso());

		// if (lVect.isEmpty())
		// throw new SIUSException( SIUSException.USER_MESSAGE,
		// "Esecuzione Sanzione Sostitutiva priva di procedimenti" );

		if (!lESSModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl.ExRicercaFascicoloByGenProc(lESSModel
					.getGenPridGeneraleProcedimento());
			setRequestAttribute("sanzioneUno", lFasGPModel);
		}

		if (!lVect.isEmpty())
			setRequestAttribute("sanzioni", lVect);

		// Lettura del fascicolo SIEP se il parametro passato è != null.
		BigDecimal lIdFascicoloSiep = null;
		DettaglioFascicoloModel lDettaglio = null;
		if (!(isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP))
				&& (getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)).length() > 4) {
			lIdFascicoloSiep = new BigDecimal(
					getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP));

			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);
		}

		if (lDettaglio != null)
			setRequestAttribute("dettaglioFascSiep", lDettaglio);

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_SS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}