package siap.siep.calcolopenadl92.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.siep.calcolopena.model.SemestreDL92Model;
import siap.siep.calcolopenadl92.model.CalcoloPenaDL92ModelDB;

/**
 * Controller di accesso alla tabella CALCOLO_PENA_DL92
 * 
 * @since MEV_2026-1
 */
public interface ICalcoloPenaDL92 {
  //public CalcoloPenaDL92ModelDB ExInserisciCalcoloPenaDL92 (CalcoloPenaDL92ModelDB aCalcoloPena) throws F3BException;
  
  public CalcoloPenaDL92ModelDB ExInserisciCalcoloPenaDL92 (CalcoloPenaDL92ModelDB aCalcoloPenaModel
          , SemestreDL92Model aSemestrePresofferto, Vector <SemestreDL92Model> aListaSemestri ) throws F3BException;

  public void ExCancellaCalcoloPenaDL92 (BigDecimal aIdCalcoloPena) throws F3BException;

  public CalcoloPenaDL92ModelDB ExRicercaCalcoloPenaDL92ById (BigDecimal aIdCalcoloPena) throws F3BException;

  public Vector<CalcoloPenaDL92ModelDB> ExRicercaCalcoloPenaDL92ByIdFas(BigDecimal aIdFascicolo) throws F3BException;
  
  public CalcoloPenaDL92ModelDB GetDettaglioStoricoById(BigDecimal aIdCalcoloPena) throws F3BException;
  
  public CalcoloPenaDL92ModelDB GetLastCalcoloDL92(BigDecimal aIdFascicolo) throws F3BException;
}
