package siap.siepe.ricezioneatti.action;

import java.math.BigDecimal;

import siap.jms.messaggio.action.ICostantiMessaggio;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;

/**
* <p>Title: ActLoadRestituzioneAttoRicevuto</p>
* <p>Description: Classe Action per la load Restituzione Atti Ricevuti</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActLoadRestituzioneAttoRicevuto extends ActionSiap implements ICostantiRicezioneAtti
{
public String processRequest() throws F3BException

  {
    BigDecimal lIdMess = getRequestBigDecimalParameter(ICostantiMessaggio.CAMPO_ID_MESSAGGIO);
    //String codTipoOperazione = getRequestStringParameter("CodTipoOperazione");

    this.setRequestAttribute(ICostantiMessaggio.CAMPO_ID_MESSAGGIO, lIdMess);
    this.setRequestAttribute("IdMessaggio", lIdMess);
    this.setRequestAttribute("CodTipoOperazione",this.getRequestStringParameter("CodTipoOperazione"));

    if  (!isRequestParameterNullObj("nextAction"))
      setRequestAttribute("nextAction", getRequestStringParameter("nextAction"));
    else
      setRequestAttribute("nextAction", "siap.siepe.ricezioneatti.action.ActRestituzioneAttoRicevuto");

    return PG_RESTITUZIONE_ATTO_RICEVUTO;
  }
}
