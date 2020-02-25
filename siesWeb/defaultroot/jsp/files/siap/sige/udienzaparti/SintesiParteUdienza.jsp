<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel"%>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige"%>

<jsp:useBean id="FascicoloSigeEsteso"    scope="session" class="siap.sige.fascicolo.model.FascicoloSigeEstesoModel"/>
<jsp:useBean id="anagraficaParteUdienza" scope="request" class="siap.sige.udienzaparti.model.AnagraficaPartiUdienzaModel" />

<%
//Fascicolo SIGE
FascicoloSigeModel lFascicolo = FascicoloSigeEsteso.getFascicoloSige();
String lDataUdienza = null;
if (FascicoloSigeEsteso.getUdienzaProcedimento()!=null && FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige()!=null)
{
	lDataUdienza = DateUtils.getDateToString(FascicoloSigeEsteso.getUdienzaProcedimento().getDataUdienzaSige(),"dd-MM-yyyy");
}else{
	lDataUdienza = "-";
}
	
%>
  <table cellspacing=0 cellpadding=0 width=95%>

    <tr>
      <td class="L"><font class="label">Parte: </font>
      	<font class="campo">
<%
		if(anagraficaParteUdienza.getCodParte().equals("F")){
%>
			<%=anagraficaParteUdienza.getCognome()%>&nbsp;<%=anagraficaParteUdienza.getNome()%>
			</font>&nbsp;
<%
	        if (anagraficaParteUdienza.getSesso().compareTo("F")==0)
       		{
%>
          		<font class="label">nata il :</font>&nbsp;
<%
        	}
        	else
        	{
%>
          		<font class="label">nato il :</font>&nbsp;
<%
        	}
%>

        	<font class="campo"><%=DateUtils.getDateToString(anagraficaParteUdienza.getDataNascita(),"dd-MM-yyyy")%></font>&nbsp;
        	<font class="label">in : </font>
        	<font class="campo">
<%
        	if (anagraficaParteUdienza.getDescComuneNascita().compareTo("-")==0)
        	{
%>
          		<%=anagraficaParteUdienza.getDescrStatoNascita()%>
<%
        	}
        	else
        	{
%>
          		<%=anagraficaParteUdienza.getDescComuneNascita()+ "  ("+anagraficaParteUdienza.getCodProvinciaNascita()+")" %>
<%
        	}
%>
        </font>
<%			
		} else {
%>
			<%=anagraficaParteUdienza.getDenominazione()%>&nbsp;<%=anagraficaParteUdienza.getRagSociale()%>
<%
		}
%> 
     		
      </td>
    </tr>

    <tr>
      <td class="L">
        <font class="label">Data Udienza : <%=lDataUdienza%> </font>
      </td>
    </tr>
  </table>
 <br>  