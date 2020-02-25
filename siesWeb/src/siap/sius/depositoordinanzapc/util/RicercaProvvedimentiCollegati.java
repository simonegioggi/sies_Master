package siap.sius.depositoordinanzapc.util;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.evento.controller.IEvento;
import siap.sico.evento.model.EventoModel;
import siap.sico.ufficio.controller.IUfficio;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.util.SICOLookupRemote;
import siap.sius.depositodecreto.controller.IDepositoDecreto;
import siap.sius.depositodecreto.model.DepositoDecretoModel;
import siap.sius.depositoordinanzapc.controller.IDepositoOrdinanzaPc;
import siap.sius.depositoordinanzapc.model.DepositoOrdinanzaPcModel;
import siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel;
import siap.sius.generaleprocedimento.controller.IGeneraleProcedimento;
import siap.sius.generaleprocedimento.model.GeneraleProcedimentoModel;
import siap.sius.util.SIUSLookupRemote;
import f3b.log.LogF3B;


/**
 * <p>Title: RicercaProvvedimentiCollegati. </p>
 * <p>Description:
 * La classe raggruppa funzioni di utilità usati per la ricerca di Provvedimenti
 * tra loro collegati a partire da un Fascicolo SIUS Origine.
 * </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class RicercaProvvedimentiCollegati
{
	// [FT] - 03/08/2016 - MAC_LOG - Dichiaro un'istanza di Logger per SIESLog
	private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
  private BigDecimal mIdFasOrigine = null;
  private GeneraleProcedimentoModel mGenProcOrigine = null;

  private IGeneraleProcedimento mGenProcCtrl = null;
  private IUfficio mUffCtrl = null;
  private IDepositoDecreto mDepDecCtrl = null;
  private IDepositoOrdinanzaPc mDepOrdCtrl = null;
  private IEvento mEveCtrl = null;

  public GeneraleProcedimentoModel getGenProcOrigine() { return mGenProcOrigine; }

  public RicercaProvvedimentiCollegati()
  {
    mIdFasOrigine = null;
    mGenProcOrigine = null;
    mGenProcCtrl = null;
    mUffCtrl = null;
    mDepDecCtrl = null;
    mDepOrdCtrl = null;
    mEveCtrl = null;

  }

  public RicercaProvvedimentiCollegati(BigDecimal aIdFasOrigine)
  throws Exception
  {
    mIdFasOrigine = aIdFasOrigine;
    mGenProcCtrl = SIUSLookupRemote.getGeneraleProcedimentoRemote();
    if (aIdFasOrigine != null)
      mGenProcOrigine = mGenProcCtrl.ExRicercaGeneraleProcedimentoByFascicolo(mIdFasOrigine);
  }


  // Ricerca del decreto collegato al Generale Procedimento Origine
  public DepositoDecretoModel RicercaDecreto(String aCodTipoDecreto)
      throws Exception
  {
    DepositoDecretoModel lDepDecMod = null;
    UfficioModel lUffMod = null;
    String lCodUff = null;

    if (mGenProcOrigine != null)
    {
      if (mDepDecCtrl == null)
        mDepDecCtrl = SIUSLookupRemote.getDepositoDecretoRemote();
      lDepDecMod = mDepDecCtrl.ExRicercaDepositoDecretoByGenProc(
          mGenProcOrigine.getIdGeneraleProcedimento(), aCodTipoDecreto);

      if (lDepDecMod != null) {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("ID del Deposito Decreto ->" +
                                 lDepDecMod.getIdDepositoDecreto());
        // Viene ricavato il comune dall'ufficio di emissione
        lCodUff = lDepDecMod.getCodUfficioInserimento();
        if (lCodUff != null) {
          if (mUffCtrl == null)
            mUffCtrl = SICOLookupRemote.getUfficioRemote();
          lUffMod = mUffCtrl.getUfficioByKey(lCodUff);
          lDepDecMod.setDescrUfficioInserimento(lUffMod.getDescrComune());
        }
      }
    }
    return lDepDecMod;
  }


  // Ricerca dell'ordinanza collegata al Generale Procedimento Origine
  public OrdinanzaEventoTenoriPrescrizioniModel RicercaOrdinanza(String aCodTipoOrdinanza)
  throws Exception
  {
    DepositoOrdinanzaPcModel lDepOrdMod = null;
    UfficioModel lUffMod = null;
    String lCodUff = null;
    OrdinanzaEventoTenoriPrescrizioniModel lOrdEveTenPres = new OrdinanzaEventoTenoriPrescrizioniModel();
    EventoModel lEvento = null;

    if (mGenProcOrigine != null)
    {
      if (mDepOrdCtrl == null)
        mDepOrdCtrl = SIUSLookupRemote.getDepositoOrdinanzaPcRemote();
      lDepOrdMod = mDepOrdCtrl.ExRicercaDepositoOrdinanzaPcByGenProcTipoOrd( mGenProcOrigine.getIdGeneraleProcedimento(), aCodTipoOrdinanza);

      if (lDepOrdMod != null) {
        // [FT] - 03/08/2016 - MAC_LOG - Utilizzo la variabile di istanza siesLogger al posto di LogF3B.getLogger()
        siesLogger.debug("ID del Deposito Ordinanza ->" +  lDepOrdMod.getIdDepositoOrdinanzaPc());
        // Viene ricavato il comune dall'ufficio di emissione
        lCodUff = lDepOrdMod.getCodUfficioInserimento();
        if (lCodUff != null) {
          if (mUffCtrl == null)
            mUffCtrl = SICOLookupRemote.getUfficioRemote();
          lUffMod = mUffCtrl.getUfficioByKey(lCodUff);
          lDepOrdMod.setDescrUfficioInserimento(lUffMod.getDescrComune());

          lOrdEveTenPres.setOrdinanza(lDepOrdMod);

         // Occorre risalire all'evento per la data di emissione
          if (mEveCtrl == null)
            mEveCtrl = SICOLookupRemote.getEventoRemote();
            lEvento = mEveCtrl.ExRicercaEventoByKey(lDepOrdMod.getIdEventoGenerato());
            lOrdEveTenPres.setEvento(lEvento);
        }

      }
    }
    return lOrdEveTenPres;
  }

}