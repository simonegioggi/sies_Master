package siap.siep.nuovaistanza.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import f3b.web.html.Option;
import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.evento.action.ICostantiEvento;
import siap.sico.web.ActionSiap;

/**
 * <p>
 * Title: ActLoadTrasferisciNuovaIstanza
 * </p>
 * <p>
 * Description: Trasferisce le istanze verso il tribunale di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Agile
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciNuovaIstanza extends ActionSiap implements ICostantiNuovaIstanza {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);

		// Paolo Cherubini modifica autorita destinatarie su richiesta di Nunzia
		// Preleva elenco delle altre autorità giudiziarie.
		// Option uffici = new Option( DecodificheManager.getInstance().getTipoUfficio());
		// uffici.setFilter( new String[] {"-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI",
		// "GIP", "GIPM", "GP", "GUP", "GUPM",
		// "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM", // AUTORITA EMITTENTI
		// "UDS", "TDS","TMIDS", // SORVEGLIANZA
		// "PM", "PGCAP"} ); // PROCURA

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		Collection lUffici = new Vector();
		lUffici = DecodificheManager.getInstance().getTipoUfficio();
		for (int i = 0; i < lUffici.size(); i++) {
			lUffici.remove(
					new DecodificheModel("UDSM", "Ufficio di Sorveglianza presso il Tribunale per minorenni",
							"TIPO_UFFICIO", "", "T", "", "", "", ""));
		}
		lUffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni", "TIPO_UFFICIO",
				"", "T", "", "", "", ""));
		Option uffici = new Option(lUffici);
		uffici.setFilter(new String[] { "-", "CAP", "CAS", "CASAP", "CAPMI", "CAPMID", "CSS", "GIPMI", "GIP",
				"GIPM", "GP", "GUP", "GUPM", "GUPMI", "PT", "TRIBSD", "CAPSM", "TMI", "DIB", "DIBM", // AUTORITA
																										// EMITTENTI
				"UDS", "TDS", "TMIDS", // SORVEGLIANZA
				"TDSM", "UDSM", "PM", "PGCAP" }); // PROCURA

		// Insieme degli uffici destinatari.
		// DecodificheManager.getInstance().getTipoUfficioSIUS().size() ;
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSIUS(), "-");

		setRequestAttribute("uffici", "" + uffici);
		setRequestAttribute("IDEvento", lEveId.toString());

		return PG_LOAD_TRASFERISCI_NUOVAISTANZA;
	}
}