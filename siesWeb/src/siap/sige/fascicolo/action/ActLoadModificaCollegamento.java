package siap.sige.fascicolo.action;

import siap.sico.decodifiche.controller.DecodificheManager;
import siap.sico.soggetto.controller.ISoggetto;
import siap.sico.soggetto.model.SoggettoModel;
import siap.sico.ufficio.controller.UfficioUtils;
import siap.sico.util.SICOLookupRemote;
import siap.sige.fascicolo.controller.IFascicoloSige;
import siap.sige.fascicolo.model.FascicoloSigeEstesoModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.util.SIGELookupRemote;
import siap.sige.web.ActionSige;
import f3b.web.html.Option;

/**
* <p>Title: ActLoadModificaCollegamento</p>
* <p>Description: Classe Action per la load Modifica del Procedimento Collegato</p>
* <p>Copyright: Copyright (c) 2006</p>
* <p>Company: Engineering S.p.A.</p>
* @version 1.0
*/
public class ActLoadModificaCollegamento extends ActionSige implements ICostantiFascicoloSige
{
    public String processRequest() throws Exception
    {
      // Si ricava il Fascicolo dalla sessione
      FascicoloSigeEstesoModel lFasSigeEsteso = (FascicoloSigeEstesoModel)getSessionAttribute("FascicoloSigeEsteso");

      // Fascicolo Collegato (Padre)
      IFascicoloSige lCtrl = SIGELookupRemote.getFascicoloSigeRemote();

      FascicoloSigeModel lFasSigePadre = null;
      if(lFasSigeEsteso.getFascicoloSige().getIdFascicoloSigeOrigine()!=null )
    	  lFasSigePadre = lCtrl.ExRicercaFascicoloSigeByKey(lFasSigeEsteso.getFascicoloSige().getIdFascicoloSigeOrigine());

      if(lFasSigePadre != null && lFasSigePadre.getDescrUfficio() != null && lFasSigePadre.getDescrUfficio().equals("")){
    	  lFasSigePadre.setDescrUfficio( (UfficioUtils.getUfficioByCodUfficio(lFasSigePadre.getChiaveUfficio())).getDescrComune() );
      }

      ISoggetto lSogCtrl = SICOLookupRemote.getSoggettoRemote();
      SoggettoModel lSoggFascPadre = new SoggettoModel();
      if(lFasSigePadre != null && lFasSigePadre.getSogIdSoggetto() != null){
    	  lSoggFascPadre = lSogCtrl.ExRicercaSoggettoByKey(lFasSigePadre.getSogIdSoggetto());
      }

      // Si Imposta il Tipo Ufficio.
      Option lOption = new Option( DecodificheManager.getInstance().getTipoUfficioSige(), lFasSigePadre.getCodTipoUfficioInserimento(), 46);
      String[] lFilter = {"DIB","DIBM","CAP"};
      lOption.setFilter(lFilter);
      setRequestAttribute("tipoUfficioSIGE", "" + lOption );

      setRequestAttribute("fascicoloPadre", lFasSigePadre);
      setRequestAttribute("soggFascPadre", lSoggFascPadre);
      setRequestAttribute("modalita", "M");

      // Parametro per individuare la provenienza.
      String aProvenienza = getRequestStringParameter("provenienza");
      setRequestAttribute("provenienza", aProvenienza);
      setRequestAttribute("fascicoloSigeEsteso", lFasSigeEsteso);

      return PG_LOAD_MODIFICACOLLEGAMENTO;
    }
}
