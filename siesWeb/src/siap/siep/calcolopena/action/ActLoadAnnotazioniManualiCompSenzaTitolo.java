package siap.siep.calcolopena.action;

/**
 * <p>Title: ActLoadAnnotazioniManualiCompSenzaTitolo</p>
 * <p>Description: Azione Load della form di inserimento Computo Fungibilità (PD)</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import java.util.Collection;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale;

public class ActLoadAnnotazioniManualiCompSenzaTitolo extends ActLoadInserisciAnnotazioniManuali
		implements ICostantiAnnotazioneManuale {

	/**
	 * Rideterminazione Pena --> Computo pena detentiva espiata per altro reato (fungibilità)
	 * 
	 * @return Nome della pagina JSP di inserimento dei dati della Fungibilità
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String lPageErr = loadRichiestaAnnotazioniManuali("0213"); // MC SENZA TITOLO

		if (lPageErr != null)
			return lPageErr;

		Collection lCollCausaleComputo = DecodificheManager.getInstance()
				.getTipoCausaleComputoMCSenzaTitolo();
		Option lOption = new Option(lCollCausaleComputo, true);
		lOption.setValueBlankItem("-");

		setRequestAttribute("CausaleComputo", "" + lOption);

		String lPage = PG_LOAD_INSERISCI_COMPUTO_SENZA_TITOLO;

		return lPage;
	}

}