<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="siap.sius.permesso.model.LicenzaModel"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.List"%>

<%@ page import="f3b.log.LogF3B"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.calendar.model.CalendarModel"%>
<%@ page import="siap.sico.util.CalendarUtil"%>

<%@ page import="siap.siep.modulocumulo.model.LibAnticipataCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiLibAnticipataCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiStatoEsecTitoloCumulato"%>
<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>

<%@ page import="org.apache.log4j.Logger"%>

<jsp:useBean id="IstruttoriaCumulo"    	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"       	scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="StatoEsecTitoloCum"   	scope="request" class="siap.siep.modulocumulo.model.StatoEsecTitoloCumulatoModel"/>

<jsp:useBean id="modalita" 				scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEmi"  			scope="request" class="java.lang.String"/>
<jsp:useBean id="tipoProvvedimento"     scope="request" class="java.lang.String"/>
<jsp:useBean id="EsitoProvvedimento"    scope="request" class="java.lang.String"/>
<jsp:useBean id="oggettoProvvedimento"  scope="request" class="java.lang.String"/>

<!-- 				LoadInserisciScomputoPermessiCumulo.jsp                       -->

<%

//[FT] - 03/08/2016 - MAC_LOG - Logger per SIESLog
final Logger siesLogger = Logger.getLogger(LogF3B.SIES_LOG);

//=================================================================================
// form per l'inserimento del Provvedimento di Scomputo Permessi (Titolo Cumulato)
//=================================================================================
BigDecimal lNumeroGiorniRiduzione = null;
LibAnticipataCumuloModel LicenzaMod = null;
if(modalita.equals("M"))
{	
	if( StatoEsecTitoloCum!=null && StatoEsecTitoloCum.getListaLiberazioniAnticipate()!=null && 
		StatoEsecTitoloCum.getListaLiberazioniAnticipate().size() > 0 )
	{	
		LicenzaMod = (LibAnticipataCumuloModel) StatoEsecTitoloCum.getListaLiberazioniAnticipate().get(0);
		if(LicenzaMod.getIdLibAnticipataCumulo()!=null && LicenzaMod.getNumeroGiorni()!=null)
		{
			lNumeroGiorniRiduzione = LicenzaMod.getNumeroGiorni();
		}
		siesLogger.debug("--XX-- Modifica Scomputo Permesso - LicenzaMod = "+LicenzaMod );
	}	
}	

%>

<html>

<head>
  <title> [S.I.E.S.] - Rideterminazione Pena - Scomputo Pewrmessi</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript">
    //============================================================================
    // Funzione per il controllo dei dati prima della submit
    //============================================================================
    
    function Verify()
    {
      //===========================================
      // Controllo sui campi altra autorità
      //===========================================
      // Data Emissione
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value;

      var data_to_verify = document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>.value+'/'+document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>.value;
      if (data_to_verify=='//' )
      {
          alert('Indicare la Data di emissione Provvedimento');
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      }
      
      if (!ControllaData(data_to_verify) )
      {
          alert('Data di emissione Provvedimento non valida');
          document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>.focus();
          return false;
      }
      
   	  // Anno e numero Provvedimento
      if ( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value.length==0 &&
      		document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value !="")
      {
        alert('Inserire Anno Provvedimento');
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      if ( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.value.length==0 &&
      		document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>.value != "")
      {
        alert('Inserire Numero Provvedimento');
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>.focus();
        return false;
      } 
      
      // Tipo provvedimento
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.selectedIndex==0)
      {        
        alert("Selezionare Tipo Provvedimento");
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>.focus();
        return false;
      }
      
      // Anno e numero SIUS
      if ( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value.length==0 &&
      		document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value !="")
      {
        alert('Inserire Anno Procedimento SIUS ');
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.focus();
        return false;
      }
      
      if ( document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.value.length==0 && 
      		document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>.value != "")
      {
        alert('Inserire Numero Procedimento SIUS');
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>.focus();
        return false;
      } 
      
      // Autorità emittente 
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex==0)
      {        
        alert("Selezionare Autorità Emittente");
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
        return false;
      }
      
      // Sede 
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.value=="")
      {        
        alert("Selezionare Sede Autorità Emittente");
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
        return false;
      }

      // Oggetto 
      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=="-")
      {        
        alert("Selezionare Oggetto del Provvedimento");
        document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.focus();
        return false;
      }
      
      // Esito
   //   if (document.f.< %=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.selectedIndex==0)
   //   {        
   //     alert("Selezionare Esito del Provvedimento");
   //     document.f.< %=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.focus();
   //     return false;
   //   }

      // Giorni 
      if (document.f.<%=ICostantiLibAnticipataCumulo.CAMPO_NUMERO_GIORNI%>.value=="")
      {        
        alert("Inserire Giorni");
        document.f.<%=ICostantiLibAnticipataCumulo.CAMPO_NUMERO_GIORNI%>.focus();
        return false;
      }
      
       return true;
    }

    //==========================================================================
    //
    //==========================================================================
    function ListaComuni(a_formname,a_fieldname,codTipoUfficio)
    {
       var desktop;
       desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    
    //==============================================================================================
    // Accoppiamento Motivo_Provvedimento 	- - - > Esito_Provvedimento 
    //  		Reclamo Scomputo   0039	  	con		Rideterminazione pena a seguito reclamo  0994
    //			Esclusione Computo 2250   	con		Rideterminazione pena a seguito scomputo 0958
    //==============================================================================================
    function scegliProvv()	// per Ora NON viene usato l'accoppiamento, inseriamo solo il Cod.Motivo_Provvedimento 
    {
	      if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=='2250')
	      {
			for(var k=0;k < document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options.length;k++)
			{
			    if(document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options[k].value=="0958")
			    {
			    	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options[k].selected=true;
			      	break;
			  	}
			}
		  }
	      else
	      {		
		  	if (document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>.value=='0039')
		  	{
				for(var k=0;k<document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options.length;k++)
				{
				    if(document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options[k].value=="0994")
				    {
				    	document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_ESITO%>.options[k].selected=true;
				      	break;
				  	}
				}
			 }
		 }
    }
    
 // Torna Insietro
    function eseguiFunzione(action)
    {
      document.f.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.f.submit();
    }
 
  </script>
</head>
<body class="corpo" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
   <% if(modalita.equals("I"))
   	  {	%>   
        <font class="campo">Inserimento Scomputo Permesso</font>
   <% }
      else if(modalita.equals("M"))
      { %>
 		<font class="campo">Modifica Scomputo Permesso</font>
 <%	  } %>     
          
      </td>

      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaScomputoPermessiCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>

  </table>

  <br>
    <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
  <br>

<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="f">

  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciScomputoPermessiCumulo">
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  <input type="hidden" name="modalita"   value="<%=modalita%>">
  
	
<%
	if( modalita.equals("M") )
	{
%>  
		<input type="hidden" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ID_STATO_ESEC_TITOLO_CUMULATO%>" value="<%=StatoEsecTitoloCum.getIdStatoEsecTitoloCumulato()%>" >
		<input type="hidden" name="<%=ICostantiLibAnticipataCumulo.CAMPO_ID_LIB_ANTICIPATA_CUMULO%>" value="<%=LicenzaMod.getIdLibAnticipataCumulo()%>" >
		<input type="HIDDEN" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_FLAG_STATO %>"            		  value="<%=StatoEsecTitoloCum.getFlagStato() %>">
<%	} %>

  <table width="100%" >
    <tr>
      <td colspan="4" class="titolo">Dati Provvedimento</td>
    </tr>
    
    <%
    //========================
    // Dati della Sorveglianza
    //========================
	%> 
    <tr>
      <td class="l" width="25%" >Data emissione provvedimento<font class=ob>(*)</font></td>
      <td class="l">
   <%
	  if( modalita.equals("I") )
	  {
%>   
        <input type="text" Title="Giorno Emissione provvedimento" value="" 
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value=""   
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>"   maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="" 
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>"   maxlength="4" size="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
<%	  }
   	  else
   	  {	%>
   	    <input type="text" Title="Giorno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"dd") ) %>" 
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_GIORNO_DATA_EMISSIONE%>" maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"MM") ) %>"   
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_MESE_DATA_EMISSIONE%>"   maxlength="2" size="2" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Emissione provvedimento" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(StatoEsecTitoloCum.getDataEmissione(),"yyyy") ) %>" 
        	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_DATA_EMISSIONE%>"   maxlength="4" size="4" 
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
   	  
<%	  }	 %>
      </td>
      <td class="l">Anno / Numero Provvedimento</td>
      <td class="l">
         <input Title="Anno Provvedimento"   value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProvvedimento(), "" ) %>" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROVVEDIMENTO%>"  
         	type="text" size="4" maxlength="4" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)"> /
         <input Title="Numero Provvedimento" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProvvedimento(), "" ) %>" 
         	name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROVVEDIMENTO%>" 
         	type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
      </td>
    </tr>

    <tr>
      <td class="l">Tipo Provvedimento <font class=ob>(*)</font></td>
      <td class="l">
        <select Title="Tipo Provvedimento" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_TIPO_PROVVEDIMENTO%>">
          <%=tipoProvvedimento%>
        </select>
      </td>
      <td class="l">Anno / Numero SIUS</td>
	  <td class="l" >
			<input Title="Anno Fascicolo Sius" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getAnnoProcedimento(), "" ) %>" 
				name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_ANNO_PROCEDIMENTO%>" 
				type="text" size="4" maxlength="4"  <%=IWebConstants.UTIL_DATA_ANNO%>>
			/
			<input Title="Numero Sius" value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getProgrProcedimento(), "" ) %>" 
				name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_PROGR_PROCEDIMENTO%>" 
				type="text" size="6" maxlength="6" onkeypress="return TicTabNumField(this,event)">
	  </td>
    </tr>
    
    <tr>
      <td class="l">Autorità Emittente <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Autorità Emittente" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>">
          <%=autoritaEmi%>
        </select>
      </td>

      <td class="l" colspan="2">Sede <font class="ob">(*)</font> &nbsp;
        <input title="Sede Autorita"  type="text" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>"  
        	value="<%=StringUtils.toStringJSP(StatoEsecTitoloCum.getDescrLuogoEmittente(), "-" ) %>"maxlength="35" size="35" >
         <a href="Javascript:ListaComuni('f','<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_LUOGO_EMITTENTE%>',document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>[document.f.<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_UFFICIO_EMITTENTE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border="0">
        </a>
      </td>
    </tr>
    
    <tr>
      <td class="l">Oggetto<font class=ob>(*)</font></td>
      <td class="l" colspan="3">
        <select Title="Oggetto" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_COD_MOTIVO%>" >
		   <%=oggettoProvvedimento%>
        </select>
      </td>
    </tr>
    
	<tr><td>&nbsp;</td></tr>
<%
//==============================================================================
//        Sezione per specificare i giorni da computare 
//==============================================================================
%>
    <tr>
      <td class="l" colspan="4">
      	<font class="label">Giorni da scomputare <font class=ob>(*)</font>&nbsp;</font>
      	<input type="text" name="<%=ICostantiLibAnticipataCumulo.CAMPO_NUMERO_GIORNI %>" value="<%=StringUtils.toStringJSP(lNumeroGiorniRiduzione,"") %>" 
      		maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
      </td>
	</tr>

  <tr>
    <td class="l">Note :&nbsp;</td>
    <td class="l" colspan="3">
      <textarea cols="100" rows="2" name="<%=ICostantiStatoEsecTitoloCumulato.CAMPO_NOTE %>"><%=StringUtils.toStringJSP(StatoEsecTitoloCum.getNote(), "" ) %></textarea>
    </td>
  </tr>

</table>  
<br>
<table>
  <tr>
    <td class="lNoBord" colspan="2">
     <INPUT class="bottone" type="submit" name="bottConferma" value="Conferma">&nbsp;&nbsp;
    </td>
  </tr>
</table>

</form>
<script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");

</script>
</body>
</html>

    