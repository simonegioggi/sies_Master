package siap.sius.statistiche.action;


/**
 * <p>Title: ActLoadRicercaPerImpugnazione</p>
 * <p>Description: Visualizza la form di "Ricerca procedimeno per estremi Ricorso/Impugnazione".
 * Poichè la jsp utilizzata è la stessa per la "Ricerca procedimento per estremi ordinanza" 
 * il titolo della funzione viene passato nella requesy.</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

public class ActLoadRicercaProcDataUdienzaFissataNonDefiniti extends ActionSiap implements ICostantiStatistiche {
	public String processRequest() throws Exception {

		// Lista delle poszioni guiridiche
		Option lOption = new Option(DecodificheManager.getInstance().getPosizioneGiuridica());
		setRequestAttribute("posizioniGiuridiche", lOption.toString());  

		// Lista degli oggetti procedimento
		lOption = new Option(DecodificheManager.getInstance().getOggettoProcedimento());
		setRequestAttribute("oggettiProcedimento", lOption.toString());

		return PG_LOAD_RICERCA_PROC_DATA_UDI_FISS_NO_DEF;
	}
}
