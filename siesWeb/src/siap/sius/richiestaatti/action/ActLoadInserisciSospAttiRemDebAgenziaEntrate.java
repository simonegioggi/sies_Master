package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import f3b.log.LogF3B;
import f3b.util.DateUtils;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import siap.sius.remissionedebito.controller.RichiestaRemissioneController;
import siap.sius.remissionedebito.model.RichiestaRemissioneModel;

/**
 * <p>
 * Title: ActLoadInserisciAccordiLavSostitutivo
 * </p>
 * <p>
 * Description: Classe di Azione responsabile della composizione dei dati per le combobox e ritorna la
 * chiamata alla corrispondente JSP. La classe estende la classe ActLoadRichiestaSanzSostDocumentiIstruttori
 * per il riuso di parti comuni.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciSospAttiRemDebAgenziaEntrate extends ActionSiap
		implements ICostantiRichiestaAtti {
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

	/**
	 * Metodo processRequest che prepara i dati necessari per la composizione della form, e ritorna come
	 * parametro la relativa JSP compresiva di path.
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

		String[] FiltroTipo1 = { "-", "38", "39" };

		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP"))
				.getFascicoloSiusModel().getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);// Imposta il valore in request.

		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoAutorita();

		Option lOption = new Option(lElencoTipiAutorita);

		// ( DESTINATARIO ) Uffici Recupero Crediti
		lOption = new Option(lElencoTipiAutorita);
		lOption.setFilter(FiltroTipo1);
		setRequestAttribute("ElencoTipiAutorita", "" + lOption); // Imposta il valore in request.

		// Verifica presenza di Provvedimenti di Riferimento a Richieste Remissione Debito

		String lProvvedimenti = "";

		// Lettura elenco Richieste di Remissione Collegate al Fascicolo SIUS.
		FascicoloGPModel lFasGPMod = (FascicoloGPModel) getSessionAttribute("fascicoloSiusGP");
		RichiestaRemissioneModel aRichiestaRemissione = new RichiestaRemissioneModel();
		aRichiestaRemissione.setFasSiuIdFascicoloSius(lFasGPMod.getFascicoloSiusModel().getIdFascicoloSius());
		RichiestaRemissioneController lCtrl = new RichiestaRemissioneController();
		Vector lVect = lCtrl.ExRicercaRichiesteRemissioneDebito(aRichiestaRemissione);

		if (lVect.size() > 0) {
			Iterator itx = lVect.iterator();
			while (itx.hasNext()) {
				RichiestaRemissioneModel richRemissione = (RichiestaRemissioneModel) itx.next();

				if ((richRemissione.getCodTipoProvvedimento() != null)
						&& (richRemissione.getCodTipoProvvedimento().compareTo("-") != 0)) {
					String lProvvedimento = new String();
					String emesso = new String(" emesso da ");

					lProvvedimento += richRemissione.getDescrTipoProvvedimento();
					if (richRemissione.getCodTipoProvvedimento().compareTo("01") == 0)
						emesso = " emessa da ";
					if (richRemissione.getDataEmissione() != null) {
						lProvvedimento += " del "
								+ DateUtils.getDateToString(richRemissione.getDataEmissione(), "dd-MM-yyyy");
					}
					if ((richRemissione.getCodAutoritaEmittenteProvv() != null)
							&& (richRemissione.getCodAutoritaEmittenteProvv().compareTo("-") != 0)) {
						lProvvedimento += emesso + richRemissione.getDescrAutoritaEmittenteProvv();
						if ((richRemissione.getCodLuogoEmittenteProvv() != null)
								&& (richRemissione.getCodLuogoEmittenteProvv().compareTo("-") != 0)) {
							lProvvedimento += " di " + richRemissione.getDescrLuogoEmittenteProvv();
						}
					}
					lProvvedimenti += lProvvedimento + "; ";
				}
			}
		}

		setRequestAttribute("EstremiProvvedimento", lProvvedimenti);
		//

		// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		// LogF3B.getLogger()
		siesLogger.debug(this.getClass().getName() + ".processRequest(): fine");

		return PG_LOAD_RICHIESTASOSPATTIREMDEBAGENZIAENTRATE; // restituisce la jsp di VIEW
	}

}