<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext"%>
<%@ page import="java.util.Iterator" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils" %>
<%@ page import="f3b.util.StringUtils" %>

<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel" %>
<%@ page import="siap.siep.modulocumulo.model.MisuraSicurezzaCumuloModel"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiMisuraSicurezzaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<jsp:useBean id="IstruttoriaCumulo"		scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaAllaSORV" 	scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="listaTitoliMisure"     scope="request" class="java.util.Vector"/>


<% 
//============================================================================== 
// Form per l'inserimento delle richieste alla SORVEGLIANZA di 
// Unificazione Misure diSicurezza
//============================================================================== 
%>

<html>
<head>
  <title> Gestione Richieste Unificazione Misura di Sicurezza</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" >

    function eseguiFunzione(action)
    {
      document.formName.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.formName.submit();
    }
    
    function indietro()
    {
      document.formIndietro.submit();
    }    
    
     

    function Verify() { 
        // Controllo Data Emissione
        if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE%>.value;
        if (data_to_verify=='//' )
        {
            alert('Indicare la Data Richiesta');
            document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
            return false;
        }
        
        if (!ControllaData(data_to_verify) )
        {
            alert('Data Richiesta non valida');
            document.formName.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
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
        <font class="campo">Inserimento Richiesta Unificazione Misure di Sicurezza&nbsp;</font>
        <% } else if( modalita.equals("M") ) { %>
        <font class="campo">Modifica Richiesta Unificazione Misure di Sicurezza &nbsp;</font>
        <% } %>
      </td>
      <td class="LBG">  
        <!--a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMallaSORV')" -->
        <a href="javascript:indietro()">
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

<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formIndietro">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActLoadElencoTitoliRichiestaSORVUnificaMS">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="HIDDEN" name="modalita" value="I">
</form>


 
<FORM method="POST" action="<%=IWebConstants.PG_MAIN%>" name="formName">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciRichiestaSORVUnificaMS">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ISTR_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">

  <input type="HIDDEN" name="modalita" value="<%=modalita%>">
  
  <% // solo in modifica %>
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA%>" value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getCodTipoRichiesta(),"")%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO%>" value="<%=StringUtils.toStringJSP(RichiestaAllaSORV.getIdRichiestePmInCumulo(),"")%>">

  <br>
  
  
  <%
  //============================================================================
  // Seleziona dalla lista
  //============================================================================
  %>
  
  <div id="idTitolo_"  style="display:block" >
  <table cellspacing="2" cellpadding="2" align="center" width="95%">
  
    <tr>
      <td class="titolo" colspan="11">Misure di Sicurezza sui titoli caricati in cumulo</td>
    </tr>    
    <tr>
      <td colspan="11">&nbsp;</td>
    </tr>
<%
	//============================================================================
	// Sezione con i dati delle Misure Di Sicurezza
	//============================================================================
	if ( listaTitoliMisure == null || listaTitoliMisure.size() == 0 ) 	
	{ 	%>  
	<tr>
		<td colspan="10">Nessun dato presente</td>
	</tr>
<% } else {
	  	Iterator itx = listaTitoliMisure.iterator();
	  	while ( itx.hasNext())
  		{
    		TitoloCumulatoModel lTitoCum = (TitoloCumulatoModel) itx.next();
%>
			<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_TITOLO_SELEZIONATO%>" value="<%=StringUtils.toStringJSP(lTitoCum.getIdTitoloCumulato()) %>" >
			
		    <tr style="background-color: rgb(255,255,153);">
		      <td class="L" colspan="11"><%=lTitoCum.getDescrTipoProvvedimento()%>
		      					  N. <font class="campo"><%=lTitoCum.getAnnoSentenza()%> / <%=lTitoCum.getNumeroSentenza()%></font>
		      					 Del <font class="campo"><%=DateUtils.getDateToString(lTitoCum.getDataProvvedimento(),"dd-MM-yyyy")%></font>
		      			  Emessa da: <font class="campo"><%=lTitoCum.getDescrTipoAutoritaEmittente() %></font>
		      			  		  di <font class="campo"><%=lTitoCum.getDescrLuogoEmittente() %></font> 
		       Data irrevocabilità : <font class="campo"><%=DateUtils.getDateToString(lTitoCum.getDataIrrevocabilita(),"dd-MM-yyyy")%></font></td>
		    </tr>
    		
<%    		
    		if(lTitoCum.getMisureSicurezzaCumulo()!=null && lTitoCum.getMisureSicurezzaCumulo().size()>0)
 			{
%>
			    <tr>
			      <td class="titolo" width="30%">Tipo Misura</td>
			      <td class="titolo" width="5%">Durata</td>
			    </tr>    
<% 			
	  			int id_rec = 0;
	  			Iterator itxMS = lTitoCum.getMisureSicurezzaCumulo().iterator();
	  			while ( itxMS.hasNext()) 
	  			{	
		    		MisuraSicurezzaCumuloModel lMSCum = (MisuraSicurezzaCumuloModel)itxMS.next();
%>
					<input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_MIS_SIC_CUM_SEL%>" value="<%=StringUtils.toStringJSP(lMSCum.getIdMisuraSicurezzaCumulo() ) %>" >
				    <tr>
				      <td class="L"  >&nbsp;<font class="campoLow"><%=lMSCum.getDescrTipo()%></font></td>
				      <td class="C" >
				        <% if (lMSCum.getNumAnni()!=null) { %>
				          Anni:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumAnni() )%>  
				          <% } %>
				        <% if (lMSCum.getNumMesi()!=null) { %>
				          &nbsp;&nbsp;Mesi:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumMesi() )%>  
				          <% } %>
				        <% if (lMSCum.getNumGiorni()!=null) { %>
				          &nbsp;&nbsp;Giorni:&nbsp;<%=StringUtils.toStringJSP(lMSCum.getNumGiorni() )%>  
				          <% } %>
				      </td>
				    </tr>
			<%	}
 			}
		%>
		<tr><td colspan="11">&nbsp;</td></tr>
		<% 			
		}

	}
%>    
  </table>
  </div>
  
  <br>
  <table width="95%" align="center">
    <tr>
      <td colspan="2" class="Titolonocap">Richiesta Unificazione Misure</td>
    </tr>
    <tr>
      <td class="l" colspan="1" width="300px">Data Richiesta </td>
      <td class="L" colspan="1">
      	<input value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" > -
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>  
    <tr>
      <td class="l" colspan="1">Motivazioni</td>
      <td class="l" colspan="1">
       <TEXTAREA title="Motivazioni" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI%>" cols="80" rows="3"></textarea>
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
  var frmvalidator  = new Validator("formName");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>
 