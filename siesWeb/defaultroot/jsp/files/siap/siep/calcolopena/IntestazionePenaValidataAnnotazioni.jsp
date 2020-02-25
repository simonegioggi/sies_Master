<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="f3b.util.StringUtils" %>
<%@ page import="f3b.util.DateUtils" %>


<jsp:useBean id="PenaComplessivaSentenza" scope="request" class="siap.siep.penacomplessiva.model.PenaComplessivaModel" />
<jsp:useBean id="PenaResidua"             scope="request" class="siap.siep.penaresidua.model.PenaResiduaModel" />

<%

//==============================================================================
// Form importata dalle jsp di Scelta delle stampe dei benefici e dei computi:
// - VediCalcoloPenaValidataRichiestaGE.jsp
// - VediCalcoloPenaValidataAnnotazioniComputo.jsp
// - VediCalcoloPenaValidataAnnotazioni.jsp
//
// Questa jsp visualizza il dettagli della pena (decorrenza/scadenza e quantum)
//==============================================================================
  boolean lErgastolo = false;

  // se la Pena Complessiva è un ergastolo o ergastolo con isolamento
  if(  PenaComplessivaSentenza.getCodTipoPenaDetentiva() != null
    && PenaComplessivaSentenza.getCodTipoPenaDetentiva() != ""
    && ( PenaComplessivaSentenza.getCodTipoPenaDetentiva().equals("03") || PenaComplessivaSentenza.getCodTipoPenaDetentiva().equals("04") )
    )
  {
    lErgastolo = true;
  }

  String datainiziopena       = StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizio(),"dd-MM-yyyy"));
  String datafinereclusione   = StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFineReclusione(),"dd-MM-yyyy"));
  String datainizioarresto    = StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizioArresto(),"dd-MM-yyyy"));
  String datafinepenapresunta = StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFinePresunta(),"dd-MM-yyyy"));
  String datafinepenavalidata = StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataFine(),"dd-MM-yyyy"));

  if (   !lErgastolo
      && (   !datainiziopena.equals("")
          || !datafinereclusione.equals("")
          || !datainizioarresto.equals("")
          || !datafinepenapresunta.equals("")
          || !datafinepenavalidata.equals("") 
         )
     )
  {
%>
  <table width="50%">
    <tr>
      <td colspan=8 class="titolo">Dati della Pena</td>
    </tr>
    <tr>
<%
    if (!datainiziopena.equals(""))
    {
%>
      <td class="l" nowrap>Data Decorrenza Pena:</td>
      <td class="l" nowrap>
        <font class="campo">
          <%=StringUtils.toStringJSP(datainiziopena)%>
        </font>
      </td>
<%
    }

    if (!datafinereclusione.equals(""))
    {
%>
      <td class="l" nowrap>Data Fine Reclusione: </font></td>
      <td class="l" nowrap>
        <font class="campo">
          <%=StringUtils.toStringJSP(datafinereclusione)%>
        </font>
      </td>
<%
    }
%>
		</tr>
		
		<tr>
<%
      if (!datainizioarresto.equals(""))
      {
%>
        <td class="l" nowrap><font class="label">Data Inizio Arresto : </font></td>
        <td class="l" nowrap>
          <font class="campo">
            <%=StringUtils.toStringJSP(datainizioarresto)%>
          </font>
        </td>
<%
      }

      if (!datafinepenapresunta.equals(""))
      {
%>
        <td class="l" nowrap><font class="label">Data Fine Pena Automatica : </font></td>
        <td class="l" nowrap>
          <font class="campo">
            <%=StringUtils.toStringJSP(datafinepenapresunta)%>
          </font>
        </td>
<%
      }
%>
   </tr>
   
   
   <tr>
<%
    if(!datafinepenavalidata.equals(""))
    {
%>
      <td class="l" nowrap><font class="label">Data Fine Pena Manuale : </font></td>
      <td class="l" nowrap>
        <font class="campo">
          <%=StringUtils.toStringJSP(datafinepenavalidata)%>
        </font>
      </td>
<%
    }
%>
    </tr>
  </table>
<%
  } // end if date presenti e non ergastolo
%>


  <table width="50%">
<%
//==============================================================================
// se la Pena Complessiva è un ergastolo o ergastolo con isolamento
//==============================================================================
  if( lErgastolo )
  {
%>
      <tr>
        <td colspan=3 class="Titolonocap">Pena complessiva</td>
      </tr>
      <tr>
        <td class="l" nowrap>
          <font class="campo">
            <%=StringUtils.toStringJSP(PenaComplessivaSentenza.getDescrTipoPenaDetentiva())%>
          </font>
        </td>
        <td class="l" nowrap>Data Inizio : <font class="campo"><%=StringUtils.toStringJSP(DateUtils.getDateToString(PenaResidua.getDataInizio(),"dd-MM-yyyy"))%> </font></td>
        <td class="l" nowrap>Data Fine : <font class="campo">MAI</font></td>
      </tr>
<%
  }
  else
  {
%>
    <tr>
      <td class="Titolo" colspan=9><font class="label">Pena Residua</font></td>
    </tr>
    <tr>
      <td class="l" nowrap><font class="label">Reclusione : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniReclusione(), "0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiReclusione(), "0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniReclusione(), "0")%></font></td>
      <td class="l"><font class="label">Multa : </font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoMulta())%> &euro;</font></td>
    </tr>
    <tr>
      <td class="l" nowrap><font class="label">Arresto : </font></td>
      <td class="l"><font class="label">Anni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumAnniArresto(), "0")%></font></td>
      <td class="l"><font class="label">Mesi</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumMesiArresto(), "0")%></font></td>
      <td class="l"><font class="label">Giorni</font></td>
      <td class="r"><font class="campo"><%=StringUtils.toStringJSP(PenaResidua.getNumGiorniArresto(), "0")%></font></td>
      <td class="l"><font class="label">Ammenda : </font></td>
      <td class="r"><font class="campo"><%=StringUtils.toEuroFormat(PenaResidua.getImportoAmmenda())%> &euro;</font></td>
    </tr>
<%
  }
%>
  </table>