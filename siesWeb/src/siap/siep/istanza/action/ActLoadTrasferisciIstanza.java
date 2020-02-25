package siap.siep.istanza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciIstanza</p>
 * <p>Description: Trasferisce le istanze verso il tribunale di sorveglianza </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciIstanza extends ActionSiap implements ICostantiIstanza
{
	public String processRequest() throws Exception
	{

		BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
    //Insieme degli uffici destinatari
		Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "-");
		String[] lFiltro = new String[1];
		lFiltro[0] = "TDS";

		lOption.setFilter(lFiltro);

	  setRequestAttribute("uffici", "" + lOption);
		setRequestAttribute("IDEvento",lEveId.toString());

		return PG_LOAD_TRASFERISCI_ISTANZA;
  }

}