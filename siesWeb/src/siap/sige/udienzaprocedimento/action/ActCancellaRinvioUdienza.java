package siap.sige.udienzaprocedimento.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

//import siap.sico.web.ActionSiap;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.udienzaprocedimento.controller.IUdienzaProcedimentoSige;
import siap.sige.udienzaprocedimento.model.UdienzaProcedimentoSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
//import siap.sius.SIUSException;
//import siap.sius.fascicolo.model.FascicoloGPModel;
//import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

//import siap.sius.udienzaprocedimento.action.ICostantiUdienzaProcedimento;
//import siap.sius.udienzaprocedimento.model.UdienzaProcedimentoModel;
//import siap.sius.udienzaprocedimento.controller.IUdienzaProcedimento;

/**
 * <p>
 * Title: ActCacellaRinvioUdienza
 * </p>
 * <p>
 * Description: Classe Action per l'annullamento di un Rinvio Udienza.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActCancellaRinvioUdienza extends ActionSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	protected FascicoloSigeEstesoModel mFasEsteso;

	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: inizio");

		String lRectPage = "";
		String nextAct = null;
		// BigDecimal lIdFasSius = null;

		// Preleva dalla sessione il model fascicoloSiusGP
		// FascicoloGPModel lFascicoloGPModel = (FascicoloGPModel)getSessionAttribute("fascicoloSiusGP");
		// if(lFascicoloGPModel == null || lFascicoloGPModel.getFascicoloSiusModel() == null ||
		// lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius() == null)
		// throw new SIUSException(SIUSException.USER_MESSAGE,"Fascicolo SIUS non in sessione");

		// lIdFasSius = lFascicoloGPModel.getFascicoloSiusModel().getIdFascicoloSius();

		// Fascicolo Sige Esteso in sessione.
		mFasEsteso = this.getFascicoloSigeEstesoInSessione();
		BigDecimal lIdFasSige = mFasEsteso.getFascicoloSige().getIdFascicoloSige();

		// ID UDIENZA_PROCEDIMENTO da annullare
		BigDecimal lIdUdiPro = getRequestBigDecimalParameter(
				ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("id udienza_procedimento -> " + lIdUdiPro);

		// Ricerca di UDIENZA_PROCEDIMENTO da aggiornare
		IUdienzaProcedimentoSige lCtrl = SIGELookupRemote.getUdienzaProcedimentoSigeRemote();
		// UdienzaProcedimentoModel lUdiProcModel = lCtrl.ExRicercaUdienzaProcedimentoByKey(lIdUdiPro);
		UdienzaProcedimentoSigeModel lUdiProcModel = lCtrl.ExRicercaUdienzaProcedimentoSigeByKey(lIdUdiPro);

		// Viene valorizzato il model da aggiornare
		// lUdiProcModel.setIdUdienzaProcedimento(lIdUdiPro);
		lUdiProcModel.setCodOperatoreAggiornamento(this.getCodUtenteConnesso());
		lUdiProcModel.setCodUfficioAggiornamento(this.getCodUfficioUtenteConnesso());
		lUdiProcModel.setDataAggiornamento(DateUtils.getSysDate());
		// lUdiProcModel.setFlagRinviata(ICostantiUdienzaProcedimento.UDIENZA_ANNULLATA);

		// lCtrl.ExCancellaRinvioUdienza(lUdiProcModel, lIdFasSius );
		lCtrl.ExCancellaRinvioUdienza(lUdiProcModel, lIdFasSige);

		if (!isRequestParameterNullObj(IWebConstants.ACTION_DOPO_CANCELLAZIONE)) {
			nextAct = getRequestStringParameter(IWebConstants.ACTION_DOPO_CANCELLAZIONE);
			if (!isRequestParameterNullObj("noQuery")) {
				nextAct += "&noQuery=";
				nextAct += "OK";
			}
		}

		lRectPage = ritornoDopoCancellazione("Cancellazione Avvenuta Correttamente!", nextAct);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lRectPage;
	}

}