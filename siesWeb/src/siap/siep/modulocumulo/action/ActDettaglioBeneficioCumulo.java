package siap.siep.modulocumulo.action;

import java.math.BigDecimal;
import java.util.Vector;

import siap.siep.modulocumulo.controller.IBeneficioCumulo;
import siap.siep.modulocumulo.controller.IPenaAccessoriaCumulo;
import siap.siep.modulocumulo.controller.ITitoloCumulato;
import siap.siep.modulocumulo.model.BeneficioCumuloModel;
import siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel;
import siap.siep.modulocumulo.model.TitoloCumulatoModel;
import siap.siep.tipologiaorario.controller.ITipologiaOrario;
import siap.siep.util.SIEPLookupRemote;

/**
 * <p>
 * Title: ActDettaglioBeneficioCumulo
 * </p>
 * <p>
 * Description: Classe Action per la load dettaglio di Beneficio
 * </p>
 * <p>
 * di titolo Cumulato
 * </p>
 * 
 * @author Intersistemi Italia SPA
 */
@SuppressWarnings("rawtypes")
public class ActDettaglioBeneficioCumulo extends ActionModuloCumulo implements ICostantiBeneficiCumulo {

	protected BeneficioCumuloModel mBenMod = null;
	// indica Beneficio di tipo Indulto
	protected boolean isBeneficioIndulto = false;

	public String processRequest() throws Exception {

		String lId = getRequestStringParameter(CAMPO_ID_BENEFICIO_CUMULO);
		String lPage = "";
		// ====================================================================================
		// Recupero i dati del TITOLO e ISTRUTTORIA CUMULO, da passare alla form di Dettaglio
		// ====================================================================================
		super.getDatiIstruttoria();
		super.getDatiTitoloCumulato();

		BigDecimal lIdTito = getRequestBigDecimalParameter(ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO);
		//
		// Recupero i dati del Beneficio_Cumulo
		IBeneficioCumulo lCtrl = SIEPLookupRemote.getBeneficioCumuloRemote();
		mBenMod = lCtrl.ExRicercaBeneficioCumuloByKey(new BigDecimal(lId));
		setRequestAttribute("beneficio", mBenMod);

		ITitoloCumulato lCtrlT = SIEPLookupRemote.getTitoloCumulatoRemote();
		TitoloCumulatoModel lTitoloRevocaBen = null;
		TitoloCumulatoModel lTitoloRevocaNMZ = null;

		if (mBenMod != null && mBenMod.getIdBeneficioCumulo() != null) {
			// Vedo se il Beneficio è stato Revocato; cioè se 'TIT_ID_TITOLO_CUMULO_COLLEGATO' is NOT NULL
			if (mBenMod.getTitIdTitoloCumulatoCollegato() != null) {
				// Cerca i dati della REVOCA attraverso il Titolo_Cumulato_Collegato
				lTitoloRevocaBen = (TitoloCumulatoModel) lCtrlT
						.ExRicercaTitoloCumulatoById(mBenMod.getTitIdTitoloCumulatoCollegato());
				if (lTitoloRevocaBen != null && lTitoloRevocaBen.getIdTitoloCumulato() != null) {
					setRequestAttribute("TitoloRevocaBeneficio", lTitoloRevocaBen);
				}
			}

			if ("03".equals(mBenMod.getCodTipoBeneficio()) || "04".equals(mBenMod.getCodTipoBeneficio())) { // COD_TIPO_BENEFICIO
																											// =
																											// 03(indulto)-
																											// 04(amnistia)
				isBeneficioIndulto = true;
				PenaAccessoriaCumuloModel lPenAccMod = new PenaAccessoriaCumuloModel();
				lPenAccMod.setBenIdBeneficioCumulo(mBenMod.getIdBeneficioCumulo());
				lPenAccMod.setTitIdTitoloCumulato(lIdTito);

				IPenaAccessoriaCumulo lCtrlPen = SIEPLookupRemote.getPenaAccessoriaCumuloRemote();
				Vector lPenAcce = lCtrlPen.ExRicercaPenaAccessoriaCumulo(lPenAccMod);
				setRequestAttribute("peneaccessorie", lPenAcce);

				lPage = PG_LOAD_DETTAGLIO_BENEFICIO_INDULTO_CUMULO;
			} else if ("01".equals(mBenMod.getCodTipoBeneficio())
					|| "02".equals(mBenMod.getCodTipoBeneficio())) { // COD_TIPO_BENEFICIO = 01(Sospensione
																		// Condizionale)- 02(Non Menzione)
																		// controllo se è stata inserita una
																		// non menzione
				BeneficioCumuloModel llBenNMMod = lCtrl
						.ExRicercaBeneficioCumuloByBenIdBeneficioCum(mBenMod.getIdBeneficioCumulo());
				setRequestAttribute("beneficiononmenzione", llBenNMMod);

				ITipologiaOrario CtrlTip = SIEPLookupRemote.getTipologiaOrarioRemote();
				Vector lTipologia = CtrlTip
						.ExRicercaTipologiaOrarioByIdBeneficioCumulo(mBenMod.getIdBeneficioCumulo());
				setRequestAttribute("tipologiaorario", lTipologia);

				// Vedo se la NonMenzione è stata Revocata; cioè se 'TIT_ID_TITOLO_CUMULO_COLLEGATO' is NOT
				// NULL ????
				if (llBenNMMod != null && llBenNMMod.getIdBeneficioCumulo() != null
						&& llBenNMMod.getTitIdTitoloCumulatoCollegato() != null) {
					// Cerca i dati della REVOCA attraverso il Titolo_Cumulato_Collegato
					lTitoloRevocaNMZ = (TitoloCumulatoModel) lCtrlT
							.ExRicercaTitoloCumulatoById(llBenNMMod.getTitIdTitoloCumulatoCollegato());
					if (lTitoloRevocaNMZ != null && lTitoloRevocaNMZ.getIdTitoloCumulato() != null) {
						setRequestAttribute("TitoloRevocaNonMenzione", lTitoloRevocaNMZ);
					}
				}

				lPage = PG_LOAD_DETTAGLIO_BENEFICIO_CUMULO;
			}

		}

		return lPage;
	}

}