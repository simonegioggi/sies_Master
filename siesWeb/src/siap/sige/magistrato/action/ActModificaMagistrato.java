package siap.sige.magistrato.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import java.util.ArrayList;

import f3b.web.IWebConstants;
import f3b.util.F3BException;
import f3b.web.RedirectTo;
import f3b.util.DateUtils;
import f3b.log.LogF3B;
import siap.sico.web.ActionSiap;
import siap.sige.SIGEException;
import siap.sige.magistrato.model.MagistratoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.magistrato.controller.IMagistrato;
import siap.sige.magistratosezione.model.MagistratoSezioneModel;

/**
 * <p>
 * Title: ActModificaMagistrato
 * </p>
 * <p>
 * Description: Classe Action per la modifica di Magistrato
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
public class ActModificaMagistrato extends ActionSiap implements ICostantiMagistrato {

	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  MagistratoModel lMagMod = new MagistratoModel();
  
  /**
   * Azione di Modifica del Magistrato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
   * @throws F3BException
   */
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : inizio");
    // riempie il model
    letturaDati();
    // chiama il controller
    IMagistrato lCtrl = SIGELookupRemote.getMagistratoRemote();
    //MagistratoModel lMagModRet = lCtrl.ExModificaMagistrato(lMagMod);
    
    //controllo se la sezione è valida
    int CountRisultati = lCtrl.ExGetNumRicercaMagistratoSezioneValida(lMagMod);
    MagistratoModel lMagSezModRet = null;
    boolean associataUdienza = lCtrl.controlloCollegamentoUdienza(lMagMod);
		// 20170914: [SG] aggiunto ulteriore controllo se trasferito (3 occorrenze)
		boolean trasferimentoMagistrato = "T".equals(lMagMod.getFlagStato());
    if(CountRisultati>0) {
    	//la sezione che voglio modificare esiste ed è valida 
    	//MagistratoSezioneModel magistratoSezioneModel = lCtrl.ExRicercaMagistratoMultiSezione(lMagMod);
    	//controllo se è presente un'udienza legata alla sezione
			if (associataUdienza && !trasferimentoMagistrato)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Impossibile modificare la Sezione! E' presente un'udienza");
    	lMagSezModRet = lCtrl.ExModificaMagistratoMultiSezioneValida(lMagMod); 
    } else if (lMagMod.getMagistratoSezioni() != null ){
    	//controllo se è presente un'udienza legata alla sezione
			if (associataUdienza && !trasferimentoMagistrato)
				throw new F3BException(F3BException.USER_MESSAGE,
						"Modifica impossibile: Il Magistrato è associato ad una Udienza.");
    	
    	lMagSezModRet = lCtrl.ExModificaMagistratoMultiSezione(lMagMod); 
			
			// Esegue inserimento della nuova sezione con i relativi Controlli
			// 20171013: [EC] L'INSERIMENTO DELLA SEZIONE NON SI FA PIU DALLA PAGINA DI MODIFICA!
			//lMagSezModRet = lCtrl.ExInserisciMagistratoMultiSezione(lMagMod, "M");
    } else {
    	// Esegue solo la modifica del Magistrato.
    	// caso in cui nella multiselect delle "Sezioni"
    	// non è presente alcuna sezione.
    	try {
    	    lMagSezModRet = lCtrl.ExModificaMagistrato(lMagMod);
    	} catch (SIGEException sigge) {
				if (!trasferimentoMagistrato)
					throw new F3BException(F3BException.USER_MESSAGE,
							"Modifica impossibile: Il Magistrato è associato ad una Udienza.");
    	}
    }

    //lMagModRet.setMagistratoSezioni(lMagSezModRet.getMagistratoSezioni());
	
    setRequestAttribute("magistrato", lMagSezModRet);

    //Prepara la pagina di destinazione.
    RedirectTo lRedir = new RedirectTo();
    lRedir.setPage(IWebConstants.PG_MAIN);
    lRedir.setAction("siap.sige.magistrato.action.ActLoadDettaglioMagistrato");
    lRedir.setParameter(CAMPO_COD_MAGISTRATO, lMagSezModRet.getCodMagistrato().toString());
	
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
    siesLogger.debug(getClass().getName() + ".processRequest : fine");
	    
    return lRedir.toString();
}

  /**
   * Lettura dei dati dalla request.
   * <p>
	 * 
	 * @throws F3BException
	 *             Propaga errore di eccezione.
   */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void letturaDati() throws F3BException {

    lMagMod.setCodMagistrato(getRequestStringParameter(CAMPO_COD_MAGISTRATO));
    lMagMod.setCognome(getRequestStringParameter(CAMPO_COGNOME));
    lMagMod.setNome(getRequestStringParameter(CAMPO_NOME));
    lMagMod.setFlagStato( getRequestStringParameter(CAMPO_FLAG_STATO));
    lMagMod.setEMailUfficio(getRequestStringParameter(CAMPO_E_MAIL_UFFICIO));
    lMagMod.setEMailPrivata(getRequestStringParameter(CAMPO_E_MAIL_PRIVATA));
    lMagMod.setNumCellulare(getRequestStringParameter(CAMPO_NUM_CELLULARE));
    lMagMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
    lMagMod.setDataInizioValidita(DateUtils.getSysDate());
    lMagMod.setDataFineValidita(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_VALIDITA,
				CAMPO_MESE_DATA_FINE_VALIDITA, CAMPO_GIORNO_DATA_FINE_VALIDITA));
    lMagMod.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lMagMod.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lMagMod.setDataAggiornamento(DateUtils.getSysDate());
    
    // Legge le sezioni di appartenenza
		String[] lSezioni = (isRequestParameterNullObj(CAMPO_SEZIONE) == true ? new String[0]
				: getRequestStringParameters(CAMPO_SEZIONE));
    
    ArrayList lArrayList = new ArrayList();
    MagistratoSezioneModel lModel = new MagistratoSezioneModel();

    //inizio controllo sezione  
    boolean sezioneSelezionata = isRequestParameterNullEmptyObj(CAMPO_SEZIONE);
    //fine controllo sezione

		// 20171012[EC]: pare che questa variabile è true solo se è selezionata un'unica sezione in pagina
    if (sezioneSelezionata) {
  	  // Sezione non seleziona dall'utente
  	  lModel = new MagistratoSezioneModel();
        lModel.setMagCodMagistrato(lMagMod.getCodMagistrato());
			// 20171012[EC]: imposto l'id dell'unica sezione presente
			if(lSezioni!= null && lSezioni.length == 1)
				lModel.setSezIdSezione(new BigDecimal(lSezioni[0]));
        lModel.setCodOperatoreInserimento(getCodUtenteConnesso());
        lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
        lModel.setDataInserimento(DateUtils.getSysDate());
        lModel.setDataInizioAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE,
					CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE, CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE));
        lModel.setDataFineAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE,
					CAMPO_MESE_DATA_FINE_ASSEGNAZIONE, CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE));
        lArrayList.add(lModel);
    } else {
    
			for (int i = 0; i < lSezioni.length; i++) {
				if (!lSezioni[i].equals("")) {
	        lModel.setMagCodMagistrato(lMagMod.getCodMagistrato());
	        lModel.setSezIdSezione(new BigDecimal(lSezioni[i]));
	        //lModel.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
	        lModel.setCodOperatoreAggiornamento(getCodUtenteConnesso());
	        lModel.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
	        lModel.setDataAggiornamento(DateUtils.getSysDate());
					lModel.setDataInizioAssegnazione(getRequestDateParameter(
							CAMPO_ANNO_DATA_INIZIO_ASSEGNAZIONE, CAMPO_MESE_DATA_INIZIO_ASSEGNAZIONE,
	                CAMPO_GIORNO_DATA_INIZIO_ASSEGNAZIONE));
	        lModel.setDataFineAssegnazione(getRequestDateParameter(CAMPO_ANNO_DATA_FINE_ASSEGNAZIONE,
							CAMPO_MESE_DATA_FINE_ASSEGNAZIONE, CAMPO_GIORNO_DATA_FINE_ASSEGNAZIONE));
	        
	        lArrayList.add(lModel);
	      }
	    } // end for( int i=0; i<lSezioni.length; i++ )
    
    } 
    
    if( lArrayList.size() != 0 )
			lMagMod.setMagistratoSezioni((MagistratoSezioneModel[]) lArrayList
					.toArray(new MagistratoSezioneModel[0]));
  }
}