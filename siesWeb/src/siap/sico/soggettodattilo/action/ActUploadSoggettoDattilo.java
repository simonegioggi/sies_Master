package siap.sico.soggettodattilo.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;

/**
* <p>Title: ActInserisciSoggettoDattilo</p>
* <p>Description: Classe Action per l'inserimento di SoggettoDattilo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActUploadSoggettoDattilo extends ActionSiap implements ICostantiSoggettoDattilo {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	protected String getNomeFile(String field) throws Exception {
		String fileName = "";
		String fileCompleto = getRequestMultipart().getName(field);
		if (fileCompleto != null) {
			int indexDos = fileCompleto.lastIndexOf("\\");
			int indexUnix = fileCompleto.lastIndexOf("/");
			if (indexDos >= 0) {
				fileName = fileCompleto.substring(indexDos + 1);
			} else if (indexUnix >= 0) {
				fileName = fileCompleto.substring(indexUnix + 1);
			} else {
				fileName = fileCompleto;
			}
		}
		return fileName;
	}

	/**
	 * La funzione esegue l'Update della tabella EVENTO con o senza l'inserimento
	 * del documento di upload nel BLOB.
	 * @param aId
	 * @throws Exception
	 */
	protected void updateTabella(BigDecimal aCod, ByteArrayInputStream mInStr) throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): inizio");

		// Valorizzazione del Model
		SoggettoDattiloModel lModel = new SoggettoDattiloModel();
		lModel.setCodSoggetto(aCod);
		if (!isRequestParameterNullObj(CAMPO_DOC_TIPO))
			lModel.setDocTipo(getRequestStringParameter(CAMPO_DOC_TIPO));
		if (mInStr != null) {
			lModel.setDocBlobIn(mInStr);
			lModel.setDocNome(getNomeFile(CAMPO_BLOB));
		}

		lModel.setDataInserimento(DateUtils.getSysDate());
		lModel.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
		lModel.setCodOperatoreInserimento(getCodUtenteConnesso());

		ISoggettoDattilo mEveCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
		mEveCtrl.ExUpdateDocument(lModel);
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".updateTabella(): fine");
		return;
	}

	/**
	* Azione di Inserimento del SoggettoDattilo
	* @return Nome della pagina JSP da visualizzare
	* al termine dell'elaborazione
	* @throws F3BException
	*/
	public String processRequest() throws Exception {

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): inizio");
		String lPage = IWebConstants.PG_MESSAGE;

		// Lettura Cod Soggetto
		BigDecimal lCod = getRequestBigDecimalParameter(CAMPO_COD_SOGGETTO);

	    UfficioModel lUfficio = this.getUfficioUtenteConnesso();
	    String codUfficio = lUfficio.getCodUfficio();
	    FascicoloSiepModel lFasc = null;
		
	    if( !isSessionAttributeNullObj("fascicolo") )
		{
	        lFasc = new FascicoloSiepModel ((FascicoloSiepModel)getSessionAttribute("fascicolo"));
		}
		
	    // L'utente potrà inserire allegati solo nel procedimento iscritto dal proprio ufficio
		if(codUfficio != null && !codUfficio.equals("") && lFasc != null && lFasc.getCodUfficioInserimento() != null){
			if(!codUfficio.equals(lFasc.getCodUfficioInserimento())){
				setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Non è possibile procedere con l'inserimento. Fascicolo appartenente ad altro ufficio.");
			} else {

				InputStream lInput = getFile(CAMPO_BLOB);
				if (lInput != null && lInput.available() > 0) {
					byte[] lBuffer = new byte[lInput.available()];
					lInput.read(lBuffer);
					ByteArrayInputStream mInStr = new ByteArrayInputStream(lBuffer);
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.debug("BYTE ARRAY INPUT LENGTH >>> " + mInStr.available());
					updateTabella(lCod, mInStr);
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Aggiornamento Documento Avvenuto Correttamente!");
				} else {
					// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
					siesLogger.warn("File di Upload non disponibile!");
					setRequestAttribute(IWebConstants.MESSAGE_TEXT, "File di Upload non disponibile!");
				}
			}

		// MEV_10_SIUS - Inizio Modifica del 18/02/2016 di S.C.
		// sono nel caso di dettaglio soggetto senza fascicolo in sessione
		// pertanto non viene permesso all'utente di allegare il documento
		} else {
			setRequestAttribute(IWebConstants.MESSAGE_TEXT, "Non è possibile procedere con l'inserimento. Ricercare prima il Fascicolo al quale si vuole allegare il documento.");
		}
		// MEV_10_SIUS - Fine Modifica del 18/02/2016 di S.C. 

		// Se c'è lo stack di ritorno effettua un ritorno in cima
		// String lRitorno = goToRitorno();
		// if (lRitorno == null && !isRequestParameterNullObj(CAMPO_AZIONE_DETTAGLIO)) {
		RedirectTo lRedirigi = new RedirectTo();
		lRedirigi.setPage(IWebConstants.PG_MAIN);
		lRedirigi.setAction("siap.sico.soggetto.action.ActLoadDettaglioSoggetto");
		lRedirigi.setParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO, lCod.toString());
		// TipoOperazione = 'A' corrispondente al pulsante "Allegare Documento"
		// presente nella pagina del Dettaglio Soggetto (visibile solo agli utenti Siep)
		//lRedirigi.setParameter("TipoOperazione", "A");
		// if (!isSessionAttributeNullObj(IWebConstants.STACK_RITORNO))
		// lRedirigi.setParameter(IWebConstants.LINK_RITORNO, "");
		// }
		setRequestAttribute(IWebConstants.GOTO_PAGE, "" + lRedirigi);

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
		siesLogger.debug(this.getClass().getPackage().getName() + ".processRequest(): fine");
		return lPage;
	}

}