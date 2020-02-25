package siap.sige.richiestaatti.action;


import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;

public class ActElencoAtti extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	  {
	    //super.processRequest(); //Processo la request della ActLoadRicercaFSPuntuale.
	    setRequestAttribute("nextAction", "siap.sige.richiestaatti.action.ActElencoAttiIstruttori" );
	    setRequestAttribute("functionName", "Elenco Atti Istruttori" );
	    return PG_LOAD_RICERCAFSIGEPUNTUALE; //restituisce la jsp di ricerca Fascicolo Sige.
	  }
}