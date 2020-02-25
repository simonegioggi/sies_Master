package siap.siepe.fascicolo.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.web.ActionSiap;
import siap.siepe.fascicolo.controller.IFascicoloSiepe;
import siap.siepe.fascicolo.model.FascicoloSoggAttModel;
import siap.siepe.util.SIEPELookupRemote;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

/**
 * <p>
 * Title: ActRicercaSoggettiConProcSiepe
 * </p>
 * <p>
 * Description: Ricerca Soggetti con Procedimenti SIEPE
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class ActRicercaSoggettiConProcSiepe extends ActionSiap implements ICostantiFascicoloSiepe {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		setLinkRitorno();
		SoggettoModel lSogMod = new SoggettoModel();

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		// Si Riempie il model del Soggetto.
		lSogMod.setCognome(getRequestStringParameter(ICostantiSoggetto.CAMPO_COGNOME));
		lSogMod.setNome(getRequestStringParameter(ICostantiSoggetto.CAMPO_NOME));
		// Impostazione Correzione Codice comune di nascita.
		if (!this.isRequestParameterNullObj(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)
				&& getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA).length() > 1) {
			ComuneModel lComMod = new ComuneModel(
					getCodComuneByDescr(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_COMUNE_NASCITA)));
			lSogMod.setCodComuneNascita(lComMod.getCodComune());
		}

		if (getRequestStringParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA).length() > 2)
			lSogMod.setDataNascita(getRequestDateParameter(ICostantiSoggetto.CAMPO_ANNO_DATA_NASCITA,
					ICostantiSoggetto.CAMPO_MESE_DATA_NASCITA, ICostantiSoggetto.CAMPO_GIORNO_DATA_NASCITA));

		if (!getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA).equals("-"))
			lSogMod.setCodStatoNascita(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_STATO_NASCITA));

		lSogMod.setPaternita(getRequestStringParameter(ICostantiSoggetto.CAMPO_PATERNITA));
		lSogMod.setCodCs(getRequestStringParameter(ICostantiSoggetto.CAMPO_COD_CS));

//		UtenteModel lUtenteMod = new UtenteModel(
//				(UtenteModel) getSessionAttribute(ICostantiSecurity.SESSION_UTENTE_CONNESSO));

		// Recupero informazioni per i filtri di ricerca.
		String strCodUfficioUtenteConnesso = getUfficioUtenteConnesso().getCodUfficio();
		String strCodTipoUfficio = getUfficioUtenteConnesso().getCodTipoUfficio();

		String lIncludeArchiviati = "";
		if (isRequestChecked(ICostantiFascicoloSiepe.CAMPO_INCLUDE_ARCHIVIATI))
			lIncludeArchiviati = "S";

		String lCodIncarico = getRequestStringParameter(CAMPO_COD_INCARICO);
		String lDescrIncarico = DecodificheUtils.getDescbyCode(DecodificheManager.getInstance()
				.getTipoIncaricoSiepe(), lCodIncarico);

		Date dataDal = (getRequestDateParameter(CAMPO_ANNO_DATA_INSERIMENTO, CAMPO_MESE_DATA_INSERIMENTO,
				CAMPO_GIORNO_DATA_INSERIMENTO));
		Date dataAl = (getRequestDateParameter(CAMPO_ANNO_DATA_AGGIORNAMENTO, CAMPO_MESE_DATA_AGGIORNAMENTO,
				CAMPO_GIORNO_DATA_AGGIORNAMENTO));

		// Chiama il controller.
		IFascicoloSiepe lFascSogCtrl = SIEPELookupRemote.getFascicoloSiepeRemote();
		Vector lFascicoliSoggetti = lFascSogCtrl.ExRicercaFascSiepeBySoggettoPagina(lSogMod,
				strCodUfficioUtenteConnesso, lIncludeArchiviati, lCodIncarico, dataDal, dataAl,
				Integer.parseInt(lPagina));

		String lReturnPage = "";

		// Estrazione model Soggetto e relativo inserimento nella request.
		// Utile per la JSP SintesiSoggetto.jsp
		if (lFascicoliSoggetti != null) {
			SoggettoModel lSoggetto = ((FascicoloSoggAttModel) lFascicoliSoggetti.get(0)).getSoggettoModel();
			setRequestAttribute("soggetto", lSoggetto);
		}

		// Settaggio dei criteri di ricerca.
		setRequestAttribute("ufUtConnesso", strCodUfficioUtenteConnesso);
		// setRequestAttribute("codDistretto", lCodDistretto );
		setRequestAttribute("lIncludeArchiviati", lIncludeArchiviati);
		setRequestAttribute("codIncarico", lCodIncarico);
		setRequestAttribute("descrIncarico", lDescrIncarico);
		setRequestAttribute("dataDalInCancelleria", DateUtils.getDateToString(dataDal, "dd/MM/yyyy"));
		setRequestAttribute("dataAlInCancelleria", DateUtils.getDateToString(dataAl, "dd/MM/yyyy"));
		setRequestAttribute("tipoUfficio", strCodTipoUfficio);

		// Paginazione
		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lFascSogCtrl.ExGetNumRicercaFascicoliBySoggetto(lSogMod,
					strCodUfficioUtenteConnesso, lIncludeArchiviati, lCodIncarico, dataDal, dataAl);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());
		// setRequestAttribute("fascicoli", lVect);

		// Setta la risposta nella request.
		setRequestAttribute("fascicoli", lFascicoliSoggetti);

		lReturnPage = ICostantiFascicoloSiepe.PG_RICERCASOGGETTICONPROCSIEPE;

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(" lReturnPage =  " + lReturnPage);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest: fine");

		return lReturnPage; // restituisce la jsp di VIEW
	}

}