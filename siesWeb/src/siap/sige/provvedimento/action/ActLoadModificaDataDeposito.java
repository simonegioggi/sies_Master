package siap.sige.provvedimento.action;

 /**
 * <p>Title: ActLoadModificaDataDeposito</p>
 * <p>Description: Classe Action per la modifica dei destinatari e data Deposito </p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: </p>
 * @version 1.0
 */

 public class ActLoadModificaDataDeposito extends ActLoadInserisciDataDeposito
 {
   public String processRequest() throws Exception
  {
      mRetPage = PG_LOAD_MODIFICADATADEPOSITO;
      return super.processRequest();
  }
 }