package siap.siep.istruttoriacumulo.controller;

import java.sql.Connection;
import java.util.Date;
import java.util.Vector;

import f3b.util.F3BException;
import f3b.util.xml.TreeModel;

import siap.sico.evento.model.EventoNotificaModel;
import siap.sico.ufficio.model.UfficioModel;
import siap.sico.utente.model.UtenteModel;
import siap.siep.fascicolo.model.FascicoloSiepModel;
import siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel;
import siap.siep.modulocumulo.model.RichiesteInviateCumModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;

public interface IStampaCumulo {

	public TreeModel prelevaDatiIstruttoriaCumulo(FascicoloSiepModel lFascicoloModel,
			UtenteModel aUtenteModel, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, Date lDataEmissione) throws F3BException;

	public TreeModel prelevaDatiComunicazioniCumulo(FascicoloSiepModel lFascicoloModel,
			UtenteModel aUtenteModel, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, EventoNotificaModel aEventoNotModel,
			String lDestinatario) throws F3BException;

	public TreeModel prelevaDatiIstruttoriaPerPropostaCumulo(FascicoloSiepModel lFascicoloModel,
			UtenteModel aUtenteModel, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			Vector<TitoloCumulatoModel> aListaTitoli, String lFunzione) throws F3BException;

	public TreeModel prelevaDatiRichiestaInviataCumulo(Connection lConn, TreeModel lTreeMod,
			UtenteModel aUtenteModel, UfficioModel lUfficioMod, IstruttoriaCumuloModel aIstruttoriaCumulo,
			RichiesteInviateCumModel lRichMod) throws F3BException;
  
}