<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>
<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.siep.sospensione.action.ICostantiSospensione"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.verbale.action.ICostantiVerbale"%>

<jsp:useBean id="posizioneluogoaltra" scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"        scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="tipoAutorita"  scope="request" class="java.lang.String"/>

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
<title>[S.I.E.S.] - Gestione Espulsione </title>
  <link rel="STYLESHEET" type="text/css" href="<%=IWebConstants.PG_STYLE%>">
  <script language="JavaScript" src="<%=IWebConstants.JS_VALIDATOR%>"></script>
  <script language="JavaScript" src="<%=IWebConstants.JS_DATE_CONTROL%>"></script>
  <script language="JavaScript">
    var desktop;
    function ListaComuni(a_formname,a_fieldname)
    {
      desktop = window.open("/jsp/Main.jsp?<%=IWebConstants.ACTION_FIELD%>=siap.sico.decodifiche.action.ActLoadRicercaComune&formname="+a_formname+"&fieldname="+a_fieldname, "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
    }


  function Verify()
  {


if (document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value.length==1)
     document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value='0'+document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value;
  if (document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value.length==1)
     document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value='0'+document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value;

   var data_to_verify_veremi = document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>.value+'-'+document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>.value+'-'+document.f.<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>.value;
   if (!ControllaData(data_to_verify_veremi) )
   {
     alert('Data di Emissione non valida');
     return false;
   }  


  if (document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value.length==1)
     document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value='0'+document.f.<%=ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value;
  if (document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value.length==1)
     document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value='0'+document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value;

   var data_to_verify_verpe = document.f.<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>.value+'-'+document.f.<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>.value+'-'+document.f.<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>.value;
   if (!ControllaData(data_to_verify_verpe) )
   {
     alert('Data di Pervenimento non valida');
     return false;
   }  
   
   
   if(document.f.<%= ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO %>[document.f.<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>.selectedIndex].value == '-')
      {
        alert("L'Autorità che ha provveduto all'espulsione è obbligatoria!");
        document.f.<%= ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO %>.focus();
        return false;
      }

    if (document.f.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE%>.value.length==1)
      document.f.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE%>.value='0'+document.f.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE%>.value;
    if (document.f.<%=ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE%>.value.length==1)
      document.f.<%=ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE%>.value='0'+document.f.<%=ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE%>.value;

    var data_espulsione_to_verify = document.f.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE%>.value+'-'+document.f.<%=ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE%>.value+'-'+document.f.<%=ICostantiSospensione.CAMPO_ANNO_DATA_ESPULSIONE%>.value;

    if (!ControllaData(data_espulsione_to_verify) )
    {
      alert('Data di Scarcerazione per Espulsione non valida!');
      document.f.<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE %>.focus();
      return false;
    }

  return true;
}

  </script>
</head>

<body class="corpo">
  <table>
    <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
    <td class="LBG"><font class="label">Funzione :</font>&nbsp;&nbsp;
    <font class="campo">AVVENUTA ESPULSIONE ART.16 COMMA 5 D.LO 286/1998 E SUCC.MOD.
</font>
</td>
</tr>
</table>
 <br>
   <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
  <br>
	<FORM method="POST" action="<%= IWebConstants.PG_MAIN%>" name="f">
  <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.sospensione.action.ActCalcolaAvvenutaEspulsione">
  <INPUT type="HIDDEN" name="posizionegiuridica" value="<%=posizioneluogoaltra.getPosizioneGiuridica().getCodPosizioneGiuridica()%>">
  <table>
    <tr>
     <td class="l">Posizione Giuridica </td>
     <td class="L" colspan=5>
      <font class="campo">
<%     if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
       {
%>
              DETENUTO PER ALTRA CAUSA
<%     }
       else
       {
%>
         <%=lPosizione.getDescrPosizioneGiuridica()%>
<%
       }
%>
      </font>
    </td>
   </tr>
<%
        if(lFascicoloAssociato.getFlagAltraCausa()!=null && lFascicoloAssociato.getFlagAltraCausa().equals("S"))
        {
           if( lAltraCausa.getIstitutoDetenzione()!= null)
           {
%>
           <tr>
             <td class="l">Detenuto presso </td>
             <td class="L" colspan=5><font class="campo"><%=lAltraCausa.getIstitutoDetenzione().getDescrTipoIstituto()%></font>


                 di<font class="campo"> <%=lAltraCausa.getIstitutoDetenzione().getDescrComune()%></font>

            </td>
           </tr>
<%
               if (lAltraCausa.getAltroLuogo()!=null)
               {
%>
                <tr>
                  <td class="l">Altro Luogo </td >
                  <td class="L" colspan=5>
                    <font class="campo"><%=StringUtils.toStringJSP(lAltraCausa.getAltroLuogo())%></font>&nbsp;
                  </td>
                </tr>
<%
               }
            }
        }
        else if( lLuogoDetenzione.getIstitutoDetenzione()!= null )
        {
%>      <tr>
           <td class="l">Detenuto presso </td>
           <td class="L" colspan=5>
            <font class="campo"><%=lLuogoDetenzione.getIstitutoDetenzione().getDescrTipoIstituto()%></font>
                di<font class="campo"> <%=lLuogoDetenzione.getIstitutoDetenzione().getDescrComune()%></font>
            </td>
          </tr>
<%
        }%>
 
<% // Nel Caso di Posizione Giuridica ARRESTI DOMICILIARI ( 02, 04)
        if(lPosizione.getCodPosizioneGiuridica() != null && (lPosizione.getCodPosizioneGiuridica().equals("02") || lPosizione.getCodPosizioneGiuridica().equals("04")) )
        {
         if(lLuogoDetenzione.getAltroLuogo() != null)
          {
%>
            <tr>
              <td class="l">Indirizzo</td>
              <td class="L" colspan=5>
                <font class="campo"><%=StringUtils.toStringJSP(lLuogoDetenzione.getAltroLuogo())%></font>&nbsp;
              </td>
          </tr>
<%
          }
        }
%>


   <tr>
<%
    if(penaresidua.getIdPenaResidua() != null && ( (penaresidua.getFlagErgastolo() == null) || (penaresidua.getFlagErgastolo() != null && !penaresidua.getFlagErgastolo().equals("S") && !penaresidua.getFlagErgastolo().equals("D")) ) )
    {
        if ((penaresidua.getNumAnniReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumMesiReclusione().compareTo(new BigDecimal(0))==0) &&
            (penaresidua.getNumGiorniReclusione().compareTo(new BigDecimal(0))==0)
            )
        {}
        else
        {
%>
          <td class="l">Reclusione</td>
          <td class="l" colspan=2>
            <font class="l">Anni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumAnniReclusione(),"0")%>&nbsp;</font>
            <font class="l">Mesi&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumMesiReclusione(),"0")%>&nbsp;</font>
            <font class="l">Giorni&nbsp;</font><font class="campo" ><%=StringUtils.toStringJSP(penaresidua.getNumGiorniReclusione(),"0")%></font>
          </td>
          <td class="l">Multa</td>
          <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoMulta())%></font>&nbsp;<font class="l">Euro</font></td>
<%
        }
%>
   </tr>
   <tr>
<%
    if ((penaresidua.getNumAnniArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumMesiArresto().compareTo(new BigDecimal(0))==0) &&
        (penaresidua.getNumGiorniArresto().compareTo(new BigDecimal(0))==0))
    {}else{
%>
      <td class="l" >Arresto</td>
      <td class="l" colspan=2>
         <font class="l">Anni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumAnniArresto(),"0")%>&nbsp;</font>
         <font class="l">Mesi&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumMesiArresto(),"0")%>&nbsp;</font>
         <font class="l">Giorni&nbsp;</font><font class="campo"><%=StringUtils.toStringJSP(penaresidua.getNumGiorniArresto(),"0")%></font>
      </td>
      <td class="l">Ammenda</td>
      <td class="l" colspan=2><font class="campo"><%=StringUtils.toEuroFormat(penaresidua.getImportoAmmenda())%></font>&nbsp;<font class="l">Euro</font></td>
<%
      }
    }
%>
</tr>

      <tr>
<%
       if (penaresidua.getDataInizio() != null)
       {
%>
         <td class="l">Data Decorrenza Pena</td>
         <td class="L"><font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(penaresidua.getDataInizio(),"dd-MM-yyyy"))%>&nbsp;</font></td>
<%
       }



       if (penaresidua.getFlagErgastolo() != null)
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
<table width=100%>
    <tr>
      <td colspan=4 class="titolo">Annotazione Verbale di Espulsione</td>
    </tr>
   <tr>
      <td class="l" width=30%>Protocollo N.</td>
      <td class="l" colspan="3">
        <input Title="Protocollo N." name="<%=ICostantiVerbale.CAMPO_NUMERO_PROTOCOLLO%>" type="text" size="50" maxlength="50">
      </td>
   </tr>
   <tr>
      <td class="l">Data Emissione provvedimento</td>
      <td class="L" >
          <input title = "Giorno Data Emissione" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Data Emissione" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Data Emissione" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_EMISSIONE %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </td>
      <td class="l">Data Ricezione Verbale</td>
      <td class="L">
          <input title = "Giorno Ricezione Verbale" value="<%=DateUtils.getSysDate("dd")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_GIORNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Mese Ricezione Verbale" value="<%=DateUtils.getSysDate("MM")%>"   type="text" size="2" maxlength="2" name="<%= ICostantiVerbale.CAMPO_MESE_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA%> > -
          <input title = "Anno Ricezione Verbale" value="<%=DateUtils.getSysDate("yyyy")%>" type="text" size="4" maxlength="4" name="<%= ICostantiVerbale.CAMPO_ANNO_DATA_PERVENIMENTO %>" <%=IWebConstants.UTIL_DATA_ANNO%> >
      </td>
   </tr> 
</table>
<table width=100%>    
   <tr>
      <td class="l" width=30%>Autorità che ha provveduto all'espulsione <font class="ob">(*)</font></td>
      <td class="l" colspan='3'>
        <select title="Autorità che ha provveduto all'espulsione" name="<%=ICostantiVerbale.CAMPO_COD_TIPO_UFFICIO_FIRMATARIO%>">
          <%=tipoAutorita%>
        </select>
      </td>
   </tr>
   <tr>
       <td class="l">Sede</td>
       <td class="L">
        <input title="Sede" value="" type="text" name="<%=  ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO%>"  maxlength="35" size="35">
        <a href="Javascript:ListaComuni('f','<%= ICostantiVerbale.CAMPO_COD_LUOGO_UFFICIO_FIRMATARIO %>');">
         <img src="/images/filefolder.gif" border=0>
        </a>
       </td>
       <td class="l">Indirizzo</td>
       <td class="L">
        <TEXTAREA title="Note" name="<%= ICostantiVerbale.CAMPO_NOTE %>"  cols=30></textarea>
       </td>
   </tr>
   <tr>
    <td class="l">Data Scarcerazione per espulsione  <font class="ob">(*)</font></td>
     <td class="l" colspan="3">
       <input type="text" Title="Giorno Scarcerazione per espulsione " value="" name="<%=ICostantiSospensione.CAMPO_GIORNO_DATA_ESPULSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Mese Scarcerazione per espulsione " value="" name="<%=ICostantiSospensione.CAMPO_MESE_DATA_ESPULSIONE%>" maxlength="2" size="2" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
       -
        <input type="text" Title="Anno Scarcerazione per espulsione " value="" name="<%=ICostantiSospensione.CAMPO_ANNO_DATA_ESPULSIONE%>" maxlength="4" size="4" onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">

    </td>
  </tr>
</table>
<table width="100%">
    <tr>
      <td class="lNoBord" colspan="2">
        <br><INPUT class="bottone" type="submit" name="I" value="Calcola">
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