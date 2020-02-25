package siap.siep.refertoscarcerazione.controller;

import java.math.BigDecimal;

import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.misuraalternativa.model.MisuraAlternativaModel;
import siap.siep.penaresidua.model.PenaResiduaModel;
import siap.siep.refertoscarcerazione.model.RefertoScarcerazioneModel;
import f3b.util.F3BException;

/**
 * <p>Title: RefertoScarcerazioneController</p>
 * <p>Description: Classe Controller per RefertoScarcerazione</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */

public interface IRefertoScarcerazione
{

  public RefertoScarcerazioneModel ExRicercaRefertoScarcerazioneByKey(BigDecimal aKey) throws F3BException;

  public RefertoScarcerazioneModel ExRicercaUltimoRefertoScarcerazione() throws F3BException;

  public EventoModel ExInserisciEventoRefertoScarcerazione(EventoModel aEve, RefertoScarcerazioneModel aRefScaMod, EventoNotificaModel aEveMod, PenaResiduaModel aPenMod,
    MisuraAlternativaModel aMisMod) throws F3BException;

  public RefertoScarcerazioneModel ExRicercaRefertoScarcerazioneByEveIdEvento(BigDecimal aKey) throws F3BException;

  public EventoModel ExInserisciEventoRefertoScarcerazione(BigDecimal aKeyEvento, EventoNotificaModel aEveMod, PenaResiduaModel aPenMod) throws F3BException;
}