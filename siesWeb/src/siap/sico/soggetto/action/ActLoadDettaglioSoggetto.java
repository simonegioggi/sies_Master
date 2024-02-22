package siap.sico.soggetto.action;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import siap.sico.jms.controller.IRicercaSICOJMS;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.soggettodattilo.controller.ISoggettoDattilo;
import siap.sico.soggettodattilo.model.SoggettoDattiloModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioAccorpatoModel;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;

/**
 * <p>
 * Title: ActLoadDettaglioSoggetto
 * </p>
 * <p>
 * Description: Azione Load del Dettaglio del Soggetto
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 *
 * @version 1.0
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ActLoadDettaglioSoggetto extends ActionSiap implements ICostantiSoggetto {

	/**
	 * Azione di caricamento del Dettaglio del Soggetto
	 * <p>
	 *
	 * @return Nome della pagina JSP su cui posizionarsi al termine dell'elaborazione
	 * @throws Exception
	 */
	public String processRequest() throws Exception {

		BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_SOGGETTO);

		// paramentro passato per i procedimenti Siep
		// Nella mschera di dettaglio sono presenti
		// 2 nuovi pulsanti ("Allegare Documento" e "Ricerca Soggetto")
		// TipoOperazione = 'A' in corrispondenza del pulsante "Allegare Documento"
		// TipoOperazione = 'R' in corrispondenza del pulsante "Ricerca Soggetto"
		if (!isRequestParameterNullObj("TipoOperazione")
				&& !getRequestStringParameter("TipoOperazione").equals("")) {
			this.setRequestAttribute("TipoOperazione", getRequestStringParameter("TipoOperazione"));
		}

		if (!isRequestParameterNullObj("CampoAzioneChiamante")
				&& !getRequestStringParameter("CampoAzioneChiamante").equals("")) {
			this.setRequestAttribute("CampoAzioneChiamante",
					getRequestStringParameter("CampoAzioneChiamante"));
		}

		// Chiama il controller.
		ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
		SoggettoModel lSoggetto = lSogCtrl.ExRicercaSoggettoByKey(lId);

		// Inserisce il model soggetto in sessione
		setSessionAttribute("soggetto", lSoggetto);

		// Modifica del 05/09/2013 "Implementazione SIES per accorpamento uffici"
		// Risolto errore preesistente
		// Inserisce il model FascicoloSigeEstesoModel in sessione
		FascicoloSigeEstesoModel lFascicoloEsteso = null;
		if (isSessionAttributeNullObj("FascicoloSigeEsteso")) {
			lFascicoloEsteso = new FascicoloSigeEstesoModel();
		} else {
			lFascicoloEsteso = (FascicoloSigeEstesoModel) getSessionAttribute("FascicoloSigeEsteso");
		}
		setSessionAttribute("FascicoloSigeEsteso", lFascicoloEsteso);

		// Inserisce il model soggetto nella request
		setRequestAttribute("soggetto", lSoggetto);

		// paramentro passato solo nel caso di iscrizione guidata
		if (!this.isRequestAttributeNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestAttribute("lTipoFunzione"));
		}

		// paramentro passato solo nel caso di iscrizione guidata E VENGO DA DETTAGLIO SOGGETTO
		if (!this.isRequestParameterNullObj("lTipoFunzione")) {
			this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
		}

		if (!this.isRequestParameterNullObj("NomeAzione")) {
			this.setRequestAttribute("NomeAzione", this.getRequestStringParameter("NomeAzione"));
		}

		// verifica se l'utente è abilitato a modificare il soggetto
		String abilitaUtente = "NO";
		String soggUffIns = lSoggetto.getCodUfficioInserimento();
		String codUfficioUtente = getCodUfficioUtenteConnesso();

		// [SG]: per il soggetto IGNOTO IGNOTO il valore di soggUffIns è NULL
		// inverto la condizione sia nell'if che nell'else
		if (codUfficioUtente.equals(soggUffIns)) {
			abilitaUtente = "SI";
		} else {
			IUfficio lUACon = SICOLookupRemote.getUfficioRemote();
			Vector lUffAccUtente = lUACon.ListaUfficiAccorpati(null, codUfficioUtente);
			Iterator itr = lUffAccUtente.iterator();
			while (itr.hasNext()) {
				UfficioAccorpatoModel lUAMod = (UfficioAccorpatoModel) itr.next();
				if (lUAMod.getCodUfficio().equals(soggUffIns)) {
					abilitaUtente = "SI";
				}
			}
		}

		// Inserisce il model soggetto nella request
		setRequestAttribute("modificabile", abilitaUtente);

		// L'utente potrà inserire allegati solo nel procedimento iscritto dal proprio ufficio
		String insertAllegato = "SI";
		FascicoloSiepModel lFasc = null;
		if (!isSessionAttributeNullObj("fascicolo")) {
			lFasc = new FascicoloSiepModel((FascicoloSiepModel) getSessionAttribute("fascicolo"));
		}

		if (codUfficioUtente != null && !codUfficioUtente.equals("") && lFasc != null
				&& lFasc.getCodUfficioInserimento() != null) {
			if (!codUfficioUtente.equals(lFasc.getCodUfficioInserimento())) {
				insertAllegato = "NO";
			}
		}
		setRequestAttribute("insertAllegato", insertAllegato);

		// Inserisce array di Riferimenti Dattiloscopici nella request
		Vector vxv = null;
		try {
			SoggettoDattiloModel sdm = new SoggettoDattiloModel();
			sdm.setCodSoggetto(lId);
			ISoggettoDattilo mEveCtrl = SICOLookupRemote.getSoggettoDattiloRemote();
			vxv = mEveCtrl.ExRicercaSoggettoDattilo(sdm);
		} catch (Exception e) {
		}
		setRequestAttribute("riferimentiDattilo", vxv);

		// Passa la action di destinazione
		// Sostituita da gestioneRitorno()
		// if (!isRequestParameterNullObj("TornaQui")) {
		// this.setRequestAttribute("TornaQui", this.getRequestStringParameter("TornaQui"));}

		IRicercaSICOJMS lCrtl = SICOLookupRemote.getRicercaSICOJMSRemote();
		Vector allBDI = new Vector(lCrtl.ExRicercaAllBDI());
		setRequestAttribute("ListaBDI", allBDI);

		// this.gestioneRitorno();
		// this.setRequestAttribute("prov", this.getRequestStringParameter("prov"));
		// valore di ritorno
		return PG_LOAD_DETTAGLIOSOGGETTO;
	} // Chiude processRequest()

} // Chiude Classe