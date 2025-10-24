package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;

import siap.sico.camponota.model.CampoNotaModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import f3b.util.F3BException;

/**
 * <p>Title: MisuraAlternativaController</p>
 * <p>Description: Classe Controller per MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface IMisuraAlternativaIndultino
{

  public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdFascicoloNaturaTipoMisuraDecisione(BigDecimal aKey, String[] aNatura, String[] aTipoMisura , String[] aDecisione)
       throws F3BException;

  public MisuraAlternativaModel ExRicercaMisuraAlternativaByIdFascicoloNaturaDecisione(BigDecimal aKey, String aNatura)
       throws F3BException;

  public EventoModel ExUpdateValidaMARipristinoIndultino(EventoModel aEvento, FascicoloSiepModel aFascicolo)
       throws F3BException;

  public EventoModel ExUpdateValidaMAProsecuzione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
       throws F3BException;
  
  public EventoModel ExUpdateValidaMAProsecuzione51Bis(EventoModel aEvento, FascicoloSiepModel aFascicolo)
      throws F3BException;

  public EventoModel ExUpdateValidaMAEstensione(EventoModel aEvento, FascicoloSiepModel aFascicolo)
       throws F3BException;

  public MisuraAlternativaModel ExRicercaMisuraAlternativaPrecedenteByIdFascicolo(BigDecimal aKey)
       throws F3BException;

  public EventoModel ExUpdateValidaMARigetto(EventoModel aEvento, FascicoloSiepModel aFascicolo)
    throws F3BException;

  public EventoModel ExAggiornaEventoInserisciCampoNota(EventoModel aEvento, CampoNotaModel aCampoNota)
    throws F3BException;

  public EventoModel ExUpdateValidaMAAmmProvvisoria(EventoModel aEvento, FascicoloSiepModel aFascicolo)
    throws F3BException;
  
  public EventoModel ExUpdateValidaVariazioneMAAmmProvvisoria(EventoModel aEvento, FascicoloSiepModel aFascicolo)
  throws F3BException;

}
