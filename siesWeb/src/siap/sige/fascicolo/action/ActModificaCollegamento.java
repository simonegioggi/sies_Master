package siap.sige.fascicolo.action;

import java.math.BigDecimal;

import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.util.DateUtils;
import f3b.util.F3BException;
import f3b.web.IWebConstants;

/**
 * <p>Title: ActModificaCollegamento</p>
 * <p>Description: Classe Action per la Modifica del Procedimento Collegato</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: Engineering S.p.A. </p>
 *  * @version 1.0
 */
public class ActModificaCollegamento extends ActionSige implements ICostantiFascicoloSige
{
  public String processRequest() throws Exception
  {

	// Si ricava il Fascicolo dalla sessione
    FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

    // Recupero del Codice Tipo Ufficio.
    String lCodTipoUfficio = getRequestStringParameter(CAMPO_COD_MITTENTE_ATTO);
    String lDescrComuneUfficio = getRequestStringParameter(CAMPO_DESCR_SEDE_MITTENTE);


    // Ricerca Nuovo Fascicolo Padre.
    FascicoloSigeModel lFasSigePadre = new FascicoloSigeModel();
    String lCodUfficio = getCodUfficioByCodTipoUfficioDescrComune(lCodTipoUfficio, lDescrComuneUfficio ) ;

    // Ricerca Nuovo Fascicolo Padre.
    FascicoloSigeModel lFasSigeRicerca = new FascicoloSigeModel();
    BigDecimal lChiaveAnno = getRequestBigDecimalParameter(CAMPO_CHIAVE_ANNO);
    BigDecimal lChiaveprog = getRequestBigDecimalParameter(CAMPO_CHIAVE_PROGR);
    lFasSigeRicerca.setChiaveAnno(lChiaveAnno);
    lFasSigeRicerca.setChiaveProgr(lChiaveprog);
    lFasSigeRicerca.setChiaveUfficio(lCodUfficio);
    
    IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();
    try
    {
      lFasSigePadre = lCtrl.ExRicercaFascicoloSigeByAnnoProgrCodUfficio(lFasSigeRicerca);

      if (lFasSigePadre == null)
      {
        // Fascicolo Inesistente.
        throw new F3BException(F3BException.USER_MESSAGE,"Fascicolo Inesistente! ");
      }
      // Il Nuovo Fascicolo Padre Esiste in Archivio. Si procede alla modifica del collegamento.
      lFasSigeEsteso.getFascicoloSige().setIdFascicoloSigeOrigine(lFasSigePadre.getIdFascicoloSige());
      lFasSigeEsteso.getFascicoloSige().setCodOperatoreAggiornamento (getCodUtenteConnesso()); //Codice dell'operatore che inserisce
      lFasSigeEsteso.getFascicoloSige().setCodUfficioAggiornamento(getCodUfficioUtenteConnesso()); //Codice dell'operatore che inserisce
      lFasSigeEsteso.getFascicoloSige().setDataAggiornamento( DateUtils.getSysDate());
      
      lFasSigeEsteso.setFascicoloSige(lCtrl.ExModificaIdFascicoloSigeOrigine(lFasSigeEsteso.getFascicoloSige()) );
    }
    catch (Exception ex)
    {
      throw(ex);
    }

    //Si Mette in sessione il fascicolo SIGE col nuovo collegamento.
    setSessionAttribute("FascicoloSigeEsteso", lFasSigeEsteso);

    //restituisce la jsp di VIEW
    return IWebConstants.PG_MAIN + "?" + IWebConstants.ACTION_FIELD + "=siap.sige.fascicolo.action.ActLoadDettaglioProcedimentoCollegato&"+CAMPO_ID_FASCICOLO_SIGE+"="+lFasSigeEsteso.getFascicoloSige().getIdFascicoloSige().toString();
  }
}
