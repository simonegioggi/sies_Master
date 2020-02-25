/**
 * 
 */
package siap.siep.nuovaistanza.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;


/**
* <p>Title: ActLoadArchiviazioneManuale</p>
* <p>Description: Classe Action per la presentazione della form pel l'Archiviazione Manuale.</p>
* <p>Copyright: Copyright (c) 2011</p>
* <p>Company: Agile</p>
* @version 1.0
* @author Luigi
*/
public class ActLoadArchiviazioneManuale extends ActionSiap  implements ICostantiNuovaIstanza
{

	public String processRequest() throws Exception 
	{
		String lPage = PG_ARCHIVIAZIONE_MANUALE;
		
		if( isSessionAttributeNullObj("fascicolo") )
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Dati del Procedimento non in sessione !!");

		isFascicoloSiepDiCompetenza();

		//ricerca Istanza per il fascicolo in sessione
		FascicoloSiepModel lFascMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");
		
		if(lFascMod.getChiaveProgr().intValue()<90000)
    	  	throw new F3BException(F3BException.USER_MESSAGE, "Il procedimento corrente non è un Registro Istanza " + lFascMod.getChiaveAnno()+"/"+lFascMod.getChiaveProgr()+"<br>Impossibile procedere.");
		
		gestioneRitorno();
		
		// Fascicolo archiviato
		if (lFascMod.getCodStatoFascicolo().compareTo("01")==0)
		{
			// Se il Fascicolo già risulta Archiviato Manualmente viene visualizzato il dettaglio
			if (lFascMod.getCodMotivoArchiviazione().equalsIgnoreCase("11"))
			{
				setRequestAttribute("modalita", "dettaglio");
				setRequestAttribute("Modificabile", "SI");
			}else
				throw new F3BException(F3BException.USER_MESSAGE, "Il fascicolo risulta già archiviato!");
		}
		else
			setRequestAttribute("modalita", "inserimento");

		return lPage;
		 
	}


}