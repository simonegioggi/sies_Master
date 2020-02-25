package siap.bdmc.fascicolosiepbdmc.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadInserisciFascicoloSiepBdmc
 * </p>
 * <p>
 * Description: Classe Action per la load inserisci di FascicoloSiepBdmc
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Bull
 * </p>
 * 
 * @version 1.0
 */
public class ActLoadInserisciFascicoloSiepBdmc extends ActionSiap implements ICostantiFascicoloSiepBdmc {

	/*****************************************************************************
	 * Azione di caricamento della pagina di Inserimento dei dati. Si occupa anche di precaricare tutti i dati
	 * da visualizzare i tale pagina (es: combo)
	 * 
	 * @return Nome della pagina JSP da visualizzare
	 * @throws F3BException
	 *****************************************************************************/
	public String processRequest() throws F3BException {
		// Inserire Eventuali ComboBOX

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
		setRequestAttribute("autoritaEmi", "" + lOption);

		String codUfficio = this.getUfficioUtenteConnesso().getCodUfficio(); // per il codice ufficio
																				// dell'operatore connesso
		String descrUfficio = this.getUfficioUtenteConnesso().getDescrTipoUfficio(); // per la descrizione del
																						// tipo ufficio
																						// dell'utente
																						// connesso
		String descrComune = this.getUfficioUtenteConnesso().getDescrComune(); // per il comune dell'ufficio
																				// connesso
		String codProv = this.getUfficioUtenteConnesso().getCodProvincia(); // per la sigla della provincia

		setRequestAttribute("codUfficio", codUfficio);
		setRequestAttribute("descrUfficio", descrUfficio);
		setRequestAttribute("descrComune", descrComune);
		setRequestAttribute("codProv", codProv);

		setRequestAttribute("descrLuogoEmittente", "");
		setRequestAttribute("codTipoAutoritaEmittente", "");

		// Imposta la Modalità a Inserimento.
		setRequestAttribute("modalita", "I");

		// Restituisce la pagina di Inserimento dei Dati
		return PG_LOAD_INSERISCIFASCICOLOSIEPBDMC;
	}

}