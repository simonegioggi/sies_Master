package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;

/**
 * <p>
 * Title: ActLoadRichiestaSanzSostDocumentiIstruttori
 * </p>
 * <p>
 * Description: Classe di Azione responsabile della composizione dei dati per le combobox e ritorna la
 * chiamata alla corrispondente JSP. Tale classe viene ereditata dalle classi figlie, che hanno la
 * resposabilità d'impostare contestualmente azione e nome funzione.
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public abstract class ActLoadRichiestaSanzSostDocumentiIstruttori extends ActionSiap
		implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo processRequest che prepara i dati necessari per la composizione della form, e ritorna come
	 * parametro la relativa JSP comprensiva di path.
	 * <p>
	 * 
	 * @return pagina JSP da caricare
	 * @throws Exception
	 *             propaga qualunque errore di eccezione.
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): inizio");

		/**
		 * Elenco dei Tipi Autorità impostate come filtro. 92 Carabinieri 59 Carabinieri - Comando Compagnia
		 * 60 Carabinieri - Comando Provinciale 61 Carabinieri - Comando Regionale 28 Carabinieri - Comando
		 * Stazione 58 Carabinieri - Nucleo Operativo 19 Commissariato di P.S. 72 Guardia di Finanza - Comando
		 * Compagnia 73 Guardia di Finanza - Comando Provinciale 74 Guardia di Finanza - Comando Regionale 70
		 * Guardia di Finanza Nucleo Provinciale Polizia Tributaria 71 Guardia di Finanza Nucleo Regionale
		 * Polizia Tributaria 64 Interpol 66 Polizia Postale - Compartimento 67 Polizia Stradale -
		 * Compartimento 93 Polizia di Stato 69 Polizia di Stato - Settore Polizia di Frontiera 68 Polizia di
		 * Stato - Sezione Polizia Stradale 20 Questura
		 */

		String[] lFiltro = { "-", "92", "59", "60", "61", "28", "58", "19", "72", "73", "74", "70", "71",
				"64", "66", "67", "93", "69", "68", "20" };
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		setRequestAttribute("dataInsFS", DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy"));// Imposta
																									// il
																									// valore
																									// in
																									// request.

		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoAutorita();

		// ( DESTINATARIO 3 - 4 ) Tipi Autorità Filtrati
		Option lOption = new Option(lElencoTipiAutorita);
		lOption.setFilter(lFiltro);
		setRequestAttribute("ElencoFiltratoTipiAutorita", "" + lOption); // Imposta il valore in request.

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_RICHIESTASANZSOSTDOCUMENTIISTRUTTORI; // restituisce la jsp di VIEW
	}

}