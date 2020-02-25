package siap.sico.jms.action;

import siap.jms.jmscode.controller.JmsCodeController;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadListaMessaggiTrasmessi</p>
* <p>Description: Classe Action per la load della Lista Messaggi Inviati</p>
* <p>Copyright: Copyright (c) 2005</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadListaMessaggiTrasmessi extends ActionSiap implements ICostantiSicoJMS
{
  public String processRequest() throws Exception
  {

    this.setLinkRitorno();    // Imposta la combo dei Tipi di Operazioni.

    JmsCodeController lCrtl = new JmsCodeController();

    Option lOption = new Option( lCrtl.ExRicercaPerDominioEDescrizione("TIPO_OPERAZIONE", "TRASFERIMENTO" ) );
    setRequestAttribute( "tipoOperazione", "" + lOption );

    return PG_LOAD_LISTAMESSAGGITRASMESSI;  //restituisce la jsp di VIEW
  }
}