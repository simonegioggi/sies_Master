package siap.siep.rateizzazionepp.controller;

import java.math.BigDecimal;
import java.util.Vector;

import f3b.util.F3BException;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.siep.annotazionemanuale.model.AnnotazioneManualeModel;
import siap.siep.rateizzazionepp.model.EventoRateizzazionePPModel;
import siap.siep.rateizzazionepp.model.RateizzazionePPModel;

/**
 * Classe interfaccia per la gestione delle rateizzazioni
 *
 * @author sgioggi
 * @since MEV_2023-13
 * @version 1.0
 */
public interface IRateizzazionePP {

	public void exInserisciRateizzazioni(Vector<RateizzazionePPModel> aListaRate) throws F3BException;

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdFasc(BigDecimal aIdFasc)
			throws F3BException;

	public void exCancellaRateizzazioniByIdFasc(BigDecimal aIdFasc) throws F3BException;

	public void exModificaRateizzazioni(Vector<RateizzazionePPModel> aListaRate, BigDecimal aIdFasc)
			throws F3BException;

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniByIdEvento(BigDecimal aIdEvento)
			throws F3BException;

	public Vector<EventoRateizzazionePPModel> exRicercaEventoRateizzazionePP(BigDecimal idFascicolo,
			String motivo) throws F3BException;

	// MEV_2023-33: aggiunti metodi di inserimento, modifica, ricerca
	public BigDecimal exInserisciRideterminazionePP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException;

	public void exUpdateRideterminazionePP(EventoModel em) throws F3BException;

	public EventoNotificaModel exModificaRideterminazionePP(EventoNotificaModel enm, String[] arrayIdRate,
			AnnotazioneManualeModel amm) throws F3BException;

	public Vector<RateizzazionePPModel> exRicercaRateizzazioniLibereByIdFasc(BigDecimal aIdFasc)
			throws F3BException;

	public Vector<EventoRateizzazionePPModel> exRicercaEventoRateizzazionePP(BigDecimal idFascicolo,
			String motivo, String validato) throws F3BException;

  public Vector<EventoRateizzazionePPModel> exRicercaEventiRateizzazionePP(BigDecimal idFascicolo, String[] motivi, boolean soloValidati)
      throws F3BException;
  public Vector<RateizzazionePPModel> exRicercaRateizzazioniBollettiniByIdEvento(BigDecimal aIdEvento)
      throws F3BException ;
}
