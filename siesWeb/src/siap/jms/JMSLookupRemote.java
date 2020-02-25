package siap.jms;

import siap.jms.messaggio.controller.IMessaggio;
import f3b.util.F3BException;
import f3b.util.LookupClass;

public class JMSLookupRemote extends LookupClass
{

public static IMessaggio getMessaggioRemote() throws F3BException
  {
    Object lRef;
    IMessaggio lRemote;

    lRef = lookup("siap.jms.messaggio.controller.MessaggioController");
    lRemote = (IMessaggio)lRef;

    return lRemote;
  }
}