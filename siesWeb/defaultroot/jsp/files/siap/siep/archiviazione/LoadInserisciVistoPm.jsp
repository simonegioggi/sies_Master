<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>

<jsp:useBean id="posizioneluogoaltra"   scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua" scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo" scope="request" class="java.lang.String"/>
<jsp:useBean id="magistratocompetente"         scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>
<jsp:useBean id="oggettodefinzione" scope="request" class="java.lang.String"/>

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
    <title>[S.I.E.S.] - Definizione Procedimento - Visto del PM</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
        //DATA EMISSIONE
        if (document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value;
        if (document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value.length==1)
          document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value='0'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>.value+'/'+document.f.<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data Richiesta non valida');
          document.f.<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>.focus();

          return false;
        }


        
        if(document.f.<%=ICostantiMagistrato.CAMPO_COGNOME %>.value=="")
        {
          alert("Il Cognome del Magistrato è obbligatorio");
          return false;
        }

        if(document.f.<%=ICostantiMagistrato.CAMPO_NOME %>.value=="")
        {
          alert("Il Nome del Magistrato è obbligatorio");
          return false;
        }

        return true;
      }


//funzioni
      function ListaMagistrati(a_formname)
      {
        var desktop;
        desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.magistrato.action.ActLoadRicercaMagistratoLista&formname="+a_formname, "Ricerca_WMagistrato", "toolbar=no, location=no, status=no, menubar=no, scrollbars=yes, resizable=no, width=500, height=500");
      }


    </script>
  </head>
  <body class="corpo">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Definizione Procedimento - Visto del PM</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciVistoPm">
     <input type="HIDDEN" name="tipobottone" value="">

    <table>
      <tr>
        <td class="l">Posizione Giuridica </td>
        <td class="L" colspan=8>
          <font class="campo">
            <%=StringUtils.toStringJSP(lPosizione.getDescrPosizioneGiuridica())%>
          </font>
        </td>
      </tr>
<%
    if( flagergastolo.equals("N") )
    {
      if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
          )
      {}
      else
      {
%>
          <tr>
            <td class="l">Reclusione</td>
            <td class="l" colspan=2>
              <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
              <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
              <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
            </td>
            <td class="l">Multa</td>
            <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
          </tr>
<%
      }

      if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
          (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
      {}
      else
      {
%>
        <tr>
          <td class="l" >Arresto</td>
          <td class="l" colspan=2>
             <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
             <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
             <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
          </td>
          <td class="l">Ammenda</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
        </tr>
<%
      }
    }
%>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }

       if ( penaresidua.getFlagErgastolo() != null)
       {
        if(penaresidua.getFlagErgastolo().equals("S"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO&nbsp;</font></td>
<%
        }
        else
        if(penaresidua.getFlagErgastolo().equals("D"))
        {
%>
          <td class="l">Pena Detentiva</td>
          <td class="L"><font class="campo">ERGASTOLO CON ISOLAMENTO DIURNO&nbsp;</font></td>
<%
        }
       }

if((!lPosizione.isLibero()) || (lFascicoloAssociato.getFlagAltraCausa()!=null &&  lFascicoloAssociato.getFlagAltraCausa().equals("S") ) )
{
 if  ((penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")))
 {
    if( penaresidua.getDataFine() != null)
          {
            if(penaresidua.getDataFine().equals(penaresidua.getDataFinePresunta()))
            {
%>
             <td class="l">Data Fine Pena</td>
             <td class="L" colspan=2>
               <font class="campo"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
             </td>
<%
          }else{
%>
                <td class="l">Data Fine Pena</td>
                <td class="lRosso" colspan=2>
                 <font class="lRosso"> <%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "dd") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "MM") )%>-<%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataFine(), "yyyy") )%></font>
                 </td>
<%            }
        }
      }
}
%>

     </tr>
  </table>
  <br>
  <table width="100%">
    <tr>
      <td colspan=5 class="titolo">Oggetto</td>
    </tr>
    <tr>
      <td class="l" width="22%">Data Richiesta <font class=ob>(*)</font></td>
      <td class="l" colspan="4">
        <input type="text" Title="Giorno Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(),"dd"))%>" name="<%=ICostantiArchiviazione.CAMPO_GIORNO_DATA_DEFINIZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(),"MM"))%>" name="<%=ICostantiArchiviazione.CAMPO_MESE_DATA_DEFINIZIONE%>" maxlength="2" size="2"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno Richiesta" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(DateUtils.getSysDate(),"yyyy"))%>" name="<%=ICostantiArchiviazione.CAMPO_ANNO_DATA_DEFINIZIONE%>" maxlength="4" size="4"  onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>  
    <tr>
      <td class="l" width="30%">Oggetto Definizione <font class="ob">(*)</font></td>
      <td class="l">
        <select Title="Oggetto Definzione" class="small" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>">
          <%=oggettodefinzione%>
        </select>
      </td>
    </tr>    
    <tr>
       <td rowspan=2 class="l" width="22%">Motivazione</td>
       <td rowspan=2 class="L" colspan="4">
          <textarea title="Motivazione" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=4></textarea>
       </td>
    </tr>
</table>
<table width="100%">
    <tr>
      <td class="titolo" width="100%" colspan=2>Magistrato firmatario</td>
    </tr>
    <tr>
      <td class="l">Magistrato Firmatario <font class=ob>(*)</font></td>
      <td class="L">
        <input readonly title="Cognome Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCognome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COGNOME %>" maxlength="35" size="25">
        <input readonly title= "Nome Magistrato"   value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getNome() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_NOME %>"      maxlength="35" size="25">
         <a href="Javascript:ListaMagistrati('f');">
           <img src="/images/filefolder.gif" border=0>
         </a>
       </td>
       <td>
         <input type="HIDDEN" title="Codice Magistrato" value="<%=StringUtils.toStringJSP(magistratocompetente.getMagistrato().getCodMagistrato() )%>" type="text" name="<%= ICostantiMagistrato.CAMPO_COD_MAGISTRATO %>"  maxlength="35" size="35">
       </td>
    </tr>
    <tr><td>&nbsp;</td></tr>
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Conferma">
      </td>
    </tr>
  </table>
</form>
  <script language="JavaScript" type="text/javascript">
    var frmvalidator = new Validator("f");

    frmvalidator.setAddnlValidationFunction("Verify");
  </script>
</body>
</html>