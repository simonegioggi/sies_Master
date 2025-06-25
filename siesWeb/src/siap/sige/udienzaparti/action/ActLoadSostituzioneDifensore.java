package siap.sige.udienzaparti.action;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.web.html.Option;
import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.web.ActionSiap;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.udienzaparti.controller.IPartiUdienza;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.util.SIGELookupRemote;

/**
 * <p>
 * Title: ActLoadSostituzioneDifensore
 * </p>
 * <p>
 * Description: Classe Action per la Sostituzione di un Difensore
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 *
 * @version 1.0
 */
public class ActLoadSostituzioneDifensore extends ActionSiap implements ICostantiPartiUdienza {

	@SuppressWarnings("rawtypes")
	public String processRequest() throws Exception {

		String idSoggetto = "";
		if (!this.isRequestParameterNullObj(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO)) {
			idSoggetto = this.getRequestStringParameter(ICostantiPartiUdienza.CAMPO_ID_SOGGETTO);
		}

		// Identificativo evento udienza
		String lIdEventoUdienza = getRequestStringParameter(ICostantiSecurity.CAMPO_ID_ENTITA_PROVV);

		// recupero le informazioni della parte interessata
		IPartiUdienza lCtrlPU = SIGELookupRemote.getPartiUdienzaRemote();

		AnagraficaPartiUdienzaModel anagParteUdienzaRet = lCtrlPU
				.ExRicercaParteUdienzaByKey(new BigDecimal(idSoggetto));

		setRequestAttribute("anagraficaParteUdienza", anagParteUdienzaRet);
		setRequestAttribute("idSoggetto", "" + idSoggetto);
		setRequestAttribute("lIdEventoUdienza", "" + lIdEventoUdienza);

		IAvvocato lCtrl = SIGELookupRemote.getAvvocatoRemote();
		Vector avvocati = new Vector();
		PartiUdienzaDifensoreModel avvocato = new PartiUdienzaDifensoreModel();

		AvvocatoModel lmModelAppo = new AvvocatoModel();
		BigDecimal id = new BigDecimal(this.getRequestStringParameter("tipo"));
		lmModelAppo.setIdAvvocato(id);

		avvocati = lCtrl.ExRicercaDifensoreParteByKeyAvvocato(id);
		avvocato = (PartiUdienzaDifensoreModel) avvocati.get(0);

		if (getRequestStringParameter("numeroDifensori").equals("2")) {
			if (avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("D'UFFICIO")
					|| avvocato.getAvvocato().getDescrTipo().equalsIgnoreCase("DELLA FASE DI GIUDIZIO")) {
				throw new F3BException(F3BException.USER_MESSAGE,
						"I difensori possono essere due solo se entrambi sono di fiducia!");
			}
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

		UfficioModel lUffUte = this.getUfficioUtenteConnesso();
		String lDescrComune = lUffUte.getDescrComune();
		this.setRequestAttribute("comune", lDescrComune);

		lOption = new Option(DecodificheManager.getInstance().getForo(), lDescrComune.toUpperCase().trim(),
				Option.NO_BLANK_ITEM);
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