<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.istruttoriacumulo.action.ICostantiIstruttoriaCumulo"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiTitoloCumulato"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiModuloCumulo"%>
<%@ page import="siap.siep.modulocumulo.model.ReatoCumuloModel"%>
<%@ page import="siap.siep.modulocumulo.action.ICostantiReatoCumulo"%>

<jsp:useBean id="IstruttoriaCumulo"     scope="request" class="siap.siep.istruttoriacumulo.model.IstruttoriaCumuloModel"/>
<jsp:useBean id="TitoloInCumulo"        scope="request" class="siap.siep.modulocumulo.model.TitoloCumulatoModel"/>
<jsp:useBean id="reatoCum" 			scope="request" class="siap.siep.modulocumulo.model.ReatoCumuloModel"/>
<jsp:useBean id="TipiPeneDetentive" scope="request" class="java.lang.String"/>
<jsp:useBean id="Valute" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoSanzione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 			scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Cumulo - Pena Reato</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  function Verify()
  {
      
      if(document.InsPenaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA%>.value=="-")
      {
      		alert ("Specificare Il tipo di Pena Detentiva");
      		document.InsPenaReatoCumulo.<%=ICostantiReatoCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA%>.focus();
      		return false;
      }	

  }
</script>
</head>
  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
      <font class="campo">Inserimento/Modifica Pena Reato</font>

<%      ReatoCumuloModel lReato = new ReatoCumuloModel();
        if( modalita.equals("M") )
        {
          	lReato = reatoCum;
        }
%>
      </td>
    </tr>
  </table>

	<br>
		<jsp:include page="/jsp/files/siap/siep/istruttoriacumulo/DettaglioIstruttoriaCumulo.jsp"/>
    	<jsp:include page="/jsp/files/siap/siep/modulocumulo/DettaglioTitoloCumulato.jsp"/>
	<br>

  <table cellspacing="2" cellpadding="2" width="95%">
  <tr>
      <td class="l">
        <font class="campo">
<%
          String lProgressivo = "";
          if(lReato.getProgrCircostanza().intValue() == 1)
            lProgressivo = (lReato.getProgrNumeroManuale() != null) ? lReato.getProgrNumeroManuale().toString() : lReato.getProgrReato().toString();

          if(!lProgressivo.equals(""))
          {
%>
            <font class="campoNoCap">
<%
              out.print("Reato N."+lProgressivo+": ");
%>
            </font>
<%
          }
          boolean lFlagAnnoNumero = false;
          if( lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals("")
              && lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
              out.println(lReato.getDescrFonte()+" ");
            if(lReato.getAnnoFonte() != null && !lReato.getAnnoFonte().equals(""))
              out.println(lReato.getAnnoFonte());
            if(lReato.getNumeroFonte() != null && !lReato.getNumeroFonte().equals(""))
              out.println("/"+lReato.getNumeroFonte());
          }

          if(lReato.getArticolo() != null && !lReato.getArticolo().equals(""))
            out.println("art."+lReato.getArticolo());
          if(lReato.getDescrSottonumerazione() != null && !lReato.getDescrSottonumerazione().equals("") && !lReato.getDescrSottonumerazione().equals("-"))
            out.println(" "+lReato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(lReato.getDescrFonte() != null && !lReato.getDescrFonte().equals("") && !lReato.getDescrFonte().equals("-"))
              out.println(lReato.getDescrFonte());
          }

          if(lReato.getComma() != null && !lReato.getComma().equals(""))
            out.println(" c. "+lReato.getComma());
          //**************************************************************************************************
          //Federica - a9-rr-078
          //aggiunto campo Comma-Qualificante 
          if(lReato.getDescrCommaQualificante() != null && !lReato.getDescrCommaQualificante().equals("") && !lReato.getDescrCommaQualificante().equals("-"))
             out.println(" "+lReato.getDescrCommaQualificante());
          //**************************************************************************************************
         
          if(lReato.getLettera() != null && !lReato.getLettera().equals(""))
            out.println(" l. "+lReato.getLettera());
          if(lReato.getNumero() != null && !lReato.getNumero().equals(""))
            out.println(" n. "+lReato.getNumero());
%>
        </font>
      </td>
    </tr>
    <tr><td></td></tr>
  </table>
  
  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="InsPenaReatoCumulo">
	<input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.modulocumulo.action.ActInserisciPenaReatoCumulo">
	
	<input type="hidden" name="<%=ICostantiIstruttoriaCumulo.CAMPO_ID_ISTRUTTORIA_CUMULO%>" value="<%=IstruttoriaCumulo.getIdIstruttoriaCumulo()%>">
  	<input type="hidden" name="<%=ICostantiTitoloCumulato.CAMPO_ID_TITOLO_CUMULATO%>"       value="<%=TitoloInCumulo.getIdTitoloCumulato()%>">
  	<input type="hidden" name="<%=ICostantiReatoCumulo.CAMPO_FLAG_STATO %>"          	value="<%=StringUtils.toStringJSP(lReato.getFlagStato()) %>">
  	 
	<input type="HIDDEN" name="<%=ICostantiReatoCumulo.CAMPO_PROGR_REATO%>" value="<%=lReato.getProgrReato().toString()%>" >
  	<input type="HIDDEN" name="<%=ICostantiReatoCumulo.CAMPO_PROGR_CIRCOSTANZA%>" value="<%=lReato.getProgrCircostanza().toString()%>" >
  	<input type="HIDDEN" name="<%=ICostantiReatoCumulo.CAMPO_ID_REATO_CUM%>" value="<%=lReato.getIdReatoCum()%>">
	
  <table cellspacing="2" cellpadding="2">
	<tr>
      <td class="l">Tipo Pena Detentiva</td>
      <td class="l">
      <select name="<%= ICostantiReatoCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA %>">
        <%=TipiPeneDetentive%>
      </select>
      </td>
		</tr>
		<tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni&nbsp;<input Title="Anni" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumAnni()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUM_ANNI %>">
        Mesi&nbsp;<input Title="Mesi" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumMesi()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUM_MESI %>">
        Giorni&nbsp;<input Title="Giorni" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumGiorni()) %>" type="text" name="<%= ICostantiReatoCumulo.CAMPO_NUM_GIORNI %>">
      </td>
		</tr>

   <tr>
      <td class="l">Durata Isolamento Diurno</td>
      <td class="L" >Anni
        <input Title="Anni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumAnniIsolamentoDiurno()) %>" name="<%= ICostantiReatoCumulo.CAMPO_ANNI_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        Mesi
        <input Title="Mesi Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumMesiIsolamentoDiurno()) %>" name="<%= ICostantiReatoCumulo.CAMPO_MESI_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        Giorni
        <input Title="Giorni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumGiorniIsolamentoDiurno()) %>" name="<%= ICostantiReatoCumulo.CAMPO_GIORNI_ISOLAMENTO_DIURNO %>"maxlength="2" size="2">
      </td>
    </tr>
		<tr>
      <td class="l">Tipo Sanzione</td>
      <td class="l">
      <select name="<%= ICostantiReatoCumulo.CAMPO_COD_TIPO_SANZIONE%>">
        <%=TipoSanzione%>
      </select>
      </td>
		</tr>
		<tr>
      <td class="l">Sanzione Pecuniaria</td>
      <td class="l">
        <input type="text" Title="Sanzione Pecuniaria" size="7" maxlength="7" value="<%=StringUtils.getParteIntera(lReato.getSanzionePecuniaria() ) %>" name="<%=ICostantiReatoCumulo.CAMPO_SANZIONE_PECUNIARIA_INT%>">
        ,
        <input type="text" Title="Sanzione Pecuniaria" size="2" maxlength="2" value="<%=StringUtils.getParteDecimale(lReato.getSanzionePecuniaria() ) %>" name="<%=ICostantiReatoCumulo.CAMPO_SANZIONE_PECUNIARIA_DEC%>">
        &nbsp;
        <select name="Valuta">
          <%=Valute%>
        </select>
      </td>
	</tr>
</table>	

<table cellspacing="2" cellpadding="2" width="90%">
    <%
    //==========================================================================
    // Descrizione dello stato del dato
    //==========================================================================
      String lDescStato = "";
      if      ( lReato.getFlagStato().equals("E")){lDescStato = "Dato Estratto dal fascicolo originale";}
      else if ( lReato.getFlagStato().equals("I")){lDescStato = "Dato Inserito manualmente dopo l'estrazione";}
      else if ( lReato.getFlagStato().equals("M")){lDescStato = "Dato estratto modificato";}
      else if ( lReato.getFlagStato().equals("C")){lDescStato = "Dato estratto cancellato";}
    %>
    <tr>
      <td class="l"><center>Situazione</center></td>
      <td class="l"> <%=lDescStato %></td> 
    </tr>
  
    <%
    //=================================================================================== 
    // Campo Motivazioni, visualizzato sia in inserimento sia in modifica dove l'utente
    // può motivare l'intervento sui dati su cui sta intervenendo
    //=================================================================================== 
    %>
    <tr>
      <td class="l">Motivo Inserimento/Modifica</td>
      <td class="l">
        <textarea cols="80" rows="4" name="<%=ICostantiReatoCumulo.CAMPO_MOTIVO_MODIFICA%>"><%=StringUtils.toStringJSP(lReato.getMotivoModificaNote()) %></textarea> 
      </td>
    </tr>
	<br>
    <tr>
<%	if(lReato.isPenaReatoInserita())
	{ %>    
      <td colspan="2">
        <input type="submit" class="bottone" name="Modifica" title="Modifica Pena Reato" value="Conferma" >
      </td>
<%	}
	else
	{	%>
	  <td colspan="2">
        <input type="submit" class="bottone" name="Inserisci" title="Inserisci Pena Reato" value="Conferma" >
      </td>	
<%	} %>	      
	</tr>
</table>
  
</form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("InsPenaReatoCumulo");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_COD_TIPO_PENA_DETENTIVA%>","req");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_NUM_ANNI%>","numeric","Il campo Anni Durata può contenere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_NUM_MESI%>","numeric","Il campo Mesi Durata può contenere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_NUM_GIORNI%>","numeric","Il campo Giorni Durata può contenere solo caratteri numerici");
    
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_ANNI_ISOLAMENTO_DIURNO%>","numeric","Il campo Anni isolamento può contenere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_MESI_ISOLAMENTO_DIURNO%>","numeric","Il campo Mesi isolamento può contenere solo caratteri numerici");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_GIORNI_ISOLAMENTO_DIURNO%>","numeric","Il campo Giorni isolamento può contenere solo caratteri numerici");

    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_SANZIONE_PECUNIARIA_INT%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReatoCumulo.CAMPO_SANZIONE_PECUNIARIA_DEC%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
    
  </script>
</body>
</html>