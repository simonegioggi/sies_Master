<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

    <%@page import="f3b.util.StringUtils"%>
<%@page import="siap.siep.fascicolo.action.ICostantiFascicoloSiep"%>
<table width="100%">
      <tr>
        <td class="l">Note</td>
        <td class="l" colspan="3">
        <%-- 20170913: [SG] aggiunto spazio tra nome e cognome --%>
        	<textarea title='Note' name='<%=ICostantiFascicoloSiep.CAMPO_NOTE%>'  cols='70' rows='3' readonly='readonly'><%=StringUtils.toStringJSP(lPosGiu.getLuogoProvaAffidamento())%>&nbsp;<%=StringUtils.toStringJSP(lPosGiu.getLuogoLavoroSemiliberta())%></textarea>
        </td>
      </tr>

      <tr>
        <td colspan=4>
          <br>
          <input class="bottone" type="submit" name="INSERISCI"  onClick="javascript:return Verify();" value="Conferma">
        </td>
      </tr>
    </table>