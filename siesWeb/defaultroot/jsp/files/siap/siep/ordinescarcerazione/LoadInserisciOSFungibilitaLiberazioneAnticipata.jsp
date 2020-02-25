<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Iterator"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.evento.model.EventoModel"%>
<%@ page import="siap.siep.avvocato.model.AvvocatoSiepModel"%>
<%@ page import="siap.siep.avvocato.action.ICostantiAvvocato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.penaresidua.action.ICostantiPenaResidua"%>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.penacomplessiva.model.PenaComplessivaModel"%>
<%@ page import="siap.siep.penacomplessiva.action.ICostantiPenaComplessiva"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.ordinescarcerazione.action.ICostantiOrdineScarcerazione"%>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa"%>
<%@ page import="siap.siep.fungibilita.action.ICostantiFungibilita"%>
<%@ page import="siap.siep.misuraalternativa.action.ICostantiMisuraAlternativa"%>

<jsp:useBean id="evento"              scope="request" class="siap.sico.evento.model.EventoNotificaModel"/>
<jsp:useBean id="penaresidua"         scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="fungibilita"         scope="request" class="siap.siep.fungibilita.model.FungibilitaModel"/>
<jsp:useBean id="autoritaEsterna"   scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaEsternaN"   scope="request" class="java.lang.String"/>
<jsp:useBean id="avvocati"           scope="request" class="java.util.Vector"/>
<jsp:useBean id="flagmisura"      scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratosorveglianza"    scope="request" class="siap.sico.magistrato.model.MagistratoModel"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="misuraalternativa"       scope="request" class="siap.sico.misuraalternativa.model.MisuraAlternativaModel"/>
<jsp:useBean id="sedeUfficioEmittente"   scope="request" class="siap.sico.ufficio.model.UfficioModel"/>
<jsp:useBean id="ufficioemittente"      scope="request" class="java.lang.String"/>
<jsp:useBean id="comunetds"      scope="request" class="siap.sico.decodifiche.model.ComuneModel"/>
<jsp:useBean id="UffUDS"       scope="request" class="java.lang.String"/>
<%
   FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");
   PosizioneGiuridicaModel lPosizione = posizioneluogoaltra.getPosizioneGiuridica();
   LuogoDetenzioneModel lLuogoDetenzione = posizioneluogoaltra.getLuogoDetenzione();
   AltraCausaModel lAltraCausa = posizioneluogoaltra.getAltraCausa();

  if(lPosizione == null)
    lPosizione = new PosizioneGiuridicaModel();

  if(lLuogoDetenzione == null)
    lLuogoDetenzione = new LuogoDetenzioneModel();

  if(lAltraCausa == null)
    lAltraCausa = new AltraCausaModel();
%>

<html>
  <head>
    <title>[S.I.E.S.] - Gestione evento </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
    <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<script language="JavaScript">
      var desktop;

    function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }
</script>
<script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }
    function Verify()
   {
     if(document.LoadInserisciOrdineScarcerazione.<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI%>.value=="")
     {
       alert("Inserire il luogo dell'Ufficio Giudiziario");
       return false;
     }

   }
  </script>
</head>
<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
      <td class="LBG">
      <font class="label">Funzione :</font>&nbsp;&nbsp;
<%
        EventoModel lProvvedimento = new EventoModel();
        lProvvedimento = new EventoModel(evento);
%>
<%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {%>
   <font class="campo">Emissione Ordine di Scarcerazione con Fungibilità- DETENUTO PER ALTRA CAUSA</font>
        <%}else{%>
       <font class="campo">Emissione Ordine di Scarcerazione con Fungibilità - <%=lPosizione.getDescrPosizioneGiuridica()%></font>
<%}%>
     </td>
    </tr>
  </table>
  <br>
    <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
  <FORM method="POST" name="LoadInserisciOrdineScarcerazione" action="<%= IWebConstants.PG_MAIN%>">
   <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.ordinescarcerazione.action.ActInserisciOSFungibilitaLiberazioneAnticipata">
   <input type="HIDDEN" name="posizionegiuridica" value="<%=lPosizione.getDescrPosizioneGiuridica()%>">
  <table>
     <tr>
        <td class="c">
         SCADENZA EFFETTIVA DELLA PENA <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd-MM-yyyy"))%>
         INFERIORE ALLA DATA DEL PROVVEDIMENTO <%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"dd-MM-yyyy"))%>
        <br>
         CON UN PERIODO FUNGIBILE DI anni <%=StringUtils.toStringJSP(fungibilita.getNumAnni())%>
         mesi <%=StringUtils.toStringJSP(fungibilita.getNumMesi())%>
         giorni <%=StringUtils.toStringJSP(fungibilita.getNumGiorni())%>
       </td>
      </tr>
        <input type="HIDDEN"  value="<%=flagmisura%>" name="flagmisura">

    <input type="HIDDEN" name="<%=ICostantiFungibilita.CAMPO_NUM_ANNI%>"       value="<%=fungibilita.getNumAnni()%>">
    <input type="HIDDEN" name="<%=ICostantiFungibilita.CAMPO_NUM_MESI%>"       value="<%=fungibilita.getNumMesi()%>">
    <input type="HIDDEN" name="<%=ICostantiFungibilita.CAMPO_NUM_GIORNI%>"     value="<%=fungibilita.getNumGiorni()%>">

    <input type="HIDDEN" name="<%=fungibilita%>" value="S">

    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>"           value="<%=evento.getEvento().getIdEvento() %>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ANNO_DATA_FINE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"yyyy"))%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_MESE_DATA_FINE%>"   value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"MM"))%>">
    <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_GIORNO_DATA_FINE%>" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(),"dd"))%>">

   <input type="HIDDEN" name="<%=ICostantiPenaResidua.CAMPO_ID_PENA_RESIDUA%>" value="<%=StringUtils.toStringJSP(penaresidua.getIdPenaResidua())%>">

   <input type="HIDDEN" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>"       value="<%=StringUtils.toStringJSP(evento.getEvento().getCodMagistrato())%>">

   <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"yyyy"))%>">
   <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"MM"))%>">
   <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>"     value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"dd"))%>">

   <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_ANNO_DATA_INVIO%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataTrasmissioneAtti(),"yyyy"))%>">
   <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_MESE_DATA_INVIO%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataTrasmissioneAtti(),"MM"))%>">
   <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_GIORNO_DATA_INVIO%>"     value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataTrasmissioneAtti(),"dd"))%>">

   <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_DATA_DECISIONE%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"yyyy"))%>">
   <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_MESE_DATA_DECISIONE%>"       value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"MM"))%>">
   <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_GIORNO_DATA_DECISIONE%>"     value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(evento.getEvento().getDataEmissione(),"dd"))%>">
<%
int conta = 0;
 %>
      <input type="hidden" name="magSorv" value="S">
      <input type="HIDDEN" title="CodiceMagistrato" value="<%=UffUDS%>" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UDS%>">
      <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_UDS%>"   value="<%=StringUtils.toStringJSP(evento.getNotifiche()[0].getNote())%>">
      <input type="hidden" name="notificaMagistrato" value="C">
      <input type="hidden" value="TDS" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_TRIBUNALE%>">
      <input type="hidden" name="notificaTribunale" value="C">
      <input type="hidden" value="<%=StringUtils.toStringJSP(sedeUfficioEmittente.getDescrComune())%>" name="<%= ICostantiMisuraAlternativa.CAMPO_SEDE_TDS %>">
      <input type="HIDDEN" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE_TDS%>"   value="<%=StringUtils.toStringJSP(evento.getNotifiche()[1].getNote())%>">
      <input type="hidden" name="tds" value="S">
      <input type="HIDDEN"  name="<%=ICostantiNotifica.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=evento.getNotifiche()[2].getIstDetIdIstitutoDetenzione()%> ">
      <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_NOTE_IST%>"   value="<%=StringUtils.toStringJSP(evento.getNotifiche()[2].getNote())%>">
      <input type="HIDDEN"  name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_E%>" value="<%=StringUtils.toStringJSP(evento.getNotifiche()[3].getAutoritaEsterna().getCodTipoAutorita()) %>">
      <input type="HIDDEN" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_E%>"  value="<%=StringUtils.toStringJSP(evento.getNotifiche()[3].getAutoritaEsterna().getDescrSede())%>">
      <input type="HIDDEN" name="<%=ICostantiNotifica.CAMPO_NOTE_E%>"   value="<%=StringUtils.toStringJSP(evento.getNotifiche()[3].getNote())%>">
<%
  int lIdxAvv = 0;
  Iterator lItxAvv = avvocati.iterator();
  while(lItxAvv.hasNext())
  {
    AvvocatoSiepModel lAvv =  (AvvocatoSiepModel)lItxAvv.next();
   %>
   <input type="HIDDEN" title="Codice Avvocato" value="<%=StringUtils.toStringJSP(lAvv.getAvvocatoFascicoloSiepModel().getIdAvvocatoFascicoloSiep())%>"name="<%= ICostantiAvvocato.CAMPO_ID_AVVOCATO %>">
   <%
  }%>
<%
  if (flagmisura.equals("N"))
{%>
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getDescrLuogoProva())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_DESCR_LUOGO_PROVA%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getAnnoRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_ANNO_REGISTRO%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getNumeroRegistro())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NUMERO_REGISTRO%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveProgrFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_PROGR_FASCICOLO_SIUS%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getChiaveAnnoFascicoloSius())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_CHIAVE_ANNO_FASCICOLO_SIUS%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getNote())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_NOTE%>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getCodTipoMisura())%>" name="<%= ICostantiMisuraAlternativa.CAMPO_COD_TIPO_MISURA %>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getNumGiorniMisura())%>" name="<%= ICostantiMisuraAlternativa.CAMPO_NUM_GIORNI_MISURA %>">
     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(misuraalternativa.getCodUfficioSorveglianza())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_COD_UFFICIO_SORVEGLIANZA%>" >

     <input type="HIDDEN"  value="<%=StringUtils.toStringJSP(comunetds.getDescrizione())%>" name="<%=ICostantiMisuraAlternativa.CAMPO_SEDE_TDS_EMITT%>">
<%}%>
   <tr><td>&nbsp;</td></tr>
    </table>
   <table>
     <tr><td class="Titolo" colspan=6>Ufficio Giudiziario Destinatario </td></tr>
      <tr>
         <td class="l">Autorità Destinazione </td>
           <td class="l">
            <select Title="Autorita Esterna" class="small" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA_FUNGI%>" >
               <%=autoritaEsternaN%>
            </select>
         </td>
         <td rowspan=2 class="l">Note</td>
         <td rowspan=2 class="L">
            <textarea title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE_FUNGI%>"  cols=20 rows=5 ></textarea>
         </td>
      </tr>
      <tr>
        <td class="l">Sede </td>
        <td class="L">
        <input title="Sede Autorita Esterna" value="" type="text" name="<%= ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('LoadInserisciOrdineScarcerazione','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE_FUNGI %>');">
        <img src="/images/filefolder.gif" border=0>
        </a>
        </td>
      </tr>
     </table>
    <%if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
    {%>
     <table>
      <%if(lAltraCausa.getAnno()!=null || lAltraCausa.getNumero()!=null)
      {%>
      <tr><td class="Titolo" colspan=6>Sentenza </td></tr>
      <tr>
         <td class="l">
            SENTENZA N.  <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAnno())%></font>/<font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getNumero())%></font>
         </td>
       </tr>
       <tr>
         <td class="l">
         <%if(lAltraCausa.getData()!=null){%> DEL : <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAltraCausa.getData(),"dd-MM-yyyy"))%></font>
          <%}%>
        </td>
        </tr>
        <tr>
         <td class="l">
         <%if(lAltraCausa.getDescrAutorita()!=null){%> DA :  <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getDescrAutorita())%></font><%}%>  &nbsp;
        <%if(lAltraCausa.getDescrLuogo()!=null){%> di <font class="campo"> <%=StringUtils.toStringJSP(lAltraCausa.getDescrLuogo())%></font><%}%>
         </td>
      </tr>
     <%}%>
</table>
<%}%>
<%if(lLuogoDetenzione.getIstitutoDetenzione()!=null){
  %>
 <table>
<tr><td class="Titolo" colspan=6>NOTIFICA ISTITUTO </td></tr>
     <tr>
       <td class="l">Autorità Destinazione : </td>
       <td class="l"><font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%></font>
di <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%></font></td>
     </tr>
  </table>
  <%}%>
<table>
  <td class="lNoBord" colspan="2">
  <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
  </td>
</tr>
</table>
  </FORM>
</body>
</html>