package siap.siep.modulocumulo.controller;


/**
* <p>Title: DatiFinaliCumuloController</p>
* <p>Description: Classe Controller per DatiFinaliCumulo</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.xml.TreeModel;
import siap.sico.evento.model.EventoModel;
import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloAggregatoModel;
import siap.siep.modulocumulo.model.DatiFinaliCumuloModel;
import siap.siep.modulocumulo.model.DatiFinaliUlterioriSanzioniModel;
import siap.siep.modulocumulo.model.PenaRideterminataCumuloModel;
import siap.siep.modulocumulo.model.PosizioneGiuridicaCumuloModel;
import siap.siep.modulocumulo.model.ProcedimentoCumulatoModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;


public interface IDatiFinaliCumulo{

  public DatiFinaliCumuloModel ExInserisciDatiFinaliCumulo (DatiFinaliCumuloModel aDatiFinaliCumulo )  throws F3BException ;
  public Vector <DatiFinaliCumuloModel> ExRicercaDatiFinaliCumulo (DatiFinaliCumuloModel aDatiFinaliCumulo ) throws F3BException; 
  public void  ExModificaDatiFinaliCumulo (DatiFinaliCumuloModel aDatiFinaliCumulo ) throws F3BException;
  public void ExCancellaDatiFinaliCumulo (DatiFinaliCumuloModel aDatiFinaliCumulo ) throws F3BException ;
  public BigDecimal ExGetCountDatiFinaliCumulo (DatiFinaliCumuloModel aDatiFinaliCumulo ) throws F3BException ;
  public DatiFinaliCumuloModel ExRicercaDatiFinaliCumuloById ( BigDecimal aIdDatiFinaliCumulo ) throws F3BException ;

  public DatiFinaliCumuloModel ExRicercaDatiFinaliCumuloByIdIstrutt ( BigDecimal aIdIstruttoria ) throws F3BException ;

  public DatiFinaliCumuloAggregatoModel ExRicercaDatiFinaliAggregatiByIdIstruttoria ( BigDecimal aIdIstruttoria ) throws F3BException ;

  // Pena Rideterminata
  public PenaRideterminataCumuloModel ExInserisciPenaRideterminataCumulo (PenaRideterminataCumuloModel aPenaRideterminataCumulo )  throws F3BException ;
  public void ExModificaPenaRideterminataCumulo (PenaRideterminataCumuloModel aPenaRideterminataCumulo ) throws F3BException;
  public void ExCancellaPenaRideterminataCumulo (PenaRideterminataCumuloModel aPenaRideterminataCumulo ) throws F3BException;

  public void ExCRUDPeneRideterminateUlterioriSanzioniCumulo (DatiFinaliCumuloAggregatoModel aDatiFInaliAggregato)  throws F3BException ;
  
  public Vector <ProcedimentoCumulatoModel> ExRicercaProcedimentiClasseIVPerRibaltamentoByIdIstru (BigDecimal aIdIstruttoria, String aChiaveUfficio) throws F3BException;

  // Posizione Giuridica Cumulo
  public PosizioneGiuridicaCumuloModel ExInserisciPosizioneGiuridicaCumulo (PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo )  throws F3BException ;
  public void  ExModificaPosizioneGiuridicaCumulo (PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo ) throws F3BException;

  // Provvedimento di cumulo
  public EventoNotificaModel ExInserisciProvvedimentoCumulo (EventoNotificaModel lEveNot, BigDecimal aIdDatiFinali) throws F3BException;
  public EventoNotificaModel ExModificaProvvedimentoCumulo (EventoNotificaModel lEveNot, BigDecimal aIdDatiFinali) throws F3BException;

  public void ExUpdateValidaProvvedimentoCumulo (EventoModel aEventoModel, Vector<TitoloCumulatoModel> alistaTitoli)  throws F3BException ;

  public ByteArrayOutputStream ExStampaProvvedimentoCumulo (EventoNotificaModel aEventoNotModel
      , FascicoloSiepModel aFascicoloModel
      , UtenteModel aUtenteMod, UfficioModel lUfficioMod)  throws F3BException;


  // MEV 26 Cumulo Step2
  public String ExInserisciDatiFinaliCumuloWithoutSequence(DatiFinaliCumuloModel aDatiFinaliCumulo , Connection lConn) throws F3BException;
  
  public String ExInserisciPosizioneGiuridicaCumuloWithoutSequence(PosizioneGiuridicaCumuloModel aPosizioneGiuridicaCumulo , Connection lConn) throws F3BException;
  
  public String ExInserisciPeneRideterminateCumuloWithoutSequence(PenaRideterminataCumuloModel aPenaRidetCumModel , Connection lConn) throws F3BException;
  
  public String ExInserisciUlerioriSanzioniCumuloWithoutSequence(DatiFinaliUlterioriSanzioniModel aDatiFinaliUlterioriSanzioniModel , Connection lConn) throws F3BException;

  public ByteArrayOutputStream ExStampaComunicazioni (EventoNotificaModel aEventoNotModel, FascicoloSiepModel aFascicoloModel
			, UtenteModel aUtenteMod, UfficioModel lUfficioMod, String lDestinatario)  throws F3BException;
  /* 
 * ISSUE MAC : aggiunto metodo che aggiorna il flag altra causa (posizione giuridica) sul fascicolo
 * Numero MAC : 20191128013
 * Autore    : monica
 * Data      : 19/dic/2019
 * Branch    : 11.2.4
 */
  public void ExUpdateFlagAltraCausaFascicolo(FascicoloSiepModel aFascicolo,
			String flagAltraCausa)  throws F3BException ;
//***** FINE INTERVENTO 20191128013 *****//
  // MEV_2025-48
  public TreeModel getTreeModelCalcoloPenaCumulo(BigDecimal aIdIstruttCumulo) throws F3BException;
}
