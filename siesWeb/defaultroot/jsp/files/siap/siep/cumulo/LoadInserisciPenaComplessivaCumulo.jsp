<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="siap.siep.penacumulo.action.ICostantiPenaCumulo"%>



<jsp:useBean id="FlagErgastolo"        scope="request" class="java.lang.String"/>
<jsp:useBean id="MagistratoCompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel" />
<jsp:useBean id="PosizioneGiuridica"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaModel" />
<!--
  < jsp:useBean id="LibAnt" scope="request" class="siap.sico.libertaanticipata.model.LicenzaLibAnticipataModel" />
-->
<jsp:useBean id="cumulo"     scope="request" class="siap.siep.cumulo.model.CumuloModel" />
<jsp:useBean id="penacumulo" scope="request" class="siap.siep.penacumulo.model.PenaCumuloModel" />

<head>
  <title> [S.I.E.S.] - Cumulo - </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>

  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }


    function Verify()
    {

      if (document.f.Dprov.value.length==1)
          document.f.Dprov.value="0"+document.f.Dprov.value;
      if (document.f.Mprov.value.length==1)
          document.f.Mprov.value="0"+document.f.Mprov.value;
      var data_to_verify=document.f.Dprov.value+'/'+document.f.Mprov.value+'/'+document.f.Yprov.value;
        if (!ControllaData(data_to_verify))
        {
          alert('Data Provvedimento di Cumulo non valida');
          document.f.Dprov.focus();
          return false;
        }


      if (document.f.<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value.length==1)
         document.f.<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value="0"+document.f.<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value;
      if (document.f.<%=ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value.length==1)
         document.f.<%=ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value="0"+document.f.<%=ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value;
      var data_to_verify=document.f.<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.value+'/'+document.f.<%=ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA%>.value+'/'+document.f.<%=ICostantiPenaCumulo.CAMPO_ANNO_DATA_DECORRENZA_PENA%>.value;
        if (!ControllaDataPassaVuota(data_to_verify))
        {
          alert('Data Decorrenza Pena non valida');
          document.f.<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%>.focus();
          return false;
        }

        var hasArrestoOrReclusione=(document.f.RecAnni.value.length>0 || document.f.RecMesi.value.length>0 || document.f.RecGiorni.value.length>0 || document.f.ArrAnni.value.length>0 || document.f.ArrMesi.value.length>0 || document.f.ArrGiorni.value.length>0);
        hasArrestoOrReclusione=hasArrestoOrReclusione || document.f.MultaInt.value.length>0 || document.f.MultaDec.value.length>0 || document.f.AmmendaDec.value.length>0 || document.f.AmmendaInt.value.length>0

        var hasArrestoAndReclusione= (document.f.RecAnni.value==0 && document.f.RecMesi.value==0 && document.f.RecGiorni.value==0 && document.f.ArrAnni.value==0 && document.f.ArrMesi.value==0 && document.f.ArrGiorni.value==0);
        hasArrestoAndReclusione=hasArrestoAndReclusione && document.f.MultaInt.value==0 && document.f.MultaDec.value==0 && document.f.AmmendaDec.value==0 && document.f.AmmendaInt.value==0

       if(!hasArrestoAndReclusione)
       {
        if (!(document.f.FlagErgastolo.value=="-") && hasArrestoOrReclusione)
        {
            alert ("Attenzione : Sono presenti dati esclusivi tra loro (Ergastolo e Arresto/Reclusione)");
            return false;
        }
       }

    }

    function MisureSicurezza(a_formname,a_fieldname)
    {
       var desktopms;
       var valorecampo = document.f.<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>.value;
       desktopms = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldvalue="+valorecampo, "MisureSicurezza","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=170");
    }
    
    function PeneAccessorie(a_formname,a_fieldname)
    {
       var desktoppa;
       var valorecampo = document.f.<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>.value;
       desktoppa = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.siep.cumulo.action.ActLoadCumuloCampoMisuraSicurezzaPenaAccessoria&formname="+a_formname+"&fieldname="+a_fieldname+"&fieldvalue="+valorecampo, "PeneAccessorie","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=600,height=170");
    }

    </script>
</head>

<body class="corpo">


  <form action="<%=IWebConstants.PG_MAIN%>" method="post" name=f>
    <input type="hidden" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.cumulo.action.ActInserisciPenaComplessivaCumulo">

  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;<font class="campo">Annotazione dati finali</font>
      </td>
    </tr>
  </table>
 <br>
 <table>
 <tr><td class=Titolo>Fascicolo Cumulante</td></tr>
 <tr><td align=center><br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br></td></tr>
<tr>
  <td class=l>Posizione Giuridica : <font class="campo"><%=PosizioneGiuridica.getDescrPosizioneGiuridica()%></font></td>
 </tr>
</table>
<br>

<table>
  <tr><td colspan=3 class=Titolo>Pena Residua</td></tr>
  <tr>
    <td class=l>
        <font class="label">Reclusione</font>
    </td>
    <td class=l>
        Anni  <input type=text size=2 maxlength="2"   name=RecAnni value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        Mesi  <input type=text size=2 maxlength="2"   name=RecMesi value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
        Giorni  <input type=text size=2 maxlength="2" name=RecGiorni value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
   </td>
   <td class=r>
<%
String MultaInt="0";
String MultaDec="0";

/*if (MultaInt.indexOf(".")>0)
{
   MultaInt=MultaInt.substring(0,MultaInt.indexOf("."));
   MultaDec=(""+penacumulo.getImportoMulta()).substring((""+penacumulo.getImportoMulta()).indexOf(".")+1);
}*/
%>

      Multa  <input type=text size="7" maxlength="7" name=MultaInt style="text-align:right" value="<%=MultaInt%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" ><strong>,</strong><input type=text size=2 maxlength="2" style="text-align:right"  name=MultaDec value="<%=MultaDec%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>
<tr>
  <td class=l>
      <font class="label">Arresto</font>
  </td>
  <td class=l>
      Anni   <input type=text size=2 maxlength="2" name=ArrAnni   value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Mesi   <input type=text size=2 maxlength="2" name=ArrMesi   value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Giorni <input type=text size=2 maxlength="2" name=ArrGiorni value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
<%
String AmmendaInt="0";
String AmmendaDec="0";
/*if (AmmendaInt.indexOf(".")>0)
{
   AmmendaInt=AmmendaInt.substring(0,AmmendaInt.indexOf("."));
   AmmendaDec=(""+penacumulo.getImportoAmmenda()).substring((""+penacumulo.getImportoAmmenda()).indexOf(".")+1);
}*/
%>
  <td class=r>
      Ammenda  <input type=text size="7" maxlength="7" style="text-align:right" name=AmmendaInt value="<%=AmmendaInt%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" ><strong>,</strong><input type=text size="2" maxlength="2"   style="text-align:right" name=AmmendaDec  value="<%=AmmendaDec%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>

<!--  // 20/05/2014 - Nuova L.A. - decreto 2013/146 - Liberazione Anticipata diventa: -->
<tr><td colspan=3 class="Titolo">Liberazione Anticipata Concessa da Detrarre dal Cumulo</td></tr>
<tr>
  <td class=l height="30">
      <font class="label">Totale Liberazione Anticipata</font>
  </td>
  <td class=l>
<%-- MEV NUOVA INFRASTRUTTURA: refactoring --%>
<%--
   Giorni <input type=text size=3 name="LibAntGiorni" value="<%=StringUtils.toStringJSP(LibAnt.getNumeroGiorni())%>">
   Giorni <input type=text size=3 name="LibAntGiorni" value="0">
--%> 
    Giorni  <input type=text size="4" maxlength="4" name="<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA %>" value="0" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>

<tr>
  <td class=l>
      <font class="label">Totale Liberazione Anticipata Speciale</font>
  </td>
  <td class=l>
    Giorni  <input type="text" size="4" maxlength="4" name="<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE %>" value="0" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>

<tr>
  <td class=l>
      <font class="label">Totale Liberazione Anticipata - Integrazione</font>
  </td>
  <td class=l>
    Giorni  <input type="text" size="4" maxlength="4"  name="<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT %>" value="0" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>
<!--  // End  Nuova   L.A.  -->

<tr>
  <td class=l>
      <font class="label">Totale Riduzione Pena resarcimento danni</font>
  </td>
  <td class=l>
    Giorni  <input type=text size="4" maxlength="4" name="<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_RISARCIMENTO_DANNI_DL92 %>" value="0" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>


<tr>
  <td class=l>
      <font class="label">Data Provvedimento di Cumulo</font>
  </td>
  <td class=l colspan=2>
<%
String yycu="";
String mmcu="";
String ddcu="";
/*if (cumulo!=null && cumulo.getDataCumulo()!=null)
{
     yycu=DateUtils.getYearToString(cumulo.getDataCumulo());
     mmcu=DateUtils.getMonthToString(cumulo.getDataCumulo());
     ddcu=DateUtils.getDayToString(cumulo.getDataCumulo());
}else
 {*/
     yycu=DateUtils.getSysDate("yyyy");
     mmcu=DateUtils.getSysDate("MM");
     ddcu=DateUtils.getSysDate("dd");
 //}
%>
        <input type=text size=2 maxlength="2" name=Dprov value="<%=ddcu%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input type=text size=2 maxlength="2" name=Mprov value="<%=mmcu%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
        <input type=text size=4 maxlength="4" name=Yprov value="<%=yycu%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
  </td>

</tr>
<tr>
  <td class=l>
      <font class="label">Data Decorrenza Pena </font>
  </td>
  <td class=l colspan=2>
<%
String anno=null;
String mese=null;
String giorno=null;
/*if (penacumulo!=null && penacumulo.getDataDecorrenzaPena()!=null)
{
     anno=DateUtils.getYearToString(penacumulo.getDataDecorrenzaPena());
     mese=DateUtils.getMonthToString(penacumulo.getDataDecorrenzaPena());
     giorno=DateUtils.getDayToString(penacumulo.getDataDecorrenzaPena());
}*/
%>

    <input type=text size=2 maxlength="2" name=<%=ICostantiPenaCumulo.CAMPO_GIORNO_DATA_DECORRENZA_PENA%> value="<%=StringUtils.toStringJSP(giorno)%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
    <input type=text size=2 maxlength="2" name=<%=ICostantiPenaCumulo.CAMPO_MESE_DATA_DECORRENZA_PENA%> value="<%=StringUtils.toStringJSP(mese)%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">/
    <input type=text size=4 maxlength="4" name=<%=ICostantiPenaCumulo.CAMPO_ANNO_DATA_DECORRENZA_PENA%> value="<%=StringUtils.toStringJSP(anno)%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
  </td>

</tr>
<tr>
  <td class=l>
      <font class="label">Ergastolo</font>
  </td>
  <td class=l colspan=2>
      <select name=FlagErgastolo>
      <%=FlagErgastolo%>
      </select>
  </td>
</tr>
<tr>
  <td class=l>
      <font class="label">Durata Isolamento Diurno</font>
  </td>
  <td class=l>
      Anni  <input type=text size=2 maxlength="2" name=IsDiuAnni value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Mesi  <input type=text size=2 maxlength="2" name=IsDiuMesi value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Giorni  <input type=text size=2 maxlength="2" name=IsDiuGiorni value="<%=StringUtils.toStringJSP("0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>

</tr>
<%-- MEV 16 - Interoperabilità SIEP/NSC - Eliminati i link seguenti
<tr>
  <td class="l">
    <font class="label"><a href="Javascript:MisureSicurezza('f','<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>');">Misure di Sicurezza</a></font>
    <input type="hidden" title="Campo" value="" type="text" name="<%=ICostantiPenaCumulo.CAMPO_MISURA_SICUREZZA%>">

  </td>
  <td class="l">
    <font class="label"><a href="Javascript:PeneAccessorie('f','<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>');">Pene Accessorie</a></font>
    <input type="hidden" title="Campo" value="" type="text" name="<%=ICostantiPenaCumulo.CAMPO_PENA_ACCESSORIA%>">

  </td>
</tr>
 --%>
<tr><td colspan=3 class=Titolo>Sospensione per parte di pena</td></tr>
<tr>
 <td class="l">Estremi Ordinanza</td>
   <td class="L" colspan="2">
<%
String estremi=null;

/*if (penacumulo!=null && penacumulo.getEstremiOrdinanza()!=null)
{
 estremi = penacumulo.getEstremiOrdinanza();
}*/
%>

      <TEXTAREA title="Estremi" name="<%=ICostantiPenaCumulo.CAMPO_ESTREMI_ORDINANZA%>" cols=70 ><%=StringUtils.toStringJSP(estremi)%></textarea>
    </td>
</tr>
<tr>
 <td class="l">Motivazioni</td>
   <td class="L" colspan="2">
<%
String motivazioni=null;

/*if (penacumulo!=null && penacumulo.getMotivazioni()!=null)
{
 motivazioni = penacumulo.getMotivazioni();
}*/
%>

      <TEXTAREA title="Motivazione" name="<%=ICostantiPenaCumulo.CAMPO_MOTIVAZIONI%>"  cols=70><%=StringUtils.toStringJSP(motivazioni)%></textarea>
    </td>
</tr>
<tr>
  <td class=l>
      <font class="label">Reclusione</font>
  </td>
  <td class=l colspan="2">
<%
BigDecimal aaR=null;
BigDecimal mmR=null;
BigDecimal ggR=null;

/*if (penacumulo!=null)
{
 if(penacumulo.getNumAnniReclusioneSosp() != null)
  aaR = penacumulo.getNumAnniReclusioneSosp();

 if(penacumulo.getNumMesiReclusioneSosp() != null)
  mmR = penacumulo.getNumMesiReclusioneSosp();

 if(penacumulo.getNumGiorniReclusioneSosp() != null)
  ggR = penacumulo.getNumGiorniReclusioneSosp();
}*/
%>
      Anni  <input type=text size=2 maxlength="2"   name=<%=ICostantiPenaCumulo.CAMPO_NUM_ANNI_RECLUSIONE_SOSP%> value="<%=StringUtils.toStringJSP(aaR,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Mesi  <input type=text size=2 maxlength="2"   name=<%=ICostantiPenaCumulo.CAMPO_NUM_MESI_RECLUSIONE_SOSP%> value="<%=StringUtils.toStringJSP(mmR,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Giorni  <input type=text size=2 maxlength="2" name=<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_SOSP%> value="<%=StringUtils.toStringJSP(ggR,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
   </td>
</tr>
<tr>
  <td class=l>
      <font class="label">Arresto</font>
  </td>
  <td class=l colspan="2">
<%
BigDecimal aaA=null;
BigDecimal mmA=null;
BigDecimal ggA=null;

/*if (penacumulo!=null)
{
 if(penacumulo.getNumAnniArrestoSosp() != null)
  aaA = penacumulo.getNumAnniArrestoSosp();

 if(penacumulo.getNumMesiArrestoSosp() != null)
  mmA = penacumulo.getNumMesiArrestoSosp();

 if(penacumulo.getNumGiorniArrestoSosp() != null)
  ggA = penacumulo.getNumGiorniArrestoSosp();
}*/
%>

      Anni  <input type=text size=2 maxlength="2"   name=<%=ICostantiPenaCumulo.CAMPO_NUM_ANNI_ARRESTO_SOSP%> value="<%=StringUtils.toStringJSP(aaA,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Mesi  <input type=text size=2 maxlength="2"   name=<%=ICostantiPenaCumulo.CAMPO_NUM_MESI_ARRESTO_SOSP%> value="<%=StringUtils.toStringJSP(mmA,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >&nbsp;
      Giorni  <input type=text size=2 maxlength="2" name=<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_ARRESTO_SOSP%> value="<%=StringUtils.toStringJSP(ggA,"0")%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" >
  </td>
</tr>
<tr>
  	<td class=l>
		<font class="label">Magistrato Competente</font>
  	</td>
  	<%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
  	<td class=l colspan=2>
      	<%=MagistratoCompetente.getMagistrato().getCognome()%>&nbsp;<%=MagistratoCompetente.getMagistrato().getNome()%>
  	</td>
	<input type="HIDDEN" title="pena" value="<%=cumulo.getIdCumulo()%>" type="text" name="idpenacumulo"  maxlength="35" size="35" >
</tr>
<tr>
  <td class=lNoBord colspan=3>
    <br><br><input class="bottone" type=submit value="Conferma">
  </td>

</tr>
</table>

</form>
<script language="JavaScript" type="text/javascript">

  var frmvalidator  = new Validator("f");


  frmvalidator.addValidation("RecAnni","numeric","Il campo Anni di Reclusione deve essere numerico");
  frmvalidator.addValidation("RecMesi","numeric","Il campo Mesi di Reclusione deve essere numerico");
  frmvalidator.addValidation("RecGiorni","numeric","Il campo Giorni di Reclusione deve essere numerico");
  frmvalidator.addValidation("ArrAnni","numeric","Il campo Anni di Arresto deve essere numerico");
  frmvalidator.addValidation("ArrMesi","numeric","Il campo Mesi di Arresto deve essere numerico");
  frmvalidator.addValidation("ArrGiorni","numeric","Il campo Giorni di Arresto deve essere numerico");
  frmvalidator.addValidation("MultaInt","numeric","Il campo Multa deve essere numerico");
  frmvalidator.addValidation("MultaDec","numeric","Il campo Multa deve essere numerico");
  frmvalidator.addValidation("AmmendaInt","numeric","Il campo Ammenda deve essere numerico");
  frmvalidator.addValidation("AmmendaDec","numeric","Il campo Ammenda deve essere numerico");
  frmvalidator.addValidation("IsDiuAnni","numeric","Il campo Anni di Isolamento Diurno deve essere numerico");
  frmvalidator.addValidation("IsDiuMesi","numeric","Il campo Mesi di Isolamento Diurno deve essere numerico");
  frmvalidator.addValidation("IsDiuGiorni","numeric","Il campo Giorni di Isolamento Diurno deve essere numerico");
 // frmvalidator.addValidation("LibAntGiorni","numeric","Il campo Giorni di Liberazione Anticipata deve essere numerico");
  frmvalidator.addValidation("Dprov","numeric","Il campo Giorno della data del Provvedimento deve essere numerico");
  frmvalidator.addValidation("Mprov","numeric","Il campo Mese della data del Provvedimento deve essere numerico");
  frmvalidator.addValidation("Yprov","numeric","Il campo Anno della data del Provvedimento essere numerico");

  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_ANNI_RECLUSIONE_SOSP%>","numeric","Il campo Anni di Reclusione Sospensione deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_MESI_RECLUSIONE_SOSP%>","numeric","Il campo Mesi di Reclusione Sospensione deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_RECLUSIONE_SOSP%>","numeric","Il campo Giorni di Reclusione Sospensione deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_ANNI_ARRESTO_SOSP%>","numeric","Il campo Anni di Arresto Sospensione deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_MESI_ARRESTO_SOSP%>","numeric","Il campo Mesi di Arresto Sospensione deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_ARRESTO_SOSP%>","numeric","Il campo Giorni di Arresto Sospensione deve essere numerico");

  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA %>","numeric","Il campo Giorni di Liberazione Anticipata deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_SPE%>","numeric","Il campo Giorni di Liberazione Anticipata Speciale deve essere numerico");
  frmvalidator.addValidation("<%=ICostantiPenaCumulo.CAMPO_NUM_GIORNI_LIBERAZIONE_ANTICIPATA_INT%>","numeric","Il campo Giorni di Integrazione Liberazione Anticipata deve essere numerico");
  
  frmvalidator.setAddnlValidationFunction("Verify");


</script>
</body>
</html>