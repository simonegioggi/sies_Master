<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.math.BigDecimal"%>

<%@ page import="f3b.web.IWebConstants"%>
<%@ page import="f3b.util.DateUtils"%>
<%@ page import="f3b.util.StringUtils"%>

<%@ page import="siap.sico.magistrato.action.ICostantiMagistrato"%>
<%@ page import="siap.siep.posizione.model.PosizioneGiuridicaModel"%>
<%@ page import="siap.siep.luogodetenzione.model.LuogoDetenzioneModel"%>
<%@ page import="siap.siep.altracausa.model.AltraCausaModel"%>
<%@ page import="siap.siep.penaresidua.model.PenaResiduaModel"%>
<%@ page import="siap.siep.fascicolo.model.FascicoloSiepModel"%>
<%@ page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<%@ page import="siap.siep.archiviazione.action.ICostantiArchiviazione"%>
<%@ page import="siap.siep.posizione.action.ICostantiPosizioneGiuridica"%>
<%@ page import="siap.siep.notifica.action.ICostantiNotifica"%>
<%@ page import="siap.siep.autoritaesterna.action.ICostantiAutoritaEsterna"%>
<%@ page import="siap.sico.ufficio.action.ICostantiUfficio"%>
<%@ page import="siap.sico.evento.action.ICostantiEvento"%>

<jsp:useBean id="posizioneluogoaltra"  scope="request" class="siap.siep.posizione.model.PosizioneGiuridicaLuogoDetenzioneAltraCausaModel"/>
<jsp:useBean id="penaresidua"          scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel"/>
<jsp:useBean id="flagergastolo"        scope="request" class="java.lang.String"/>
<jsp:useBean id="autoritaCumulo"       scope="request" class="java.lang.String"/>
<jsp:useBean id="codiceAutorita"       scope="request" class="java.lang.String"/>
<jsp:useBean id="uffrecrediti"         scope="request" class="java.lang.String"/>
<jsp:useBean id="fascicolo"            scope="session" class="siap.siep.fascicolo.model.FascicoloSiepModel"/>
<jsp:useBean id="magistratocompetente" scope="request" class="siap.sico.magistratocompetente.model.MagistratoCompetenteMagistratoModel"/>

<jsp:useBean id="codtipoufficio"       scope="request" class="java.lang.String"/>
<jsp:useBean id="descsede"             scope="request" class="java.lang.String"/>
<jsp:useBean id="destipoufficio"       scope="request" class="java.lang.String"/>

<jsp:useBean id="sedeCumulo"     	 	scope="request" class="java.lang.String"/>
<jsp:useBean id="EsitoTrasmissione"	   	scope="request" class="siap.siep.annotazioneesitotrasmissione.model.AnnotazioneEsitoTrasmissioneModel"/>
<jsp:useBean id="StessoUfficio"     	scope="request" class="java.lang.String"/>


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
    <title>[S.I.E.S.] - Annotazione dati cumulo</title>

    <script language="JavaScript" src=<%=IWebConstants.JS_DATE_CONTROL%>></script>
    <script language="JavaScript" src=<%=IWebConstants.JS_VALIDATOR%>></script>
    <script language="JavaScript">
      function Verify()
      {
        //DATA EMISSIONE
        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value.length==1)
          document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value='0'+document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value;
        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value.length==1)
          document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value='0'+document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value;

        var data_to_verify = document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.value+'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>.value+'/'+document.f.<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>.value;

        if (!ControllaData(data_to_verify) )
        {
          alert('Data Provvediemnto di cumulo non valida');
          document.f.<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>.focus();

          return false;
        }

       if(!document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled)
       {
        if (document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>[document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.selectedIndex].value == '-')
        {
          alert("Il Campo Ufficio che ha emesso il cumulo è obbligatorio");
          return false;
        }
       }
       if(!document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled)
       {
         if(document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE %>.value=="")
         {
          alert("Il Campo Sede Ufficio che ha emesso il cumulo è obbligatorio");
          return false;
         }
        }

        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>.value=="")
        {
          alert("Anno Procedimento SIEP è obbligatorio");
          return false;
        }

        if(document.f.<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>.value=="")
        {
          alert("Numero Procedimento SIEP è obbligatorio");
          return false;
        }

        return true;
      }


//javascript per gestire il radio e disabilitare così i campi autorità e sede
//modifica del 23-11-2006 -- Dario indicazione di Viviana
       function radio()
       {
        var altra = document.getElementById('divaltraautorita');
        var stessa = document.getElementById('divstessaautorita');
        if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked)
        {
         stessa.style.display='block';
         altra.style.display='none';

         document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE_STESSA%>.disabled=false;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE_STESSA%>.disabled=false;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled=true;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled=true;
        }
        else if(document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked)
        {
         stessa.style.display='none';
         altra.style.display='block';

         document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE_STESSA%>.disabled=true;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE_STESSA%>.disabled=true;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.disabled=false;
         document.f.<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>.disabled=false;
        }
       }
       
       function radioIni()
       {
   			var lStesso = '<%=StessoUfficio%>';
   			//alert('radioIni'+lStesso);
   		
   			if(lStesso == "SI")
   			{	
          		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked = true;
          		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked = false;
           	}
          	else if(lStesso == "NO")
          	{
         	 	document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[0].checked = false;
        		document.f.<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>[1].checked = true;
          	}

   			radio();
       }

//funzioni
      function ListaComuniUff(a_formname,a_fieldname,codTipoUfficio)
      {
         var desktop;
         desktop = window.open("<%=IWebConstants.PG_MAIN%>?<%=IWebConstants.ACTION_FIELD%>=siap.sico.ufficio.action.ActLoadListaUfficiPerTipo&formname="+a_formname+"&fieldname="+a_fieldname+"&<%=ICostantiUfficio.CAMPO_TIPO_UFFICIO%>="+codTipoUfficio , "Ricerca_Comune","toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=300,height=500");
      }


    </script>
  </head>
  <body class="corpo" onload="radioIni();">
    <table>
      <tr><td class="LBG"><a href="Javascript:window.print();"><img align="middle" src="<%=IWebConstants.IMAGES_DIR%>quickprint24.gif" alt="Stampa questa videata" border=0></a></td>
        <td class=lbg>
           <font  class="label">Funzione :&nbsp;</font>
         <font class="campo">Annotazione dati cumulo</font>
        </td>
      </tr>
    </table>
    <br>
      <jsp:include page="/jsp/files/siap/siep/fascicolo/DettaglioSoggettoSentenza.jsp"/>
    <br>
    <form method="POST" name="f" action="<%=IWebConstants.PG_MAIN%>">
    <input type="HIDDEN" name="<%=IWebConstants.ACTION_FIELD%>" value="siap.siep.archiviazione.action.ActInserisciAnnProvCumulo">
    <input type="HIDDEN" name="<%=ICostantiPosizioneGiuridica.CAMPO_COD_POSIZIONE_GIURIDICA%>" value="<%=lPosizione.getCodPosizioneGiuridica()%>">
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
      <td colspan=5 class="titolo">Dati Cumulo</td>
    </tr>
    <tr>
     <td class="l" colspan="5">
         Annotazione cumulo stesso ufficio &nbsp;<input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="0356" onclick="radio();" checked>
         &nbsp; Annotazione cumulo altro ufficio  &nbsp; <input type="radio" name="<%=ICostantiArchiviazione.CAMPO_COD_OGGETTO_DEFINIZIONE%>" value="0357" onclick="radio();">
      </td>
    </tr>
    <tr>
      <td class="l" width="30%">Data Provvedimento di cumulo <font class=ob>(*)</font></td>
      <td class="l" colspan="4">
        <input type="text" Title="Giorno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"dd"),"")%>" 
        	name="<%=ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>" maxlength="2" size="2"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Mese cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"MM"),"")%>" 
        	name="<%=ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>" maxlength="2" size="2"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillDM(value)">
        /
        <input type="text" Title="Anno cumulo" value="<%=StringUtils.toStringJSP(DateUtils.getDateToString(EsitoTrasmissione.getDataEsito(),"yyyy"),"")%>" 
        	name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>" maxlength="4" size="4"  
        	onFocus="javascript:textboxSelect(this)" onkeypress="return TicTabNumField(this,event)" onBlur="javascript:value=FillYear(value)">
      </td>
    </tr>
</table>
<div id="divaltraautorita" style="display:none; width:100%;">
<table width="100%">
    <tr>
      <td class="l" width="3%">Ufficio che ha emesso il cumulo <font class=ob>(*)</font></td>
      <td class="L" colspan="4">
        <select Title="Ufficio"  class="small" name="<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>">
         <%=autoritaCumulo%>
         </select>
      </td>
    </tr>
    <tr>
      <td class="l" width="30%">Sede <font class=ob>(*)</font></td>
     <td class="L" colspan="4">
          <input title="Sede Ufficio"  value="<%=sedeCumulo%>" type="text" name="<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>"  maxlength="35" size="35">
          <a href="Javascript:ListaComuniUff('f','<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE%>',document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>[document.f.<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE%>.selectedIndex].value);">
          <img src="/images/filefolder.gif" border=0></a>
      </td>
    </tr>
</table>
</div>
<div id="divstessaautorita" style="display:none; width:100%;">
<table width="100%">
    <tr>
      <td class="l" width="30%">Ufficio che ha emesso il cumulo
      <td class="L" colspan="4"><%=destipoufficio%></td>
      <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_COD_UFFICIO_UNIONE_STESSA%>" value="<%=codtipoufficio%>">
    </tr>
    <tr>
      <td class="l" width="30%">Sede</td>
      <td class="L" colspan="4"><%=descsede%></td>
      <input type="HIDDEN" name="<%=ICostantiFascicoloSiep.CAMPO_SEDE_UFFICIO_UNIONE_STESSA%>" value="<%=descsede%>">
    </tr>
</table>
</div>
<table width="100%">
    <tr>
      <td class="l" width="30%">Numero Procedimento SIEP <font class=ob>(*)</font></td>
      <td class="l" colspan="4">
         <input Title="Anno Unione" value="<%=StringUtils.toStringJSP(EsitoTrasmissione.getChiaveAnno(),"" )%>" name="<%=ICostantiFascicoloSiep.CAMPO_ANNO_FASCICOLO_UNIONE%>" 
         	type="text" size="4" maxlength="4">
         /
         <input Title="Numero Unione" value="<%=StringUtils.toStringJSP(EsitoTrasmissione.getChiaveProgr(),"" )%>" name="<%=ICostantiFascicoloSiep.CAMPO_NUM_FASCICOLO_UNIONE%>" 
         	type="text" size="6" maxlength="6"></font></td>
      </td>
    </tr>
    <tr>
       <td rowspan=2 class="l" width="30%">Note</td>
       <td rowspan=2 class="L" colspan="4">
          <textarea title="Note" name="<%=ICostantiArchiviazione.CAMPO_NOTE%>"  cols=70 rows=4></textarea>
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

    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","gt=1900");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","lt=2099");

//data emissione
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_GIORNO_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_MESE_UNIONE%>","numeric");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","maxlen=4","La lunghezza massima per l'Anno provvedimento cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","minlen=4","La lunghezza minima per l'Anno provvedimento cumulo è di 4 caratteri");
    frmvalidator.addValidation("<%= ICostantiFascicoloSiep.CAMPO_ANNO_UNIONE%>","numeric");

    frmvalidator.setAddnlValidationFunction("Verify");

  </script>
</body>
</html>