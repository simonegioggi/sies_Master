<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.F3BException"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Vector"%>

<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>
<%@ page import="siap.sius.depositodecreto.action.ICostantiDepositoDecreto"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.magistrato.model.MagistratoModel"%>
<%@ page import="siap.sius.esperto.action.ICostantiEsperto"%>
<%@ page import="siap.sius.esperto.model.EspertoModel"%>
<%@ page import="siap.sius.motivazionedecreto.action.ICostantiMotivazioneDecreto"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>
<%@ page import="siap.sius.magistratorelatore.action.ICostantiMagistratoRelatore"%>

<%@ page import="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"%>
<%@ page import="siap.sius.avvocato.action.ICostantiAvvocatoFascicoloSius"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoSiusModel"%>
<%@ page import="siap.sius.avvocato.model.AvvocatoModel"%>

<%@ page import="siap.sius.fascicolo.model.FascicoloGPModel"%>

<!--jsp:useBean id="avvocato" scope="request" class="java.util.ArrayList"/-->
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel" />
<jsp:useBean id="modalita"    scope="request" class="java.lang.String"/>
<jsp:useBean id="contenuto"   scope="request" class="java.lang.String"/>
<jsp:useBean id="codContenuto"     scope="request" class="java.lang.String"/>
<jsp:useBean id="codOggetti"  scope="request" class="java.lang.String"/>
<jsp:useBean id="descOggetti" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratorelatore"   scope="request" class="siap.sius.magistratorelatore.model.MagistratoRelatoreModel"/>
<jsp:useBean id="avvocatosius"   scope="request" class="siap.sius.avvocato.model.AvvocatoSiusModel"/>

<jsp:useBean id="codDettagli"   scope="request" class="java.lang.String"/>
<jsp:useBean id="lFascicolo"    scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<html>
<head>
  <title>[S.I.E.S.] - Emissione Decreto Irreperibilità</title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">

  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>

  <script language="JavaScript">

     var desktop;

      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

  // Funzione dei controlli formali della form
  function Verify()
  {
    // Controllo obbligatorietà contenuto.
 /*   var contenuto=document.LoadInserisciDecretoIrreperibilità.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>[document.LoadInserisciDecretoIrreperibilità.<%= ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.selectedIndex].value;
    if(contenuto =="-")
    {
      alert("Il Campo Contenuto è obbligatorio");
      return false;
    }
*/
    <% if (magistratorelatore.getEsperto() == null && magistratorelatore.getMagistrato() == null ) {%>

        alert('Il Magistrato Relatore è obbligatorio');
        return false;
    <% } else if (magistratorelatore.getMagistrato() == null) {%>
        alert('<%=magistratorelatore.getEsperto().getCognome()%> è un esperto, occorre un magistrato');
        return false;
        <% } %>

    // Controllo validità data Emissione.
    var dataEmissione=document.LoadInserisciDecretoIrreperibilità.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value +'/'+
                      document.LoadInserisciDecretoIrreperibilità.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value +'/'+
                      document.LoadInserisciDecretoIrreperibilità.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

    if (! ControllaData(dataEmissione))
    {
      alert('Data emissione non valida!');
      return false;
    }

    return true;
  }
  </script>

  <script language="JavaScript">
    var desktop;

    // Chiamata funzione lista Oggetti
    function ListaOggetti(a_formname,a_field_contenuto, a_fieldname, a_fieldcodes, a_fieldcodesdet, i_fieldcodes, i_fieldcodesdet )
    {
      // Compone il link URL per passare i parametri alla ElencoUdienza.JSP
      var aLink = "<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadListaOggetti";
          aLink += "&formname="+a_formname;
          aLink += "&field_contenuto="+a_field_contenuto;
          aLink += "&fieldname="+a_fieldname;
          aLink += "&fieldcodes="+a_fieldcodes;
          aLink += "&fieldcodesdet="+a_fieldcodesdet;
          aLink += "&ifieldcodes="+i_fieldcodes;
          aLink += "&ifieldcodesdet="+i_fieldcodesdet;
      desktop = window.open(aLink, "Lista_Oggetti","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=yes,width=760,height=500");
    }
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG">
        <font class="label"> Funzione :</font>&nbsp;
<%
        // Imposta l'azione da Chiamare.
        String lAzione = new String();
        lAzione = "siap.sius.depositodecreto.action.ActInserisciDecretoIrreperibilità";
        String lActRet = new String("siap.sius.depositodecreto.action.ActLoadInserisciDecretoIrreperibilità");

%>
        <font class="campo">Emissione Decreto Irreperibilità</font>
      </td>

    </tr>


    <tr>
       <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
    <tr>
    <jsp:include page="<%=ICostantiMagistratoRelatore.PG_SINTESIMAGISTRATORELATORE%>">
          <jsp:param name="MagRelRitorno" value="<%=lActRet%>"/>
    </jsp:include>
    </tr>
  </table>
      <jsp:include page="<%=ICostantiAvvocatoFascicoloSius.PG_INCLUDE_AVVOCATI%>">
      <jsp:param name="AvvRitorno" value="<%=lActRet%>"/>
      </jsp:include>

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciDecretoIrreperibilità">
  <table cellspacing=2 cellpadding=2>
    <!-- Sezione Contenuto Oggetti -->

    <tr>
      <td class="l">Data Emissione<font class="ob">(*)</font></td>
      <td class="L">
        <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)"> /
        <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
      </td>
    </tr>

  <tr>
    <td class="l">Contenuto<font class="ob">(*)</font></td>
    <td class="L"> <%=contenuto%></td>
  </tr>

    <tr>
      <td class="l">Oggetto<font class="ob">(*)</font></td>
        <td class="l">
          <Textarea Title="Oggetto" name="<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>" cols=88 rows=3 readonly><%=descOggetti%></Textarea>
          <a href="Javascript:ListaOggetti('LoadInserisciDecretoIrreperibilità',document.LoadInserisciDecretoIrreperibilità.<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>.value, '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDecretoIrreperibilità.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDecretoIrreperibilità.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/fileselected.gif" title="Oggetti per il Contenuto selezionato" border=0></a>
          &nbsp;
          <a href="Javascript:ListaOggetti('LoadInserisciDecretoIrreperibilità','-', '<%= ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO %>', '<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>', '<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>', document.LoadInserisciDecretoIrreperibilità.<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>.value, document.LoadInserisciDecretoIrreperibilità.<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>.value );">
          <img src="/images/filefolder.gif" title="Elenco di tutti gli Oggetti Selezionabili" border=0></a>
        </td>
    </tr>

    <tr>
      <td class="l">Viste le informative:</td>
      <td class="l">
       <input title="Note " name="<%= ICostantiDepositoDecreto.CAMPO_NOTE%>" value="" size="88" maxlength="88" >
      </td>
    </tr>

<%
    FascicoloGPModel lModel = new FascicoloGPModel();
%>

    <tr>
      <td>
        <input class="bottone" type="submit" value="Conferma" onclick="Javascript:return Verify();">
      </td>
    </tr>

    </table>

    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAzione%>" >
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_OGGETTO%>" value="<%=codOggetti%>">
    <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_DETTAGLIO_OGGETTO%>" value="<%=codDettagli%>">
   <input type="HIDDEN" name="<%=ICostantiFascicoloSius.CAMPO_COD_CONTENUTO%>" value="<%=codContenuto%>">


  </form>

  <script language="JavaScript" type="text/javascript">

    var frmvalidator = new Validator("LoadInserisciDecretoIrreperibilità");
    // Controllo data emissione.
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req", "Il campo Giorno Data Emissione è obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req", "Il campo Mese Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req", "Il campo Anno Data Emissione é obbligatorio");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","minlen=4","La lunghezza del campo Anno deve essere di 4 caratteri");

    // Controllo campo oggetto.
    frmvalidator.addValidation("<%=ICostantiFascicoloSius.CAMPO_DESCR_OGGETTO%>", "req","E' necessario selezionare almeno un oggetto");

  </script>

  </body>
</html>
