package siap.sige.aula.controller;

import java.math.BigDecimal;
import java.util.Vector;

import siap.sige.aula.model.AulaUdienzaModel;
import f3b.util.F3BException;

/**
* <p>Title: IAula</p>
* <p>Description: Classe Interfaccia Aula</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public interface IAula
{
  public AulaUdienzaModel ExInserisciAula ( AulaUdienzaModel aAula ) throws F3BException;
  
  public AulaUdienzaModel ExRicercaAulaByKey ( BigDecimal aIdAula, BigDecimal aIdSezione ) throws F3BException;

  public AulaUdienzaModel ExRicercaAulaByDescrizione ( BigDecimal aIdSezione, String descAula ) throws F3BException;
  
  public Vector <AulaUdienzaModel>ExRicercaAulaByIdSezione ( BigDecimal idSezione ) throws F3BException;

  public Vector <AulaUdienzaModel>ExRicercaAula ( AulaUdienzaModel aAula ) throws F3BException;

  public int ExGetNumRicercaAula ( AulaUdienzaModel aAula ) throws F3BException;
  
  public void ExCancellaAula ( BigDecimal idAula, BigDecimal idSezione ) throws F3BException;

  public AulaUdienzaModel ExModificaAula ( AulaUdienzaModel aCollegio ) throws F3BException;
  
  public AulaUdienzaModel ExRicercaAulaPredefinitaSezione (String idSezione) throws F3BException;
  
  public AulaUdienzaModel ExRicercaAulaByIdAula (BigDecimal idAula) throws F3BException;
  
}