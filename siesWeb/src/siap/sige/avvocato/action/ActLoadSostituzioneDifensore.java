package siap.sige.avvocato.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.util.DecodificheUtils;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoSigeModel;
import siap.sige.util.SIGELookupRemote;

public class ActLoadSostituzioneDifensore extends ActionSiap implements ICostantiAvvocato {

	/**
	 * Azione di Inserimento del Avvocato
	 *
	 * @return Nome della pagina JSP da visualizzare al termine dell'elaborazione
	 * @throws F3BException
	 */
	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector avvocati = new Vector();
		AvvocatoSigeModel avvocato = new AvvocatoSigeModel();

		AvvocatoModel lmModelAppo = new AvvocatoModel();
		BigDecimal id = new BigDecimal(this.getRequestStringParameter("tipo"));
		lmModelAppo.setIdAvvocato(id);

		avvocati = lCtrl.ExRicercaAvvocatoFascicoloSigeByKeyAvvocato(id);
		avvocato = (AvvocatoSigeModel) avvocati.get(0);
		if (getRequestStringParameter("numeroDifensori").equals("2")) {
			if (avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("D'UFFICIO")
					|| avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("DELLA FASE DI GIUDIZIO"))

				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
		}
		setRequestAttribute("avvocato", avvocato);

		Option lOption = new Option(DecodificheManager.getInstance().getTipoAvvocato());
		String[] lFilter = { "-", "01", "02" };
		lOption.setFilter(lFilter);
		setRequestAttribute("tipoAvvocato", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita());
		setRequestAttribute("autoritaEsterna", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getTipoAutorita(), "22");
		setRequestAttribute("autoritaEsternaDif", "" + lOption);

		lOption = new Option(DecodificheManager.getInstance().getMotivoDesignazione());
		setRequestAttribute("motivoDesignazione", "" + lOption);

		this.gestioneRitorno();

		// 19/03/2010 Nuova gestione Combo per Foro avvocato.
		// IAvvocato lCtrl1 = SIGELookupRemote.getAvvocatoRemote();
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
			lOption = new Option(DecodificheManager.getInstance().getForo(),
					lDescrComune.toUpperCase().trim(), Option.NO_BLANK_ITEM);
		}

		setRequestAttribute("foro", "" + lOption);

		// 20210627 MEV_21 Nuova gestione Combo per Stato di Nascita
		lOption = new Option(DecodificheManager.getInstance().getNazioni(), "-");
		setRequestAttribute("nazione", "" + lOption);

		// 20210627 MEV_21 Nuova gestione Combo per Stato Difensore
		lOption = new Option(DecodificheManager.getInstance().getListaAttivitaAvvocato(), "-");
		setRequestAttribute("statoAvv", "" + lOption);

		return PG_SOSTITUZIONE_AVVOCATO;
	}

}