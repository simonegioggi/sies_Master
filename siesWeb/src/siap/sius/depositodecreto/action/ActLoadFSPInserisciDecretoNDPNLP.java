package siap.sius.depositodecreto.action;

import siap.sico.web.ActionSiap;
import siap.sius.fascicolo.action.ICostantiFascicoloSius;

public class ActLoadFSPInserisciDecretoNDPNLP extends ActionSiap
implements ICostantiDepositoDecreto
{
    public String processRequest() throws Exception
    {
      // Imposta alla JSP il nome della funzione e l'azione
      // da chiamare alla conferma.
      setRequestAttribute("functionName", "Emissione Decreto NDP/NLP");
      setRequestAttribute("nextAction", "siap.sius.depositodecreto.action.ActLoadInserisciDecretoNDPNLP");

      return ICostantiFascicoloSius.PG_LOAD_RICERCAFSPUNTUALE;
    }
}

