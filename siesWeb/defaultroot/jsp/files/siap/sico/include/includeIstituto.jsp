<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%-- <%@ taglib prefix="s" uri="/struts-tags" %> --%>
<%-- <%@ taglib prefix="sx" uri="/struts-dojo-tags" %> --%>
<%@ page import="f3b.web.IWebConstants"%>

<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>

<%@ page import="f3b.util.StringUtils"%>
<jsp:useBean id="formname"        scope="request" class="java.lang.String"/>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%--    <jsp:include page="/ajax/commonInclude.jsp"/> --%>
 <script language="JavaScript">
   
   function showKey() {
      var autoCompleter = dojo.widget.byId('auto2');
      alert(autoCompleter.getSelectedKey());
   }
   
   function showValue() {
      var autoCompleter = dojo.widget.byId('auto2');
      alert(autoCompleter.getSelectedValue());
   }
  
   function showDivIstituto()
   {
	   //Mostro la Div riempita da popup e sbianco eventualmente l'autocompleter
	   document.getElementById("DivIstituto").style.visibility = 'visible';
	   document.getElementById("DivIstituto").style.display = 'block';
	   document.getElementById("DivScriviMag").style.visibility = 'hidden';
	   document.getElementById("DivScriviMag").style.display = 'none';
	   var autoCompleter = dojo.widget.byId('CodIstituto');
	   
	   autoCompleter.setValue('');
   }

   function hideDivIstituto()
   {
	   //Mostro la Div autocompleter e sbianco eventualmente il campo hidden codIstituto
	   document.getElementById("DivIstituto").style.visibility = 'hidden';
	   document.getElementById("DivScriviIstituto").style.visibility = 'visible';
	   document.getElementById("DivIstituto").style.display = 'none';
	   document.getElementById("DivScriviIstituto").style.display = 'block';
	   
	   document.<%=request.getParameter("formname")%>.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>.value='';
   }
   
  
   var desktop;
   function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
   {
     desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
   }

   function riempiCodIstituto()
   {   
	   var codIst = document.<%=request.getParameter("formname")%>.<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>;
		     
	   if(codIst.value=='')
	   {
	      var autoCompleter = dojo.widget.byId('CodIstituto');
	
	      codIst.value = autoCompleter.getSelectedKey();
	      alert(autoCompleter.getSelectedKey());
	   }
	    return true;
   }
  
  </script>
</head>



<style type="text/css"> 

				#containerIstituto {
				height:30px;
				position:relative;
				}
				 
				#containerIstituto div {
				position:absolute;
				top:0px; } 
	
				#containerIstituto .DivIstituto { position:absolute;top:0px; left:0px; z-index:0; display:none; }
 				#containerIstituto .DivScriviIstituto { position:absolute;top:0px; left:0px; z-index:1; }
 	</style>




<div id="containerIstituto">

<input type="hidden" value="" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" >

   <div id="DivScriviIstituto">
<!--   <s:url var="magList" action="CaricaIstituto" namespace="/"/> -->
  <table width="100%">
   <tr>
   <td class="l" width="25%">Istituto Detenzione </td>
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
<!--   <sx:autocompleter   -->
<!-- 				    id="CodIstituto" -->
<!-- 				    name="CodIstituto" -->
<!-- 				    keyName="CodIstitutoKey" -->
<!-- 				    indicator="indicator"  -->
<!-- 				    autoComplete="true" -->
<!-- 				    href="%{#magList}" -->
<!-- 				    cssStyle="width: 500px;"  -->
<!-- 				    autoComplete="false"  -->
<!-- 				    loadOnTextChange="true" -->
<!-- 				    loadMinimumCount="3" -->
<!-- 				    showDownArrow="false"  -->
<!-- 				    searchType="substring" -->
<!-- 				    forceValidOption="true" -->
<!-- 			    > -->
<!--   </sx:autocompleter> -->
      
<img id="indicator" src="/images/indicator.gif" alt="Loading..." style="display:none"/>
 <a href="Javascript:ListaIstitutoDetenzione('<%=request.getParameter("formname")%>','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
             <img src="/images/filefolder.gif" border="0" onClick="javascript:showDivIstituto();">
        </a>
</td>
   </tr>
   </table>
</div>


<div id="DivIstituto" style="display:none">

  <table width="100%">
   <tr>
   <td class="l" width="25%">Istituto Detenzione </td>
   <td class="L">
        <input readonly Title="Istituto" name="Comune" value="" size="50" onClick="javascript:hideDivIstituto()">
      <a href="Javascript:ListaIstitutoDetenzione('<%=request.getParameter("formname")%>','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
      <img src="/images/filefolder.gif" border="0" onClick="javascript:showDivIstituto()"></a>

      </td>
    </tr>
   </table>   
   </div>
   
<body>

</body>
</html>