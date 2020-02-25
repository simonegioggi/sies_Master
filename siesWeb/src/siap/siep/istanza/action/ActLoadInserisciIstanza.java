package siap.siep.istanza.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciIstanza</p>
* <p>Description: Classe Action per la load inserisci di Istanza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadInserisciIstanza extends ActionSiap implements ICostantiIstanza
{
  public String processRequest() throws Exception
  {
    //Insieme delle nazioni
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "039");
    setRequestAttribute("nazioni", "" + lOption );

    //Nazionalità
    lOption = new Option( DecodificheManager.getInstance().getNazionalita(), "I");
    setRequestAttribute("nazionalita", "" + lOption );

    //Insieme delle autorità emittenti
    lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), "-");
    setRequestAttribute("autoritaEmi", "" + lOption );

    //Sesso
    lOption = new Option( DecodificheManager.getInstance().getSesso(), "M");
    setRequestAttribute("sesso", "" + lOption );

    //Flag Data Nascita Presunta
    lOption = new Option( DecodificheManager.getInstance().getFlagSN(), "N");
    setRequestAttribute("dataNascitaPresunta", "" + lOption );

    //Oggetto dell'istanza
    lOption = new Option( DecodificheManager.getInstance().getOggettoProcedimentoTDS());
    setRequestAttribute("contenuto", "" + lOption );

    setRequestAttribute("modalita", "I");

    return PG_LOAD_INSERISCIISTANZA;  //restituisce la jsp di VIEW
  }
}