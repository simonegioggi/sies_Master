package siap.siep.penacomplessiva.action;

import java.math.BigDecimal;

import org.apache.log4j.Logger;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.penacomplessiva.controller.IPenaComplessiva;
import siap.siep.penacomplessiva.model.PenaComplessivaSanzioneSostitutivaModel;
import siap.siep.util.SIEPLookupRemote;
import f3b.log.LogF3B;
import f3b.util.F3BException;
import f3b.web.IWebConstants;
import f3b.web.RedirectTo;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadInserisciPenaComplessiva</p>
* <p>Description: Classe Action per la load inserisci di PenaComplessiva</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/
public class ActLoadInserisciPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
{
    private static Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);
    
  public String processRequest() throws Exception
  {

    if(!this.isRequestParameterNullObj("lTipoFunzione")) // paramentro passato solo nel caso di iscrizione guidata
    {
      this.setRequestAttribute("lTipoFunzione", this.getRequestStringParameter("lTipoFunzione"));
    }

    if( isSessionAttributeNullObj("fascicolo") )
    {
       String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD +
       "=siap.siep.fascicolo.action.ActLoadRicercaFascicoloUnivoco&" +
       ICostantiFascicoloSiep.CAMPO_AZIONE_CHIAMANTE + "=" +
       "siap.siep.penacomplessiva.action.ActLoadInserisciPenaComplessiva";

       return lPage;
    }

    this.isFascicoloSiepDiCompetenza();

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
      if("01".equals(lFasMod.getCodStatoFascicolo())) // COS_STATO_FASCICOLO = ARCHIVIATO/DEFINITO
      {
        RedirectTo lRedirigi = new RedirectTo();

        lRedirigi.setPage( IWebConstants.PG_MAIN );
        lRedirigi.setAction("siap.siep.fascicolo.action.ActLoadDettaglioFascicolo" );
        lRedirigi.setParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP, lFasMod.getIdFascicoloSiep().toString());
        setRequestAttribute( IWebConstants.GOTO_PAGE, "" + lRedirigi );

        throw new F3BException( F3BException.USER_MESSAGE, "Il fascicolo risulta archiviato" );
      }
      
      if(lFasMod.getFlagValidato() != null && lFasMod.getFlagValidato().equals("S"))
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

    return preparazioneForm();  //restituisce la jsp di VIEW
  }
  
  protected String preparazioneForm()
  {
	    Option lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentivaErgastolo(), "-");
	    setRequestAttribute("tipoPenaDetentiva", ""+lOption );

	    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
	    setRequestAttribute("valute", ""+lOption );

      // 21/06/2010 Sostituzione Elenco Autorità Emittenti
   		// lOption = new Option(DecodificheManager.getInstance().getTipoUfficioS(), "-");
      lOption = new Option( DecodificheManager.getInstance().getTipoAutoritaEmittente(), "-");
	    setRequestAttribute("autoritaSentenza", ""+lOption );

	    lOption = new Option( DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), "-");
	    siesLogger.debug("lOption = "+lOption);
	    setRequestAttribute("tipoSanzioneSostitutiva", ""+lOption );

	   // MEV_2023-13 - Aggiunta nuova combo
       lOption = new Option( DecodificheManager.getInstance().getTipoPenaSostitutiva(), "-");
       setRequestAttribute("tipoPenaSostitutiva", ""+lOption );
       // MEV_2023-13 - FINE
	        
	    lOption = new Option( DecodificheManager.getInstance().getTipoContinuazione(), "-");
	    setRequestAttribute("tipoContinuazione", ""+lOption );

	    // Imposta Modalità.
	    setRequestAttribute("modalita", "I");

	    return PG_LOAD_INSERISCIPENACOMPLESSIVA;  //restituisce la jsp di VIEW

  }
  
}