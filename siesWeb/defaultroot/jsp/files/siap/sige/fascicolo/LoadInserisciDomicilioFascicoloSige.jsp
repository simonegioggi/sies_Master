<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.residenza.model.ResidenzaModel"%>
<%@ page import="siap.sico.residenza.action.ICostantiResidenza" %>
<%@ page import="siap.sico.soggetto.action.ICostantiSoggetto" %>
<%@ page import="siap.sico.soggetto.model.SoggettoModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeModel" %>
<%@ page import="siap.sige.fascicolo.model.FascicoloSigeEstesoModel" %>
<%@ page import="siap.sige.fascicolo.action.ICostantiFascicoloSige" %>
<%@ page import="siap.sico.decodifiche.action.ICostantiComune"%>

<jsp:useBean id="domicilioassociato" scope="request" class="siap.sico.residenza.model.ResidenzaAssociataModel"/>

<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="nazioni" scope="request" class="java.lang.String"/>

<%
  FascicoloSigeEstesoModel lFascicoloAssociato = (FascicoloSigeEstesoModel)session.getAttribute("FascicoloSigeEsteso");
  SoggettoModel lSoggetto = lFascicoloAssociato.getSoggetto();

  ResidenzaModel lResidenza = domicilioassociato.getResidenza();

  if(lResidenza == null)
    lResidenza = new ResidenzaModel();
%>

<html>
<head>
<title>[S.I.E.S.] - Gestione Domicilii Procedimento SIGE </title>
<link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

<script language="JavaScript">
  var desktop;
  var modificato = false;

  function ListaDomicili(a_formname)
  {
    modificato = true;
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.residenza.action.ActRicercaDomicilio&formname="+a_formname+"&<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>=<%=lSoggetto.getIdSoggetto()%>&PopUp=Y", "Ricerca_Domicilio", "toolbar=no, location=no, status=no, menubar=no ,scrollbars=yes, resizable=no, width=450, height=500");
  }

  var desktop;
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
    document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>.value = '0';
<%    } %>
    modificato = true;
    return;
  }

  function Verify()
  {
    if (document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039')
    {
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value="";
      if (document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value.length == 0 &&
    	  document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>.checked == false )
      {
        alert('Il Luogo è obbligatorio se lo Stato è Italia');
        return false;
      }
    }

    if ( (document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value=='039')
    &&( document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value!=""))
    {
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.value="";
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE%>.focus();
      alert('Per Italia specificare solo il Luogo, non il Comune Estero.');
      return false;
    }
    if ( (document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
    &&( document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value!=""))
    {
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value="";
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO %>.focus();
      alert('Per Stato Estero specificare solo il Comune Estero non il Luogo.');
      return false;
    }
    if (document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>[document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_COD_STATO%>.selectedIndex].value!='039')
    {
	  pulisciIdResidenza();
      document.LoadInserisciDomicilioFascicoloSige.<%= ICostantiResidenza.CAMPO_DESCR_COMUNE %>.value="";
      cancellaCodComuneReale();
    }
    else if (document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>.value=="" &&
    		 document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiResidenza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE%>.checked == false)
    {
      alert('Solo per Italia il campo Luogo è obbligatorio!');
      return false;
    }
     else if (!modificato )
    {
      alert('Il domicilio corrente  non è stata modificato!');
      return false;
    }

    return true;
  }

  function cancellaCodComuneReale() {
  	document.LoadInserisciDomicilioFascicoloSige.<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>.value = "";      	
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
		lAction = "siap.sige.fascicolo.action.ActInserisciDomicilioFascicoloSige";
        if( modalita.equals("I") )
        {%>
           <font class="campo">Inserimento Domicilio per Procedimento SIGE</font>
	  <%}
        else if( modalita.equals("M") )
        {%>
           <font class="campo">Modifica Domicilio per Procedimento SIGE </font>
	  <%}%>
      </td>
    <jsp:include page="<%=IWebConstants.PG_RETURN_BUTTON%>"/>
    </tr>
  </table>
  <br>
      <jsp:include page="<%=ICostantiFascicoloSige.PG_LOAD_SINTESIPROCEDIMENTOSIGE%>"/>
  <br>
<form method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciDomicilioFascicoloSige">
 <%
if( modalita.equals("I") )
{
%>
 <table cellspacing=2 cellpadding=2>
    <tr>
      <td class="l">
        <a href="Javascript:ListaDomicili('LoadInserisciDomicilioFascicoloSige');">
          Lista Domicili Disponibili <img src="/images/filefolder.gif" border=0>
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
        <input Title="Luogo" name="<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescrComune())%>" type="text" maxlength="35" size="35" onChange="cancellaCodComRealePulisciID()" >
        <a href="Javascript:ListaComuni('LoadInserisciDomicilioFascicoloSige','<%=ICostantiResidenza.CAMPO_DESCR_COMUNE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
    </tr>

    <tr>
       <td class="l">Comune Estero</font></td>
       <td class="l">
       <input Title="Comune Estero" name="<%=ICostantiResidenza.CAMPO_DESC_COMUNE_ESTERO%>" value="<%=StringUtils.toStringJSP(lResidenza.getDescComuneEstero())%>" type="text" maxlength="200" size="35">
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

<%
		String checkDPD = "";
		//if ( anagraficaParteUdienza != null && anagraficaParteUdienza.getResidenza() != null 
		//	 && anagraficaParteUdienza.getResidenza().getFlgDomAvv() != null
		//	 && anagraficaParteUdienza.getResidenza().getFlgDomAvv().equals("S"))
		if(lResidenza != null && lResidenza.getFlgDomicilioDifensore() != null 
		   && lResidenza.getFlgDomicilioDifensore().equals("S"))
		{
			checkDPD = "checked";
		}
%>
    <tr>
      <td class="l">Domiciliato presso il difensore (ex art. 161 cpp)</td>
      <td class="l">
        	<input type=checkbox <%=checkDPD%> name="<%=ICostantiResidenza.CAMPO_FLAG_DOMICILIO_PRESSO_DIFENSORE %>" value="S" onChange="pulisciIdResidenza()">
      </td>
    </tr>

    <tr>
      <td colspan="2">
        <br>
        <input type="HIDDEN" name="<%=ICostantiResidenza.CAMPO_ID_RESIDENZA%>" value="<%=StringUtils.toStringJSP(lResidenza.getIdResidenza(), "0")%>">
        <input type="HIDDEN" name="<%=ICostantiSoggetto.CAMPO_ID_SOGGETTO%>" value="<%=StringUtils.toStringJSP(lSoggetto.getIdSoggetto())%>">
	    <input type="HIDDEN" name="<%=ICostantiComune.CAMPO_COD_COMUNE_REALE%>" value="">
        <input type="HIDDEN" name="Action" value="<%=lAction%>">
        <input type="HIDDEN" name="modalita" value="<%=modalita%>">
        <input class="bottone"  type="submit" value="Conferma">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadInserisciDomicilioFascicoloSige");

  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "numeric");
  frmvalidator.addValidation("<%=ICostantiResidenza.CAMPO_CAP%>", "minlen=5", "La lunghezza minima per il CAP è di 5 caratteri");

  frmvalidator.setAddnlValidationFunction("Verify");
</script>
</body>
</html>
