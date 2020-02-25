package siap.sige.udienzaparti.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.sige.udienza.action.ICostantiUdienzaSige;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaprocedimento.action.ICostantiUdienzaProcedimentoSige;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserimentoParte</p>
 * <p>Description: Classe Action per la load Inserimento Parte Offesa/Civile associata ad una udienza</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
*/
public class ActLoadInserisciParteUdinza extends ActionSiap implements ICostantiPartiUdienza {

	/**
	  * Carica la form per l'inserimento delle Parti.
	  * <p>
	  * @return Nome della pagina JSP da visualizzare
	  * al termine dell'elaborazione.
	  * <p>
	  * @throws Exception propaga errore di eccezione.
	  */
	public String processRequest() throws Exception {

		// Prepara la pagina di destinazione
		String lPage = PG_LOAD_INSERISCI_PARTE_UDIENZA;

		// Gestione pulsante di ritorno
		gestioneRitorno();

		String idEventoUdienza = null;
		String idUdienzaSIGE = null;
		String idUdienzaProcedimentoSIGE = null;
		String codTipoParte = "";

		if (!isRequestParameterNullObj(CAMPO_COD_TIPO_PART)) {
			codTipoParte = getRequestStringParameter(CAMPO_COD_TIPO_PART);
		}

		if (!isRequestParameterNullObj(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE)) {
			idUdienzaProcedimentoSIGE = getRequestStringParameter(ICostantiUdienzaProcedimentoSige.CAMPO_ID_UDIENZA_PROCEDIMENTO_SIGE);
		}

		if (!isRequestParameterNullObj(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE)) {
			idUdienzaSIGE = getRequestStringParameter(ICostantiUdienzaSige.CAMPO_ID_UDIENZA_SIGE);
		}

		if (!isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA)) {
			idEventoUdienza = getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_EVENTO_UDIENZA);
		} else if( !isRequestParameterNullObj(ICostantiEvento.CAMPO_ID_EVENTO) ){
		    idEventoUdienza = getRequestStringParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		}

		Option lOption = new Option(DecodificheManager.getInstance().getSesso(), "M");
		setRequestAttribute("sesso", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getRagioneSociale(), "-");
		setRequestAttribute("ragioneSociale", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getProvincie(), "-");
		setRequestAttribute("province", "" + lOption);

		lOption = new Option(DecodificheUtils.getDecodesWithoutCode(DecodificheManager.getInstance().getNazioni(), "-"), "039");
		setRequestAttribute("nazioni", "" + lOption);
		setRequestAttribute("nazioniResidenza", "" + lOption);

		// Imposta Modalità Inserimento.
		setRequestAttribute("modalita", "I");
		setRequestAttribute("anagraficaParteUdienza", new AnagraficaPartiUdienzaModel());
		setRequestAttribute("idEventoUdienza", idEventoUdienza);
		setRequestAttribute("idUdienzaSige", idUdienzaSIGE);
		setRequestAttribute("idUdienzaProcedimentoSige", idUdienzaProcedimentoSIGE);
		setRequestAttribute("codTipoParte", codTipoParte);

		return lPage; // restituisce la jsp di VIEW
	}

}