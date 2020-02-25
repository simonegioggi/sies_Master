package siap.sige.detenzione.action;

/**
* <p>Title: ActModificaDetenzioneFasSige</p>
* <p>Description: Classe Action per l'inserimento del Luogo Detenzione in Sige</p>
* <p>Copyright: Copyright (c) 2009</p>
* <p>Company: </p>
* @version 1.0
*/

import java.math.BigDecimal;
import java.util.Date;

import siap.sico.web.ActionSiap;
import siap.siep.altracausa.action.ICostantiAltraCausa;
import siap.siep.altracausa.model.AltraCausaModel;
import siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione;
import siap.siep.luogodetenzione.model.LuogoDetenzioneModel;
import siap.sige.detenzione.controller.IFasSigeDetenzione;
import siap.sige.detenzione.model.FasSigeDetenzioneModel;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.util.SIGELookupRemote;
import f3b.util.DateUtils;
import f3b.web.IWebConstants;

public class ActModificaDetenzioneFasSige extends ActionSiap implements ICostantiLuogoDetenzione
{
/**
 * Modifica il Luogo Detenzione e Altro Luogo
 * @return la Pagina JSP da visualizzare
 * @throws Exception
 */
  public String processRequest() throws Exception
  {
    FascicoloSigeEstesoModel lFasEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    BigDecimal lIdFascicoloSIGE = lFasEsteso.getFascicoloSige().getIdFascicoloSige();
    String     lCodTipoIstitutoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE);
    String     lIdLuogoDetenzione = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_ID_LUOGO_DETENZIONE);
    String     lIdAltraCausa = getRequestStringParameter(ICostantiAltraCausa.CAMPO_ID_ALTRA_CAUSA);
    String     lAltroLuogo = getRequestStringParameter(ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO);
    Date       lDataEmissione = getRequestDateParameter(CAMPO_ANNO_DATA_INIZIO_DETENZIONE,CAMPO_MESE_DATA_INIZIO_DETENZIONE,CAMPO_GIORNO_DATA_INIZIO_DETENZIONE);
    Date       lDataFineDetenzione = getRequestDateParameter(CAMPO_ANNO_DATA_FINE_DETENZIONE,CAMPO_MESE_DATA_FINE_DETENZIONE,CAMPO_GIORNO_DATA_FINE_DETENZIONE);

    LuogoDetenzioneModel lLuogoDetenzione  = new LuogoDetenzioneModel();
    AltraCausaModel lAltraCausa     = new AltraCausaModel();

    // Crea Model Luogo detenzione per insert
    FasSigeDetenzioneModel lDetenzioneSige = new FasSigeDetenzioneModel();
    lDetenzioneSige.setIdFasSigeDetenzione ( new BigDecimal(getRequestStringParameter(ICostantiFasSigeDetenzione.CAMPO_ID_FAS_SIGE_DETENZIONE)) );

    // Valorizza Campi Luogo Detenzione
    if (lIdLuogoDetenzione.length()>0)
    	lLuogoDetenzione.setIdLuogoDetenzione(new BigDecimal (lIdLuogoDetenzione));
    lLuogoDetenzione.setDataInizioDetenzione(lDataEmissione);
    lLuogoDetenzione.setDataFineDetenzione(lDataFineDetenzione);
    lLuogoDetenzione.setIstDetIdIstitutoDetenzione(lCodTipoIstitutoDetenzione);
    //lLuogoDetenzione.setPosGiuIdPosizioneGiuridica( new BigDecimal(lFasEsteso.getFascicoloSige().getCodPosizioneGiuridica()) );
    lLuogoDetenzione.setAltroLuogo(lAltroLuogo);
    lLuogoDetenzione.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lLuogoDetenzione.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lLuogoDetenzione.setDataAggiornamento(DateUtils.getSysDate());

    // Valorizza Campi AltraCausa
    if (lAltraCausa != null)
    {
      if (lIdAltraCausa.length()>0)
      	lAltraCausa.setIdAltraCausa(new BigDecimal (lIdAltraCausa) );
      lAltraCausa.setIstDetIdIstitutoDetenzione(getRequestStringParameter(ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE));
      lAltraCausa.setAltroLuogo(getRequestStringParameter(ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA));
      lAltraCausa.setCodOperatoreAggiornamento(getCodUtenteConnesso());
      lAltraCausa.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
      lAltraCausa.setDataAggiornamento(DateUtils.getSysDate());
    }

    lDetenzioneSige.setFasIdFasSige(lIdFascicoloSIGE);

    if (lIdLuogoDetenzione.length()>0)
    	lDetenzioneSige.setLdIdLuogoDetenzione( new BigDecimal (lIdLuogoDetenzione) );
    if (lIdAltraCausa.length()>0)
    	lDetenzioneSige.setAcIdAltraCausa(new BigDecimal (lIdAltraCausa) );
    lDetenzioneSige.setCodUfficioAggiornamento(getCodUfficioUtenteConnesso());
    lDetenzioneSige.setCodOperatoreAggiornamento(getCodUtenteConnesso());
    lDetenzioneSige.setDataAggiornamento(DateUtils.getSysDate());
    lDetenzioneSige.setLuogoDetenzione(lLuogoDetenzione);
    lDetenzioneSige.setAltraCausa(lAltraCausa);

    // Controller
    IFasSigeDetenzione lCtrl = SIGELookupRemote.getFasSigeDetenzioneRemote();
    lDetenzioneSige = lCtrl.ExModificaFasSigeDetenzione(lDetenzioneSige);

    lFasEsteso.setDetenzione(lDetenzioneSige);
    // Si aggiorna in sessione il fascicolo SIGE.
    setSessionAttribute("FascicoloSigeEsteso", lFasEsteso);
    
    // Se l'operazione è andata a buon fine il fascicolo in sessione
    // risulta aggiornato automaticamente
    //String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.detenzione.action.ActLoadDettaglioDetenzioneFasSige&"+ICostantiFasSigeDetenzione.CAMPO_ID_FAS_SIGE_DETENZIONE+"="+lDetenzioneSige.getIdFasSigeDetenzione().toString();
    String lPage = IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.detenzione.action.ActRicercaLuogoDetenzioneByProcedimentoSige";
    return lPage; 
  }
}
