package siap.sige.fascicolo.util;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import siap.sico.avvocato.model.AvvocatoModel;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sige.avvocato.controller.IAvvocato;
import siap.sige.avvocato.model.AvvocatoFascicoloSigeModel;
import siap.sige.provvedimento.controller.IProvvedimentoSige;
import siap.sige.provvedimento.model.ProvvedimentoSigeEventoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.F3BException;
import f3b.web.html.Option;

/**
 * <p>
 * Title: FascicoloSigeUtils
 * </p>
 * <p>
 * Description: Classe di utilita' per il package FascicoloSige
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Eutelia
 * </p>
 * 
 * @version 1.0
 */
public class FascicoloSigeUtils extends ActionSige {

	/**
	 * Funzione per l'impostazione della Combo per il Tipo Giudizio SIGE.
	 * 
	 * @return String - Combo impostata in base al tipo di ufficio connesso. Descrizione: La funzione consente
	 *         di precaricare la Combo per il Tipo Giudizio (Monocratico, Collegiale,...),
	 *         <p>
	 *         Copyright: Copyright (c) 2008
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @version 1.0
	 *
	 */
	public String leggiTipoGiudizio(String aCodTipoUfficioConnesso) throws Exception {

		// Imposta Tipo Giudizio.
		Option lGiudizioOpt = new Option(DecodificheManager.getInstance().getTipoGiudizioSige());
		if (aCodTipoUfficioConnesso.compareTo("GIP") == 0)
			lGiudizioOpt.setFilter("M");
		else if ("CASAP_CAS_CAP".indexOf(aCodTipoUfficioConnesso) >= 0)
			lGiudizioOpt.setFilter("C");
		else {
			lGiudizioOpt.setAddBlankItem(true);
			lGiudizioOpt.setValueBlankItem("-");
		}
		return lGiudizioOpt.toString();
	}

	/**
	 * Funzione per l'impostazione del Destinatario della comunicazione di Fissazione Udienza SIGE.
	 * 
	 * @return String - Descrizione destinatario impostata in base al tipo di ufficio connesso.
	 *         <p>
	 *         Copyright: Copyright (c) 2008
	 *         </p>
	 *         <p>
	 *         Company:
	 *         </p>
	 * @version 1.0
	 *
	 */
	public String leggiTipoDestinatario(String aCodTipoUfficioConnesso) throws Exception {

		// Imposta Tipo Giudizio.
		String strTipoDest = "";
		// MERGE v10 COLLAUDO: MODIFICATA gestione casistiche
//		if ("CAS".equals(aCodTipoUfficioConnesso))
//			strTipoDest = "Procuratore della Repubblica";
//		else if (isUfficioMinori(aCodTipoUfficioConnesso))
//			strTipoDest = "Procuratore della Repubblica dei Minorenni";
//		else
//			strTipoDest = "Procuratore Generale della Repubblica";
//		if ("CASAP_CAP".indexOf(aCodTipoUfficioConnesso) >= 0)
//			strTipoDest = "Procuratore Generale della Repubblica";
//		else if ("".equals(super.checkMinori()))
//			strTipoDest = "Procuratore della Repubblica dei Minorenni";
//		else
//			strTipoDest = "Procuratore della Repubblica";
		
		// 20171128 [EC] mac segnalata da MAffucci con email del 27112017 per anomalia Venezia
		if ("CAS".equals(aCodTipoUfficioConnesso) || "CASAP".equals(aCodTipoUfficioConnesso) || "CAP".equals(aCodTipoUfficioConnesso))
			strTipoDest = "Procuratore Generale della Repubblica";
		else if (isUfficioMinori(aCodTipoUfficioConnesso))
			strTipoDest = "Procuratore della Repubblica dei Minorenni";
		else
			strTipoDest = "Procuratore della Repubblica";
		return strTipoDest;
	}

	/**
	 * Funzione per la ricerca degli avvocati attuali x Id Fascicolo SIGE. Pone nella request con nome
	 * "avvocato" il vettore degli AvvocatoFascicoloSigeModel().
	 * <p>
	 * Copyright: Copyright (c) 2008
	 * </p>
	 * <p>
	 * Company:
	 * </p>
	 * 
	 * @version 1.0
	 *
	 */
	@SuppressWarnings("rawtypes")
	public Vector ricercaAvvocati(BigDecimal lIdFasSige) throws Exception {

		// Ricerca Avvocati
		IAvvocato lCtrlAvv = SIGELookupRemote.getAvvocatoRemote();
		Vector lAvvocati = new Vector();

		AvvocatoModel lAvvMod = new AvvocatoModel();
		AvvocatoFascicoloSigeModel lAvvFascMod = new AvvocatoFascicoloSigeModel();
		lAvvFascMod.setFasSigeIdFascicoloSige(lIdFasSige);
		lAvvocati = lCtrlAvv.ExRicercaDifensoreAttualiFascicolo(lAvvMod, lAvvFascMod);

		return lAvvocati;
	}

	/**
	 * Ritorna un boolean che stabilisce la presenza di un provvedimento con Deposito Validato per un dato
	 * Fascicolo SIGE.
	 */
	public boolean HasFascicoloSigeProvvDefinitorioConDepositoValidato(BigDecimal aIdFascicolo)
			throws F3BException {

		boolean lRet = false;
		// Ricerca Provvedimento Definitorio con Deposito Validato
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvEventoSige = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(aIdFascicolo);
		if (lProvvEventoSige != null && lProvvEventoSige.getEventoNotifica() != null
				&& lProvvEventoSige.getEventoNotifica().getEvento() != null
				&& lProvvEventoSige.getEventoNotifica().getEvento().getNumAllValidati() > 0)
			lRet = true;

		return lRet;
	}

	/**
	 * MERGE v10 COLLAUDO: aggiunto HashSet per uffici minorenni
	 */
	private static Set<String> ufficiMinori = new HashSet<String>();
	static {
		ufficiMinori.add("PMM");
		ufficiMinori.add("DIBM");
		ufficiMinori.add("GIPM");
		ufficiMinori.add("GUPM");
		ufficiMinori.add("CAPSM");
		ufficiMinori.add("TDSM");
		ufficiMinori.add("UDSM");
	}

	/**
	 * MERGE v10 COLLAUDO: aggiunto metodo di controllo
	 * 
	 * @param aCodTipoUfficioConnesso
	 * @return
	 * @throws F3BException
	 */
	private boolean isUfficioMinori(String aCodTipoUfficioConnesso) throws F3BException {

		if (ufficiMinori.contains(aCodTipoUfficioConnesso))
			return true;
		return false;
	}

	
	/**
	 * Ritorna l'eventuale Provvedimento definitoro con Deposito Validato per un dato
	 * Fascicolo SIGE.
	 */
	public ProvvedimentoSigeEventoModel getProvvDefinitorioConDepositoValidatoByFascicolo(BigDecimal aIdFascicolo)
			throws F3BException {

		// Ricerca Provvedimento Definitorio con Deposito Validato
		IProvvedimentoSige lCtrlProv = SIGELookupRemote.getProvvedimentoRemote();
		ProvvedimentoSigeEventoModel lProvvEventoSige = lCtrlProv
				.ExRicercaProvvedimentoDefinitorioByIdFascicolo(aIdFascicolo);
		return lProvvEventoSige;
	}

}