package siap.siep.sanzionesostitutiva.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import siap.siep.pagoPA.controller.IBollettinoPagopa;
import siap.siep.pagoPA.model.BollettinoPagopaModel;
import siap.siep.util.SIEPLookupRemote;

/**
 * Title: ActElencoStatoPagamenti 
 * Description: Classe che mostra elenco stato pagamento bollettini PagoPA
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public class ActElencoStatoPagamenti extends ActionSiap implements ICostantiSanzioneSostitutiva {

	public String processRequest() throws Exception {

		BigDecimal idFascicolo = getRequestBigDecimalParameter(
				ICostantiEvento.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP);
		// Ricerca lo stato dei pagamenti per id fascicolo
		IBollettinoPagopa ibp = SIEPLookupRemote.getBollettinoPagopaRemote();
		Vector<BollettinoPagopaModel> elencoStatoPagamenti = ibp
				.ExRicercaBollettinoPagopaByFasSieIdFascicoloSiep(idFascicolo);
		setRequestAttribute("elencoStatoPagamenti", elencoStatoPagamenti);

		return PG_ELENCO_STATO_PAGAMENTI;
	}

}