package siap.siep.richiesta.action;

/**
* <p>Title: ActRichiestaDepenalizzazione</p>
* <p>Description: Classe Action per l'inserimento della richiesta di Depenalizzazione</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/


import siap.sico.evento.model.EventoModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;

public class ActRichiestaDepenalizzazione extends ActRichiestaGE
                                          implements ICostantiRichiesta
{
  public String processRequest() throws Exception
  {
    //===============================================================
    // Recupero i dati dell'Annotazione
    //===============================================================
    AnnotazioneManualeModel lAnnMod = setAnnotazioneManuale();
    
    // MEV 37 - Inizio
    if(getRequestStringParameter("Radio_Depe_Ammi").equals("D") )
	{
    	lAnnMod.setCodTipoAnnotazione( DEPENALIZZAZIONE );
	}
    else if(getRequestStringParameter("Radio_Depe_Ammi").equals("I") )
    {	
    	lAnnMod.setCodTipoAnnotazione( ILLECITO_AMMINISTRATIVO );
    }	
    // MEV 37 - Fine
    
    // Richiesta al Giudice dell'Esecuzione 
    lAnnMod.setCodFonte      (getRequestStringParameter("CodFonte"));
    lAnnMod.setAnnoFonte     (getRequestBigDecimalParameter("AnnoFonte"));
    lAnnMod.setNumeroFonte   (getRequestStringParameter("NumeroFonte"));
    lAnnMod.setArticolo      (getRequestStringParameter("Articolo"));
    lAnnMod.setCodSottonumerazione(getRequestStringParameter("CodSottonumerazione"));
    lAnnMod.setComma    (getRequestStringParameter("Comma"));
    lAnnMod.setLettera  (getRequestStringParameter("Lettera"));
    lAnnMod.setNumero   (getRequestStringParameter("Numero"));

    lAnnMod.setCodDpr("-");
    
    //==============================================
    // Luigi 27-09-2005
    // Caricamento dati dell'evevnto di richiesta
    //==============================================
    EventoModel lEveMod = setEvento("0210");

    //lEveMod.setCodMotivo("0210"); // DEPENALIZZAZIONE
    lEveMod.setDataEmissione(lAnnMod.getDataRichiesta());

    String lPage = "";
    lPage = eseguiRichiesta("siap.siep.richiesta.action.ActLoadRichiestaDepenalizzazione",
                            lAnnMod, lEveMod);

    return lPage;
  }
}