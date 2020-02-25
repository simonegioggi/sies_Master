package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciProvvedimentoLS</p>
 * <p>Description: Trasferisce l' istanza relativa all'ordine esecuzione LS verso il tribunale di sorveglianza </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciProvvedimentoLS extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws Exception
  {
    BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    //Insieme degli uffici destinatari
    //Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "-");
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiusTDSMUDSM(), "-");
    String[] lFiltro = new String[3];
    lFiltro[0] = "-";
    lFiltro[1] = "TDS";
    lFiltro[2] = "TDSM";

    lOption.setFilter(lFiltro);


   if(!isRequestParameterNullObj("sedetribunale"))
     setRequestAttribute("sedetribunale", this.getRequestStringParameter("sedetribunale"));

   if(!isRequestParameterNullObj("titolo"))
    setRequestAttribute("titolo", this.getRequestStringParameter("titolo"));

    setRequestAttribute("uffici", "" + lOption);
    setRequestAttribute("IDEvento",lEveId.toString());

    return PG_LOAD_TRASFERISCI_PROVVEDIMENTO_LS;
  }

}