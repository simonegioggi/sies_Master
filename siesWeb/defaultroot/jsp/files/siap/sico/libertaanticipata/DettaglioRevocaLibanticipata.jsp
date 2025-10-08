<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="java.util.Iterator"%>
<%@ page import="java.lang.String"%>

<%@ page import="siap.sico.libertaanticipata.action.ICostantiLibertaAnticipata"%>
<%@ page import="siap.sico.libertaanticipata.model.LicenzaPeriodiLibAnticipataModel"%>
<%@ page import="siap.sico.libertaanticipata.model.PeriodoLibAnticipataModel"%>

<jsp:useBean id="LicenzePeriodi"	scope="request" class="java.util.Vector"/>
<jsp:useBean id="datiOrdinanza"		scope="request" class="siap.sius.depositoordinanzapc.model.OrdinanzaEventoTenoriPrescrizioniModel"/>

<%
int[] num  = {0,0,0,0};
int[] n = {0,0,0,0};
String[] titolo = new String[4];
String[] cod = {"R","I","N","S",};
Iterator itx = LicenzePeriodi.iterator();
// Conteggio delle licenze distinte per tipo
while (itx.hasNext()) {
    LicenzaPeriodiLibAnticipataModel lLicPer = (LicenzaPeriodiLibAnticipataModel) itx.next();
    if (lLicPer.getLicenza().getFlagConcesso().compareTo("R") == 0)
        num[0]++;
    else if (lLicPer.getLicenza().getFlagConcesso().compareTo("I")  == 0)
        num[1]++;
    else if (lLicPer.getLicenza().getFlagConcesso().compareTo("N") == 0)
        num[2]++;
    else if (lLicPer.getLicenza().getFlagConcesso().compareTo("S") == 0)
        num[3]++;
}
titolo[0] = "Periodi Rigettati (non Revocati): " + num[0];
titolo[1] = "Periodi Inammissibili: " + num[1];
titolo[2] = "Periodi N.L.P./N.D.P.: " + num[2];
titolo[3] = "Periodi Scomputati: " + num[3];
if (num[3] == 0)
	titolo[3] = "";
// NEL seguente CICLO FOR SONO GESTITI I CASI DA 'NON REVOCATI' (num[0] / Titolo[0]))
// FINO A 'Periodi N.L.P./N.D.P.'(num[2] / Titolo[2])
for (int k = 0; k < 3; k++) {
%>
<table cellspacing="2" cellpadding="2">
	<tr><td>&nbsp;</td></tr>
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
			<font class="l"><%=tipo%>&nbsp;<%=n[k]%>)<br></font>
	</td>
<%
						for (int i = 0; i < p.length; i++) {
%>
		<td class="L">
			<font class="l">
				<%=DateUtils.getDateToString(p[i].getDataInizio(),"dd/MM/yyyy")%>-
				<%=DateUtils.getDateToString(p[i].getDataFine(),"dd/MM/yyyy")%>;
			</font>
		</td>
<%
						}
%>
	</tr>
<%
		 			} // chiude if (lLicPerConc != nul
				} // chiude if (lLicPerConc.getLicenza() != null && 
			} // chiude if (lLicPerConc.getLicenz......
		}	// chiude iterartor while (itxC.hasNext())
%>
</table>
<%
	} // chiude if (num[k] > 0)
} // chiude ciclo  for (int k= 0; k < 2; k++)
// I 'PERIODI SCOMPUTATI'(Revoca su L.A.) SONO CALCOLATI nella seguente parte sotto  if (num[3] > 0)
if (num[3] > 0) {
%>
<table cellspacing="2" cellpadding="2">
	<tr><td>&nbsp;</td></tr>
	<tr><td class="Titolo" colspan="6"><%=titolo[3]%><td></tr>
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
				String tipo="";
				if (lLicPerConc.getLicenza().getDescrStatoPermesso() != null) {
					if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LS"))
						tipo = "Revoca Liberazione Anticipata Speciale";
					else if (lLicPerConc.getLicenza().getDescrStatoPermesso().substring(0,2).equals("LI"))
						tipo = "Revoca Integrazione Liberazione Anticipata";	
					else 
						tipo = "Revoca Liberazione Anticipata";
				}
				n[3]++;
				PeriodoLibAnticipataModel[] ppSco = lLicPerConc.getPeriodi();
%>	
	<tr>
		<td class="L"><font class="l"><%=tipo%>&nbsp;<%=n[3]%>)<br></font></td>							
<%
				for (int i = 0; i < ppSco.length; i++) {
%>
		<td class="L">
		  	<font class="l">
    			<%=DateUtils.getDateToString(ppSco[i].getDataInizio(),"dd/MM/yyyy")%>-
				<%=DateUtils.getDateToString(ppSco[i].getDataFine(),"dd/MM/yyyy")%>;
			</font>
		</td>
<%
				}
%>
	</tr>
<%
			} // chiude if (lLicPerConc != null
		} // chiude if (lLicPerConc.getLicenza().getFlagConcesso().compareTo("S") == 0
	} // chiude while
%>
</table>
<%			
} // chiude if (num[3] > 0)
%>
<br>