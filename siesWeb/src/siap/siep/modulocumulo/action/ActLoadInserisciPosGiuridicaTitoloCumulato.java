package siap.siep.modulocumulo.action;

import f3b.util.F3BException;
import f3b.web.html.Option;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;

/***
 * Action per la load inserisci posizione giuridica
 * @author d.fiorletta
 *
 */
public class ActLoadInserisciPosGiuridicaTitoloCumulato extends ActionModuloCumulo implements ICostantiModuloCumulo
{

  public String processRequest() throws F3BException
  {    
    //==========================================================================
    // Recupero i dati del cumulo
    //==========================================================================
    super.getDatiIstruttoria();
    
    DatiFinaliCumuloAggregatoModel lDatiFinaliAggModel = super.getDatiFinaliCumuloAggregato();

    if (!isRequestParameterNullObj(MODALITA) && MODALITA_MODIFICA.equals(getRequestStringParameter(MODALITA))){
//    if (lDatiFinaliAggModel.getDatiFinaliCumulo().getCodPosizioneGiuridica()!=null){ 
      setRequestAttribute(ICostantiModuloCumulo.MODALITA, ICostantiModuloCumulo.MODALITA_MODIFICA);
    } 
    else {
      setRequestAttribute(ICostantiModuloCumulo.MODALITA, ICostantiModuloCumulo.MODALITA_INSERIMENTO);
    }

    
    //==========================================================================
    // Caricare qui i dati delle combo
    //==========================================================================
    // Posizioni Giuridiche   
    Option lComboPGLibero = new Option (DecodificheManager.getInstance().getPosizioniGiuridicheCumLibero());
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null)
      lComboPGLibero.setSelected(lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
    setRequestAttribute("posizioneGiuridicaLibero", ""+lComboPGLibero);    
    
    Option lComboPGEspIst = new Option (DecodificheManager.getInstance().getPosizioniGiuridicheCumEspIst());
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null)
      lComboPGEspIst.setSelected(lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
    setRequestAttribute("posizioneGiuridicaEspIst", ""+lComboPGEspIst);    

    Option lComboPGEspAltro = new Option (DecodificheManager.getInstance().getPosizioniGiuridicheCumEspAltro());
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null)
      lComboPGEspAltro.setSelected(lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodPosizioneGiuridica());
    setRequestAttribute("posizioneGiuridicaEspAltro", ""+lComboPGEspAltro);    
    
    // Ufficio Emittente
//    Option lComboUffici = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS());
    Option lComboUffici = new Option(DecodificheManager.getInstance().getTipoUfficio());
    lComboUffici.setFilter(new String[]{"TDS","UDS","TDSM","UDSM"});
    //FIXME da sostituire con getTipoUfficioSiusTDSMUDSM
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null && lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getUfficioSorv()!=null)
      lComboUffici.setSelected (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getUfficioSorv().getCodTipoUfficio());
    setRequestAttribute("tipoUfficioSIUS", "" + lComboUffici);  
    
    // Tipo Provvedimento
    Option lComboTipoProvv = new Option(DecodificheManager.getInstance().getTipoProvvSorveglianza());
    if (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo()!=null && lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodTipoProvvedimento()!=null)
      lComboTipoProvv.setSelected (lDatiFinaliAggModel.getPosizioneGiuridicaCumulo().getCodTipoProvvedimento());
    setRequestAttribute("tipoProvvedimento", "" + lComboTipoProvv);  
    
    
    
    return PG_LOAD_INSERISCI_POSIZIONE_GIURIDICA;
  }
  
}
