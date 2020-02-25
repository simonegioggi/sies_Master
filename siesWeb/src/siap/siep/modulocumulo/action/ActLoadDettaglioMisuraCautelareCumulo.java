package siap.siep.modulocumulo.action;

import java.math.BigDecimal;

import siap.siep.istitutodetenzione.controller.IIstitutoDetenzione;
import siap.siep.istitutodetenzione.model.IstitutoDetenzioneModel;
import siap.siep.modulocumulo.controller.IMisuraCautelareCumulo;
import siap.siep.modulocumulo.model.MisuraCautelareCumuloModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

public class ActLoadDettaglioMisuraCautelareCumulo extends ActionModuloCumulo implements ICostantiMisuraCautelareCumulo
{
  public String processRequest() throws F3BException 
  {
    super.getDatiIstruttoria();
    super.getDatiTitoloCumulato();
  
    
    BigDecimal idMisuraCautelare = getRequestBigDecimalParameter (CAMPO_ID_MISURA_CAUTELARE_CUMULO);
    

    IMisuraCautelareCumulo lCtrl = SIEPLookupRemote.getMisuraCautelareCumuloRemote();
    MisuraCautelareCumuloModel lMisura = lCtrl.ExRicercaMisuraCautelareCumuloById (idMisuraCautelare);
    
    setRequestAttribute("MisuraCautelareCumulo", lMisura);
    
    
    if (lMisura!=null && lMisura.getIstDetIdIstitutoDetenzione()!=null && !"".equals(lMisura.getIstDetIdIstitutoDetenzione()))
    {
      IIstitutoDetenzione lCtrlIstituto = SIEPLookupRemote.getIstitutoDetenzioneRemote();
      IstitutoDetenzioneModel lIstitutoModel = lCtrlIstituto.ExRicercaIstitutoDetenzioneByKey(lMisura.getIstDetIdIstitutoDetenzione());
      setRequestAttribute("IstitutoDetenzione", lIstitutoModel );
    }   
    
    return PG_DETTAGLIO_MISURA_CAUTELARE_CUMULO;
  }
}
