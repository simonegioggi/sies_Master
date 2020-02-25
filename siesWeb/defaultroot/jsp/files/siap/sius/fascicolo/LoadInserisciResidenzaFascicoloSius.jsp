<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.residenza.model.ResidenzaAssociataModel"%>

<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel" %>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="residenzaassociata" scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>

<%
  FascicoloGPModel lFascicoloAssociato = (FascicoloGPModel)session.getAttribute("fascicoloSiusGP");
  SoggettoModel lSoggetto = lFascicoloAssociato.getFascicoloSiusModel().getSoggetto();

  ResidenzaModel lResidenza = residenzaassociata.getResidenza();

  if(lResidenza == null)
    lResidenza = new ResidenzaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Residenza Procedimento </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  var desktop;

  var modificato = false;

  function ListaResidenze(a_formname)
  {
    modificato = true;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActRicercaResidenza&formname="+a_formname+"&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSoggetto.getIdSoggetto()%>&PopUp=Y", "Ricerca_Residenza", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=450, height=500");
  }

  function ListaComuni(a_formname, a_fieldname)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=300, height=500");
  }

  function pulisciIdResidenza()
  {
<%
     if( modalita.equals("I") )
     {
%>
    document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>.value = '0';
<%    } %>
    modificato = true;
    return;
  }

  function Verify()
  {
    // alert ("modificato " + modificato);
     var ritorno =  true;

    if (document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039')
    {
      document.LoadInserisciResidenzaFascicoloSius.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value="";
      if (document.LoadInserisciResidenzaFascicoloSius.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length==0)
      {
        alert('Il Luogo è obbligatorio se lo Stato è Italia');
        return false;
      }
    }

    if ( (document.LoadInserisciResidenzaFascicoloSius.value=='039')
    &&( document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.value!=""))
    {
      document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.value="";
      document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
      alert('Per Italia specificare solo il Luogo, non il Comune Estero.');
      ritorno =  false;
    }
    else if ( (document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_COD_STATO%>.value!='039')
    &&( document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value != ""))
    {
      document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value = "";
      document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>.focus();
      alert('Per Stato Estero specificare solo il Comune Estero non il Luogo.');
      ritorno =  false;
    }
     else if (!modificato )
    {
      alert('La residenza corrente  non è stata modificata!');
      return false;
    }
    if (document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_COD_STATO%>.value!='039')
    {
	  pulisciIdResidenza();
      document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value='';
      cancellaCodComuneReale();
	}

    return ritorno;
  }

  function cancellaCodComuneReale() {
  	document.LoadInserisciResidenzaFascicoloSius.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
  }

  function cancellaCodComRealePulisciID() {
	  pulisciIdResidenza();
	  cancellaCodComuneReale();
  }

</script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
                        <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        String lAction = new String();
        if( modalita.equals("I") )
        {
         lAction = "siap.sius.fascicolo.action.ActInserisciResidenzaFascicoloSius";
%>
           <font class="campo">Inserimento Residenza Fascicolo</font>
<%
        }
        else if( modalita.equals("M") )
        {
          lAction = "siap.sius.fascicolo.action.ActInserisciResidenzaFascicoloSius";
%>
           <font class="campo">Modifica Residenza Fascicolo</font>
<%
       }
%>
      </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
  <br>
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciResidenzaFascicoloSius">
<%
if( modalita.equals("I") )
{
%>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">
        <a href="Javascript:ListaResidenze('LoadInserisciResidenzaFascicoloSius');">
          Lista Residenze Disponibili <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
  </table>
<% } %>
  <table cellspacing=2 cellpadding=2>
                <tr>
      <td class="l">Indirizzo</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getIndirizzo())%>" type="text" name="<%=ICostantiResidenza.CAMPO_INDIRIZZO%>" maxlength="100" size="50" onChange="pulisciIdResidenza()">
      </td>
                </tr>
                <tr>
      <td class="l">Cap</td>
      <td class="l">
        <input value="<%=StringUtils.toStringJSP(lResidenza.getCap())%>" Title="CAP" type="text" name="<%=ICostantiResidenza.CAMPO_CAP%>" maxlength="5" size="5" onChange="pulisciIdResidenza()">
      </td>
                </tr>
                <tr>
      <td class="l">Luogo <font class="ob">(*solo per l'Italia)</font></td>
      <td class="l">
        <input Title="Luogo" name="<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>" value="<%=lResidenza.getDescrComune()%>" type="text" maxlength="35" size="35" onChange="cancellaCodComRealePulisciID()">
        <a href="Javascript:ListaComuni('LoadInserisciResidenzaFascicoloSius','<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>
    <tr>
       <td class="l">Comune Estero</font></td>
       <td class="l">
       <input Title="Comune Estero" name="<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero(), "")%>" type="text" maxlength="200" size="35">
       </td>
    </tr>

    <tr>
      <td class="l">Stato</td>
      <td class="l">
        <select title="Stato" name="<%=ICostantiResidenza.CAMPO_COD_STATO%>" onChange="pulisciIdResidenza()">
          <%=nazioni%>
        </select>
      </td>
                </tr>
    <tr>
      <td colspan="2">
        <br>
        <input type="HIDDEN" name="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" value="<%=StringUtils.toStringJSP(lResidenza.getIdResidenza(), "0")%>">
        <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=lSoggetto.getIdSoggetto()%>">
        <input type="HIDDEN" name="Action" value="<%=lAction%>">
        <input type="HIDDEN" name="modalita" value="<%=modalita%>">
	    <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
        <input class="bottone"  type="submit" value="Conferma">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciResidenzaFascicoloSius");

  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "minlen=5", "La lunghezza minima per il CAP è di 5 caratteri");
  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
