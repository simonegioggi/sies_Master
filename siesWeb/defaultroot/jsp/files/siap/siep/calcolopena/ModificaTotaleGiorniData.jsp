<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode"%>
<%@ page import="siap.siep.misuracautelare.model.MisuraCautelareModel"%>
<%@ page import="siap.siep.misuracautelare.controller.IMisuraCautelare"%>
<%@ page import="siap.siep.util.SIEPLookupRemote"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Vector" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="java.math.RoundingMode"%>
<%@ page import="siap.siep.misuracautelare.action.ICostantiMisuraCautelare" %>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.siep.util.CaricaHTML_Servlet" %>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%
    String totGiorni=request.getParameter("totGiorni"); 
    String idMisuraCautelare=request.getParameter("idMisuraCautelare");
  	
    int numGiorniMisuraCautelare = 0;
    int mesiMisuraCautelare = 0;
	int anniMisuraCautelare = 0;
	BigDecimal numGiorniAnniMesiGiorni = new BigDecimal(totGiorni);
  	BigDecimal periodiGiorniUnoTre = new BigDecimal(3);
	BigDecimal numGiorniAnniMesiGiorniDivTre = new BigDecimal(0);
	BigDecimal numDiff = new BigDecimal(0);
	BigDecimal uno = new BigDecimal(1); 	 
	numGiorniAnniMesiGiorniDivTre=numGiorniAnniMesiGiorni.divide(periodiGiorniUnoTre, 1, RoundingMode.HALF_UP);
	BigDecimal numGiorniAnniMesiGiorniDivTreInteroNegato = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue()).negate();
	numDiff = numGiorniAnniMesiGiorniDivTre.add(numGiorniAnniMesiGiorniDivTreInteroNegato);
	//Se il decimale è >= 6 allora arrotondamento per eccesso.
	//Se il decimale è <= 5 allora arrotondamento per difetto.
	BigDecimal zero5 = new BigDecimal("0.5");
	if(numDiff.compareTo(zero5)==0 || numDiff.compareTo(zero5)==-1){
		//Se il decimale è <= 5 allora arrotondamento per difetto.
		numGiorniAnniMesiGiorniDivTre = new BigDecimal(numGiorniAnniMesiGiorniDivTre.intValue());
		//numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
	} else {
		numGiorniAnniMesiGiorniDivTre = numGiorniAnniMesiGiorniDivTre.add(uno);
	}
	
	numGiorniMisuraCautelare = numGiorniAnniMesiGiorniDivTre.intValue();
	mesiMisuraCautelare=0;
	anniMisuraCautelare=0;
    //I conteggi sono effettuati usando gli algoritmi di ricalcolaGAM	   
	if (numGiorniMisuraCautelare > 30)
    {
      int tmp = numGiorniMisuraCautelare/30 ;
      mesiMisuraCautelare+=tmp;
      numGiorniMisuraCautelare-=tmp*30;
    }       
    if (numGiorniMisuraCautelare==30)
    {
      mesiMisuraCautelare++;
      numGiorniMisuraCautelare=0;
    }        
    if (mesiMisuraCautelare > 12)
    {
      int tmp=mesiMisuraCautelare/12;
      anniMisuraCautelare+=tmp;
      mesiMisuraCautelare-=tmp*12;
    }
    
    if (mesiMisuraCautelare==12)
    {
      anniMisuraCautelare++;
      mesiMisuraCautelare=0;
    }
    
    /* if (mesiMisuraCautelare<12)
    {
      anniMisuraCautelare=0;
    } */
    
    MisuraCautelareModel lMisMod = new MisuraCautelareModel();
    IMisuraCautelare lCtrl = SIEPLookupRemote.getMisuraCautelareRemote();
    BigDecimal idMisCaut = new BigDecimal(idMisuraCautelare);
    lMisMod = lCtrl.ExRicercaMisuraCautelareByKey(idMisCaut);
    //lMisMod.setIdMisuraCautelare(idMisCaut);
    lMisMod.setFlagModificaManuale("S");
    lMisMod.setGiorni(new BigDecimal(totGiorni));
    lMisMod.setNumGiorni(new BigDecimal(numGiorniMisuraCautelare));
    lMisMod.setNumMesi(new BigDecimal(mesiMisuraCautelare));
    lMisMod.setNumAnni(new BigDecimal(anniMisuraCautelare));
    MisuraCautelareModel lMisModRet = lCtrl.ExModificaMisuraCautelare(lMisMod);
    //inizio calcolo giorni  per periodi sovrapposti
    //BigDecimal lIdFascicolo = this.getRequestBigDecimalParameter(ICostantiFascicoloSiep.CAMPO_ID_FASCICOLO_SIEP );
    //Vector lVect = lCtrl.ExRicercaMisureCautelariByIdFascicolo(lIdFascicolo);
    Vector misureCautelari = lCtrl.ExRicercaMisureCautelariByIdFascicolo(lMisMod.getFasSieIdFascicoloSiep());
    
    Iterator itxModManuale = misureCautelari.iterator();
    int sommaColonnaAnniModManuale = 0;
  	int sommaColonnaMesiModManuale = 0;
  	int sommaColonnaGiorniModManuale = 0;
    while ( itxModManuale.hasNext())
    { 
        MisuraCautelareModel lMis = (MisuraCautelareModel)itxModManuale.next();
    	if (lMis.getFlagModificaManuale()!=null && lMis.getFlagModificaManuale().equals("S")){  
        	sommaColonnaAnniModManuale = sommaColonnaAnniModManuale + Integer.parseInt(lMis.getNumAnni().toString());
    		sommaColonnaMesiModManuale = sommaColonnaMesiModManuale + Integer.parseInt(lMis.getNumMesi().toString());
    		sommaColonnaGiorniModManuale = sommaColonnaGiorniModManuale + Integer.parseInt(lMis.getNumGiorni().toString());
    	}  
    }
    
    
    Vector vectorPeriodi = new Vector();   
    
    //inserimento dei periodi per il calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
	//"Cessata al momento del passaggio in giudicato computabili"
	Iterator itx = misureCautelari.iterator();
    int indiceTipoMisura=0;
    while ( itx.hasNext())
    { indiceTipoMisura=indiceTipoMisura+1;
        MisuraCautelareModel lMis = (MisuraCautelareModel)itx.next();
    	if (lMis.getFlagModificaManuale()==null && lMis.getFlagComputabile().equals("S")  && lMis.getDataInizio()!=null && lMis.getDataFine()!=null){  
  	  		vectorPeriodi.add(lMis.getDataInizio());
        	vectorPeriodi.add(lMis.getDataFine());
    	}  
    }
    
    //inizio calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari
    //"Cessata al momento del passaggio in giudicato computabili"  
    Date DItempdata;       
    Date DFtempdata;
    String DItemp=""; String DFtemp="";
    int sommaColonnaAnni = 0;
	int sommaColonnaMesi = 0;
	int sommaColonnaGiorni = 0;
    if (vectorPeriodi != null && vectorPeriodi.size() != 0) {    	    
		for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   	
			// trova l'elemento minimo
			int jMin=i;
			for (int j=i+2;j<vectorPeriodi.size();j=j+1){ 
				if(DateUtils.isLower((Date)vectorPeriodi.get(j),(Date)vectorPeriodi.get(jMin)))// vectorPeriodi[j]<vectorPeriodi[jMin]
					jMin=j;
			}
			//scambia gli elementi con indice i e jMin
			if(i != jMin){
				//scambia
				DItemp = (String)vectorPeriodi.get(jMin);
				DFtemp = (String)vectorPeriodi.get(jMin+1);
				vectorPeriodi.set(jMin, vectorPeriodi.get(i));
				vectorPeriodi.set(jMin+1,vectorPeriodi.get(i+1));
				vectorPeriodi.set(i,DItemp);
				vectorPeriodi.set(i+1,DFtemp);
			}
		}   
    }	
    
    // popolo una tabella di sei colonne e n righe quanti sono i periodi
 	// nomi colonne: Data-inizio | Data-finale | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
 	// nella cella Periodi-continuativi che hanno uguale numero sono continuativi 
 	// ==> si deve fare la somma dei giorni mesi anni
 	int nRighe = vectorPeriodi.size()/2;
 	int nColonne = 7;	
 	String[][] matricePeriodi = new String[nRighe][nColonne];
 	//popolo  Data-inizio | Data-finale |
 	int r=0; int c=0;
 	for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   
 		matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
 		r++;
 	}
 	r=0; c=1;
 	for (int i=1;i<=vectorPeriodi.size()-1;i=i+2){   
 		matricePeriodi[r][c] = vectorPeriodi.get(i).toString();
 		r++;
 	}
 	// popolo le colonne | Anni | Mesi | Giorni | Periodi-continuativi | numero periodo
 	r=0;
 	int numPeriodiContinuativi = 0;
    for (int i=0;i<vectorPeriodi.size()-1;i=i+2){   	
    	DItempdata = (Date)vectorPeriodi.get(i);
    	String ggInizio = DateUtils.getDayToString   (DItempdata).toString();
        String mmInizio   = DateUtils.getMonthToString (DItempdata).toString();
        String aaInizio   = DateUtils.getYearToString  (DItempdata).toString();
        
        DFtempdata = (Date)vectorPeriodi.get(i+1);
    	String ggFine = DateUtils.getDayToString   (DFtempdata).toString();
    	String mmFine   = DateUtils.getMonthToString (DFtempdata).toString();
    	String aaFine   = DateUtils.getYearToString  (DFtempdata).toString();
    	
        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
        String sep = "~#";
        String[] aPairs = new String[3];
        aPairs = calcoloGioniMesiAnni.split(sep);
        String numAnni = aPairs[0];
        String numMesi = aPairs[1];
        String numGiorni = aPairs[2];
        
        boolean periodiContinuativi = false;
        //controllo i periodi se sono continuativi(sovrapposti)
        if(i==0){
        	periodiContinuativi=true;//non cambia il numero del periodo
        } else {
        	Date DIprimoPeriodo = (Date)vectorPeriodi.get(i-1);
        	Date DFsecondoPeriodo = (Date)vectorPeriodi.get(i);
        	if(DateUtils.isEquals(DIprimoPeriodo, DFsecondoPeriodo) ||
        	   (DateUtils.isLower(DIprimoPeriodo, DFsecondoPeriodo) && DateUtils.getIntervallo(DIprimoPeriodo, DFsecondoPeriodo)==1)){
        		periodiContinuativi=true;//non cambia il numero del periodo
        	} else {
            	periodiContinuativi=false;// cambia il numero del periodo
            }            
        } 
        
        int cAnni=2; int cMesi=3; int cGiorni=4; int cPeriodiContinuativi=5; int cNumeroPeriodo=6;
    	//for (int i=1;i<vectorPeriodi.size()-1;i=i+2){   
   		matricePeriodi[r][cAnni] = numAnni;
   		matricePeriodi[r][cMesi] = numMesi;
   		matricePeriodi[r][cGiorni] = numGiorni;
   		if(periodiContinuativi) {
   			//Periodi sono Continuativi
   			matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
   		}    			
   		else {
   			numPeriodiContinuativi = numPeriodiContinuativi + 1;
   			matricePeriodi[r][cPeriodiContinuativi] = Integer.toString(numPeriodiContinuativi);
   			periodiContinuativi = false;
   		}
   		matricePeriodi[r][cNumeroPeriodo] = Integer.toString(i);//uguale al numero del periodo: primo periodo "0" secondo "2" terzo "4"
   		r++;
    	//}
    }
       

    //somma anni mesi giorni per periodi consecutivi
    String[][] matricePeriodiConsecutivi = new String[nRighe][nColonne];   
    boolean matricePeriodiConsecutiviEsiste=false;//non esiste
    Vector vectorPeriodiTemp = new Vector();
	boolean periodiContinuativi = false;
	int nRigPC=0;
	int nColPC=0;     
	
	String ggInizio="";
    String mmInizio="";
    String aaInizio="";		        
	String ggFine="";
	String mmFine="";
	String aaFine="";
	int numeroPeriodo=0;
	Date data = new Date();
	DItempdata = new Date();
	DFtempdata = new Date();
	for (int i=0;i<nRighe;i++){  
		/* if (i==0) {
			periodiContinuativi = false;
		} else {
			if(matricePeriodi[i-1][1].equalsIgnoreCase(matricePeriodi[i][0])){
				periodiContinuativi = true;
			} else {
				periodiContinuativi = false;
	    		nRigPC=nRigPC+1;
			}
		} */
		 if (i==0) {
				periodiContinuativi = false;
		} else {
			if(matricePeriodi[i-1][5].equalsIgnoreCase(matricePeriodi[i][5])){
				periodiContinuativi = true;
			} else {
				periodiContinuativi = false;
	    		nRigPC=nRigPC+1;
			}	
		}
		
   		if (periodiContinuativi) {
   			// SI periodi Consecutivi
			//numeroPeriodo = Integer.valueOf(matricePeriodi[i][6]);				
			//DItempdata = (Date)vectorPeriodi.get(numeroPeriodo-2);
   		/* 	for (int k=0;k<vectorPeriodi.size()-1;k=k+1){   	
	    		data = (Date)vectorPeriodi.get(k);
	    	    String ggVectorPeriodi = DateUtils.getDayToString   (data);
	            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
	            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
	            String ggMatricePeriodi = matricePeriodi[i-1][0].substring(8, 10);
	            String mmMatricePeriodi   = matricePeriodi[i-1][0].substring(5, 7);
	            String aaMatricePeriodi   = matricePeriodi[i-1][0].substring(0, 4);		     
	    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
	    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
	    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
	    			DItempdata = data;	
	    			break;
	    		}	    				    		
	    	} */

	    	ggInizio = DateUtils.getDayToString   (DItempdata);
	        mmInizio   = DateUtils.getMonthToString (DItempdata);
	        aaInizio   = DateUtils.getYearToString  (DItempdata);
	        
	        //DFtempdata = (Date)vectorPeriodi.get(numeroPeriodo+1);
	        for (int k=0;k<vectorPeriodi.size();k=k+1){   	
	    		data = (Date)vectorPeriodi.get(k);
	    	    String ggVectorPeriodi = DateUtils.getDayToString   (data);
	            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
	            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
	            String ggMatricePeriodi = matricePeriodi[i][1].substring(8, 10);
	            String mmMatricePeriodi   = matricePeriodi[i][1].substring(5, 7);
	            String aaMatricePeriodi   = matricePeriodi[i][1].substring(0, 4);		     
	    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
	    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
	    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)) {
	    			DFtempdata = data;	
	    			break;
	    		}
	    			
	    	}
	       
	    	ggFine = DateUtils.getDayToString   (DFtempdata);
	    	mmFine   = DateUtils.getMonthToString (DFtempdata);
	    	aaFine   = DateUtils.getYearToString  (DFtempdata);

	        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
	        String sep = "~#";
	        String[] aPairs = new String[3];
	        aPairs = calcoloGioniMesiAnni.split(sep);
	        String numAnni = aPairs[0];
	        String numMesi = aPairs[1];
	        String numGiorni = aPairs[2];  

	        matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][2] = numAnni;
	        matricePeriodiConsecutivi[nRigPC][3] = numMesi;
	        matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
	        matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5];
	        matricePeriodiConsecutiviEsiste=true;
   		} else {
   			//NO periodi Consecutivi  			
   			// if (matricePeriodi[i][6]!=null)
   			//	numeroPeriodo = Integer.valueOf(matricePeriodi[i][6]);
   			//else 
   			//	numeroPeriodo = 0;  	 			   		    
	    	if (i==0) {
	    		/* for (int k=0;k<vectorPeriodi.size()-1;k=k+1){   
		    		data = (Date)vectorPeriodi.get(k);
		            String ggVectorPeriodi = DateUtils.getDayToString   (data);
		            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
		            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
		            String ggMatricePeriodi = matricePeriodi[i][0].substring(8, 10);
		            String mmMatricePeriodi   = matricePeriodi[i][0].substring(5, 7);
		            String aaMatricePeriodi   = matricePeriodi[i][0].substring(0, 4);		            
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
		    			DItempdata = data;	
		    			break;
		    		}		    			
		    	} */
	    		DItempdata = (Date)vectorPeriodi.get(i);
	    	} else {
	    		for (int k=0;k<vectorPeriodi.size()-1;k=k+1){   	
		    		data = (Date)vectorPeriodi.get(k);
                    
		            String ggVectorPeriodi = DateUtils.getDayToString   (data);
		            String mmVectorPeriodi   = DateUtils.getMonthToString (data);
		            String aaVectorPeriodi   = DateUtils.getYearToString  (data);	
		            String ggMatricePeriodi =  matricePeriodi[i][0].substring(8, 10);
		            String mmMatricePeriodi   =  matricePeriodi[i][0].substring(5, 7);
		            String aaMatricePeriodi   =  matricePeriodi[i][0].substring(0, 4);		                     
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
		    			DItempdata = data;	
		    			break;
		    		}	    			
		    	}
	    		//DItempdata = (Date)vectorPeriodi.get(numeroPeriodo); 	
	    	}
	    	ggInizio = DateUtils.getDayToString   (DItempdata);
	        mmInizio   = DateUtils.getMonthToString (DItempdata);
	        aaInizio   = DateUtils.getYearToString  (DItempdata); 
	       
		    if (i==0){ 		
		    	/* for (int k=0;k<vectorPeriodi.size()-1;k=k+1){   	
		    		data = (Date)vectorPeriodi.get(k);
		    		String ggVectorPeriodi = DateUtils.getDayToString   (data);
			        String mmVectorPeriodi   = DateUtils.getMonthToString (data);
			        String aaVectorPeriodi   = DateUtils.getYearToString  (data);				       
		            String ggMatricePeriodi =  matricePeriodi[i][1].substring(8, 10);
		            String mmMatricePeriodi   =  matricePeriodi[i][1].substring(5, 7);
		            String aaMatricePeriodi   =  matricePeriodi[i][1].substring(0, 4);       
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    			 && (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    			 && aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi) ){
		    			DFtempdata = data;	
		    			break;
		    		}
		    			
		    	} */
		    	DFtempdata = (Date)vectorPeriodi.get(i+1);
		    } else { 
		    	for (int k=0;k<vectorPeriodi.size();k=k+1){   	
		    		data = (Date)vectorPeriodi.get(k);
		    		String ggVectorPeriodi = DateUtils.getDayToString   (data);
			        String mmVectorPeriodi   = DateUtils.getMonthToString (data);
			        String aaVectorPeriodi   = DateUtils.getYearToString  (data);			        
		            String ggMatricePeriodi =  matricePeriodi[i][1].substring(8, 10);
		            String mmMatricePeriodi   =  matricePeriodi[i][1].substring(5, 7);
		            String aaMatricePeriodi   =  matricePeriodi[i][1].substring(0, 4);        
		    		if( (ggVectorPeriodi.equalsIgnoreCase(ggMatricePeriodi) || ("0"+ggVectorPeriodi).equalsIgnoreCase(ggMatricePeriodi)) 
		    				&& (mmVectorPeriodi.equalsIgnoreCase(mmMatricePeriodi) || ("0"+mmVectorPeriodi).equalsIgnoreCase(mmMatricePeriodi)) 
		    				&& aaVectorPeriodi.equalsIgnoreCase(aaMatricePeriodi)){
		    			DFtempdata = data;
		    			break;
		    		}
		    				
		    	}		        
    			//DFtempdata = (Date)vectorPeriodi.get(numeroPeriodo+1);
		    }
	    	ggFine = DateUtils.getDayToString   (DFtempdata);
	    	mmFine   = DateUtils.getMonthToString (DFtempdata);
	    	aaFine   = DateUtils.getYearToString  (DFtempdata);  		    		
  		
   		    
	        String calcoloGioniMesiAnni = CaricaHTML_Servlet.getQuantumIntervallo (ggInizio,mmInizio,aaInizio,ggFine,mmFine,aaFine);
	        String sep = "~#";
	        String[] aPairs = new String[3];
	        aPairs = calcoloGioniMesiAnni.split(sep);
	        String numAnni = aPairs[0];
	        String numMesi = aPairs[1];
	        String numGiorni = aPairs[2];
	        matricePeriodiConsecutivi[nRigPC][0] = DateUtils.getDateToString(DItempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][1] = DateUtils.getDateToString(DFtempdata,"dd/MM/yyyy" );
	        matricePeriodiConsecutivi[nRigPC][2] = numAnni;
	        matricePeriodiConsecutivi[nRigPC][3] = numMesi;
	        matricePeriodiConsecutivi[nRigPC][4] = numGiorni;
	        matricePeriodiConsecutivi[nRigPC][5] = matricePeriodi[i][5].toString();
	        matricePeriodiConsecutiviEsiste=true;
   		}
   	}		
 	
	//somma colonna | Anni | Mesi | Giorni |
	//int sommaColonnaAnni = 0;
	//int sommaColonnaMesi = 0;
	//int sommaColonnaGiorni = 0;
	if(matricePeriodiConsecutiviEsiste) { //ci sono periodi consecutivi ==> in ogni riga della tabella è stato inserito un periodo consecutivo
		for (int i=0;i<=nRigPC;i++){
			if (matricePeriodiConsecutivi[i][2]!=null)
				sommaColonnaAnni = sommaColonnaAnni + Integer.parseInt(matricePeriodiConsecutivi[i][2]);
			if (matricePeriodiConsecutivi[i][3]!=null)
				sommaColonnaMesi = sommaColonnaMesi + Integer.parseInt(matricePeriodiConsecutivi[i][3]);
			if (matricePeriodiConsecutivi[i][4]!=null)
				sommaColonnaGiorni = sommaColonnaGiorni + Integer.parseInt(matricePeriodiConsecutivi[i][4]);   		
		}	
	}	
    
	sommaColonnaAnni = sommaColonnaAnni + sommaColonnaAnniModManuale;
	sommaColonnaMesi = sommaColonnaMesi + sommaColonnaMesiModManuale;
	sommaColonnaGiorni = sommaColonnaGiorni + sommaColonnaGiorniModManuale;
	
	int giorni=sommaColonnaGiorni;
	while (giorni>0){
		giorni = giorni-30;
		if(giorni>=0){
			sommaColonnaMesi = sommaColonnaMesi+1;
			sommaColonnaGiorni = giorni;
		}			
	}
	int mesi=sommaColonnaMesi;
	while (mesi>0){
		mesi = mesi-12;
		if(mesi>=0){
			sommaColonnaAnni = sommaColonnaAnni+1;
			sommaColonnaMesi = mesi;
		}			
	}
	int anni=sommaColonnaAnni;
	
    //fine calcolo totale custodia cautelare Anni Mesi Giorni 
    //dove consideriamo periodi sovrapposti solo per Misure Cautelari

    String totGiorniMesiAnni = numGiorniMisuraCautelare+"|"+mesiMisuraCautelare+"|"+anniMisuraCautelare;
    String totSommaGiorniMesiAnni = sommaColonnaGiorni+"|"+sommaColonnaMesi+"|"+sommaColonnaAnni;
    totGiorni = totGiorni +"|"+ totGiorniMesiAnni +"|"+ totSommaGiorniMesiAnni;
    out.println (totGiorni);
%>