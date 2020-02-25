package siap.siep.modulocumulo.action;

/**
 * <p>Title: ActDettaglioSoggettoCumulato</p>
 * <p>Description: Azione Load del Dettaglio del Soggetto Cumulato</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @version 1.0
 */

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.modulocumulo.controller.ISoggettoCumulato;
import siap.siep.modulocumulo.model.SoggettoCumulatoModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;

@SuppressWarnings("rawtypes")
public class ActDettaglioSoggettoCumulato extends ActionModuloCumulo implements ICostantiSoggettoCumulato {

	/**
	 * Azione di caricamento del Dettaglio del Soggetto Cumulato
	 * <p>
	 * 
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws F3BException
	 */
	public String processRequest() throws Exception {

		if (isSessionAttributeNullObj("fascicolo")) {
			return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
		}

		// ==========================================================================
		// Recupero i dati del CUMULO, FASCICOLO, SENTENZA da passare alla form
		// di DettaglioTitoloCumulato.jsp
		// ==========================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO_CUMULATO);

		// Chiama il controller.
		ISoggettoCumulato lSogCtrl = SIEPLookupRemote.getSoggettoCumuloRemote();
		SoggettoCumulatoModel lSoggetto = lSogCtrl.ExRicercaSoggettoCumulatoByKey(lId);

		// Ricerca Descrizione Stato Nascita
		DecodificheModel lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSoggetto.getCodStatoNascita());
		List lNazioni = (List) DecodificheManager.getInstance().getNazioni();
		int lIndModel = lNazioni.indexOf(lDecMod);
		String lDescri = ((DecodificheModel) lNazioni.get(lIndModel)).getDescription();

		lSoggetto.setDescrStatoNascita(lDescri);

		// Ricerca la descrizione di Stato Cittadinanza (nel model DESCRNAZIONALITA)
		lDecMod = new DecodificheModel();
		lDecMod.setContesto("NAZIONE");
		lDecMod.setCode(lSoggetto.getNazionalita());
		List lStatoCitta = (List) DecodificheManager.getInstance().getStatoCittadinanza();
		lIndModel = lStatoCitta.indexOf(lDecMod);
		String lDescrStatoCit = ((DecodificheModel) lStatoCitta.get(lIndModel)).getDescription();

		lSoggetto.setDescrNazionalita(lDescrStatoCit);
		//
		// Inserisce il model soggetto nella request
		setRequestAttribute("soggetto", lSoggetto);

		// verifica se l'utente è abilitato a modificare il soggetto
		String abilitaUtente = "NO";
		String soggUffIns = lSoggetto.getCodUfficioInserimento();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		if (soggUffIns.equals(codUfficioUtente)) {
			abilitaUtente = "SI";
		} else {
			IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
			Vector lUffAccUtente = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);
			Iterator itr = lUffAccUtente.iterator();
			while (itr.hasNext()) {
				UfficioAccorpatoModel lUAMod = (UfficioAccorpatoModel) itr.next();
				if (soggUffIns.equals(lUAMod.getCodUfficio())) {
					abilitaUtente = "SI";
				}
			}
		}

		// Inserisce il model soggetto nella request
		setRequestAttribute("modificabile", abilitaUtente);

		return PG_LOAD_DETTAGLIOSOGGETTOCUMULATO;

	} // Chiude processRequest()

} // Chiude Classe