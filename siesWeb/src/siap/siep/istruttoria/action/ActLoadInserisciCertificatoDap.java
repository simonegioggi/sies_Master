package siap.siep.istruttoria.action;

import siap.sico.web.ActionSiap;
import siap.siep.fascicolo.action.ICostantiFascicoloSiep;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo;
import f3b.util.DateUtils;


/**
 *
 * <p>Title: ActLoadInserisciCertificatoDap</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ActLoadInserisciCertificatoDap extends ActionSiap implements ICostantiIstruttoria
{
   public String processRequest() throws Exception
   {

     if( isSessionAttributeNullObj("fascicolo") )
        {
              return ICostantiFascicoloSiep.REDIRECT_FASCICOLO_RICERCATO + getClass().getName();
        }

     this.isFascicoloSiepDiCompetenza();
     this.isEventoNonValidato();
     FascicoloSiepModel lFascicoloModel = (FascicoloSiepModel)getSessionAttribute("fascicolo");
     setRequestAttribute("datairrevocabilita", DateUtils.getDateToString(lFascicoloModel.getDataIrrevocabilita(),"dd/MM/yyyy"));
     
  // Se vengo da IstruttoriaCUMULO
     if(!isRequestParameterNullObj(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO) )
     {
     	setRequestAttribute("IdIstruttoriaCumulo", getRequestStringParameter(ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO));
     }

     return PG_LOAD_INSERISCI_CERTIFICATO_DAP;
   }


}