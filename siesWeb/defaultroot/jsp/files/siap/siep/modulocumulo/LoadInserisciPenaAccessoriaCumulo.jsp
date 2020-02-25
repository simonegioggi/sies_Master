<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>

<%@ page import="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiPenaAccessoriaCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>

<jsp:useBean id="penaAccessoriaCumulo"  scope="request" class="siap.siep.modulocumulo.model.PenaAccessoriaCumuloModel"/>

<jsp:useBean id="TipoPenaAccessoria"    scope="request" class="java.lang.String"/>
<jsp:useBean id="DurataPeneAccessorie"  scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaOrdinanza"     scope="request" class="java.lang.String"/>
 
<!-- 			LoadInserisciPenaAccessoriaCumulo		 -->
<html>
<head>
  <title> Gestione Pene Accessorie Cumulo </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_JQUERY%>"></script>
    
  <script language="JavaScript" >  
    //==========================================================================
    // Ritorna alla lista delle Pene Accessorie per il Titolo
    //==========================================================================
    function eseguiFunzione(action)
    {
      document.LoadInserisciPenaAccessoria.<%=IWebConstants.ACTION_FIELD%>.value = action;
      document.LoadInserisciPenaAccessoria.submit();
    }
    
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }

    function cambia()
    {
      //alert("cambia");
      var cod;
      cod = document.LoadInserisciPenaAccessoria.<%=ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA%>.value;
      if (cod == "999") // Altre pena accessoria
      {
        document.LoadInserisciPenaAccessoria.<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>.disabled = false;
      }
      else{
        document.LoadInserisciPenaAccessoria.<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>.value="";
        document.LoadInserisciPenaAccessoria.<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>.disabled = true;
      }
      
      return;
    }
    
 	// Codice di prova per gestire il js con jQUery
    function cambiaJQuery()
    {
      if ( $('#<%=ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA%>').val()=='999') // Altre pena accessoria
      {
  
        $("#<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>").prop('disabled',false);
      }
      else{
        $("#<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>").val('');
        $("#<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA%>").prop('disabled',true);
      }
      
      //$("#divOrdinanza").hide();
     
      return;
    }
    
    
    function Verify() 
    { 
	    // alert("Verify");
    	if ($('#<%=ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA%>').val() == '-' )
    	{
      		alert('Tipo Pena Accessoria Obbligatorio');
      		$("#<%=ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA%>").focus();
      		return false;
    	}
	
    }
    
    
    function eseguiJQuery(){
      $("#divOrdinanza").toggle();
    }

  </script>
</head>


<body class="corpo" onLoad ="JavaScript:cambiaJQuery();" >
  <table>
    <tr>
      <td class="LBG">
        <a href="Javascript:window.print();">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0>
        </a>
      </td>
      <td class="LBG">
        <font class="label">Funzione :</font>&nbsp;&nbsp;
        <%
        PenaAccessoriaCumuloModel lPenaAccessoriaCumulo = new PenaAccessoriaCumuloModel(); 
        String lAzione = new String();
        if( modalita.equals("I") ) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciPenaAccessoriaCumulo"; 
        %>
        <font class="campo">Inserimento Pena Accessoria Cumulo</font>
        <%
        }
        else if( modalita.equals("M") ) {
          lAzione = "siap.siep.modulocumulo.action.ActInserisciPenaAccessoriaCumulo";
          lPenaAccessoriaCumulo = penaAccessoriaCumulo;
        %>
        <font class="campo">Modifica Pena Accessoria Cumulo</font>
        <%}%>
      </td>
      <td class="LBG">
        <a href="javascript:eseguiFunzione('siap.siep.modulocumulo.action.ActRicercaPeneAccessorieCumulo')">
          <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
        </a>
      </td>
    </tr>
  </table>
  
  <% // INCLUDE DEL DETTAGLIO DEL TITOLO e DELL'ISTRUTTORIA%>
  <br>
  <table align="center" width="95%" style="border: 0;" cellspacing="1" cellpadding="1">
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
      </td>
    </tr>
    <tr>
      <td>
        <jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
      </td>
    </tr>
  </table>

<% 
//=========================================================================== 
// Form per l'inserimento e la modifica delle Pene Accessorie Sul Titolo
//=========================================================================== 
%> 
<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPenaAccessoria" >
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>">
  
  <input type="hidden" name="<%= ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  <input type="hidden" name="<%= ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"        value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  
  <input type="hidden" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_ID_PENA_ACCESSORIA_CUMULO %>" value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo()) %>">
  <input type="hidden" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_FLAG_STATO %>"         value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getFlagStato()) %>">

  <input type="hidden" name="modalita" value="<%=modalita%>">	

  <table width='95%' cellspacing=2 cellpadding=2>
    <tr>
      <td  width='25%' class="l">Tipo di Pena Accessoria</td>
      <td class="l">
        <select class="small" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA %>" onchange="JavaScript:cambiaJQuery();" id="<%=ICostantiPenaAccessoriaCumulo.CAMPO_COD_TIPO_PENA_ACCESSORIA%>" >
          <%=TipoPenaAccessoria%>
        </select>
      </td>
    </tr>

    <tr>
      <td width='25%' class="l"> 
        <font class="l">Descrizione Altre P.A.</font>
      </td>
      <td class="l">
        <input type="text"  Title="Descrizione Altre P.A."  maxlength="100" size="70"
               name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA %>"
               id="<%=ICostantiPenaAccessoriaCumulo.CAMPO_DESCR_ALTRE_PA %>"
               value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getDescrAltrePA()) %>" 
               disabled >
      </td>
    </tr>
    
    <tr>
        <td width='25%' class="l">Tipo Durata</td>
        <td class="l">
          <select name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_DURATA%>">
            <%=DurataPeneAccessorie%>
          </select>
        </td>
    </tr>
    <tr>
        <td class="l">Durata</td>
        <td class="l">
          Anni <input maxlength=2 size=2 Title="Anni Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumAnni()) %>" type="text" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_NUM_ANNI %>" ONKEYPRESS="return TicTabNumField(this,event)" >
          Mesi <input maxlength=2 size=2 Title="Mesi Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumMesi()) %>" type="text" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_NUM_MESI %>" ONKEYPRESS="return TicTabNumField(this,event)" >
          Giorni <input maxlength=2  size=2 Title="Giorni Durata" value="<%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNumGiorni()) %>" type="text" name="<%= ICostantiPenaAccessoriaCumulo.CAMPO_NUM_GIORNI %>" ONKEYPRESS="return TicTabNumField(this,event)" >
        </td>
    </tr>
    <tr> 		    
  		<td class="l">Note</td>
   		<td class="l">
       	<textarea cols="80" rows="3" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_NOTE%>"><%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getNote())%></textarea>
   		</td>
   	</tr>

  </table>

  <%
  //==============================================================================================================
  // Da verificare se da utilizzare in fase di gestione Stato esecuzione (all'interno della successiva DIV)
  //==============================================================================================================
  %>
  <div style="display:none"> 
  </div>
    <br>
    <table width='95%' cellspacing=2 cellpadding=2>
    <%
    //==========================================================================
    // Descrizione dello stato visualizzata solo in fase di modifica del dato
    //==========================================================================
    if (lPenaAccessoriaCumulo!=null && lPenaAccessoriaCumulo.getIdPenaAccessoriaCumulo()!=null)
    {
      	String lDescStato = "";
      	if      ( lPenaAccessoriaCumulo.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      	else if ( lPenaAccessoriaCumulo.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      	else if ( lPenaAccessoriaCumulo.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      	else if ( lPenaAccessoriaCumulo.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    	<tr>
      		<td class="l"> Situazione </td>
      		<td class="l"> <%=lDescStato %></td> 
    	</tr>
 <% } %>
  
	  	<tr> 		    
      		<td class="l">Motivo Inserimento/Modifica </td>
      		<td class="l">
        		<textarea cols="100" rows="6" name="<%=ICostantiPenaAccessoriaCumulo.CAMPO_MOTIVO_MODIFICA%>"
        		><%=StringUtils.toStringJSP(lPenaAccessoriaCumulo.getMotivoModifica() ) %></textarea>
      		</td>
      	</tr>
 <%	if( modalita.equals("I") )
	{	%>     	
      	<tr>
      		<td align="left">
        		<input class="bottone" type="submit" name="conferma" title="Inserisci Pena Accessoria" value="Conferma">
      		</td>
      	</tr>
<%	}
	else if( modalita.equals("M") )
	{	%>
    	<tr>
      		<td align="left">
        		<input class="bottone" type="submit" name="conferma" title="Modifica Pena Accessoria" value="Conferma">
      		</td>
    	</tr> 
<%	} %>    

  </table>

</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciPenaAccessoria");


  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  frmvalidator.setAddnlValidationFunction("Verify"); 
  
  frmvalidator.addValidation("<%=ICostantiPenaAccessoriaCumulo.CAMPO_NUM_ANNI%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoriaCumulo.CAMPO_NUM_MESI%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiPenaAccessoriaCumulo.CAMPO_NUM_GIORNI%>","numeric");

</script>
</body>
</html>
