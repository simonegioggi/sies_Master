package siap.sico.jms.action;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;

import siap.jms.SIAPSender;
import siap.jms.jmscode.action.ICostantiJmsCode;
import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.jms.controller.RicercaSICOJMSController;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.jms.action.ICostantiSiepJMS;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
 *
 * <p>Title: ActRicercaSoggettoAltreBDI</p>
 * <p>Description: Azione di ricerca di un soggetto specificato e dei suoi fascicoli
 * fuori distretto.</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull Italia S.p.A.</p>
 */
public class ActRicercaSoggettoAltreBDI extends ActionSiap implements ICostantiSiepJMS, ICostantiSoggetto {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Restituisce la descriizone di una nazione passandogli il codice
	 * sfruttando il decodifiche manager invece che il Database
	 * @param aCodice
	 * @return
	 */
	private String getDescrStatoNascitaByCod(String aCodice) {
		String lDescrizione = "";
		Option lOption = new Option(DecodificheManager.getInstance().getNazioni(), aCodice);
		if (lOption != null) {
			lOption.setFilter(aCodice);
			lDescrizione = lOption.getSelecteds()[0];
		}
		return lDescrizione;
	}

	private SoggettoModel getSoggetto(boolean fromDetail) throws F3BException {
		SoggettoModel lSogMod = new SoggettoModel();

		if (fromDetail) {

			BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO);
			ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
			lSogMod = lSogCtrl.ExRicercaSoggettoByKey(lId);

		} else {

			/* 20210601	MEV_Scheda-21 Correzione Comune Nascita per omonimie dei Comuni senza flag validità.
			String lComune = getRequestStringParameters(CAMPO_COD_COMUNE_NASCITA)[1];

			// parse della request
			if (lComune != null && lComune.length() > 1) {
				
				ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(lComune));
				lSogMod.setCodComuneNascita(lComMod.getCodComune());
				lSogMod.setDescrComuneNascita(lComMod.getDescrizione());
			} */
			String lComune = getRequestStringParameters(CAMPO_COD_COMUNE_NASCITA)[1];

			// parse della request
			if (lComune != null && lComune.length() > 1) {
				// Recupero dati del Comune di nascita
				ComuneModel lComMod;

				if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE)
						&& getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
					// se presente dal codice comune (e descrizione)
					lComMod = new ComuneModel(getDatiComuneByCodDescr(
							getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE),
							lComune));
				} else {
					// altrimenti dalla sola descrizione (rischio omonimi)
					lComMod = new ComuneModel(getDatiComuneByDescrOmonimia(
							getRequestStringParameter(CAMPO_COD_COMUNE_NASCITA)));
				}
				lSogMod.setCodComuneNascita(lComMod.getCodComune());
				lSogMod.setDescrComuneNascita(lComMod.getDescrizione());
			}
			

			// String lComuneEstero = getRequestStringParameters( CAMPO_DESC_COMUNE_NASCITA_ESTERO )[1];
			// if (lComuneEstero!=null) lSogMod.setDescComuneNascitaEstero(getRequestStringParameter(lComuneEstero));

			// riempie il model
			lSogMod.setCognome(getRequestStringParameters(CAMPO_COGNOME)[1].toUpperCase());
			lSogMod.setNome(getRequestStringParameters(CAMPO_NOME)[1].toUpperCase());

			// STUB 27/09/2005 I Campi di data nascita sono modificati.
			if (getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA2).length() > 2) {
				Date lDate = DateUtils.getDate(getRequestStringParameter(CAMPO_ANNO_DATA_NASCITA2), getRequestStringParameter(CAMPO_MESE_DATA_NASCITA2), getRequestStringParameter(CAMPO_GIORNO_DATA_NASCITA2));
				lSogMod.setDataNascita(lDate);
			}

			String lCodStatoNascita = getRequestStringParameters(CAMPO_COD_STATO_NASCITA)[1];
			if (!lCodStatoNascita.equals("-")) {
				String lDescrStato = getDescrStatoNascitaByCod(lCodStatoNascita);
				lSogMod.setCodStatoNascita(lCodStatoNascita);
				lSogMod.setDescrStatoNascita(lDescrStato);
			}

			lSogMod.setAttoNascita(getRequestStringParameter(CAMPO_ATTO_NASCITA));

			/* Il Codice che sul Database si Chiama AFIS è in realtà il CODICE CUI */
			String lCodCUI = getRequestStringParameters(CAMPO_COD_AFIS)[1];
			if (lCodCUI != null && lCodCUI.length() > 1)
				lSogMod.setCodAfis(lCodCUI);

			if (!isRequestParameterNullObj("tipoClasse")) {
				String[] lClassiFascicolo = this.getRequestStringParameters("tipoClasse");
				lSogMod.setClassiFascicolo(lClassiFascicolo);
			}

		}
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.info("Soggettoricercato = " + lSogMod);

		return lSogMod;
	}

	private String standardRequest(boolean fromDetail) throws Exception {

		SoggettoModel lSogMod = getSoggetto(fromDetail);

		String dalDettaglio = "N";
		if (fromDetail){
			dalDettaglio = "S";
		}
		setRequestAttribute("fromDetail", dalDettaglio);
		
		UfficioModel lBDIMittente = getUfficioByCodUfficio(getUfficioUtenteConnesso().getCodDistretto());

		RicercaSICOJMSController lCtrlMess = new RicercaSICOJMSController();
		MessaggioModel lMessage = lCtrlMess.ExSpedisciRichiestaRicerca(lSogMod);

		lMessage.setCodBdiMittente(lBDIMittente.getCodUfficio());
		lMessage.setDescrBdiMittente(lBDIMittente.getDescrComune());
		lMessage.setCodUfficioDestinatario("-");
		lMessage.setCodUfficioMittente(getCodUfficioUtenteConnesso());
		lMessage.setCodTipoMessaggio(RICHIESTA_RICERCA);
		lMessage.setCodTipoOperazione(RICERCA_SOGGETTO);
		lMessage.setCodiceUtenteMittente(this.getCodUtenteConnesso());
		lMessage.setDataInvio(DateUtils.getSysDate());

		SIAPSender lSender = new SIAPSender();

		// STUB 10/06/2005 gestione della ricerca soggetto su singola BDI.
		String lCodBDI = getRequestStringParameter(ICostantiJmsCode.CAMPO_CODICE);
		String lDescrBDI = getRequestStringParameter(ICostantiJmsCode.CAMPO_DESCRIZIONE);
		// gestione della ricerca soggetto su singola BDI.
		if (lCodBDI.compareTo("-") == 0)
			lSender.sendToMultipleBDI(lMessage);
		else {
			// Richiesta ricerca ad una sola BDI
			lMessage.setCodUfficioDestinatario(lCodBDI);
			lMessage.setCodBdiDestinataria(lCodBDI);
			lMessage.setDescrBdiDestinataria(lDescrBDI);
			lMessage.setDescrUfficioDestinatario(lDescrBDI);
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("\nBDI Unica a cui mandare = " + lCodBDI + "\n");
			// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
			siesLogger.info("\nBDI Descr a cui mandare = " + lDescrBDI + "\n");

			lSender.send(lMessage);
		}

		setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Richiesta di ricerca Soggetto sottomessa al Sistema!");

		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		// STUB 08/08/2005 lRedirigi.setAction("siap.sico.jms.action.ActRicercaEsitiSoggetto");
		// lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadRicercaFascicoloPerSoggetto");// STUB 08/08/2005
		lRedirigi.setAction("siap.sico.jms.action.ActLoadListaEsitiRicercaSoggAltreBDI");// STUB 08/08/2005
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		return IWebConstants.PG_MESSAGE;
	}

	public String processRequest() throws Exception {
		return standardRequest(!isRequestParameterNullObj("DalDettaglio"));
	}

}