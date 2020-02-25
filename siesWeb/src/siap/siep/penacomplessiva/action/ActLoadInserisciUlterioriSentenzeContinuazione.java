package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
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
public class ActLoadInserisciUlterioriSentenzeContinuazione extends ActionSiap implements ICostantiPenaComplessiva
{
  public String processRequest() throws Exception
  {
    FascicoloSiepModel lFasMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    //Controllo presenza record (c'e' 1 Pena Complessiva per fascicolo)
    if( ! isRequestParameterNullObj(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP) )
    {
      BigDecimal lIdFascicolo = getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP);

      IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();

      PenaComplessivaSanzioneSostitutivaModel lPenSanMod = null;
      lPenSanMod = lCtrl.ExRicercaPenaComplessivaSanzioneSostitutivaByIdFascicoloSiep(lIdFascicolo);

      if(lPenSanMod != null)
        throw new F3BException( F3BException.USER_MESSAGE, "Pena Complessiva già inserita" );
    }

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

    this.isFascicoloSiepDiCompetenza();

    // 21/06/2010 Sostituzione Elenco Autorità Emittenti
 		// Option lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
    Option lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
    setRequestAttribute("autoritaSentenza", ""+lOption );

    lOption = new Option( DecodificheManager.getInstance().getTipoContinuazione(), "-");
    setRequestAttribute("tipoContinuazione", ""+lOption );

    // Imposta Modalità.
    setRequestAttribute("modalita", "I");

    BigDecimal lIdPena = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);
    setRequestAttribute("lIdPenaComplessiva", lIdPena);

    return PG_LOAD_INSERISCI_ULTERIORI_CONTINUAZIONI;  //restituisce la jsp di VIEW
  }
}