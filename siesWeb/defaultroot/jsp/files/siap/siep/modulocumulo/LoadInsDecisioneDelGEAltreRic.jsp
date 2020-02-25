<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiRichiestePmInCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.model.RichiesteInviateCumModel"%>
<%@ page import="siap.siep.modulocumulo.model.TitoloCumulatoModel"%>
<%@ page import="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>

<jsp:useBean id="IstruttoriaCumulo" 	scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="modalita" 			   	scope="request" class="java.lang.String"/>
<jsp:useBean id="RichiestaGE"     		scope="request" class="siap.siep.modulocumulo.model.RichiestePmInCumuloModel"/>
<jsp:useBean id="Titolo"     			scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="ProvvGECum"     		scope="request" class="siap.siep.modulocumulo.model.ProvvedimentoGeSorvCumModel"/>
<jsp:useBean id="UfficioEmittente"     	scope="request" class="java.lang.String"/>


<!-- 							LoadInsDecisioneDelGEAltreRic								 -->
<%
//==============================================================================
//  Form con le funzioni di Inserimento/Modifica della decisione del G.E.
//	a seguito di: Altre Richieste del P.M. - (cod = 014) 
//  (Gestione Cumulo)
//==============================================================================

RichiesteInviateCumModel lRicInv = null;
if(RichiestaGE!=null && RichiestaGE.getRichiesteInviateCum()!=null)
{
	lRicInv = (RichiesteInviateCumModel)RichiestaGE.getRichiesteInviateCum();
}

//Data Richiesta al GE
String lDataRich = StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd/MM/yyyy"));

// Conformità Decisione del GE
String lconforme="";
if("M".equals(modalita))
{
	if(ProvvGECum.getFlagConforme()!=null)
		lconforme = ProvvGECum.getFlagConforme();
}

%>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Decisione del GE su Altre Richieste del PM </title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_CONFIRM%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
  <script language="JavaScript">
  	
	//Apre la finestra con la Lista delle Sedi uffici in base alla tipologia di Ufficio Selezionata
  	function ListaUfficiComuni(a_formname, a_fieldname, codTipoUfficio)
  	{
    	desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  	}
	
    //==========================================================================
    // Ritorna alla Griglia Delle Richieste al GE
    //==========================================================================
    function tornaIndietro(action)
    {
      document.LoadInsDecGEaltro.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInsDecGEaltro.submit();
    }
    
    function Verify() 
    { 
   		// Anno e Numero Procedimento SIGE
        if(document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
           document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value != ''	 )
        {
        	alert('Anno Procedimento G.E. NON valido');
            document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        } 
         
        if( document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value != '' && 
        	document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
        	alert('Numero Procedimento G.E. NON valido');
            document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.focus();
            return false;
        }
         
        if(document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.value == '' && 
           document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>.value == ''	 )
        {
  			alert('digitare Anno e Numero Procedimento G.E.');
            document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>.focus();
            return false;
        }

 	   // Data Emissione Procedimento SIGE
       if ( document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value;
       if ( document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value.length==1)
         	document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value='0'+document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value;

       var data_to_verify = document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D%>.value+'/'+document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D%>.value;
       if (data_to_verify=='//' )
       {
           alert('Indicare la Data Emissione Procedimento');
           document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
       if (!ControllaData(data_to_verify) )
       {
           alert('Data Emissione Procedimento NON valida');
           document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D%>.focus();
           return false;
       }
       
    	// Data Emissione Procedimento SIGE deve essere <= Data del Giorno
    	var data_od = '<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
		if(!CompareDate(data_to_verify, data_od))
		{
			alert('Data Emissione Procedimento SIGE NON può essere superiore alla Data Odierna');
			document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
      		return false;
		}
		
		// Data della Richiesta deve essere <= Data Emissione Procedimento SIGE 
	 	var data_Ric = '<%=lDataRich%>';
	 	if(!CompareDate(data_Ric, data_to_verify))
	 	{
			alert('Data Emissione Procedimento SIGE DEVE essere superiore o uguale \nalla Data Richiesta del PM al G.E.');
			document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>.focus();
     		return false;
	 	}
       
       // Tipo Ufficio e sede Ufficio Emittente
       if(document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.value.length == 1	 )
       {
      	 	alert('Ufficio Emittente non valido');
          	document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.focus();
          	return false;
       } 
       
       if(document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value == '-' || 
      	 document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 1 ||
      	 document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.value.length == 0 )
       {
      	 	alert('Sede Ufficio Emittente non valida');
          	document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>.focus();
          	return false;
       }
       
        return true;
		//return false;
    }
    
  </script>

</head>

<body class="corpo" >
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;
<%	if("I".equals(modalita) )
	{	%>
        	<font class="campo">Inserimento Decisione del G.E. - Altre Richieste del P.M.</font>
<% 	}	
	else if("M".equals(modalita) )
	{	%>
			<font class="campo">Modifica Decisione del G.E. - Altre Richieste del P.M.</font>		
<%	}	%>	        
      </td>
      <td class="LBG"><!-- Tasto indietro alla Griglia delle Richieste al GE-->
        <a href="javascript:tornaIndietro('siap.siep.modulocumulo.action.ActLoadGrigliaRichiesteDelPMalGE')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  <br>

<% // INCLUDE DEL DETTAGLIO FASCICOLO%>
	<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>

<% // INCLUDE DEL DETTAGLIO DELL'ISTRUTTORIA %>
  <table align="center" width="95%" style="border:0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
  </table>


<form action="<%=IWebConstants.PG_MAIN%>" method="post" name="LoadInsDecGEaltro">
  <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciDecisioneDelGECumulo">
  <input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ID_RICHIESTE_PM_IN_CUMULO %>" value="<%=RichiestaGE.getIdRichiestePmInCumulo() %>" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_RICHIESTA %>" value="01" >
  <input type="hidden" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_TIPO_PROVVEDIMENTO %>" value="03" >
  <input type="hidden" name="modalita" value="<%=modalita%>" >

<%	// =======================================================
	//		Dati sui Titoli coinvolti nella Richiesta	
	// =======================================================
%>

  <table cellpadding="2" cellspacing="2" width="98%" align="center" style="border:0;">
     <tr><td class="Titolo" colspan="100%">In relazione al Titolo</td></tr>
<%      	
  String AnnoNumero ="";
  AnnoNumero = Titolo.getAnnoSentenza() +"/"+Titolo.getNumeroSentenza();
%> 
    <tr>
      <td class="l" colspan="8">
        <font class="label"><%=StringUtils.toStringJSP(Titolo.getDescrTipoProvvedimento() )%>&nbsp;N. &nbsp; </font>
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
    <tr><td></td></tr>   
<%
  //========================================================================
  //  Dati esclusivi della Richiesta
  //========================================================================
  %>
  <table width="95%" align="center">
    <tr><td colspan="4" class="Titolonocap">Dati della richiesta</td></tr>
    <tr>
      <td class="l" width="200px">Data Richiesta</td>
      <td class="l" colspan="2">
      	<font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(RichiestaGE.getDataEmissione(),"dd-MM-yyyy"),"-")%></font>
      </td>
    </tr>  	
    <tr>
      <td class="l" width="200px">Tipo Richiesta:</td>
      <td class="l" >
        <font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getDescrTipoAnnotazione() )%></font>
      </td>
    </tr>

<%	if(RichiestaGE.getMotivazioni()!=null && !RichiestaGE.getMotivazioni().equals("") )
	{	%>
	<tr>
      <td class="l" colspan="1">Motivazioni :  </td>
      <td class="l" colspan="3">
      	<font class="campo"><%=StringUtils.toStringJSP(RichiestaGE.getMotivazioni(),"" )%></font>
      </td>
    </tr>  		
<%	} %>
  </table>
  
 <%
  //=======================================================
  //  		Eventuale Richiesta Inviata
  //=======================================================
 %>
  
<%	if(lRicInv!=null && lRicInv.getIdRichiesteInviateCum()!=null )
	{	%>  
  <table width="95%" align="center" style="display:block">
  	<tr><td> </td></tr>
    <tr>
      <td class="l" colspan="1" width="200px">Inviata a : </td>
      <td class="l" colspan="1"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrUfficioDest(),"")%></font>
      	 di <font class="campo"><%=StringUtils.toStringJSP(lRicInv.getDescrLuogoDest(),"")%></font>
      </td>
      <td class="l" colspan="1">in data: <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(lRicInv.getDataEmissione(),"dd-MM-yyyy"),"-")%></font></td>
    </tr>
<%	  if( !("null").equals(lRicInv.getContenuto()) )
	  {		%>
	  <tr>
	  	<td class="l" colspan="1" width="200px">Contenuto </td>
        <td class="l" colspan="2"><font class="campo"><%=StringUtils.toStringJSP(lRicInv.getContenuto(),"")%></font></td>
      </tr>
 <%	  } %>           
  </table>
<%	} %>  
  	
  	<br>
  
<!-- 							Inizio Dati Inseribili/Modificabili										 -->  
  <%
  //========================================================================
  //  Dati esclusivi della Decisione G.E. da Inserire / Modificare
  //========================================================================
  %>  
<tr>
  <td colspan="100%" align="center">

  <table width="95%" align="center">
    <tr><td colspan="3" class="Titolonocap">Decisione del GE</td></tr>      
    <tr>
      <td class="l">Ordinanza <font class="ob">(*)</font> :</td>
	  <td class="l">
        Anno/Numero Procedimento SIGE
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_PROVV%>" size=4 maxlength=4 value="<%=StringUtils.toStringJSP(ProvvGECum.getAnnoProvv(),"") %>" 
          		onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
        /
        <input type="text" name="<%=ICostantiRichiestePmInCumulo.CAMPO_NUMERO_PROVV%>" size="8" maxlength="6" value="<%=StringUtils.toStringJSP(ProvvGECum.getNumeroProvv(),"") %>" >
      </td>
      <td class="l">
        <font class="label">Data emissione Procedimento</font><font class="ob">(*)</font>
          &nbsp;&nbsp;
         <input type="text"  title="Giorno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_GIORNO_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "dd"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
         <input type="text"  title="Mese di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MESE_DATA_EMISSIONE_D %>" maxlength="2" size="2" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "MM"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)" >
         /
         <input type="text"  title="Anno di arrivo documento" name="<%=ICostantiRichiestePmInCumulo.CAMPO_ANNO_DATA_EMISSIONE_D %>" maxlength="4" size="4" 
                 value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(ProvvGECum.getDataD(), "yyyy"), "") %>" 
                 onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)" >
      </td>	
    </tr>
    
    <tr>
      <td class="l">Ufficio Emittente <font class="ob">(*)</font> :</td>
      <td class="l" colspan="1">
          <select Title="Ufficio Emittente" name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>">
            <%=UfficioEmittente%>
          </select>
      </td>
      <td class="l">Sede Ufficio Emittente <font class="ob">(*)</font> :
          <font class="campo">
            <input type="text" Title="Luogo Ufficio Emittente" size="35"
                   name="<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>" value="<%=StringUtils.toStringJSP(ProvvGECum.getDescrLuogoEmittente(), "") %>" >
            <a href="Javascript:ListaUfficiComuni('LoadInsDecGEaltro','<%=ICostantiRichiestePmInCumulo.CAMPO_COD_LUOGO_EMITTENTE%>',
            					document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>[document.LoadInsDecGEaltro.<%=ICostantiRichiestePmInCumulo.CAMPO_COD_UFFICIO_EMITTENTE%>.options.selectedIndex].value);">
              <img src="/images/filefolder.gif" border=0>
            </a>
          </font>
      </td>
    </tr>
  </table>
    
    <table width="95%" align="center">
      <tr>
        <td class="l" colspan=3>
<% 
		String checkC = "";
      	String checkD = "";
      	String checkR = "";
      	String checkI = "";
      
      	if("C".equals(ProvvGECum.getFlagConforme()) ){
        	checkC = "checked";
      	}
      	else if("D".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkD = "checked";
      	}
      	else if("I".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkI = "checked";
      	}
      	else if("R".equals(ProvvGECum.getFlagConforme()) ) { 
        	checkR = "checked";
      	}
      	else { checkC = "checked"; }
%>  
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="C" <%=checkC%> >in conformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Conforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="D" <%=checkD%> >in difformita' alla richiesta del PM &nbsp;&nbsp;	<!-- Difforme -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="R" <%=checkR%> >rigetta &nbsp;&nbsp;	<!-- Rigetta -->
          <input type="radio" name="<%=ICostantiRichiestePmInCumulo.CAMPO_FLAG_CONFORME %>" value="I" <%=checkI%> >dichiara inammissibile &nbsp;&nbsp;	<!-- Inammissibile -->	
	        
        </td>
      </tr>
    </table>
     
  <table width="95%" align="center">
    <tr>
      <td class="l" colspan="1" > Motivazioni </td>
      <td class="l" colspan="3" >
        <textarea cols="100" rows="3" name="<%=ICostantiRichiestePmInCumulo.CAMPO_MOTIVAZIONI_D %>"><%=StringUtils.toStringJSP(ProvvGECum.getMotivazioniD(),"")%></textarea>
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

</td>
</tr>
<!-- 					end dati inseribili / modificabili													-->
</table>  
</form>

</body>

<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("LoadInsDecGEaltro");
  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>

</html>


