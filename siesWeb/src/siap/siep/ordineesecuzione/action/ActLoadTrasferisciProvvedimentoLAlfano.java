package siap.siep.ordineesecuzione.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadTrasferisciProvvedimentoLAlfano</p>
 * <p>Description: Trasferisce l' istanza relativa all'ordine esecuzione L. Alfano verso gli uffici di sorveglianza </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciProvvedimentoLAlfano extends ActionSiap implements ICostantiOrdineEsecuzione
{
  public String processRequest() throws Exception
  {
    BigDecimal lEveId =	getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

    //Insieme degli uffici destinatari
    //Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "UDS");
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepTDSMUDSM(), "-");

   if(!isRequestParameterNullObj("sedetribunale"))
     setRequestAttribute("sedetribunale", this.getRequestStringParameter("sedetribunale"));

   if(!isRequestParameterNullObj("titolo"))
    setRequestAttribute("titolo", this.getRequestStringParameter("titolo"));

    setRequestAttribute("uffici", "" + lOption);
    setRequestAttribute("IDEvento",lEveId.toString());

    return PG_LOAD_TRASFERISCI_PROVVEDIMENTO_LA;
  }

}