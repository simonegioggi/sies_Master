package siap.siep.sospensione.action;

import f3b.util.F3BException;

/**
 * <p>Title: </p>
 * <p>Description: C</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciDifferimentoProvv extends ActLoadInserisciDifferimentoMaster
{
  public String processRequest() throws F3BException
  {
    setRequestAttribute(TIPO_DIFFERIMENTO, DIFFERIMENTO_PROV);
	setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());
    return super.processRequest();
  }
}