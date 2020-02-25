<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%-- <%@ taglib prefix="s" uri="/struts-tags" %> --%>
<%-- <%@ taglib prefix="sx" uri="/struts-dojo-tags"%> --%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>

<%@ page import="f3b.util.StringUtils"%>

<jsp:useBean id="magistrato"   scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
   <%--jsp:include page="/ajax/commonInclude.jsp"/--%>
 <script language="JavaScript">
   
  
   function showKey() {
      var autoCompleter = dojo.widget.byId('auto2');
      alert(autoCompleter.getSelectedKey());
   }
   
   function showValue() {
      var autoCompleter = dojo.widget.byId('auto2');
      alert(autoCompleter.getSelectedValue());
   }
  

   
   function showDiv()
   {
	   
	   document.getElementById("DivMag").style.visibility = 'visible';
	   document.getElementById("DivScriviMag").style.visibility = 'hidden';
   }
   function hideDiv()
   {
	   document.getElementById("DivMag").style.visibility = 'hidden';
	   document.getElementById("DivScriviMag").style.visibility = 'visible';
   }
   
   function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
   {
     var desktop;
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
   }
   function riempiCodMagistrato()
   {   
	   var codIst = document.<%=request.getParameter("formname")%>.CodMagistrato;
		     
	   if(codIst.value=='')
	   {
	      var autoCompleter = dojo.widget.byId('AutoCompMagistrato');
	
	      codIst.value = autoCompleter.getSelectedKey();
	      alert(autoCompleter.getSelectedKey());
	   }
	    return true;
   }

  
  </script>
</head>



<style type="text/css"> 

				#container {
				height:30px;
				position:relative;
				}
				 
				#container div {
				position:absolute;
				top:0px; } 
	
				#container .DivMag { position:absolute;top:0px; left:0px; z-index:0; display:none; }
 				#container .DivScriviMag { position:absolute;top:0px; left:0px; z-index:1; }
 				 
</style>


<div id="container">
    <input type="hidden" value="" name="CodMagistrato" >
   
   <div id="DivMag">
   <table width='100%'>
   <tr>
   <td class="l" width='25%'>Magistrato </td>
   <td class="L">
       <input  title="Cognome Magistrato" value="" type="text" name="<%=ICostantiMagistrato.CAMPO_COGNOME%>" maxlength="35" size="25" onClick="javascript:hideDiv();">
       <input  title= "Nome Magistrato"    value="" type="text" name="<%=ICostantiMagistrato.CAMPO_NOME%>"   maxlength="35" size="25" onClick="javascript:hideDiv();">
         <a href="Javascript:ListaMagistrati('<%=request.getParameter("formname")%>','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME%>','<%=ICostantiMagistrato.CAMPO_NOME%>');">
        <img src="/images/filefolder.gif" border="0" onClick="javascript:showDiv();">
        </a>
      </td>
    </tr>
   </table>
</div>
<div id="DivScriviMag">
<!--   <s:url var="magList" action="CaricaMagistrato" namespace="/"/> -->
  <table width='100%'>
   <tr>
   <td class="l" width='25%'>Magistrato </td>
   <td class="L">
   <style>          
/* the drop down */
.dojoComboBoxOptions {
	font-family: 'Tahoma';
	font-size: 12px;
	color : blue;
    font-weight : bold;
	background-color: white;
	border: 1px solid #afafaf;
	position: absolute;
	z-index: 1000;
	overflow: auto;
	cursor: default;
}
 </style>              
  <%-- sx:autocompleter  
				    id="AutoCompMagistrato"
				    name="AutoCompMagistrato"
				    indicator="indicator" 
				    keyName="CodMagistratoKey"
				    autoComplete="true"
				    href="%{#magList}"
				    cssStyle="width: 400px;" 
				    autoComplete="true" 
				    loadOnTextChange="true"
				    loadMinimumCount="3"
				    showDownArrow="false" 
				    forceValidOption="true"/--%>
      
<img id="indicator" src="/images/indicator.gif" alt="Loading..." style="display:none"/>
<a href="Javascript:ListaMagistrati('<%=request.getParameter("formname")%>','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%=ICostantiMagistrato.CAMPO_COGNOME%>','<%=ICostantiMagistrato.CAMPO_NOME%>');">
        <img src="/images/filefolder.gif" border="0" onClick="javascript:showDiv();">
        </a>
</td>
   </tr>
   </table>
   </div>
     </div>
<body>

</body>
</html>