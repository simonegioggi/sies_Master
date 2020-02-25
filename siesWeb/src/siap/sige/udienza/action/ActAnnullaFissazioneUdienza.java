package siap.sige.udienza.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
* <p>Title: ActAvvunnaFissazioneUdienza </p>
* <p>Description: Classe Action per l'annullamento di una Fissazione Udienza
* </p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/
public class ActAnnullaFissazioneUdienza extends ActRicercaFSigePuntuale {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		String lRectPage = "";
		String nextAct = null;

		// ID UDIENZA_PROCEDIMENTO da annullare
		BigDecimal lIdUdiPro = getRequestBigDecimalParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug("id udienza_procediment -> " + lIdUdiPro);

		// Ricerca di UDIENZA_PROCEDIMENTO da aggiornare
		IUdienzaProcedimentoSige lCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		UdienzaProcedimentoSigeModel lUdiProcModel = lCtrl.ExRicercaUdienzaProcedimentoSigeByKey(lIdUdiPro);
		// Viene valorizzato il model da aggiornare
		lUdiProcModel.setIdUdienzaProcedimentoSige(lIdUdiPro);
		lUdiProcModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lUdiProcModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lUdiProcModel.setDataAggiornamento(DateUtils.getSysDate());
		lUdiProcModel.setFlagRinviata(ICostantiUdienzaProcedimentoSige.UDIENZA_ANNULLATA);
		// intervento per 11.2.1 occorre svuotare il campo udi_id_udienza_sige
		lUdiProcModel.setUdiIdUdienzaSige(null);
		lCtrl.ExCancellaFissazioneUdienza(lUdiProcModel);

		// Aggiornamento del Fascicolo in sessione
		reloadFascicoloSigeEsteso();

		if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
			nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
			if (!isRequestParameterNullObj("noQuery")) {
				nextAct += "&noQuery=";
				nextAct += "OK";
			}
		}

		lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");
		return lRectPage;
	}
}