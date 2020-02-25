package siap.sige.sentenza.action;

import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;

public class ActRicercaFSPIscrizioneSenDecSenDelib extends ActRicercaFSigePuntuale implements ICostantiFasSigeSentenza{
  public String processRequest() throws Exception {
      //  Passando il parametro noQuery non effettua nuovamente la ricerca
      if( isRequestParameterNullObj( "noQuery") )
          super.processRequest();

      // Bottone di ritorno
      setLinkRitorno();
      return PG_LOAD_ASSEGNA_SENTENZA;
  }
}