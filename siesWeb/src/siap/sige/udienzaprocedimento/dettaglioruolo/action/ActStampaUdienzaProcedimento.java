package siap.sige.udienzaprocedimento.dettaglioruolo.action;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.web.IWebConstants;
import siap.sico.evento.model.XModel;
import siap.sico.template.action.ICostantiTemplate;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;

/**
 * <p>
 * Title: ActStampaUdienzaProcedimento
 * </p>
 * <p>
 * Description: Azione per la stampa "Procedimenti fissati per Udienza"
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ActStampaUdienzaProcedimento extends ActionSige implements ICostantiUdienzaProcedimentoSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("inizio");

		// BigDecimal lIdUdienza;
		String returnpage = IWebConstants.PG_DOWNLOAD_NEW;

		String lOrderBy = null;
		lOrderBy = getRequestStringParameter("tipo");

		/*
		 * 20110127 ( da verificare le condizioni di filtro etc.... ) String tipoProc =
		 * getRequestStringParameter("tipoProc") ;
		 */
		String tipoProc = "TUTTI"; // 20110127 Temporanea forzatura.

		String lStatoProcedimento = getRequestStringParameter("lStatoProcedimento");

		// ???
		setRequestAttribute("tipoProc", tipoProc);
		setRequestAttribute("lStatoProcedimento", lStatoProcedimento);

		// Lettura IDUdienza
		BigDecimal lIdUdienza = null;
		// if( !isRequestParameterNullObj(CAMPO_UDI_ID_UDIENZA_SIGE) )
		// lIdUdienza = getRequestBigDecimalParameter( CAMPO_UDI_ID_UDIENZA_SIGE );

		// intervento per 11.2.1 (nb: il valore di )

		// Lettura DataUdienza
		Date lDataUdienza = null;
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA))
			lDataUdienza = getRequestDateParameter(ICostantiUdienzaSige.CAMPO_DATA_UDIENZA, "yyyyMMdd");

		// introduco per 11.2.1
		String tipoRito = null; // C=collegiale; M=monocratico
		IUdienzaSige lCtrlUd = SIGELookupRemote.getUdienzaSigeRemote();

		String listaIdUdienze = "";
		if (this.getParameter("listaIdUdienze") != null && !"".equals(this.getParameter("listaIdUdienze"))) {
			listaIdUdienze = getRequestStringParameter("listaIdUdienze");
			setRequestAttribute("listaIdUdienze", listaIdUdienze);

			String[] udiArr = null;
			if (!listaIdUdienze.equals("")) {
				udiArr = listaIdUdienze.split(";");
			}
			// deve essere sempre 1 udienza!
			if (udiArr != null && udiArr.length == 1) {
				lIdUdienza = new BigDecimal(listaIdUdienze);
				UdienzaSigeModel udi = lCtrlUd.ExRicercaUdienzaSigeById(lIdUdienza);
				tipoRito = udi.getColIdCollegio() != null ? "C" : "M";
			}

		}

		// Lettura CodMagistrato
		// il magistrato mi serve solo per le monocratiche
		String lCodMagistrato = null;
		if (!isRequestParameterNullObj(CAMPO_COD_MAGISTRATO))
			lCodMagistrato = getRequestStringParameter(CAMPO_COD_MAGISTRATO);

		if (tipoRito != null && "C".equals(tipoRito))
			lCodMagistrato = null;

		// Lettura IDEsperto
		BigDecimal lIdEsperto = null;
		if (!isRequestParameterNullObj(CAMPO_ID_ESPERTO))
			lIdEsperto = getRequestBigDecimalParameter(CAMPO_ID_ESPERTO);

		/* Informazioni ufficio */
		String lTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();
		String lDescTipoUfficio = getUfficioUtenteConnesso().getDescrTipoUfficio().toUpperCase();

		String lIdDocumento = getRequestStringParameter(ICostantiTemplate.CAMPO_ID_TEMPLATE);

		XModel lXModel = new XModel();

		lXModel.setTipoUfficio(lTipoUfficio);
		lXModel.setTipoUfficioT1(lDescTipoUfficio);
		lXModel.setUfficio(getUfficioUtenteConnesso().getDescrComune().toUpperCase());

		ByteArrayOutputStream lReport = null;
		// Lookup.
		IUdienzaProcedimentoSige lCtrlUdPr = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		if (lIdUdienza != null) {
			lReport = lCtrlUdPr.ExStampaProcedimentixUdienza(lIdUdienza, null, lCodMagistrato, lIdEsperto,
					lXModel, lIdDocumento, lOrderBy, getUtenteConnesso(), lStatoProcedimento, tipoProc,
					getCodUfficioUtenteConnesso());
		} else if (lDataUdienza != null) {
			lReport = lCtrlUdPr.ExStampaProcedimentixDataUdienza(lDataUdienza, null, lCodMagistrato,
					lIdEsperto, lXModel, lIdDocumento, lOrderBy, getUtenteConnesso(), lStatoProcedimento,
					tipoProc, getCodUfficioUtenteConnesso());
		}

		// Prepara la pagina di destinazione.
		if (lReport != null)
			setRequestAttribute("report", lReport);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.info("fine");

		return returnpage;
	}

}