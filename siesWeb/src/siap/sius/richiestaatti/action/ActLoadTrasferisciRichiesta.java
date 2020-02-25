package siap.sius.richiestaatti.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciRichiesta</p>
 * <p>Description: Trasferisce la richiesta di relazione CSSA </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: </p>
 * @author bull
 * @version 1.0
 */
public class ActLoadTrasferisciRichiesta extends ActionSiap implements ICostantiRichiestaAtti
{
	public String processRequest() throws Exception
	{

		BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		setRequestAttribute("IDEvento",lEveId.toString());

    // Destinatari UEPE.
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepe());
    setRequestAttribute("UEPE", "" + lOption);

		return PG_LOAD_TRASFERISCI_RICHIESTA;
  }

}
