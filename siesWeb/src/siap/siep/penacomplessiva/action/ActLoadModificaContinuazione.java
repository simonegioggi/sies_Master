package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.continuazione.action.ICostantiContinuazione;
import siap.siep.continuazione.controller.IContinuazione;
import siap.siep.continuazione.model.ContinuazioneModel;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciUlterioriSentenzeContinuazione</p>
* <p>Description: Classe Action per la load inserisci di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadModificaContinuazione extends ActionSiap implements ICostantiPenaComplessiva,
                                                                        ICostantiContinuazione
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFasMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    if(lFasMod != null)
    {
      if("01".equals(lFasMod.getCodMotivoArchiviazione())) // COS_STATO_FASCICOLO = ARCHIVIATO/DEFINITO
      {
        RedirectTo lRedirigi = new RedirectTo();

        lRedirigi.setPage( IWebConstants.PG_MAIN );
        lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo" );
        lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, lFasMod.getIdFascicoloSiep().toString());
        setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

        throw new F3BException( F3BException.USER_MESSAGE, "Il fascicolo risulta archiviato" );
      }

      this.isFascicoloSiepDiCompetenza();

      if(lFasMod.getFlagValidato().equals("S"))
      {
        RedirectTo lRedirigi = new RedirectTo();

        lRedirigi.setPage( IWebConstants.PG_MAIN );
        lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo" );
        lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, lFasMod.getIdFascicoloSiep().toString());
        setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

        throw new F3BException( F3BException.USER_MESSAGE, "Il fascicolo risulta validato" );
      }
    }
    else
      throw new F3BException( F3BException.USER_MESSAGE, "Selezionare un procedimento" );

    // riempie il model.
    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_CONTINUAZIONE);

    IContinuazione lCtrl = SIEPLookupRemote.getContinuazioneRemote();
    ContinuazioneModel lContMod = lCtrl.ExRicercaContinuazioneByKey(lId);

    // 21/06/2010 Sostituzione Elenco Autorità Emittenti
    // Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), lContMod.getCodTipoAutorita());
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), lContMod.getCodTipoAutorita());
    setRequestAttribute("autoritaSentenza", ""+lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoContinuazione(), lContMod.getCodTipoContinuazione());
    setRequestAttribute("tipoContinuazione", ""+lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "M");

    setRequestAttribute("continuazione", lContMod);

    return PG_LOAD_MODIFICA_CONTINUAZIONE;  //restituisce la jsp di VIEW
  }
}