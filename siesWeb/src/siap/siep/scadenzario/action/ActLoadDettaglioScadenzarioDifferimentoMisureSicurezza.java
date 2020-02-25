package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * MEV_39
 * <p>
 * Title: ActLoadDettaglioScadenzarioDifferimentoMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario differimento MS
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: EII
 * </p>
 * 
 * @author Gioggi
 * @version 1.0
 */
public class ActLoadDettaglioScadenzarioDifferimentoMisureSicurezza extends ActionSiap implements
		ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_SCADENZARIO);
		// chiama il controller
		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		// riempie il model
		ScadenzarioModel llScaMod = lCtrl.ExRicercaScadenzarioByKey(new BigDecimal(lId));
		setRequestAttribute("scadenzario", llScaMod);
		llScaMod.setFlagVisto("S");
		llScaMod.setDataVisto(DateUtils.getSysDate());
		lCtrl.ExModificaScadenzario(llScaMod);

		BigDecimal KeyFas = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

		IFascicoloSiep lctrlF = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasciMod = lctrlF.ExRicercaFascicoloByKeyNoError(KeyFas);

		// Ricerca Misure sicurezza ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(KeyFas);
		if (lListMis.size() <= 0) {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento privo di Misura di Sicurezza, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}

		setRequestAttribute("fascicoloSiep", lFasciMod);
		setRequestAttribute("tipo", getRequestStringParameter("tipo"));
		setRequestAttribute("giorniScadenza", getRequestStringParameter("giorniScadenza"));
		setRequestAttribute("mesiScadenza", getRequestStringParameter("mesiScadenza"));
		setRequestAttribute("anniScadenza", getRequestStringParameter("anniScadenza"));

		return PG_LOAD_DETTAGLIOSCADENZARIODIFFERIMENTO_MS;
	}

}