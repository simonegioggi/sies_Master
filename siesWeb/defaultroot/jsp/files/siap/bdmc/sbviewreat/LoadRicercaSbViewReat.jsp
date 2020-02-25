<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="siap.web.ISIAPCostantiWeb"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="f3b.util.DateUtils"%>

<%@ page import="siap.bdmc.sbviewreat.model.SbViewReatModel"%>
<%@ page import="siap.bdmc.sbviewreat.action.ICostantiSbViewReat"%>
<jsp:useBean id="sbviewreat" scope="request" class="siap.bdmc.sbviewreat.model.SbViewReatModel"/>

<html>
<head>
  <title> Ricerca SbViewReat </title>
  <link rel="STYLESHEET" type="text/css" href="<%=ISIAPCostantiWeb.PG_STYLE%>">
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_DIR%>/controlli.js"></script>
  <script language="JavaScript" src="<%=ISIAPCostantiWeb.JS_CONFIRM%>"></script>
  <script language="JavaScript" >
    //============================================================================
    // Aggiungere qui eventuali funzioni javascript da richiamare nella finestra 
    //============================================================================
    function Verify() { 
      // Inserire i controlli che non possono essere effettuati dal genvalidator 
      /* Esempio:
      if (document.LoadInserisciSbViewReat.<%="ICostantiSbViewReat.CAMPO_"%>.value=="" ) { 
        alert("Inserire il Codice Fiscale o la Partita IVA!"); 
        document.LoadInserisciSbViewReat.<%="ICostantiSbViewReat.CAMPO_"%>.focus(); 
        return false; 
      } 
      */

    } 
  </script>
</head>

<body class="corpo">
  <table>
    <tr>
      <td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=ISIAPCostantiWeb.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
                      <font class="campo">Ricerca SbViewReat</font>
      </td>
    </tr>
  </table>

<FORM method="POST" action="Main.jsp" name="LoadRicercaSbViewReat">
  <input type="HIDDEN" name="<%=ISIAPCostantiWeb.ACTION_FIELD%>" value="siap.bdmc.sbviewreat.action.ActRicercaSbViewReat">
  <%
    //==================================================
    // Aggiungere qui eventuali altri campi hidden      
    //==================================================
  %>
  <table>
    <tr>
      <td class="l">Nume Prog Capo Impu</td>
      <td class="l"> 
        <input type="text" maxlength="10" size="12" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getNumeProgCapoImpu()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_NUME_PROG_CAPO_IMPU %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Prog Reat</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="17" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getNumeProgReat()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_NUME_PROG_REAT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Font Giur</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getCodiFontGiur()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_CODI_FONT_GIUR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Font Giur</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getAnnoFontGiur()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_ANNO_FONT_GIUR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Font Giur</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getNumeFontGiur()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_NUME_FONT_GIUR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti Font Giur</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getArtiFontGiur()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_ARTI_FONT_GIUR %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Commi Arti Font</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getCommiArtiFont()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_COMMI_ARTI_FONT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Lett Arti Font</td>
      <td class="l"> 
        <input type="text" maxlength="50" size="50" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getLettArtiFont()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_LETT_ARTI_FONT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Arti Font</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getNumeArtiFont()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_NUME_ARTI_FONT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Arti Qual Font</td>
      <td class="l"> 
        <input type="text" maxlength="3" size="3" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getArtiQualFont()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_ARTI_QUAL_FONT %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Id Pren</td>
      <td class="l"> 
        <input type="text" maxlength="0" size="2" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getIdPren()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_ID_PREN %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Anno Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="4" size="6" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getAnnoFascBdmc()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_ANNO_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Nume Fasc Bdmc</td>
      <td class="l"> 
        <input type="text" maxlength="6" size="8" ONKEYPRESS="return TicTabNumField(this,event)" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getNumeFascBdmc()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_NUME_FASC_BDMC %>"  
               > 
      </td> 
    </tr>
    <tr>
      <td class="l">Codi Sede Inst</td>
      <td class="l"> 
        <input type="text" maxlength="15" size="15" 
               value="<%=StringUtils.toStringJSP(sbviewreat.getCodiSedeInst()) %>"
               name="<%= ICostantiSbViewReat.CAMPO_CODI_SEDE_INST %>"  
               > 
      </td> 
    </tr>

    <tr>
      <td align="center">
        <input class="bottone" type="submit" name="conferma" value="Conferma">
      </td>
    </tr>

  </table>
</FORM>
</body>
</html>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("LoadRicercaSbViewReat");

  //================================================================
  // Aggiungere le opportune chiamate al genvalidator 
  //================================================================
  //frmvalidator.addValidation("","req","Il campo XXXX è obbligatorio");
  //frmvalidator.addValidation("","numeric","Il XXXX è un campo numerico");
  //frmvalidator.addValidation("","maxlen=4","La lunghezza massima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","minlen=4","La lunghezza minima per XXXX è di 4 caratteri");
  //frmvalidator.addValidation("","gt=1900");
  //frmvalidator.addValidation("","lt=3000");
  //frmvalidator.addValidation("","alphanumeric");
  //frmvalidator.addValidation("","numeric");
  //frmvalidator.addValidation("","alpha");
  //frmvalidator.addValidation("","alnumhyphen");
  //frmvalidator.addValidation("","email");
  //frmvalidator.addValidation("","regexp");
  //frmvalidator.addValidation("","dontselect");

  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_NUME_PROG_CAPO_IMPU %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_NUME_PROG_REAT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_CODI_FONT_GIUR %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_ANNO_FONT_GIUR %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_NUME_FONT_GIUR %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_ARTI_FONT_GIUR %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_COMMI_ARTI_FONT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_LETT_ARTI_FONT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_NUME_ARTI_FONT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_ARTI_QUAL_FONT %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_ID_PREN %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_ANNO_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_NUME_FASC_BDMC %>",);
  //frmvalidator.addValidation("<%= ICostantiSbViewReat.CAMPO_CODI_SEDE_INST %>",);

  frmvalidator.setAddnlValidationFunction("Verify"); 

</script>