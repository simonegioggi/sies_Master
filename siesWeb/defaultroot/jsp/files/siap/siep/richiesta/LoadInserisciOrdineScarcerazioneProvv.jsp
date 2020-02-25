<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.Vector" %>

<%@ page import="f3b.web.IWebConstants" %>
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>


<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento" %>

<%@ page import="siap.siep.notifica.action.ICostantiNotifica" %>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna" %>
<%@ page import="siap.siep.annotazionemanuale.model.AnnotazioneManualeModel" %>
<%@ page import="siap.siep.luogodetenzione.action.ICostantiLuogoDetenzione"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.annotazionemanuale.action.ICostantiAnnotazioneManuale" %>

<jsp:useBean id="annotazioneManuale"     scope="request" class="java.util.Vector"/>
<jsp:useBean id="posizioneluogoaltra"    scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="magistratocompetente"   scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="evento"                 scope="request" class="siap.sico.evento.model.EventoModel" />
<jsp:useBean id="codiceAutorita"         scope="request" class="java.lang.String"/>
<jsp:useBean id="codicemotivo"           scope="request" class="java.lang.String" />


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
    <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
        if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="" && document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Magistrato Assegnatario è obbligatorio");
          document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.focus();

          return false;
        }

        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data di emissione non valida');
          return false;
        }

        if (document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value;
        if (document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value.length==1)
          document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value='0'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value;

        var data_to_verify_trasm = document.f.<%=ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>.value+'-'+document.f.<%=ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>.value;

        if (!ControllaData(data_to_verify_trasm) )
        {
         alert('Data di Trasmissione atti non valida');
         return false;
        }

      }

      function ListaMagistrati(a_formname,a_fieldname,a_field2,a_field3)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMag&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2+"&field3="+a_field3, "Ricerca_Magistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }

      function ListaIstitutoDetenzione(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.siep.istitutodetenzione.action.ActLoadListaIstitutoDetenzione&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_Istituto_Detenzione","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }


      function ListaComuni(a_formname,a_fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaUDS(a_formname,a_fieldname)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUDS&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_UDS","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=370,height=500");
      }

      function ListaComuniTds(formname,fieldname)
      {
        desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComuneTds&formname="+formname+"&fieldname="+fieldname, "Ricerca_Comune_Tds","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }

      function ListaCSSA(a_formname,a_fieldname,a_field2)
      {
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.cssa.action.ActLoadListaCSSAFiltroComune&formname="+a_formname+"&fieldname="+a_fieldname+"&field2="+a_field2, "Ricerca_CSSA","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=500,height=500");
      }

  </script>
    <title>[S.I.E.S.] - Ordine Provvisorio di Scarcerazione</title>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font class="label">Funzione :&nbsp;</font>
        <%if(codicemotivo.equals("0367")){%>
        <font class="campo">Ordine Provvisorio di Scarcerazione per Indulto</font>
        <%}else if(codicemotivo.equals("0369")){%>
        <font class="campo">Ordine Provvisorio di Scarcerazione per Ex artt. 673 c.p. e 672 comma 3° c.p.p.</font>
        <%}%>
      </td>
        <td class="LBG">
<!--
     Torna alla pagina "Provvedimenti e Stampe per Rideterminazione Pena"
     per eliminare history.go(-1);
     predisporre un azione di Dettaglio per richiamare la pagina
     che al momento manca 02/04/2004  DL
-->
          <a href="Javascript:history.go(-1);">
            <img align="middle" src="<%=IWebConstants.IMAGES_DIR%>arrowleft24.gif" alt="ritorna su" width="24" height="24" border="0">
          </a>
        </td>
      </tr>
    </table>

    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>

    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.richiesta.action.ActInserisciOrdineScarcerazioneProvv">
    <input type="HIDDEN" name="<%=ICostantiEvento.CAMPO_ID_EVENTO%>" value="<%=evento.getIdEvento()%>">
    <input type="HIDDEN" name="codicemotivo" value="<%=codicemotivo%>">
    
<%
    AnnotazioneManualeModel lAnnManApp = new AnnotazioneManualeModel();
    if( !annotazioneManuale.isEmpty() )
    {
      lAnnManApp = (AnnotazioneManualeModel)annotazioneManuale.firstElement();
    }
%>
    <input type="HIDDEN" name="<%=ICostantiAnnotazioneManuale.CAMPO_ID_ANNOTAZIONE_MANUALE%>" value="<%=StringUtils.toStringJSP(lAnnManApp.getIdAnnotazioneManuale())%>">
  <table>
    <tr>
      <td class="l">Posizione Giuridica</td>
      <td class="L">
        <font class="campo"><%=lPosizione.getDescrPosizioneGiuridica()%></font></td>
    </tr>
  </table>
  <table>
    <tr>
      <td class="l">Data Emissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
        <td class="l">Data Trasmissione</td>
        <td class="L">
          <input value="<%=DateUtils.getSysDate("dd")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("MM")%>" type="text" size="2" maxlength="2" name="<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)"> -
          <input value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI %>" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
        </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
  </table>
  <table style="width: 95%;">
    <tr>
      <td colspan=8 class="titolo">Richiesta</td>
    </tr>
<%
    for(int i = 0;i<annotazioneManuale.size();i++)
    {
      AnnotazioneManualeModel lAnnPrima = (AnnotazioneManualeModel)annotazioneManuale.get(i);
%>
      <tr>
        <td class="l" width="20%">Data Richiesta</td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(DateUtils.getDateToString(lAnnPrima.getDataRichiesta(),"dd-MM-yyyy"))%>
          </font>
        </td>
      </tr>
      <tr>
        <td class="l" >DPR </td>
        <td class="L">
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnPrima.getDescrDpr())%>
          </font>
          &nbsp;Per  &nbsp;
          <font class="campo">
            <%=StringUtils.toStringJSP(lAnnPrima.getDescrTipoAnnotazione())%>
          </font>
        </td>
      </tr>
<%
  if( lAnnPrima.getMotivazioni()!= null )
  {
%>
    <tr>
      <td class="l">Motivazioni </td>
      <td class="L">
        <font class="campo">
          <%=StringUtils.toStringJSP(lAnnPrima.getMotivazioni())%></font>
        </font>
      </td>
    </tr>
<%
  }
}
%>
  </table>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" width="100%" colspan=6> Magistrato Assegnatario </td>
    </tr>
    <tr>
      <td class="l">Magistrato
      <td class="L">
        <input type="HIDDEN" title="CodiceMagistratoNuovo" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35" >
        <input title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input title= "Nome Magistrato"    value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"   maxlength="35" size="25">
          <a href="Javascript:ListaMagistrati('f','<%=ICostantiMagistrato.CAMPO_COD_MAGISTRATO%>','<%= ICostantiMagistrato.CAMPO_COGNOME %>','<%= ICostantiMagistrato.CAMPO_NOME %>');">
            <img src="/images/filefolder.gif" border=0>
          </a>
      </td>
    </tr>
  </table>

<%
//==============================================================================
//                        Sezione con i destinatari
//==============================================================================
%>
  <table style="width: 95%;">
    <tr>
      <td class="Titolo" colspan=6>Comunicazione per</td>
    </tr>
    <tr>
      <td class="l" width='30%'>Istituto di Detenzione</td>
      <td class="l" colspan="3">
<%
      if(posizioneluogoaltra != null &&  posizioneluogoaltra .getLuogoDetenzione()!= null && posizioneluogoaltra .getLuogoDetenzione().getIstitutoDetenzione() != null)
      {
%>
        <input readonly Title="Istituto" name="Comune" value="<%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrTipoIstituto())%> di <%=StringUtils.toStringJSP(posizioneluogoaltra.getLuogoDetenzione().getIstitutoDetenzione().getDescrComune())%>" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="<%=posizioneluogoaltra.getLuogoDetenzione().getIstDetIdIstitutoDetenzione()%>" size=50>
        <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
<%
      }
      else
      {
%>
        <input readonly Title="Istituto" name="Comune" value="" size=50>
        <input type="hidden"  Title="Istituto" name="<%=ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE%>" value="" size=35>
        <a href="Javascript:ListaIstitutoDetenzione('f','<%= ICostantiLuogoDetenzione.CAMPO_IST_DET_ID_ISTITUTO_DETENZIONE %>','Comune');">
          <img src="/images/filefolder.gif" border=0>
        </a>
<%
      }
%>
</td>
</tr>
  <tr>
      <td class="l"width='30%'>UEPE</td>
         <td class="l" colspan="3">
        <input readonly Title="UEPE Competente" name="Indirizzo" value="" size=60 >
        <input type="hidden" Title="UEPE Competente" name="<%=ICostantiNotifica.CAMPO_CSS_ID_CSSA%>" value="" size=35 >
       <a href="Javascript:ListaCSSA('f','<%=ICostantiNotifica.CAMPO_CSS_ID_CSSA%>','Indirizzo');">
        <img src="/images/filefolder.gif" border=0>
       </a>

      </td>

    </tr>
  <tr>
      <td class="l" width='30%'>Magistrato di Sorveglianza</td>
      <td class="L" colspan="3">
       <input title="ufficio" value="" type="text" name="<%=ICostantiNotifica.CAMPO_SEDE_MDS%>" maxlength="35" size="25">
       <a href="Javascript:ListaUDS('f','<%=ICostantiNotifica.CAMPO_SEDE_MDS%>');">
              <img src="/images/filefolder.gif" border=0> </a>
      </td>
      </tr>
   <tr>
           <td class="L" width='30%'>Tribunale di Sorveglianza</td>
        <td class="L" colspan="3">
         <input title="Sede Tribunale Sorveglianza" value="" type="text" name="<%= ICostantiNotifica.CAMPO_SEDE_TDS %>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuniTds('f','<%= ICostantiNotifica.CAMPO_SEDE_TDS%>');">
         <img src="/images/filefolder.gif" border=0></a></td>

     </tr>

  <tr>
<!--autorità di polizia-->
    <td class="l" width='30%'>Autorità Competente</td>
    <td class="L" colspan="3">
      <select  Title="Autorità Competente"  class="l" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_TIPO_AUTORITA%>">
        <%=codiceAutorita%>
      </select>

  </tr>
  <tr>
    <td class="l">Sede</td>
    <td class="L">
    <input title="Sede Autorità Competente"  type="text" name="<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>"  maxlength="35" size="35">
         <a href="Javascript:ListaComuni('f','<%=ICostantiAutoritaEsterna.CAMPO_COD_SEDE%>');">
          <img src="/images/filefolder.gif" border=0>
        </a>
      </td>
      </td>
      <td class="l">Indirizzo</td>
      <td class="L">
        <TEXTAREA title="Note" name="<%=ICostantiNotifica.CAMPO_NOTE%>"  cols=30 ></textarea>
      </td>
    </tr>

    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma" onClick="javascript:return Verify();">
      </td>
    </tr>
  </table>
</form>
<script language="JavaScript" type="text/javascript">
  var frmvalidator  = new Validator("f");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","req","Il campo Giorno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","req","Il campo Mese Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_EMISSIONE%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","req","Il campo Anno Emissione dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_EMISSIONE%>","lt=2050");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Giorno Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_GIORNO_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","req","Il campo Mese Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_MESE_DATA_TRASMISSIONE_ATTI%>","numeric");

  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","req","Il campo Anno Trasmissione Atti dell'Atto è obbligatorio");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","numeric");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","gt=1900");
  frmvalidator.addValidation("<%= ICostantiEvento.CAMPO_ANNO_DATA_TRASMISSIONE_ATTI%>","lt=2050");
</script>
</body>
</html>