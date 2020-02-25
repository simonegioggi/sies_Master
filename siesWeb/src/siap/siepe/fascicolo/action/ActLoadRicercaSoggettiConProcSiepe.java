package siap.siepe.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.dao.DAOException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadRicercaSoggettiConProcSiepe</p>
 * <p>Description: Azione di caricamento della form di ricerca dei Soggetti con Procedimenti di Esecuzione Esterna</p>
 * <p>Copyright: Bull Italia Copyright (c) 2006</p>
 * <p>Company: Bull Italia</p>
 */

public class ActLoadRicercaSoggettiConProcSiepe extends ActionSiap implements ICostantiFascicoloSiepe
{
  public String processRequest() throws Exception
  {
    Option lOption = new Option( DecodificheManager.getInstance().getNazioni(), "-");
    setRequestAttribute("nazioni", "" + lOption );

    try
    {
      lOption = new Option( DecodificheManager.getInstance().getTipoIncaricoSiepe(), 75);
      lOption.setValueBlankItem("-");
      lOption.setAddBlankItem(true);
      setRequestAttribute("incarico", "" + lOption );
    }
    catch (Exception e)
    {
      throw new DAOException(e.toString());
    }

    return PG_LOAD_RICERCASOGGETTICONPROCSIEPE; //restituisce la jsp di VIEW
  }
}
