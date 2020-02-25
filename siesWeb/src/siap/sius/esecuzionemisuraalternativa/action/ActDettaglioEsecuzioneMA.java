package siap.sius.esecuzionemisuraalternativa.action;

/**
 * <p>Title: ActDettaglioEsecuzioneMA</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
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
import siap.sius.esecuzionemisuraalternativa.controller.IEsecuzioneMA;
import siap.sius.esecuzionemisuraalternativa.model.EMAFascGPModel;
import siap.sius.esecuzionemisuraalternativa.model.EsecuzioneMisuraAlternativaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActDettaglioEsecuzioneMA extends ActionSiap implements ICostantiEsecuzioneMA {

	public String processRequest() throws Exception {

		BigDecimal lIdEsecuzioneMA = null;
		BigDecimal lIdSoggetto = null;
		BigDecimal lIdFascicoloSius = null; // STUB 07/05/2004

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA))
			lIdEsecuzioneMA = getRequestBigDecimalParameter(ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA);
		// lIdEsecuzioneMA = new BigDecimal (getRequestStringParameter(
		// ICostantiEsecuzioneMA.CAMPO_ID_ESECUZIONE_MA));
		else if (!isRequestAttributeNullObj("esecuzione")) {
			lIdEsecuzioneMA = (((EMAFascGPModel) getRequestAttribute("esecuzione")).getEsecuzioneMAModel()
					.getIdEsecuzioneMisuraAlternati());
			lIdSoggetto = (((EMAFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getSoggetto().getIdSoggetto());
			// STUB 07/05/2004.
			lIdFascicoloSius = (((EMAFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getIdFascicoloSius());
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Esecuzione Misura Alternativa non selezionata");

		if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_SOGGETTO))
			lIdSoggetto = getRequestBigDecimalParameter(ICostantiEsecuzioneMA.CAMPO_ID_SOGGETTO);

		if (lIdSoggetto == null)
			throw new SIUSException(SIUSException.USER_MESSAGE, "Soggetto non individuato");
		else // STUB 10/04/2004
		{
			SoggettoModel lSogMod = new SoggettoModel();
			ISoggetto lCtrlSog = SICOLookupRemote.getSoggettoRemote();
			lSogMod = lCtrlSog.ExRicercaSoggettoByKey(lIdSoggetto);
			setSessionAttribute("soggetto", lSogMod);
		}

		// STUB 07/05/2004.
		if (!isRequestParameterNullObj(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS))
			lIdFascicoloSius = getRequestBigDecimalParameter(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS);

		if (lIdFascicoloSius == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Procedimento Sius di Iscrizione M.A. non individuato");
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, lIdFascicoloSius);

		IEsecuzioneMA lEMACtrl = SIUSLookupRemote.getEsecuzioneMARemote();

		EsecuzioneMisuraAlternativaModel lEMAModel = lEMACtrl
				.ExRicercaEsecuzioneMisuraAlternativaByKey(lIdEsecuzioneMA);

		if (lEMAModel == null || lEMAModel.getIdEsecuzioneMisuraAlternati() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE MISURA ALTERNATIVA Assente!");
		else
			setRequestAttribute("misuraAlternativa", lEMAModel);

		// 30/06/2009 Gestione Parametro Codice Ufficio.
		String lCodUfficio = this.getCodUfficioUtenteConnesso();
		if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_CHIAVE_UFFICIO))
			lCodUfficio = getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_CHIAVE_UFFICIO);

		setRequestAttribute("lCodUfficioFascicolo", lCodUfficio);

		// STUB 02/11/2005
		// Vector lVect = lEMACtrl.ExRicercaDettaglioEsecuzioneMA(lIdEsecuzioneMA, lIdSoggetto,
		// this.getCodUfficioUtenteConnesso() );
		Vector[] lVectMisureECorrelati = lEMACtrl.ExRicercaDettaglioEMAeCorrelati(lIdEsecuzioneMA,
				lIdSoggetto, lCodUfficio);

		if (!lEMAModel.getGenPridGeneraleProcedimento().equals(null)) {
			IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
			FascicoloGPModel lFasGPModel = lFasCtrl
					.ExRicercaFascicoloByGenProc(lEMAModel.getGenPridGeneraleProcedimento());
			setRequestAttribute("misuraUno", lFasGPModel);
		}

		// STUB 02/11/2005
		// setRequestAttribute("misure", lVect);
		setRequestAttribute("misure", lVectMisureECorrelati[0]);
		setRequestAttribute("correlati", lVectMisureECorrelati[1]);

		// Leggo anche il fascicolo SIEP se presente.
		BigDecimal lIdFascicoloSiep = null;
		if (!isRequestParameterNullObj(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP)
				&& getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP).trim()
						.length() > 0
				&& getRequestStringParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP).trim()
						.compareToIgnoreCase("null") != 0)
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP);
		if (lIdFascicoloSiep != null) {
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneMA.CAMPO_ID_FASCICOLO_SIEP);
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_MA;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}