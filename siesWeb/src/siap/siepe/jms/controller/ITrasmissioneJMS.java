package siap.siepe.jms.controller;

import java.math.BigDecimal;

import siap.jms.messaggio.model.MessaggioModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siepe.fascicolo.model.FascicoloSiepeEstesoModel;
import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

/**
 * <p>Title:ITrasmissioneJMS </p>
 * <p>Description:Interfaccia per TrasmissioneJMSController </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public interface ITrasmissioneJMS
{
   public MessaggioModel getMessageForAttivita(BigDecimal aKeyAttivita, FascicoloSiepeEstesoModel lfas , UfficioModel aUfficio)
   throws F3BException;

   public MessaggioModel getMessageForRichiesta(BigDecimal aKey, FascicoloSiepeEstesoModel lfas , UfficioModel aUfficio)
   throws F3BException;

   public TreeModel getTreeModelForAttivita(BigDecimal aKeyAttivita, FascicoloSiepeEstesoModel aFasEsteso , UfficioModel aUfficio, UtenteModel aUtente)
   throws F3BException;

   public TreeModel getTreeModelForTrasmissioneAttivita(BigDecimal aKeyAttivita, FascicoloSiepeEstesoModel aFasEsteso , UfficioModel aUfficio, UtenteModel aUtente)
   throws F3BException;

   public TreeModel getTreeModelForTrasmissioneRichiesta(BigDecimal aKeyRichiesta, FascicoloSiepeEstesoModel aFasEsteso , UfficioModel aUfficio, UtenteModel aUtente)
   throws F3BException;

   public TreeModel getTreeModelForRichiesta(BigDecimal aKeyRichiesta,   FascicoloSiepeEstesoModel aFasEsteso, UfficioModel aUfficio,  UtenteModel aUtente) throws F3BException;

   public MessaggioModel getMessageForRelazione(BigDecimal aKey, FascicoloSiepeEstesoModel lfas , UfficioModel aUfficio)
   throws F3BException;

   public TreeModel getTreeModelForFascicolo(FascicoloSiepeEstesoModel aFasEsteso , UfficioModel aUfficio, UtenteModel aUtente)
   throws F3BException;

}
