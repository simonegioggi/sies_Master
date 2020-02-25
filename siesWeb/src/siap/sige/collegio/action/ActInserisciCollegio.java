package siap.sige.collegio.action;

//import java.math.BigDecimal;
//import java.util.ArrayList;

import org.apache.log4j.Logger;

import siap.sige.collegio.controller.ICollegio;
//import siap.sige.collegiomagistrato.model.CollegioMagistratoModel;
//import siap.sige.collegioesperto.model.CollegioEspertoModel;
//import siap.sige.collegiogiudicepopolare.model.CollegioGiudicePopolareModel;
//import siap.sius.esperto.action.ICostantiEsperto;
//import siap.sico.web.ActionSiap;
import siap.sige.collegio.model.CollegioModel;
//import siap.sige.magistrato.action.ICostantiMagistrato;
//import siap.sige.giudicepopolare.action.ICostantiGiudicePopolare;
import siap.sige.util.SIGELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;
//import f3b.util.F3BException;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciCollegio</p>
* <p>Description: Classe Action per l'inserimento della Sezione</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Eutelia</p>
* @version 1.0
*/
public class ActInserisciCollegio extends ActionCollegio implements ICostantiCollegio {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
	// CollegioModel lColMod = new CollegioModel();
	/**
	* Effettua Inserimento della Collegio.
	* <p>
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione.
	* <p>
	* @throws Exception
	*/
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");


		lColMod.setCodCollegio(getRequestStringParameter(CAMPO_COD_COLLEGIO));

		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE))
			lColMod.setSezIdSezione(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));

		lColMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());

		lColMod.setDataInizioValidita(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_VALIDITA, CAMPO_MESE_DATA_INIZIO_VALIDITA, CAMPO_GIORNO_DATA_INIZIO_VALIDITA));
		lColMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA, CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));

		lColMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lColMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lColMod.setDataInserimento(DateUtils.getSysDate());

		// I metodi seguenti di lettura sono ereditati dalla ActionCollegio
		// Prevedono che nell'istanza lColMod sia valorizzato il codice collegio.

		// Legge e Popola i dati afferenti ai Magistrati.
		letturaDatiMagistrati();

		// Legge e Popola i dati afferenti ai Giudici Popolari.
		letturaDatiGiudiciPopolari();

		// Legge e Popola i dati afferenti agli Esperti.
		letturaDatiEsperti();

		// Chiama il controller.
		ICollegio lCtrl = SIGELookupRemote.getCollegioRemote();
		CollegioModel lColModRet = lCtrl.ExInserisciCollegio(lColMod); // setta la risposta nella request

		setRequestAttribute("sezione", lColModRet);

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.collegio.action.ActLoadDettaglioCollegio");
		lRedir.setParameter(CAMPO_ID_COLLEGIO, lColModRet.getIdCollegio().toString());
		// Per passare il punto di ritorno
		lRedir.setParameter(IWebConstants.LINK_RITORNO, "10");

		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");

		return lPage;
	}
}