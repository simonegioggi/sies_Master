<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.reato.model.ReatoModel"%>
<%@ page import="siap.siep.reato.action.ICostantiReato"%>

<jsp:useBean id="reato" 			scope="request" class="siap.siep.reato.model.ReatoModel"/>
<jsp:useBean id="TipiPeneDetentive" scope="request" class="java.lang.String"/>
<jsp:useBean id="Valute" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="TipoSanzione" 		scope="request" class="java.lang.String"/>
<jsp:useBean id="modalita" 			scope="request" class="java.lang.String"/>
<jsp:useBean id="lTipoFunzione"     scope="request" class="java.lang.String"/>
<jsp:useBean id="modo"       		scope="request" class="java.lang.String"/>
<jsp:useBean id="TornaQui"     		scope="request" class="java.lang.String"/>

<html>
<head>
  <title>[S.I.E.S.] - Gestione Pena Reato</title>

  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">

<%
    String lAction = "siap.siep.reato.action.ActInserisciPenaReato";
	// Gestione Reato SIGE
	boolean modoSIGE = false;
	if (modo != null && modo.equalsIgnoreCase("SIGE"))
	{
		modoSIGE = true;
		lAction = "siap.sige.reato.action.ActInserisciPenaReatoSige";
	}

  if(!lTipoFunzione.equals(""))
  {
%>
    function  Aggravanti()
    {
       document.location.href="<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.circostanza.action.ActLoadInserisciCircostanza&lTipoFunzione=<%=lTipoFunzione%>";
       document.LoadInserisciPenaReato.AggAtt.disabled=true;
       document.LoadInserisciPenaReato.Inserisci.disabled=true;
    }
<%
  }
%>
   function  Inserisci()
   {
      document.LoadInserisciPenaReato.<%=IWebConstants.ACTION_FIELD%>.value = "<%=lAction%>";

<%
      if(!lTipoFunzione.equals(""))
      {
%>
        document.LoadInserisciPenaReato.AggAtt.disabled=true;
<%
      }
%>
      document.LoadInserisciPenaReato.Inserisci.disabled=true;
    }

</script>

  <script language="JavaScript">
    function Verify()
    {
      // Controllo valorizzazione di almeno uno dei campi
      if(   document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_COD_TIPO_PENA_DETENTIVA%>.value=="-"
         && document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_NUM_ANNI%>.value.length==0
         && document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_NUM_MESI%>.value.length==0
         && document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_NUM_GIORNI%>.value.length==0
         && document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_COD_TIPO_SANZIONE%>.value=="-"
         && document.LoadInserisciPenaReato.SP_int.value.length==0
         && document.LoadInserisciPenaReato.SP_dec.value.length==0)
      {
        alert('Almeno un campo deve essere valorizzato');
        document.LoadInserisciPenaReato.<%=ICostantiReato.CAMPO_COD_TIPO_PENA_DETENTIVA%>.focus();

        return false;
      }
      Inserisci();
    }
  </script>
</head>
  <body class="corpo">
    <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border="0"></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        ReatoModel lReato = new ReatoModel();
       // String lAzione = new String();
//        if( modalita.equals("I") )
//        {
//          lAzione = "siap.siep.reato.action.ActInserisciPenaReato";

//        }
//        else
        if( modalita.equals("M") )
        {
       //   lAzione = "siap.siep.reato.action.ActInserisciPenaReato";
          lReato = reato;
%>
           <font class="campo">Inserimento/Modifica Pena Reato</font>
<%
        }
%>
      </td>
    </tr>
  </table>
 <%if (!modoSIGE) { %>
		<br>
			<jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
		<br>
<%} else {%>
		<br>
			<jsp:include page="/jsp/files/siap/sige/fascicolo/SintesiProcedimentoSige.jsp"/>
			<jsp:include page="/jsp/files/siap/sige/sentenza/IncSentenza.jsp"/>
		<br>
<%}%>

  <table cellspacing="2" cellpadding="2" width="95%">
  <tr>
      <td class="l">
        <font class="campo">
<%
          String lProgressivo = "";
          if(reato.getProgrCircostanza().intValue() == 1)
            lProgressivo = (reato.getProgrNumeroManuale() != null) ? reato.getProgrNumeroManuale().toString() : reato.getProgrReato().toString();

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
          if( reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals("")
              && reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals("") )
          {
            lFlagAnnoNumero = true;
          }

          if(lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte()+" ");
            if(reato.getAnnoFonte() != null && !reato.getAnnoFonte().equals(""))
              out.println(reato.getAnnoFonte());
            if(reato.getNumeroFonte() != null && !reato.getNumeroFonte().equals(""))
              out.println("/"+reato.getNumeroFonte());
          }

          if(reato.getArticolo() != null && !reato.getArticolo().equals(""))
            out.println("art."+reato.getArticolo());
          if(reato.getDescrSottonumerazione() != null && !reato.getDescrSottonumerazione().equals("") && !reato.getDescrSottonumerazione().equals("-"))
            out.println(" "+reato.getDescrSottonumerazione());

          if(!lFlagAnnoNumero)
          {
            if(reato.getDescrFonte() != null && !reato.getDescrFonte().equals("") && !reato.getDescrFonte().equals("-"))
              out.println(reato.getDescrFonte());
          }

          if(reato.getComma() != null && !reato.getComma().equals(""))
            out.println(" c. "+reato.getComma());
          //**************************************************************************************************
          //Federica - a9-rr-078
          //aggiunto campo Comma-Qualificante 
          if(reato.getDescrCommaQualificante() != null && !reato.getDescrCommaQualificante().equals("") && !reato.getDescrCommaQualificante().equals("-"))
             out.println(" "+reato.getDescrCommaQualificante());
          //**************************************************************************************************
         
          if(reato.getLettera() != null && !reato.getLettera().equals(""))
            out.println(" l. "+reato.getLettera());
          if(reato.getNumero() != null && !reato.getNumero().equals(""))
            out.println(" n. "+reato.getNumero());
%>
        </font>
      </td>
    </tr>
  </table>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciPenaReato">
		<table cellspacing="2" cellpadding="2">
		<tr>
      <td class="l">Tipo Pena Detentiva</td>
      <td class="l">
      <select name="<%= ICostantiReato.CAMPO_COD_TIPO_PENA_DETENTIVA %>">
        <%=TipiPeneDetentive%>
      </select>
      </td>
		</tr>
		<tr>
      <td class="l">Durata</td>
      <td class="l">
        Anni&nbsp;<input Title="Anni" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumAnni()) %>" type="text" name="<%= ICostantiReato.CAMPO_NUM_ANNI %>">
        Mesi&nbsp;<input Title="Mesi" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumMesi()) %>" type="text" name="<%= ICostantiReato.CAMPO_NUM_MESI %>">
        Giorni&nbsp;<input Title="Giorni" size="2" maxlength="2" value="<%=StringUtils.toStringJSP(lReato.getNumGiorni()) %>" type="text" name="<%= ICostantiReato.CAMPO_NUM_GIORNI %>">
      </td>
		</tr>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
    <tr>
      <td class="l">Data Inizio Isolamento Diurno</td>
      <td class="L" >
        <input Title="Giorno Inizio Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataInizioIsolamentoDiurno(),"dd")) %>" name="<%= ICostantiReato.CAMPO_GIORNO_DATA_INIZIO_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        -
        <input Title="Mese Inizio Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataInizioIsolamentoDiurno(),"MM")) %>" name="<%= ICostantiReato.CAMPO_MESE_DATA_INIZIO_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        -
        <input Title="Anno Inizio Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataInizioIsolamentoDiurno(),"yyyy")) %>" name="<%= ICostantiReato.CAMPO_ANNO_DATA_INIZIO_ISOLAMENTO_DIURNO %>"maxlength="4" size="4">
      </td>
    </tr>
    <tr>
      <td class="l">Data Fine Isolamento Diurno</td>
      <td class="L" >
        <input Title="Giorno Fine Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataFineIsolamentoDiurno(),"dd")) %>" name="<%= ICostantiReato.CAMPO_GIORNO_DATA_FINE_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        -
        <input Title="Mese Fine Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataFineIsolamentoDiurno(),"MM")) %>" name="<%= ICostantiReato.CAMPO_MESE_DATA_FINE_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        -
        <input Title="Anno Fine Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP( DateUtils.getDateToString(lReato.getDataFineIsolamentoDiurno(),"yyyy")) %>" name="<%= ICostantiReato.CAMPO_ANNO_DATA_FINE_ISOLAMENTO_DIURNO %>"maxlength="4" size="4">
      </td>
    </tr>
--%>
   <tr>
      <td class="l">Durata Isolamento Diurno</td>
      <td class="L" >Anni
        <input Title="Anni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumAnniIsolamentoDiurno()) %>" name="<%= ICostantiReato.CAMPO_ANNI_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        Mesi
        <input Title="Mesi Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumMesiIsolamentoDiurno()) %>" name="<%= ICostantiReato.CAMPO_MESI_ISOLAMENTO_DIURNO %>" maxlength="2" size="2">
        Giorni
        <input Title="Giorni Isolamento Diurno" type="text" value="<%=StringUtils.toStringJSP(lReato.getNumGiorniIsolamentoDiurno()) %>" name="<%= ICostantiReato.CAMPO_GIORNI_ISOLAMENTO_DIURNO %>"maxlength="2" size="2">
      </td>
    </tr>
		<tr>
      <td class="l">Tipo Sanzione</td>
      <td class="l">
      <select name="<%= ICostantiReato.CAMPO_COD_TIPO_SANZIONE%>">
        <%=TipoSanzione%>
      </select>
      </td>
		</tr>
		<tr>
      <td class="l">Sanzione Pecuniaria</td>
      <td class="l">
<%
        String Sp_int=new String("");
        String Sp_dec=new String("");

        if (StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).trim()!="")
        {
          if (StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).indexOf(".")!=-1)
          {
            Sp_int=StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).substring(0,StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).indexOf("."));
            Sp_dec=StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).substring(StringUtils.toStringJSP(lReato.getSanzionePecuniaria()).indexOf(".")+1);
          }
          else
          {
            Sp_int=StringUtils.toStringJSP(lReato.getSanzionePecuniaria());
            Sp_dec="00";
          }
        }
        else
        {
          Sp_int="";
          Sp_dec="";
        }
%>
        <input Title="Sanzione Pecuniaria" size="7" maxlength="7" value="<%= Sp_int %>" type="text" name="SP_int">
        ,
        <input Title="Sanzione Pecuniaria" size="2" maxlength="2" value="<%= Sp_dec %>" type="text" name="SP_dec">
        &nbsp;
        <select name="Valuta">
          <%=Valute%>
        </select>
      </td>
		</tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Conferma" class="bottone"   name="Inserisci">
      </td>

<%if(!lTipoFunzione.equals(""))
{%>
      <td colspan="2">
        <input type="button"  class="bottone"  name="AggAtt" value="Prosegui" onClick="javascript:return Aggravanti();">
      </td>
<%}%>

		</tr>
  </table>
  <input type="HIDDEN" value="<%=lReato.getProgrReato().toString()%>" name="<%=ICostantiReato.CAMPO_PROGR_REATO%>">
  <input type="HIDDEN" value="<%=lReato.getProgrCircostanza().toString()%>" name="<%=ICostantiReato.CAMPO_PROGR_CIRCOSTANZA%>">
  <input type="HIDDEN" value="<%=((lReato.getFasSieIdFascicoloSiep() != null) ) ?   lReato.getFasSieIdFascicoloSiep().toString() : "" %>" name="<%=ICostantiReato.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>">
	<input type="HIDDEN" name="<%=ICostantiReato.CAMPO_ID_REATO%>" value="<%=lReato.getIdReato()%>">
	<input type="HIDDEN" name="Action" value="">
  <input type="HIDDEN" name="lTipoFunzione" value="<%=lTipoFunzione%>">
   <input type="HIDDEN" name="<%=IWebConstants.LINK_RITORNO%>" value="<%=TornaQui%>">
  
</form>
  <script language="JavaScript" type="text/javascript">

    var frmvalidator  = new Validator("LoadInserisciPenaReato");

    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_COD_TIPO_PENA_DETENTIVA%>","req");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_NUM_ANNI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_NUM_MESI%>","numeric");
    frmvalidator.addValidation("<%=ICostantiReato.CAMPO_NUM_GIORNI%>","numeric");
    frmvalidator.addValidation("SP_int","numeric");
    frmvalidator.addValidation("SP_dec","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");
    
  </script>
</body>
</html>