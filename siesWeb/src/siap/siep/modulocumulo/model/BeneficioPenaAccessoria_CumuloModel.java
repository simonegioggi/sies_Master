package siap.siep.modulocumulo.model;

import java.util.Vector;

/**
 * <p>Title: BeneficioPenaAccessoria_CumuloModel</p>
 * <p>Description: Aggragato di BeneficioCumulo e PenaAccessoriaCumulo</p>
 */
public class BeneficioPenaAccessoria_CumuloModel
{
  private BeneficioCumuloModel mBeneficio;
  private PenaAccessoriaCumuloModel mPenaAccessoria;
  private Vector <PenaAccessoriaCumuloModel> mListaPA_Cumulo;
  private boolean mPresenzaPenaAccessoriaCumulo;
//  
  private TitoloCumulatoModel mTitoloCumulatoRevocante;

  public BeneficioPenaAccessoria_CumuloModel()
  {
	  mBeneficio = new BeneficioCumuloModel();
	  mPenaAccessoria = new PenaAccessoriaCumuloModel();
	  mPresenzaPenaAccessoriaCumulo = false;
	  mTitoloCumulatoRevocante = new TitoloCumulatoModel();
  }

//Metodi get
  public BeneficioCumuloModel getBeneficioCumulo() 				{return  mBeneficio;}
  public PenaAccessoriaCumuloModel getPenaAccessoriaCumulo() 	{return  mPenaAccessoria;}
  public boolean getPresenzaPenaAccessoriaCumulo()				{return  mPresenzaPenaAccessoriaCumulo;}
  public Vector <PenaAccessoriaCumuloModel> getListaPACumulo() 	{ return mListaPA_Cumulo; }
  public TitoloCumulatoModel getTitoloCumulatoRevocante()		{ return mTitoloCumulatoRevocante; }

//Metodi set
  public void setBeneficioCumulo(BeneficioCumuloModel aValore) 				{  mBeneficio = aValore;}
  public void setPenaAccessoriaCumulo(PenaAccessoriaCumuloModel aValore) 	{  mPenaAccessoria = aValore;}
  public void setPresenzaPenaAccessoriaCumulo(boolean aValore)				{  mPresenzaPenaAccessoriaCumulo = aValore;}
  public void  setListaPACumulo (Vector <PenaAccessoriaCumuloModel> aValore) 	{  mListaPA_Cumulo = aValore; } 
  public void setTitoloCumulatoRevocante (TitoloCumulatoModel aValore)		{  mTitoloCumulatoRevocante = aValore;}
  
}
