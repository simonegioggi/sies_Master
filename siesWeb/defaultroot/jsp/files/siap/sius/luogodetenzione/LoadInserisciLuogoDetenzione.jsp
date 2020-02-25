<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Date"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.Iterator"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.decodifiche.model.DecodificheModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.sius.fascicolo.model.FascicoloSiusModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep" %>
<%@ page import="siap.sius.luogodetenzione.action.ICostantiLuogoDetenzione" %>
<%@ page import="siap.siep.altracausa.action.ICostantiAltraCausa" %>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel" %>
<%@ page import="siap.sius.fascicolo.action.ICostantiFascicoloSius"%>

<jsp:useBean id="PosizioneGiuridicaLuogoDetenzioneAltraCausaModel" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="modalita" scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicoloSiusGP" scope="session" class="siap.sius.fascicolo.model.FascicoloGPModel"/>

<%@ page import="java.util.Collection"%>

<% 
Collection posizioneGiuridica =(Collection) request.getAttribute("posizioneGiuridica");
%>


<script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
<script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
<%
  FascicoloSiepModel lFascicoloAssociato = (FascicoloSiepModel)session.getAttribute("fascicolo");

  PosizioneGiuridicaModel lPosizione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getPosizioneGiuridica();
  LuogoDetenzioneModel lLuogoDetenzione = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getLuogoDetenzione();
  AltraCausaModel lAltraCausa = PosizioneGiuridicaLuogoDetenzioneAltraCausaModel.getAltraCausa();

  if(lPosizione == null)
  {
    lPosizione = new PosizioneGiuridicaModel();
    //lPosizione.setPrimaPosizione(true);
  }

  if(lLuogoDetenzione == null)
  {
    lLuogoDetenzione = new LuogoDetenzioneModel();
  }

  if(lAltraCausa == null)
  {
    lAltraCausa = new AltraCausaModel();
  }
%>

<script language="JavaScript">
  var desktop;

<%
if(modalita.equals("M"))
{
%>
  var modifica = true;
<%
}
else
{
%>
  var modifica = false;
<%
}
%>

  function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
  {
    desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
  }

  function ListaComuni(a_formname,a_fieldname)
  {
    desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
  }
</script>

<script language="JavaScript">
  function Verify()
  {
    // Controllo validita' della data decorrenza detenzione
    var data_emissione=document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>.value+'/'+document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>.value+'/'+document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>.value;
    if (!modifica)
    {
    if (data_emissione!='//')
       {
         if (! ControllaData(data_emissione) )
         {
           alert('Data di decorrenza non valida');
           return false;
         }

       // Data decorrenza minore <= data sistema
       var data_sistema='<%=DateUtils.getSysDate("dd/MM/yyyy")%>';
       if ( ! CompareDate( data_emissione,data_sistema) )
       {
         alert('Data Decorrenza maggiore della data attuale!');
         return false;
       }
       }
     } else {
       // Controllo validita' della data FINE DETENZIONE
       var data_finedetenzione = document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_FINE_DETENZIONE%>.value+'/'+document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_MESE_DATA_FINE_DETENZIONE%>.value+'/'+document.LoadInserisciLuogoDetenzione.<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_FINE_DETENZIONE%>.value;

       if ((data_finedetenzione.length > 2) && ! ControllaData(data_finedetenzione))
       {
         alert('Data fine detenzione non valida');
         return false;
       }
     }
     return true;
  }
</script>

  <html>
  <head>
    <title>[S.I.E.S.] - Gestione Luogo Detenzione </title>
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=LBG>
          <font class="label">Funzione :</font>&nbsp;
<%
            PosizioneGiuridicaModel lPosGiu = new PosizioneGiuridicaModel();
            Date lDataDecorrenza = null;
            Date lDataFineDetenzione = null;

            String lAction = new String();

            if( modalita.equals("I") )
            {
              lAction = "siap.sius.luogodetenzione.action.ActInserisciLuogoDetenzione";
              lPosGiu = new PosizioneGiuridicaModel(lPosizione);
              lDataDecorrenza = lLuogoDetenzione.getDataInizioDetenzione();
              //lDataFineDetenzione = lLuogoDetenzione.getDataFineDetenzione();
%>
              <font class="campo">Iscrizione Luogo Detenzione</font>
<%
            }
            else if( modalita.equals("M") )
            {
              lAction = "siap.sius.luogodetenzione.action.ActModificaLuogoDetenzione";
              lPosGiu = new PosizioneGiuridicaModel(lPosizione);
              lDataDecorrenza = lLuogoDetenzione.getDataInizioDetenzione();
              lDataFineDetenzione = lLuogoDetenzione.getDataFineDetenzione();

%>
              <font class="campo">Modifica Luogo Detenzione</font>
<%
            }
%>
        </td>
      </tr>
  </table>

  <br>
  <table cellspacing=2 cellpadding=2>
    <tr>
      <jsp:include page="<%=ICostantiFascicoloSius.PG_LOAD_SINTESIPROCEDIMENTOSIUS%>"/>
    </tr>
  </table >

  <FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="LoadInserisciLuogoDetenzione">
    <table cellspacing=2 cellpadding=2>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="l">
  <%if (posizioneGiuridica != null){
             String DescPosizione = "";
            Iterator lIter = posizioneGiuridica.iterator();
            while(lIter.hasNext())
            {
              DecodificheModel lDecMod = (DecodificheModel)lIter.next();
             if (lPosGiu.getDescrPosizioneGiuridica().equals(lDecMod.getDescription()))
                 DescPosizione = lDecMod.getDescription();
            }
            if (DescPosizione.length() > 0)
            {%>
                 <%=DescPosizione%>
            <%}
}
%>
        </td>
      </tr>
      <tr>
        <td class="l">Data di Decorrenza</td>
        <td class="l">
          <input Title="Giorno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_INIZIO_DETENZIONE%>" <% if( modalita.equals("M")) {%> readonly <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_MESE_DATA_INIZIO_DETENZIONE%>" <% if( modalita.equals("M")) {%> readonly <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)" >
          -
          <input Title="Anno Data Decorrenza" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataDecorrenza, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>" <% if( modalita.equals("M")) {%> readonly <% } %> onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)" >
        </td>
       </tr>
      <tr>
        <td class="l">Tipo Istituto</td>
         <%if(lLuogoDetenzione.getIstDetIdIstitutoDetenzione() == null || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("") || lLuogoDetenzione.getIstDetIdIstitutoDetenzione().equals("-"))
           {%>
              <td class="l">
              <input readonly  Title="Istituto" name="Comune" value="" size=50>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=50>
             <%
            if( modalita.equals("I") )
              {
              %>
              <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciLuogoDetenzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
              <%
              }
              %>
               </td>
        <%}else {%>
              <td class="l">
<%
if (lLuogoDetenzione.getIstitutoDetenzione()!= null){
%>
              <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(lLuogoDetenzione.getIstitutoDetenzione().getDescrComune())%>" size=50>
<%
}
%>
              <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=lLuogoDetenzione.getIstDetIdIstitutoDetenzione()%>" size=50>
              <%
            if( modalita.equals("I") )
              {
              %>
             <a href="Javascript:ListaIstitutoDetenzione('LoadInserisciLuogoDetenzione','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
              <img src="/images/filefolder.gif" border=0></a>
              <%
              }
              %>
        </td>
       <%}%>
      </tr>

      <tr>
        <td class="l">Altro Luogo</td>
        <td class="L">
          <input title="Altro Luogo" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%>" type="text" name="<%=ICostantiLuogoDetenzione.CAMPO_ALTRO_LUOGO%>" size="35"  <% if( modalita.equals("M")) {%> readonly <% } %> >
        </td>
      </tr>

      <%
      if( modalita.equals("M") )
      {%>
      <tr>
        <td class="l">Data Fine Detenzione</td>
        <td class="l">
          <input Title="Giorno Fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "dd") )%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_GIORNO_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Mese Fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "MM"))%>" type="text" size="2" maxlength="2" name="<%= ICostantiLuogoDetenzione.CAMPO_MESE_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillDM(value)">
          -
          <input Title="Anno Fine Detenzione" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(lDataFineDetenzione, "yyyy"))%>" type="text" size="4" maxlength="4" name="<%= ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_FINE_DETENZIONE%>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)"  onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <%
      }%>

      <tr>
        <td colspan=2>
          <br>
          <input class="bottone" type="submit" value="Conferma">
        </td>
      </tr>

      <%if(lLuogoDetenzione.getFasSiuIdFascicoloSius() == null && lLuogoDetenzione.getIdLuogoDetenzione() != null)
        {%>
           <tr>
           <td class="l" colspan=2>NB: I dati elencati sono legati al procedimento SIEP, premere Conferma senza variarli per validarli in SIUS.        </td>
           </tr>
      <%}%>
    </table>
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="<%=lAction%>">
    <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_ID_POSIZIONE_GIURIDICA%>" value="<%=StringUtils.toStringJSP(lPosGiu.getIdPosizioneGiuridica())%>">
    <input type="HIDDEN" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIU_ID_FASCICOLO_SIUS%>" value="<%=StringUtils.toStringJSP(fascicoloSiusGP.getFascicoloSiusModel().getIdFascicoloSius())%>">
    <input type="HIDDEN" name="<%=ICostantiLuogoDetenzione.CAMPO_FAS_SIE_ID_FASCICOLO_SIEP%>" value="<%=StringUtils.toStringJSP(fascicoloSiusGP.getFascicoloSiusModel().getFasSieIdFascicoloSiep())%>">
    <input type="HIDDEN" name="<%=ICostantiLuogoDetenzione.CAMPO_ID_LUOGO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(lLuogoDetenzione.getIdLuogoDetenzione())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_ID_ALTRA_CAUSA%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getIdAltraCausa())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getIstDetIdIstitutoDetenzione())%>">
    <input type="HIDDEN" name="<%=ICostantiAltraCausa.CAMPO_ALTRO_LUOGO_ALTRA%>" value="<%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%>">

  </form>

  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("LoadInserisciLuogoDetenzione");

    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","numeric");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","gt=1900");
    frmvalidator.addValidation("<%=ICostantiLuogoDetenzione.CAMPO_ANNO_DATA_INIZIO_DETENZIONE%>","lt=3000");

    //Chiama la funzione di Verify().
    frmvalidator.setAddnlValidationFunction("Verify");
  </script>

</body>
</html>