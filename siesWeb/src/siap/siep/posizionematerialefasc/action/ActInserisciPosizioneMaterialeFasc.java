package siap.siep.posizionematerialefasc.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizionematerialefasc.controller.IPosizioneMaterialeFasc;
import siap.siep.posizionematerialefasc.model.PosizioneMaterialeFascModel;
import siap.siep.statoprocedimento.controller.IStatoProcedimento;
import siap.siep.statoprocedimento.model.StatoProcedimentoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciPosizioneMaterialeFasc
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di PosizioneMaterialeFasc
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
public class ActInserisciPosizioneMaterialeFasc extends ActionSiap implements ICostantiPosizioneMaterialeFasc {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Azione di Inserimento del PosizioneMaterialeFasc
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		// Si ricava il Fascicolo dalla sessione
		FascicoloSiepModel lFascicolo = ((FascicoloSiepModel) getSessionAttribute("fascicolo"));

		// Prelievo Data Inizio
		// STUB 15/02/2006 Date lDataInizio = getRequestDateParameter( CAMPO_ANNO_DATA_INIZIO,
		// CAMPO_MESE_DATA_INIZIO, CAMPO_GIORNO_DATA_INIZIO );
//		Calendar lCal = Calendar.getInstance();
		Date lDataInizio = DateUtils.getDate(getRequestIntParameter(CAMPO_ANNO_DATA_INIZIO),
				getRequestIntParameter(CAMPO_MESE_DATA_INIZIO),
				getRequestIntParameter(CAMPO_GIORNO_DATA_INIZIO),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.HOUR_OF_DAY),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.MINUTE),
				DateUtils.getDateToInt(DateUtils.getSysDate(), Calendar.SECOND));

		// Valorizzazione del record da inserire
		PosizioneMaterialeFascModel lPosMod = new PosizioneMaterialeFascModel();
		lPosMod.setCodPosizioneMateriale(getRequestStringParameter(CAMPO_COD_POSIZIONE_MATERIALE));
		lPosMod.setCodUfficio(getRequestStringParameter(CAMPO_COD_UFFICIO));
		lPosMod.setFasSieIdFascicoloSiep(lFascicolo.getIdFascicoloSiep());

		// Stato Procedimento
		String lStatoProcedimento = getStatoProcedimento(lFascicolo.getIdFascicoloSiep());
		if (lStatoProcedimento.length() < 1)
			lStatoProcedimento = lFascicolo.getDescrStatoProcedimento();
		lPosMod.setDescrStatoProcedimento(lStatoProcedimento);

		lPosMod.setDataInizio(lDataInizio);
		lPosMod.setDataInserimento(DateUtils.getSysDate());
		lPosMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lPosMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());

		// Inserimento
		IPosizioneMaterialeFasc lPosMatFasCtrl = SIEPLookupRemote.getPosizioneMaterialeFascRemote();
		/*PosizioneMaterialeFascModel llPosModRet = */lPosMatFasCtrl.ExInserisciPosizioneMaterialeFasc(lPosMod);

		// Dettaglio
		RedirectTo lPage = new RedirectTo();
		lPage.setPage(IWebConstants.PG_MAIN);
		lPage.setAction("siap.siep.posizionematerialefasc.action.ActLoadDettaglioPosizioneMaterialeFasc");
		lPage.setParameter(CAMPO_FAS_SIE_ID_FASCICOLO_SIEP, lPosMod.getFasSieIdFascicoloSiep().toString());

		return lPage.toString();
	}

	// La funzione restituisce la descrizione dello Stato Procedimento del fascicolo
	// memorizzata nella tabella STATO_PROCEDIMENTO
	@SuppressWarnings("rawtypes")
	private String getStatoProcedimento(BigDecimal aIdFascicolo) throws F3BException {
		String lDescrStatoProcedimento = "";

		IStatoProcedimento lStatProcCtrl = SIEPLookupRemote.getStatoProcedimentoRemote();
		Vector lStati = lStatProcCtrl.ExRicercaStatoProcedimentoByFascicoloSiep(aIdFascicolo);

		Iterator itx = lStati.iterator();
		while (itx.hasNext()) {
			StatoProcedimentoModel lStatoProc = (StatoProcedimentoModel) itx.next();
			if (lStatoProc.getDescrStatoProcedimento() != null
					&& lStatoProc.getDescrStatoProcedimento().trim().length() > 0) {
				lDescrStatoProcedimento += lStatoProc.getDescrStatoProcedimento();
				if (lStatoProc.getData() != null) {
					lDescrStatoProcedimento += ": ";
					lDescrStatoProcedimento += DateUtils.getDateToString(lStatoProc.getData(), "dd-MM-yyyy");
					lDescrStatoProcedimento += " ";
				}
			}
		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug("Stato Procedimento -> " + " : " + lDescrStatoProcedimento);
		return lDescrStatoProcedimento;
	}

}