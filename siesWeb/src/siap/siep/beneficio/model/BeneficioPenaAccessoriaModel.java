package siap.siep.beneficio.model;


import siap.siep.penaaccessoria.model.PenaAccessoriaModel;

/**
 * <p>Title: BeneficioPenaAccessoriaModel</p>
 * <p>Description: Aggragato di Beneficio e PenaAccessoria</p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: Eutelia</p>
 */
public class BeneficioPenaAccessoriaModel
{
  private BeneficioModel mBeneficio;
  private PenaAccessoriaModel mPenaAccessoria;
  private boolean mPresenzaPenaAccessoria;

  public BeneficioPenaAccessoriaModel()
  {
	  mBeneficio = new BeneficioModel();
	  mPenaAccessoria = new PenaAccessoriaModel();
	  mPresenzaPenaAccessoria = false;
  }

//Metodi get
  public BeneficioModel getBeneficio() {return  mBeneficio;}
  public PenaAccessoriaModel getPenaAccessoria() {return  mPenaAccessoria;}
  public boolean getPresenzaPenaAccessoria() {return  mPresenzaPenaAccessoria;}

//Metodi set
  public void setBeneficio(BeneficioModel aValore) {  mBeneficio = aValore;}
  public void setPenaAccessoria(PenaAccessoriaModel aValore) {  mPenaAccessoria = aValore;}
  public void setPresenzaPenaAccessoria(boolean aValore) {  mPresenzaPenaAccessoria = aValore;} 
}