package siap.siep.sanzionesostitutiva.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.sanzionesostitutiva.controller.ISanzioneSostitutiva;
import siap.siep.sanzionesostitutiva.model.RicercaStatoPagamentiModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * MEV_2023-33: Action per la ricerca dello stato dei pagamenti
 *
 * @author df
 * @version 1.0
 */
public class ActRicercaStatoPagamenti extends ActionSiap
		implements ICostantiSanzioneSostitutiva, ICostantiFascicoloSiep {

	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		siesLogger.debug("ActRicercaStatoPagamenti....");
		this.setLinkRitorno();

		if (this.isSessionAttributeNullObj("fascicolo")) {
			setRequestAttribute("fascicoloNotInSession", "S");
		}

		// =======================================================
		FascicoloSiepModel lFasMod = new FascicoloSiepModel();

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_INIZIALE))
			lFasMod.setChiaveAnnoIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_INIZIALE))
			lFasMod.setChiaveProgrIniziale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_INIZIALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_ANNO_FINALE))
			lFasMod.setChiaveAnnoFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO_FINALE));

		if (!isRequestParameterNullObj(CAMPO_CHIAVE_PROGR_FINALE))
			lFasMod.setChiaveProgrFinale(getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR_FINALE));

		// Data iscrizione Iniziale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_INIZIALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_INIZIALE)) {
			lFasMod.setDataIscrizioneIniziale(
					getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_INIZIALE,
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_INIZIALE));

		}

		// Data iscrizione Finale
		if (!isRequestParameterNullObj(CAMPO_GIORNO_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_MESE_ISCRIZIONE_FINALE)
				&& !isRequestParameterNullObj(CAMPO_ANNO_ISCRIZIONE_FINALE)) {
			lFasMod.setDataIscrizioneFinale(
					getRequestDateParameter(ICostantiFascicoloSiep.CAMPO_ANNO_ISCRIZIONE_FINALE,
							ICostantiFascicoloSiep.CAMPO_MESE_ISCRIZIONE_FINALE,
							ICostantiFascicoloSiep.CAMPO_GIORNO_ISCRIZIONE_FINALE));
		}

		String tipoRicerca = getRequestStringParameter(
				ICostantiSanzioneSostitutiva.CAMPO_TIPO_RICERCA_STATO_PAGAMENTO);

		String chiaveUfficio = getCodUfficioUtenteConnesso();
		lFasMod.setChiaveUfficio(chiaveUfficio);

		ISanzioneSostitutiva lCtrlSanzSost = SIEPLookupRemote.getSanzioneSostitutivaRemote();
	  //==================================================================================================================
		// Stampo excel
		if (!isRequestParameterNullObj("tipoRicerca") && "stampaExcel".equals(getRequestStringParameter("tipoRicerca"))) {
			// Richiesta stampa Excel, effettuo la ricerca non paginata
			Vector <RicercaStatoPagamentiModel> lVect = lCtrlSanzSost.ExRicercaFascicoliPerStatoPagamentoPaged(lFasMod,
					Integer.parseInt("0"), tipoRicerca);
			
			return stampaExcel(lVect, lFasMod, tipoRicerca);
	  }
		// FINE STAMPA		
	  //==================================================================================================================
		
		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);


		Vector lVect = lCtrlSanzSost.ExRicercaFascicoliPerStatoPagamentoPaged(lFasMod,
				Integer.parseInt(lPagina), tipoRicerca);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrlSanzSost.ExGetCountRicercaFascicoliPerStatoPagamento(lFasMod, tipoRicerca);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		String descTipoRicerca = "";
		if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_INTERAMENTE_PAGATO))
			descTipoRicerca = "Procedimenti con pena pecuniaria totalmente pagata";
		else if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_RETEIZZATO_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento rateizzato con rate non pagate";
		else if (tipoRicerca.equals(CAMPO_TIPO_RICERCA_UNICA_RATA_NON_PAGATO))
			descTipoRicerca = "Procedimenti con pagamento in unica soluzione non pagata";

		setRequestAttribute("criteriRicerca", lFasMod);
		setRequestAttribute("tipoRicerca", tipoRicerca);
		setRequestAttribute("descTipoRicerca", descTipoRicerca);
		setRequestAttribute("elencoFascicoli", lVect);

		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);

		// Lancio la ricerca
		return PG_ESITO_RICERCA_STATO_PAGAMENTI;
		
	}

	/**
	 * 
	 * 
	 * */
	private String stampaExcel (Vector <RicercaStatoPagamentiModel> listaStatoPagamenti,FascicoloSiepModel lFasMod, String tipoRicerca) throws Exception {
		siesLogger.debug("stampaExcel....");
		HSSFWorkbook wb = new HSSFWorkbook();
		ISanzioneSostitutiva lCtrlSanzSost = SIEPLookupRemote.getSanzioneSostitutivaRemote();
		lCtrlSanzSost.ExCreateExcelStatoPagamenti (listaStatoPagamenti, wb, this.getUfficioUtenteConnesso(), lFasMod, tipoRicerca);
	
		// Generazione file xls
		ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
		try {
			wb.write(fileOut);
		// } catch (IOException ioe) {
		} catch (Exception ioe) {
			siesLogger.error("Exception",ioe);
			throw new F3BException("ActRicercaStatoPagamenti.stampaExcel: " + ioe);
		}
	
		setRequestAttribute("report", fileOut);
		setRequestAttribute(IWebConstants.DISPOSITION_FIELD, IWebConstants.ATTACHMENT_DISPOSITION_FILE);
	
		return IWebConstants.PG_DOWNLOAD_DOCUMENT;
	}
}