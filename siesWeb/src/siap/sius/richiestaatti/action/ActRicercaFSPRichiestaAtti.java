package siap.sius.richiestaatti.action;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import siap.sico.security.action.ICostantiSecurity;
import siap.sico.ufficio.model.UfficioModel;
import siap.sius.fascicolo.action.ActRicercaFSPuntuale;
import f3b.security.model.FunctionModel;

public class ActRicercaFSPRichiestaAtti extends ActRicercaFSPuntuale implements ICostantiRichiestaAtti {
	public String processRequest() throws Exception {

		// Passando il parametro noQuery non effettua nuovamente la ricerca
		if (isRequestParameterNullObj("noQuery"))
			super.processRequest();

		@SuppressWarnings("unchecked")
		Collection<FunctionModel> lListaStampe = (Collection<FunctionModel>) super.getRequest().getAttribute(
				ICostantiSecurity.FUN_FIGLIE);
		UfficioModel um = getUfficioUtenteConnesso();
		String tipoUfficio = um.getCodTipoUfficio();

		if (tipoUfficio.equalsIgnoreCase("UDSM"))
			setListDocsByUDSM(lListaStampe);
		if (tipoUfficio.equalsIgnoreCase("TDSM"))
			setListDocsByTDSM(lListaStampe);

		// Bottone di ritorno
		setLinkRitorno();

		// Ritorna la JSP di view dell'elenco stampe.
		return ICostantiRichiestaAtti.PG_ELENCOSTAMPEDOCISTRUTTORI;
	}

	//
	// MEV-10 sius Minorenni Requisito FA9 UC9.1
	//
	// Per la funzionalità Richiesta Atti > Richiesta Documenti Istruttori rinuove le seguenti Voci
	// per il codice tipo ufficio UDSM:
	//
	// Commissione Centrale n. l.; (codice funzione: 10655)
	// Informativa det. Estero; (codice funzione: 32030070)
	// Programma protezione n.; (codice funzione: 10678)
	// L. 2003/207 – Idoneità domicilio; (codice funzione: 32030010)
	// L. 2003/207 – Possesso Permesso Soggiorno; (codice funzione: 32030020)
	// L. 2003/207 – Sottoposizione Sorv. Particolare; (codice funzione: 32030030)
	//
	private void setListDocsByUDSM(Collection<FunctionModel> lListaStampe) {
		Set<BigDecimal> elenco = new HashSet<BigDecimal>();
		elenco.add(new BigDecimal("10655"));
		elenco.add(new BigDecimal("32030070"));
		elenco.add(new BigDecimal("10678"));
		elenco.add(new BigDecimal("32030010"));
		elenco.add(new BigDecimal("32030020"));
		elenco.add(new BigDecimal("32030030"));

		Collection<FunctionModel> newListaStampe = new Vector<FunctionModel>();
		for (FunctionModel func : lListaStampe) {
			// MEV10-s3: aggiunta chiamata al metodo per cambio etichetta
			func = cambiaEtichettaRelazione(func);
			BigDecimal funcId = func.getFunctionId();
			if (elenco.contains(funcId))
				continue;

			newListaStampe.add(func);
		}
		super.getRequest().setAttribute(ICostantiSecurity.FUN_FIGLIE, newListaStampe);
	}

	//
	// MEV-10 sius Minorenni Requisito FA9 UC9.2
	//
	// Per la funzionalità Richiesta Atti > Richiesta Documenti Istruttori rinuove le seguenti Voci
	// per il codice tipo ufficio UDSM:
	//
	// Commissione Centrale n. l.; (codice funzione: 10655)
	// Informativa det. Estero; (codice funzione: 32030070)
	// Programma protezione n.; (codice funzione: 10678)
	//
	private void setListDocsByTDSM(Collection<FunctionModel> lListaStampe) {
		Set<BigDecimal> elenco = new HashSet<BigDecimal>();
		elenco.add(new BigDecimal("10655"));
		elenco.add(new BigDecimal("32030070"));
		elenco.add(new BigDecimal("10678"));

		Collection<FunctionModel> newListaStampe = new Vector<FunctionModel>();
		for (FunctionModel func : lListaStampe) {
			// MEV10-s3: aggiunta chiamata al metodo per cambio etichetta
			func = cambiaEtichettaRelazione(func);
			BigDecimal funcId = func.getFunctionId();
			if (elenco.contains(funcId))
				continue;

			newListaStampe.add(func);
		}
		super.getRequest().setAttribute(ICostantiSecurity.FUN_FIGLIE, newListaStampe);
	}

	/**
	 * MEV10-s3: aggiunta funzione per cambiare etichette a determinate funzionalità
	 * 
	 * @param func
	 * @return FunctionModel
	 */
	private FunctionModel cambiaEtichettaRelazione(FunctionModel func) {
		BigDecimal funcId = func.getFunctionId();
		BigDecimal rcCSSA = new BigDecimal("10682");
		BigDecimal reCSSA = new BigDecimal("10683");
		if (funcId.intValue() == rcCSSA.intValue())
			func.setLabelFunction("Relazione conclusiva UEPE/USSM");
		else if (funcId.intValue() == reCSSA.intValue())
			func.setLabelFunction("Relazione UEPE/USSM");
		return func;
	}
}