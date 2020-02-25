<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<jsp:useBean id="LicenzePeriodi"     scope="request" class="java.util.Vector"/>

<!-- DettaglioOrdinanzaViolazioneCEDU -->
<%

  int[] num  = {0,0,0,0,0};
  int[] n = {0,0,0,0,0};
  String[] titolo = new String[5];
  String[] cod = {"C","C","R","I","N"};

  Iterator itx = LicenzePeriodi.iterator();
  // Conteggio delle licenze distinte per tipo
  while (itx.hasNext())
  {
      LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
 
      if( lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0) 
      {
    	     if(lLicPer.getLicenza().getCodTipoLicenza() != null && lLicPer.getLicenza().getCodTipoLicenza().compareTo("RD") == 0 )
            	num[0]++;
           	 else if(lLicPer.getLicenza().getCodTipoLicenza() != null && lLicPer.getLicenza().getCodTipoLicenza().compareTo("SL") == 0 ) 
          		num[1]++;	
      }
      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
          num[2]++;
      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
          num[3]++;
      else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
          num[4]++;
  }

//  titolo[0] = "Periodi valutati per Riduzione Pena: " + num[0];
	titolo[0] = "Periodi valutati per Riduzione Pena: ";
 // titolo[1] = "Periodi valutati per liquidazione Somma: " + num[1];
  	titolo[1] = "Periodi valutati per liquidazione Somma: ";
  titolo[2] = "Periodi non concessi Rigettati: " + num[2];
  titolo[3] = "Periodi non concessi Inammissibili: " + num[3];
  titolo[4] = "Periodi non concessi N.L.P./N.D.P.: " + num[4];
  
  // I 'PERIODI CONCESSI' SONO CALCOLATI FUORI CICLO nella parte  if(num[0] , num[1]> 0)

  if(num[0] > 0)		// Riduzione Periodo Concesso
  {
		Iterator itxP = LicenzePeriodi.iterator();
		while (itxP.hasNext())
		{
			LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxP.next();
    		if( lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0 )
    		{
    			if(lLicPerConc.getLicenza().getCodTipoLicenza() != null && lLicPerConc.getLicenza().getCodTipoLicenza().compareTo("RD") == 0 )
    			{ %>
				    <table cellspacing="2" cellpadding="2" width="90%">
				    	<tr>
          					<td class="l" width="35%"> Totale Giorni Riduzione Pena Concessi  </td>
          					<td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lLicPerConc.getLicenza().getNumeroGiorni() )%> </font></td>
				    	</tr>
				    </table>   				
    				
	<% 	      		if(lLicPerConc != null && lLicPerConc.getPeriodi() != null )
		        	{  
	%>
						<table cellspacing="2" cellpadding="2" width="90%">
			            	<tr>
			            		<td class="l" width="35%"><%=titolo[0]%></td>
			            		<td class="L">
	<%
					    PeriodoLibAnticipataModel[] pp = lLicPerConc.getPeriodi();
			  			for (int i = 0; i < pp.length; i++)
					    {
						%>
						           <font class="l">
					              		<%=DateUtils.getDateToString(pp[i].getDataInizio(),"dd/MM/yyyy")%>-
					              		<%=DateUtils.getDateToString(pp[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
						          </font>
						<%
					     }
						%>
								</td>
						      </tr>
						</table>
						<br>						      
<%
		        	} // chiude if(lLicPerConc != nul

    			}		        	
    			
    		}	// chiude if(lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0
		
		}	// chiude while
		
  } 	// chiude if(num[0] > 0)
 
//--------------------------------------------

  if(num[1] > 0)		// Somma per Risarcimento
  {
		Iterator itxS = LicenzePeriodi.iterator();
		while (itxS.hasNext())
		{
			LicenzaPeriodiLibAnticipataModel lLicPerConcS = (LicenzaPeriodiLibAnticipataModel) itxS.next();
    		if( lLicPerConcS.getLicenza().getFlagConcesso().compareTo("C") == 0 )
    		{
    			if(lLicPerConcS.getLicenza().getCodTipoLicenza() != null && lLicPerConcS.getLicenza().getCodTipoLicenza().compareTo("SL") == 0 )
    			{ %>
				    <table cellspacing="2" cellpadding="2" width="90%">
				    	<tr>
          					<td class="l" width="35%"> Somma Liquidata a Titolo Risarcimento Danno: </td> 
          					<td class="l" ><font class="campo"><%=StringUtils.toStringJSP(lLicPerConcS.getLicenza().getSommaRisarcDanni() )%> &nbsp;&euro;</font></td>
				    	</tr>
				    </table>  
    				
	<% 	      		if(lLicPerConcS != null && lLicPerConcS.getPeriodi() != null )
		        	{  
	%>
						<table cellspacing="2" cellpadding="2" width="90%">
			            	<tr>
			            		<td class="l" width="35%"><%=titolo[1]%></td>
			            		<td class="L">
	<%
					    PeriodoLibAnticipataModel[] pp = lLicPerConcS.getPeriodi();
			  			for (int i = 0; i < pp.length; i++)
					    {
						%>
						           <font class="l">
					              		<%=DateUtils.getDateToString(pp[i].getDataInizio(),"dd/MM/yyyy")%>-
					              		<%=DateUtils.getDateToString(pp[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
						          </font>
						<%
					     }
						%>
								</td>
						      </tr>
						</table>						      
<%
		        	} // chiude if(lLicPerConcS != nul

    			}		        	
    			
    		}	// chiude if(lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0
		
		}	// chiude while
		
  } 	// chiude if(num[1] > 0)
	  
//	  
//  CICLO FOR PER I CASI 'Periodi non concessi':RIGETTATI,INAMMISSIBULI E N.L.P./N.D.P.'(num[2,3,4] / Titolo[2,3,4])
// 
  for (int k= 2; k < 5; k++)
  {
%>
	    <table cellspacing="2" cellpadding="2" width="30%">
	    	<tr> <td> <br></td></tr>
	    	<tr>
	        <td class="Titolo" colspan=6 style="text-align:left" > <%=titolo[k]%><td>
	    	</tr>
	    </table>
	
	<%  if (num[k] > 0)
	    {
			Iterator itxC = LicenzePeriodi.iterator();
	%>
		    <table cellspacing="2" cellpadding="2" width="90%">
	<%
		    while (itxC.hasNext())
		    {
		    	LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC.next();
		        if( lLicPerConc.getLicenza().getFlagConcesso().compareTo(cod[k]) == 0)
		        {
	      			if( lLicPerConc != null && lLicPerConc.getPeriodi() != null )
	      			{
					    n[k]++;
					    PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
	%>
					    <tr>
					    	<td class="L" width="3%">
					    		<font class="l"> <%=n[k]%> ) </font>
						 	</td>
						 	<td class="L">
				<% 			for (int i = 0; i < p.length; i++)
						    {	%>
						           <font class="l">
						              <%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
						              <%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;&nbsp;
						          </font>

				<%			}	%>
							</td>
				      </tr>
			<%
	      			} // chiude if(lLicPerConc != nul
		        } // chiude if( lLicPerConc.getLicenz......
		
	     	}	// chiude iterartor while (itxC.hasNext())
	%>
		     </table>
	<%   }	// chiude if (num[k] > 0)
  
  } // chiude ciclo  for (int k= 2; k < 5; k++)
 
 %>
<br>


