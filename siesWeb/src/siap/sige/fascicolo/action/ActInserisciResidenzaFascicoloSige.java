package siap.sige.fascicolo.action;

/**
* <p>Title: ActInserisciResidenzaFascicoloSige</p>
* <p>Description: Classe Action per l'inserimento della Residenza Fascicolo SIGE</p>
* <p>Copyright: Copyright (c) 2008</p>
* <p>Company: </p>
* @version 1.0
*/
import java.math.BigDecimal;

import siap.sico.decodifiche.action.ICostantiComune;
import siap.sico.decodifiche.model.ComuneModel;
import siap.sico.residenza.action.ICostantiResidenza;
import siap.sico.residenza.model.ResidenzaAssociataModel;
import siap.sico.residenza.model.ResidenzaFascicoloSigeModel;
import siap.sico.residenza.model.ResidenzaModel;
import siap.sico.soggetto.action.ICostantiSoggetto;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;

public class ActInserisciResidenzaFascicoloSige extends ActionSige implements ICostantiResidenza
{
  public String processRequest() throws Exception
  {
    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");
    BigDecimal lIdFascicolo = lFasEsteso.getFascicoloSige().getIdFascicoloSige();

    BigDecimal lIdSoggetto = getRequestBigDecimalParameter(ICostantiSoggetto.CAMPO_ID_SOGGETTO);
    BigDecimal lIdResidenza = getRequestBigDecimalParameter(CAMPO_ID_RESIDENZA);

    ResidenzaModel lResMod = new ResidenzaModel();

    String lCodStato = getRequestStringParameter( CAMPO_COD_STATO);
    lResMod.setCodStato( lCodStato );

    // Recupero dati del Comune di Residenza con Controllo omonimia.
    ComuneModel lComMod;
    if (!isRequestParameterNullObj(ICostantiComune.CAMPO_COD_COMUNE_REALE) &&
    	getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE).length() > 1) {
    	// se presente dal codice comune (e descrizione)
    	lComMod = new ComuneModel(getDatiComuneByCodDescrFlagVal(getRequestStringParameter(ICostantiComune.CAMPO_COD_COMUNE_REALE), getRequestStringParameter(CAMPO_DESCR_COMUNE)));
    }
    else {
    	// altrimenti dalla sola descrizione (rischio omonimi)
    	lComMod = new ComuneModel(getDatiComuneByDescrOmonimiaFlagVal(getRequestStringParameter(CAMPO_DESCR_COMUNE)));
    }

    lResMod.setCodComune(lComMod.getCodComune());
    lResMod.setCodProvincia(lComMod.getCodProvincia());

    lResMod.setCap( getRequestStringParameter( CAMPO_CAP) );
    lResMod.setIndirizzo( getRequestStringParameter( CAMPO_INDIRIZZO) );

    // 'Residenza' (R)
    lResMod.setCodTipoResidenza("R");

    lResMod.setCodOperatoreInserimento(getCodUtenteConnesso());
    lResMod.setDataInserimento(DateUtils.getSysDate());
    lResMod.setCodUfficioInserimento(getCodUfficioUtenteConnesso());
    lResMod.setDescComuneEstero(getRequestStringParameter(CAMPO_DESC_COMUNE_ESTERO));

    // NON é SULLA TABELLA RESIDENZA LA VALIDITA' lResMod.setDataInizioValidita( DateUtils.getSysDate() );
    lResMod.setSogIdSoggetto(lIdSoggetto);
    lResMod.setIdResidenza(lIdResidenza);

    ResidenzaFascicoloSigeModel lResFasc = new ResidenzaFascicoloSigeModel();
    lResFasc.setDataInizioValidita(DateUtils.getSysDate());
    lResFasc.setFasSigeIdFascicoloSige(lIdFascicolo);
    lResFasc.setResIdResidenza(lIdResidenza);

    ResidenzaAssociataModel lResAss = new ResidenzaAssociataModel();
    lResAss.setResidenza(lResMod);
    lResAss.setResidenzaFascicoloSige(lResFasc);

    //--- FASCICOLO ---
    IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

    if (!isRequestParameterNullObj("modalita") && this.getRequestStringParameter("modalita").compareToIgnoreCase("M") == 0)
      lResAss = lCtrl.ExModificaResidenzaFascicoloSige(lResAss);
    else
      lResAss = lCtrl.ExInserisciResidenzaFascicoloSige(lResAss);

    // Come back baby !
    return goToRitorno();
  }
}