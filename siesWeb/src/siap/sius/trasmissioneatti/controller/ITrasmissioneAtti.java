package siap.sius.trasmissioneatti.controller;


/**
* <p>Title: TrasmissioneAttiController</p>
* <p>Description: Classe Controller per TrasmissioneAtti</p>
* <p>Copyright: Copyright (c) 2002</p>
* <p>Company: Bull</p>
* @version 1.0
*/

import siap.sico.evento.model.EventoNotificaModel;
import siap.sius.fascicolo.model.FascicoloGPModel;
import f3b.util.F3BException;

public interface ITrasmissioneAtti
{
  public EventoNotificaModel ExTrasmettiAtto (FascicoloGPModel aFascicoloGPModel, EventoNotificaModel aEventoNotModel )
  throws F3BException;

  //public FascicoloGPModel ExRicercaAttoByKey(BigDecimal aIdEvento)
  //throws F3BException;

  //public Vector ExRicercaEventoBySoggetto(SoggettoModel aSogModel )
  //throws F3BException;

  //public FascicoloGPModel ExRicercaEventoByAnnoProgr(BigDecimal aChiaveAnno, BigDecimal aChiaveProgr)
  //throws F3BException;

  //public Vector ExRicercaAttoByEstremiFS(FascicoloSiusModel aFascicoloSius, String sTipoAtto)
  //throws F3BException;

}
