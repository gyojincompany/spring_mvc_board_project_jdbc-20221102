package com.gyojincompany.mvcboard.command;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.gyojincompany.mvcboard.dao.BoardDao;
import com.gyojincompany.mvcboard.dto.BoardDto;

public class BContentViewCommand implements BCommand {

	@Override
	public void excute(Model model) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = model.asMap();
		HttpServletRequest request = (HttpServletRequest) map.get("request");
		//model 객체에서 request 객체를 빼냄
		
		String bid = request.getParameter("bid");
		
		BoardDao dao = new BoardDao();
		BoardDto dto = dao.content_view(bid);
		
		model.addAttribute("content", dto);
	}

}
