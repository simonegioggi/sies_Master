package siap.siep.istruttoriacumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.controller.IIstruttoriaCumulo;
import siap.siep.modulocumulo.action.ActionModuloCumulo;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.util.SIEPLookupRemote;

public class ActRicercaPropriProcedimenti extends ActionModuloCumulo implements ICostantiIstruttoriaCumulo {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	public String processRequest() throws F3BException {

		siesLogger.debug("--XX-- ActRicercaPropriProcedimenti - INIZIO");
		BigDecimal lIdIstruttoriaCumulo = getRequestBigDecimalParameter(CAMPO_ID_ISTRUTTORIA_CUMULO);

		SoggettoModel soggetto = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		/* MEV_2025-52 - Ricerca soggetto da Iscrizione proprio titolo */
		FascicoloSiepModel lFasModel = null;
		// if (!isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO)) {
		if (isRequestChecked("checkRicercaProvv")) {
			// Ricerca per estremi fascicolo proprio ufficio
			BigDecimal chiaveAnno = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ANNO);
			BigDecimal chiaveProg = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_PROGR);
			BigDecimal chiaveProgRich = new BigDecimal(chiaveProg.toString()) ;
			String chiaveUffi = getCodUfficioUtenteConnesso();
			
			BigDecimal offsetUffAccorpato = new BigDecimal(0); // è il trattino della combo
			if (!isRequestParameterNullEmptyObj(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO)) {
				siesLogger.debug("--XX-- Ufficio Accorpato presente in form");
				offsetUffAccorpato = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_CHIAVE_ACCORPATO);
				chiaveProgRich = chiaveProg.add(offsetUffAccorpato); // offsetUffAccorpato eventualmente 0
				siesLogger.debug("--XX-- nova chiave progr chiaveProg = "+chiaveProg);
			}
			
			lFasModel = new FascicoloSiepModel();
			lFasModel.setChiaveAnno(chiaveAnno);
			lFasModel.setChiaveProgr(chiaveProgRich);
			lFasModel.setChiaveUfficio(chiaveUffi);
			
			setRequestAttribute("checkRicercaProvvVal", "checked"); 
			setRequestAttribute("chiaveAnnoRich", chiaveAnno.toString());
			setRequestAttribute("chiaveProgRich", chiaveProg.toString());
			setRequestAttribute("offsetUffAccorpato", offsetUffAccorpato.toString());	
			/* MEV_2025-52 - FINE */
		}
		else if ((isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
				|| getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals(""))
				&& (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_AFIS)
						|| getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS).equals(""))
				&& (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA)
						|| getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).equals(""))) {
			// Provengo dalla Lista Titoli Coinvolti ; La successiva Ricerca sarà effettuata con dati del
			// Soggetto di sessione
			soggetto = (SoggettoModel) getSessionAttribute("soggetto");
			setRequestAttribute("primoCaricamento", "true"); 
		} else {
			// Provengo dalla Form Ricerca Fascicoli Propriio Ufficio ; La successiva Ricerca sarà effettuata
			// Recuperando i dati della form
			siesLogger.debug(
					"--XX-- Provengo dalla Form di Ricerca - parametri VALORIZZATI - passaggio dopo Ricerca  ");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).equals("")) {
				soggetto.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME).toUpperCase());
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COGNOME))
			    setRequestAttribute("isCheckCognome", "checked");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).equals("")) {
				soggetto.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME).toUpperCase());
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_NOME))
				setRequestAttribute("isCheckNome", "checked");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_AFIS)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS).equals("")) {
				soggetto.setCodAfis(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_AFIS));
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_AFIS)) 
				setRequestAttribute("isCheckCUI", "checked");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).equals("")) {
				/* MEV_2025-52 - Modificata ricerca comune. Si entra per descrizione */
//				soggetto.setCodComuneNascita(getDatiComuneByDescrOmonimiaFlagVal(
//						getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA))
//								.getCodComune());
				soggetto.setDescrComuneNascita(
						getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA));
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)) 
			   setRequestAttribute("isCheckComune", "checked");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("")
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-")) {
				soggetto.setCodStatoNascita(
						getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA))  
			   setRequestAttribute("isCheckStato", "checked");

			if (!isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA)
					&& !getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).equals("")) {
				soggetto.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
						ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA,
						ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));
			} else if (isRequestParameterNullObj(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA)) 
				setRequestAttribute("isCheckDataNascita", "checked");
		}

		// Passaggio parametri della Ricerca
		setRequestAttribute("lsoggetto", soggetto);
		
		/* MEV_2025-52 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato */
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
		Vector lUffAcc = lUACon.ListaUfficiAccorpati("PM", codUfficioUtente);
		setRequestAttribute("elencoUfficiAccorpati", lUffAcc);
		/* MEV_2025-52 - FINE */

		Option lOption = new Option(DecodificheManager.getInstance().getNazioni());
		if (soggetto.getCodStatoNascita() != null) {
			lOption.setSelected(soggetto.getCodStatoNascita());
		}
		setRequestAttribute("StatoNascita", "" + lOption);
		//
		IIstruttoriaCumulo Ctrl = SIEPLookupRemote.getIstruttoriaCumuloRemote();

		// prende dalla sessione il codice dell'ufficio dell'utente connesso
		String ufficio = this.getCodUfficioUtenteConnesso();
		BigDecimal lCountRisultati = null;

		if (isRequestParameterNullObj("CountRisultati")) {
			/* MEV_2025-52 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato */
			//lCountRisultati = Ctrl.ExCountFascicoliBySoggettoProprioUfficioPaged(soggetto, ufficio, 0);
			lCountRisultati = Ctrl.ExCountFascicoliBySoggettoProprioUfficioPaged(soggetto, lFasModel, ufficio, 0);
		} else {
			lCountRisultati = getRequestBigDecimalParameter("CountRisultati");
		}

		Vector<FascicoloSiepModel> lFascicoliSoggetti = null;
		/* MEV_2025-52 - Aggiunta ricerca per chiaveAnno/chiaveProgr e ufficio accorpato */
		//lFascicoliSoggetti = Ctrl.ExRicercaFascicoliBySoggettoProprioUfficioPaged(soggetto, ufficio,
		//		Integer.parseInt(lPagina), lIdIstruttoriaCumulo);
		lFascicoliSoggetti = Ctrl.ExRicercaFascicoliBySoggettoProprioUfficioPaged(soggetto, lFasModel, ufficio,
				Integer.parseInt(lPagina), lIdIstruttoriaCumulo);

		if (lFascicoliSoggetti != null && lFascicoliSoggetti.size() > 0)
			siesLogger.debug("--XX-- ======================== >>>>   TROVATI  >" + lFascicoliSoggetti.size()
					+ "< Procedimenti legati alla Ricerca effettuata");

		// Passaggio Lista dei Fascicoli Trovati
		setRequestAttribute("ListaProcedimenti", lFascicoliSoggetti);

		// ==========================================================================
		// Recupero l'istruttoria da passare alla form
		// ==========================================================================
		/* IstruttoriaCumuloModel lIstruttoriaModel = */super.getDatiIstruttoria();

		// 26/04/2019 MEV70
		Vector<TitoloCumulatoModel> lListaTitoliInIstruttoria = null;
		lListaTitoliInIstruttoria = Ctrl.ExRicercaTitoliByIstruttoria(lIdIstruttoriaCumulo);
		setRequestAttribute("ListaTitoliInIstruttoria", lListaTitoliInIstruttoria);

		setRequestAttribute("CountRisultati", lCountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		return PG_LOAD_ELENCO_FASCICOLI_UFFICIO;
	}

}