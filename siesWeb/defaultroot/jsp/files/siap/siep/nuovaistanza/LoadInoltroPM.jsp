<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%--@ taglib prefix="s" uri="/struts-tags" --%>


<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.nuovaistanza.model.NuovaIstanzaModel"%>
<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.nuovaistanza.action.ICostantiNuovaIstanza"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>


<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="autorita" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistrato"          scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="oggettoIstanza"   scope="request" class="java.lang.String"/>
<jsp:useBean id="listaIstanze"  scope="request" class="java.util.Vector"/>
<jsp:useBean id="listaeventi"  scope="request" class="java.util.Vector"/>


<html>
  <head>
    <title>[S.I.E.S.] - Inoltro al PM</title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
    <script language="JavaScript">
   


   function Verify()
   {
    
  	var data_to_verify = document.InserisciInoltroPM.<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>.value+'/'+document.InserisciInoltroPM.<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>.value+'/'+document.InserisciInoltroPM.<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM%>.value;

    if (!ControllaData(data_to_verify) )
  	{
        alert('Data inoltro PM non valida');
    	return false;
  	}
  }

   function hideDiv()
   {
	 //  document.getElementById("datiInoltro").style.visibility = 'hidden';
   }
   
   function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
   {
     var desktop;
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
   }

  </script>
  </head>
 <body class="corpo" onLoad="javascript:hideDiv()">
   <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font> &nbsp;&nbsp;<font class="campo">Inoltro al Pubblico Ministero</font>
      </td>
          <td class="LBG">
          <a href="javascript:history.back()">
            <img align="middle" src="/images/arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
    </tr>
  </table>
 
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  
  <!-- ******************FORM****************** -->
  
<form name="InserisciInoltroPM"  method="POST" action="<%= IWebConstants.PG_MAIN%>" onSubmit="javascript:riempiCodMagistrato()">
   <input type="hidden"  name="Action" value="siap.siep.nuovaistanza.action.ActInserisciInoltroPM" />
    
  	<table>
	  	<jsp:include page="/jsp/files/siap/siep/nuovaistanza/IncludeListaIstanze.jsp">
	  	<jsp:param name="formname" value="ActInserisciInoltroPM" />
	  	</jsp:include> 
	</table>
	<br>
<%
if(listaIstanze.size()==1 && listaeventi.size()==0)
{
%>
	<br>
	<br>
<%
 }
%>
	<br> <br>
<%
String lDisplayDatiInoltro = "display:none";
int da_compilare=0;
java.util.Date datainoltro=null;
Iterator<NuovaIstanzaModel> lItx = listaIstanze.iterator() ;
while (lItx.hasNext()){  
   NuovaIstanzaModel lModel =  (NuovaIstanzaModel)lItx.next(); 
   if (lModel.getDataInoltroPM()==null)
	   da_compilare++;
   else 
	   datainoltro=lModel.getDataInoltroPM();
}
if(listaIstanze.size()==1 && listaeventi.size()==0)
{
	lDisplayDatiInoltro = "display:block";
}
%>
	<div style="<%=lDisplayDatiInoltro%>" id="datiInoltro" >

		<table>
     	<tr>
     		<td class="l" width="25%">Data Inoltro al PM  </td>
        	<td class="L"> <input type="hidden"  name="IdNuovaIstanza" value="" />
        	<% 
        	if (da_compilare>0) {
        	%>
          	<input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"  > /
          	<input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > /
          	<input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
          	<% } else {%>
				<font class="campo">
					<%=StringUtils.toStringJSP(DateUtils.getDateToString(datainoltro,"dd-MM-yyyy"))%>
				</font>
          	<%} %>
        	</td> 
   		</tr>
	    <tr>  
	      	<td class="l">Magistrato</td>
	      	<td class="L">
	      	<input type="HIDDEN" title="CodiceMagistrato" value="<%=StringUtils.toStringJSP(magistrato.getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        	<% if (da_compilare>0) {%>
		      	<input readonly title="Cognome Magistrato" value="" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
		      	<input readonly title= "Nome Magistrato"    value="" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
		      	<a href="Javascript:ListaMagistrati('InserisciInoltroPM','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
		        	<img src="/images/filefolder.gif" border=0></a>
          	<% } else {%>
		       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getCognome())%></font>
		       <font class="campo"><%=StringUtils.toStringJSP(magistrato.getNome() )%></font>
          	<%} %>
	    	</td>
	    </tr>
	    </table>
 
<%
if (da_compilare>0){
%>
  
		<table width="100%">    
		   <tr>
		       <td class="lNoBord" ><br></br><input  value="Conferma" type="submit"></td>
		   </tr>
		</table>
<%
}
%> 
		  
  	</div>
  
</form>
  
  
<%
if (da_compilare>0 && !lDisplayDatiInoltro.equals("display:none")){
%>
  
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("InserisciInoltroPM");

  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>","gt=1");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_GIORNO_DATA_INOLTRO_PM%>","lt=31");

  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>","gt=1");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_MESE_DATA_INOLTRO_PM%>","lt=12");

  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM%>","numeric");
  frmvalidator.addValidation("<%=ICostantiNuovaIstanza.CAMPO_ANNO_DATA_INOLTRO_PM%>","gt=1900");

  

 </script>
<%
}
%> 

  </body>
</html>
  