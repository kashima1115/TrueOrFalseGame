<%@ page language="java" contentType="text/html; charset=Windows-31J"
    pageEncoding="Windows-31J"%>
<%@ page import="java.util.*" %>
<%@ page import="servlet.TrueOrFalseBean" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=Windows-31J">
    <title>���~�Q�[���̎�������</title>
  </head>
  <body>
    <h1>���~�Q�[���̎������ʈꗗ�\</h1>

    <form action="../servlet/servlet.BattleDetail" method="post">
    <% // �������ʂ̃f�[�^���X�g���擾
       List<TrueOrFalseBean> list = (List<TrueOrFalseBean>)request.getAttribute("list");
       // �擾�ł��Ȃ������ꍇ�A���b�Z�[�W�o��
       // �擾�ł����ꍇ�A�f�[�^���o��
       if(list.isEmpty()) {
           out.print("�Y���̃f�[�^�͂���܂���B");
       }else{   %>

    <table border="1">
      <tr>
      	<th>�I��</th>
        <th>����ID</th>
        <th>��U�ΐ�ҏ��</th>
        <th>���s</th>
        <th>��U�ΐ�ҏ��</th>
        <th>���s</th>
        <th>������</th>

      </tr>

<!-- i����̍ۂɐ�U���W�b�N�����Ăяo�� -->
<!-- �����̂Ƃ��Ɍ�U�̃��W�b�N�����Ăяo�����Ƃň�̕\�ɂ܂Ƃ߂� -->

<% int i = 0; %>

      <% for(TrueOrFalseBean bn : list) { %>

<% i++; %>

<% if(i % 2 != 0){ %>

      <tr>
		<td><input type="radio" name="battleId" value="<%=bn.getBattle_id() %>" <%if(i == 1){ %>checked<% } %>></td>
        <td><%=bn.getBattle_id() %></td>

        <td><%=bn.getPFlogic_name() %>�@<%=bn.getPFlogic_writer() %>�@<%=bn.getPFlogic_ver() %></td>
        <td><%=bn.getResult() %></td>

         <% } %>

         <% if(i % 2 == 0){ %>

        <td><%=bn.getLogic_name() %>�@<%=bn.getLogic_writer() %>�@<%=bn.getLogic_ver() %></td>
        <td><%=bn.getResult() %></td>

        <td><%=bn.getYear() %>�N<%=bn.getMonth() %>��<%=bn.getDay() %>��</td>
      </tr>

 <% } %>

	  <% } %>
    </table>

    <% } %>

    <input type="submit"  value="�I�����������̏ڍׂ�����" />

    </form>
    <form action="../servlet/servlet.BattleResult" method="get">

    <input type="submit"  value="�X�V" />

    </form>
  </body>
</html>