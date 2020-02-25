package siap.siep.ordinescarcerazione.controller;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaAggregatoModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;

public interface IOrdineScarcerazione
{
  /**
   * 
   * @param aEvento
   * @return
   * @throws F3BException
   */
  public EventoNotificaModel ExInserisciOModificaEventoNotifica(EventoNotificaModel aEvento) throws F3BException;

  public EventoModel ExUpdateValidaOrdineScarcerazione(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;

  /**
   * Inserimento della Misura Alternativa e dell'Ordine di Scarcerazione
   * @param aMisura - E' l'aggregato della misura alternativa simultata proveniente da SIUS se è null non viene trattato
   * @param aEvento - E' l'aggragato dell'evento Ordine di Scarcerazione
   * @return un EventoNotificaModel che è dato dall'inserimento dell' aEvento all'interno del DB.
   * @throws F3BException
   */
  public EventoNotificaModel ExInserisciMAeOrdineScarcerazione(MisuraAlternativaAggregatoModel aMisura,MisuraAlternativaAggregatoModel aEvento) throws F3BException;

  public EventoModel ExUpdateValidaOS(EventoModel aEvento, FascicoloSiepModel aFascicolo)
    throws F3BException;

  /**
   * Effettua la validazione degli Ordini di Scarcerazione per nuova scadenza
   * pena a seguito di concessione LA.
   * Vengono validati: 
   * - L'ordine di Scarcerazione
   * - L'ordinanza di concessione (e (DepositoOrdinanzaPC) collegata all'OS
   * - Le LA collegate all'ordinanza (FLAG_ELABORATO a S)
   * - L'ultima pena residua trovata (validata o meno :-( )
   * Vengono aggiornati:
   * - stato procedimento
   * - nome procedimento
   * - La misura alternativa (data fine) se il condannato è in misura
   * - Lo scadenzario fine pena
   * @param aEvento - Ordine di Scarcerazione da validare
   * @param aFascicolo
   * @return
   * @throws F3BException
   */
  public EventoModel ExUpdateValidaOSLibAnt(EventoModel aEvento, FascicoloSiepModel aFascicolo)
    throws F3BException;

  public EventoNotificaModel ExInserisciOModificaNotifica(EventoNotificaModel aEvento) throws F3BException;


}