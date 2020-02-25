package siap.sige.udienzamonocratica.action;

import java.math.BigDecimal;
import java.util.Vector;

import org.apache.log4j.Logger;

import siap.sige.SIGEException;
import siap.sige.aula.action.ICostantiAula;
import siap.sige.collegio.action.ICostantiCollegio;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.magistrato.action.ICostantiMagistrato;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienza.controller.IUdienzaSige;
import siap.sige.udienza.model.UdienzaSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
 * <p>
 * Title: ActInserisciUdienzaMonocraticaSige
 * </p>
 * <p>
 * Description: Classe Action per l'inserimento di UdienzaMonocraticaSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia S.p.A.
 * </p>
 * 
 * @version 1.0
 */
public class ActInserisciUdienzaMonocraticaSige extends ActionSige implements ICostantiUdienzaSige,
		ICostantiCollegio {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	private UdienzaSigeModel prepareModelInsertUdienzaMonocraticaFix() throws F3BException {
		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));
		if (!isRequestParameterNullObj(CAMPO_SEZ_ID_SEZIONE) && !"-".equals(getRequestStringParameter(CAMPO_SEZ_ID_SEZIONE)))
			lUdiMod.setCodIdSezioneUdienza(getRequestBigDecimalParameter(CAMPO_SEZ_ID_SEZIONE));
		lUdiMod.setCodGiudice(getRequestStringParameter(CAMPO_COD_GIUDICE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
	
		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
				lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));  
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA))
			lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));  
		lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));
		lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));

		// codice del giudice magistrato assegnatario
		if (!isRequestParameterNullObj("CodMagistrato"))
			lUdiMod.setCodMagistratoAss(getRequestStringParameter("CodMagistrato"));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());

		return lUdiMod;
	}

	private UdienzaSigeModel prepareModelUdienzaMonocratica() throws F3BException {
		// ==========================================================================
		// Recupero i dati presenti in maschera
		// n.b. eliminare o commentare i campi non presenti in maschera
		// es: chiave della tabella, date_ins, foreignkey...
		// ==========================================================================
		UdienzaSigeModel lUdiMod = new UdienzaSigeModel();

		lUdiMod.setDataUdienza(getRequestDateParameter(ICostantiUdienzaSige.CAMPO_ANNO_DATA_UDIENZA,
				ICostantiUdienzaSige.CAMPO_MESE_DATA_UDIENZA, ICostantiUdienzaSige.CAMPO_GIORNO_DATA_UDIENZA));
		lUdiMod.setCodGiudice(getRequestStringParameter(CAMPO_COD_GIUDICE));
		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE))
			lUdiMod.setCodIdAssistente(getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_COD_ID_ASSISTENTE));
		lUdiMod.setCodProcuratore(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_COD_PROCURATORE));
		lUdiMod.setLuogoUdienza(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_LUOGO_UDIENZA));

		// 20090402 - Commentato così come richiesto dall'amministrazione il 20090402 .
		// lUdiMod.setNumeroMaxFascicoli(
		// getRequestBigDecimalParameter(ICostantiUdienzaSige.CAMPO_NUMERO_MAX_FASCICOLI));

		lUdiMod.setOraInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_INIZIO));
		lUdiMod.setMinInizio(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_INIZIO));
		lUdiMod.setOraFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ORA_FINE));
		lUdiMod.setMinFine(getRequestStringParameter(ICostantiUdienzaSige.CAMPO_MIN_FINE));

		lUdiMod.setCodUfficioAppartenenza(getCodUfficioUtenteConnesso());
		lUdiMod.setDataInserimento(DateUtils.getSysDate());
		lUdiMod.setCodOperatoreInserimento(getCodUtenteConnesso());
		lUdiMod.setCodUfficioInserimento(getUfficioUtenteConnesso().getCodUfficio());
		
		// intervento per richieste su 11.1.2
		// devo recuperare anche idSezione e idAula
		if (!isRequestParameterNullObj(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE))
			lUdiMod.setCodIdSezioneUdienza(getRequestBigDecimalParameter(ICostantiCollegio.CAMPO_SEZ_ID_SEZIONE));
		if (!isRequestParameterNullObj(ICostantiAula.CAMPO_ID_AULA))
			lUdiMod.setCodIdAulaUdienza(getRequestBigDecimalParameter(ICostantiAula.CAMPO_ID_AULA));
		return lUdiMod;
	}

	/*****************************************************************************
	 * Azione di Inserimento del UdienzaMonocraticaSige
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 ****************************************************************************/
	@SuppressWarnings("rawtypes")
	public String processRequest() throws F3BException {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : inizio");

		FascicoloSigeEstesoModel mFasEsteso = null;
		if (!isSessionAttributeNullObj("FascicoloSigeEsteso"))
			mFasEsteso = getFascicoloSigeEstesoInSessione();
		BigDecimal lIdFasSige = null;
		if(mFasEsteso!=null)
			mFasEsteso.getFascicoloSige().getIdFascicoloSige();

		RedirectTo lRedir = new RedirectTo();
		lRedir.setPage(IWebConstants.PG_MAIN);
		lRedir.setAction("siap.sige.udienzamonocratica.action.ActLoadDettaglioUdienzaMonocraticaSige");

		UdienzaSigeModel lUdiMod = null;
		if (!isRequestParameterNullObj(FORM_DEF_COLLEGIO)) {
			lRedir.setParameter(FORM_DEF_COLLEGIO, getRequestStringParameter(FORM_DEF_COLLEGIO));
			lUdiMod = prepareModelInsertUdienzaMonocraticaFix();
		} else {
			// entro in else se sto arrivo qui da Inserimento Udienza Monocratica da Funzioni di Supporto
			lUdiMod = prepareModelUdienzaMonocratica();
		}

		// ===================================================
		// Recupera il controller ed effettua l'operazione di ricerca ed inserimento
		// ===================================================
		IUdienzaSige lCtrlUdi = SIGELookupRemote.getUdienzaSigeRemote();

		// qui dovrebbe entrare solo per MONOCRATICHE
		// [EC] 20171019: PRIMA DI INSERIRE L'UDIENZA, OCCORRE VERIFICARE CHE GIA' NE ESISTA QUALCUNA CON LE
		// STESSE CARATTERISTICHE(data udienza, rito monocratico, sezione, giudice ed ufficio appartenenza)

		Vector lVect = lCtrlUdi.ExRicercaUdienzaSige(lUdiMod);
		UdienzaSigeModel lUdiRetMod = new UdienzaSigeModel();
		if (lVect.size() == 0) {
			lUdiRetMod = lCtrlUdi.ExInserisciUdienzaSige(lUdiMod, lIdFasSige);
		}
		
		// se provengo da funzioni amministrative e l'udienza monocratica già 
		// esiste devo restitire un messaggio all'utente e NON INSERIRE L'UDIENZA MONOCRATICA
		if (isRequestParameterNullObj(FORM_DEF_COLLEGIO) && lVect.size() > 0) {
			 throw new SIGEException(SIGEException.USER_MESSAGE,
			 "L'udienza Monocratica che si sta provando ad inserire è già esistente!");
			 
		}
		// [EC] 20171019: PER SIZE > 0 PRENDO L'ULTIMA CREATA IN QUANTO LE UDIENZE SONO RESTITUITE ORDINATE
		// PERA DATA INSERIMENTO
		else if (lVect.size() > 0) {
			// devo recuperare l'udienza ritrovata
			lUdiRetMod = (UdienzaSigeModel) lVect.get(0);
		}
		
		 if(this.getParameter( ICostantiMagistrato.CAMPO_COD_MAGISTRATO ) != null){
			lRedir.setParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO , getRequestStringParameter(ICostantiMagistrato.CAMPO_COD_MAGISTRATO));
		}
		// else{
		// throw new SIGEException(SIGEException.USER_MESSAGE,
		// "Sono state trovate più udienze con i dati immessi! Situazione inconsistente. Possibile presenza di dati sporchi!");
		// // recupero l'ultima udienza inserita
		//
		// }

		// ======================================================================
		// Prepara la pagina di destinazione
		// Viene restituita la pagina di dettaglio con i dati appena inseriti
		// ======================================================================
		lRedir.setParameter(CAMPO_ID_UDIENZA_SIGE, lUdiRetMod.getIdUdienzaSige().toString());
		String lPage = lRedir.toString();

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(getClass().getName() + ".processRequest : fine");
		return lPage;
	}

}