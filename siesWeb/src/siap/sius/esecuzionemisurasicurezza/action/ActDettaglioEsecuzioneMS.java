package siap.sius.esecuzionemisurasicurezza.action;

/**
 * <p>Title: ActDettaglioEsecuzioneMS</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: Bull</p>
 * @author unascribed
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.DettaglioFascicoloModel;
import siap.siep.util.SIEPLookupRemote;
import siap.sius.SIUSException;
import siap.sius.esecuzionemisurasicurezza.controller.IEsecuzioneMS;
import siap.sius.esecuzionemisurasicurezza.model.EMSFascGPModel;
import siap.sius.esecuzionemisurasicurezza.model.EsecuzioneMisuraSicurezzaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActDettaglioEsecuzioneMS extends ActionSiap implements ICostantiEsecuzioneMS {

	public String processRequest() throws Exception {

		BigDecimal lIdEsecuzioneMS = null;
		BigDecimal lIdSoggetto = null;
		BigDecimal lIdFascicoloSius = null;

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS))
			lIdEsecuzioneMS = getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS);
		// lIdEsecuzioneMS = new BigDecimal (getRequestStringParameter(
		// ICostantiEsecuzioneMS.CAMPO_ID_ESECUZIONE_MS));
		else if (!isRequestAttributeNullObj("esecuzione")) {
			lIdEsecuzioneMS = (((EMSFascGPModel) getRequestAttribute("esecuzione")).getEsecuzioneMSModel()
					.getIdEsecuzioneMisuraSicurezza());
			lIdSoggetto = (((EMSFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getSoggetto().getIdSoggetto());

			lIdFascicoloSius = (((EMSFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getIdFascicoloSius());
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Esecuzione Misura Sicurezza non selezionata");

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_SOGGETTO))
			lIdSoggetto = getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_SOGGETTO);

		if (lIdSoggetto == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Soggetto non individuato");
		else {
			SoggettoModel lSogMod = new SoggettoModel();
			ISoggetto lCtrlSog = SICOLookupRemote.getSoggettoRemote();
			lSogMod = lCtrlSog.ExRicercaSoggettoByKey(lIdSoggetto);
			setSessionAttribute("soggetto", lSogMod);
		}

		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS))
			lIdFascicoloSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);

		if (lIdFascicoloSius == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Procedimento Sius di Iscrizione M.S. non individuato");
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, lIdFascicoloSius);

		IEsecuzioneMS lEMSCtrl = SIUSLookupRemote.getEsecuzioneMSRemote();

		EsecuzioneMisuraSicurezzaModel lEMSModel = lEMSCtrl
				.ExRicercaEsecuzioneMisuraSicurezzaByKey(lIdEsecuzioneMS);

		if (lEMSModel == null || lEMSModel.getIdEsecuzioneMisuraSicurezza() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA SICUREZZA Assente!");
		else
			setRequestAttribute("misuraSicurezza", lEMSModel);

		// 30/06/2009 Gestione Parametro Codice Ufficio.
		String lCodUfficio = this.getCodUfficioUtenteConnesso();
		if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_CHIAVE_UFFICIO))
			lCodUfficio = getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_CHIAVE_UFFICIO);

		setRequestAttribute("lCodUfficioFascicolo", lCodUfficio);

		Vector[] lVectMisureECorrelati = lEMSCtrl.ExRicercaDettaglioEMSeCorrelati(lIdEsecuzioneMS,
				lIdSoggetto, lCodUfficio);

		if (!lEMSModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl
					.ExRicercaFascicoloByGenProc(lEMSModel.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		setRequestAttribute("misure", lVectMisureECorrelati[0]);
		setRequestAttribute("correlati", lVectMisureECorrelati[1]);

		// Leggo anche il fascicolo SIEP se presente.
		BigDecimal lIdFascicoloSiep = null;
		if (!isRequestParameterNullObj(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP)
				&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP).trim()
						.length() > 0
				&& getRequestStringParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP).trim()
						.compareToIgnoreCase("null") != 0)
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP);
		if (lIdFascicoloSiep != null) {
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneMS.CAMPO_ID_FASCICOLO_SIEP);
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}