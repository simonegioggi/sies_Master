<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="siap.sico.decodifiche.model.DecodificheModel" %>


<%@ page import="siap.sico.ufficio.model.UfficioModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo" %>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="ListaTitoli"  			scope="request" class="java.util.Vector"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>

<jsp:useBean id="sceltaReato"			scope="request" class="java.lang.String"/>
<jsp:useBean id="AnnoReato"				scope="request" class="java.lang.String"/>
<jsp:useBean id="MeseReato"				scope="request" class="java.lang.String"/>
<jsp:useBean id="GiornoReato"			scope="request" class="java.lang.String"/>

<jsp:useBean id="listaDPR"        scope="request" class="java.lang.String"/>
<jsp:useBean id="VectDPR"        scope="request" class="java.util.Vector"/>

<!-- 		LoadElencoTitoliRichiestaGEBenefici		 --> 

<%
//==================================================================================
// Form di Elenco Titoli da selezionare per l'inserimento delle
//	Richieste del PM al GE di Applicazione Benefici
//==================================================================================

%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione Cumulo - Elenco Titoli per Richieste del PM al GE - Applicazione Benefici </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  	<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  	<script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
    <script language="JavaScript" src="/html/conferma.js"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    <script language="JavaScript">
		
    var lsceltaReato = '<%=sceltaReato%>';

<% 
String objDPR = "var objDpr = {";
int contaInseriti = 0;
for (int i=0; i< VectDPR.size(); i++) {
  DecodificheModel lDecode = (DecodificheModel) VectDPR.elementAt(i);
  
  if (lDecode.getCodiceAlt2()!=null) {
    if (contaInseriti>0) objDPR+=",\n";
    contaInseriti++;
    objDPR+="\""+lDecode.getCode()+"\":"+"\""+lDecode.getCodiceAlt2()+"\"";
  }
}
objDPR += "\n};";
%>
<%=objDPR%>


   function eseguiFunzione(action)
   {
     document.elencoRicGEBen.<%=IWebConstants.ACTION_FIELD%>.value = action;
     document.elencoRicGEBen.submit();
   }
      
   function controlla()
   {
   		//alert('controlla - scelta = '+lsceltaReato);
		if(document.elencoRicGEBen.numeroTitoli.value==0)
	    {
			alert("Nessun Titolo Presente");
	    }
		
		if(lsceltaReato == 'SI' )
		{
			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>[1].checked = true;
			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO%>.value = '<%=AnnoReato%>';
			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO%>.value = '<%=MeseReato%>';
			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.value = '<%=GiornoReato%>';
			
		}
		else
		{
			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>[0].checked = true;
		}
		
		radioScelta();
   }
   
   function Ricerca()
   {
      var radio = document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>;

      
      if (radio[1].checked)
      {
        // Data Commesso Reato 
        if (document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value.length==1)
          document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value='0'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.value;
        if (document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value.length==1)
          document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value='0'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO%>.value;

        var data_to_verify = document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value+'/'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value+'/'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.value;
        if (data_to_verify=='//' )
        {
           alert('Indicare la Data Commesso Reato per la Ricerca');
           document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.focus();
           return false;
        }
        
        if (!ControllaData(data_to_verify) )
        {
          alert('Data Commesso Reato non valida');
          document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.focus();
          return false;
        } 
       
        document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_DATA_COMMESSO_REATO%>.value = data_to_verify;
      }
      
      //alert('data_to_verify = '+data_to_verify);
      document.elencoRicGEBen.<%=IWebConstants.ACTION_FIELD%>.value = "siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaGEBenefici";
      document.elencoRicGEBen.submit();
      
      //return true; 
      document.getElementById("tableResult").style.display = "none";
      document.getElementById("bottRicerca").disabled = true;
   }
   
   function radioScelta()
   {
   		if(document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>[1].checked)
   		{
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.disabled = false;
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.disabled = false;
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.disabled = false;

   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR %>.disabled = false;
   		}
   		else if(document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>[0].checked)
   		{
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.disabled = true;
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.disabled = true;
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.disabled = true;

   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR %>.selectedIndex = 0;
        document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value = "";
        document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value = "";
        document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.value = "";
   			document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR %>.disabled = true;
   		}
   }

    function caricaData()
    {      
      var codDPR = document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR %>.value;

      var data = objDpr[codDPR];
      var DD = "";
      var MM = "";
      var YYYY = "";
      if (data!=undefined) {
        DD = data.substring(0,2);
        MM = data.substring(3,5);
        YYYY = data.substring(6,10);
      }

      document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value = DD;
      document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value = MM;
      document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.value = YYYY;
    }
   
   function Verify() 
   { 
	   // Data Commesso Reato 
	   if(document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>[1].checked)
	   {	
		   	if (document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value.length==1)
		     	document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value='0'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.value;
		   	if (document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value.length==1)
		     	document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value='0'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO%>.value;
		
		   	var data_to_verify = document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>.value+'/'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>.value+'/'+document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>.value;
		   	if (data_to_verify=='//' )
		   	{
		     	 alert('Indicare la Data Commesso Reato! ' );
		      	 document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.focus();
		       	 return false;
		   	}
		   
		  	if (!ControllaData(data_to_verify) )
		   	{
		       	alert('Data Commesso Reato NON valida');
		       	document.elencoRicGEBen.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO%>.focus();
		       	return false;
		   	}
	   }  	

      	return true; 
   } 
      
   </script>
  </head>
  
<body class="corpo" onload="controlla();">
  <table>
      <tr>
        <td class="LBG">
          <a href="Javascript:window.print();">
              <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
          </a>
        </td>
          
        <td class="LBG">
          <font class="label">Funzione :</font>
          <%-- font class="campo">ELENCO TITOLI per Richiesta Applicazione Benefici&nbsp;</font--%>
          <font class="campo">Richieste al G.E. Applicazione Benefici&nbsp;</font>
        </td>
        
        <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      	</td>
         
      </tr>
  </table>
  <br>
  <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
  
  <table cellspacing="2" cellpadding="2" width="100%">
  <tr><td>&nbsp;</td></tr>
  <tr><td class="l"><center><font class="label" style="color:red; font-size: 10pt"  >Selezionare i Titoli oggetto della Richiesta dal successivo Elenco  </font></center></td></tr>
  </table>

  <form method="POST" action="<%=IWebConstants.PG_MAIN%>" name="elencoRicGEBen" >
  
  	<input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadInsRichiestaGEBenefici">
  	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="modalita"   value="<%=modalita%>">
    <input type="hidden" name="numeroTitoli" value="<%=ListaTitoli.size()%>">
    <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_DATA_COMMESSO_REATO %>" value="">

<div id="Divdata" style="display:block">
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="L"> Visualizza Titoli&nbsp;&nbsp;&nbsp;<input type="radio" value="T" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>" onClick="javascript:radioScelta();">&nbsp;Tutti 
      	 &nbsp;&nbsp; oppure &nbsp;&nbsp;
   		   <input type="radio" value="R" name="<%=ICostantiRichiestePmInCumulo.CAMPO_CHECK_SCELTA_TITOLI%>" onClick="javascript:radioScelta();">&nbsp;solo Titoli con Reato Commesso &nbsp;&nbsp;Fino AL
      </td>
      <td class="l">
        <select name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_DPR%>" onChange="javascript:caricaData();">
          <%=listaDPR%>
        </select>
      </td>
      <td class="l">
          <input type="text" size="2" maxlength="2" 
                 name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_COMMESSO_REATO %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="2" maxlength="2" 
                 name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_COMMESSO_REATO %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
          /
          <input type="text" size="4" maxlength="4" 
                 name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_COMMESSO_REATO %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
      
      <td class="lNoBord" colspan="2">
   	<!--  INPUT class="bottone" type="submit" name="bottRicerca" value="Avvia Ricerca" onclick="javascript:Ricerca();">&nbsp;&nbsp; -->
     	<INPUT class="bottone" type="button" name="bottRicerca" id="bottRicerca" value="Avvia Ricerca" onclick="javascript:Ricerca();">&nbsp;&nbsp;
      </td>
    
    </tr>
  </table>
</div>  

  <br>
  <table cellspacing="2" cellpadding="2" width="100%">
    <tr>
      <td class="titolo" colspan=3 width=100%>Elenco  Titoli </td>
    </tr>
  </table>  
  
  <table id="tableResult">
    <tr>
      <td class="int" width="8%"  >Titolo</td>
      <td class="int" width="10%" >Data Titolo </td>
      <td class="int" width="8%"  >Numero sentenza</td>
      <td class="int" width="18%" >Autorità Titolo Esecutivo</td>
      <td class="int" width="10%" >Data Irrevocabilita</td>
      <td class="int" width="8%"  >Numero SIEP</td>
      <td class="int" width="25%" >Ufficio Esecuzione</td>
      <td class="int" width="5%"  >Selezione</td>
    </tr>

<%
  String NumAutoritaSiep = "";
  Iterator itx = ListaTitoli.iterator();
  while ( itx.hasNext())
  {
    TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
    NumAutoritaSiep = "";
%>
    <tr>
      <td class="L">
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoProvvedimento(),"")%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(lTitoCum.getAnnoSentenza())%>/<%=StringUtils.toStringJSP(lTitoCum.getNumeroSentenza(),"") %>
      </td>
      <td class="C" >
        <%=StringUtils.toStringJSP(lTitoCum.getDescrTipoAutoritaEmittente()+" "+lTitoCum.getDescrLuogoEmittente())%> 
      </td>
      <td class="C" nowrap>
        <%=StringUtils.toStringJSP(DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy"))%>
      </td>
      <td class="C" nowrap>
   <% 	if (lTitoCum.getProcedimentoCumulato()!=null) 
        {   
      		if("S".equals(lTitoCum.getProcedimentoCumulato().getFlagAccorpato()) )
      	  	{
      	  		UfficioModel lUfficioOrigine = lTitoCum.getProcedimentoCumulato().getUfficioOrigine();
      	  		NumAutoritaSiep = lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato() +"/"+ lTitoCum.getProcedimentoCumulato().getChiaveProgrOrigine();
      	  		NumAutoritaSiep += "<br> <font class=\"cRosso\">(Ex "+lUfficioOrigine.getCodTipoUfficio()+" di "+lUfficioOrigine.getDescrComune()+")</font>";   %>

				<%=NumAutoritaSiep%>
	<%  	}
      		else
      		{	%>	
      		
      			<%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getChiaveAnnoFasCumulato()+"/"+lTitoCum.getProcedimentoCumulato().getChiaveProgrFasCumulato())%>
    <% 		}
        }	
      	else 
      	{   %>
        	&nbsp;
    <%  } 	%>
      </td>
      <td class="C" >
        <% if (lTitoCum.getProcedimentoCumulato()!=null) { %>
        <%=StringUtils.toStringJSP(lTitoCum.getProcedimentoCumulato().getDescrTipoUfficioFasCumulato()+" di "+lTitoCum.getProcedimentoCumulato().getDescrLuogoUfficioFasCumulato() )%>
        <% } else { %>
        &nbsp;
        <% } %>
      </td>

      <td class="C">
          <input type="checkbox" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO %>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato(),"")%>">
        </td>
    </tr>
  
<%  
  }
%>
	</table>

<div id="diConferma" style="display:block" >	
 <table id="tabConferma" style="display:block">
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" name="bottConferma" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
 </table>
</div> 

</form>
  
</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("elencoRicGEBen");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
  