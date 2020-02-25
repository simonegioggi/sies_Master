package siap.sico.residenza.action;

import java.math.BigDecimal;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.residenza.controller.IResidenza;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sico.util.SICOLookupRemote;
import siap.sico.web.ActionSiap;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
* <p>Title: ActInserisciResidenza</p>
* <p>Description: Classe Action per l'inserimento di Residenza</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public class ActInserisciResidenza extends ActionSiap implements ICostantiResidenza
{
  /**
  * Azione di Inserimento della Residenza
  * @return Nome della pagina JSP da visualizzare
  * al termine dell'elaborazione
  * @throws F3BException
  */
  public String processRequest() throws F3BException
  {
    BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);

    ResidenzaModel lResMod = new ResidenzaModel();

    if(!this.isRequestParameterNullObj(CAMPO_COD_STATO))
    {
      lResMod.setCodStato(getRequestStringParameter(CAMPO_COD_STATO));
    }

    if (getRequestStringParameter(CAMPO_DESCR_COMUNE).length() > 1)
    {
	    //Recupero dati del Luogo
	    ComuneModel lComMod;
	    if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE) &&
	    	getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 0) {
	    	// se presente dal codice comune (e descrizione)
	    	lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE), getRequestStringParameter(CAMPO_DESCR_COMUNE)));
	    }
	    else {
	    	// altrimenti dalla sola descrizione (rischio omonimi)
	    	lComMod = new ComuneModel(getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));    	
	    }
	    
      //ComuneModel lComMod = new ComuneModel(getCodComuneByDescr(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
      lResMod.setCodComune(lComMod.getCodComune());
      lResMod.setCodProvincia(lComMod.getCodProvincia());
    }
    else
     {
       lResMod.setCodProvincia("-");
       lResMod.setCodComune("-");
     }
     if(!this.isRequestParameterNullObj(CAMPO_CAP))
    {
      lResMod.setCap(getRequestStringParameter(CAMPO_CAP));
    }
    if(!this.isRequestParameterNullObj(CAMPO_INDIRIZZO))
    {
      lResMod.setIndirizzo(getRequestStringParameter(CAMPO_INDIRIZZO));
    }

    // 'Residenza' (R), 'Domicilio' (D)
    if(!this.isRequestParameterNullObj(CAMPO_COD_TIPO_RESIDENZA))
    {
      lResMod.setCodTipoResidenza(getRequestStringParameter(CAMPO_COD_TIPO_RESIDENZA));
    }
    if(!this.isRequestParameterNullObj(CAMPO_DESC_COMUNE_ESTERO))
    {
      lResMod.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO));
    }

    lResMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lResMod.setDataInserimento(DateUtils.getSysDate());
    lResMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lResMod.setSogIdSoggetto(lIdSoggetto);

    IResidenza lCtrl = SICOLookupRemote.getResidenzaRemote();
    lResMod = lCtrl.ExInserisciResidenza(lResMod);

    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sico.residenza.action.ActLoadDettaglioResidenza&"+CAMPO_ID_RESIDENZA+"="+lResMod.getIdResidenza().toString();
  }
}