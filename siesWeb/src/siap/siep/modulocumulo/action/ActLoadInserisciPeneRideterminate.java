package siap.siep.modulocumulo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * Action per la load inserimento/modifica dei dati delle pena rideterminate
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciPeneRideterminate extends ActionModuloCumulo implements ICostantiModuloCumulo
{
  public String processRequest() throws F3BException
  {
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    DatiFinaliCumuloAggregatoModel lDatiAggregati = super.getDatiFinaliCumuloAggregato();
    
    // Carico la combo LPU
    Option lComboLPU = new Option(DecodificheManager.getInstance().getTipoSanzioneSostitutivaLpu());
    if (lDatiAggregati!=null && lDatiAggregati.getUltSanSanSosLPU()!=null)
      lComboLPU.setSelected(lDatiAggregati.getUltSanSanSosLPU().getCodTipoLpu());
      
    setRequestAttribute("comboLPU",""+lComboLPU);
    
    
    if (!isRequestParameterNullObj(MODALITA) && MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))){
      // provengo del dettaglio e voglio andare in modifica
      setRequestAttribute (MODALITA, MODALITA_MODIFICA);
    }
    else {
      setRequestAttribute (MODALITA, MODALITA_INSERIMENTO);
    }
    
    return PG_LOAD_INSERISCI_PENE_RIDETERMINATE;
  }
}
