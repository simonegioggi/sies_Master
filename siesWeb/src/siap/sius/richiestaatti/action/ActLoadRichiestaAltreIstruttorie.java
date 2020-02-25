package siap.sius.richiestaatti.action;

import java.util.Collection;
import java.util.Date;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.DateUtils;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadRichiestaAltreIstruttorie
 * </p>
 * <p>
 * Description: Classe di Azione responsabile della composizione dei dati per le combobox e ritorna la chiamata alla
 * corrispondente JSP
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
public class ActLoadRichiestaAltreIstruttorie extends ActionSiap implements ICostantiRichiestaAtti {
	/**
	 * Metodo processRequest che prepara i dati necessari per la composizione della form, e ritorna come parametro la
	 * relativa JSP compresiva di path.
	 * <p>
	 * 
	 * @return pagina JSP da caricare
	 * @throws Exception
	 *             propaga qualunque errore di eccezione.
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {
		gestioneRitorno();

		// Data Fascicolo SIUS
		Date lDataInserimento = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel()
				.getDataInserimento();
		String lDataInserimentoString = DateUtils.getDateToString(lDataInserimento, "dd/MM/yyyy");
		setRequestAttribute("dataInsFS", lDataInserimentoString);// Imposta il valore in request.

		// ( DESTINATARIO 1 ) Elenco Uffici Giudiziari
		Collection lElencoUffGiudiz = DecodificheManager.getInstance().getTipoUfficio();
		Option lOption = new Option(DecodificheUtils.getDecodesWithoutCode(lElencoUffGiudiz, "SSPA"));
		setRequestAttribute("ElencoUffGiudiz", "" + lOption); // Imposta il valore in request.
		
		// MEV10-s3: aggiunto controllo su tipologia di ufficio connesso (solo "TDSM" e "UDSM")
		String codTipoUfficio = ((FascicoloGPModel) getSessionAttribute("fascicoloSiusGP")).getFascicoloSiusModel().
				getCodTipoUfficio();
		if ("UDSM".equals(codTipoUfficio) || "TDSM".equals(codTipoUfficio)) {
			Collection lCol = DecodificheManager.getInstance().getTipoAutorita();
			String[] lStringFilter  = new String[]{ "-", "40", "B5" };
			lOption = new Option(lCol);
			lOption.setFilter(lStringFilter);
			setRequestAttribute("ElencoDest3", "" + lOption);
		}

		// ( DESTINATARIO 4 - 5 ) Elenco di tutti i destinatari
		Collection lElencoTipiAutorita = DecodificheManager.getInstance().getTipoAutorita();
		lOption = new Option(lElencoTipiAutorita);
		setRequestAttribute("ElencoTipiAutorita", "" + lOption); // Imposta il valore in request.

		return PG_LOAD_RICHIESTAALTREISTRUTTORIE; // restituisce la jsp di VIEW
	}

}