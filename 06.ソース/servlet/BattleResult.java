package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * 試合結果取得サーブレット
 *
 * @author arahari
 * @version 1.0
 */

public class BattleResult extends HttpServlet {
    /**
    *
    * 試合結果取得メソッド
    *
    * データベースへの検索処理
    */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try{
        	BattleResultDBAccess brd = new BattleResultDBAccess();
            List<BattleResultBean> list = brd.BattleResultBean();
            // listをrequestにセット
            request.setAttribute("list", list);
        }catch(Exception e){
            e.printStackTrace();
        }

        /** 次のJSPに遷移 */
        RequestDispatcher rd = request.getRequestDispatcher("/JSP/BattleResult.jsp");
        rd.forward(request, response);
    }
}