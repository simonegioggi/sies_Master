package siap.sius.depositosentenza.action;

import siap.sius.provvedimento.action.ICostantiProvvedimento;

 /**
 * <p>Title: ActLoadModificaDataDepositoSentenza</p>
 * <p>Description: Classe Action per la modifica dei destinatari e data Deposito Sentenza</p>
 * <p>Company: Engineering S.p.A.</p>
 * @version 1.0
 */
 public class ActLoadModificaDataDepositoSentenza extends ActLoadInserisciDataDeposito
 	implements ICostantiDepositoSentenza, ICostantiProvvedimento
 {

   public ActLoadModificaDataDepositoSentenza()
   {
	   // Valorizzazione della pagina di ritorno
	   super(PG_LOAD_MODIFICA_DATA_DEPOSITO_SENTENZA);
   }

   public String processRequest() throws Exception
   {
	   return super.processRequest();
   }

 }