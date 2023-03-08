package siap.siep.sanzionesostitutiva.action;

import f3b.util.F3BException;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;

public class ActGrigliaRiscossionePP extends ActionSiap implements ICostantiSanzioneSostitutiva 
{
    public String processRequest() throws F3BException
    {
      if (this.isSessionAttributeNullObj("fascicolo"))
      {
        return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
      }

      //this.isEventoNonValidato();
      setRequestAttribute("strFunzione", "Gestione Riscossione Pene Pecuniarie");  
      //setRequestAttribute("lFlagSanzione",lFlagSanzione);
      
      return ICostantiSanzioneSostitutiva.PG_GRIGLIA_RISCOSSIONE_PP;
    }
}
