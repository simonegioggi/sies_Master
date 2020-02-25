package siap.siep.avvocato.action;

/**
* <p>Title: ActRicercaDifensore</p>
* <p>Description: Classe Action per la Ricerca di un Difensore</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.web.ActionSiap;
import siap.siep.avvocato.controller.IAvvocato;
import siap.siep.avvocato.model.AvvocatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

@SuppressWarnings("rawtypes")
public class ActRicercaDifensore extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Ricerca di un Difensore
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	public String processRequest() throws F3BException {

		String lPagina = "1";
		if (!isRequestParameterNullObj(IWebConstants.NUM_PAGE))
			lPagina = getRequestStringParameter(IWebConstants.NUM_PAGE);

		IAvvocato lCtrl = SIEPLookupRemote.getAvvocatoRemote();
		Vector lVect = null;

		AvvocatoModel lAvvModRic = new AvvocatoModel();
		if (!this.isRequestParameterNullObj(CAMPO_COGNOME))
			lAvvModRic.setCognome(getRequestStringParameter(CAMPO_COGNOME).toUpperCase());
		if (!this.isRequestParameterNullObj(CAMPO_NOME))
			lAvvModRic.setNome(getRequestStringParameter(CAMPO_NOME).toUpperCase());
		if (!this.isRequestParameterNullObj(CAMPO_FORO))
			lAvvModRic.setForo(getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
		lAvvModRic.setCodUffAppartenenza(this.getCodUfficioUtenteConnesso());

		lAvvModRic.setFlagVisualizza(new BigDecimal(1));

		lVect = lCtrl.ExRicercaAvvocatoPaged(lAvvModRic, Integer.parseInt(lPagina));
		this.setRequestAttribute("avvocati", lVect);

		BigDecimal CountRisultati;
		if (isRequestParameterNullObj("CountRisultati")) {
			CountRisultati = lCtrl.ExGetCountAvvocati(lAvvModRic);
		} else
			CountRisultati = getRequestBigDecimalParameter("CountRisultati");

		setRequestAttribute("CountRisultati", CountRisultati);
		setRequestAttribute(IWebConstants.NUM_PAGE, lPagina);
		setRequestAttribute(IWebConstants.REQUEST_FOR_PAGING, getCompleteRequestURL());

		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_FORO)) {
			setRequestAttribute("foro",
					getRequestStringParameter(ICostantiAvvocato.CAMPO_FORO).toUpperCase());
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_COGNOME)) {
			setRequestAttribute("cognome",
					getRequestStringParameter(ICostantiAvvocato.CAMPO_COGNOME).toUpperCase());
		}
		if (!this.isRequestParameterNullObj(ICostantiAvvocato.CAMPO_NOME)) {
			setRequestAttribute("nome",
					getRequestStringParameter(ICostantiAvvocato.CAMPO_NOME).toUpperCase());

		}
		return PG_LISTA_DIFENSORI;
	}

}