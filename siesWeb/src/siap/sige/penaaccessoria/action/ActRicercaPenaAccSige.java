package siap.sige.penaaccessoria.action;

import java.math.BigDecimal;

import siap.siep.penaaccessoria.action.ActRicercaPenaAccessoria;
import siap.sige.penaaccessoria.model.PenaAccSigeModel;
import siap.sige.sentenza.action.ICostantiFasSigeSentenza;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;


/**
* <p>Title: ActRicercaPenaAccessoria</p>
* <p>Description: Classe Action per la ricerca di PenaAccessoria</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActRicercaPenaAccSige extends ActRicercaPenaAccessoria 
{

	public String processRequest() throws Exception
	{
		String lPage = PG_RICERCAPENAACCESSORIA;
		
		// Gestione ritorno
		setLinkRitorno();

		// Lettura dalla request dell'ID dell'aggregato FascicoloSige_Sentenza dalla sessione 
		BigDecimal lIdFasSigeSen  = (BigDecimal) getSessionAttribute(ICostantiFasSigeSentenza.CAMPO_ID_FAS_SIGE_SENTENZA);
		
		// Pena Accessoria Sige per il passaggio della condizione di ricerca
		PenaAccSigeModel lPenaAccessoriaSige = null;
		lPenaAccessoriaSige = new PenaAccSigeModel();
		lPenaAccessoriaSige.setFasSigeSenId(lIdFasSigeSen);
     
		// Utilizzo della funzione di ricerca ereditato dall'ancestor
		try
		{
			ricercaPeneAccessorieBenefici(lPenaAccessoriaSige);
		}
		catch(F3BException fe)
		{
			// Viene intercettato il messaggio di "elementi non trovati"
			if (fe.getErrorCode() != F3BException.USER_MESSAGE)
				throw fe;
		}

		setRequestAttribute("modalita", "R");
		setRequestAttribute("modo", "SIGE");
		modificabilita();

		return lPage;
	}
	
	/**
	 * La modificabilità del Fascicolo Sige in sessione 
	 * abilità la funzione di inserimento di una nuova 
	 * pena accessoria
	 * @throws Exception
	 */
	private void modificabilita() throws Exception 
	{
		ActionSige lActSige = new ActionSige( this);
		
		// Modificabilità della Pena Accessoria 
		String lpenaSigeModificabile = "SI";
				
		if ( lActSige.IsFascicoloSigeModificabile())
			lpenaSigeModificabile = "SI";
		else
			lpenaSigeModificabile = "NO";

		setRequestAttribute("Cancellabile", lpenaSigeModificabile);
		setRequestAttribute("Modificabile", lpenaSigeModificabile);
			
	 }

 }