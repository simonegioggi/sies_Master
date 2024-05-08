package siap.sius.esecuzionesanzionesostitutiva.action;

/**
 * <p>Title: ActDettaglioEsecuzioneSS</p>
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
import siap.sius.esecuzionesanzionesostitutiva.controller.IEsecuzioneSS;
import siap.sius.esecuzionesanzionesostitutiva.model.ESSFascGPModel;
import siap.sius.esecuzionesanzionesostitutiva.model.EsecuzioneSanzioneSostitutivaModel;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;
import siap.sius.fascicolo.controller.IFascicoloSius;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActDettaglioEsecuzioneSS extends ActionSiap implements ICostantiEsecuzioneSS {

	public String processRequest() throws Exception {

		BigDecimal lIdEsecuzioneSS = null;
		BigDecimal lIdSoggetto = null;
		BigDecimal lIdFascicoloSius = null;

		this.setLinkRitorno();

		if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS))
			lIdEsecuzioneSS = getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS);
		// lIdEsecuzioneSS = new BigDecimal (getRequestStringParameter(
		// ICostantiEsecuzioneSS.CAMPO_ID_ESECUZIONE_SS));
		else if (!isRequestAttributeNullObj("esecuzione")) {
			lIdEsecuzioneSS = (((ESSFascGPModel) getRequestAttribute("esecuzione")).getEsecuzioneSSModel()
					.getIdEsecuzioneSanzioneSost());
			lIdSoggetto = (((ESSFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getSoggetto().getIdSoggetto());

			lIdFascicoloSius = (((ESSFascGPModel) getRequestAttribute("esecuzione")).getFascicoloSiusModel()
					.getIdFascicoloSius());
		} else
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Esecuzione Sanzione Sostitutiva non selezionata");

		if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_SOGGETTO))
			lIdSoggetto = getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_ID_SOGGETTO);

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
					"Procedimento Sius di Iscrizione M.A. non individuato");
		setRequestAttribute(ICostantiFascicoloSius.CAMPO_ID_FASCICOLO_SIUS, lIdFascicoloSius);

		IEsecuzioneSS lESSCtrl = SIUSLookupRemote.getEsecuzioneSSRemote();

		EsecuzioneSanzioneSostitutivaModel lESSModel = lESSCtrl
				.ExRicercaEsecuzioneSanzioneSostitutivaByKey(lIdEsecuzioneSS);

		if (lESSModel == null || lESSModel.getIdEsecuzioneSanzioneSost() == null)
			throw new SIUSException(SIUSException.USER_MESSAGE,
					"Attenzione! ESECUZIONE SANZIONE SOSTITUTIVA Assente!");
		else
			setRequestAttribute("sanzioneSostitutiva", lESSModel);

		// 30/06/2009 Gestione Parametro Codice Ufficio.
		String lCodUfficio = this.getCodUfficioUtenteConnesso();
		if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_CHIAVE_UFFICIO))
			lCodUfficio = getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_CHIAVE_UFFICIO);

		setRequestAttribute("lCodUfficioFascicolo", lCodUfficio);

		String lCodContenuto = "";
	    if (!lESSModel.getGenPridGeneraleProcedimento().equals(null)) {  // ??? COME FA AD essere null?
            IFascicoloSius lFasCtrl = SIUSLookupRemote.getFascicoloSiusRemote();
            FascicoloGPModel lFasGPModel = lFasCtrl
                    .ExRicercaFascicoloByGenProc(lESSModel.getGenPridGeneraleProcedimento());
            setRequestAttribute("sanzioneUno", lFasGPModel);
            lCodContenuto = lFasGPModel.getGeneraleProcedimentoModel().getCodOggettoProcedimento();
        }
	      
	    // MEV_2023-35 si parametrizza il lCodContenuto per gestire anche le EPS
		Vector[] lVectSanzioniECorrelati = lESSCtrl.ExRicercaDettaglioESSeCorrelati(lIdEsecuzioneSS,
				lIdSoggetto, lCodUfficio, lCodContenuto);



		setRequestAttribute("sanzioni", lVectSanzioniECorrelati[0]);
		setRequestAttribute("correlati", lVectSanzioniECorrelati[1]);

		// Leggo anche il fascicolo SIEP se presente.
		BigDecimal lIdFascicoloSiep = null;
		if (!isRequestParameterNullObj(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP)
				&& getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP).trim()
						.length() > 0
				&& getRequestStringParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP).trim()
						.compareToIgnoreCase("null") != 0)
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP);
		if (lIdFascicoloSiep != null) {
			lIdFascicoloSiep = getRequestBigDecimalParameter(ICostantiEsecuzioneSS.CAMPO_ID_FASCICOLO_SIEP);
			IFascicoloSiep lFSiepCtrl = SIEPLookupRemote.getFascicoloSiepRemote();
			DettaglioFascicoloModel lDettaglio = lFSiepCtrl.ExDettaglioFascicoloSiep(lIdFascicoloSiep);

			if (lDettaglio == null)
				throw new F3BException(SIUSException.USER_MESSAGE, "Fascicolo SIEP non individuato");

			setRequestAttribute("dettaglioFascSiep", lDettaglio);
		}

		String lReturnPage = "";
		lReturnPage = PG_DETTAGLIO_ESECUZIONE_SS;

		return lReturnPage; // restituisce la jsp di VIEW
	}

}