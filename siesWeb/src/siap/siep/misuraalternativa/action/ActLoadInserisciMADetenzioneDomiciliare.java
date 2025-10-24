package siap.siep.misuraalternativa.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.controller.IMisuraAlternativa;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>Title: ActLoadInserisciMADetenzioneDomiciliare</p>
 * <p>Description: Classe Action per la load inserisci di Concessione Detenzione Domiciliare</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public class ActLoadInserisciMADetenzioneDomiciliare extends ActConcessione
{
  public String processRequest() throws F3BException
  {
//tutti i controlli e la maggior parte delle request si trovano nel padre
//passo la posizione precedente per vedere se esiste il verbale o no
    String lRitorno = this.getConcessione("12");
    if(!lRitorno.equals(""))
      return lRitorno;

//setto il campo codice motivo
    Option lOption = new Option(DecodificheManager.getInstance().getMotivoProvvedimentoMADDom());
    setRequestAttribute("motivoProvv", "" + lOption);

    setRequestAttribute("tipoMisura", "DETENZIONE");

// Cerca gli Eventi "validati" con motivo "2005"
    // Instanzia il model dell'evento
    EventoModel lEvent = new EventoModel();

    // prende dalla Session l'ID del procedimento e lo carica nel model
    lEvent.setFasSieIdFascicoloSiep(((FascicoloSiepModel)getSessionAttribute("fascicolo")).getIdFascicoloSiep());
    // carica nel model il Tipo evento
    lEvent.setCodTipoEvento("01");
    // carica nel model il flag documento registrato
    lEvent.setFlagDocumentoRegistrato("S");
    // carica nel model il tipo motivo
    String[] lMotivo = {"2005"};
    // carica nel model il codice dei provvedimenti
    String[] lProvv = {"04","09","12"};

    // Ricerca nella tabella EVENTO
    IEvento lCtrlEvento = SICOLookupRemote.getEventoRemote();
    EventoModel lEve = lCtrlEvento.ExRicercaEventoPerMotivoPerProvv(lMotivo,lProvv,lEvent);

    // setta la risposta della ricerca nella request
    setRequestAttribute("eventoammissioneprovvisoria",lEve);

    if(lEve != null )
    {
      // Ricerca nella tabella MISURA ALTERNATIVA
      IMisuraAlternativa lCtrlMA = SICOLookupRemote.getMisuraAlternativaRemote();
      MisuraAlternativaModel lMA = lCtrlMA.ExRicercaMisuraAlternativaByIdEvento(lEve.getEveIdEvento());

      // setta la risposta della ricerca nella request
      setRequestAttribute("maammissioneprovvisoria",lMA);
    }

	// MEV 10 - filtro sui minorenni
	setRequestAttribute("filtroMinorenni", this.getFiltroMinorenni());
    
    return PG_LOAD_INSERISCI_MA_CONCESSIONE;
  }
}