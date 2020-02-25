package siap.sius.fascicolo.action;

import siap.sico.utente.model.UtenteModel;
import siap.sico.web.ActionSiap;

/**
 * <p>Title: ActLoadRichiestaCertificatoPenale</p>
 * <p>Description: Classe che permette di richiedere un certificato penale su NSC</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadRichiestaCertificatoPenale extends ActionSiap implements ICostantiFascicoloSius
{
   public String processRequest() throws Exception
   {
     
     UtenteModel lUtenteModel = this.getUtenteConnesso();

     setRequestAttribute("utente", lUtenteModel );

     return PG_LOAD_RICHIESTA_CERTIFICATO_PENALE;
   }
   
}