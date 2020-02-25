<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="Titolo" 				scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>  
<jsp:useBean id="RichiestaSORV"		 	scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>

<!-- 					LoadInserisciRichiestaSORVRevocaMA							 -->
<% 
//============================================================================== 
// Form per l'inserimento e della richieste alla SORVEGLIANZA di 
// Revoca Misura ALternativa
//============================================================================== 
%>

<html>
<head>
  <title> Gestione Richieste alla SORV Revoca Misura Alternativa</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.InsRicSorvRevoMA.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.InsRicSorvRevoMA.submit();
    }
   

    function Verify() 
    { 
   		// Data Emissione
       if (document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
         	 document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
       if (document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
           document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

       var data_to_verify = document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Richiesta');
           document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Richiesta non valida');
           document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
           return false;
       } 
       
       // Data Emissione deve essere <= Data del Giorno
       var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
 	   if(!CompareDate(data_to_verify, data_od))
 	   {
 			alert('Data Richiesta NON può essere superiore alla Data Odierna');
 			document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
         	return false;
 	   }
 	   
 		// Campo Motivo Richiesta
	 	if(document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>.value == "" || 
	   	   document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>.value.length == 0	)
	    {
	   		alert("Motivazione Richiesta Obbligatoria");
	   		document.InsRicSorvRevoMA.<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>.focus();
	   		return false;
	    }
 	   
      return true; 
    } 

  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;

        <% if (modalita.equals("I") ) { %>
        <font class="campo">Inserimento Richiesta alla SORVEGLIANZA Revoca di Misura Alternativa&nbsp;</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Richiesta alla SORVEGLIANZA Revoca di Misura Alternativa &nbsp;</font>
        <% } %>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
 
  <br>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>
 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="InsRicSorvRevoMA">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaSORVRevocaMA">
  
  <input type="HIDDEN" name="modalita" value="<%=modalita%>">

  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" value="02">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_ANNOTAZIONE%>" value="031">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=StringUtils.toStringJSP(RichiestaSORV.getIdRichiestePmInCumulo(),"")%>">
  
  <%
  //============================================================================
  // Sezione con i dati delle Misura Alternativa concessa in relazione al Titolo
  //============================================================================
  %>
  <div id="idTitolo_"  style="display:block" >
    <table width="95%" align="center">
      
<%	String AnnoNumero = "";
	AnnoNumero = Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();	
%>
	  <tr><td colspan="1" class="Titolonocap" >In relazione al Titolo Esecutivo</td></tr>
  	  <tr>	
      <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(Titolo.getIdTitoloCumulato()) %>" >
		<td class="l" >
	  	<font class="label"><%=StringUtils.toStringJSP(Titolo.getDescrTipoProvvedimento() )%>&nbsp;N. </font>
      	<font class="campo"><%=AnnoNumero%></font>&nbsp;
      	&nbsp;<font class="label"> del </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataProvvedimento(),"dd-MM-yyyy"),"-")%></font>&nbsp;	
         
      	&nbsp;<font class="label"> Emessa da </font>
      	<font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrTipoAutoritaEmittente(), "") %></font>
      	<font class="label">&nbsp;di&nbsp; </font>
      	<font class="campo"><%=StringUtils.toStringJSP(Titolo.getDescrLuogoEmittente(), "") %></font>
          
      	&nbsp;<font class="label"> Irrevocabile il  </font>
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(Titolo.getDataIrrevocabilita(),"dd-MM-yyyy"),"-")%></font>&nbsp;
    	</td>
  	  </tr>
  	  
<%	StatoEsecTitoloCumulatoModel lStatoMod = (StatoEsecTitoloCumulatoModel)Titolo.getStatoEsecTitoloCumulato();  %>
	  
	  <tr>
      	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO %>" value="<%=StringUtils.toStringJSP(lStatoMod.getIdStatoEsecTitoloCumulato() ) %>" >
      	<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO %>" value="<%=StringUtils.toStringJSP(lStatoMod.getCodMotivo() ) %>" >
      	<td class="l" >
          <font class="label" style="color:red; text-align:left"> Misura Concessa: </font>
          &nbsp;<font class="campoLow"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>&nbsp;
          
          &nbsp;<font class="label"> Concessa da </font>
          <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrUfficioEmittente(), "") %></font>
          <font class="label">&nbsp;di&nbsp; </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrLuogoEmittente(), "") %></font>
      	  &nbsp;<font class="label">&nbsp;con&nbsp; </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrTipoProvvedimento(), "") %></font>
      	  &nbsp;<font class="label"> del </font>
      	  <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lStatoMod.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>&nbsp;
        </td>
      </tr>

    </table>
  </div>
  
  <br>
  <table width="95%" align="center">
    <tr>
      <td colspan="2" class="Titolonocap">Richiesta al Giudice di Sorveglianza</td>
    </tr>
	<tr>
      <td class="l" width="30%" >Data Richiesta </td>
 <% if (modalita.equals("I") ) 
 	{ %>      
      <td class="L" >
        <input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
 <% }
 	else if (modalita.equals("M") ) 
 	{ %>
 		<td class="L" >
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "dd"),"" )%>"   
          	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "MM"), "" )%>"   
          	type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
          <input value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaSORV.getDataEmissione(), "yyyy"), "" )%>" 
          	type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      	</td>
 <% }  %>     
    </tr>
    <tr>
      <td class="l" width="30%" >Si richiede la Revoca della Misura Alternativa</td>
      <td class="L" >
        <font class="campo"><%=StringUtils.toStringJSP(lStatoMod.getDescrMotivo(),"") %></font>
      </td>
    </tr>
    <tr>
      <td class="l" > Motivazioni </td>
      <td class="l" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI %>"><%=StringUtils.toStringJSP(RichiestaSORV.getMotivazioni(), "" )%></textarea>
      </td>
    </tr>
  </table>

  <br>
  
  <table cellspacing="2" cellpadding="2" width="95%" align="center">
    <tr>
      <td align="left">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>
  </table>

</FORM>

</body>
</html>

<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("InsRicSorvRevoMA");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 