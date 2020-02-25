package siap.sige.impugnazione.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Vector;

import siap.sico.evento.model.EventoModel;
import siap.sico.utente.model.UtenteModel;
import siap.sige.fascicolo.model.FascicoloSigeModel;
import siap.sige.impugnazione.model.ImpugnazioneSigeModel;
import f3b.util.F3BException;

/**
* <p>Title: ImpugnazioneSigeController</p>
* <p>Description: Classe Controller per Impugnazione Sige</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

public interface IImpugnazioneSige
{
  public ImpugnazioneSigeModel ExInserisciImpugnazione (ImpugnazioneSigeModel aImpugnazione, BigDecimal aIdProvvedimento, EventoModel evento, FascicoloSigeModel aFascicoloSige)
              throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazione (ImpugnazioneSigeModel aImpugnazione )
              throws F3BException;
  public ImpugnazioneSigeModel ExRicercaImpugnazioneByKey (BigDecimal aKey)
              throws F3BException;
  public ImpugnazioneSigeModel ExModificaImpugnazione (ImpugnazioneSigeModel aImpugnazione )
              throws F3BException;
  public ImpugnazioneSigeModel ExRicercaImpugnazioneByIdEvento (BigDecimal aIdEvento)
  			  throws F3BException;
  public ImpugnazioneSigeModel ExRicercaImpugnazioneByIdProvvTipoImp (BigDecimal aIdEvento, String aTipo)
              throws F3BException;
  public String ExRicercaDataRicorso ( BigDecimal aIdEvento)
      		  throws F3BException;
  public void ExAnnullaImpugnazione (ImpugnazioneSigeModel aImpugnazione, FascicoloSigeModel aFascicoloSige)
              throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazioniAnnullateByProv (BigDecimal aIdProv)
  			  throws F3BException;
  public String ExRicercaDateRicorsi ( BigDecimal aIdEvento)
              throws F3BException;
  
  public boolean ExVerificaImpugnazione (BigDecimal aFascKey, String aTipoEvento )
              throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazioniFascicoloSige (BigDecimal aFascKey, String aTipoRicorso )
	          throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazioniProvvedimentoSige (BigDecimal aIdProv, String aTipoRicorso)
	  	      throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazioniProvvedimentoSige (BigDecimal aIdProv)
    throws F3BException;
  public Vector <ImpugnazioneSigeModel>ExRicercaImpugnazioneByIdProvvedimentoSige (BigDecimal aKey)
          throws F3BException;
  
  //public ByteArrayOutputStream ExStampaImpugnazioneSige ( BigDecimal aIdFascicolo, EventoModel lEvento, String aCodUff, UtenteModel aUtenteModel)  
  public ByteArrayOutputStream ExStampaImpugnazioneSige ( BigDecimal aIdFascicolo, BigDecimal aIdImpugnazione, String aCodTemplate, String aCodUff, UtenteModel aUtenteModel)  
  			  throws F3BException;
  
  public Vector <ImpugnazioneSigeModel> ExRicercaImpugnazioniFascicoloSigePerEsitoDecisione (BigDecimal aIdProv, String aTipoRicorso) throws F3BException;
  
  public ImpugnazioneSigeModel ExImpostaEsitoImpugnazione (ImpugnazioneSigeModel impugnazione, EventoModel evento, BigDecimal idFascicolo)throws F3BException;
  
  public ImpugnazioneSigeModel ExAggiornaEsitoImpugnazione (ImpugnazioneSigeModel impugnazione, BigDecimal idFascicolo)throws F3BException;
  
  public ImpugnazioneSigeModel ExEliminaImpugnazione (ImpugnazioneSigeModel impugnazione)throws F3BException;
  
  public Vector<ImpugnazioneSigeModel> ExRicercaImpugnazioniAccolteByIdProvvedimento (BigDecimal idProvvedimento) throws F3BException;
 //@emma 13072018 intervento post COLLAUDO 11.2 (aggiungo metodo sull'interfaccia)
  public ImpugnazioneSigeModel ExEliminaEsitoImpugnazione (ImpugnazioneSigeModel impugnazione, BigDecimal idFascicolo)throws F3BException;
}
