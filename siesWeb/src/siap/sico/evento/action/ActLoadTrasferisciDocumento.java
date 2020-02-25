package siap.sico.evento.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.decodifiche.model.DecodificheModel;
import siap.sico.web.ActionSiap;
import f3b.web.html.Option;

/**
 * <p>
 * Title: ActLoadTrasferisciDocumento
 * </p>
 * <p>
 * Description: Trasferisce il documentoistanze verso il tribunale di sorveglianza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ActLoadTrasferisciDocumento extends ActionSiap implements ICostantiEvento {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String processRequest() throws Exception {

		BigDecimal lEveId = getRequestBigDecimalParameter(ICostantiEvento.CAMPO_ID_EVENTO);
		// EventoNotificaModel lEve = evento;

		// Insieme degli uffici destinatari
		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficio());
		// 21/06/2007 Trasferimento anche all'Ufficio.
		// lOption.setFilter( new String[] {"-", "TDS", "UDS"} );

		// A.S. 18/05/2015 su richiesta di Michele/Nunzia
		// Per SIEP la descrizione UDSM cambia da "Ufficio di Sorveglianza presso il Tribunale per minorenni"
		// in "Magistrato di Sorveglianza per i minorenni"
		Collection lUffici = new Vector();
		lUffici = DecodificheManager.getInstance().getTipoUfficio();
		for (int i = 0; i < lUffici.size(); i++) {
			lUffici.remove(new DecodificheModel("UDSM",
					"Ufficio di Sorveglianza presso il Tribunale per minorenni", "TIPO_UFFICIO", "", "T", "",
					"", "", ""));
		}
		lUffici.add(new DecodificheModel("UDSM", "Magistrato di Sorveglianza per i minorenni",
				"TIPO_UFFICIO", "", "T", "", "", "", ""));
		Option lOption = new Option(lUffici);
		lOption.setFilter(new String[] { "-", "UDS", "TDS", "TDSM", "UDSM" });

		setRequestAttribute("uffici", "" + lOption);
		setRequestAttribute("IDEvento", lEveId.toString());

		// STUB 11/09/2006 Destinatari UEPE.
		lOption = new Option(DecodificheManager.getInstance().getTipoUfficioSiepeMinorili());
		setRequestAttribute("UEPE", "" + lOption);

		return PG_LOAD_TRASFERISCI_DOCUMENTO;
	}

}