<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>
<%@ page import="java.lang.String" %>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<jsp:useBean id="LicenzePeriodi" scope="request" class="java.util.Vector"/>

<%
int[] num  = {0,0,0,0,0,0};
int[] n = {0,0,0,0,0,0};
String[] titolo = new String[6];
String[] cod = {"C","C","R","I","N","S",};

Iterator itx = LicenzePeriodi.iterator();
// Conteggio delle licenze distinte per tipo
while (itx.hasNext()) {
	LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
    if (lLicPer.getLicenza().getFlagConcesso().compareTo("C") == 0) {
    	if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("C") == 0)
			num[0]++;
		else if (lLicPer.getLicenza().getFlagScorta() != null && lLicPer.getLicenza().getFlagScorta().compareTo("S") == 0)
			num[1]++;	
    } else if (lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
        num[2]++;
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
        num[3]++;
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
        num[4]++;
    else if ( lLicPer.getLicenza().getFlagConcesso().compareTo("S") == 0)
        num[5]++;
}

titolo[0] = "Periodi concessi: " + num[0];
titolo[1] = "Semestri concessi: " + num[1];
titolo[2] = "Periodi non concessi Rigettati: " + num[2];
titolo[3] = "Periodi non concessi Inammissibili: " + num[3];
titolo[4] = "Periodi non concessi N.L.P./N.D.P.: " + num[4];
titolo[5] = "Periodi Scomputati: " + num[5];

if (num[5] == 0)
	titolo[5] = "";

// I 'PERIODI CONCESSI' SONO CALCOLATI FUORI CICLO nella parte if(num[0] > 0)
if (num[0] > 0) {
%>
<table cellspacing="2" cellpadding="2">
	<tr> <td> <br></td></tr>
	<tr>
    	<td class="Titolo" colspan=6> <%=titolo[0]%><td>
	</tr>
</table>
<%	
	Iterator itxP = LicenzePeriodi.iterator();
	while (itxP.hasNext()) {
		LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxP.next();
		if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0
				&& lLicPerConc.getLicenza().getFlagScorta() != null
				&& lLicPerConc.getLicenza().getFlagScorta().compareTo("C") == 0) {
			if (lLicPerConc != null && lLicPerConc.getPeriodi() != null ) {
%>
<table cellspacing="2" cellpadding="2">
<%
				String tipo="";
				if (lLicPerConc.getLicenza().getDescrStatoPermesso() != null) {
					if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS"))
						tipo = "Liberazione Anticipata Speciale";
   					else if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI"))
   						tipo = "Integrazione Liberazione Anticipata";	
   					else 
   						tipo = "Liberazione Anticipata";
				}
				n[0]++;
				PeriodoLibAnticipataModel[] pp = lLicPerConc.getPeriodi();
				if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0) {
%>
	<tr>
		<td class="L"><font class="l"><%=tipo%>&nbsp;<%=n[0]%>)<br></font></td>							
<%
				}
	   			for (int i = 0; i < pp.length; i++) {
%>
		<td class="L">
			<font class="l">
		  		<%=DateUtils.getDateToString(pp[i].getDataInizio(),"dd/MM/yyyy")%>-
				<%=DateUtils.getDateToString(pp[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
			</font>
		</td>
<%
				}
%>
	</tr>
<%
			} // chiude if (lLicPerConc != null)
   		} // chiude if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("C") == 0
	} // chiude while
%>
</table>
<%			
} // chiude if (num[0] > 0)
//	  
// NEL seguente CICLO FOR SONO GESTITI I CASI DA 'SEMESTRI CONCESSI' (num[1] / Titolo[1]))
// FINO A 'Periodi non concessi N.L.P./N.D.P.'(num[4] / Titolo[4])
for (int k= 1; k < 5; k++) {
%>
<table cellspacing="2" cellpadding="2">
	<tr><td><br></td></tr>
	<tr>
    	<td class="Titolo" colspan=6> <%=titolo[k]%><td>
	</tr>
</table>
<%
	if (num[k] > 0) {
		Iterator itxC = LicenzePeriodi.iterator();
%>
<table cellspacing="2" cellpadding="2">
<%
		while (itxC.hasNext()) {
			LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxC.next();
			if (lLicPerConc.getLicenza().getFlagConcesso().compareTo(cod[k]) == 0) {
				if (lLicPerConc.getLicenza() != null
						&& lLicPerConc.getLicenza().getFlagScorta() != null
						&& lLicPerConc.getLicenza().getFlagScorta().compareTo("C") != 0) {
					if (lLicPerConc != null && lLicPerConc.getPeriodi() != null) {
						String tipo = "";
		      			if (lLicPerConc.getLicenza().getDescrStatoPermesso() != null) {
							if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS"))
								tipo = "Liberazione Anticipata Speciale";
							else if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI"))
								tipo = "Integrazione Liberazione Anticipata";	
							else 
								tipo = "Liberazione Anticipata";
		      			}
					    n[k]++;
				    	PeriodoLibAnticipataModel[] p = lLicPerConc.getPeriodi();
%>
	<tr>
		<td class="L">
			<font class="l"> <%= tipo%>&nbsp;<%=n[k]%>)<br></font>
		</td>
<%
						for (int i = 0; i < p.length; i++) {
%>
		<td class="L">
			<font class="l">
				<%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
				<%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
			</font>
		</td>
<%
					     }
%>
	</tr>
<%
					} // chiude if (lLicPerConc != null ...
	        	} // chiude if (lLicPerConc.getLicenza() != null && ...
	        } // chiude if (lLicPerConc.getLicenz() ...
		} // chiude iterator while (itxC.hasNext())
%>
</table>
<%
	} // chiude if (num[k] > 0)
} // chiude ciclo for (int k= 0; k < 5; k++)

// I 'PERIODI SCOMPUTATI'(Reclamo su L.A.) SONO CALCOLATI nella seguente parte sotto  if(num[5] > 0)
if (num[5] > 0) {
%>
<table cellspacing="2" cellpadding="2">
	<tr> <td> <br></td></tr>
	<tr>
    	<td class="Titolo" colspan=6> <%=titolo[5]%><td>
	</tr>
</table>
<%	
	Iterator itxSco = LicenzePeriodi.iterator();
	while (itxSco.hasNext()) {
		LicenzaPeriodiLibAnticipataModel lLicPerConc = (LicenzaPeriodiLibAnticipataModel) itxSco.next();
		if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("S") == 0) {
			if (lLicPerConc != null && lLicPerConc.getPeriodi() != null) {
%>
<table cellspacing="2" cellpadding="2">
<%
				String tipo = "";
				if (lLicPerConc.getLicenza().getDescrStatoPermesso() != null) {
					if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS"))
	   					tipo = "Reclamo Liberazione Anticipata Speciale";
	   				else if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI"))
	   					tipo = "Reclamo Integrazione Liberazione Anticipata";
	   				else
	   					tipo = "Reclamo Liberazione Anticipata";
				}
				n[5]++;
				PeriodoLibAnticipataModel[] ppSco = lLicPerConc.getPeriodi();
%>
	<tr>
		<td class="L"><font class="l"><%=tipo%>&nbsp;<%=n[5]%>)<br></font></td>							
<%
				for (int i = 0; i < ppSco.length; i++) {
%>
		<td class="L">
		  	<font class="l">
				<%=DateUtils.getDateToString(ppSco[i].getDataInizio(),"dd/MM/yyyy")%>-
				<%=DateUtils.getDateToString(ppSco[i].getDataFine(),"dd/MM/yyyy")%>; &nbsp;
			</font>
		</td>
<%
				}
%>
	</tr>
<%
			} // chiude if (lLicPerConc != null ...
		} // chiude if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("S") == 0
	} // chiude while
%>
</table>
<%			
} // chiude if (num[5] > 0)
%>
<br>