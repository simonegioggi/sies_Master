package siap.sico.webservice.action;

import f3b.util.DateUtils;
import it.mig.sies.type.ANAGRAFICADocument;
import it.mig.sies.type.DATIUTENTEDocument;

import java.math.BigDecimal;

import siap.siep.statoprocedimento.model.StatoProcedimentoModel;

public class ActNscToSiesLoadStatoProcedimento
{
  private String mCodUfficio="";
  
  public ActNscToSiesLoadStatoProcedimento(String aCodUfficio) 
  {
      mCodUfficio=aCodUfficio;
  }
  
  public StatoProcedimentoModel processRequest(ANAGRAFICADocument.ANAGRAFICA adatiAnagrafica, DATIUTENTEDocument.DATIUTENTE adatiUtente) throws Exception
  {
        StatoProcedimentoModel lStatoProcedimentoModel = new StatoProcedimentoModel();

        lStatoProcedimentoModel.setProgressivo(new BigDecimal(1));
        lStatoProcedimentoModel.setCodStatoProcedimento("0348");    // "0348" Iscritto da NSC" 
        lStatoProcedimentoModel.setData(null);
        
        lStatoProcedimentoModel.setCodOperatoreInserimento("nsc-"+adatiUtente.getUSERNAME().toString());
        lStatoProcedimentoModel.setDataInserimento(DateUtils.getSysDate());
        lStatoProcedimentoModel.setCodUfficioInserimento(mCodUfficio);
        
        return lStatoProcedimentoModel;
        
  } 

}
