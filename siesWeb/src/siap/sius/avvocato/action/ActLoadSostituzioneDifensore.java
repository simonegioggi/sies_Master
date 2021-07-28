package siap.sius.avvocato.action;

/**
 * <p>Title: ActLoadSostituzioneDifensor</p>
 * <p>Description: Classe Action per l'inserimento di Avvocato</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sius.avvocato.controller.IAvvocato;
import siap.sius.avvocato.model.AvvocatoModel;
import siap.sius.avvocato.model.AvvocatoSiusModel;
import siap.sius.util.SIUSLookupRemote;

public class ActLoadSostituzioneDifensore extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento del Avvocato
	 * 
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 *         <p>
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		IAvvocato lCtrl = SIUSLookupRemote.getAvvocatoRemote();
		Vector avvocati = new Vector();
		AvvocatoSiusModel avvocato = new AvvocatoSiusModel();

		//// [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di
		//// LogF3B.getLogger()
		// siesLogger.debug("RICERCA PER ID");

		AvvocatoModel lmModelAppo = new AvvocatoModel();
		BigDecimal id = new BigDecimal(this.getRequestStringParameter("tipo"));
		lmModelAppo.setIdAvvocato(id);

		avvocati = lCtrl.ExRicercaAvvocatoFascicoloSiusByKeyAvvocato(id);
		avvocato = (AvvocatoSiusModel) avvocati.get(0);
		if (getRequestStringParameter("numeroDifensori").equals("2")) {
			if (avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("D'UFFICIO")
					|| avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("DELLA FASE DI GIUDIZIO"))
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
		}
		
		// MEV_21: si passa l'avvocato con nome differente per evitare il caricamento in form 
		//setRequestAttribute("avvocato", avvocato);
		setRequestAttribute("avvocatoVecchio", avvocato);
		
		Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
		setRequestAttribute("tipoAvvocato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaDif", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
		setRequestAttribute("motivoDesignazione", "" + lOption);

		this.gestioneRitorno();

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		// IAvvocato lCtrl1 = SIUSLookupRemote.getAvvocatoRemote();
		// Vector lVect = lCtrl1.ExRicercaForo();
		// this.setRequestAttribute("foro", lVect);

		UfficioModel lUffUte = this.getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		this.setRequestAttribute("comune", lDescrComune);

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.

		String lStatoForo = DecodificheUtils.getCodAltebyCode(DecodificheManager.getInstance().getForoAll(),
				avvocato.getAvvocato().getForo());

		if ("SOPPRESSO".equals(lStatoForo)) {
			lOption = new Option(DecodificheManager.getInstance().getForo(), Option.BLANK_ITEM);
		} else {
			lOption = new Option(DecodificheManager.getInstance().getForo(), avvocato.getAvvocato().getForo(),
					Option.NO_BLANK_ITEM);
		}
		setRequestAttribute("foro", "" + lOption);

    // MEV_21 Nuova gestione Combo per Stato di Nascita
  	lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
//  	if (avvocato.getAvvocato().getCodStatoNascita()!=null)
//  		lOption.setSelected(avvocato.getAvvocato().getCodStatoNascita());
  	setRequestAttribute("nazione", "" + lOption );      

  	// MEV_21 Nuova gestione Combo per Stato Difensore
  	lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");  	
//  	if (avvocato.getAvvocato().getCodNonAttivita()!=null)
//  		lOption.setSelected(avvocato.getAvvocato().getCodNonAttivita());
  	setRequestAttribute("statoAvv", "" + lOption );  
  	
		return PG_SOSTITUZIONE_AVVOCATO;
	}

}