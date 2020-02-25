package siap.sico.misuraalternativa.controller;

import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.posizione.model.PosizioneGiuridicaModel;
import f3b.util.F3BException;

/**
 * <p>Title: IMisuraAlternativaBackupSrc</p>
 * <p>Description: Classe Controller per MisuraAlternativa</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface IMisuraAlternativaBackupSrc
{
//ricerche
  public MisuraAlternativaModel ExRicercaMisuraAlternativaDicEffAffInProvaByIdFascicolo(BigDecimal aKey) throws F3BException;
  public MisuraAlternativaModel ExRicercaMisuraAlternativaProrogaUltPeriodoByIdFascicolo(BigDecimal aKey) throws F3BException;
  public MisuraAlternativaModel ExRicercaMisuraAlternativaDetDomSpeAmmAffByIdFascicolo(BigDecimal aKey) throws F3BException;
  public MisuraAlternativaModel ExRicercaMAAmmissioneADetDomByIdFascicolo(BigDecimal aIdFascicolo) throws F3BException;
  public MisuraAlternativaModel ExRicercaMisuraAlternativaRipristinoDetDomSpecByIdFascicolo(BigDecimal aKey) throws F3BException;
  public MisuraAlternativaModel ExRicercaMisuraAlternativaConcessioneLibCondByIdFascicolo(BigDecimal aKey) throws F3BException;


//validazioni
    public EventoModel ExUpdateValidaMACoLibCond(EventoModel aEvento, FascicoloSiepModel aFascicolo, PosizioneGiuridicaModel aPosizione) throws F3BException;
    public EventoModel ExUpdateValidaMAReLibCond(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaUlteriorePeriodoMA(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaMADetDomSpeAmmAff(EventoModel aEvento, FascicoloSiepModel aFascicolo, String tipoMisura) throws F3BException;
    public EventoModel ExUpdateValidaMARipristinoDetDomSpec(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaMAProrogaUltPeriodo(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaAmmissioneADetDom(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaMADetDomSpec(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaMADetDomSpecSospProvv(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;
    public EventoModel ExUpdateValidaMADicEff(EventoModel aEvento, FascicoloSiepModel aFascicolo) throws F3BException;

}