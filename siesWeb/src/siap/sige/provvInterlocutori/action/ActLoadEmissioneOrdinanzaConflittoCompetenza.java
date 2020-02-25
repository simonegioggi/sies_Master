package siap.sige.provvInterlocutori.action;

import siap.sige.fascicolo.action.ActLoadRicercaFSigePuntuale;
import siap.sige.provvedimento.action.ICostantiProvvedimentoSige;

public class ActLoadEmissioneOrdinanzaConflittoCompetenza extends ActLoadRicercaFSigePuntuale
implements ICostantiProvvedimentoSige
{
	public String processRequest() throws Exception
	{
		setRequestAttribute("functionName", "Ordinanza Conflitto di Competenza");
		setRequestAttribute("nextAction", "siap.sige.provvInterlocutori.action.ActLoadOrdinanzaConflittoCompetenza");
		// Si invoca il metodo della superclasse.
		String lPage = super.processRequest();
		return lPage;
	}
}