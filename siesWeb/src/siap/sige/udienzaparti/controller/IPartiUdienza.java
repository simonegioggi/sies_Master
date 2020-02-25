package siap.sige.udienzaparti.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import siap.siep.notifica.model.NotificaModel;
import siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel;
import siap.sige.udienzaparti.model.ParteCivileUdienzaModel;
import siap.sige.udienzaparti.model.ParteOffesaUdienzaModel;
import siap.sige.udienzaparti.model.PartiUdienzaDifensoreModel;
import siap.sige.udienzaparti.model.UdienzaPartiModel;
import f3b.util.F3BException;

/**
 * <p>
 * Title: IPartiUdienza
 * </p>
 * <p>
 * Description: Classe Interfaccia Parti Udienza
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Engineering S.p.A.
 * </p>
 * 
 * @version 1.0
 */
@SuppressWarnings("rawtypes")
public interface IPartiUdienza {

	public Vector<AnagraficaPartiUdienzaModel> ExRicercaPartiUdienzaByIdUdienza(BigDecimal aIdUdienza,
			String aCodTipoPart) throws F3BException;

	public AnagraficaPartiUdienzaModel ExInserisciParteUdienza(AnagraficaPartiUdienzaModel aParteUdienza,
			UdienzaPartiModel aUdienzaParteModel) throws F3BException;

	public AnagraficaPartiUdienzaModel ExRicercaParteUdienzaByKey(BigDecimal aIdSoggetto) throws F3BException;

	public Vector<PartiUdienzaDifensoreModel> ExRicercaDifensoreByIdSoggetto(BigDecimal aIdSoggetto)
			throws F3BException;

	public void ExModificaParteUdienza(AnagraficaPartiUdienzaModel aParteUdienza, ArrayList aNotifiche,
			BigDecimal aEveIdEvento) throws F3BException;

	public NotificaModel ExRicercaNotificaByIdSoggetto(BigDecimal aIdSoggetto) throws F3BException;

	public void ExCancellaParteUdienza(BigDecimal aIdSoggetto) throws F3BException;

	public List<ParteOffesaUdienzaModel> ExRicercaPartiOffesaUdienzaByIdUdienza(
			BigDecimal idUdienzaProcedimentoSige) throws F3BException;

	public List<ParteCivileUdienzaModel> ExRicercaPartiCiviliUdienzaByIdUdienza(
			BigDecimal idUdienzaProcedimentoSige) throws F3BException;

}