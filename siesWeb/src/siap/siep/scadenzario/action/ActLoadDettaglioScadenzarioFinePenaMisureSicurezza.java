package siap.siep.scadenzario.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.controller.IFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.misurasicurezza.controller.IMisuraSicurezza;
import siap.siep.misurasicurezza.model.FascMsToFascSiepModel;
import siap.siep.misurasicurezza.model.MisuraSicurezzaModel;
import siap.siep.scadenzario.controller.IScadenzario;
import siap.siep.scadenzario.model.ScadenzarioModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActLoadDettaglioScadenzarioFinePenaMisureSicurezza
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Scadenzario
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
public class ActLoadDettaglioScadenzarioFinePenaMisureSicurezza extends ActionSiap implements
		ICostantiScadenzario {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		String lId = getRequestStringParameter(CAMPO_ID_SCADENZARIO);
		// chiama il controller
		IScadenzario lCtrl = SIEPLookupRemote.getScadenzarioRemote();
		// MEV_39: modificata query di ricerca per il model da caricare a video
		ScadenzarioModel llScaMod = lCtrl.ExRicercaScadenzarioCSMSByKey(new BigDecimal(lId),
				getCodUfficioUtenteConnesso());
		setRequestAttribute("scadenzario", llScaMod);
		ScadenzarioModel sm = lCtrl.ExRicercaScadenzarioByKey(new BigDecimal(lId));
		sm.setFlagVisto("S");
		sm.setDataVisto(DateUtils.getSysDate());
		// MEV_39: aggiunti set di prorietà
		sm.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		sm.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		sm.setDataAggiornamento(DateUtils.getSysDate());
		lCtrl.ExModificaScadenzario(sm);

		BigDecimal KeyFas = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);
		IFascicoloSiep lctrlF = SIEPLookupRemote.getFascicoloSiepRemote();
		FascicoloSiepModel lFasciMod = lctrlF.ExRicercaFascicoloByKeyNoError(KeyFas);

		// Ricerca Misure sicurezza ORDINATE per DATA_INSERIMENTO
		List lListMis = new ArrayList();
		IMisuraSicurezza lMisCtrl = SIEPLookupRemote.getMisuraSicurezzaRemote();
		lListMis = lMisCtrl.ExRicercaMisuraSicurezzaByIdFascicoloOrd(KeyFas);
		// prendo solo l'ultima MISURA (28/10/2014 se esiste)
		MisuraSicurezzaModel lMisSicMod = null;
		if (lListMis.size() > 0)
			lMisSicMod = (MisuraSicurezzaModel) lListMis.get(lListMis.size() - 1);
		else {
			RedirectTo lRedirigi = new RedirectTo();
			lRedirigi.setPage(IWebConstants.PG_MAIN);
			setRequestAttribute(IWebConstants.MESSAGE_TEXT,
					"Procedimento privo di Misura di Sicurezza, impossibile procedere!");
			return IWebConstants.PG_MESSAGE;
		}

		// MEV_39: recupero dati fasc collegato e li imposto come attributo della richiesta
		FascMsToFascSiepModel model = lMisCtrl.ExRicercaDatiFascColl(getCodUfficioUtenteConnesso(), lId,
				KeyFas);
		setRequestAttribute("fascMsToFascSiepModel", model);

		setRequestAttribute("MisuraModel", lMisSicMod);
		setRequestAttribute("listaMisure", lListMis);
		setRequestAttribute("fascicoloSiep", lFasciMod);

		return PG_LOAD_DETTAGLIOSCADENZARIOFINEPENA_MS;
	}

}