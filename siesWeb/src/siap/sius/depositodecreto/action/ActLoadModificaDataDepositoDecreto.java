package siap.sius.depositodecreto.action;


 /**
 * <p>Title: ActLoadModificaDataDepositoDecreto</p>
 * <p>Description: Classe Action per la modifica dei destinatari e data Deposito Decreto</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

 public class ActLoadModificaDataDepositoDecreto extends ActLoadInserisciDataDepositoDecreto
 //implements ICostantiDepositoDecreto, ICostantiProvvedimento
 {
   public String processRequest() throws Exception
  {
      mRetPage = PG_LOAD_MODIFICADATADEPOSITODECRETO;
      return super.processRequest();
  }
 }