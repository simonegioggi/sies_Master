package siap.siep.sanzionesostitutiva.action;

import siap.sico.web.ActionSiap;

public class ActLoadInserisciOrdineIngiunzione extends ActionSiap implements ICostantiSanzioneSostitutiva 
{
    public String processRequest() throws Exception {
        
        //TODO aggiungere i vari controlli e caricamento combo 
        
        return PG_LOAD_INSERISCI_ORDINE_INGIUNZIONE;
    }
    
}
