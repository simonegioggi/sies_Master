package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import f3b.log.LogF3B;
import f3b.model.DecodeModel;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.controller.IModuloCumulo;
import siap.siep.sentenza.action.ICostantiSentenza;
import siap.siep.sentenza.model.SentenzaModel;
import siap.siep.sentenza.model.SentenzaSoggettoFascicoloModel;
import siap.siep.util.SIEPLookupRemote;

import org.apache.log4j.Logger;

/**
 * Action che effettua la ricerca dei Procedimenti (fascicoli_siep) per Sentenza e Soggetto da visualizzare in
 * popup per selezionare il procedimento in caso di mancata indicazione da paret del richiedenete.
 * 
 * @author d.fiorletta
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActRicercaProcedimentiPerTitoloSoggetto extends ActionSiap implements ICostantiModuloCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException, Exception {

		siesLogger.debug("--XX-- ActRicercaProcedimentiPerTitoloSoggetto - INIZIO");

		// ==========================================================================
		// Recupero i parametri di ricerca
		// ==========================================================================
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		FascicoloSiepModel lFascicoloSiepModel = new FascicoloSiepModel();
		SoggettoModel lSoggettoModel = new SoggettoModel();
		SentenzaModel lSentenzaModel = new SentenzaModel();

		// Tipo Provvedimento
		lSentenzaModel.setCodTipoProvvedimento(null);
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO).equals("-")) {
			lSentenzaModel.setCodTipoProvvedimento(
					getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_PROVVEDIMENTO));
		}

		// Anno e Numero Provvedimento
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_ANNO_SENTENZA)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_ANNO_SENTENZA).equals("")) {
			lSentenzaModel
					.setAnnoSentenza(getRequestBigDecimalParameter(ICostantiSentenza.CAMPO_ANNO_SENTENZA));
		}

		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_NUMERO_SENTENZA)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA).equals("")) {
			lSentenzaModel
					.setNumeroSentenza(getRequestStringParameter(ICostantiSentenza.CAMPO_NUMERO_SENTENZA));
		}

		// Autorità emittente
		lSentenzaModel.setCodTipoAutoritaEmittente(null);
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE)
						.equals("-")) {
			lSentenzaModel.setCodTipoAutoritaEmittente(
					getRequestStringParameter(ICostantiSentenza.CAMPO_COD_TIPO_AUTORITA_EMITTENTE));
		}

		String lDescrLuogo = null;
		lSentenzaModel.setCodLuogoEmittente(null);
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE).equals("")) {
			lDescrLuogo = getRequestStringParameter(ICostantiSentenza.CAMPO_COD_LUOGO_EMITTENTE);
			ComuneModel lComuneLuogoEmittente = getCodComuneByDescr(lDescrLuogo);
			lSentenzaModel.setCodLuogoEmittente(lComuneLuogoEmittente.getCodComune());
		}

		// Verifico l'esistenza dell'autorità
		// if (lSentenzaModel.getCodTipoAutoritaEmittente()!=null &&
		// lSentenzaModel.getCodLuogoEmittente()!=null){
		// try {
		// getCodUfficioByCodTipoUfficioDescrComune (lSentenzaModel.getCodTipoAutoritaEmittente(),
		// lDescrLuogo);
		// } catch (Exception e) {
		// if (e instanceof SICOException) {
		// throw new SICOException(SICOException.USER_MESSAGE, "L'Ufficio
		// "+lSentenzaModel.getDescrTipoAutoritaEmittente()
		// +" - "+lSentenzaModel.getCodTipoAutoritaEmittente()
		// +" di "+lDescrLuogo+" indicato nella richiesta come emittente la sentenza risulta non censito.");
		// }
		// }
		// }

		// Data Sentenza
		if (!isRequestParameterNullObj(ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO)
				&& !getRequestStringParameter(ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO).equals("")) {
			Date lDataProvvedimento = getRequestDateParameter(ICostantiSentenza.CAMPO_ANNO_DATA_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_MESE_DATA_PROVVEDIMENTO,
					ICostantiSentenza.CAMPO_GIORNO_DATA_PROVVEDIMENTO);
			lSentenzaModel.setDataProvvedimento(lDataProvvedimento);
		}

		// Data Irrevocabilità
		if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA)
				&& !getRequestStringParameter(ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA)
						.equals("")) {
			Date lDataIrrevocabilita = getRequestDateParameter(
					ICostantiFascicoloSiep.CAMPO_ANNO_DATA_IRREVOCABILITA,
					ICostantiFascicoloSiep.CAMPO_MESE_DATA_IRREVOCABILITA,
					ICostantiFascicoloSiep.CAMPO_GIORNO_DATA_IRREVOCABILITA);
			lFascicoloSiepModel.setDataIrrevocabilita(lDataIrrevocabilita);
		}

		// Estremi del soggetto
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals("")) {
			lSoggettoModel.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		}

		// String lNome = null;
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).equals("")) {
			lSoggettoModel.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		}

		// String lCodAfis = null;
		if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_AFIS)
				&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS).equals("")) {
			lSoggettoModel.setCodAfis(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS));
		}
		// ==========================================================================

		lFascicoloSiepModel.setChiaveUfficio(getCodUfficioUtenteConnesso());

		siesLogger.debug("lFascicoloSiepModel = " + lFascicoloSiepModel);
		siesLogger.debug("lSentenzaModel = " + lSentenzaModel);
		siesLogger.debug("lSoggettoModel = " + lSoggettoModel);

		// ==========================================================================
		// Lancio la ricerca
		// ==========================================================================
		IModuloCumulo lModuloCumuloCtrl = SIEPLookupRemote.getModuloCumuloRemote();
		// Vector <FascicoloSiepModel> lListaProcedimenti = null;
		Vector<SentenzaSoggettoFascicoloModel> lListaProcedimenti = null;

		lListaProcedimenti = lModuloCumuloCtrl.ExRicercaProcedimentiPerTitoloSoggetto(lSentenzaModel,
				lSoggettoModel, lFascicoloSiepModel, Integer.parseInt(lPagina));
		BigDecimal lCountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			lCountRisultati = lModuloCumuloCtrl.ExCountProcedimentiPerTitoloSoggetto(lSentenzaModel,
					lSoggettoModel, lFascicoloSiepModel);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		siesLogger.debug("CountRisultati = " + lCountRisultati);
		siesLogger.debug("lListaProcedimenti = " + lListaProcedimenti.size());

		setRequestAttribute("ListaProcedimenti", lListaProcedimenti);

		// Passo i dati per gestire la paginazione
		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		// Combo Tipo Provvedimento
		Collection lTipoProvvColl = new ArrayList();
		lTipoProvvColl.add(new DecodeModel("-", "-"));
		lTipoProvvColl.add(new DecodeModel("01", "Sentenza"));
		lTipoProvvColl.add(new DecodeModel("02", "Decreto Penale"));
		// lTipoProvvColl.add(new DecodeModel("05","Sentenza (di riconoscimento di sentenza straniera)"));
		// lTipoProvvColl.add(new DecodeModel("03","Ordinanza"));

		Option lOptionTipoProvv = new Option(lTipoProvvColl, "-");
		if (lSentenzaModel.getCodTipoProvvedimento() != null) {
			lOptionTipoProvv.setSelected(lSentenzaModel.getCodTipoProvvedimento());
		}
		setRequestAttribute("TipoProvvedimento", "" + lOptionTipoProvv);

		// Combo Autorita Emittente
		Option lOptionAutEmi = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		if (lSentenzaModel.getCodTipoAutoritaEmittente() != null) {
			lOptionAutEmi.setSelected(lSentenzaModel.getCodTipoAutoritaEmittente());
		}
		setRequestAttribute("autoritaEmi", "" + lOptionAutEmi);

		return PG_LISTA_PROCEDIMENTI_SENTENZA_SOGGETTO;
	}

}