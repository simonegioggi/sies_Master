package siap.siep.sentenza.action;


import siap.sico.decodifiche.controller.DecodificheManager;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaSentenza extends ActionSiap implements ICostantiSentenza
{
  public String processRequest() throws Exception
  {
    Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIEPTrattino());
    setRequestAttribute("autoritaEsterna", "" + lOption );

	// MEV 15 - Revisione SIGE
	// Aggiunto parametro per identificare la funzione che richiama la maschera
	// di Ricerca Titolo Esecutivo da Ricerche.
	// Quando viene richiamata da SIGE sulla maschera viene inserito
    // il Calendario in corrispondenza di ogni campo data
    String codFunzione = getCodFunMenuVerticale();
	setRequestAttribute("codFunzione", codFunzione);

    return PG_LOAD_RICERCASENTENZA; //restituisce la jsp di VIEW
  }
}
