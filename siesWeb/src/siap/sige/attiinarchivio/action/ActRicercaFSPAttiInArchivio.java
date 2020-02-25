package siap.sige.attiinarchivio.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.model.EventoModel;
import siap.sige.SIGEException;
import siap.sige.fascicolo.action.ActRicercaFSigePuntuale;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.web.html.Option;

public class ActRicercaFSPAttiInArchivio extends ActRicercaFSigePuntuale
implements ICostantiAttiInArchivio
{
  public String processRequest() throws Exception
  {
    String page=PG_LOAD_INSERISCI_DATA_INVIO_ATTI_IN_ARCHIVIO;

    //  Passando il parametro noQuery non effettua nuovamente la ricerca
    if( isRequestParameterNullObj( "noQuery") )
      super.processRequest();
    
    // 30//11/2018 aggiungo blocco su richiesta Nunzia (email del 29/11/2018 - Unificazione procedimento.docx)
    if (IsFascicoloUnificato())
		throw new SIGEException(SIGEException.USER_MESSAGE,
				"Non è possibile procedere con la funzione Atti in Archivio per questo Procedimento!");
    
    // Bottone di ritorno
    setLinkRitorno();
    
    Option lOption = new Option(DecodificheManager.getInstance().getTipoAttiInArchivio());
    lOption.setAddBlankItem(true);
    lOption.setValueBlankItem("-");
    lOption.setSelected("-");
    
	setRequestAttribute("tipoAtti", lOption.toString());
	
	FascicoloSigeEstesoModel lFascicoloEsteso = getFascicoloSigeEstesoInSessione();
	
	 
	IFascicoloSige lFasCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    EventoModel evento = lFasCtrl.ExRicercaDataInvioAtti(lFascicoloEsteso.getFascicoloSige().getIdFascicoloSige());
    super.setRequestAttribute("lEve", null);
    if (evento != null) {
    	super.setRequestAttribute("lEve", evento);
    	page = PG_DETTAGLIO_INSERISCI_DATA_INVIO_ATTI_IN_ARCHIVIO;
    }
    return page;
  }
}