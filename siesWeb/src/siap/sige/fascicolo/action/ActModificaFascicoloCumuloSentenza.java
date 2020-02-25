package siap.sige.fascicolo.action;

/**
 * <p>Title: ActModificaFascicoloCumuloSentenza</p>
 * <p>Description: Classe Azione di modifica del Fascicolo SIGE
 * </p>
 */
import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.sentenza.controller.IFasSigeSentenza;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;

public class ActModificaFascicoloCumuloSentenza extends ActInserisciFascicolo implements
		ICostantiFascicoloSige {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getPackage().getName() + ".processRequest : inizio");

		if (isSessionAttributeNullObj("FascicoloSigeEsteso"))
			throw new F3BException(F3BException.USER_MESSAGE, "Dati del Procedimento SIGE non in sessione !!");

		// Lettura del Fascicolo in sessione
		FascicoloSigeEstesoModel lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");

		String senIdSentenzaCumulo = "";
		if (!isRequestParameterNullObj("senIdSentenzaCumulo"))
			senIdSentenzaCumulo = getRequestStringParameter("senIdSentenzaCumulo");

		String idEventoProvvCumulo = "";
		if (!isRequestParameterNullObj("idEventoProvvCumulo"))
			idEventoProvvCumulo = getRequestStringParameter("idEventoProvvCumulo");

		// Controllo Dati
		if (lFascicoloEsteso.getFascicoloSige() == null
				|| lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige() == null)
			throw new F3BException(F3BException.USER_MESSAGE, "ID Procedimento SIGE assente !!");

		BigDecimal lIdFascicoloSige = lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige();

		// Valorizzazione ulteriori dati
		FascicoloSigeModel fascicoloSige = new FascicoloSigeModel();

		// Viene istanziato il controller per attuare la modifica del Fascicolo SIGE
		IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
		fascicoloSige = lCtrl.ExRicercaFascicoloSigeByKey(lIdFascicoloSige);
		fascicoloSige.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
		fascicoloSige.setCodOperatoreAggiornamento(getCodUtenteConnesso());
		fascicoloSige.setDataAggiornamento(DateUtils.getSysDate());
		fascicoloSige.setSenIdSentenzaCumulo(new BigDecimal(senIdSentenzaCumulo));
		// Identifica il Provvedimento di Cumulo associato al Fascicolo Sige
		fascicoloSige.setIdEventoProvvCumulo(new BigDecimal(idEventoProvvCumulo));

		lCtrl.ExModificaFascicoloSige(fascicoloSige);

		IFasSigeSentenza lFasSenCtrl = SIGELookupRemote.getFasSigeSentenzaRemote();
		// Vector lProcedimentoSiepDiCumulo =
		// lFasSenCtrl.ExRicercaProcedimentoSiepDiCumulo(getFascicoloSigeEstesoInSessione().getFascicoloSiep().getIdFascicoloSiep());
		Vector lProcedimentoSiepDiCumulo = lFasSenCtrl
				.ExRicercaSentenzeAssegnateFascicolo(getFascicoloSigeEstesoInSessione().getFascicoloSiep()
						.getIdFascicoloSiep());

		setRequestAttribute("ListaProcedimentoSiepDiCumulo", lProcedimentoSiepDiCumulo);

		return PG_LOAD_DETTAGLIOFASCICOLOSIGE;
	}

}