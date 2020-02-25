package siap.siep.penacomplessiva.action;

/**
 * <p>Title: ActLoadModificaFascicolo</p>
 * <p>Description: Azione di load della Modifica del Fascicolo</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Bull</p>
 * @version 1.0
 */
import java.math.BigDecimal;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.lock.model.LockModel;
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

public class ActLoadModificaPenaComplessiva extends ActionSiap implements ICostantiPenaComplessiva
{
  public String processRequest() throws Exception
  {

  //Controllo che non si stia lavorando su una entità in modifica ad altri
      LockModel lck =
      lockIfNotLocked("pena complessiva", getRequestStringParameter(CAMPO_ID_PENA_COMPLESSIVA), getCodUtenteConnesso());
      if (lck != null)
      {
        setRequestAttribute (IWebConstants.MESSAGE_TEXT, "La "+lck.getEntity()+" è in gestione ad un altro utente! <BR>Riprovare più tardi!");
        return IWebConstants.PG_MESSAGE;
      }
    FascicoloSiepModel lFasMod = (FascicoloSiepModel)getSessionAttribute("fascicolo");

    if(lFasMod != null)
    {
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

    isFascicoloSiepDiCompetenza();

    preparazioneDati() ;
    
    return PG_LOAD_INSERISCIPENACOMPLESSIVA;
  }
  
  
  
  protected void preparazioneDati() throws Exception
  {
	    // riempie il model.
	    BigDecimal lId = getRequestBigDecimalParameter(CAMPO_ID_PENA_COMPLESSIVA);

	    IPenaComplessiva lCtrl = SIEPLookupRemote.getPenaComplessivaRemote();
	    PenaComplessivaSanzioneSostitutivaModel lPenSanMod = lCtrl.ExRicercaPenaComplessivaSanzioneSostitutivaByKey(lId);

	    Option lOption = new Option(DecodificheManager.getInstance().getTipoPenaDetentivaErgastolo(), lPenSanMod.getPenaComplessiva().getCodTipoPenaDetentiva());
	    setRequestAttribute("tipoPenaDetentiva", ""+lOption );

	    lOption  = new Option( DecodificheManager.getInstance().getFlagLireEuro(), "EUR");
	    setRequestAttribute("valute", ""+lOption );

	/*
	    lOption = new Option( DecodificheManager.getInstance().getTipoUfficioS(), lPenSanMod.getPenaComplessiva().getCodTipoAutoritaCont());
	    setRequestAttribute("autoritaSentenza", ""+lOption );
	*/
	    if(lPenSanMod.getSanzioneSostitutiva() != null)
	      lOption = new Option( DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), lPenSanMod.getSanzioneSostitutiva().getCodTipoSanzione());
	    else
	      lOption = new Option( DecodificheManager.getInstance().getTipoSanzioneSostitutiva(), "-");

	    setRequestAttribute("tipoSanzioneSostitutiva", ""+lOption );

	    // Imposta Modalità.
	    setRequestAttribute("modalita", "M");

	    setRequestAttribute("penaComplessivaSanzioneSostitutiva", lPenSanMod);

  }
  
  
}
